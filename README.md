# Focus Island (포커스 아일랜드) 🏝️

**Focus Island** is a beautiful, highly polished, zero-cost-infra visual routine tracker and Android home screen widget application. Inspired by MUJI's minimalist design philosophy, the app blends relaxing visual aesthetics with lightweight, local-first architectures to bring peace, mindfulness, and consistency to your daily habits.

---

## ✨ Key Features & Design Details

### 1. Scenic Visual Island Growth 🌴 (Jetpack Compose Canvas)
*   **Aesthetic Growth States**: As you complete your daily routines, your island visually sprouts and flourishes in real-time right on your dashboard card:
    *   **0% Completed**: A calm, quiet island sand bank floating in the sea.
    *   **1% - 29% Completed**: A fresh tiny sprout starts to bud and grow leaves.
    *   **30% - 59% Completed**: A majestic tropical palm tree swings with gentle wind-sway animations.
    *   **60% - 99% Completed**: Beautiful tropical pink and yellow blooming flowers decorate the sand.
    *   **100% Completed**: A starry sky shines above, twinkling, completed with a beautiful custom rainbow.
*   **MUJI Color Principles**: Handpicked soft sage greens, warm sandy pastel choices, smooth negative spacing, and modern display typography built on Material Design 3 guidelines.

### 2. Beautiful Android Home Screen Widget (Glance-style Native Provider)
*   Provides a clean 4x2 interactive home-screen routine capsule.
*   Dynamically binds the overall completion progress and lists up to 3 active routine checklist indicators.
*   Triggers automatic standard Broadcaster updates from the host database.

### 3. Smart Local Offline Architecture ($0 Server Fees)
*   **Room Database Repository**: High-performance structured local SQLite storage—no cloud database overhead or setup fees.
*   **Auto Midnight Refresh**: Integrated dynamic check routines. When the calendar crosses into a new day, completion checklists safely reset to zero automatically so you can start fresh.
*   **Dynamic State Flow**: Built on modern Android architecture patterns with `ViewModel`, `StateFlow`, and reactive repository patterns.

### 4. Interactive Ad Revenue Integration & Premium Switch
*   **Banner Ads Sandbox**: Always-visible simulated Google AdMob banner aligned nicely with UI layouts.
*   **Interstitial Milestone Ads**: Triggers a beautiful immersive overlay dialog notifying achievements when matching 100% completions.
*   **Simulated "Premium" Upgrade**: Users can toggle their premium subscription instantly with top bar controls. Upgrading to Premium completely disables ads instantly and increases aesthetic visual rewards.

### 5. Multi-Language Adaptability (Localization)
*   Fully localized string resources for English and Korean.
*   Adheres strictly to the dynamic system configurations of Android.

---

## 🛠️ Project Structure & Architecture

```text
/app/src/main/
├── AndroidManifest.xml                        // Configures widgets, activity permissions
├── java/com/example/
│   ├── MainActivity.kt                        // Core entry point
│   ├── data/                                  // Offline SQLite Room Database
│   │   ├── Routine.kt                         // Routine entity model
│   │   ├── RoutineDao.kt                      // DAOs for data queries
│   │   ├── RoutineDatabase.kt                 // Room SQLite DB helper
│   │   └── RoutineRepository.kt               // Unified data access repository
│   ├── ui/                                    // UI & MVVM State Presentation layers
│   │   ├── RoutineListScreen.kt               // Gorgeous MUJI style Compose Screen
│   │   ├── RoutineViewModel.kt                // Focus Island MVVM State Machine
│   │   └── theme/                             // Typography, Color palettes & Themes
│   └── widget/                                // Widget Provider (Native Home Widget)
│       └── RoutineWidgetProvider.kt
└── res/
    ├── layout/widget_layout.xml               // 4x2 Home screen widget configuration
    ├── xml/widget_info.xml                    // App widget provider metadata
    └── values/strings.xml                     // Fully localized multilingual string values
```

---

## 🚀 Getting Started

1. **Running on Emulator**: Since this app runs in **Google AI Studio Build**, you can test and check the beautiful visual island growth directly via the integrated Streaming Device Preview on the right sidebar.
2. **Adding Routines**: Tap the standard floating action button (`+`) to open the minimalist selection modal dialog, type your item (e.g., "Read 20 Mins"), choose a pastel label, and save.
3. **Checking Routines**: Click on check circles to watch your custom island react, blossom, and trigger delightful haptic feedback!
