# TextFlow — Android App

TextFlow is a floating AI writing assistant for Android. A small bubble floats
over any app (WhatsApp, Gmail, browser…). Select text, tap the bubble, type
`@` + a command (`@fix`, `@summ`, `@translate`, …), and an LLM rewrites the
selection in place — with manual Undo/Redo.

This repository currently contains the **MVP foundation**: the full project
skeleton, design system, the canonical 13-command data model, the command
autocomplete card, and the Available Commands + Settings screen. The
AccessibilityService implementation, overlay bubble, LLM proxy client, and
sign-in are later tasks (see [Roadmap](#roadmap)).

The authoritative product spec lives at `build-spec.md` (one directory above
this repo in the shared workspace) — **prompts and design tokens in code must
never drift from it**.

## Project layout

```
app/src/main/java/com/textflow/app/
├── MainActivity.kt                  # entry point (dev home screen)
├── data/TextFlowCommand.kt          # the 13 commands + verbatim system prompts
├── service/TextFlowAccessibilityService.kt  # stub (manifest scaffolding only)
└── ui/
    ├── theme/  Color.kt · Shape.kt · Type.kt · Theme.kt   # spec §5 tokens
    ├── components/  CommandAutocompleteCard.kt · SettingsRow.kt
    └── screens/  HomeScreen.kt      # Available Commands grid + Settings
app/src/main/res/xml/accessibility_service_config.xml  # service event config
scripts/verify_prompts.py            # checks prompts are byte-for-byte the spec
```

## Tech stack / versions

| Thing | Choice |
|---|---|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose + Material3 (BOM 2024.12.01) |
| Gradle | 8.11.1 (Kotlin DSL, version catalog in `gradle/libs.versions.toml`) |
| AGP | 8.7.3 |
| compileSdk / targetSdk | 35 (Android 15) |
| minSdk | 26 (Android 8.0) — see justification below |
| JVM | 17 |

**minSdk 26 rationale:** the accessibility APIs TextFlow depends on
(`TYPE_VIEW_TEXT_SELECTION_CHANGED`, retrieving window content) are stable on
26+, and 26+ lets us ship adaptive-only launcher icons (no legacy PNG sets).
26 covers ~97% of active devices at the time of writing.

## Design tokens (spec §5)

| Token | Value | Where |
|---|---|---|
| Background | `#0E0D12` | `ui/theme/Color.kt` → `TextFlowBackground` |
| Cards | `#1C1B22` | `TextFlowCard` |
| Accent (command names) | `#8B7CF6` | `TextFlowAccent` |
| Accent (filled toggles/buttons) | `#8A7CFB` | `TextFlowAccentFill` |
| Secondary text | `#9A98A5` | `TextFlowSecondaryText` |
| Primary text | white `#FFFFFF` | `TextFlowOnPrimary` |
| Card radius | 20dp | `ui/theme/Shape.kt` → `CardShape` |
| Button/input radius | 28dp | `InputShape` |
| Typography | bold titles/commands, regular descriptions | `ui/theme/Type.kt` |

All screens consume these via `TextFlowTheme` / `MaterialTheme` — never
hard-code a color.

## The 13 commands

`data/TextFlowCommand.kt` holds all 13 commands with their **verbatim** system
prompts: `typi`, `fix`, `summ`, `polite`, `casual`, `expand`, `translate`,
`bullet`, `improve`, `rephrase`, `emoji`, `formal`, `funny`.

The spec table is canonical (the older 11-command mockup is wrong — `@formal`
and `@funny` must stay). To guard against drift, run:

```bash
python3 scripts/verify_prompts.py            # spec = ../build-spec.md
python3 scripts/verify_prompts.py /path/to/build-spec.md
```

It exits 0 only when all 13 prompts are byte-for-byte identical to the spec
table. Unit tests (`CommandsTest`) also pin the command list and filtering.

## Building

### Android Studio (recommended)
1. Open this directory as a project (Android Studio Ladybug or newer).
2. Let Gradle sync (it downloads the wrapper distribution + dependencies).
3. Run `app` on a device/emulator (API 26+).

### Command line
Requires JDK 17 and the Android SDK (set `ANDROID_HOME` or `local.properties`):

```bash
# Linux / macOS
./gradlew :app:assembleDebug          # debug APK → app/build/outputs/apk/debug/
./gradlew :app:testDebugUnitTest      # unit tests

# Windows
gradlew.bat :app:assembleDebug
```

### Headless CI
```bash
./gradlew :app:assembleDebug :app:testDebugUnitTest --no-daemon
```
Licenses must be accepted once on the CI machine:
`sdkmanager --licenses` (see
[developer.android.com](https://developer.android.com/studio#command-line-tools-only)).

## What's already wired in the manifest

- `android.permission.SYSTEM_ALERT_WINDOW` — overlay bubble above other apps.
- `android.permission.INTERNET` — for the server-side LLM proxy (never stores
  API keys in the client).
- `<service>` declaration for the accessibility service bound to
  `res/xml/accessibility_service_config.xml` — listens for
  `typeWindowContentChanged` / `typeViewTextSelectionChanged`, retrieves window
  content. The Kotlin class is a stub; the implementation is a later task.
- `MainActivity` offers the overlay permission intent
  (`Settings.ACTION_MANAGE_OVERLAY_PERMISSION`) via `OverlayPermission`.

## Roadmap

1. **AccessibilityService implementation** — detect selection, read text,
   replace it with the command result (`service/TextFlowAccessibilityService.kt`).
2. **Floating bubble + WindowManager overlay** — host
   `CommandAutocompleteCard` as a real overlay window.
3. **LLM proxy client** — POST selected text + system prompt to the server-side
   proxy; no keys in the app.
4. **Undo/Redo** — per-session original/AI-result state, honoring the
   "Hide Undo/Redo" setting.
5. **Settings persistence** — DataStore/SharedPreferences for the two toggles
   (spec §3.2), synced per-account once auth lands.
6. **Sign-in** — email/password + Google OAuth (Firebase/Supabase), plus
   "Forgot Password?" / "Sign Up" flows (spec §3.3).
