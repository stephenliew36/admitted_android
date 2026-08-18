package com.realtouchapp.admitted.main.roadmmap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.common.algorithm.ScoreAlgorithm
import com.realtouchapp.admitted.common.parity.GoogleProfileName
import com.realtouchapp.admitted.common.parity.GoogleProfileNameMapper
import com.realtouchapp.admitted.common.parity.SchoolParityRules
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.database.dao.KeyValueDao
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.stream.Collectors
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/2/13 上午 01:06
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory,
    val keyValueDao: KeyValueDao,
    private val  admittedDataStore: AdmittedDataStore
): ViewModel() {

    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response: LiveData<NetworkResult<CommonResponse>>
        get() = _response
//    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
//    val response: LiveData<NetworkResult<CommonResponse>>
//        get() = _response

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo = _userInfo
    private var scoreAlgorithmData = ScoreAlgorithm()
    private val _scoreAlgorithm = MutableLiveData<ScoreAlgorithm>()
    val scoreAlgorithm = _scoreAlgorithm

    private val _responseIntellectualCuriosity = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseIntellectualCuriosity = _responseIntellectualCuriosity

    private val _responseCommunityImpact = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseCommunityImpact = _responseCommunityImpact

    private val _responseAcademicYear = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseAcademicYear = _responseAcademicYear

    private val _academicYears = MutableLiveData<ArrayList<AcademicYear>>()
    val academicYears = _academicYears


    private val _responsetestHonors = MutableLiveData<NetworkResult<CommonResponse>>()
    val responsetestHonors = _responsetestHonors

    private val _responseCourse = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseCourse = _responseCourse

    private val _responsetestTest = MutableLiveData<NetworkResult<CommonResponse>>()
    val responsetestTest = _responsetestTest

    private val _responseUserTargetSchools = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseUserTargetSchools = _responseUserTargetSchools

    private val _targetSchools = MutableLiveData<ArrayList<TargetSchool>>()
    val targetSchools = _targetSchools

    private val _userTargetSchools = MutableLiveData<ArrayList<UserTargetSchool>>()
    val userTargetSchools = _userTargetSchools

    private val _applicationSchools = MutableLiveData<ArrayList<TargetSchool>>()
    val applicationSchools = _applicationSchools

    // 「家長」的當前選擇的學生名稱
    private val _currentStudentName = MutableStateFlow("")
    val currentStudentName: StateFlow<String>
        get() = _currentStudentName.asStateFlow()

    //取得申請中的學校
    fun cacheApplicationSchools() {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getApplicationSchools()?.let { schools ->
                val list = schools.mapNotNull { it.schoolId }

                val targetSchools =
                    admittedReposritory.targetSchoolDao.getEnabledFromIds(list)
                val hashMapA: HashMap<Int, TargetSchool> = HashMap()
                targetSchools!!.forEach { item ->
                    //計算分數
                    scoreAlgorithmData.calcuate(item)
                    hashMapA.put(item.schoolId!!, item)
                }
                schools.forEach { applicationSchool ->
                    hashMapA[applicationSchool.schoolId]?.let {
                        it.applyingStatus = applicationSchool.applyingStatus
                    }
                }
                viewModelScope.launch {
                    applicationSchools.value = targetSchools as ArrayList<TargetSchool>?
                }
            }
        }
    }


    fun cacheTargeSchools() {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.targetSchoolDao.getAllEnabled()?.let { schools ->
                viewModelScope.launch {
                    schools.forEach {
                        scoreAlgorithmData.calcuate(it)
                    }
                    targetSchools.value = schools as ArrayList<TargetSchool>
                }
            }

        }
    }

    fun cacheUserTargeSchools() {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getUserTargetschools()?.let { schools ->
                viewModelScope.launch {
                    userTargetSchools.value = schools as ArrayList<UserTargetSchool>
                }
            }

        }
    }


    fun initialScoreAlgorithn() {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getUserInfo()?.let {
                viewModelScope.launch {
                    if (it.isRequireLanguageExam.equals("1")){

                        scoreAlgorithmData.isRequireLanguageExam = true
                    }else{

                        scoreAlgorithmData.isRequireLanguageExam = false
                    }
                    scoreAlgorithm.value = scoreAlgorithmData
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getAcademicYears()?.let {
                viewModelScope.launch {
                    scoreAlgorithmData.gpas = it
                    scoreAlgorithm.value = scoreAlgorithmData
                }
            }
        }
        viewModelScope.launch {
            admittedReposritory.getFlowIntellectualCuriosityCached().collect {
                it!!.data!!.data?.let {
                    scoreAlgorithmData.intellectualCuriosities =
                        it as ArrayList<IntellectualCuriosity>
                    scoreAlgorithm.value = scoreAlgorithmData
                }
            }
        }
        viewModelScope.launch {
            admittedReposritory.getFlowCommunityImpactCached().collect {
                it!!.data!!.data?.let {
                    scoreAlgorithmData.communityImpacts = it as ArrayList<CommunityImpact>
                    scoreAlgorithm.value = scoreAlgorithmData
                }
            }
        }
        viewModelScope.launch {
            admittedReposritory.getFlowHonorsCached().collect {
                it!!.data!!.data?.let {
                    scoreAlgorithmData.honors = it as ArrayList<Honors>
                    scoreAlgorithm.value = scoreAlgorithmData
                }
            }
        }
        viewModelScope.launch {
            admittedReposritory.getFlowTestCached().collect {
                it!!.data!!.data?.let {
                    scoreAlgorithmData.tests = it as ArrayList<Test>
                    scoreAlgorithm.value = scoreAlgorithmData
                }
            }
        }
    }

    fun updateScoreAlgorithmGPA(academicYears: ArrayList<AcademicYear>) {
        scoreAlgorithmData.gpas = academicYears
        scoreAlgorithm.value = scoreAlgorithmData
    }

    fun updateScoreAlgorithmIC(intellectualCuriosities: ArrayList<IntellectualCuriosity>) {
        scoreAlgorithmData.intellectualCuriosities = intellectualCuriosities
        scoreAlgorithm.value = scoreAlgorithmData
    }

    fun updateScoreAlgorithmCI(communityImpacts: ArrayList<CommunityImpact>) {
        scoreAlgorithmData.communityImpacts = communityImpacts
        scoreAlgorithm.value = scoreAlgorithmData
    }

    fun updateScoreAlgorithmHonor(honorss: ArrayList<Honors>) {
        scoreAlgorithmData.honors = honorss
        scoreAlgorithm.value = scoreAlgorithmData
    }

    fun updateScoreAlgorithmTest(tests: ArrayList<Test>) {
        scoreAlgorithmData.tests = tests
        scoreAlgorithm.value = scoreAlgorithmData
    }

    fun getUserInfoCache() {
        viewModelScope.launch(Dispatchers.IO) {
            val userType = _userType.value
            val isStudent = (userType == UserType.PARENT || userType == UserType.TEACHER)

            admittedReposritory.cacheUserBasic(isStudent)?.let {
                val filled = applyPendingGoogleName(it)
                viewModelScope.launch {
                    userInfo.value = filled
                }
            }
        }
    }

    fun mergePendingGoogleName(user: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val filled = applyPendingGoogleName(user)
            viewModelScope.launch {
                userInfo.value = filled
            }
        }
    }

    private suspend fun applyPendingGoogleName(user: UserInfo): UserInfo {
        val pendingFirst = admittedDataStore.getValue(AdmittedDataStore.KEY_PENDING_GOOGLE_FIRST_NAME).first()
        val pendingLast = admittedDataStore.getValue(AdmittedDataStore.KEY_PENDING_GOOGLE_LAST_NAME).first()
        if (pendingFirst.isBlank() && pendingLast.isBlank()) return user
        val filled = GoogleProfileNameMapper.fillMissing(
            user.firstName,
            user.lastName,
            GoogleProfileName(
                pendingFirst.ifBlank { null },
                pendingLast.ifBlank { null },
                null
            )
        )
        if (filled.firstName != user.firstName || filled.lastName != user.lastName) {
            user.firstName = filled.firstName
            user.lastName = filled.lastName
            admittedReposritory.keyValueDao.saveUserInfo(user)
        }
        return user
    }

    fun getUserBasic() {

        viewModelScope.launch {
            val userType = _userType.value
            val isStudent = (userType == UserType.PARENT || userType == UserType.TEACHER)

            admittedReposritory.getUserBasic(isStudent).collect {
                testPrint(TAG = "onDone", msg = "admittedReposritory.putUserBasic(userInfo).collec")
                _response.value = it

//                when (it?.status) {
//                    NetworkResult.Status.SUCCESS,
//                    NetworkResult.Status.ERROR -> {
//                        loadSelectedStudentName()
//                    }
//                    else -> { }
//                }
            }
        }
    }


    fun putUserBasic(userInfo: UserInfo) {
        viewModelScope.launch {
            testPrint(TAG = "onDone", msg = "ViewmodelputUserBasic")
            admittedReposritory.putUserBasic(userInfo).collect {
                testPrint(TAG = "onDone", msg = "admittedReposritory.putUserBasic(userInfo).collec")
                _response.value = it
                if (it!!.status == NetworkResult.Status.SUCCESS) {
                    viewModelScope.launch(Dispatchers.IO) {
                        admittedReposritory.keyValueDao.saveUserInfo(userInfo)
                    }

                }
            }
        }
    }

    fun getUserTargetSchools() {
        viewModelScope.launch {
            admittedReposritory.getUserTargetSchool().collect {
                responseUserTargetSchools.value = it
            }
        }
    }

    fun deleteHonors(honors: Honors) {

        viewModelScope.launch {
            admittedReposritory.deleteHonors(honors).collect {
                responsetestHonors.value = it
            }
        }
    }

    fun postHonors(honors: Honors) {

        viewModelScope.launch {
            admittedReposritory.postHonors(honors).collect {
                responsetestHonors.value = it
            }
        }
    }

    fun putHonors(honors: Honors) {

        viewModelScope.launch {
            admittedReposritory.putHonors(honors).collect {
                responsetestHonors.value = it
            }
        }
    }

    fun getHonorscache() {

        viewModelScope.launch {
            admittedReposritory.getFlowHonorsCached().collect {
                responsetestHonors.value = it
            }
        }
    }

    fun getHonors() {

        viewModelScope.launch {
            admittedReposritory.getHonors().collect {
                responsetestHonors.value = it
            }
        }
    }

    fun deleteTest(test: Test) {

        viewModelScope.launch {
            admittedReposritory.deleteTest(test).collect {
                _response.value = it
            }
        }
    }

    fun deleteTests(tests: ArrayList<Test>) {
        for (test in tests) {
            viewModelScope.launch {
                admittedReposritory.deleteTest(test).collect {
                    _response.value = it
                }
            }
        }

    }

    fun postTest(test: Test) {

        viewModelScope.launch {
            admittedReposritory.postTest(test).collect {
                _responsetestTest.value = it
            }
        }
    }

    fun putTest(test: Test) {

        viewModelScope.launch {
            admittedReposritory.putTest(test).collect {
                _responsetestTest.value = it
            }
        }
    }

    fun getTestcache() {

        viewModelScope.launch {
            admittedReposritory.getFlowTestCached().collect {
                _responsetestTest.value = it
            }
        }
    }

    fun getTest() {

        viewModelScope.launch {
            admittedReposritory.getTest().collect {
                _responsetestTest.value = it
            }
        }
    }

    fun deleteCourse(course: Course) {

        viewModelScope.launch {
            admittedReposritory.deleteCourse(course).collect {
                _responseCourse.value = it
            }
        }
    }

    fun postCourse(course: Course) {

        viewModelScope.launch {
            admittedReposritory.postCourse(course).collect {
                _responseCourse.value = it
            }
        }
    }

    fun putCourse(course: Course) {

        viewModelScope.launch {
            admittedReposritory.putCourse(course).collect {
                _responseCourse.value = it
            }
        }
    }

    fun getCoursecache() {

        viewModelScope.launch {
            admittedReposritory.getFlowCourseCached().collect {
                _responseCourse.value = it
            }
        }
    }

    fun getCourse() {

        viewModelScope.launch {
            admittedReposritory.getCourse().collect {
                _responseCourse.value = it
            }
        }
    }

    fun postIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity) {

        viewModelScope.launch {
            admittedReposritory.postIntellectualCuriosity(intellectualCuriosity).collect {
                _responseIntellectualCuriosity.value = it
            }
        }
    }

    fun putIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity) {

        viewModelScope.launch {
            admittedReposritory.putIntellectualCuriosity(intellectualCuriosity).collect {
                _responseIntellectualCuriosity.value = it
            }
        }
    }

    fun getIntellectualCuriositycache() {

        viewModelScope.launch {
            admittedReposritory.getFlowIntellectualCuriosityCached().collect {
                _responseIntellectualCuriosity.value = it
            }
        }
    }

    fun deleteIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity) {

        viewModelScope.launch {
            admittedReposritory.deleteIntellectualCuriosity(intellectualCuriosity).collect {
                _responseIntellectualCuriosity.value = it
            }
        }
    }

    fun getIntellectualCuriosity() {

        viewModelScope.launch {
            admittedReposritory.getIntellectualCuriosity().collect {
                _responseIntellectualCuriosity.value = it
            }
        }
    }

    fun putAcademicYear(academicYear: AcademicYear) {
        viewModelScope.launch {
            admittedReposritory.putAcademicYear(academicYear).collect {

                if (it!!.status == NetworkResult.Status.SUCCESS) {
                    viewModelScope.launch(Dispatchers.IO) {
                        admittedReposritory.keyValueDao.saveAcademicYear(academicYear)
                    }
                }
                responseAcademicYear.value = it
            }
        }
    }

    fun getAcademicYear() {

        viewModelScope.launch {
            admittedReposritory.getAcademicYear().collect {
                responseAcademicYear.value = it
            }
        }
    }

    fun getAcademicYearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getAcademicYears()?.let {
                viewModelScope.launch {
                    academicYears.value = it
                }
            }
        }
    }

    fun deleteAcademicYear(academicYear: AcademicYear) {

        viewModelScope.launch {
            admittedReposritory.deleteAcademicYear(academicYear).collect {
                responseAcademicYear.value = it
            }
        }
    }

    fun postCommunityImpact(communityImpact: CommunityImpact) {
        viewModelScope.launch {
            admittedReposritory.postCommunityImpact(communityImpact).collect {
                responseCommunityImpact.value = it
            }
        }
    }

    fun putCommunityImpact(communityImpact: CommunityImpact) {

        viewModelScope.launch {
            admittedReposritory.putCommunityImpact(communityImpact).collect {
                responseCommunityImpact.value = it
            }
        }
    }

    fun getCommunityImpactcache() {

        viewModelScope.launch {
            admittedReposritory.getFlowCommunityImpactCached().collect {
                responseCommunityImpact.value = it
            }
        }
    }

    fun getCommunityImpact() {

        viewModelScope.launch {
            admittedReposritory.getCommunityImpact().collect {
                responseCommunityImpact.value = it
            }
        }
    }

    fun deleteCommunityImpact(communityImpact: CommunityImpact) {

        viewModelScope.launch {
            admittedReposritory.deleteCommunityImpact(communityImpact).collect {
                responseCommunityImpact.value = it
            }
        }
    }


    private val _categoryTargetSchools =
        MutableLiveData<ArrayList<HashMap<String, ArrayList<TargetSchool>>>>()
    val categoryTargetSchools = _categoryTargetSchools

    fun postUserTargetSchool(userTargetSchool: UserTargetSchool) {
        viewModelScope.launch {
            admittedReposritory.postUserTargetSchool(userTargetSchool).collect {

            }
        }
    }
    fun putUserTargetSchool(userTargetSchools: ArrayList<UserTargetSchool>) {
        viewModelScope.launch {
            admittedReposritory.putUserTargetSchool(userTargetSchools).collect{
                _response.value = it
            }

        }

    }


    fun postApplicationSchool(applicationSchool: ApplicationSchool) {
        viewModelScope.launch {
            admittedReposritory.postApplicationSchool(applicationSchool).collect {

            }
        }
    }

    fun getCategoryTagetSchools(
        keyword: String?,
        tagetSchooFilter: TargetSchooFilter,
        isfav: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var keywordTargetSchools = ArrayList<TargetSchool>()
            if (keyword.isNullOrEmpty()) {
                if (isfav) {
                    val userTargetSchools = admittedReposritory.keyValueDao.getUserTargetschools()
                    userTargetSchools?.let {
                        userTargetSchools.forEach {
                            keywordTargetSchools.add(admittedReposritory.targetSchoolDao.getEnabledById(it.schoolId!!)!!)
                        }
                    }
                } else {
                    keywordTargetSchools = admittedReposritory.targetSchoolDao.getAllEnabled() as ArrayList<TargetSchool>
                }

            } else {
                if (isfav) {
                    var userTargetSchools = admittedReposritory.keyValueDao.getUserTargetschools()
                    userTargetSchools?.let {
                        var tmpKeywordTargetSchools = ArrayList<TargetSchool>()
                        userTargetSchools.forEach {
                            tmpKeywordTargetSchools.add(
                                admittedReposritory.targetSchoolDao.getEnabledById(
                                    it.schoolId!!
                                )!!
                            )
                        }
                        if (tmpKeywordTargetSchools.size > 0) {
                            keywordTargetSchools = tmpKeywordTargetSchools.stream()
                                .filter { it.schoolName!!.contains(keyword, ignoreCase = true) }
                                .collect(Collectors.toList()) as ArrayList<TargetSchool>
                        }
                    }

                } else {
                    keywordTargetSchools =
                        admittedReposritory.targetSchoolDao.getAllEnabled(keyword) as ArrayList<TargetSchool>
                }

            }

            //計算分數和上榜機率
            keywordTargetSchools.forEach {
                scoreAlgorithmData.calcuate(it)
            }

            var categories = ArrayList<HashMap<String, ArrayList<TargetSchool>>>()
            if (tagetSchooFilter.isFilter()) {
//                //過濾中

                var country = ""
                if (tagetSchooFilter.isCountry_UnitedStates){
                    country=country+"|1|"
                }
                if (country.equals("")){
                    country ="|1|"
                }
                // 難度判斷：Far Reach 名單最高優先（固定 6 所學校，與 web 版相同），
                // 其餘依難易度分到 Safety/Target/Reach（REACH 與 FAR 都視為 Reach）
                val noDifficultyChecked = !tagetSchooFilter.isDifficulty_FarReach && !tagetSchooFilter.isDifficulty_Reach
                        && !tagetSchooFilter.isDifficulty_Target && !tagetSchooFilter.isDifficulty_Safety
                val matchDifficulty: (TargetSchool) -> Boolean = { school ->
                    if (noDifficultyChecked) {
                        //沒有勾選當作全部允許
                        true
                    } else if (TargetSchool.isFarReachSchool(school)) {
                        tagetSchooFilter.isDifficulty_FarReach
                    } else {
                        when (school.difficulty) {
                            TargetSchool.DIFFICULTY_SAFETY -> tagetSchooFilter.isDifficulty_Safety
                            TargetSchool.DIFFICULTY_TARGET -> tagetSchooFilter.isDifficulty_Target
                            else -> tagetSchooFilter.isDifficulty_Reach
                        }
                    }
                }
                var isPublic= "|0|1|"
                if ((tagetSchooFilter.isPublic_Private==false && tagetSchooFilter.isPublic_Public==false)||(tagetSchooFilter.isPublic_Private==true && tagetSchooFilter.isPublic_Public==true)){
                    //沒有勾選公私立
                    isPublic="|0|1|"
                }else if (tagetSchooFilter.isPublic_Private){
                    isPublic="|0|"
                }else if (tagetSchooFilter.isPublic_Public){
                    isPublic="|1|"
                }


//                categories.addMap("filter", keywordTargetSchools)
                categories.addMap("filter", collect(keywordTargetSchools, {
                    if (country.contains(it.country!!.toString())&&(matchDifficulty(it))&&(isPublic.contains("|${it.isPublic}|"))){
                        if (tagetSchooFilter.isOthers_Art==false && tagetSchooFilter.isOthers_LiberalArts==false){
                            //沒有勾選文學或是藝術學校
                            true
                        }else if(tagetSchooFilter.isOthers_Art==true){
                            if (it.isArtSchools == 1){
                                true
                            }else{
                                false
                            }
                        }else if(tagetSchooFilter.isOthers_LiberalArts==true){
                            if (it.isLiberalArtsSchools == 1){
                                true
                            }else{
                                false
                            }
                        }else{
                            true
                        }
                    }else{
                        false
                    }
                }))

            } else {
                keywordTargetSchools = ArrayList(SchoolParityRules.exploreSchools(keywordTargetSchools))
                //不在過濾中
//                categories.addMap("Search", keywordTargetSchools)
                if (isFreeVersion.value == false){
                    // Far Reach 名單最高優先（固定 6 所學校：Harvard/Princeton/Yale/MIT/Caltech/Stanford，與 web 版相同），
                    // 其餘依難易度分到 Safety/Target/Reach（REACH 與 FAR 機率皆 < 60%，都視為 Reach）
                    categories.addMap("Target Schools", collect(keywordTargetSchools, { !TargetSchool.isFarReachSchool(it) && it.difficulty.equals(TargetSchool.DIFFICULTY_TARGET) }))
                    categories.addMap("Reach Schools", collect(keywordTargetSchools, { !TargetSchool.isFarReachSchool(it) && (it.difficulty.equals(TargetSchool.DIFFICULTY_REACH) || it.difficulty.equals(TargetSchool.DIFFICULTY_FAR)) }))
                    categories.addMap("Far Reach Schools", collect(keywordTargetSchools, { TargetSchool.isFarReachSchool(it) }))
                    categories.addMap("Safety Schools", collect(keywordTargetSchools, { !TargetSchool.isFarReachSchool(it) && it.difficulty.equals(TargetSchool.DIFFICULTY_SAFETY) }))
                }


                categories.addMap("Schools in United States", collect(keywordTargetSchools, { it.country == CountryCode.USA.code }))
                categories.addMap("Public Schools", collect(keywordTargetSchools, { it.isPublic == 1 }))
                categories.addMap("Private Schools", collect(keywordTargetSchools, { it.isPublic == 0 }))
                categories.addMap("Liberal Schools", collect(keywordTargetSchools, { it.isLiberalArtsSchools == 1 }))
                categories.addMap("Art Schools", collect(keywordTargetSchools, { it.isArtSchools == 1 }))

            }

            viewModelScope.launch {
                categoryTargetSchools.value = categories
            }

        }

    }

    private fun collect(
        targetSchools: ArrayList<TargetSchool>,
        meathod: (target: TargetSchool) -> Boolean
    ): ArrayList<TargetSchool> {
        return targetSchools.stream().filter {
            meathod(it)
        }.collect(Collectors.toList()) as ArrayList<TargetSchool>
    }

    fun deleteUserTargetSchool(userTargetSchool: UserTargetSchool){
        viewModelScope.launch {
            admittedReposritory.deleteUserTargetSchool(userTargetSchool).collect{
                cacheUserTargeSchools()
//                responseUserTargetSchool.value= it
            }
        }
    }

    fun deleteUserApplicationSchool(applicationSchool: ApplicationSchool){
        viewModelScope.launch {
            admittedReposritory.deleteApplicationSchool(applicationSchool).collect{
                cacheUserTargeSchools()
//                responseUserTargetSchool.value= it
            }
        }
    }

    private val _isFreeVersion = MutableStateFlow(false)
    val isFreeVersion: StateFlow<Boolean> = _isFreeVersion

    private val _userType = MutableStateFlow<UserType?>(null)
    val userType: StateFlow<UserType?>
        get() = _userType.asStateFlow()

    init {
        // 加载存储的值
        viewModelScope.launch {
            admittedDataStore.isFreeVersion().collect {
                _isFreeVersion.value = it
            }
        }

        loadUserType()
        loadSelectedStudentName()
    }

    private fun loadUserType() {
        viewModelScope.launch(Dispatchers.IO) {
            val userType = admittedReposritory.keyValueDao.getUserType()
            _userType.emit(UserType.from(userType))
        }
    }

    private fun loadSelectedStudentName() {
        viewModelScope.launch(Dispatchers.IO) {
            val studentName = admittedReposritory.keyValueDao.getStudentName()
            _currentStudentName.emit(studentName ?: "")
        }
    }

    fun getValue(key:String) = admittedDataStore.getValue(key)

    suspend fun saveDataClass(key:String,value:Any) {
        admittedDataStore.saveDataClass(key,value)
    }

}

private fun ArrayList<HashMap<String, ArrayList<TargetSchool>>>.addMap(
    titile: String,
    targetSchools: ArrayList<TargetSchool>
) {
    if (targetSchools.size > 0) {
        val map = HashMap<String, ArrayList<TargetSchool>>()
//        map.put(titile + "(${targetSchools.size})", targetSchools)
        map.put(titile, targetSchools)
        this.add(map)
    }


}