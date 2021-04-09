//
//  PreviewViewController.swift
//  MonsterPreview
//
//  Created by Tom Hicks on 4/7/21.
//

import UIKit
import QuickLook
import SwiftUI

class PreviewViewController: UIViewController, QLPreviewingController {
        
    func preparePreviewOfFile(at url: URL, completionHandler handler: @escaping (Error?) -> Void) {
        
        let document = MonsterDocument(fileURL: url)
        document.open(completionHandler: {[weak self](_) in
                        self?.presentMonsterViewController(for: document)
                        handler(nil)
        })
    }
    
    func presentMonsterViewController(for document: MonsterDocument) {
        let monsterViewModel = MonsterImportHelper.import5ESBMonster(document.monsterDTO ?? MonsterDTO())
        let monsterViewController = UIHostingController(rootView: MonsterDetailView(viewModel: monsterViewModel))
        monsterViewController.loadViewIfNeeded()
        monsterViewController.view.layoutIfNeeded()
        addChild(monsterViewController)
        view.addSubview(monsterViewController.view)
        monsterViewController.didMove(toParent: self)
        
        if let monsterView = monsterViewController.view {
            monsterView.translatesAutoresizingMaskIntoConstraints = false
            NSLayoutConstraint.activate([
                monsterView.leftAnchor.constraint(equalTo: view.leftAnchor),
                monsterView.rightAnchor.constraint(equalTo: view.rightAnchor),
                monsterView.topAnchor.constraint(equalTo: view.topAnchor),
                monsterView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            ])
        }
    }

}
