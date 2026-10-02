//
//  ZipExtractor.swift
//  MonsterCards
//
//  Created by Antigravity on 10/2/26.
//

import Foundation
import zlib

public enum ZipExtractor {
    private static func decompressDeflate(compressedData: Data, uncompressedSize: Int) -> Data? {
        var stream = z_stream()
        let status = inflateInit2_(&stream, -MAX_WBITS, ZLIB_VERSION, Int32(MemoryLayout<z_stream>.size))
        guard status == Z_OK else { return nil }
        defer { inflateEnd(&stream) }

        var decompressedData = Data(count: uncompressedSize)
        let decompressedCount = decompressedData.count

        let actualCount: Int? = compressedData.withUnsafeBytes { rawIn in
            decompressedData.withUnsafeMutableBytes { rawOut in
                guard let inBase = rawIn.bindMemory(to: Bytef.self).baseAddress,
                      let outBase = rawOut.bindMemory(to: Bytef.self).baseAddress else {
                    return nil
                }
                stream.next_in = UnsafeMutablePointer<Bytef>(mutating: inBase)
                stream.avail_in = uInt(compressedData.count)
                stream.next_out = outBase
                stream.avail_out = uInt(decompressedCount)

                let inflateStatus = inflate(&stream, Z_FINISH)
                if inflateStatus == Z_STREAM_END || inflateStatus == Z_OK {
                    return decompressedCount - Int(stream.avail_out)
                }
                return nil
            }
        }

        guard let count = actualCount else { return nil }
        return decompressedData.prefix(count)
    }

    public static func parseZip(data: Data) -> [String: Data] {
        var results: [String: Data] = [:]
        guard data.count > 22 else { return results }

        // Find End of Central Directory (EOCD) signature: 0x06054b50
        var eocdOffset = -1
        let maxSearch = min(data.count, 65535 + 22)
        let endIdx = data.count - 22
        let startIdx = data.count - maxSearch

        data.withUnsafeBytes { raw in
            guard let ptr = raw.bindMemory(to: UInt8.self).baseAddress else { return }
            for i in stride(from: endIdx, through: startIdx, by: -1) {
                if ptr[i] == 0x50 && ptr[i+1] == 0x4b && ptr[i+2] == 0x05 && ptr[i+3] == 0x06 {
                    eocdOffset = i
                    break
                }
            }
        }

        guard eocdOffset >= 0 else { return results }

        let cdEntriesCount = Int(data.subdata(in: (eocdOffset + 10)..<(eocdOffset + 12)).withUnsafeBytes { $0.load(as: UInt16.self) })
        let cdOffset = Int(data.subdata(in: (eocdOffset + 16)..<(eocdOffset + 20)).withUnsafeBytes { $0.load(as: UInt32.self) })

        var curOffset = cdOffset
        for _ in 0..<cdEntriesCount {
            guard curOffset + 46 <= data.count else { break }
            let sig = data.subdata(in: curOffset..<(curOffset + 4)).withUnsafeBytes { $0.load(as: UInt32.self) }
            guard sig == 0x02014b50 else { break }

            let method = data.subdata(in: (curOffset + 10)..<(curOffset + 12)).withUnsafeBytes { $0.load(as: UInt16.self) }
            let compSize = Int(data.subdata(in: (curOffset + 20)..<(curOffset + 24)).withUnsafeBytes { $0.load(as: UInt32.self) })
            let uncompSize = Int(data.subdata(in: (curOffset + 24)..<(curOffset + 28)).withUnsafeBytes { $0.load(as: UInt32.self) })
            let nameLen = Int(data.subdata(in: (curOffset + 28)..<(curOffset + 30)).withUnsafeBytes { $0.load(as: UInt16.self) })
            let extraLen = Int(data.subdata(in: (curOffset + 30)..<(curOffset + 32)).withUnsafeBytes { $0.load(as: UInt16.self) })
            let commentLen = Int(data.subdata(in: (curOffset + 32)..<(curOffset + 34)).withUnsafeBytes { $0.load(as: UInt16.self) })
            let localOffset = Int(data.subdata(in: (curOffset + 42)..<(curOffset + 46)).withUnsafeBytes { $0.load(as: UInt32.self) })

            let nameData = data.subdata(in: (curOffset + 46)..<(curOffset + 46 + nameLen))
            let filename = String(data: nameData, encoding: .utf8) ?? ""

            curOffset += 46 + nameLen + extraLen + commentLen

            guard !filename.hasSuffix("/") && uncompSize > 0 else { continue }

            guard localOffset + 30 <= data.count else { continue }
            let localNameLen = Int(data.subdata(in: (localOffset + 26)..<(localOffset + 28)).withUnsafeBytes { $0.load(as: UInt16.self) })
            let localExtraLen = Int(data.subdata(in: (localOffset + 28)..<(localOffset + 30)).withUnsafeBytes { $0.load(as: UInt16.self) })
            let dataStart = localOffset + 30 + localNameLen + localExtraLen
            let dataEnd = dataStart + compSize
            guard dataEnd <= data.count else { continue }

            let fileData = data.subdata(in: dataStart..<dataEnd)
            if method == 0 {
                results[filename] = fileData
            } else if method == 8 {
                if let decompressed = decompressDeflate(compressedData: fileData, uncompressedSize: uncompSize) {
                    results[filename] = decompressed
                }
            }
        }

        return results
    }
}
