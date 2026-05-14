package com.shishusneh.app.domain

import java.util.concurrent.TimeUnit

data class VaccineInfo(
    val id: String,
    val name: String,
    val diseasePrevented: String,
    val weekFromBirth: Int,     // How many weeks after birth
    val dayFromBirth: Int = weekFromBirth * 7,
    val notes: String = ""
)

enum class VaccineStatus {
    DONE, DUE_SOON, UPCOMING, MISSED
}

data class VaccineScheduleItem(
    val vaccine: VaccineInfo,
    val dueDate: Long,          // Actual due date timestamp
    val status: VaccineStatus,
    val daysDiff: Long          // Positive = days until due, Negative = days overdue
)

object VaccineSchedule {

    val vaccines = listOf(
        VaccineInfo(
            id = "bcg_opv0",
            name = "BCG + OPV-0",
            diseasePrevented = "Tuberculosis + Polio",
            weekFromBirth = 0,
            notes = "Given at birth"
        ),
        VaccineInfo(
            id = "hep_b1",
            name = "Hepatitis B-1",
            diseasePrevented = "Hepatitis B",
            weekFromBirth = 0,
            notes = "Given at birth"
        ),
        VaccineInfo(
            id = "penta1_opv1",
            name = "Pentavalent-1 + OPV-1",
            diseasePrevented = "Diphtheria, Tetanus, Whooping Cough, Hib, Hepatitis B, Polio",
            weekFromBirth = 6
        ),
        VaccineInfo(
            id = "penta2_opv2",
            name = "Pentavalent-2 + OPV-2",
            diseasePrevented = "Diphtheria, Tetanus, Whooping Cough, Hib, Hepatitis B, Polio",
            weekFromBirth = 10
        ),
        VaccineInfo(
            id = "penta3_opv3_ipv",
            name = "Pentavalent-3 + OPV-3 + IPV",
            diseasePrevented = "Diphtheria, Tetanus, Whooping Cough, Hib, Hep B, Polio (injectable)",
            weekFromBirth = 14
        ),
        VaccineInfo(
            id = "measles_rubella1",
            name = "Measles-Rubella-1",
            diseasePrevented = "Measles + Rubella",
            weekFromBirth = 39, // ~9 months
            notes = "Given at 9 months"
        ),
        VaccineInfo(
            id = "vitamin_a1",
            name = "Vitamin A-1",
            diseasePrevented = "Vitamin A Deficiency, Night Blindness",
            weekFromBirth = 39,
            notes = "Given at 9 months with Measles vaccine"
        ),
        VaccineInfo(
            id = "je1",
            name = "Japanese Encephalitis-1",
            diseasePrevented = "Japanese Encephalitis (Brain Fever)",
            weekFromBirth = 39,
            notes = "Endemic areas only"
        ),
        VaccineInfo(
            id = "dpt_boost_opv_boost",
            name = "DPT Booster + OPV Booster",
            diseasePrevented = "Diphtheria, Tetanus, Pertussis, Polio (booster)",
            weekFromBirth = 74, // ~17 months
            notes = "16-24 months booster"
        ),
        VaccineInfo(
            id = "measles_rubella2",
            name = "Measles-Rubella-2",
            diseasePrevented = "Measles + Rubella (booster)",
            weekFromBirth = 74,
            notes = "16-24 months booster"
        ),
        VaccineInfo(
            id = "vitamin_a2",
            name = "Vitamin A (subsequent doses)",
            diseasePrevented = "Vitamin A Deficiency",
            weekFromBirth = 74,
            notes = "Every 6 months until 5 years"
        )
    )

    fun getSchedule(babyDobMillis: Long): List<VaccineScheduleItem> {
        val now = System.currentTimeMillis()
        return vaccines.map { vaccine ->
            val dueDate = babyDobMillis + TimeUnit.DAYS.toMillis(vaccine.dayFromBirth.toLong())
            val daysDiff = TimeUnit.MILLISECONDS.toDays(dueDate - now)
            val status = when {
                daysDiff < -7 -> VaccineStatus.MISSED
                daysDiff < 0 -> VaccineStatus.DUE_SOON
                daysDiff <= 7 -> VaccineStatus.DUE_SOON
                else -> VaccineStatus.UPCOMING
            }
            // Check if vaccine was "done" (birth vaccines are always done if baby is old enough)
            val finalStatus = if (daysDiff < -14) VaccineStatus.DONE else status

            VaccineScheduleItem(
                vaccine = vaccine,
                dueDate = dueDate,
                status = finalStatus,
                daysDiff = daysDiff
            )
        }
    }

    fun getNextVaccine(babyDobMillis: Long): VaccineScheduleItem? {
        return getSchedule(babyDobMillis)
            .filter { it.status == VaccineStatus.DUE_SOON || it.status == VaccineStatus.UPCOMING }
            .minByOrNull { it.dueDate }
    }
}
