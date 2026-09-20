#!/usr/bin/env python3
"""Migrate MonsterCards.xcodeproj for Xcode 27 / Swift 6 / modern iOS SDK."""
import re

path = "iOS/MonsterCards.xcodeproj/project.pbxproj"

with open(path, 'r') as f:
    content = f.read()

# --- Changes to apply ---

# 1. objectVersion from 52 → 58 (Xcode 14+)
content = re.sub(r'objectVersion = 52;', 'objectVersion = 58;', content)

# 2. LastUpgradeCheck from 1200 → 2700 (Xcode 27)
content = re.sub(r'LastUpgradeCheck = 1240;', 'LastUpgradeCheck = 2700;', content)

# 3. LastSwiftUpdateCheck from 1240 → 2700
content = re.sub(r'LastSwiftUpdateCheck = 1240;', 'LastSwiftUpdateCheck = 2700;', content)

# 4. IPHONEOS_DEPLOYMENT_TARGET: 14.4 → 15.0 (Preview appex)
# 5. IPHONEOS_DEPLOYMENT_TARGET: 14.0 → 15.0 (all others - minimum supported by current dependencies & Xcode SDK)
for old_val in ['14.4', '14.0']:
    content = re.sub(
        r'IPHONEOS_DEPLOYMENT_TARGET = ' + re.escape(old_val) + ';',
        'IPHONEOS_DEPLOYMENT_TARGET = 15.0;',
        content
    )

# 6. SWIFT_VERSION: 5.0 → 6.0 across all targets
content = re.sub(
    r'SWIFT_VERSION = 5\.0;',
    'SWIFT_LANGUAGE_VERSION = 6;',
    content
)

# Verify counts
v15 = len(re.findall(r'IPHONEOS_DEPLOYMENT_TARGET = 15\.0;', content))
v6 = content.count('SWIFT_LANGUAGE_VERSION = 6')
v52_old = content.count('objectVersion = 52')
v14_old = content.count('IPHONEOS_DEPLOYMENT_TARGET = 14.')

print(f"IPHONEOS_DEPLOYMENT_TARGET → 15.0 applied: {v15} times (expected 8)")
print(f"SWIFT_VERSION → SWIFT_LANGUAGE_VERSION = 6 applied: {v6} times (expected 8)")
print(f"Remaining objectVersion=52: {v52_old}")
print(f"Remaining IPHONEOS_DEPLOYMENT_TARGET = 14.x: {v14_old}")

with open(path, 'w') as f:
    f.write(content)

print("\nDone. All changes written.")
