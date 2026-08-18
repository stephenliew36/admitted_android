package com.realtouchapp.admitted

import com.realtouchapp.admitted.common.algorithm.ScoreAlgorithm
import com.realtouchapp.admitted.common.parity.ApplicationTestAdviceRules
import com.realtouchapp.admitted.common.parity.BarLegendRules
import com.realtouchapp.admitted.common.parity.GoogleProfileName
import com.realtouchapp.admitted.common.parity.GoogleProfileNameMapper
import com.realtouchapp.admitted.common.parity.SchoolParityRules
import com.realtouchapp.admitted.common.parity.SelectionStateRules
import com.realtouchapp.admitted.common.parity.SingleActionGate
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.admitted.network.entity.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test as JUnitTest

class ParityRulesTest {
    @JUnitTest
    fun scoreValidation_coversNewScalesAndBoundaries() {
        assertTrue(Test.GenreInfo.DUOLINGO.isValidScore("10"))
        assertTrue(Test.GenreInfo.DUOLINGO.isValidScore("160"))
        assertFalse(Test.GenreInfo.DUOLINGO.isValidScore("161"))
        assertFalse(Test.GenreInfo.DUOLINGO.isValidScore(""))
        assertFalse(Test.GenreInfo.DUOLINGO.isValidScore("12"))
        assertTrue(Test.GenreInfo.IB.isValidScore("1"))
        assertTrue(Test.GenreInfo.IB.isValidScore("7"))
        assertFalse(Test.GenreInfo.IB.isValidScore("8"))
        assertTrue(Test.GenreInfo.TOEFL_ALTERNATE.isValidScore("5.5"))
        assertFalse(Test.GenreInfo.TOEFL_ALTERNATE.isValidScore("5.25"))
        assertFalse(Test.GenreInfo.TOEFL_ALTERNATE.isValidScore(null))
        assertFalse(Test.GenreInfo.SAT.isValidScore("1610"))
    }

    @JUnitTest
    fun selectedApAttempt_usesHighestScoreThenNewestDate() {
        val olderHigh = test(5, "Biology", "5", 2024, "January")
        val newerHigh = test(5, "Biology", "5", 2025, "February")
        val lowerNew = test(5, "Biology", "4", 2026, "December")
        val chemistry = test(5, "Chemistry", "4", 2023, "May")

        val selected = Test.selectedApAttempts(listOf(olderHigh, newerHigh, lowerNew, chemistry))

        assertEquals(2, selected.size)
        assertTrue(selected.contains(newerHigh))
        assertTrue(selected.contains(chemistry))
    }

    @JUnitTest
    fun scoring_usesSelectedApAndNewLanguageGenres() {
        val algorithm = ScoreAlgorithm().apply {
            isRequireLanguageExam = true
            tests = arrayListOf(
                test(5, "Biology", "4", 2024, "June"),
                test(5, "Biology", "5", 2023, "June"),
                test(7, "IB Biology HL", "7", 2025, "May"),
                test(8, null, "155", 2025, "May")
            )
        }

        assertEquals(41, algorithm.getTestsScore())
    }

    @JUnitTest
    fun googleNameMapping_prefersStructuredNamesAndFallsBackSafely() {
        assertEquals("Ada", GoogleProfileNameMapper.resolve(" Ada ", "Lovelace", "Different Name").firstName)
        assertEquals("Lovelace", GoogleProfileNameMapper.resolve("Ada", "Lovelace", null).lastName)
        assertEquals("Stephen", GoogleProfileNameMapper.resolve(null, null, "Stephen Liew").firstName)
        assertEquals("Liew", GoogleProfileNameMapper.resolve(null, null, "Stephen Liew").lastName)
        assertNull(GoogleProfileNameMapper.resolve(null, null, "Prince").lastName)
        assertNull(GoogleProfileNameMapper.resolve(" ", " ", " ").fullName)
        val filled = GoogleProfileNameMapper.fillMissing("", null, GoogleProfileName("Ada", "Lovelace", "Ada Lovelace"))
        assertEquals("Ada", filled.firstName)
        assertEquals("Lovelace", filled.lastName)
        assertEquals("Kept", GoogleProfileNameMapper.fillMissing("Kept", "Name", GoogleProfileName("Ada", "Lovelace", null)).firstName)
    }

