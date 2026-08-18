package com.realtouchapp.admitted.common.algorithm

import com.realtouchapp.admitted.network.entity.AcademicYear
import com.realtouchapp.admitted.network.entity.CommunityImpact
import com.realtouchapp.admitted.network.entity.Honors
import com.realtouchapp.admitted.network.entity.IntellectualCuriosity
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.admitted.network.entity.Test
import java.util.stream.Collectors

/*
    作者: RobertChou
    時間: 2023/2/18 下午 02:16
    信箱: didi31139@gmail.com
    說明:
*/
class ScoreAlgorithm {
    var isRequireLanguageExam = true
    var gpas = ArrayList<AcademicYear>()
    var intellectualCuriosities = ArrayList<IntellectualCuriosity>()
    var communityImpacts = ArrayList<CommunityImpact>()
    var honors = ArrayList<Honors>()
    var tests = ArrayList<Test>()

    fun getTotalPercent():Int{
        var total: Float = 0f

        //CI 30
        total += getCIGradePoint(9) + getCIGradePoint(10) + getCIGradePoint(11) + getCIGradePoint(12)

        //IC 70
        total += getICGradePoint(9) + getICGradePoint(10) + getICGradePoint(11) + getICGradePoint(12)
        //GPA 100
        for (gpa in gpas) {
            total = total + getPointGPA(gpa)
        }

        //Honor 100
        var honorsPercent = getHonroScorePercent()
        total += getHonorsGradePoint(9) + getHonorsGradePoint(10) + getHonorsGradePoint(11) + getHonorsGradePoint(
            12
        )

        //Test 100
        val testPercent = getTestsScore()*3
        total += testPercent
        total /= 1500f

        return (total*100).toInt()
    }


    /**取得全學年百分比*/
    fun getGpaScorePercent():Int {
        var total = 0f
        for (gpa in gpas) {
            total = total + getPointGPA(gpa)
        }

        return (total / 4f).toInt()
    }

    /**取得全學年百分比*/
    fun getGpaScoreTotal():Int {
        var total = 0f
        for (gpa in gpas) {
            total = total + getPointGPA(gpa)
        }
        return total.toInt()
    }
    /**取得全學年百分比*/
    fun getGpaScoreTotalAverge():Float {
        var total = 0f
        for (gpa in gpas) {
            val grade = gpa.grade.ifEmpty { "0" }
            total += try { grade.toFloat() } catch (_: Exception) { 0f }
        }
        return (total/4f)
    }

    fun getPointGPA(academicYear: AcademicYear): Int {
        if (academicYear.grade.isEmpty()) {
            return 0
        }
        return when (academicYear.grade.toFloat()) {
            in 4.5..5.0 -> 100
            in 4.2..4.499 -> 90
            in 4.0..4.199 -> 80
            in 3.8..3.999 -> 70
            in 3.5..3.799 -> 60
            in 3.0..3.499 -> 50
            in 2.7..2.999 -> 40
            in 2.3..2.699 -> 30
            in 2.0..2.299 -> 20
            in 1.0..1.999 -> 5
            else -> {
                0
            }
        }
    }

    fun getScoreAverageGap(academicYear: AcademicYear): Float {
        var point = getPointGPA(academicYear).toFloat()
        return (point / 300f) * 20f
    }

    fun getTestsScore(): Int {
        var sat_act_highest = 0
        var apPoint = 0
        var toefl_ielts_highest = 0

        val selectedApAttempts = Test.selectedApAttempts(tests)
        for (test in tests.filter { it.genre != Test.GenreInfo.AP.value() }) {
            val point = getPointTest(test)
            when (test.genre) {
                Test.GenreInfo.SAT.value(), Test.GenreInfo.ACT.value() -> {
                    if (point > sat_act_highest) {
                        sat_act_highest = point
                    }
                }
                Test.GenreInfo.IB.value() -> {
                    apPoint = apPoint + point
                }
                Test.GenreInfo.TOEFL.value(), Test.GenreInfo.IELTS.value(),
                Test.GenreInfo.TOEFL_ALTERNATE.value(), Test.GenreInfo.DUOLINGO.value() -> {
                    if (point > toefl_ielts_highest) {
                        toefl_ielts_highest = point
                    }
                }
            }
        }

        selectedApAttempts.forEach { apPoint += getPointTest(it) }

        if (apPoint > 30) {
            apPoint = 30
        }

        if (isRequireLanguageExam){

            return sat_act_highest + apPoint + toefl_ielts_highest
        }else{

            return ((sat_act_highest + apPoint) * 10) / 7
        }

    }

