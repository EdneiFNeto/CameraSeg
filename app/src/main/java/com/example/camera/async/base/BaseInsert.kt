package com.example.camera.async.base

import android.content.Context
import android.os.AsyncTask
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.camera.R
import com.example.camera.database.AppDatabase
import com.example.camera.model.User

open class BaseInsert<T>(private val context: Context, private val list: List<T>, private val model: String) :
    AsyncTask<T, T, List<T>>(){
    private var id: Long?=0L

    override fun doInBackground(vararg p0: T): List<T> {

        val query = "SELECT * FROM $model"
        var database = AppDatabase.getInstance(context)

        return when(model){
            context.resources.getString(R.string.class_user)->{
                if(list.isNotEmpty()){
                    for(l in list){
                        var user = l as User
                        id = database?.roomUses()?.insert(user)
                    }
                    database.roomUses()?.all(SimpleSQLiteQuery("SELECT * FROM $model WHERE id = ?", arrayOf(id) ))
                            as List<T>
                }else{
                    database.roomUses()?.all(SimpleSQLiteQuery(query))
                            as List<T>
                }
            }

            else -> arrayListOf()
        }
    }

}
