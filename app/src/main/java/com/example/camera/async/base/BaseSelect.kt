package com.example.camera.async.base

import android.content.Context
import android.os.AsyncTask
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.camera.R
import com.example.camera.database.AppDatabase
import java.util.ArrayList

open class BaseSelect<T>(val context: Context, val list: ArrayList<T>, val model: String) :
    AsyncTask<T, T, List<T>>(){
    override fun doInBackground(vararg p0: T): List<T> {
        var database = AppDatabase.getInstance(context)
        var query = "SELECT * FROM $model"
        return when(model){
            context.resources.getString(R.string.class_user)-> database.roomUses()?.all(SimpleSQLiteQuery(query)) as List<T>
            else -> arrayListOf()
        }
    }
}
