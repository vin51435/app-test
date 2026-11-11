package com.manasvia.android.data.repository

import com.manasvia.android.core.common.Resource
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperConfig
import com.manasvia.android.domain.model.wallpaper.WallpaperContent
import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import com.manasvia.android.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default offline-first implementation of [WallpaperRepository].
 * Pre-populated with curated spiritual and motivational wisdom (Bhagavad Gita, Stoicism, Mindfulness)
 * and 6 aesthetic theme presets matching Manasvia's design system.
 * Ready for 100% offline standalone execution ($0 cloud cost).
 */
@Singleton
class DefaultWallpaperRepositoryImpl @Inject constructor() : WallpaperRepository {

    private val mutex = Mutex()
    private var quoteCursorIndex = 0

    // Curated Wisdom Catalog
    private val curatedQuotes: List<WallpaperContent> = listOf(
        WallpaperContent(
            id = "gita_2_47",
            quoteText = "Karmanye vadhikaraste ma phaleshu kadachana — You have a right to perform your prescribed duty, but you are not entitled to the fruits of actions.",
            author = "Lord Krishna",
            sourceReference = "Bhagavad Gita 2.47",
            categoryTag = "BHAGAVAD_GITA"
        ),
        WallpaperContent(
            id = "stoic_marcus_mind",
            quoteText = "You have power over your mind — not outside events. Realize this, and you will find strength.",
            author = "Marcus Aurelius",
            sourceReference = "Meditations, Book IV",
            categoryTag = "STOICISM"
        ),
        WallpaperContent(
            id = "mindfulness_frankl_space",
            quoteText = "Between stimulus and response there is a space. In that space is our power to choose our response. In our response lies our growth and our freedom.",
            author = "Viktor E. Frankl",
            sourceReference = "Man's Search for Meaning",
            categoryTag = "MINDFULNESS"
        ),
        WallpaperContent(
            id = "focus_habit_excellence",
            quoteText = "We are what we repeatedly do. Excellence, then, is not an act, but a habit.",
            author = "Will Durant",
            sourceReference = "The Story of Philosophy",
            categoryTag = "DISCIPLINE"
        ),
        WallpaperContent(
            id = "stoic_seneca_time",
            quoteText = "It is not that we have a short time to live, but that we waste a lot of it.",
            author = "Seneca",
            sourceReference = "On the Shortness of Life",
            categoryTag = "STOICISM"
        ),
        WallpaperContent(
            id = "zen_buddha_mind",
            quoteText = "The mind is everything. What you think you become.",
            author = "Gautama Buddha",
            sourceReference = "Dhammapada",
            categoryTag = "MINDFULNESS"
        ),
        WallpaperContent(
            id = "upanishad_isha_inner",
            quoteText = "All this is full; all that is full. From fullness, fullness proceeds.",
            author = "Sage of the Forest",
            sourceReference = "Isha Upanishad, Shanti Mantra",
            categoryTag = "UPANISHADS"
        )
    )

