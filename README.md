# 🍼 Shishu-Sneh (शिशु स्नेह)

**Your Baby's First Year Guide** — A maternal and infant healthcare Android app built for new mothers in India.

---

## 📱 App Overview

Shishu-Sneh acts as a "digital elder" helping new mothers track their baby's health through:

- **Growth Tracking** — Weight & height with MPAndroidChart line charts
- **Vaccination Schedule** — India's UIP immunization calendar with WorkManager alerts
- **Milestone Checklist** — WHO developmental milestones with Yes/Not Yet tracking
- **Feeding Guide** — Age-appropriate nutrition tips (breastfeeding → complementary foods)
- **Home Dashboard** — Beautiful overview of all health metrics at a glance

---

## 🏗️ Architecture

```
MVVM + Clean Architecture
├── UI Layer      → Jetpack Compose screens + ViewModels
├── Domain Layer  → Business logic (VaccineSchedule, MilestoneData)
└── Data Layer    → Room Database + Repositories
```

**Tech Stack:**
| Technology | Usage |
|------------|-------|
| Jetpack Compose + Material 3 | UI |
| Navigation Compose | Screen routing |
| Room Database | Local data persistence |
| WorkManager | Background vaccine notifications |
| MPAndroidChart | Growth line charts |
| Kotlin Coroutines + Flow | Async data streams |
| MVVM + ViewModel | State management |

---

## 🎨 Design

- **Color Palette:** Warm cream (`#FFF8F0`), peach (`#FF8A65`), soft green (`#81C784`)
- **Typography:** Material 3 typography with rounded shapes
- **Cards:** Rounded corners (16-24dp), soft shadows, pastel backgrounds
- **Animations:** Spring animations on splash, AnimatedVisibility for form

---

## 📁 Project Structure

```
app/
├── data/
│   ├── db/
│   │   ├── entities/        ← MotherProfile, GrowthRecord, MilestoneRecord
│   │   ├── dao/             ← DAOs for each entity
│   │   └── AppDatabase.kt   ← Room database singleton
│   └── repository/          ← ProfileRepository, GrowthRepository, MilestoneRepository
├── domain/
│   ├── VaccineSchedule.kt   ← India UIP vaccine data + schedule calculation
│   └── MilestoneData.kt     ← WHO milestone definitions by week
├── ui/
│   ├── theme/               ← Color.kt, Type.kt, Theme.kt
│   ├── navigation/          ← NavGraph.kt, Screen.kt
│   └── screens/
│       ├── splash/          ← Animated splash with auto-routing
│       ├── profile/         ← Mother + baby setup form
│       ├── home/            ← Dashboard with all cards
│       ├── growth/          ← MPAndroidChart weight/height tracker
│       ├── vaccination/     ← Full immunization schedule
│       ├── feeding/         ← Age-appropriate feeding guide
│       └── milestones/      ← WHO milestone checklist
├── viewmodel/               ← ProfileViewModel, HomeViewModel, GrowthViewModel, MilestoneViewModel
└── worker/
    ├── VaccineReminderWorker.kt     ← Shows notification
    ├── VaccineSchedulerHelper.kt    ← Schedules WorkManager jobs
    └── BootReceiver.kt              ← Re-schedules on device boot
```

---

## 🔔 Vaccination Reminder System

When a baby profile is saved:
1. `VaccineSchedulerHelper.scheduleAllVaccines(dob)` calculates all due dates
2. For each vaccine, **2 WorkManager jobs** are created:
   - 2 days before due date → "Due in 2 days" notification
   - On due date → "Due TODAY!" notification
3. Notifications use `NotificationCompat` with high priority
4. Workers are tagged `vaccine_reminder` for easy cancellation
5. `ExistingWorkPolicy.REPLACE` prevents duplicate reminders

### Vaccines Covered (India UIP)
- BCG + OPV-0 (Birth)
- Hepatitis B-1 (Birth)
- Pentavalent 1,2,3 + OPV 1,2,3 (6, 10, 14 weeks)
- Pentavalent-3 + IPV (14 weeks)
- Measles-Rubella 1 + Vitamin A-1 (9 months)
- DPT Booster + OPV Booster (16-24 months)
- Measles-Rubella 2 (16-24 months)

---

## 🗄️ Room Database

**Tables:**
- `mother_profile` — One profile per family (mother name, baby name, DOB, gender)
- `growth_records` — Weight (kg) + height (cm) with timestamps
- `milestone_records` — Per-milestone achieved/not-yet status

---

## 🚀 How to Open in Android Studio

1. Open Android Studio (Hedgehog 2023.1.1 or newer)
2. `File → Open → ShishuSneh/`
3. Wait for Gradle sync (first sync downloads dependencies including MPAndroidChart from JitPack)
4. Connect a device or start an emulator (API 24+)
5. Click ▶ Run

**Minimum SDK:** API 24 (Android 7.0)  
**Target SDK:** API 35 (Android 15)  
**Compile SDK:** 35  

---

## 📋 Feature Checklist

- [x] Splash screen with animation
- [x] Mother/baby profile setup with date picker
- [x] Home dashboard with hero card, weight card, vaccine card
- [x] Growth tracker with MPAndroidChart line chart
- [x] Vaccination schedule with India UIP data
- [x] WorkManager vaccine reminders (2 days before + on day)
- [x] Feeding guide with age-appropriate tips
- [x] Milestone checklist with Yes/Not Yet toggle
- [x] Bottom navigation with 5 tabs
- [x] Room DB persistence
- [x] MVVM architecture
- [x] Material 3 + warm pastel theme

---

## 🎯 Impact Goals

- **Child Survival:** Timely vaccination reminders reduce vaccine-preventable deaths
- **Stunting Prevention:** Growth tracking helps identify nutritional deficiencies early
- **Health Equity:** Simple, accessible UI for mothers with limited tech literacy