    internal fun getPointTest(test: Test): Int {
        val numericScore = test.score?.toFloatOrNull() ?: return 0
        when (test.genre) {
            Test.GenreInfo.SAT.value() -> {
                when (numericScore.toInt()) {
                    in 1570..1600 -> return 35
                    in 1500..1560 -> return 33
                    in 1450..1490 -> return 30
                    in 1400..1440 -> return 27
                    in 1330..1390 -> return 23
                    in 1280..1320 -> return 19
                    in 1200..1270 -> return 15
                    in 1100..1190 -> return 11
                    in 1000..1090 -> return 6
                    else -> {
                        return 2
                    }
                }
            }
            Test.GenreInfo.ACT.value() -> {
                when (numericScore.toInt()) {
                    36 -> return 35
                    35 -> return 33
                    in 33..34 -> return 30
                    in 31..32 -> return 27
                    in 28..30 -> return 23
                    in 25..27 -> return 19
                    in 21..24 -> return 15
                    in 15..20 -> return 11
                    in 10..14 -> return 6
                    else -> {
                        return 2
                    }
                }
            }
            Test.GenreInfo.AP.value() -> {
                when (numericScore.toInt()) {
                    5 -> return 3
                    4 -> return 2
                    3 -> return 1

                    else -> {
                        return 0
                    }
                }
            }
            Test.GenreInfo.TOEFL.value() -> {
                when (numericScore.toInt()) {
                    120 -> return 35
                    in 110..119 -> return 33
                    in 100..109 -> return 30
                    in 90..99 -> return 25
                    in 80..89 -> return 20
                    in 70..79 -> return 10
                    else -> {
                        return 0
                    }
                }
            }
            Test.GenreInfo.IELTS.value() -> {
                when (numericScore) {
                    9.0f -> return 35
                    8.5f -> return 33
                    8.0f -> return 33
                    7.5f -> return 30
                    7.0f -> return 25
                    6.5f -> return 20
                    6.0f -> return 10
                    else -> {
                        return 0
                    }
                }
            }
            Test.GenreInfo.TOEFL_ALTERNATE.value() -> return when {
                numericScore >= 6f -> 35
                numericScore >= 5.5f -> 33
                numericScore >= 5f -> 30
                numericScore >= 4.5f -> 25
                numericScore >= 4f -> 20
                numericScore >= 3f -> 10
                else -> 0
            }
            Test.GenreInfo.IB.value() -> return when (numericScore.toInt()) {
                7 -> 3
                6 -> 2
                5 -> 1
                else -> 0
            }
            Test.GenreInfo.DUOLINGO.value() -> return when {
                numericScore >= 155f -> 35
                numericScore >= 140f -> 33
                numericScore >= 125f -> 30
                numericScore >= 110f -> 25
                numericScore >= 95f -> 20
                numericScore >= 85f -> 10
                else -> 0
            }
        }
        return 0
    }
    /**取得全學年百分比*/
    fun getHonroScorePercent():Int{
        val point9 = getHonorsGradePoint(9)
        val point10 = getHonorsGradePoint(10)
        val point11 = getHonorsGradePoint(11)
        val point12 = getHonorsGradePoint(12)
        val total = point9+point10+point11+point12
        return total/4
    }
    /**取得當前年級分分數*/
    private fun getHonorsGradePoint(grade:Int):Int{
        return getHonorsPoint(honors.stream().filter { it.grade.equals("|$grade|") }.collect(Collectors.toList()) as ArrayList<Honors>)
    }

