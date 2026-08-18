package com.realtouchapp.admitted.main.roadmmap.ui.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.BeginActivity
import com.realtouchapp.admitted.databinding.ActivityProfileBinding
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.EditArrayItemBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.EditNameBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.EditSchoolBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.LanguageSelectBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.viewmodel.PorfileActivityViewModel
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.UserInfo
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.gone
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() , View.OnClickListener{

    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<PorfileActivityViewModel>()
    private lateinit var userInfo: UserInfo
    companion object{

        fun Intent(activity: AppCompatActivity){
            val intent = android.content.Intent()
                .setClass(activity, ProfileActivity::class.java)
            activity.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        initialViewsSize()
        initalView()
        subscribeUi()
        viewModel.getUserInfo()
        viewModel.loadUserType()
    }

    private fun initialViewsSize() {
        //Back
        binding.ivActivityProfileBack.setViewSizeOrignalWH(24,24)
        binding.tvActivityProfileBack.setTextSize(13)

        binding.tvActivityProfileQrcode.setTextSize(10)

        binding.tvActivityProfilePi.setTextSize(13)

        binding.tvActivityProfileName.setTextSize(15)
        binding.tvActivityProfileNationalityInput.setTextSize(15)

        binding.tvActivityProfileNationality.setTextSize(15)
        binding.tvActivityProfileNationalityInput.setTextSize(15)

        binding.tvActivityProfileSchool.setTextSize(15)
        binding.tvActivityProfileSchoolInput.setTextSize(15)

        binding.tvActivityProfileYear.setTextSize(15)
        binding.tvActivityProfileYearInput.setTextSize(15)

        binding.tvActivityProfileSchoolsystem.setTextSize(15)
        binding.tvActivityProfileSchoolsystemInput.setTextSize(15)

        binding.tvActivityProfileGradingscal.setTextSize(15)
        binding.tvActivityProfileGradingscalInput.setTextSize(15)

        binding.tvActivityProfileSchedule.setTextSize(15)
        binding.tvActivityProfileScheduleInput.setTextSize(15)

        binding.tvActivityProfileLanguage.setTextSize(15)
        binding.tvActivityProfileLanguageInput.setTextSize(15)

        binding.tvActivityProfileIdentity.setTextSize(15)
        binding.tvActivityProfileIdentityInput.setTextSize(15)

        binding.tvActivityProfileAi.setTextSize(13)

        binding.tvActivityProfileEmail.setTextSize(15)
        binding.tvActivityProfileEmailInput.setTextSize(15)

        binding.tvActivityProfilePassword.setTextSize(15)
        binding.tvActivityProfilePasswordInput.setTextSize(15)

        binding.tvActivityProfileAccounttype.setTextSize(15)
        binding.tvActivityProfileAccounttypeInput.setTextSize(15)

        binding.tvActivityProfileAdd.setTextSize(15)
        binding.tvActivityProfileDelete.setTextSize(13)

        binding.tvActivityProfileTerms.setTextSize(15)
        binding.tvActivityProfilePrivacy.setTextSize(15)
        binding.tvActivityProfileLogout.setTextSize(15)
    }

    private fun initalView() {
        binding.llActivityProfileBack.setOnClickListener(this)
        binding.rlActivityProfileAdd.setOnClickListener(this)
        binding.tvActivityProfileNameInput.setOnClickListener(this)
        binding.tvActivityProfileNationalityInput.setOnClickListener(this)
        binding.tvActivityProfileSchoolInput.setOnClickListener(this)
        binding.tvActivityProfileYearInput.setOnClickListener(this)
        binding.tvActivityProfileSchoolsystemInput.setOnClickListener(this)
        binding.tvActivityProfileGradingscalInput.setOnClickListener(this)
        binding.tvActivityProfileScheduleInput.setOnClickListener(this)
        binding.tvActivityProfileLanguageInput.setOnClickListener(this)

        binding.tvActivityProfileQrcode.setOnClickListener(this)

        binding.tvActivityProfileDelete.setOnClickListener(this)
        binding.tvActivityProfileLogout.setOnClickListener(this)
        binding.clActivityProfilePrivacy.setOnClickListener(this)
        binding.clActivityProfileTerms.setOnClickListener(this)

        binding.llProfileInfoStudent.gone()
        binding.llProfileInfoParent.gone()
        lifecycleScope.launch {
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        binding.clActivityProfileQrcode.gone()
                        binding.llProfileInfoParent.visible()
                    }
                    UserType.STUDENT -> {
                        // 學生
                        binding.clActivityProfileQrcode.visible()
                        binding.llProfileInfoStudent.visible()
                    }
                    else -> { }
                }
            }
        }
    }

    private fun subscribeUi() {
        viewModel.userInfo.observe(this) {
            it?.let {
                userInfo = it

            }
        }

        viewModel.response.observe(this) { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    //成功
                    when (result.action) {
                        NetworkResult.Action.PUT_USERBASIC -> {
                            viewModel.getUserInfo()

                        }
                        NetworkResult.Action.GET_USERBASIC -> {
                            result.data?.let {
                                it.data?.let { userData ->
                                    userInfo = userData as UserInfo
                                    binding.tvActivityProfileNameInput.text=userInfo.firstName +" " + userInfo.lastName
                                    binding.tvActivityProfileNationalityInput.text=userData.nationality


                                    binding.tvActivityProfileSchoolInput.text=userData.school
                                    userData.grade?.let {

                                        binding.tvActivityProfileYearInput.text="Grade $it"
                                    }
                                    userData.schoolSystem?.let {
                                        binding.tvActivityProfileSchoolsystemInput.text = resources.getStringArray(R.array.array_school_system).get(if (it == -1) 0 else it)
                                    }
                                    userData.gradingScale?.let {
                                        binding.tvActivityProfileGradingscalInput.text=resources.getStringArray(R.array.array_gradingscale).get(if (it == -1) 0 else it)
                                    }
                                    userData.schedule?.let {
                                        binding.tvActivityProfileScheduleInput.text=resources.getStringArray(R.array.array_schedule).get(if (it == -1) 0 else it)
                                    }
                                    userData.isRequireLanguageExam?.let {
                                        if (it.equals("1")){
                                            binding.tvActivityProfileLanguageInput.text= "Yes"
                                        }else{
                                            binding.tvActivityProfileLanguageInput.text= "No"
                                        }

                                    }
                                    userData.email?.let {
                                        binding.tvActivityProfileEmailInput.text= it
                                    }


                                    binding.tvActivityProfilePasswordInput.text="*******"
                                    userData.subscriptionType?.let {
                                        binding.clActivityProfileAccounttype.visibility = View.VISIBLE
                                        binding.clActivityProfileAccounttype.setOnClickListener {
                                            AccountTypeActivity.Intent(this, userData.subscriptionType!!, userData.expirationDate!!)
                                        }

                                        binding.ivActivityProfileAccounttype.visibility = View.VISIBLE
                                        when(it){
                                            "0"->{
                                                binding.tvActivityProfileAccounttypeInput.text="Monthly"
                                            }
                                            "1"->{
                                                binding.tvActivityProfileAccounttypeInput.text="Quarterly"
                                            }
                                            "2"->{
                                                binding.tvActivityProfileAccounttypeInput.text="Yearly"
                                            }
                                            else->{
                                                binding.tvActivityProfileAccounttypeInput.text=""
                                            }
                                        }
                                    }?:run{
                                        binding.tvActivityProfileAccounttypeInput.text=""
                                    }

                                }

                            }
                        }
                        NetworkResult.Action.PUT_USERBASIC -> {
                        }
                        else -> {


                        }
                    }
                }
                NetworkResult.Status.ERROR -> {
                    //失敗
                    if (result.statusCode == AdmittedApi.StatusCode.ERROR_OTTHER.code) {
                        //本地端失敗(網路未開啟等等)
                        result.message?.let {
                            showSnackBar(binding.root, it)
                        }
                    } else {
                        //失敗
                        result.error!!.message?.let { message ->
                            showSnackBar(binding.root, deCryption(message).msgDesc)
                        }
                    }
                }
                NetworkResult.Status.LOADING -> {
                    //載入中 可顯示載入動畫
                }
            }

        }
        viewModel.islogout.observe(this){
            if (it){
                viewModelStore.clear()
                val intent = android.content.Intent().setClass(this@ProfileActivity, BeginActivity::class.java)
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_activity_profile_back -> {
                finish()
            }
            R.id.tv_activity_profile_qrcode -> {
                MyQrcodeActivity.intent(this)
            }
            R.id.rl_activity_profile_add -> {

            }

            R.id.tv_activity_profile_name_input -> {
                showEditNameBSDailogFragment()
            }
            R.id.tv_activity_profile_nationality_input -> {
                showEditArrayItemBSDailogFragment(ItemType.National)
            }

            R.id.tv_activity_profile_School_input -> {
                showEditSchoolBSDailogFragment()
            }
            R.id.tv_activity_profile_year_input -> {
                showEditArrayItemBSDailogFragment(ItemType.Year)
            }
            R.id.tv_activity_profile_schoolsystem_input -> {
                showEditArrayItemBSDailogFragment(ItemType.SChoolSystem)
            }
            R.id.tv_activity_profile_gradingscal_input -> {
                showEditArrayItemBSDailogFragment(ItemType.GradingScal)
            }
            R.id.tv_activity_profile_schedule_input -> {
                showEditArrayItemBSDailogFragment(ItemType.Schedule)
            }
            R.id.tv_activity_profile_language_input -> {
                showLanguageSelectBSDailogFragment()
            }
            R.id.tv_activity_profile_delete -> {
            }
            R.id.cl_activity_profile_terms -> {
                startActivity(
                    android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        Uri.parse("${AdmittedApi.SERVER_ADDRESS}/termsConditions?lang=en")
                    )
                )

            }
            R.id.cl_activity_profile_privacy -> {
                startActivity(
                    android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        Uri.parse("${AdmittedApi.SERVER_ADDRESS}/privacy?lang=en")
                    )

                )
            }
            R.id.tv_activity_profile_logout -> {
                viewModel.logout(this@ProfileActivity)
            }
        }
    }
    var editSchoolBSDailogFragment:EditSchoolBSDailogFragment?=null
    fun showEditSchoolBSDailogFragment(){
        if (editSchoolBSDailogFragment == null){
            editSchoolBSDailogFragment = EditSchoolBSDailogFragment()
            editSchoolBSDailogFragment!!.school = userInfo.school!!
            editSchoolBSDailogFragment!!.doneMeathod={ school->
                userInfo.school = school
                viewModel.putUserBasic(userInfo)
            }
            editSchoolBSDailogFragment!!.show(supportFragmentManager,"EditSchoolBSDailogFragment")
        }else{
            if (!editSchoolBSDailogFragment!!.isVisible){

                editSchoolBSDailogFragment!!.dismiss()
                supportFragmentManager.beginTransaction().remove(editSchoolBSDailogFragment!!).commit()
                editSchoolBSDailogFragment = null
                showEditSchoolBSDailogFragment()
            }

        }

    }

    var editNameBSDailogFragment: EditNameBSDailogFragment?=null
    fun showEditNameBSDailogFragment(){
        if (editNameBSDailogFragment == null){
            editNameBSDailogFragment = EditNameBSDailogFragment()
            editNameBSDailogFragment!!.firstName = userInfo.firstName!!
            editNameBSDailogFragment!!.lastName = userInfo.lastName!!
            editNameBSDailogFragment!!.doneMeathod={fistName,lastName->
                userInfo.firstName = fistName
                userInfo.lastName = lastName
                viewModel.putUserBasic(userInfo)
            }
            editNameBSDailogFragment!!.show(supportFragmentManager,"EditNameBSDailogFragment")
        }else{
            if (!editNameBSDailogFragment!!.isVisible){
                editNameBSDailogFragment!!.dismiss()
                supportFragmentManager.beginTransaction().remove(editNameBSDailogFragment!!).commit()
                editNameBSDailogFragment = null
                showEditNameBSDailogFragment()
            }
        }
    }

    var languageSelectBSDailogFragment:LanguageSelectBSDailogFragment?=null
    fun showLanguageSelectBSDailogFragment(){
        if (languageSelectBSDailogFragment == null){
            languageSelectBSDailogFragment = LanguageSelectBSDailogFragment()
            if (userInfo.isRequireLanguageExam.equals("1")){
                languageSelectBSDailogFragment!!.isYesSelect = true
            }else{
                languageSelectBSDailogFragment!!.isYesSelect = false
            }
            languageSelectBSDailogFragment!!.onDone={
                if (it){
                    userInfo.isRequireLanguageExam = "1"
                }else{
                    userInfo.isRequireLanguageExam = "0"
                }
                viewModel.putUserBasic(userInfo)
            }

            languageSelectBSDailogFragment!!.show(supportFragmentManager,"LanguageSelectBSDailogFragment")
        }else{
            if (!languageSelectBSDailogFragment!!.isVisible){
                languageSelectBSDailogFragment!!.dismiss()
                supportFragmentManager.beginTransaction().remove(languageSelectBSDailogFragment!!).commit()
                languageSelectBSDailogFragment = null
                showLanguageSelectBSDailogFragment()
            }
        }
    }

    var editArrayItemBSDailogFragment:EditArrayItemBSDailogFragment?=null
    fun showEditArrayItemBSDailogFragment(type:ItemType){
        if (editArrayItemBSDailogFragment == null){
            editArrayItemBSDailogFragment = EditArrayItemBSDailogFragment()
            when(type){
                ItemType.National->{
                    editArrayItemBSDailogFragment!!.arrayid = R.array.nationalities_array
                    editArrayItemBSDailogFragment!!.doneMeathod={ position->
                        userInfo.nationality = resources.getStringArray(R.array.nationalities_array).get(position-1)

                        viewModel.putUserBasic(userInfo)

                    }
                }
                ItemType.Year->{
                    editArrayItemBSDailogFragment!!.arrayid = R.array.grade_levels
                    editArrayItemBSDailogFragment!!.doneMeathod={ position->
                        userInfo.grade = position+8
                        viewModel.putUserBasic(userInfo)
                    }
                }
                ItemType.SChoolSystem->{
                    editArrayItemBSDailogFragment!!.arrayid = R.array.array_school_system
                    editArrayItemBSDailogFragment!!.doneMeathod={ position->
                        userInfo.nationality = (position-1).toString()
                        viewModel.putUserBasic(userInfo)
                    }

                }
                ItemType.GradingScal->{
                    editArrayItemBSDailogFragment!!.arrayid = R.array.array_gradingscale
                    editArrayItemBSDailogFragment!!.doneMeathod={ position->
                        userInfo.gradingScale = (position-1)
                        viewModel.putUserBasic(userInfo)
                    }

                }
                ItemType.Schedule->{
                    editArrayItemBSDailogFragment!!.arrayid = R.array.array_schedule
                    editArrayItemBSDailogFragment!!.doneMeathod={ position->
                        userInfo.schedule = (position-1)
                        viewModel.putUserBasic(userInfo)
                    }

                }
                else->{}
            }

            editArrayItemBSDailogFragment!!.show(supportFragmentManager,"EditArrayItemBSDailogFragment")
        }else{
            if (!editArrayItemBSDailogFragment!!.isVisible){
                editArrayItemBSDailogFragment!!.dismiss()
                supportFragmentManager.beginTransaction().remove(editArrayItemBSDailogFragment!!).commit()
                editArrayItemBSDailogFragment = null
                showEditArrayItemBSDailogFragment(type)
            }
        }
    }
    enum class ItemType{
        National,Year,SChoolSystem,GradingScal,Schedule
    }
}