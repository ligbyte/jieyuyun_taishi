package com.stkj.cashier.app.base

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.ViewDataBinding
import com.king.base.util.StringUtils
import com.king.frame.mvvmframe.base.BaseDialogFragment
import com.king.frame.mvvmframe.base.BaseModel
import com.king.frame.mvvmframe.base.BaseViewModel
import com.stkj.cashier.App
import com.stkj.cashier.R
import com.stkj.cashier.app.home.HomeActivity
import com.stkj.cashier.constants.Constants
import es.dmoral.toasty.Toasty

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
abstract class BaseDialogFragment<VM : BaseViewModel<out BaseModel>,VDB : ViewDataBinding> : BaseDialogFragment<VM,VDB>(){

    fun getApp() = requireActivity().application as App

    override fun initData(savedInstanceState: Bundle?) {
        registerMessageEvent {
            showToast(it)
        }

    }

    //-----------------------------------

    fun showToast(@StringRes resId: Int){
        Toasty.normal(requireContext(),resId).show()
    }

    fun showToast(text: CharSequence){
        Toasty.normal(requireContext(),text).show()
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

    fun startActivity(clazz: Class<*>,username: String? = null){
        var intent = newIntent(clazz)
        intent.putExtra(Constants.KEY_USERNAME,username)
        startActivity(intent)
    }

//    fun startLoginActivity(username: String? = null,isCode: Boolean = false,isAlphaAnim: Boolean = false,isClearTask: Boolean = false) {
//        val intent = Intent(context, if (isCode) CodeLoginActivity::class.java else LoginActivity::class.java)
//        intent.putExtra(Constants.KEY_USERNAME, username)
//        intent.putExtra(Constants.KEY_CLEAR_TASK, isClearTask)
//        if (isClearTask) {
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        if(isAlphaAnim){
//            val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(requireContext(), R.anim.alpha_in_anim, R.anim.app_dialog_scale_out)
//            startActivity(intent, optionsCompat.toBundle())
//        }else{
//            startActivity(intent)
//        }
//    }

    fun startHomeActivity(){
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(requireContext(), R.anim.alpha_in_anim, R.anim.alpha_out_anim)
        startActivity(intent, optionsCompat.toBundle())
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

}