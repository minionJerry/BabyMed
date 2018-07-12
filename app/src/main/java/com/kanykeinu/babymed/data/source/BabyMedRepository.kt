package com.kanykeinu.babymed.data.source

import com.kanykeinu.babymed.data.source.local.dao.ChildDao
import com.kanykeinu.babymed.data.source.local.dao.IllnessDao
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class BabyMedRepository @Inject constructor(val childDao : ChildDao, val illnessDao : IllnessDao) {

    fun getChildrenFromDb() : Observable<List<Child>> {
        return childDao.getAll()
                .toObservable()
    }

    fun insertChildToDb(child : Child): Observable<Unit>? {
        return Observable.fromCallable{ childDao.insert(child)}
    }

    fun getIllnessesFromDb(childId : Long) : Observable<List<Illness>> {
        return illnessDao.getByChildId(childId)
                .toObservable()
    }

    fun insertIllnessToBd (illness: Illness) : Observable<Unit>? {
        return Observable.fromCallable{ illnessDao.insert(illness = illness)}
    }

    fun deleteChildFromDb(child: Child): Observable<Unit>? {
        return Observable.fromCallable{ childDao.delete(child)}
    }

    fun getChildById(id : Long) : Observable<Child> {
        return childDao.getById(id).toObservable()
    }
}