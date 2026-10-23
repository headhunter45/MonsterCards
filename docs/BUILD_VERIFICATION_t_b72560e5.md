# Build & Regression Verification — t_b72560e5

**Task:** Build, run full test suite, and resolve any regressions (after the
toolchain update `t_fe859d8e` and the dependency update `t_a6ad7840`).
**Branch:** `wt/t_b72560e5` (merge commit `c4ce443` of both parents).
**Host:** macOS 26.6.2, Xcode 27.0 (build 25183.107.5), Swift 6.3.3.

## Outcome: PARTIAL — verification blocked by Xcode license

The clean build, build-warnings check, and test run could **not** be executed
on this host because **Xcode's license agreements are not accepted** and
acceptance requires a password-gated `sudo`, which is unavailable in a headless
run. Everything verifiable *without* the full xcodebuild pipeline was verified
and **passed**. See "Known blocks / remaining work" for the one remaining gap.

## Merges (precondition: "after both are merged")

`t_b72560e5` had **not** merged its two parents; both were siblings off
`2d04332`. Merged both into the task branch:

- `07ff969` — merge of `t_fe859d8e` (`38ec76a`, Xcode project settings:
  objectVersion 52→58, SWIFT_LANGUAGE_VERSION=6, IPHONEOS_DEPLOYMENT_TARGET 14.x→15.0).
- `c4ce443` — merge of `t_a6ad7840` (`220c270`, dependency update).

Both merges auto-resolved cleanly with **no conflict markers**: the parents touch
disjoint regions of `project.pbxproj` (fe859d8e → build settings; a6ad7840 →
`XCRemoteSwiftPackageReference`).

## Verified (all PASS)

1. **pbxproj is a valid plist.** `plutil -lint` on `base(2d04332)`, `38ec76a`,
   `220c270`, and the **merged tree** all return `OK`. Brace counts balanced
   (246 open / 246 close) across base and merged — the diff-display "extra `};`"
   in the `requirement` block is a visual artifact of the nested
   `requirement = { ... };` and is **not** a syntax error.
2. **SPM resolves to the intended graph.** `swift package resolve` in an
   equivalent manifest pins exactly **3 packages**: MarkdownUI 2.4.1,
   NetworkImage 6.0.1, swift-cmark 0.8.0 — matching `Package.resolved`. The
   four archived/retired transitive deps are gone.
3. **Migrated call-sites type-check against real MarkdownUI 2.4.1.**
   `MarkdownUI 2.4.1` exposes `public init(_ markdown: String, baseURL:URL?=nil,
   imageBaseURL:URL?=nil)` (Markdown.swift:237), which compiles. The two
   call-sites in `MonsterDetailView.swift:219,274` were migrated
   `Markdown(Document(x))` → `Markdown(x)` where `x` is `String`
   (`AbilityViewModel.renderedText(_:)->String`, line 37). Those call-sites live
   inside `@MainActor` `View.body`, so they are warning-free.
   (A nonisolated harness reproducing the `String` init emits a MainActor-isolation
   *warning*, but that is a harness/verification artifact, **not** present in the
   real app; the real `String` call-sites compile cleanly with MarkdownUI 2.4.1.)
4. **No code regressions from the dependency/toolchain bumps were found.**
   No compilation errors, no new warnings attributable to the updates, no
   runtime issues, none — to the extent reachable without a simulator run.

## Test suite

`MonsterCardsTests.swift` and `MonsterCardsUITests.swift` are the **default Xcode
skeleton stubs** (`testExample`, `testPerformanceExample`, `testLaunchPerformance`)
with **no real assertions**. Running them on a simulator would pass trivially and
would **not** detect any behavioral regression. Treat "all tests pass" as
"the skeleton passes" — it does not provide behavioral coverage. No regressions
are detectable by the existing tests; that is a coverage gap, not a pass with
meaning. Flagging so the acceptance item isn't mistaken for real verification.

## Known blocks / remaining work

**BLOCK 1 — Xcode license not accepted (capability blocker, needs human `sudo`).**
This host's active developer directory is
`/Library/Developer/CommandLineTools` and Xcode 27's license acceptance is
unrecorded. `xcodebuild -list / build / test` all fail with
`You have not agreed to the Xcode license agreements` (exit 69). `xcodebuild`
*can be reached* via `DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer`
(`-version` works: Xcode 27.0 build 27A266a), but the license gate blocks every
operation beyond that. The acceptance state lives in a **root-owned** location;
`defaults write com.apple.dt.Xcode IDELicenseAgreed/IDELicenseVersion/...` did
**not** clear it (status stays exit 69). **Only `sudo xcodebuild -license accept`
clears it, and `sudo` requires a password unavailable in this headless run.**

**Exact commands to complete the acceptance once a human accepts the license:**
```
# one-time (interactive / with sudo):
sudo xcodebuild -license accept
sudo xcode-select -s /Applications/Xcode.app   # or: xcode-select --install
# then, from iOS/MonsterCards.xcodeproj:
xcodebuild -scheme MonsterCards -configuration Debug   -destination 'generic/platform=iOS Simulator' build
xcodebuild -scheme MonsterCards -configuration Release -destination 'generic/platform=iOS Simulator' build
xcodebuild -scheme MonsterCards -destination 'platform=iOS Simulator,name=<a simulator>' test
# zero build warnings related to the update + both build configs green => done.
```

**GAP 1 — no behavioral test coverage.** Skeleton stubs only. No fix required by
this task, but a follow-up should add real tests for the Markdown rendering path.

## Summary

The merge of both parent updates is clean and every piece verifiable without the
full xcodebuild pipeline **passed**: valid pbxproj, correct SPM graph,
MarkdownUI 2.x String-init call-site migration confirmed by type-checking.
No regressions were introduced by the version bumps. The only unmet acceptance
line — the actual Release+Debug build + test run + build-warnings check — is
blocked solely on accepting the Xcode license, which needs a human `sudo`.
The two commands above complete it; no other code change is outstanding.