    private fun getHonorsPoint(honors:ArrayList<Honors>):Int{
        var competitionPoint = 0
        var certificatePoint = 0
        var researchPoint = 0
        for (honor in honors){
            var point = getPointHonor(honor)
            when (honor.type) {
                1 -> {
                    competitionPoint = competitionPoint + point
                }
                2 -> {
                    certificatePoint = certificatePoint + point
                }
                3 -> {
                    researchPoint = researchPoint + point
                }
            }
        }
        if (competitionPoint>40){
            competitionPoint = 40
        }
        if (competitionPoint>30){
            competitionPoint = 30
        }
        if (researchPoint>30){
            researchPoint = 30
        }
        return competitionPoint +certificatePoint + researchPoint
    }

    private fun getPointHonor(honor: Honors): Int {
        when (honor.type) {
            1 -> {
                //Competition
                when(honor.rank){
                    1,2,3->{
                        //前三名
                        when(honor.level){
                            1->{
                                //School
                                return 1
                            }
                            2->{
                                //State/Regional
                                return 5
                            }
                            3->{
                                //National
                                return 10
                            }
                            4->{
                                //International
                                return 15
                            }
                        }
                    }
                    4->{
                        //沒有名次
                        return 1
                    }
                }
            }
            2 -> {
                //Certificate
               return 10
            }
            3 -> {
                //Research
                when(honor.researchType){
                    1->{
                        //SCI/SSCI
                        return 30
                    }
                    2->{
                        //International Journal
                        return 20
                    }
                    3->{
                        //International Conference
                        return 15
                    }
                    4->{
                        //Student Journal
                        return 3
                    }
                    5->{
                        //Self Research
                        return 1
                    }
                }
            }

        }
        return 0
    }

    /**取得全學年百分比*/
    fun getCIScorePercent():Int{
        val point9 = getCIGradePoint(9)
        val point10 = getCIGradePoint(10)
        val point11 = getCIGradePoint(11)
        val point12 = getCIGradePoint(12)
        val total = point9+point10+point11+point12
        return (total.toFloat()/4f).toInt()
    }

    /**取得年級成績 包含連續年度*/
    private fun getCIGradePoint(grade: Int):Int{
        var communityServicePoint = 0
        for (communityImpact in communityImpacts.stream().filter { it.grade!!.contains(grade.toString()) }.collect(Collectors.toList())){
            val point = getPointCI(communityImpact)
            communityServicePoint = communityServicePoint + point
            if (checkCIIsEachWithConsecutive(communityImpact,grade)){
                //如果有連續兩年 再加2分
                communityServicePoint = communityServicePoint + 2
            }
        }

        if (communityServicePoint>30){
            communityServicePoint = 30
        }
        return communityServicePoint

    }

    //確認是否連續
    private fun checkCIIsEachWithConsecutive(communityImpact: CommunityImpact,currentGrade: Int):Boolean{
        var isEachWithConsecutive = false
        val previousGrade = currentGrade -1
        if (communityImpact.grade!!.contains(previousGrade.toString())){
            return  true
        }
        //判斷名稱相同且型態相同但不是同一筆資料
        var list  = communityImpacts.stream().filter {
            (it.name.equals(communityImpact.name))&&(it.type == communityImpact.type &&(it.id!=communityImpact.id) )
        }.collect(Collectors.toList())
        var previousList = list.stream().filter{it.grade!!.contains(previousGrade.toString())}.collect(Collectors.toList())

        if (previousList.size>0){
            isEachWithConsecutive = true
        }

        return isEachWithConsecutive
    }

    fun getPointCI(communityImpact: CommunityImpact):Int{
                //1=Community service
                when(communityImpact.type){
                    1->{
                        //1:International
                        if (communityImpact.role == 1){
                            return (8+2)
                        }else{
                            return 8
                        }
                    }
                    2->{
                        //2:Non International
                        if (communityImpact.role == 1){
                            return (5+2)
                        }else{
                            return 5
                        }
                    }
                }
        return 0
    }

    /**取得全學年百分比*/
    fun getICScorePercent():Int{
        val point9 = getICGradePoint(9)
        val point10 = getICGradePoint(10)
        val point11 = getICGradePoint(11)
        val point12 = getICGradePoint(12)
        val total = point9+point10+point11+point12
        return total/4
    }

