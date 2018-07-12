package com.kanykeinu.babymed.data.source.local.dao

import androidx.room.*
import com.kanykeinu.babymed.data.source.local.entity.Illness
import io.reactivex.Completable
import io.reactivex.Flowable

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
    fun getById(id : Long) : Flowable<Illness>

    @Query("Select * from illness where child_id = :id")
    fun getByChildId(id : Long) : Flowable<List<Illness>>

}
