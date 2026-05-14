package com.shishusneh.app.domain

data class MilestoneItem(
    val id: String,
    val weekGroup: Int,         // Weeks at which this milestone is expected
    val title: String,
    val question: String,       // The yes/no question to ask
    val category: MilestoneCategory,
    val tip: String = ""
)

enum class MilestoneCategory(val displayName: String, val emoji: String) {
    MOTOR("Motor Skills", "🤸"),
    SOCIAL("Social & Emotional", "💞"),
    COMMUNICATION("Communication", "🗣️"),
    COGNITIVE("Cognitive", "🧠"),
    FEEDING("Feeding", "🍼")
}

object MilestoneData {

    val milestones = listOf(
        // Week 4 — 1 Month
        MilestoneItem(
            id = "wk4_head_lift",
            weekGroup = 4,
            title = "Head Lifting",
            question = "Is your baby lifting their head briefly when on their tummy?",
            category = MilestoneCategory.MOTOR,
            tip = "Tummy time helps strengthen neck muscles. Try 2-3 minutes after feeds."
        ),
        MilestoneItem(
            id = "wk4_eye_track",
            weekGroup = 4,
            title = "Eye Tracking",
            question = "Does your baby follow a moving object with their eyes?",
            category = MilestoneCategory.COGNITIVE,
            tip = "Hold a colorful toy about 30 cm away and move it slowly."
        ),
        MilestoneItem(
            id = "wk4_startle",
            weekGroup = 4,
            title = "Startle Response",
            question = "Does your baby startle at loud sounds?",
            category = MilestoneCategory.COGNITIVE
        ),

        // Week 8 — 2 Months
        MilestoneItem(
            id = "wk8_social_smile",
            weekGroup = 8,
            title = "Social Smile",
            question = "Does your baby smile when they see your face?",
            category = MilestoneCategory.SOCIAL,
            tip = "Smile often at your baby — they learn from watching your expressions!"
        ),
        MilestoneItem(
            id = "wk8_cooing",
            weekGroup = 8,
            title = "Cooing Sounds",
            question = "Is your baby making gurgling or cooing sounds?",
            category = MilestoneCategory.COMMUNICATION
        ),
        MilestoneItem(
            id = "wk8_head_steady",
            weekGroup = 8,
            title = "Head Control",
            question = "Can your baby hold their head steady for a few seconds?",
            category = MilestoneCategory.MOTOR
        ),

        // Week 12 — 3 Months
        MilestoneItem(
            id = "wk12_head_control",
            weekGroup = 12,
            title = "Strong Head Control",
            question = "Is your baby holding their head up steadily when held upright?",
            category = MilestoneCategory.MOTOR
        ),
        MilestoneItem(
            id = "wk12_laugh",
            weekGroup = 12,
            title = "Laughing",
            question = "Does your baby laugh or squeal with delight?",
            category = MilestoneCategory.SOCIAL
        ),
        MilestoneItem(
            id = "wk12_grasp",
            weekGroup = 12,
            title = "Grasping Objects",
            question = "Can your baby grasp a rattle or your finger?",
            category = MilestoneCategory.MOTOR
        ),

        // Week 16 — 4 Months
        MilestoneItem(
            id = "wk16_roll_front_to_back",
            weekGroup = 16,
            title = "Rolling",
            question = "Has your baby rolled from front to back at least once?",
            category = MilestoneCategory.MOTOR
        ),
        MilestoneItem(
            id = "wk16_object_interest",
            weekGroup = 16,
            title = "Object Interest",
            question = "Does your baby reach out for objects they see?",
            category = MilestoneCategory.COGNITIVE
        ),
        MilestoneItem(
            id = "wk16_recognize_face",
            weekGroup = 16,
            title = "Face Recognition",
            question = "Does your baby recognize your face and smile at you specifically?",
            category = MilestoneCategory.SOCIAL
        ),

        // Week 24 — 6 Months
        MilestoneItem(
            id = "wk24_sit_support",
            weekGroup = 24,
            title = "Sitting with Support",
            question = "Can your baby sit with support without slumping?",
            category = MilestoneCategory.MOTOR
        ),
        MilestoneItem(
            id = "wk24_babble",
            weekGroup = 24,
            title = "Babbling",
            question = "Is your baby babbling with consonant sounds like 'ba', 'da', 'ma'?",
            category = MilestoneCategory.COMMUNICATION
        ),
        MilestoneItem(
            id = "wk24_solid_food",
            weekGroup = 24,
            title = "Ready for Solids",
            question = "Is your baby showing interest in food and can hold head steady during feeding?",
            category = MilestoneCategory.FEEDING,
            tip = "6 months is the recommended age to start complementary foods alongside breastfeeding."
        ),
        MilestoneItem(
            id = "wk24_transfer_hands",
            weekGroup = 24,
            title = "Transfers Objects",
            question = "Does your baby transfer objects from one hand to the other?",
            category = MilestoneCategory.MOTOR
        ),

        // Week 36 — 9 Months
        MilestoneItem(
            id = "wk36_crawl",
            weekGroup = 36,
            title = "Crawling",
            question = "Is your baby crawling (any style — commando crawl counts!)?",
            category = MilestoneCategory.MOTOR
        ),
        MilestoneItem(
            id = "wk36_pincer",
            weekGroup = 36,
            title = "Pincer Grasp",
            question = "Can your baby pick up small objects using thumb and forefinger?",
            category = MilestoneCategory.MOTOR
        ),
        MilestoneItem(
            id = "wk36_stranger_anxiety",
            weekGroup = 36,
            title = "Stranger Awareness",
            question = "Does your baby show preference for you over strangers?",
            category = MilestoneCategory.SOCIAL
        ),
        MilestoneItem(
            id = "wk36_mama_dada",
            weekGroup = 36,
            title = "Mama / Dada",
            question = "Is your baby saying 'mama' or 'dada' (even randomly)?",
            category = MilestoneCategory.COMMUNICATION
        ),

        // Week 52 — 12 Months (1 Year)
        MilestoneItem(
            id = "wk52_walk",
            weekGroup = 52,
            title = "First Steps",
            question = "Has your baby taken at least one independent step?",
            category = MilestoneCategory.MOTOR
        ),
        MilestoneItem(
            id = "wk52_words",
            weekGroup = 52,
            title = "First Words",
            question = "Is your baby saying 1-2 words with meaning (e.g., mama, ball, bye)?",
            category = MilestoneCategory.COMMUNICATION
        ),
        MilestoneItem(
            id = "wk52_wave",
            weekGroup = 52,
            title = "Waving Bye-bye",
            question = "Does your baby wave bye-bye?",
            category = MilestoneCategory.SOCIAL
        )
    )

    fun getMilestonesUpToWeek(currentWeek: Int): List<MilestoneItem> {
        return milestones.filter { it.weekGroup <= maxOf(currentWeek, 4) }
    }

    fun getMilestoneGroups(currentWeek: Int): Map<Int, List<MilestoneItem>> {
        return getMilestonesUpToWeek(currentWeek).groupBy { it.weekGroup }
    }

    fun getTodaysMilestone(currentWeek: Int): MilestoneItem? {
        val validWeeks = listOf(4, 8, 12, 16, 24, 36, 52)
        val relevantWeek = validWeeks.lastOrNull { it <= currentWeek } ?: 4
        return milestones.filter { it.weekGroup == relevantWeek }.randomOrNull()
    }
}
