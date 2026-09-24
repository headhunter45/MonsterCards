//
//  SearchResultItem.swift
//  MonsterCards
//
//  Ported from Android SearchResultItem.java.
//  Disambiguation type for search-result items across collections and dashboards.

import Foundation

/// Discriminant for a SearchResultItem's concrete entity type.
public enum SearchResultItemType: String, Codable, CaseIterable {
    case monster
    case collection
}

/// Lightweight non-persisted wrapper around either a Monster or a Collection,
/// with enough metadata to distinguish cross-collection search results.
public struct SearchResultItem: Identifiable, Hashable {

    public let id: String         // objectID-based unique key.
    public let type: SearchResultItemType
    public let monster: Monster?
    public let collection: Collection?

    // MARK: - Constructors mirroring Android

    /// Wraps a Monster — sets `type = .monster`.
    public init(monster: Monster) {
        self.id = monster.objectID.uriRepresentation().lastPathComponent
        self.type = .monster
        self.monster = monster
        self.collection = nil
    }

    /// Wraps a Collection — sets `type = .collection`.
    public init(collection: Collection) {
        self.id = collection.objectID.uriRepresentation().lastPathComponent
        self.type = .collection
        self.monster = nil
        self.collection = collection
    }

    // MARK: - Identifiable / Hashable

    public static func == (lhs: SearchResultItem, rhs: SearchResultItem) -> Bool {
        lhs.id == rhs.id && lhs.type == rhs.type
    }

    public func hash(into hasher: inout Hasher) {
        hasher.combine(id)
        hasher.combine(type)
    }
}
