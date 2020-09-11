package com.example.camera.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.camera.R
import com.example.camera.async.base.BaseSelect
import com.example.camera.model.User
import com.example.camera.ui.base.BaseActivity

class SplachActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach)

        Handler().postDelayed(Runnable {
            Select(this, arrayListOf(),
                resources.getString(R.string.class_user)).execute()
        }, 3000)
    }

    inner class Select(context: Context, list: ArrayList<User>, model:String):
        BaseSelect<User>(context, list, model){
        override fun onPostExecute(result: List<User>?) {
            super.onPostExecute(result)
            when(result?.isNotEmpty()){
                true ->startActivity(Intent(context, MainActivity::class.java))
                else ->startActivity(Intent(context, LoginActivity::class.java))
            }
        }
    }
}
