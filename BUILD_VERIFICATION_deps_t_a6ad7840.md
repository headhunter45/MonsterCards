# Build verification — third-party dependency update (t_a6ad7840)

Re-run requested by operator ("rebuild and run the project now") after the
operator unblocked Xcode and bumped the iOS deployment target.

## Environment
- Host: macOS 26.6.2
- Xcode 27.0 (build 27A266a), selected via `xcode-select -p` →
  `/Applications/Xcode.app/Contents/Developer` (license now accepted — the prior
  human-gated blocker is cleared).
- Swift 6 (swiftlang-6.4.0.34.1).
- Branch: `wt/t_a6ad7840`, HEAD `5e066bd` "Updates ios targets to 17.0"
  (includes dependency commit `220c270` + operator's deployment-target bump).
- Project: `iOS/MonsterCards.xcodeproj`.

## Result
`** BUILD SUCCEEDED **` (xcodebuild exit 0), Debug configuration,
destination `platform=iOS Simulator,name=iPhone 17` (an available runtime on
this host; iOS 17.0 and 26.5 simulators are present). Build product:
`Debug-iphonesimulator/MonsterCards.app` + `MonsterPreview.appex` embed.

Command:
```
xcodebuild -project iOS/MonsterCards.xcodeproj -scheme MonsterCards \
  -configuration Debug -destination 'platform=iOS Simulator,name=iPhone 17' \
  -derivedDataPath /tmp/mc-der build CODE_SIGNING_ALLOWED=NO
```
Full log captured during the run (transient): `/tmp/mc-build2.log`.

## Resolved dependency graph (SPM)
SPM resolved cleanly to exactly 3 packages, no conflicts:
- `swift-markdown-ui`  `gonzalezreal/MarkdownUI`  @ 2.4.1
- `networkimage`       `gonzalezreal/NetworkImage` @ 6.0.1
- `cmark-gfm`          `swiftlang/swift-cmark`     @ 0.8.0

`Package.resolved` (v2) pins these three. No CocoaPods/Carthage manifest exists;
SPM is the only package manager. Resolving dropped 4 archived/retired
transitves: AttributedText, SwiftCommonMark, combine-schedulers,
xctest-dynamic-overlay.

## Warnings / errors
- 0 compile errors.
- 21 build warnings, all pre-existing and unrelated to the dependency update:
  `NSManagedObject`/`NSManagedObjectContext` `@_implementationOnly` / "CoreData
  was not imported by this file" warnings in the app's own source
  (`Views/Search.swift`, `Views/Library.swift`, `Views/ImportMonster.swift`) —
  noted as "this is an error in the Swift 6 language mode"; these are latent
  Swift-6-mode issues in first-party code, not introduced by any dependency.
  (The project currently compiles in Swift 5 mode; these will surface if the
  module is moved to `SWIFT_LANGUAGE_VERSION=6`.) One informational warning:
  "Metadata extraction skipped, no AppIntents.framework dependency found."

## Run (honoring the operator's "rebuild and run" request)
- Booted the `iPhone 17` (iOS 17.0) simulator.
- `xcrun simctl install` the built `MonsterCards.app` → exit 0.
- `xcrun simctl launch com.majinnaibu.monstercards.MonsterCards` → exit 0,
  launched process PID 66988; verified alive ~4 s after launch with no crash
   report in `DiagnosticReports`. App runs.
- No crash logs generated.

## Release configuration
Also built `-configuration Release` (same destination) → `** BUILD SUCCEEDED **`
(exit 0, 0 errors). Both Debug and Release compile cleanly with the updated
dependencies.

## Acceptance
- Package.resolved reflects target versions — yes (MarkdownUI 2.4.1,
  NetworkImage 6.0.1, swift-cmark 0.8.0).
- No unresolved version conflicts — yes (SPM resolved to exactly the 3 pins,
  exit 0).
- Project compiles after dependency changes — yes (Debug + Release
  `** BUILD SUCCEEDED **`).
- Project runs — yes (installed + launched on iPhone 17 simulator, stayed
  alive, no crash log).
- No Xcode project-level settings were changed by this task (operator's
  deployment-target bump `5e066bd` is separate; test/UITest targets still at
  14.0, app target at 17.6).

## Caveat (carried from root synthesis t_e9801924)
This MarkdownUI-based lineage is an **interim**. The operator later directed a
move from `gonzalezreal/swift-markdown-ui` (now in maintenance mode) to
`gonzalezreal/textual`, tracked by senior-eng task `t_28affc1e` "iOS: migrate
MarkdownUI → gonzalezreal/textual on a clean develop base." This task's result
proves the current tree compiles cleanly with the resolved dependencies; it is
not the final target library set. Final library choice is owned by the pm at
`t_e9801924` / `t_28affc1e`.
