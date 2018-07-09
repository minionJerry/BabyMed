package com.kanykeinu.babymed.data.source.local.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.kanykeinu.babymed.data.source.local.entity.Child
import io.reactivex.*

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