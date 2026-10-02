//
//  Open5eApiWrapper.swift
//  MonsterCards
//
//  Created by Antigravity on 10/1/26.
//

import Foundation

struct Open5ePageResult: Sendable {
    let nextUrl: String?
    let rawResults: [String]
}

struct Open5eApiWrapper: Sendable {
    private static let defaultBaseUrl = "https://api.open5e.com/v2/creatures/"

    static func fetchPage(urlStr: String? = nil) async throws -> Open5ePageResult {
        let endpoint = urlStr ?? defaultBaseUrl
        guard let url = URL(string: endpoint) else {
            throw ImporterError.invalidFormat("Invalid URL: \(endpoint)")
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        request.setValue("MonsterCards-iOS", forHTTPHeaderField: "User-Agent")
        request.timeoutInterval = 15

        let (data, response) = try await URLSession.shared.data(for: request)

        guard let httpResponse = response as? HTTPURLResponse, (200...299).contains(httpResponse.statusCode) else {
            let code = (response as? HTTPURLResponse)?.statusCode ?? -1
            throw ImporterError.invalidFormat("Open5e API error HTTP \(code)")
        }

        guard let root = try? JSONSerialization.jsonObject(with: data) as? [String: Any] else {
            throw ImporterError.invalidFormat("Invalid JSON response from Open5e API")
        }

        let nextUrl = root["next"] as? String
        var rawResults: [String] = []

        if let results = root["results"] as? [[String: Any]] {
            for item in results {
                if let itemData = try? JSONSerialization.data(withJSONObject: item),
                   let jsonStr = String(data: itemData, encoding: .utf8) {
                    rawResults.append(jsonStr)
                }
            }
        }

        return Open5ePageResult(nextUrl: nextUrl, rawResults: rawResults)
    }
}
