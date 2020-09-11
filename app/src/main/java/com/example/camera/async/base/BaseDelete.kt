package com.example.camera.async.base

import android.content.Context
import android.os.AsyncTask
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.camera.R
import com.example.camera.database.AppDatabase
import com.example.camera.model.User
import java.util.ArrayList

open class BaseDelete<T>(val context: Context, val list: ArrayList<T>, val model: String):
AsyncTask<T, T, List<T>>(){
    override fun doInBackground(vararg p0: T): List<T> {
        var database = AppDatabase.getInstance(context)
        return when(model){
            context.resources.getString(R.string.class_user)->{
                if(list.isNotEmpty()){
                    for(l in list){
                        var user = l as User
                        var delete = database.roomUses()?.delete(user)
                    }
                }else{
                    database.roomUses()?.all(SimpleSQLiteQuery("DELETE FROM $model")) as List<T>
                }

                database.roomUses()?.all(SimpleSQLiteQuery("SELECT * FROM $model")) as List<T>
            }

            else -> arrayListOf()
        }
    }

}
