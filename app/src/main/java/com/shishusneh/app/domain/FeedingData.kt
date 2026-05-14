package com.shishusneh.app.domain

import com.shishusneh.app.R

/**
 * Category for a feeding tip — used by the UI layer to map to a background color.
 * Keeping Compose Color types out of domain models.
 */
enum class TipCategory {
    PEACH, GREEN, PURPLE, YELLOW, BLUE, WARNING, CARD_PEACH
}

data class FeedingTip(
    val titleRes: Int,
    val descriptionRes: Int,
    val emoji: String,
    val category: TipCategory,
    val minDays: Int = 0,
    val maxDays: Int = Int.MAX_VALUE
)

object FeedingData {

    val allTips = listOf(
        FeedingTip(
            R.string.tip_exclusive_breastfeeding,
            R.string.desc_exclusive_breastfeeding,
            "🤱",
            TipCategory.PEACH,
            minDays = 0, maxDays = 180
        ),
        FeedingTip(
            R.string.tip_breastfeeding_latch,
            R.string.desc_breastfeeding_latch,
            "💞",
            TipCategory.GREEN,
            minDays = 0, maxDays = 180
        ),
        FeedingTip(
            R.string.tip_night_feeding,
            R.string.desc_night_feeding,
            "🌙",
            TipCategory.PURPLE,
            minDays = 0, maxDays = 180
        ),
        FeedingTip(
            R.string.tip_starting_solids,
            R.string.desc_starting_solids,
            "🥣",
            TipCategory.YELLOW,
            minDays = 180, maxDays = 365
        ),
        FeedingTip(
            R.string.tip_foods_introduce,
            R.string.desc_foods_introduce,
            "🥕",
            TipCategory.CARD_PEACH,
            minDays = 180, maxDays = 365
        ),
        FeedingTip(
            R.string.tip_foods_avoid,
            R.string.desc_foods_avoid,
            "⚠️",
            TipCategory.WARNING,
            minDays = 0, maxDays = 365
        ),
        FeedingTip(
            R.string.tip_iron_rich_foods,
            R.string.desc_iron_rich_foods,
            "🌿",
            TipCategory.GREEN,
            minDays = 180, maxDays = Int.MAX_VALUE
        ),
        FeedingTip(
            R.string.tip_signs_hunger,
            R.string.desc_signs_hunger,
            "👶",
            TipCategory.BLUE,
            minDays = 0, maxDays = 365
        ),
        FeedingTip(
            R.string.tip_vitamin_d,
            R.string.desc_vitamin_d,
            "☀️",
            TipCategory.YELLOW,
            minDays = 0, maxDays = 365
        ),
        FeedingTip(
            R.string.tip_hydration_toddlers,
            R.string.desc_hydration_toddlers,
            "🥛",
            TipCategory.BLUE,
            minDays = 365, maxDays = Int.MAX_VALUE
        )
    )

    fun getTipsForAge(babyAgeDays: Int): List<FeedingTip> =
        allTips.filter { babyAgeDays in it.minDays..it.maxDays }
}
