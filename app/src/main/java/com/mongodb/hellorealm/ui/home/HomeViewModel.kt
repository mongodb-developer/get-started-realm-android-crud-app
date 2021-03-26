package com.mongodb.hellorealm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mongodb.hellorealm.ui.home.model.VisitInfo
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

    fun readData() {
        val visitInfo = db.where(VisitInfo::class.java).findAll()
        if (visitInfo.isEmpty()) {
            db.executeTransactionAsync {
                val info = VisitInfo().apply {
                    visitCount = 1
                }
                it.insert(info)
                _visitInfo.postValue(info)
            }
        } else {

            db.beginTransaction()
            visitInfo.first()?.apply {
                _visitInfo.postValue(this)
                visitCount++
            }
            db.commitTransaction()
        }
    }

    override fun onCleared() {
        super.onCleared()
        db.close()
    }
}