    /**取得年級成績 包含連續年度*/
    private fun getICGradePoint(grade: Int):Int{
        var tag = "getICGradePoint"
        //testPrint(tag,"Start $grade 年級")
        var internshipPoint = 0
        var schoolClubPoint = 0
        var gradeList = intellectualCuriosities.stream().filter { it.grade!!.contains(grade.toString()) }.collect(Collectors.toList())
        //testPrint(tag,"${gradeList.size}  筆數")
        for (intellectualCuriosity in gradeList){
            //testPrint(tag,"${intellectualCuriosity.title} type 是 ${intellectualCuriosity.type}")
            var point = getPointIC(intellectualCuriosity)
            when(intellectualCuriosity.type){
                5->{
                    //Internship
                    //testPrint(tag,"實習分數  $internshipPoint  + $point 分")
                    internshipPoint = internshipPoint + point
                    if (checkICIsWithConsecutive(intellectualCuriosity,grade)){
                        //以前曾經參與過 再加10分
                        //testPrint(tag,"實習分數  $internshipPoint 分 +10分")
                        internshipPoint = internshipPoint +10

                    }
                }
                0,1,2,3,4,6->{
                    //School Club
                    //testPrint(tag,"社團分數  $schoolClubPoint  + $point 分")
                   schoolClubPoint = schoolClubPoint + point
                    if (checkICIsEachWithConsecutive(intellectualCuriosity,grade)){
                        //前一年有參與過 再加2分
                        //testPrint(tag,"社團分數  $schoolClubPoint  + 2分")
                        schoolClubPoint = schoolClubPoint +2

                    }
                }
            }
        }
        if (schoolClubPoint> 40){
            schoolClubPoint = 40
        }
        if (internshipPoint>30){
            internshipPoint = 30
        }
        //testPrint(tag,"實習分數  $internshipPoint  和  社團分數$schoolClubPoint ")
        //testPrint(tag,"END $grade 年級")
        return schoolClubPoint + internshipPoint
    }

    /**取得年級實習成績 包含連續年度*/
    private fun getInternshipGradePoint(grade: Int):Int{
        var tag = "getICGradePoint"
        //testPrint(tag,"Start $grade 年級")
        var internshipPoint = 0
        val gradeList = intellectualCuriosities.stream().filter { it.grade!!.contains(grade.toString()) }.collect(Collectors.toList())
        //testPrint(tag,"${gradeList.size}  筆數")
        for (intellectualCuriosity in gradeList){
            //testPrint(tag,"${intellectualCuriosity.title} type 是 ${intellectualCuriosity.type}")
            var point = getPointIC(intellectualCuriosity)
            when(intellectualCuriosity.type){
                5->{
                    //Internship
                    //testPrint(tag,"實習分數  $internshipPoint  + $point 分")
                    internshipPoint = internshipPoint + point
                    if (checkICIsWithConsecutive(intellectualCuriosity,grade)){
                        //以前曾經參與過 再加10分
                        //testPrint(tag,"實習分數  $internshipPoint 分 +10分")
                        internshipPoint = internshipPoint +10

                    }
                }
                0,1,2,3,4,6->{
                }
            }
        }
        if (internshipPoint>30){
            internshipPoint = 30
        }
        //testPrint(tag,"實習分數  $internshipPoint  和  社團分數$schoolClubPoint ")
        //testPrint(tag,"END $grade 年級")
        return internshipPoint

    }

    /**取得年級校園社團成績 包含連續年度*/
    private fun getSchoolClubGradePoint(grade: Int):Int{
        var tag = "getICGradePoint"
        //testPrint(tag,"Start $grade 年級")
        var schoolClubPoint = 0
        val gradeList = intellectualCuriosities.stream().filter { it.grade!!.contains(grade.toString()) }.collect(Collectors.toList())
        //testPrint(tag,"${gradeList.size}  筆數")
        for (intellectualCuriosity in gradeList){
            //testPrint(tag,"${intellectualCuriosity.title} type 是 ${intellectualCuriosity.type}")
            val point = getPointIC(intellectualCuriosity)
            when(intellectualCuriosity.type){
                5->{
                }
                0,1,2,3,4,6->{
                    //School Club
                    //testPrint(tag,"社團分數  $schoolClubPoint  + $point 分")
                    schoolClubPoint = schoolClubPoint + point
                    if (checkICIsEachWithConsecutive(intellectualCuriosity,grade)){
                        //前一年有參與過 再加2分
                        //testPrint(tag,"社團分數  $schoolClubPoint  + 2分")
                        schoolClubPoint = schoolClubPoint +2

                    }
                }
            }
        }
        if (schoolClubPoint> 40){
            schoolClubPoint = 40
        }
        //testPrint(tag,"實習分數  $internshipPoint  和  社團分數$schoolClubPoint ")
        //testPrint(tag,"END $grade 年級")
        return schoolClubPoint
    }

