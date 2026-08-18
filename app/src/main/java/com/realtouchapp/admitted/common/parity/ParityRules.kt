package com.realtouchapp.admitted.common.parity

import com.realtouchapp.admitted.network.entity.CountryCode
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.admitted.network.entity.Test
import java.net.URI

data class GoogleProfileName(
    val firstName: String?,
    val lastName: String?,
    val fullName: String?
)

object GoogleProfileNameMapper {
    fun resolve(givenName: String?, familyName: String?, fullName: String?): GoogleProfileName {
        val given = givenName.clean()
        val family = familyName.clean()
        val full = fullName.clean()
        val parts = full.orEmpty().split(Regex("\\s+")).filter { it.isNotBlank() }
        val resolvedFirst = given ?: parts.firstOrNull()
        val resolvedLast = family ?: parts.drop(1).takeIf { it.isNotEmpty() }?.joinToString(" ")
        val resolvedFull = full ?: listOfNotNull(resolvedFirst, resolvedLast)
            .joinToString(" ")
            .ifBlank { null }
        return GoogleProfileName(resolvedFirst, resolvedLast, resolvedFull)
    }

    fun fillMissing(existingFirst: String?, existingLast: String?, profile: GoogleProfileName): GoogleProfileName {
        return GoogleProfileName(
            existingFirst.clean() ?: profile.firstName,
            existingLast.clean() ?: profile.lastName,
            profile.fullName
        )
    }

    private fun String?.clean(): String? = this?.trim()?.takeIf { it.isNotEmpty() }
}

/**
 * Mirrors iOS `testGroup.adviceTitleString` / `adviceString` tier indexes for Applications college copy.
 * Index maps into the corresponding `array_application_test_*_content` string-array.
 */
object ApplicationTestAdviceRules {
    fun adviceTitle(test: Test): String {
        val score = formatScore(test) ?: return ""
        return when (test.genreInfo()) {
            Test.GenreInfo.SAT -> "SAT $score / 1600"
            Test.GenreInfo.ACT -> "ACT $score / 36"
            Test.GenreInfo.TOEFL -> "TOEFL iBT  $score / 120"
            Test.GenreInfo.IELTS -> "IELTS $score / 9.0"
            Test.GenreInfo.AP -> "${test.title()} $score/5"
            Test.GenreInfo.TOEFL_ALTERNATE -> "TOEFL $score / 6"
            Test.GenreInfo.IB -> "${test.title()} $score/7"
            Test.GenreInfo.DUOLINGO -> "Duolingo English Test $score / 160"
        }
    }

    /** Advice tier index for string-array lookup; IB always returns 0 (single message). */
    fun adviceIndex(test: Test): Int {
        val score = test.score?.toFloatOrNull() ?: return 0
        return when (test.genreInfo()) {
            Test.GenreInfo.SAT -> when {
                score >= 1500 -> 0
                score >= 1400 -> 1
                score >= 1290 -> 2
                score >= 1100 -> 3
                else -> 4
            }
            Test.GenreInfo.ACT -> when {
                score >= 35 -> 0
                score >= 31 -> 1
                score >= 25 -> 2
                score >= 15 -> 3
                else -> 4
            }
            Test.GenreInfo.TOEFL -> when {
                score >= 110 -> 0
                score >= 100 -> 1
                score >= 90 -> 2
                score >= 80 -> 3
                else -> 4
            }
            Test.GenreInfo.IELTS -> when {
                score >= 8.5f -> 0
                score >= 8f -> 1
                score >= 7.5f -> 2
                score >= 7f -> 3
                else -> 4
            }
            Test.GenreInfo.AP -> when (score.toInt()) {
                5 -> 0
                4 -> 1
                3 -> 2
                else -> 3
            }
            Test.GenreInfo.TOEFL_ALTERNATE -> when {
                score >= 6f -> 0
                score >= 5f -> 1
                score >= 4f -> 2
                else -> 3
            }
            Test.GenreInfo.IB -> 0
            Test.GenreInfo.DUOLINGO -> when {
                score >= 140f -> 0
                score >= 120f -> 1
                else -> 2
            }
        }
    }

    fun formatScore(test: Test): String? {
        val raw = test.score ?: return null
        return when (test.genreInfo()) {
            Test.GenreInfo.IELTS, Test.GenreInfo.TOEFL_ALTERNATE ->
                String.format("%.1f", raw.toFloatOrNull() ?: return null)
            else -> raw.toFloatOrNull()?.toInt()?.toString() ?: raw
        }
    }
}

object SchoolParityRules {
    private const val VIDEO_PATH = "media.usnews.com/video/"

    fun roadMapSchools(schools: List<TargetSchool>): List<TargetSchool> =
        schools.filter { it.country == CountryCode.USA.code }

    fun exploreSchools(schools: List<TargetSchool>): List<TargetSchool> = roadMapSchools(schools)

    fun displayPhotoUrls(photos: List<String>?): List<String> {
        val normalized = photos.orEmpty().mapNotNull(::normalizeImageUrl)
        val stillImages = normalized.filterNot { it.contains(VIDEO_PATH, ignoreCase = true) }
        return stillImages.ifEmpty { normalized }
    }

    fun photoPages(photos: List<String>?, imagesPerPage: Int = 3): List<List<String>> {
        require(imagesPerPage > 0)
        return displayPhotoUrls(photos).chunked(imagesPerPage)
    }

    fun normalizeImageUrl(raw: String?): String? = normalizeHttpUrl(raw)

    fun normalizeExternalUrl(raw: String?): String? = normalizeHttpUrl(raw)

    private fun normalizeHttpUrl(raw: String?): String? {
        var value = raw?.trim()?.takeIf { it.isNotEmpty() && !it.equals("N/A", ignoreCase = true) }
            ?: return null
        value = when {
            value.startsWith("//") -> "https:$value"
            !value.matches(Regex("^[A-Za-z][A-Za-z0-9+.-]*://.*")) -> "https://$value"
            else -> value
        }.replace(" ", "%20")
        return try {
            val uri = URI(value)
            if ((uri.scheme == "http" || uri.scheme == "https") && !uri.host.isNullOrBlank()) {
                uri.toASCIIString()
            } else null
        } catch (_: Exception) {
            null
        }
    }
}

object BarLegendRules {
    fun origins(values: List<Double>): List<Double> {
        val total = values.sum()
        if (total <= 0.0) return values.map { 0.0 }
        var start = 0.0
        return values.map { value ->
            val origin = (start / total * 100.0).coerceAtLeast(0.0)
            start += value.coerceAtLeast(0.0)
            origin
        }
    }
}

object SelectionStateRules {
    fun restoredPosition(savedPosition: Int, itemCount: Int): Int =
        if (itemCount <= 0) 0 else savedPosition.coerceIn(0, itemCount - 1)
}

class SingleActionGate {
    private var actionInProgress = false

    @Synchronized
    fun tryBegin(): Boolean {
        if (actionInProgress) return false
        actionInProgress = true
        return true
    }

    @Synchronized
    fun reset() {
        actionInProgress = false
    }
}