    @JUnitTest
    fun schoolRules_filterNormalizeAndPagePhotos() {
        val us = TargetSchool().copy(schoolId = 1, country = 1)
        val canada = TargetSchool().copy(schoolId = 2, country = 11)
        assertEquals(listOf(us), SchoolParityRules.roadMapSchools(listOf(us, canada)))
        assertEquals(listOf(us), SchoolParityRules.exploreSchools(listOf(us, canada)))

        val pages = SchoolParityRules.photoPages(
            listOf(" ", "//images.example/a.jpg", "https://media.usnews.com/video/a", "example.com/b c.jpg", "https://example.com/d.jpg", "https://example.com/e.jpg")
        )
        assertEquals(2, pages.size)
        assertEquals(3, pages.first().size)
        assertEquals("https://images.example/a.jpg", pages.first().first())
        assertEquals("https://example.com/b%20c.jpg", pages.first()[1])
        assertNull(SchoolParityRules.normalizeExternalUrl("javascript:alert(1)"))
        assertEquals("https://example.com/path", SchoolParityRules.normalizeExternalUrl("example.com/path"))
        assertTrue(SchoolParityRules.photoPages(emptyList()).isEmpty())
        assertEquals(
            listOf("https://media.usnews.com/video/only"),
            SchoolParityRules.displayPhotoUrls(listOf("https://media.usnews.com/video/only"))
        )
    }

    @JUnitTest
    fun legendOrigins_matchSegmentStarts() {
        assertEquals(listOf(0.0, 60.0), BarLegendRules.origins(listOf(60.0, 40.0)))
        assertEquals(listOf(0.0, 20.0, 50.0), BarLegendRules.origins(listOf(20.0, 30.0, 50.0)))
        assertEquals(listOf(0.0, 0.0), BarLegendRules.origins(listOf(0.0, 0.0)))
    }

    @JUnitTest
    fun selectionStateAndActionGate_surviveBoundsAndSuppressDuplicates() {
        assertEquals(0, SelectionStateRules.restoredPosition(-4, 4))
        assertEquals(3, SelectionStateRules.restoredPosition(9, 4))
        assertEquals(2, SelectionStateRules.restoredPosition(2, 4))

        val gate = SingleActionGate()
        assertTrue(gate.tryBegin())
        assertFalse(gate.tryBegin())
        gate.reset()
        assertTrue(gate.tryBegin())
    }

    @JUnitTest
    fun ibRequiresSubjectAndGroupsBySubject() {
        assertTrue(Test.GenreInfo.IB.requiresSubject())
        assertTrue(Test.GenreInfo.AP.requiresSubject())
        assertFalse(Test.GenreInfo.SAT.requiresSubject())
        assertTrue(test(7, "IB Biology HL", "6", 2025, "May").requiresSubject())
        assertEquals("IB Biology HL", test(7, "IB Biology HL", "6", 2025, "May").title())
        assertEquals("IB", test(7, null, "6", 2025, "May").title())

        val bioOld = test(7, "IB Biology HL", "6", 2024, "May")
        val bioNew = test(7, "IB Biology HL", "7", 2025, "May")
        val chem = test(7, "IB Chemistry HL", "5", 2025, "May")
        val sat = test(1, null, "1500", 2024, "March")
        val groups = Test.groupedAttempts(listOf(bioOld, bioNew, chem, sat))

        assertEquals(3, groups.size)
        assertEquals(listOf(sat), groups.first())
        assertTrue(groups.any { it.contains(bioOld) && it.contains(bioNew) })
        assertTrue(groups.any { it == listOf(chem) || it.contains(chem) && it.size == 1 })
        assertEquals(bioNew, Test.selectedHighestMostRecent(listOf(bioOld, bioNew)))
        assertEquals("6 / 6", test(6, null, "6", 2025, "May").displayScore())
        assertEquals("155 / 160", test(8, null, "155", 2025, "May").displayScore())
    }

