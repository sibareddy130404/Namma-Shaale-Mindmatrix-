<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Material_3-757575?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material 3"/>
</p>

# 🏫 Namma Shaale — School Asset Manager

> **Namma Shaale** (ನಮ್ಮ ಶಾಲೆ — "Our School" in Kannada) is a modern Android application built for schools and educational institutions to **register, track, and manage physical assets** such as furniture, electronics, lab equipment, and more. It also features an integrated **issue tracking system** and **report generation** to streamline maintenance workflows.

---

## ✨ Features

| Feature | Description |
|---|---|
| 📊 **Dashboard** | Real-time overview with stat cards showing total, working, needs-repair, and broken asset counts. Animated entry with quick-action shortcuts. |
| 📝 **Asset Registration** | Register new assets with name, serial number, category, condition, and an optional photo captured via the device camera. |
| 📋 **Asset List & Search** | Browse all registered assets with live search by name or serial number and filter chips (All · Working · Needs Repair · Broken). |
| 🔍 **Asset Detail View** | View full asset details including condition, timestamps, and linked issues. Update condition or delete assets with confirmation dialogs. |
| 🐛 **Issue Tracking** | Log issues against any asset, view all issues with tabs (All · Open · Resolved), mark issues as resolved, or remove them. |
| 📸 **Camera Integration** | Capture asset photos using CameraX with a full-screen viewfinder and circular shutter button. |
| 📄 **Report Generation** | Auto-generated plain-text summary reports with asset overview, category breakdown, and recent unresolved issues — shareable via Android's share sheet. |
| 🌗 **Dynamic Theming** | Supports Material You dynamic colors (Android 12+), plus custom light/dark themes with a Deep Teal & Warm Amber color palette. |

---

## 🏗️ Architecture

The project follows **MVVM (Model–View–ViewModel)** architecture with a clean separation of concerns:

```
UI (Jetpack Compose)  →  ViewModel  →  Repository  →  DAO  →  Room (SQLite)
        ↑                                                          |
        └──────────── Flow auto-updates ←──────────────────────────┘
```

### 📁 Project Structure

```
com.nammashale.assetmanager/
│
├── MainActivity.kt                  # Entry point with bottom navigation bar
├── NammaShaaleApp.kt                # Application class (database & repository singletons)
│
├── data/
│   ├── model/
│   │   ├── Asset.kt                 # Asset entity (Room table: assets)
│   │   └── Issue.kt                 # Issue entity (Room table: issues, FK → assets)
│   ├── dao/
│   │   ├── AssetDao.kt              # Data Access Object for assets
│   │   └── IssueDao.kt              # Data Access Object for issues
│   ├── database/
│   │   └── AppDatabase.kt           # Room database singleton (v1)
│   └── repository/
│       └── AssetRepository.kt       # Single source of truth for all data operations
│
├── ui/
│   ├── components/
│   │   ├── AssetCard.kt             # Reusable asset card component
│   │   └── ConditionChip.kt         # Color-coded condition badge (🟢🟡🔴)
│   ├── navigation/
│   │   └── NavGraph.kt              # Navigation routes & graph definition
│   ├── screens/
│   │   ├── DashboardScreen.kt       # Home dashboard with stats & quick actions
│   │   ├── AssetRegistrationScreen.kt # Asset registration form
│   │   ├── AssetListScreen.kt       # Searchable & filterable asset list
│   │   ├── AssetDetailScreen.kt     # Detailed asset view with issue management
│   │   ├── CameraScreen.kt          # CameraX photo capture screen
│   │   ├── IssueListScreen.kt       # All issues with tab filtering
│   │   └── ReportScreen.kt          # Generated text report with share option
│   ├── theme/
│   │   ├── Color.kt                 # Custom color palette (light & dark)
│   │   ├── Theme.kt                 # Material 3 theme with dynamic color support
│   │   └── Type.kt                  # Custom typography scale
│   └── viewmodel/
│       ├── AssetViewModel.kt        # Business logic for asset operations
│       └── IssueViewModel.kt        # Business logic for issue operations
│
└── util/
    └── ReportGenerator.kt           # Plain-text report builder utility
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Architecture** | MVVM + Repository Pattern |
| **Local Database** | Room (SQLite) |
| **Async** | Kotlin Coroutines + Flow |
| **Navigation** | Jetpack Navigation Compose |
| **Camera** | CameraX |
| **Build System** | Gradle (Kotlin DSL) with Version Catalog |
| **Annotation Processing** | KSP (Kotlin Symbol Processing) |
| **Min SDK** | 26 (Android 8.0 Oreo) |
| **Target SDK** | 35 (Android 15) |
| **Java Compatibility** | JDK 17 |

---

## 📦 Dependencies

```kotlin
// Core
androidx.core:core-ktx

// Lifecycle & ViewModel
androidx.lifecycle:lifecycle-runtime-ktx
androidx.lifecycle:lifecycle-viewmodel-compose
androidx.lifecycle:lifecycle-runtime-compose

