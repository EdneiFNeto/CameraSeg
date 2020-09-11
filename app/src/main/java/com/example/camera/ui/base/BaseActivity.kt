package com.example.camera.ui.base

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.camera.R
import com.example.camera.async.base.BaseDelete
import com.example.camera.async.base.BaseSelect
import com.example.camera.model.User
import com.example.camera.ui.MainActivity
import com.example.camera.ui.base.interfaces.CallbackClick
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar

open class BaseActivity : AppCompatActivity(){

    protected lateinit var mGoogleSignInClient: GoogleSignInClient
    protected var user :User? = null
    private var TAG = "BaseActivityLog"

    override fun onStart() {
        super.onStart()
        getSettingFontToolbar()
        logout()
    }

    protected  open fun getToolbar(): MaterialToolbar?{
        var toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        if(toolbar!= null)
            return  toolbar
        return null
    }

    protected open fun showNavigationIcon(icon: Int, callback: CallbackClick){
        var toolbar = getToolbar()
        toolbar?.setNavigationIcon(icon)
        toolbar?.setNavigationOnClickListener {
            callback.onClick()
        }
    }

    protected  open fun getSettingFontToolbar(){
        getToolbar()?.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)
    }

    protected  open fun icons(id: Int, visible:Boolean, callback: CallbackClick){
        var menu = getToolbar()?.menu
        var menuItem = menu?.findItem(id)
        menuItem?.isVisible = visible
        menuItem?.setOnMenuItemClickListener {
            callback.onClick()
            true
        }
    }

    fun logout(){
        icons(R.id.item_logout, true, object :CallbackClick{
            override fun onClick() {
                DeleteDatabase(this@BaseActivity, arrayListOf<User>(),
                    resources.getString(R.string.class_user)).execute()
            }
        })
    }

    protected  open fun bottonIcon(id: Int, callback: CallbackClick){
        getBottonIcon()?.setOnMenuItemClickListener { menuItem ->
            true
        }
    }

    private fun getBottonIcon(): BottomAppBar? {
        return findViewById<BottomAppBar>(R.id.bottomAppBar)
    }

    inner class DeleteDatabase(context:Context, list: ArrayList<User>, model:String):
            BaseDelete<User>(context, list, model){
        override fun onPostExecute(result: List<User>?) {
            super.onPostExecute(result)
            Log.e("DeleteDatabase", "Delete $result")
            signOut()
        }
    }

    protected open fun initGoogle(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    protected  open fun signOut() {

        if(mGoogleSignInClient!= null)
            initGoogle()

        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this, OnCompleteListener<Void> {
                Log.i("signOut", "Disconnected")
                finish()
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return true
    }


    inner class SelectUser(context: Context, list: java.util.ArrayList<User>, model:String):
        BaseSelect<User>(context, list, model){

        override fun onPostExecute(result: List<User>?) {
            super.onPostExecute(result)
            Log.i(TAG, "Select user $result")
            if(result!= null && result.isNotEmpty()){
                user = User.helper(result[0])
                var intent = Intent(resources.getString(R.string.action_get_user))
                intent.putExtra(resources.getString(R.string.extra_success), resources.getString(R.string.select_user))
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }
    }

}
