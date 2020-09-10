package com.example.camera.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.example.camera.R
import com.example.camera.ui.base.interfaces.CallbackClick
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar

open class BaseActivity : AppCompatActivity(){

    override fun onStart() {
        super.onStart()
        getSettingFontToolbar()
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


    protected  open fun bottonIcon(id: Int, callback: CallbackClick){
        getBottonIcon()?.setOnMenuItemClickListener { menuItem ->
            true
        }
    }

    private fun getBottonIcon(): BottomAppBar? {
        return findViewById<BottomAppBar>(R.id.bottomAppBar)
    }
}
