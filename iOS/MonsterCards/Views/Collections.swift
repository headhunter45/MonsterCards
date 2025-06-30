//
//  Collections.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI

struct Collections: View {
//    @State var allCollections: [MonsterCollection] = []
    var body: some View {
        Text("Collections")
    }
}

struct Collections_Previews: PreviewProvider {
    static var previews: some View {
        Collections().environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
    }
}