// Jetpack Compose (BOM-managed)
androidx.activity:activity-compose
androidx.compose.ui:ui
androidx.compose.ui:ui-graphics
androidx.compose.ui:ui-tooling-preview
androidx.compose.material3:material3
androidx.compose.material:material-icons-extended

// Navigation
androidx.navigation:navigation-compose

// Room Database
androidx.room:room-runtime
androidx.room:room-ktx
androidx.room:room-compiler (KSP)

// CameraX
androidx.camera:camera-core
androidx.camera:camera-camera2
androidx.camera:camera-lifecycle
androidx.camera:camera-view

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android
```

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 17** or higher
- An Android device or emulator running **API 26+**

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/sibareddy130404/Namma-Shaale-Mindmatrix-.git
   ```

2. **Open in Android Studio:**
   - Launch Android Studio
   - Select **File → Open** and navigate to the cloned directory

3. **Sync Gradle:**
   - Android Studio will prompt you to sync Gradle — click **Sync Now**

4. **Run the app:**
   - Connect a physical device or start an emulator
   - Click the **▶ Run** button or press `Shift + F10`

### Permissions

The app requests the following permission at runtime:

| Permission | Purpose |
|---|---|
| `CAMERA` | Capturing photos of assets during registration |

> **Note:** Camera hardware is declared as `required="false"`, so the app installs on devices without a camera — the photo feature will simply be unavailable.

---

## 📱 Screens Overview

### 1. Dashboard
The home screen displays animated stat cards with gradient backgrounds showing asset counts by condition. Quick-action buttons provide one-tap navigation to asset list, issues, and reports.

### 2. Asset Registration
A scrollable form with validated text fields for asset name and serial number, dropdown menus for category (Furniture, Electronics, Sports Equipment, Books, Lab Equipment, Others) and condition (Working, Needs Repair, Broken), plus a tap-to-capture photo card.

### 3. Asset List
A `LazyColumn` of all assets with live search and `FilterChip` condition filters. Each item shows the asset avatar (2-letter initials), name, serial number, category, and a color-coded condition badge.

### 4. Asset Detail
Full asset information with header card, editable condition (via dropdown), registration/update timestamps, and a linked issues list. Includes delete (with confirmation dialog) and "Log Issue" FAB.

### 5. Issue Tracker
Tabbed view (All · Open · Resolved) of all reported issues with resolve and delete actions. Issues are color-coded — open issues appear in error container colors, resolved in surface variants.

### 6. Camera
Full-screen CameraX viewfinder with a styled circular capture button. The captured photo URI is passed back to the registration screen via `SavedStateHandle`.

### 7. Reports
An auto-generated monospaced text report summarizing total assets, condition breakdown, category distribution, and recent unresolved issues. Shareable via Android's `ACTION_SEND` intent.

---

## 🎨 Design System

### Color Palette

| Role | Light | Dark |
|---|---|---|
| **Primary** | Deep Teal `#006B5E` | `#5CDBC6` |
| **Secondary** | Warm Amber `#8B5E00` | `#FFB E48` |
| **Tertiary** | Soft Purple `#6750A4` | `#CFBCFF` |
| **Working** | Green `#2E7D32` | — |
| **Needs Repair** | Amber `#F9A825` | — |
| **Broken** | Red `#C62828` | — |

### Dynamic Colors
On Android 12+ devices, the app uses **Material You** dynamic colors derived from the user's wallpaper. On older devices, the custom teal/amber palette is applied.

---

## 📊 Data Model

### Asset Table (`assets`)

| Column | Type | Description |
|---|---|---|
| `id` | `Long` (PK, auto) | Unique identifier |
| `name` | `String` | Asset display name |
| `serialNo` | `String` | Unique serial/inventory number |
| `category` | `String` | Asset category |
| `condition` | `String` | Working / Needs Repair / Broken |
| `photoPath` | `String?` | File path to captured photo |
| `createdAt` | `Long` | Registration timestamp (epoch ms) |
| `updatedAt` | `Long` | Last update timestamp (epoch ms) |

### Issue Table (`issues`)

| Column | Type | Description |
|---|---|---|
| `id` | `Long` (PK, auto) | Unique identifier |
| `assetId` | `Long` (FK → assets) | Linked asset |
| `description` | `String` | Issue description |
| `date` | `Long` | Report date (epoch ms) |
| `resolved` | `Boolean` | Resolution status |

> Issues cascade-delete when their parent asset is removed.

---

## 🤝 Contributing

Contributions are welcome! Here's how to get started:

1. **Fork** the repository
2. Create a **feature branch** (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. Open a **Pull Request**

---

## 👥 Team — MindMatrix

This project was developed by **Team MindMatrix** as part of the **Namma Shaale** initiative.

---

## 📄 License

This project is open source and available for educational purposes.

---

<p align="center">
  Made with ❤️ by <strong>Team MindMatrix</strong>
</p>
