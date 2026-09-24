//
//  PreviewViewController.swift
//  MonsterPreview
//
//  Created by Tom Hicks on 4/7/21.
//  Last modified by Tom Hicks on 9/23/26.
//

import UIKit
import QuickLook
import SwiftUI

@MainActor
final class PreviewViewController: UIViewController, @preconcurrency QLPreviewingController {
    
    // MARK: - QLPreviewingController
    
    func preparePreviewOfFile(at url: URL, completionHandler handler: @escaping (Error?) -> Void) {
        Task { @MainActor in
            let document = MonsterDocument(fileURL: url)
            
            // Asynchronously open the document on the MainActor
            let isOpen = await document.open()
            
            guard isOpen else {
                handler(CocoaError(.fileReadCorruptFile))
                return
            }
            
            // Extract the model and display
            let dto = document.monsterDTO ?? MonsterDTO()
            self.presentMonsterViewController(for: dto)
            
            // Notify QuickLook that preview preparation succeeded
            handler(nil)
        }
    }
    
    // MARK: - Private Presentation
    
    private func presentMonsterViewController(for dto: MonsterDTO) {
        let monsterViewModel = MonsterImportHelper.import5ESBMonster(dto)
        let hostingController = UIHostingController(rootView: MonsterDetailView(viewModel: monsterViewModel))
        
        // Add as child view controller
        addChild(hostingController)
        view.addSubview(hostingController.view)
        
        // Configure layout constraints
        let subview = hostingController.view!
        subview.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            subview.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            subview.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            subview.topAnchor.constraint(equalTo: view.topAnchor),
            subview.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        
        hostingController.didMove(toParent: self)
    }
}
