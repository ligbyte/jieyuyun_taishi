package com.stkj.cashier.app.base

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.stkj.cashier.util.util.BarUtils
import com.stkj.cashier.util.util.LogUtils
//import com.huayi.hgt.hyznjar.CustomAPI
import com.king.base.util.StringUtils

import com.king.frame.mvvmframe.base.BaseActivity
import com.king.frame.mvvmframe.base.BaseModel
import com.king.frame.mvvmframe.base.BaseViewModel
import com.stkj.cashier.App
import com.stkj.cashier.R
import com.stkj.cashier.app.home.HomeActivity
import com.stkj.cashier.app.main.MainActivity
import com.stkj.cashier.constants.Constants
import es.dmoral.toasty.Toasty

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
abstract class BaseActivity<VM : BaseViewModel<out BaseModel>,VDB : ViewDataBinding> : BaseActivity<VM,VDB>(){

    fun getApp() = application as App

    fun setToolbarTitle(title: String?){
        title?.let {
            viewDataBinding?.root?.findViewById<TextView>(R.id.tvTitle)?.text = it
        }
    }


    override fun initData(savedInstanceState: Bundle?)   {

        registerMessageEvent {
            LogUtils.e("网络消息回调1"+it)
            showToast(it)
            // 触发回调
            callback?.onDataReceived(it)
        }

    }

    override fun onResume() {
        super.onResume()
//        CustomAPI(this).switchNavBar(0)
        BarUtils.setNavBarVisibility(this, false)
    }

    override fun onPause() {
        super.onPause()
//        CustomAPI(this).switchNavBar(1)
        BarUtils.setNavBarVisibility(this, true)
    }
    //-----------------------------------

    fun showToast(@StringRes resId: Int){
        Toasty.normal(context,resId).show()
    }

    fun showToast(text: CharSequence){
        Toasty.normal(context.applicationContext,text).show()
    }

    //-----------------------------------

    fun checkInput(tv: TextView): Boolean {
        return !TextUtils.isEmpty(tv.text)
    }

    fun checkInput(tv: TextView,msgId: Int): Boolean {
        if (TextUtils.isEmpty(tv.text)) {
            if (msgId != 0) {
                showToast(msgId)
            }
            return false
        }
        return true
    }

    fun checkInput(tv: TextView, msg: CharSequence? = null): Boolean {
        if (TextUtils.isEmpty(tv.text)) {
            if (StringUtils.isNotBlank(msg)) {
                showToast(msg!!)
            }
            return false
        }
        return true
    }

    //-----------------------------------

    fun setClickRightClearListener(tv: TextView) {
        tv.setOnTouchListener { v: View?, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_UP -> return@setOnTouchListener clickRightClear(tv, event)
            }
            false
        }
    }

    private fun clickRightClear(tv: TextView, event: MotionEvent): Boolean {
        val drawableRight = tv.compoundDrawables[2]
        if (drawableRight != null && event.rawX >= tv.right - drawableRight.bounds.width()) {
            tv.text = null
            return true
        }
        return false
    }


    fun setClickRightEyeListener(et: EditText) {
        et.setOnTouchListener { v: View?, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val drawableRight = et.compoundDrawables[2]
                    if (drawableRight != null && event.rawX >= et.right - drawableRight.bounds.width()) {
                        clickEye(et)
                        return@setOnTouchListener true
                    }
                    return@setOnTouchListener false
                }
            }
            false
        }
    }

    private fun clickEye(etPassword: EditText) {
        if (etPassword.inputType != InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) { //隐藏密码
            etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            val drawableRight = ContextCompat.getDrawable(context, R.drawable.ic_password_hide)
            val compoundDrawable = etPassword.compoundDrawables
            etPassword.setCompoundDrawablesWithIntrinsicBounds(compoundDrawable[0], compoundDrawable[1], drawableRight, compoundDrawable[3])
        } else { //显示密码
            etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            val drawableRight = ContextCompat.getDrawable(context, R.drawable.ic_password_show)
            val compoundDrawable = etPassword.compoundDrawables
            etPassword.setCompoundDrawablesWithIntrinsicBounds(compoundDrawable[0], compoundDrawable[1], drawableRight, compoundDrawable[3])
        }
    }


    //-----------------------------------

    fun startActivity(clazz: Class<*>,username: String? = null){
        var intent = newIntent(clazz)
        intent.putExtra(Constants.KEY_USERNAME,username)
        startActivity(intent)
    }

    fun startHomeActivity(){
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.alpha_in_anim, R.anim.alpha_out_anim)
        startActivity(intent, optionsCompat.toBundle())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun startMainActivity(){
        val intent = Intent(context, MainActivity::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and  Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NO_ANIMATION

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        val options: ActivityOptions = ActivityOptions.makeBasic()

        var mDisplayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager;
        //得到显示器数组
        var displays = mDisplayManager.displays

        if (displays.size>1){
            options.setLaunchDisplayId(  displays[1].displayId)
            LogUtils.e("startMainActivity"+displays[1].toString())
            LogUtils.e("startMainActivity"+displays[0].toString())
        }
//        val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.alpha_in_anim, R.anim.alpha_out_anim)
        startActivity(intent,options.toBundle())
    }
    fun startWebActivity(url: String,title: String? = null){
        var intent = Intent(context, WebActivity::class.java)
        title?.let {
            intent.putExtra(Constants.KEY_TITLE,it)
        }
        intent.putExtra(Constants.KEY_URL,url)
        startActivity(intent)
    }

    //-----------------------------------


    open fun onClick(v : View){
        if(v.id == R.id.ivLeft){
            finish()
        }
    }

    // 定义一个回调接口
    interface MyCallback {
        fun onDataReceived(data: String)
        fun onError(error: Exception)
    }
    // 声明接口变量
    private var callback: MyCallback? = null

    // 设置回调接口
    fun setCallback(callback: MyCallback) {
        this.callback = callback
    }



}