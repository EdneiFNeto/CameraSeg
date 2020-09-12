package com.example.camera.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.camera.database.dao.RoomUser
import com.example.camera.model.User

@Database(
    entities = [
        User::class
    ], version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun roomUses(): RoomUser?

    companion object {

        private val DB = "db_dev"

        fun getInstance(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, DB)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}