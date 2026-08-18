package com.realtouchapp.admitted.test

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.ActivityTestBinding
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.SchoolSelectBSDailogFragment
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.request.LoginRequest
import com.realtouchapp.admitted.network.request.user.AcademicYearRequest
import com.realtouchapp.admitted.network.result.DeCryptionResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.toJsonEncode
import dagger.hilt.android.AndroidEntryPoint


/*
    作者: RobertChou
    時間: 2023/2/1 下午 10:31
    信箱: didi31139@gmail.com
    說明:
*/
@AndroidEntryPoint
class TestActivity :AppCompatActivity() ,OnClickListener{
    private lateinit var binding: ActivityTestBinding
    private val viewModel by viewModels<TestViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val dividerItemDecoration = DividerItemDecoration(
//            rvMovies.context,
//            LinearLayoutManager.VERTICAL
//        )
//        Log.d("TAG", "STinrg"+roman("III"))

        subscribeUi()
        initailView()

        var acade =AcademicYearRequest("2.2")
        binding.tvResult.setText(acade.toJsonEncode())
        var academicYear = AcademicYear("1",grade="2.2")

        binding.tvResult.setText(AcademicYearRequest(academicYear.grade).toJsonEncode())

    }
    var honors: ArrayList<Honors>? =ArrayList<Honors>()
    var tests: ArrayList<Test>? =ArrayList<Test>()
    var courses: ArrayList<Course>? =ArrayList<Course>()
    var intellectualCuriositys: ArrayList<IntellectualCuriosity>? =ArrayList<IntellectualCuriosity>()
    var communityImpact: ArrayList<CommunityImpact>? =ArrayList<CommunityImpact>()
    private fun subscribeUi() {
        viewModel.token.observe(this) {
            binding.tvResult.text = "Token:"+it
        }

        viewModel.response.observe(this, Observer { result ->

            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when(result.action){
                        NetworkResult.Action.CACHE_USERTARGETSCHHOOL->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                            }
                        }
                        NetworkResult.Action.GET_USERTARGETSCHHOOL->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                            }
                        }
                        NetworkResult.Action.CACHETARGETSCHHOOL_DETAIL_LIST->{
                            result.data?.let { it ->
//                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                                var schoolSelectBSDailogFragment = SchoolSelectBSDailogFragment()
                                schoolSelectBSDailogFragment.targetSchools = it.data as ArrayList<TargetSchool>
                                schoolSelectBSDailogFragment.doneMeathod = {userTargetSchools->
                                       testPrint("Selecteed",userTargetSchools.size.toString())

                                }
                                schoolSelectBSDailogFragment.showOnce(supportFragmentManager)

                            }
                        }
                        NetworkResult.Action.GETTARGETSCHHOOL_DETAIL_LIST,NetworkResult.Action.GETTARGETSCHHOOL_BRIEFT ->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                            }
                        }
                        NetworkResult.Action.GETTARGETSCHHOOL_DETAIL_ID ->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                            }
                        }
                        NetworkResult.Action.GET_USERBASIC->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                            }
                        }
                        NetworkResult.Action.PUT_USERBASIC->{

                        }
                        NetworkResult.Action.CACHE_USERBASIC->{
                            result.data?.let { it ->
                                binding.tvResult.text = "CACHE_USERBASIC:"+it
                            }

                        }
                        NetworkResult.Action.GET_INTELLECTUALCURIOSITY->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                                intellectualCuriositys = ArrayList<IntellectualCuriosity>()
                                intellectualCuriositys!!.addAll(it.data as List<IntellectualCuriosity>)

                            }
                        }

                        NetworkResult.Action.CACHE_INTELLECTUALCURIOSITY->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                                intellectualCuriositys = ArrayList<IntellectualCuriosity>()
                                intellectualCuriositys!!.addAll(it.data as List<IntellectualCuriosity>)

                            }
                        }
                        NetworkResult.Action.GET_COMMUNITYIMPACT->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                                communityImpact = ArrayList<CommunityImpact>()
                                communityImpact!!.addAll(it.data as List<CommunityImpact>)

                            }
                        }

                        NetworkResult.Action.CACHE_COMMUNITYIMPACT->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                                communityImpact = ArrayList<CommunityImpact>()
                                communityImpact!!.addAll(it.data as List<CommunityImpact>)

                            }
                        }
                        NetworkResult.Action.GET_COURSE->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                                courses = ArrayList<Course>()
                                courses!!.addAll(it.data as List<Course>)

                            }
                        }

                        NetworkResult.Action.CACHE_COURSE->{
                            result.data?.let { it ->
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                                courses = ArrayList<Course>()
                                courses!!.addAll(it.data as List<Course>)

                            }
                        }
                        NetworkResult.Action.GET_TESTS->{
                            result.data?.let { it ->
//                                binding.tvResult.text  = (it.data as List<Test>).toString()
                                binding.tvResult.text =GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                                tests = ArrayList<Test>()
                                tests!!.addAll(it.data as List<Test>)

                            }
                        }
                        NetworkResult.Action.CACHE_TESTS->{
                            result.data?.let { it ->
                                binding.tvResult.text  = (it.data as List<Test>).toString()
                                tests = ArrayList<Test>()
                                tests!!.addAll(it.data as List<Test>)
                            }
                        }
                        NetworkResult.Action.GET_HONORS->{
                            result.data?.let { it ->
                                binding.tvResult.text  = (it.data as List<Honors>).toString()
                                honors = ArrayList<Honors>()
                                honors!!.addAll(it.data as List<Honors>)
                            }
                        }
                        NetworkResult.Action.PUT_HONORS->{
                            result.data?.let { it ->
                                viewModel.getHonorscache()
                            }
                        }
                        NetworkResult.Action.CACHE_HONORS->{
                            result.data?.let { it ->
                                binding.tvResult.text  = (it.data as List<Honors>).toString()
                                honors = ArrayList<Honors>()
                                honors!!.addAll(it.data as List<Honors>)
                            }
                        }
                        NetworkResult.Action.DELETE_HONORS->{
                            result.message?.let { message ->
                                var response =  Gson().fromJson(
                                    AdmittedEncrption().deCryption(message),
                                    DeCryptionResponse::class.java)
                                binding.tvResult.text = Gson().toJson(response).toString()
                            }
                            result.data?.let { it ->

                            }
                        }
                        NetworkResult.Action.LOGIN->{
                            result.message?.let { message ->
                                var response =  Gson().fromJson(
                                    AdmittedEncrption().deCryption(message),
                                    DeCryptionResponse::class.java)
                                binding.tvResult.text = Gson().toJson(response).toString()
                            }
                        }
                        NetworkResult.Action.LOGIN->{
                            result.data?.let { it ->
                                binding.tvResult.text  = (it.data as DeCryptionResponse).token
                            }
                        }
                        else -> {
                            result.message?.let { message ->
                                var response =  Gson().fromJson(
                                    AdmittedEncrption().deCryption(message),
                                    DeCryptionResponse::class.java)
                                binding.tvResult.text = Gson().toJson(response).toString()
                            }
                        }
                    }

