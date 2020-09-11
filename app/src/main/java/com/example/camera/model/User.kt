package com.example.camera.model

import androidx.room.Dao
import androidx.room.PrimaryKey

@Dao
class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
           val name: String?,
           val email: String?) {

}
