package com.example.camera.database.base

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

interface BaseRoom<T> {

    @RawQuery
    fun all(query: SupportSQLiteQuery): List<T>

    @Delete
    fun delete(t: T): Int

    @Update
    fun update(t: T): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T): Long
}
