package com.mongodb.hellorealm.ui.addviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mongodb.hellorealm.ui.home.model.VisitInfo
import com.mongodb.hellorealm.ui.home.model.updateCount
import io.realm.Realm

class AddViewsViewModel : ViewModel() {

    private val db = Realm.getDefaultInstance()

    private val _visitInfo = MutableLiveData<VisitInfo>()
    val visitInfo: LiveData<Int> = Transformations.map(_visitInfo) {
        it.visitCount
    }

    fun addViewCount(count: Int) {
        val visitInfo = db.where(VisitInfo::class.java).findFirst()
        if (visitInfo == null) {
            db.executeTransactionAsync {
                val info = VisitInfo().apply {
                    visitCount = count
                }
                _visitInfo.postValue(info)
                it.insert(info)

            }
        } else {

            db.beginTransaction()
            visitInfo.apply {
                visitCount += count
                _visitInfo.postValue(this)
            }

            db.commitTransaction()
        }
    }
}