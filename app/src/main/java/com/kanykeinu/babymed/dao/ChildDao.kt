package com.kanykeinu.babymed.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.kanykeinu.babymed.model.Child
import io.reactivex.*
import io.reactivex.Observable
import java.util.*

@Dao
interface ChildDao {
    @Query("SELECT * from child")
    fun getAll(): Flowable<List<Child>>

    @Insert(onConflict = REPLACE)
    fun insert(child: Child)

    @Delete
    fun delete(child: Child)

    @Update
    fun update(child: Child)

    @Query("Select * from child where id = :id")
    fun getById(id : Int) : Flowable<Child>

    @Query("Select birth_date from child where id = :id")
    fun getBirthDateByChildId(id : Long) : String
}