    //確認是否連續前一年有參加
    private fun checkICIsEachWithConsecutive(intellectualCuriosity: IntellectualCuriosity,currentGrade: Int):Boolean{
        var isEachWithConsecutive = false
        var previousGrade = currentGrade -1
        if (intellectualCuriosity.grade!!.contains(previousGrade.toString())){
            return  true
        }
        //判斷名稱相同且型態相同但不是同一筆資料
        var list  = intellectualCuriosities.stream().filter {
            (it.name.equals(intellectualCuriosity.name))&&(it.type!!.equals(intellectualCuriosity.type)&&(it.id!=intellectualCuriosity.id) )
        }.collect(Collectors.toList())
        var previousList = list.stream().filter{it.grade!!.contains(previousGrade.toString())}.collect(Collectors.toList())
        if (previousList.size>0){
            isEachWithConsecutive = true
        }
        return isEachWithConsecutive
    }
    //確認是否以前有參與過(不用連續)
    private fun checkICIsWithConsecutive(intellectualCuriosity: IntellectualCuriosity,currentGrade: Int):Boolean{
        //如果前三年有參加過
        return checkICIsEachWithConsecutive(intellectualCuriosity,currentGrade)||checkICIsEachWithConsecutive(intellectualCuriosity,currentGrade-1)||checkICIsEachWithConsecutive(intellectualCuriosity,currentGrade-2)
    }

    private fun getPointIC(intellectualCuriosity: IntellectualCuriosity):Int{
        when(intellectualCuriosity.type){
            0,1,2,3,4,6->{
                //12=School Club
                when(intellectualCuriosity.role){
                    1->{
                        //leader
                        return 8
                    }
                    2->{
                        //member
                        return 5
                    }
                }
            }
            5->{
                //11=Internship
                return 15

            }

        }
        return 0
    }


    fun calcuate(targetSchool: TargetSchool){
       calcuateTotal(targetSchool)
//       calcuateAver(targetSchool)
    }

    fun calcuateTotal(targetSchool: TargetSchool){
        var result= 0f
        result += calcuteGradePercent(targetSchool,9)
        result += calcuteGradePercent(targetSchool,10)
        result += calcuteGradePercent(targetSchool,11)
        result += calcuteGradePercent(targetSchool,12)

        if (getConsiderRatio(targetSchool.standardizedTestScores)==0){
            result = result*10f/8f
        }else{
            result += getTestsScore()*.2f
        }

        result = result*.9f

        targetSchool.scoreAlgorithm = result.toInt().toString()
        when(result.toInt()){
            in 80..100 -> targetSchool.difficulty = TargetSchool.DIFFICULTY_SAFETY
            in 60..79 ->targetSchool.difficulty = TargetSchool.DIFFICULTY_TARGET
            in 40..59 ->targetSchool.difficulty = TargetSchool.DIFFICULTY_REACH
            in 0..39 ->targetSchool.difficulty = TargetSchool.DIFFICULTY_FAR
            else -> {
                targetSchool.difficulty = TargetSchool.DIFFICULTY_FAR
            }
        }
//        testPrint("calcuate",targetSchool.schoolName +"  [$result%]")
    }

