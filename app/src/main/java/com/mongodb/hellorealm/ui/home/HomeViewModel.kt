package com.mongodb.hellorealm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mongodb.hellorealm.ui.home.model.VisitInfo
import com.mongodb.hellorealm.ui.home.model.updateCount
import io.realm.Realm

class HomeViewModel : ViewModel() {

    private val db = Realm.getDefaultInstance()
    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to Realm"
    }

    private val _visitInfo = MutableLiveData<VisitInfo>()
    val visitInfo: LiveData<Int> = Transformations.map(_visitInfo) {
        it.visitCount
    }

    val text: LiveData<String> = _text

    init {
        updateData()
    }

    private fun updateData() {
        var visitInfo = db.where(VisitInfo::class.java).findFirst()
        visitInfo = visitInfo?.let { db.copyFromRealm(it).updateCount() } ?: VisitInfo().updateCount()
        updateCountToDB(visitInfo)
    }

    fun onRefreshCount() {
        val visitInfo = db.where(VisitInfo::class.java).findFirst()
        visitInfo?.let {
            _visitInfo.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        db.close()
    }

    private fun updateCountToDB(info: VisitInfo) {
        db.executeTransactionAsync {
            it.copyToRealmOrUpdate(info)
            _visitInfo.postValue(info)
        }
    }
}