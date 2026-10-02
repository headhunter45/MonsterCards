//
//  ReferenceMonsterRepositoryTests.swift
//  MonsterCardsTests
//

import XCTest
import CoreData
@testable import MonsterCards

class ReferenceMonsterRepositoryTests: XCTestCase {
    
    var persistenceController: PersistenceController!
    var context: NSManagedObjectContext!
    var repository: ReferenceMonsterRepository!
    
    override func setUp() {
        super.setUp()
        persistenceController = PersistenceController(inMemory: true)
        context = persistenceController.container.viewContext
        repository = ReferenceMonsterRepository.shared
    }
    
    override func tearDown() {
        context = nil
        persistenceController = nil
        repository = nil
        super.tearDown()
    }
    
    func testBatchInsertAndQueryReferenceMonsters() throws {
        let vm1 = MonsterViewModel()
        vm1.name = "Goblin Archer"
        vm1.size = "Small"
        vm1.type = "Humanoid"
        vm1.gameSystem = .dnd5e
        vm1.sourceLabel = "5e SRD"
        vm1.challengeRating = .oneQuarter
        
        let vm2 = MonsterViewModel()
        vm2.name = "Adult Red Dragon"
        vm2.size = "Huge"
        vm2.type = "Dragon"
        vm2.gameSystem = .dnd5e
        vm2.sourceLabel = "5e SRD"
        vm2.challengeRating = .seventeen

        let vm3 = MonsterViewModel()
        vm3.name = "Pathfinder Owlbear"
        vm3.size = "Large"
        vm3.type = "Beast"
        vm3.gameSystem = .pf2e
        vm3.sourceLabel = "Bestiary"
        vm3.challengeRating = .four

        let dict1 = ReferenceMonsterRepository.dictionaryFromViewModel(
            vm1,
            id: "m1",
            sourceId: "open5e_srd",
            sourceLabel: "5e SRD",
            gameSystem: .dnd5e,
            bookSource: "SRD 5.1"
        )
        let dict2 = ReferenceMonsterRepository.dictionaryFromViewModel(
            vm2,
            id: "m2",
            sourceId: "open5e_srd",
            sourceLabel: "5e SRD",
            gameSystem: .dnd5e,
            bookSource: "SRD 5.1"
        )
        let dict3 = ReferenceMonsterRepository.dictionaryFromViewModel(
            vm3,
            id: "m3",
            sourceId: "pf2e_bestiary",
            sourceLabel: "Bestiary",
            gameSystem: .pf2e,
            bookSource: "Bestiary 1"
        )

        try repository.insertBatch(monsters: [dict1, dict2, dict3], in: context)
        try context.save()

        XCTAssertEqual(repository.countForSource(sourceId: "open5e_srd", in: context), 2)
        XCTAssertEqual(repository.countForSource(sourceId: "pf2e_bestiary", in: context), 1)
        XCTAssertEqual(repository.totalReferenceCount(in: context), 3)

        // Verify isolated from user Monster entity count
        let userMonsterCount = (try? context.count(for: NSFetchRequest<Monster>(entityName: "Monster"))) ?? 0
        XCTAssertEqual(userMonsterCount, 0, "Reference monsters must not appear in user Monster entity tables")

        // Search by name
        let dragonResults = repository.searchReferenceMonsters(query: "Dragon", in: context)
        XCTAssertEqual(dragonResults.count, 1)
        XCTAssertEqual(dragonResults.first?.name, "Adult Red Dragon")

        // Search by game system filter
        let pf2eResults = repository.searchReferenceMonsters(query: "", gameSystem: .pf2e, in: context)
        XCTAssertEqual(pf2eResults.count, 1)
        XCTAssertEqual(pf2eResults.first?.name, "Pathfinder Owlbear")

        // Convert to MonsterViewModel
        let convertedVM = pf2eResults.first!.toViewModel()
        XCTAssertEqual(convertedVM.name, "Pathfinder Owlbear")
        XCTAssertEqual(convertedVM.gameSystem, .pf2e)
        XCTAssertEqual(convertedVM.sourceLabel, "Bestiary")
    }
    
    func testAtomicReplaceSource() throws {
        let vm = MonsterViewModel()
        vm.name = "Initial Monster"
        let dict1 = ReferenceMonsterRepository.dictionaryFromViewModel(
            vm,
            id: "init1",
            sourceId: "src1",
            sourceLabel: "Src1",
            gameSystem: .dnd5e
        )
        try repository.insertBatch(monsters: [dict1], in: context)
        XCTAssertEqual(repository.countForSource(sourceId: "src1", in: context), 1)

        let vmNew = MonsterViewModel()
        vmNew.name = "Replacement Monster"
        let dictNew = ReferenceMonsterRepository.dictionaryFromViewModel(
            vmNew,
            id: "rep1",
            sourceId: "src1",
            sourceLabel: "Src1",
            gameSystem: .dnd5e
        )
        try repository.replaceSource(sourceId: "src1", monsters: [dictNew], in: context)

        XCTAssertEqual(repository.countForSource(sourceId: "src1", in: context), 1)
        let results = repository.searchReferenceMonsters(query: "Replacement", in: context)
        XCTAssertEqual(results.count, 1)
        XCTAssertEqual(results.first?.name, "Replacement Monster")
    }
}
