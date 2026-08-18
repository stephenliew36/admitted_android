package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentAddtesttBinding
import com.realtouchapp.admitted.ext.closeSoftIntPut
import com.realtouchapp.admitted.member.view.adapter.SpinnerArrayAdapter
import com.realtouchapp.admitted.network.entity.Test
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.utils.DateModular
import com.realtouchapp.tools.utils.UserInterfaceTool

class AddTestFinalBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    companion object{
        const val TAG="AddTestFinalBSDailogFragment"
    }
    lateinit var doneMeathod: ((Test) -> Unit)
    lateinit var editMeathod: ((Test) -> Unit)
    var test: Test? =null
    private var isEditMode = false
    //400~1600
    var satArray=ArrayList<String>()
    //0~36
    var actArray=ArrayList<String>()
    //0~120
    var toeflArray=ArrayList<String>()
    //0~9
    var ieltsArray=ArrayList<String>()
    //1~5
    var apArray=ArrayList<String>()
    var alternateToeflArray=ArrayList<String>()
    var ibArray=ArrayList<String>()
    var duolingoArray=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    private lateinit var binding:BsdialogfragmentAddtesttBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentAddtesttBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intialScoreArray()
        initialViewSize()
        initialView()
    }
    
    override fun onResume() {
        super.onResume()
        setEditModeView()
    }

    private fun initialViewSize(){
        binding.llBsdf.setViewSizeWidthByRatio(.915f)

        binding.ivBsdfAddtestDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfAddtestDivider.setMarginTopOrignal(16)

        binding.tvBsdfAddtestTitle.setTextSize(17)
        binding.tvBsdfAddtestTitle.setMarginTopOrignal(24)

        binding.tvBsdfAddtestName.setTextSize(13)
        binding.tvBsdfAddtestName.setMarginTopRatioHWithoutBar(.021f)
        binding.rlBsdfAddtestName.setMarginTopRatioHWithoutBar(.008f)
        binding.rlBsdfAddtestName.setViewSizeWidthByRatio(.915f)
        binding.rlBsdfAddtestName.setViewSizeByRatioWH(.915f,.065f)
        binding.spBsdfAddtestName.setViewSizeByRatioWH(.915f,.062f)
        binding.etBsdfAddtestName.setViewSizeByRatioWH(.915f,.062f)
        binding.etBsdfAddtestName.setTextSize(13)
        binding.ivBsdfAddtestName.setViewSizeByRatioWH(.027f,.008f)

        binding.tvBsdfAddtestApsubject.setTextSize(13)
        binding.tvBsdfAddtestApsubject.setMarginTopRatioHWithoutBar(.031f)
        binding.rlBsdfAddtestApsubject.setMarginTopRatioHWithoutBar(.008f)
        binding.rlBsdfAddtestApsubject.setViewSizeByRatioWH(.915f,.065f)
        binding.spBsdfAddtestApsubject.setViewSizeByRatioWH(.915f,.062f)
        binding.etBsdfAddtestApsubject.setViewSizeByRatioWH(.915f,.062f)
        binding.etBsdfAddtestApsubject.setTextSize(13)
        binding.ivBsdfAddtestApsubject.setViewSizeByRatioWH(.027f,.008f)

        binding.tvBsdfAddtestDate.setTextSize(13)
        binding.tvBsdfAddtestDate.setMarginTopRatioHWithoutBar(.031f)
        binding.rlBsdfAddtestDateMonth.setMarginTopRatioHWithoutBar(.008f)
        binding.rlBsdfAddtestDateMonth.setViewSizeByRatioWH(.600f,.065f)
        binding.spBsdfAddtestDateMonth.setViewSizeByRatioWH(.600f,.062f)
        binding.etBsdfAddtestDateMonth.setViewSizeByRatioWH(.600f,.062f)
        binding.etBsdfAddtestDateMonth.setTextSize(13)
        binding.ivBsdfAddtestDateMonth.setViewSizeByRatioWH(.027f,.008f)

        binding.rlBsdfAddtestDateYear.setMarginTopRatioHWithoutBar(.008f)
        binding.rlBsdfAddtestDateYear.setViewSizeByRatioWH(.315f,.065f)
        binding.spBsdfAddtestDateYear.setViewSizeByRatioWH(.315f,.062f)
        binding.etBsdfAddtestDateYear.setViewSizeByRatioWH(.315f,.062f)
        binding.etBsdfAddtestDateYear.setTextSize(13)
        binding.ivBsdfAddtestDateYear.setViewSizeByRatioWH(.027f,.008f)

        binding.tvBsdfAddtestScore.setTextSize(13)
        binding.tvBsdfAddtestScore.setMarginTopRatioHWithoutBar(.031f)
        binding.rlBsdfAddtestScore.setMarginTopRatioHWithoutBar(.008f)
        binding.rlBsdfAddtestScore.setViewSizeByRatioWH(.915f,.065f)
        binding.etBsdfAddtestScore.setViewSizeByRatioWH(.915f,.062f)
        binding.etBsdfAddtestScore.setTextSize(13)
        binding.etBsdfAddtestScoreMax.setTextSize(13)

        binding.btnBsdfAddtestDone.setMarginTopRatioHWithoutBar(.031f)
        binding.btnBsdfAddtestDone.setMarginBottomRatioHWithoutBar(.031f)
    }

    private fun initialView(){


        val arrayList = resources.getStringArray(R.array.array_addtest_type).toList()

        binding.etBsdfAddtestName.addTextChangedListener {

            val result = it.toString()
            Test.GenreInfo.fromValue(arrayList.indexOf(result).takeIf { index -> index >= 0 }?.plus(1))
                ?.let(::configureGenre)
            setFirstEditScore()
            checkEnableDonne()
        }
        setSpinner(binding.etBsdfAddtestName,binding.spBsdfAddtestName,R.array.array_addtest_type)
        setSpinner(binding.etBsdfAddtestApsubject,binding.spBsdfAddtestApsubject,R.array.array_apsubjectt)

        var month = ArrayList<String>()
        month.addAll(resources.getStringArray(R.array.array_month))
        setSpinner(binding.etBsdfAddtestDateMonth,binding.spBsdfAddtestDateMonth,month,.58f)

        var year = DateModular.getCurrentYear()
        var years =ArrayList<String>()
        for (i in 0 .. 10){
            years.add((year-i).toString())
        }
        setSpinner(binding.etBsdfAddtestDateYear,binding.spBsdfAddtestDateYear,years,.3f)

        binding.etBsdfAddtestApsubject.addTextChangedListener {
            checkEnableDonne()
        }
        binding.etBsdfAddtestDateMonth.addTextChangedListener {
            checkEnableDonne()
        }
        binding.etBsdfAddtestDateYear.addTextChangedListener {
            if (!it!!.toString().equals("")){
                test!!.year = it.toString().toInt()
                checkEnableDonne()
            }
        }

        binding.etBsdfAddtestScore.addTextChangedListener {
//            test!!.score = it.toString()
            checkEnableDonne()
        }

        binding.btnBsdfAddtestDone.setOnClickListener (this)

    }

    private fun setEditModeView(){
        if (test == null){
            isEditMode =false
            //代表是新增
            test =Test(null,null,null,null,null,null,null,null)
        }else{
            isEditMode = true
        }

        if (!isEditMode){
            binding.tvBsdfAddtestTitle.setText(R.string.tv_bsdf_addtest_add)

            binding.etBsdfAddtestName.setText("")
            binding.etBsdfAddtestApsubject.setText("")
            binding.etBsdfAddtestDateMonth.setText("")
            binding.etBsdfAddtestDateYear.setText("")
            binding.etBsdfAddtestScore.setText("")
            binding.spBsdfAddtestName.setSelection(0)
            binding.spBsdfAddtestDateMonth.setSelection(0)
            binding.spBsdfAddtestDateYear.setSelection(0)
            binding.spBsdfAddtestScore.setSelection(0)
            binding.spBsdfAddtestApsubject.setSelection(0)
            binding.btnBsdfAddtestDone.setText(R.string.btn_bsdf_addciactivity_add)
        }else{

            binding.tvBsdfAddtestTitle.setText(R.string.tv_bsdf_addtest_edit)
            test!!.genre?.let { genre ->
                binding.spBsdfAddtestName.setSelection(genre)
                Test.GenreInfo.fromValue(genre)?.let(::configureGenre)
            }

            test!!.apSubject?.let {
                binding.etBsdfAddtestApsubject.setText(it)
            }
            test!!.month?.let {
                binding.etBsdfAddtestDateMonth.setText(it)
            }
            test!!.year?.let {
                binding.etBsdfAddtestDateYear.setText(it.toString())
            }
            binding.etBsdfAddtestScore.text =  test!!.score?:""
            tmpScore = test !!.score?:""
        }
        binding.btnBsdfAddtestDone.setText(R.string.btn_bsdf_addciactivity_done)
    }
    //第一次編輯的時候用來站存分數，避免觸發選擇分類的時候沒有分數顯示
    var tmpScore = ""
    private fun setFirstEditScore(){
        if (!tmpScore.equals("")){
            binding.etBsdfAddtestScore.text =  test!!.score?:""
            tmpScore = ""
        }
    }
    private fun  checkEnableDonne(){
        if(binding.etBsdfAddtestName.text.isEmpty()||binding.etBsdfAddtestDateMonth.text.isEmpty()||binding.etBsdfAddtestDateYear.text.isEmpty()||binding.etBsdfAddtestScore.text.isEmpty()){
            enableTestNext(false)
        }else if (test!!.requiresSubject() && binding.etBsdfAddtestApsubject.text.isEmpty()){
            enableTestNext(false)
        }else{
            enableTestNext(true)
        }
    }
    private fun enableTestNext(isEnable :Boolean){
        if (isEnable) {
            binding.btnBsdfAddtestDone.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBsdfAddtestDone.setOnClickListener (this)
        }else{
            binding.btnBsdfAddtestDone.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBsdfAddtestDone.setOnClickListener(null)
        }
    }
    fun setSpinner(tv: AppCompatTextView, sp: AppCompatSpinner, array:ArrayList<String>,spwidth:Float){
        var tmppArrayList=array
        tmppArrayList.add(0,"")
        val spinnerArrayAdapter= SpinnerArrayAdapter(tv.context,tmppArrayList.toList())
        spinnerArrayAdapter.sp_width = spwidth
        sp.adapter = spinnerArrayAdapter

        sp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, iew: View?, pos: Int, id: Long) {
                    if (!tmppArrayList[pos].equals("")){
                        tv.text = tmppArrayList[pos]
                    }
                    closeSoftIntPut(tv)

                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    closeSoftIntPut(tv)
                }
            }
        sp.dropDownVerticalOffset = UserInterfaceTool().getScreenHeightWithoutBarByRatio(tv.context,0.068f)
        //設定初始值不為0 才能觸發按相同位置
        sp.setSelection(-1,false)
        tv.text = ""
    }

    private fun intialScoreArray(){
        //400~1600
        satArray=ArrayList<String>()
        for (i in 120 downTo 0){
            satArray.add((400+i*10).toString())
        }
        //0~36
        actArray=ArrayList<String>()
        for (i in 36 downTo 0){
            actArray.add(i.toString())
        }
        //0~120
        toeflArray=ArrayList<String>()
        for (i in 120 downTo 0){
            toeflArray.add(i.toString())
        }
        //0~9
        ieltsArray=ArrayList<String>()
        for (i in 18 downTo 0){
            ieltsArray.add((i*0.5f).toString())
        }
        //1~5
        apArray=ArrayList<String>()
        for (i in 1.. 5){
            apArray.add((i).toString())
        }
        for (i in 2..12) alternateToeflArray.add((i * 0.5f).toString())
        for (i in 1..7) ibArray.add(i.toString())
        for (i in 2..32) duolingoArray.add((i * 5).toString())
    }

    private fun configureGenre(info: Test.GenreInfo) {
        if (test == null) {
            test = Test(null,null,null,null,null,null,null,null)
        }
        test!!.genre = info.value()
        val showSubject = info.requiresSubject()
        binding.tvBsdfAddtestApsubject.visibility = if (showSubject) View.VISIBLE else View.GONE
        binding.rlBsdfAddtestApsubject.visibility = if (showSubject) View.VISIBLE else View.GONE
        if (showSubject) {
            bindSubjectSpinner(info)
        } else {
            test!!.apSubject = null
            binding.etBsdfAddtestApsubject.text = ""
        }
        val scores = when (info) {
            Test.GenreInfo.SAT -> satArray
            Test.GenreInfo.ACT -> actArray
            Test.GenreInfo.TOEFL -> toeflArray
            Test.GenreInfo.IELTS -> ieltsArray
            Test.GenreInfo.AP -> apArray
            Test.GenreInfo.TOEFL_ALTERNATE -> alternateToeflArray
            Test.GenreInfo.IB -> ibArray
            Test.GenreInfo.DUOLINGO -> duolingoArray
        }
        binding.etBsdfAddtestScoreMax.setText(info.scoreSuffix())
        setSpinner(binding.etBsdfAddtestScore, binding.spBsdfAddtestScore, ArrayList(scores))
    }

    private fun bindSubjectSpinner(info: Test.GenreInfo) {
        val arrayRes = if (info == Test.GenreInfo.IB) R.array.array_ibsubject else R.array.array_apsubjectt
        setSpinner(binding.etBsdfAddtestApsubject, binding.spBsdfAddtestApsubject, arrayRes)
    }


    private fun setSpinner(tv: AppCompatTextView, sp: AppCompatSpinner, arrayRes:Int){
        var tmppArrayList=ArrayList<String>()
        tmppArrayList.add("")
        tmppArrayList.addAll(resources.getStringArray(arrayRes))
        val spinnerArrayAdapter= SpinnerArrayAdapter(tv.context,tmppArrayList.toList())
        sp.adapter = spinnerArrayAdapter
        sp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent:AdapterView<*>?, view: View?, pos: Int, id: Long) {
                    if (!tmppArrayList[pos].equals("")){
                        tv.text = tmppArrayList[pos]
                    }
                    closeSoftIntPut(tv)

                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    closeSoftIntPut(tv)
                }
            }
        sp.dropDownVerticalOffset = UserInterfaceTool().getScreenHeightWithoutBarByRatio(tv.context,0.068f)
        //設定初始值不為0 才能觸發按相同位置
        sp.setSelection(-1,false)
        tv.text = ""
    }
    fun setSpinner(tv: AppCompatTextView, sp: AppCompatSpinner, array:ArrayList<String>){
        var tmppArrayList=array
        tmppArrayList.add(0,"")
        val spinnerArrayAdapter= SpinnerArrayAdapter(tv.context,tmppArrayList.toList())
        sp.adapter = spinnerArrayAdapter

        sp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, iew: View?, pos: Int, id: Long) {
                    if (!tmppArrayList[pos].equals("")){
                        tv.text = tmppArrayList[pos]
                    }
                    closeSoftIntPut(tv)

                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    closeSoftIntPut(tv)
                }
            }
        sp.dropDownVerticalOffset = UserInterfaceTool().getScreenHeightWithoutBarByRatio(tv.context,0.068f)
        //設定初始值不為0 才能觸發按相同位置
        sp.setSelection(-1,false)
        tv.text = ""
    }
    fun collectData(){
        test!!.apSubject = binding.etBsdfAddtestApsubject.text.toString()
        test!!.month = binding.etBsdfAddtestDateMonth.text.toString()
        test!!.year = binding.etBsdfAddtestDateYear.text.toString().toInt()
        test!!.score = binding.etBsdfAddtestScore.text.toString()

    }
    
    override fun onClick(view: View) {
        when(view.id){
            R.id.btn_bsdf_addtest_done->{
                collectData()
                if (isEditMode){
                    editMeathod(test!!)
                }else{
                    doneMeathod(test!!)

                }
                dismiss()
            }

        }
    }
}