    // Curated Theme Presets matching product_strategy_uniqueness_and_themes.md
    private val themePresets: List<WallpaperSpec> = listOf(
        WallpaperSpec(
            content = curatedQuotes[1], // Marcus Aurelius
            background = WallpaperBackground.LinearGradient(
                startColorHex = "#0A0B0E",
                endColorHex = "#1E1F29"
            ),
            config = WallpaperConfig(
                fontFamily = "Cinzel",
                textColorHex = "#F4F5F7",
                scrimOpacity = 0.55f,
                anchor = WallpaperAnchor.LOWER_THIRD
            ),
            themeName = "Dark Velvet"
        ),
        WallpaperSpec(
            content = curatedQuotes[5], // Buddha
            background = WallpaperBackground.LinearGradient(
                startColorHex = "#F7F4EB",
                endColorHex = "#DFD8C7"
            ),
            config = WallpaperConfig(
                fontFamily = "Cormorant Garamond",
                textColorHex = "#2C2621",
                scrimOpacity = 0.15f,
                anchor = WallpaperAnchor.LOWER_THIRD
            ),
            themeName = "Zen Parchment"
        ),
        WallpaperSpec(
            content = curatedQuotes[0], // Bhagavad Gita
            background = WallpaperBackground.LinearGradient(
                startColorHex = "#16111E",
                endColorHex = "#311D3F"
            ),
            config = WallpaperConfig(
                fontFamily = "Cinzel",
                textColorHex = "#FFF8EE",
                scrimOpacity = 0.60f,
                anchor = WallpaperAnchor.LOWER_THIRD
            ),
            themeName = "Sacred Temple"
        ),
        WallpaperSpec(
            content = curatedQuotes[2], // Viktor Frankl
            background = WallpaperBackground.LinearGradient(
                startColorHex = "#0F1A17",
                endColorHex = "#1D312B"
            ),
            config = WallpaperConfig(
                fontFamily = "Fraunces",
                textColorHex = "#EBF2EE",
                scrimOpacity = 0.50f,
                anchor = WallpaperAnchor.LOWER_THIRD
            ),
            themeName = "Nordic Mist"
        ),
        WallpaperSpec(
            content = curatedQuotes[3], // Will Durant
            background = WallpaperBackground.LinearGradient(
                startColorHex = "#050811",
                endColorHex = "#0B1E38"
            ),
            config = WallpaperConfig(
                fontFamily = "Space Grotesk",
                textColorHex = "#F0F6FC",
                scrimOpacity = 0.65f,
                anchor = WallpaperAnchor.LOWER_THIRD
            ),
            themeName = "Cyber Focus"
        ),
        WallpaperSpec(
            content = curatedQuotes[6], // Isha Upanishad
            background = WallpaperBackground.LinearGradient(
                startColorHex = "#FAF5F0",
                endColorHex = "#F2DFDB"
            ),
            config = WallpaperConfig(
                fontFamily = "Playfair Display",
                textColorHex = "#3D313A",
                scrimOpacity = 0.25f,
                anchor = WallpaperAnchor.LOWER_THIRD
            ),
            themeName = "Ethereal Dawn"
        )
    )

    // In-memory reactive state holding current active specification
    private val activeSpecState = MutableStateFlow(themePresets.first())
    private val activeConfigState = MutableStateFlow(themePresets.first().config)

    override fun getDailyWallpaper(): Flow<Resource<WallpaperSpec>> {
        return activeSpecState.asStateFlow().map { spec ->
            Resource.Success(spec)
        }
    }

    override suspend fun getDailyWallpaperSync(): WallpaperSpec {
        return activeSpecState.value
    }

    override fun getCuratedPresets(): List<WallpaperSpec> {
        return themePresets
    }

    override fun getActiveConfig(): Flow<WallpaperConfig> {
        return activeConfigState.asStateFlow()
    }

    override suspend fun updateConfig(config: WallpaperConfig) {
        mutex.withLock {
            activeConfigState.value = config
            activeSpecState.value = activeSpecState.value.copy(config = config)
        }
    }

    override suspend fun setBackground(background: WallpaperBackground) {
        mutex.withLock {
            activeSpecState.value = activeSpecState.value.copy(background = background)
        }
    }

    override suspend fun setContent(content: WallpaperContent) {
        mutex.withLock {
            activeSpecState.value = activeSpecState.value.copy(content = content)
        }
    }

    override suspend fun applyPreset(preset: WallpaperSpec) {
        mutex.withLock {
            activeConfigState.value = preset.config
            activeSpecState.value = preset
        }
    }

    override suspend fun cycleNextQuote(): WallpaperContent {
        return mutex.withLock {
            val currentIndex = curatedQuotes.indexOfFirst { it.id == activeSpecState.value.content.id }
            val nextIndex = if (currentIndex >= 0) {
                (currentIndex + 1) % curatedQuotes.size
            } else {
                (quoteCursorIndex + 1) % curatedQuotes.size
            }
            quoteCursorIndex = nextIndex
            val nextQuote = curatedQuotes[nextIndex]
            activeSpecState.value = activeSpecState.value.copy(content = nextQuote)
            nextQuote
        }
    }

    private var currentRefreshInterval: WallpaperRefreshInterval = WallpaperRefreshInterval.DEFAULT

    override fun getRefreshInterval(): WallpaperRefreshInterval = currentRefreshInterval

    override fun updateRefreshInterval(interval: WallpaperRefreshInterval) {
        currentRefreshInterval = interval
    }
}