    fun calcuateAver(targetSchool: TargetSchool){
        //分母
        var denominator =0F+(100 * 4)*getConsiderRatio(targetSchool.gpaRequest)//GPA分屬
        denominator += 100 *3* getConsiderRatio(targetSchool.standardizedTestScores) //一般測驗
        denominator += (30 * 4) * getConsiderRatio(targetSchool.volunteerWork)//志工
        denominator += (30 * 4) * getConsiderRatio(targetSchool.workExperience)//實習經驗
        denominator += (100 * 4) * getConsiderRatio(targetSchool.applicantsInterestLevel)//榮譽經歷
        denominator += (40 * 4) * getConsiderRatio(targetSchool.extracurricularActivities)//社團活動
        //分子
        var numerator =0F+(getGpaScorePercent() * 4)*getConsiderRatio(targetSchool.gpaRequest)//GPA分屬
        numerator +=  getTestsScore() * getConsiderRatio(targetSchool.standardizedTestScores)*3 //一般測驗
        numerator +=  (getCIGradePoint(9)+getCIGradePoint(10)+getCIGradePoint(11)+getCIGradePoint(12))*getConsiderRatio(targetSchool.volunteerWork)//志工
        numerator +=  (getInternshipGradePoint(9)+getInternshipGradePoint(10)+getInternshipGradePoint(11)+getInternshipGradePoint(12))*getConsiderRatio(targetSchool.workExperience)//實習經驗
        numerator +=  (getHonorsGradePoint(9)+getHonorsGradePoint(10)+getHonorsGradePoint(11)+getHonorsGradePoint(12))*getConsiderRatio(targetSchool.applicantsInterestLevel)//榮譽經歷
        numerator +=  (getSchoolClubGradePoint(9)+getSchoolClubGradePoint(10)+getSchoolClubGradePoint(11)+getSchoolClubGradePoint(12))*getConsiderRatio(targetSchool.extracurricularActivities)//社團活動

        var result =((numerator/denominator)*90).toInt()
        targetSchool.scoreAlgorithm = result.toString()
        when(result){
            in 50..100 -> targetSchool.difficulty = TargetSchool.DIFFICULTY_SAFETY
            in 25..49 ->targetSchool.difficulty = TargetSchool.DIFFICULTY_TARGET
            in 10..24 ->targetSchool.difficulty = TargetSchool.DIFFICULTY_REACH
            in 0..9 ->targetSchool.difficulty = TargetSchool.DIFFICULTY_FAR
            else -> {
                targetSchool.difficulty = TargetSchool.DIFFICULTY_FAR
            }
        }
//        testPrint("calcuate",targetSchool.schoolName +" $numerator/$denominator [$result%]")
    }
    fun calcuteGradePercent(targetSchool: TargetSchool,grade:Int):Float{

        //分母
        var denominator =0F+(100)*getConsiderRatio(targetSchool.gpaRequest)//GPA分屬
//        denominator += 100 * getConsiderRatio(targetSchool.standardizedTestScores) //一般測驗
        denominator += 30 * getConsiderRatio(targetSchool.volunteerWork)//志工
        denominator += 30  * getConsiderRatio(targetSchool.workExperience)//實習經驗
        denominator += 100 * getConsiderRatio(targetSchool.applicantsInterestLevel)//榮譽經歷
        denominator += 40 * getConsiderRatio(targetSchool.extracurricularActivities)//社團活動

        //分子
        var numerator = 0F+getGpaScorePercent()*getConsiderRatio(targetSchool.gpaRequest)//GPA分屬
//        numerator +=  getTestsScore() * getConsiderRatio(targetSchool.standardizedTestScores) //一般測驗
        numerator +=  getCIGradePoint(grade)*getConsiderRatio(targetSchool.volunteerWork)//志工
        numerator +=  getInternshipGradePoint(grade)*getConsiderRatio(targetSchool.workExperience)//實習經驗
        numerator +=  getHonorsGradePoint(grade)*getConsiderRatio(targetSchool.applicantsInterestLevel)//榮譽經歷
        numerator +=  getSchoolClubGradePoint(grade)*getConsiderRatio(targetSchool.extracurricularActivities)//社團活動

        var result =((numerator/denominator))*20f
//        targetSchool.scoreAlgorithm = result.toString()
//        testPrint("calcuateGrade:${grade}",targetSchool.schoolName +" $numerator/$denominator [$result%]")
        return result
    }
    fun getConsiderRatio(consider:String?):Int{
        when(consider){
            null-> return 0
            "Very Important"-> return  5
            "Important"-> return  3
            "Considered"-> return  1
            "N/A"-> return  0
            "Not Considered"-> return  0
            else->return 0
        }
    }

}