    @JUnitTest
    fun scoring_toeflScaleIbCapAndLanguageSkip() {
        val algorithm = ScoreAlgorithm()
        algorithm.tests = arrayListOf(test(6, null, "5.5", 2025, "May"))
        algorithm.isRequireLanguageExam = true
        assertEquals(33, algorithm.getPointTest(algorithm.tests.first()))
        assertEquals(33, algorithm.getTestsScore())

        algorithm.tests = arrayListOf(
            test(5, "Biology", "5", 2024, "May"),
            test(5, "Chemistry", "5", 2024, "May"),
            test(5, "Physics", "5", 2024, "May"),
            test(5, "Calculus", "5", 2024, "May"),
            test(5, "English", "5", 2024, "May"),
            test(5, "History", "5", 2024, "May"),
            test(5, "Stats", "5", 2024, "May"),
            test(5, "Psych", "5", 2024, "May"),
            test(7, "IB Biology HL", "7", 2025, "May"),
            test(7, "IB Chemistry HL", "7", 2025, "May")
        )
        assertEquals(30, algorithm.getTestsScore())

        algorithm.isRequireLanguageExam = false
        algorithm.tests = arrayListOf(
            test(1, null, "1600", 2025, "March"),
            test(5, "Biology", "5", 2025, "May"),
            test(3, null, "120", 2025, "May")
        )
        assertEquals(54, algorithm.getTestsScore())
    }

    @JUnitTest
    fun applicationAdvice_titlesAndTiersMatchIos() {
        assertEquals("IB Biology HL 7/7", ApplicationTestAdviceRules.adviceTitle(test(7, "IB Biology HL", "7", 2025, "May")))
        assertEquals(0, ApplicationTestAdviceRules.adviceIndex(test(7, "IB Biology HL", "3", 2025, "May")))
        assertEquals("Duolingo English Test 155 / 160", ApplicationTestAdviceRules.adviceTitle(test(8, null, "155", 2025, "May")))
        assertEquals(0, ApplicationTestAdviceRules.adviceIndex(test(8, null, "140", 2025, "May")))
        assertEquals(1, ApplicationTestAdviceRules.adviceIndex(test(8, null, "120", 2025, "May")))
        assertEquals(2, ApplicationTestAdviceRules.adviceIndex(test(8, null, "100", 2025, "May")))
        assertEquals("TOEFL 5.5 / 6", ApplicationTestAdviceRules.adviceTitle(test(6, null, "5.5", 2025, "May")))
        assertEquals(1, ApplicationTestAdviceRules.adviceIndex(test(6, null, "5.5", 2025, "May")))
        assertEquals(0, ApplicationTestAdviceRules.adviceIndex(test(6, null, "6", 2025, "May")))
        assertEquals(3, ApplicationTestAdviceRules.adviceIndex(test(6, null, "3.5", 2025, "May")))
        assertEquals("IELTS 8.0 / 9.0", ApplicationTestAdviceRules.adviceTitle(test(4, null, "8", 2025, "May")))
    }

    @JUnitTest
    fun selectedIbAttempts_dedupesBySubject() {
        val bioOld = test(7, "IB Biology HL", "6", 2024, "May")
        val bioNew = test(7, "IB Biology HL", "7", 2025, "May")
        val chem = test(7, "IB Chemistry HL", "5", 2025, "May")
        val selected = Test.selectedIbAttempts(listOf(bioOld, bioNew, chem))
        assertEquals(2, selected.size)
        assertTrue(selected.contains(bioNew))
        assertTrue(selected.contains(chem))
    }

    private fun test(genre: Int, subject: String?, score: String?, year: Int?, month: String?) =
        Test(null, subject, month, year, genre, null, score, null)
}