//                    loading.visibility = View.GONE
                }

                NetworkResult.Status.ERROR -> {
                    if (result.statusCode == AdmittedApi.StatusCode.ERROR_OTTHER.code){
                        result.message?.let{
                            binding.tvResult.text = it
                        }

                    }else{
                        result.message?.let { message ->
                            var response =  Gson().fromJson(
                                AdmittedEncrption().deCryption(message),
                                DeCryptionResponse::class.java)
                            binding.tvResult.text = Gson().toJson(response).toString()
                        }
                    }


                }

                NetworkResult.Status.LOADING -> {
//                    loading.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun initailView(){
        binding.btnPostTargetschool.setOnClickListener(this)
        binding.btnPutTargetschool.setOnClickListener(this)
        binding.btnGetTargetschool.setOnClickListener(this)
        binding.btnDeletelistTargetschool.setOnClickListener(this)
        binding.btnPostTargetschoolId.setOnClickListener(this)
        binding.btnDeleteTargetschoolId.setOnClickListener(this)
        binding.btnCacheUsertargetschool.setOnClickListener(this)


        binding.btnPostApplication.setOnClickListener(this)
        binding.btnGetApplication.setOnClickListener(this)
        binding.btnPutApplication.setOnClickListener(this)
        binding.btnCacheApplication.setOnClickListener(this)

        binding.btnGetTargetschoolBrief.setOnClickListener(this)
        binding.btnGetTargetschoolDeatil.setOnClickListener(this)
        binding.btnGetTargetschoolId.setOnClickListener(this)
        binding.btnCacheTargetschool.setOnClickListener(this)


        binding.btnGetUserinfo.setOnClickListener(this)
        binding.btnPutUserinfo.setOnClickListener(this)
        binding.btnCacheUserinfo.setOnClickListener(this)

        binding.btnLogin.setOnClickListener(this)
        binding.btnCachetoken.setOnClickListener(this)
        binding.btnCache.setOnClickListener(this)
        binding.btnGet.setOnClickListener(this)
        binding.btnPost.setOnClickListener(this)
        binding.btnPut.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_login->{
                viewModel.login(LoginRequest("didi31139@gmail.com","12345678").toJsonEncode())
            }
            R.id.btn_cachetoken->{
                viewModel.getToken()
            }
            R.id.btn_cache->{
//                viewModel.getHonorscache()

//                viewModel.getTestcache()
//                viewModel.getCoursecache()
//                viewModel.getIntellectualCuriositycache()
                viewModel.getCommunityImpactcache()
            }
            R.id.btn_get->{
//                viewModel.getHonors()

//                viewModel.getTest()
//                viewModel.getCourse()
                viewModel.getIntellectualCuriosity()
//                viewModel.getCommunityImpact()
            }
            R.id.btn_post->{
//                var honors=Honors(null,9,9,9,9,"1","1","title","3")
//                viewModel.postHonors(honors)

//                var test = Test(null,null,"December",2023,2,1,"1400",null)
//                viewModel.postTest(test)

//                var course = Course(null,2,"MATH",2023,"20",9,null)
//                viewModel.postCourse(course)

//                var intellectualCuriosity = IntellectualCuriosity(null,"2","|9|","4","20",9,null,"leadertest","testtitle","","")
//                viewModel.postIntellectualCuriosity(intellectualCuriosity)

                var communityImpact = CommunityImpact(null,"2","|9|",4,"20",9,1,1,"testtitle",2,3)
                viewModel.postCommunityImpact(communityImpact)
            }
            R.id.btn_put->{
//                var honorsItem = honors!!.get(0)
//                honorsItem.title = "newPut"
//                viewModel.putHonors(honorsItem)

//                var putItem = tests!!.get(0)
//                putItem.score = "999"
//                viewModel.putTest(putItem)

//                var putItem = courses!!.get(0)
//                putItem.grade = "999"
//                viewModel.putCourse(putItem)
//                var putItem = intellectualCuriositys!!.get(0)
//                putItem.grade = "999"
//                viewModel.putIntellectualCuriosity(putItem)

                var putItem = communityImpact!!.get(0)
                putItem.grade = "999"
                viewModel.putCommunityImpact(putItem)


            }
            R.id.btn_delete->{
//                viewModel.deleteHonors(honors!!.get(0))
//                viewModel.deleteTest(tests!!.get(0))
//                viewModel.deleteCourse(courses!!.get(0))
//                viewModel.deleteIntellectualCuriosity(intellectualCuriositys!!.get(0))
                viewModel.deleteCommunityImpact(communityImpact!!.get(0))
            }
            R.id.btn_get_userinfo->{
                viewModel.getUserInfo()
            }
            R.id.btn_put_userinfo->{
//                viewModel.putUserInfo(null)
            }
            R.id.btn_cache_userinfo->{
                viewModel.getUserInfocache()
            }
            R.id.btn_cache_targetschool->{
                viewModel.cacheTargetSchoolDetail()
            }
            R.id.btn_get_targetschool_deatil->{
                viewModel.getTargetSchoolDetail()
            }
            R.id.btn_get_targetschool_brief->{
                viewModel.getTargetSchoolBriefList()
            }
            R.id.btn_get_targetschool_id->{
                viewModel.getTargetSchoolDetail(binding.etId.text.toString())
            }
            R.id.btn_cache_usertargetschool->{
                viewModel.cacheUserTargetSchool()
            }
            R.id.btn_get_targetschool->{
                viewModel.getUserTargetSchools()
            }

        }
    }
}

