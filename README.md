<div align="center">

<!-- ═══════════════════ HERO BANNER ═══════════════════ -->

# 🏫 Namma Shaale

### *ನಮ್ಮ ಶಾಲೆ — "Our School"*

**A Modern Android Asset Management System for Schools & Institutions**

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Design-Material_3-757575?style=for-the-badge&logo=materialdesign&logoColor=white)](https://m3.material.io)

[![API](https://img.shields.io/badge/Min_SDK-26_(Oreo)-green?style=flat-square)](https://developer.android.com/about/versions/oreo)
[![Target](https://img.shields.io/badge/Target_SDK-35_(Android_15)-blue?style=flat-square)](https://developer.android.com/about/versions/15)
[![Version](https://img.shields.io/badge/Version-1.0-orange?style=flat-square)]()
[![License](https://img.shields.io/badge/License-Open_Source-brightgreen?style=flat-square)]()

---

*Register, track, and manage physical assets in schools — from furniture and electronics to lab equipment — with built-in issue tracking, camera capture, and shareable reports.*

[📲 Features](#-features) · [🏗️ Architecture](#%EF%B8%8F-architecture) · [🚀 Getting Started](#-getting-started) · [📊 Database Schema](#-database-schema) · [🤝 Contributing](#-contributing)

</div>

---

## 📋 Table of Contents

- [Features](#-features)
- [App Screens](#-app-screens)
- [Architecture](#%EF%B8%8F-architecture)
- [Project Structure](#-project-structure)
- [Tech Stack](#%EF%B8%8F-tech-stack)
- [Getting Started](#-getting-started)
- [Database Schema](#-database-schema)
- [Design System](#-design-system)
- [Version Catalog](#-version-catalog)
- [Contributing](#-contributing)
- [Team](#-team)

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 📊 Smart Dashboard
Real-time stat cards with **gradient backgrounds** and **slide-in animations** showing live counts of total, working, needs-repair, and broken assets. One-tap quick actions for instant navigation.

### 📝 Asset Registration
Complete registration form with **validated inputs**, dropdown selectors for 6 asset categories, condition picker, and integrated **camera capture** for asset photos.

### 🔍 Search & Filter
**Live search** across asset names and serial numbers with **Material 3 FilterChips** for instant condition-based filtering (All · Working · Needs Repair · Broken).

### 📸 CameraX Integration
Full-screen **CameraX viewfinder** with a styled circular shutter button. Captured photos are saved locally and linked to assets via URI.

</td>
<td width="50%">

### 🐛 Issue Tracker
Log maintenance issues against any asset. **Tabbed interface** (All · Open · Resolved) with color-coded cards, resolve/delete actions, and unresolved count badges.

### 📄 Report Generator
Auto-generated **plain-text summary reports** with asset overview, category breakdown, and recent unresolved issues — shareable via Android's native **share sheet**.

### 🎨 Dynamic Theming
Supports **Material You** dynamic colors on Android 12+ devices. Falls back to a handcrafted **Deep Teal & Warm Amber** palette with full dark mode support.

### ⚡ Reactive Data
**Kotlin Flow** + **Room** ensure the UI always reflects the latest data. Changes propagate automatically from SQLite → DAO → Repository → ViewModel → Compose.

</td>
</tr>
</table>

---

## 📱 App Screens

| # | Screen | Description |
|:-:|--------|-------------|
| 1 | **🏠 Dashboard** | Home screen with animated gradient stat cards (Total Assets, Working, Needs Repair, Broken) and quick-action buttons for asset list, issues, and reports. Uses `AnimatedVisibility` with `fadeIn` + `slideInVertically` transitions. |
| 2 | **📝 Asset Registration** | Scrollable form with `OutlinedTextField` for name & serial number (with validation), `ExposedDropdownMenuBox` for category (Furniture, Electronics, Sports Equipment, Books, Lab Equipment, Others) and condition (Working, Needs Repair, Broken), plus a tap-to-capture photo card. |
| 3 | **📋 Asset List** | `LazyColumn` of all registered assets with a search bar and `FilterChip` row. Each item displays a 2-letter avatar, asset name, serial number, category, and a color-coded condition badge. |
| 4 | **🔍 Asset Detail** | Full asset info with header card, editable condition (dropdown), registration & update timestamps, and linked issues list. Includes asset deletion with confirmation dialog and a "Log Issue" floating action button. |
| 5 | **📸 Camera** | Full-screen CameraX viewfinder (`PreviewView`) with a translucent circular capture button. Photo URI is passed back to registration via `SavedStateHandle`. |
| 6 | **🐛 Issue List** | All issues across assets with `TabRow` filtering (All · Open · Resolved). Open issues shown in `errorContainer` colors, resolved in muted `surfaceVariant`. Resolve and delete actions on each card. |
| 7 | **📄 Reports** | Auto-generated monospaced text report with asset summary, condition breakdown, category distribution, and up to 5 recent unresolved issues. Share button triggers `ACTION_SEND` intent. |

---

## 🏗️ Architecture

The app follows **MVVM (Model–View–ViewModel)** with the **Repository Pattern**, ensuring clean separation of concerns and testability.

```
┌─────────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER                       │
│  ┌─────────────┐    ┌──────────────┐    ┌───────────────────┐   │
│  │   Compose    │◄──│  ViewModel   │◄──►│    Navigation     │   │
│  │   Screens    │    │ (StateFlow)  │    │    (NavGraph)     │   │
│  └──────┬───────┘    └──────┬───────┘    └───────────────────┘   │
│         │                   │                                    │
│         │  observes         │  calls                             │
│         ▼                   ▼                                    │
├─────────────────────────────────────────────────────────────────┤
│                          DATA LAYER                             │
│         ┌───────────────────────────────┐                       │
│         │         Repository            │  ◄─ Single Source     │
│         │    (AssetRepository.kt)       │     of Truth          │
│         └──────────┬────────────────────┘                       │
│                    │                                             │
│         ┌─────────┴─────────┐                                   │
│         ▼                   ▼                                   │
│  ┌─────────────┐    ┌─────────────┐                             │
│  │  AssetDao   │    │  IssueDao   │   ◄─ Data Access Objects   │
│  └──────┬──────┘    └──────┬──────┘                             │
│         │                  │                                     │
│         └────────┬─────────┘                                     │
│                  ▼                                               │
│         ┌─────────────────┐                                     │
│         │   AppDatabase   │   ◄─ Room (SQLite)                  │
│         │  (Singleton)    │                                     │
│         └─────────────────┘                                     │
└─────────────────────────────────────────────────────────────────┘
```

### 🔄 Data Flow

```
User Action ──► Compose UI ──► ViewModel ──► Repository ──► DAO ──► Room (SQLite)
                   ▲                                                     │
                   │              Flow auto-emits on DB change           │
                   └─────────────────────────────────────────────────────┘
```

---

## 📂 Project Structure

```
📦 namma-shaale/
├── 📄 build.gradle.kts                    # Root build config
├── 📄 settings.gradle.kts                 # Project settings (name: NammaShaale)
├── 📄 gradle.properties                   # Gradle JVM & AndroidX settings
├── 📄 .gitignore                          # Git exclusions
│
├── 📁 gradle/
│   ├── 📄 libs.versions.toml             # 📌 Version Catalog (centralized deps)
│   └── 📁 wrapper/
│       └── 📄 gradle-wrapper.properties   # Gradle wrapper config
│
└── 📁 app/
    ├── 📄 build.gradle.kts                # App-level build config (SDK, deps)
    ├── 📄 proguard-rules.pro              # ProGuard/R8 rules
    │
    └── 📁 src/main/
        ├── 📄 AndroidManifest.xml         # App manifest (permissions, activities)
        │
        ├── 📁 res/
        │   ├── 📁 drawable/               # Vector launcher icon assets
        │   ├── 📁 mipmap-anydpi-v26/      # Adaptive icon config
        │   └── 📁 values/
        │       ├── 📄 strings.xml         # App name string resource
        │       └── 📄 themes.xml          # Base XML theme (NoActionBar)
        │
        └── 📁 java/com/nammashale/assetmanager/
            │
            ├── 📄 MainActivity.kt              # 🚀 Entry point + Bottom navigation
            ├── 📄 NammaShaaleApp.kt             # 📱 Application class (singletons)
            │
            ├── 📁 data/                         # ━━━ DATA LAYER ━━━
            │   ├── 📁 model/
            │   │   ├── 📄 Asset.kt              #   Room entity: assets table
            │   │   └── 📄 Issue.kt              #   Room entity: issues table (FK→assets)
            │   ├── 📁 dao/
            │   │   ├── 📄 AssetDao.kt           #   CRUD + search/filter queries
            │   │   └── 📄 IssueDao.kt           #   CRUD + unresolved queries
            │   ├── 📁 database/
            │   │   └── 📄 AppDatabase.kt        #   Room DB singleton (v1)
            │   └── 📁 repository/
            │       └── 📄 AssetRepository.kt    #   Single source of truth
            │
            ├── 📁 ui/                           # ━━━ PRESENTATION LAYER ━━━
            │   ├── 📁 components/
            │   │   ├── 📄 AssetCard.kt          #   Reusable asset list card
            │   │   └── 📄 ConditionChip.kt      #   Color-coded status badge
            │   ├── 📁 navigation/
            │   │   └── 📄 NavGraph.kt           #   Routes + NavHost setup
            │   ├── 📁 screens/
            │   │   ├── 📄 DashboardScreen.kt    #   📊 Home dashboard
            │   │   ├── 📄 AssetRegistrationScreen.kt  #   📝 Register new asset
            │   │   ├── 📄 AssetListScreen.kt    #   📋 Browse & search assets
            │   │   ├── 📄 AssetDetailScreen.kt  #   🔍 View & manage asset
            │   │   ├── 📄 CameraScreen.kt       #   📸 CameraX capture
            │   │   ├── 📄 IssueListScreen.kt    #   🐛 Issue tracker
            │   │   └── 📄 ReportScreen.kt       #   📄 Generated reports
            │   ├── 📁 theme/
            │   │   ├── 📄 Color.kt              #   🎨 Light + Dark palettes
            │   │   ├── 📄 Theme.kt              #   Material 3 + Dynamic colors
            │   │   └── 📄 Type.kt               #   Custom typography scale
            │   └── 📁 viewmodel/
            │       ├── 📄 AssetViewModel.kt     #   Asset business logic
            │       └── 📄 IssueViewModel.kt     #   Issue business logic
            │
            └── 📁 util/                         # ━━━ UTILITIES ━━━
                └── 📄 ReportGenerator.kt        #   Plain-text report builder
```

> **Total: 30 source files** across data, UI, and utility layers.

---

## 🛠️ Tech Stack

<table>
<tr>
<td>

### 🔤 Language & Build
| Component | Technology |
|---|---|
| Language | **Kotlin 2.1.0** |
| Build System | **Gradle 8.7.3** (Kotlin DSL) |
| Dependency Mgmt | **Version Catalog** (`libs.versions.toml`) |
| Annotation Proc. | **KSP 2.1.0-1.0.29** |
| Java Target | **JDK 17** |

</td>
<td>

### 📱 Android & UI
| Component | Technology |
|---|---|
| Min SDK | **26** (Android 8.0 Oreo) |
| Target SDK | **35** (Android 15) |
| UI Framework | **Jetpack Compose** |
| Design System | **Material 3** (Material You) |
| Icons | **Material Icons Extended** |

</td>
</tr>
<tr>
<td>

### 💾 Data & Storage
| Component | Technology |
|---|---|
| Local Database | **Room 2.6.1** (SQLite) |
| Async | **Kotlin Coroutines 1.9.0** |
| Reactive Streams | **Kotlin Flow** |
| State Management | **StateFlow + SharedFlow** |

</td>
<td>

### 🧭 Navigation & Camera
| Component | Technology |
|---|---|
| Navigation | **Navigation Compose 2.8.5** |
| Camera | **CameraX 1.4.1** |
| Lifecycle | **Lifecycle Runtime 2.8.7** |
| Activity | **Activity Compose 1.9.3** |

</td>
</tr>
</table>

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Version |
|---|---|
| Android Studio | **Hedgehog (2023.1.1)** or newer |
| JDK | **17** or higher |
| Android Device/Emulator | **API 26+** (Android 8.0 Oreo) |
| Gradle | Wrapper included (no separate install needed) |

### 📥 Installation

```bash
# 1️⃣ Clone the repository
git clone https://github.com/sibareddy130404/Namma-Shaale-Mindmatrix-.git

# 2️⃣ Navigate into the project
cd Namma-Shaale-Mindmatrix-
```

### ▶️ Running the App

1. **Open** the project in Android Studio
2. Wait for **Gradle Sync** to complete (automatic on first open)
3. Connect a **physical device** or start an **emulator** (API 26+)
4. Click **▶ Run** (or press `Shift + F10`)

### 🔐 Permissions

| Permission | Required | Purpose |
|---|:---:|---|
| `CAMERA` | Runtime | Capturing photos of assets during registration |

> 📝 **Note:** Camera hardware is declared as `android:required="false"` in the manifest. The app installs on devices without cameras — the photo capture feature will simply be unavailable.

---

## 📊 Database Schema

The app uses **Room** (SQLite) with 2 tables linked by a foreign key:

```
┌─────────────────────────────────┐       ┌─────────────────────────────────┐
│          📦 assets              │       │          🐛 issues              │
├─────────────────────────────────┤       ├─────────────────────────────────┤
│ 🔑 id         LONG   (PK,auto) │──┐    │ 🔑 id          LONG  (PK,auto) │
│    name       STRING            │  │    │ 🔗 assetId     LONG  (FK)──────│──┘
│    serialNo   STRING            │  │    │    description  STRING          │
│    category   STRING            │  └───►│    date         LONG            │
│    condition  STRING            │       │    resolved     BOOLEAN         │
│    photoPath  STRING?           │       └─────────────────────────────────┘
│    createdAt  LONG              │
│    updatedAt  LONG              │        CASCADE DELETE: When an asset is
└─────────────────────────────────┘        deleted, all its issues are auto-
                                           matically removed.
```

### Asset Fields

| Column | Type | Default | Description |
|---|---|---|---|
| `id` | `Long` | Auto-generated | Primary key |
| `name` | `String` | — | Display name (e.g., "Wooden Chair", "Projector") |
| `serialNo` | `String` | — | Unique serial/inventory number |
| `category` | `String` | — | One of: Furniture, Electronics, Sports Equipment, Books, Lab Equipment, Others |
| `condition` | `String` | `"Working"` | One of: Working, Needs Repair, Broken |
| `photoPath` | `String?` | `null` | File path to captured photo |
| `createdAt` | `Long` | `System.currentTimeMillis()` | Registration timestamp (epoch ms) |
| `updatedAt` | `Long` | `System.currentTimeMillis()` | Last update timestamp (epoch ms) |

### Issue Fields

| Column | Type | Default | Description |
|---|---|---|---|
| `id` | `Long` | Auto-generated | Primary key |
| `assetId` | `Long` | — | Foreign key → `assets.id` (cascade delete) |
| `description` | `String` | — | Issue description text |
| `date` | `Long` | `System.currentTimeMillis()` | Report date (epoch ms) |
| `resolved` | `Boolean` | `false` | Resolution status |

---

## 🎨 Design System

### Color Palette

<table>
<tr>
<td>

#### ☀️ Light Theme
| Role | Color | Hex |
|---|---|---|
| Primary | 🟢 Deep Teal | `#006B5E` |
| Primary Container | 🟩 Mint | `#7AF8E2` |
| Secondary | 🟠 Warm Amber | `#8B5E00` |
| Secondary Container | 🟨 Gold | `#FFDEA6` |
| Tertiary | 🟣 Soft Purple | `#6750A4` |
| Background | ⬜ Cool Mint | `#F5FBF8` |

</td>
<td>

#### 🌙 Dark Theme
| Role | Color | Hex |
|---|---|---|
| Primary | 🟢 Light Teal | `#5CDBC6` |
| Primary Container | 🟢 Dark Teal | `#005046` |
| Secondary | 🟡 Bright Amber | `#FFBE48` |
| Secondary Container | 🟤 Deep Gold | `#694600` |
| Tertiary | 🟣 Lavender | `#CFBCFF` |
| Background | ⬛ Deep Green-Black | `#0F1513` |

</td>
</tr>
</table>

### Condition Status Colors

| Condition | Color | Hex | Usage |
|---|---|---|---|
| ✅ Working | 🟢 Green | `#2E7D32` | Badges, chips, stat cards |
| ⚠️ Needs Repair | 🟡 Amber | `#F9A825` | Badges, chips, stat cards |
| ❌ Broken | 🔴 Red | `#C62828` | Badges, chips, stat cards |

### 🪄 Dynamic Colors (Material You)

On **Android 12+** devices, the app automatically adapts to the user's wallpaper colors using `dynamicLightColorScheme()` / `dynamicDarkColorScheme()`. On older devices, the custom Deep Teal palette is applied.

### Typography

The app uses a custom **Material 3 type scale** built on `FontFamily.SansSerif` with carefully tuned:
- Font sizes from `11sp` (labelSmall) to `57sp` (displayLarge)
- Line heights for optimal readability
- Letter spacing adjustments per text role

---

## 📋 Version Catalog

All dependency versions are centralized in [`gradle/libs.versions.toml`](gradle/libs.versions.toml):

```toml
[versions]
agp             = "8.7.3"
kotlin          = "2.1.0"
ksp             = "2.1.0-1.0.29"
coreKtx         = "1.15.0"
lifecycleRuntimeKtx = "2.8.7"
activityCompose = "1.9.3"
composeBom      = "2024.12.01"
navigationCompose = "2.8.5"
room            = "2.6.1"
camerax         = "1.4.1"
coroutines      = "1.9.0"
```

---

## 🧭 Navigation Map

```
                    ┌─────────────────┐
                    │    Dashboard    │ ◄── Start Destination
                    │   (Home Page)   │
                    └──────┬──────────┘
                           │
            ┌──────────────┼──────────────┐
            ▼              ▼              ▼
   ┌────────────────┐ ┌──────────┐ ┌───────────┐
   │Asset Registration│ │Asset List│ │Issue List │
   └───────┬────────┘ └────┬─────┘ └───────────┘
           │               │
           ▼               ▼
     ┌──────────┐   ┌──────────────┐
     │  Camera  │   │ Asset Detail  │
     └──────────┘   └──────────────┘

     ╔══════════════════════════════╗
     ║  Bottom Navigation Bar      ║
     ║  Dashboard │ Register │     ║
     ║  Assets   │ Issues          ║
     ╚══════════════════════════════╝
```

| Route | Path | Arguments |
|---|---|---|
| Dashboard | `dashboard` | — |
| Asset Registration | `asset_registration` | `photoUri` (SavedStateHandle) |
| Asset List | `asset_list` | — |
| Asset Detail | `asset_detail/{assetId}` | `assetId: Long` |
| Issue List | `issue_list` | — |
| Camera | `camera` | — |
| Report | `report` | — |

---

## 🔑 Key Implementation Details

<details>
<summary><b>💾 Singleton Database Pattern</b></summary>

The database uses `@Volatile` + `synchronized` to ensure thread-safe singleton creation:

```kotlin
companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "namma_shaale_database"
            ).fallbackToDestructiveMigration().build()
                .also { INSTANCE = it }
        }
    }
}
```
</details>

<details>
<summary><b>🔄 Reactive UI with StateFlow</b></summary>

ViewModels convert Room's `Flow` into `StateFlow` for Compose consumption:

```kotlin
val allAssets: StateFlow<List<Asset>> = repository.allAssets
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
```

The `WhileSubscribed(5000)` strategy keeps the flow active for 5 seconds after the last subscriber leaves, preventing unnecessary restarts during configuration changes.
</details>

<details>
<summary><b>🔍 Combined Search + Filter</b></summary>

Search and filter are implemented using `combine` on three flows:

```kotlin
val filteredAssets: StateFlow<List<Asset>> = combine(
    allAssets, _searchQuery, _selectedFilter
) { assets, query, filter ->
    var result = assets
    if (query.isNotBlank()) {
        result = result.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.serialNo.contains(query, ignoreCase = true)
        }
    }
    if (filter != "All") {
        result = result.filter { it.condition == filter }
    }
    result
}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
```
</details>

<details>
<summary><b>📸 CameraX Photo Capture</b></summary>

Photos are captured using CameraX's `ImageCapture` use case and saved to external storage:

```kotlin
val photoFile = File(
    context.getExternalFilesDir(null),
    SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis()) + ".jpg"
)
imageCapture.takePicture(
    ImageCapture.OutputFileOptions.Builder(photoFile).build(),
    executor,
    object : ImageCapture.OnImageSavedCallback { /* ... */ }
)
```

The captured URI is passed back via Navigation's `SavedStateHandle`.
</details>

---

## 🤝 Contributing

We welcome contributions! Here's how to get started:

```bash
# 1. Fork the repo on GitHub

# 2. Clone your fork
git clone https://github.com/<your-username>/Namma-Shaale-Mindmatrix-.git

# 3. Create a feature branch
git checkout -b feature/amazing-feature

# 4. Make your changes and commit
git commit -m "feat: add amazing feature"

# 5. Push to your fork
git push origin feature/amazing-feature

# 6. Open a Pull Request on GitHub
```

### 💡 Ideas for Contribution

- [ ] Add image loading with **Coil** for asset photos
- [ ] Implement **barcode/QR code scanning** for serial numbers
- [ ] Add **export to CSV/PDF** functionality
- [ ] Implement **cloud sync** with Firebase
- [ ] Add **multi-language support** (Kannada, Hindi, etc.)
- [ ] Write **unit tests** for ViewModels and Repository
- [ ] Add **UI tests** with Compose Testing

---

## 👥 Team

<div align="center">

### 🧠 Team MindMatrix

*Developed as part of the **Namma Shaale** initiative — empowering schools with modern technology for asset management.*

</div>

---

## 📄 License

This project is **open source** and available for educational purposes.

---

<div align="center">

---

**⭐ If this project helped you, consider giving it a star!**

Made with ❤️ by **Team MindMatrix** using **Kotlin** & **Jetpack Compose**

[![GitHub](https://img.shields.io/badge/View_on-GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/sibareddy130404/Namma-Shaale-Mindmatrix-)

</div>
