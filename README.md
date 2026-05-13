# Shishu-Sneha-App-Development
<h1 align="center">🍼 Shishu-Sneh (शिशु स्नेह)</h1>

<p align="center">
  <strong>Your Baby's First Year Guide</strong><br>
  A comprehensive maternal and infant healthcare Android application designed specifically for new mothers in India, acting as a "digital elder" to help track and ensure healthy infant development.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android" alt="Platform">
  <img src="https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=flat-square&logo=kotlin" alt="Kotlin">
  <img src="https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=flat-square&logo=jetpackcompose" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/Architecture-MVVM-FF9800?style=flat-square" alt="MVVM">
</p>

---

## ✨ Key Features

*   📊 **Growth Tracking**: Track your baby's weight and height over time. Visualized using beautiful, interactive `MPAndroidChart` line charts to ensure healthy development curves.
*   💉 **Vaccination Schedule (India UIP)**: A comprehensive immunization calendar following India's Universal Immunization Programme.
    *   **Smart Reminders**: Automated background notifications via `WorkManager` alert the mother 2 days before and on the exact day a vaccine is due.
*   🏆 **Milestone Checklist**: Monitor the baby's developmental progress based on World Health Organization (WHO) guidelines, with a simple "Yes/Not Yet" tracking system.
*   🍼 **Feeding Guide**: Step-by-step, age-appropriate nutrition advice, seamlessly guiding the transition from exclusive breastfeeding to complementary solid foods.
*   🌐 **Multi-language Support**: Built-in language settings to switch between English and localized languages (like Hindi) for broader accessibility.
*   🎨 **Beautiful & Warm UI**: A soft, welcoming pastel theme (cream, peach, soft green) utilizing Material 3 components, spring animations, and smooth transitions to create an approachable, mother-friendly experience.

---

## 🏗️ Architecture & Tech Stack

The application strictly adheres to modern Android development standards, utilizing **Clean Architecture** principles and the **MVVM** (Model-View-ViewModel) design pattern.

```text
MVVM + Clean Architecture
├── UI Layer      → Jetpack Compose Screens, Navigation Compose
├── Domain Layer  → Business logic, Schedule Calculators
└── Data Layer    → Room Database, DataStore, Repositories
```

### Core Technologies:
| Category | Technology Used |
| :--- | :--- |
| **UI Framework** | Jetpack Compose, Material 3, Navigation Compose |
| **State Management** | MVVM, Kotlin Coroutines, StateFlow |
| **Local Persistence** | Room Database (SQLite), DataStore (Preferences) |
| **Background Tasks** | WorkManager (Persistent Alarms & Reminders) |
| **Data Visualization**| MPAndroidChart |
| **Language** | 100% Kotlin |

---

## 🗄️ Database Schema

The app relies on a robust local **Room Database** for offline-first functionality:
*   `MotherProfile`: Core entity storing the mother's name, baby's name, DOB, and gender.
*   `GrowthRecord`: Timestamped entries for weight (kg) and height (cm).
*   `MilestoneRecord`: Tracking entity saving the achieved/pending status of specific developmental milestones.

---

## 🚀 Getting Started

### Prerequisites
*   **Android Studio**: Hedgehog (2023.1.1) or newer.
*   **Minimum SDK**: API 24 (Android 7.0 Nougat)
*   **Target SDK**: API 35 (Android 15)

### Installation
1.  Clone the repository to your local machine.
2.  Open Android Studio and select `File → Open` and choose the `ShishuSneh` directory.
3.  Wait for the initial Gradle sync to complete (this will download necessary dependencies like `MPAndroidChart` via JitPack).
4.  Connect a physical Android device or start an emulator.
5.  Click the **▶ Run** button.

---

## 🎯 Impact & Vision

**Shishu-Sneh** is built with a mission to improve maternal and child healthcare outcomes in rural and semi-urban India:
*   **Child Survival:** Timely vaccination reminders drastically reduce vaccine-preventable diseases.
*   **Stunting Prevention:** Consistent growth tracking helps identify nutritional deficiencies early.
*   **Health Equity:** Providing an accessible, digital tool with a simple UI for mothers with limited health or tech literacy.
