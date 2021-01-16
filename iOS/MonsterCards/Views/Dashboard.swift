//
//  Dashboard.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI

struct Dashboard: View {
    var body: some View {
        Text("Dashboard")
    }
}

struct Dashboard_Previews: PreviewProvider {
    static var previews: some View {
        Dashboard().environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
    }
}
