package com.example.camera.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.camera.R
import com.example.camera.async.base.BaseSelect
import com.example.camera.model.User
import com.example.camera.ui.base.BaseActivity
import com.example.camera.ui.base.interfaces.CallbackClick
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        Select(this, arrayListOf(), resources.getString(R.string.class_user))
            .execute()

        logout()

        showNavigationIcon(R.drawable.ic_action_left_arrow, object : CallbackClick {
            override fun onClick() {
                finish()
            }
        })
    }

    inner class Select(context: Context, list: ArrayList<User>, model:String):
            BaseSelect<User>(context, list, model){
        override fun onPostExecute(result: List<User>?) {
            super.onPostExecute(result)
            if(result?.isNotEmpty() == true){
                Picasso.with(context)
                    .load(result?.get(0).icon)
                    .into(imageProfile)
                title_profile.text = "${result?.get(0).name}"
                email_profile.text = "${result?.get(0).email}"
            }
        }
    }
}
