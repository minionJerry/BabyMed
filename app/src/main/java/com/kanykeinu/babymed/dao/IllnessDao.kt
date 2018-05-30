package com.kanykeinu.babymed.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kanykeinu.babymed.model.Child
import com.kanykeinu.babymed.model.Illness
import io.reactivex.Flowable
import io.reactivex.Observable
import org.intellij.lang.annotations.Flow

@Dao
interface IllnessDao {
    @Query("SELECT * from illness")
    fun getAll(): Flowable<List<Illness>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(illness: Illness)

    @Delete
    fun delete(illness : Illness)

    @Update
    fun update(illness: Illness)

    @Query("Select * from illness where id = :id")
    fun getById(id : Int) : Flowable<Illness>

    @Query("Select * from illness where child_id = :id")
    fun getByChildId(id : Int) : Flowable<Illness>

}
