package com.mongodb.hellosyncrealm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mongodb.hellosyncrealm.ui.home.model.VisitInfo
import com.mongodb.hellosyncrealm.ui.home.model.updateCount
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

class HomeViewModel(private val realmApp: App) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to Realm"
    }

    private val _visitInfo = MutableLiveData<VisitInfo>()
    val visitInfo: LiveData<Int> = Transformations.map(_visitInfo) {
        it.visitCount
    }

    val text: LiveData<String> = _text

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        updateData()
    }

    private fun updateData() {
        _isLoading.postValue(true)
        fun updateCountToDB(info: VisitInfo, db: Realm) {
            db.executeTransactionAsync {
                it.copyToRealmOrUpdate(info)
                _visitInfo.postValue(info)
                _isLoading.postValue(false)
            }
        }

        fun onUserSuccess(user: User) {
            val config = SyncConfiguration.Builder(user, user.id).build()

            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    var visitInfo = realm.where(VisitInfo::class.java).findFirst()
                    visitInfo = if (visitInfo != null) {
                        realm.copyFromRealm(visitInfo).updateCount()
                    } else {
                        VisitInfo().apply {
                            partition = user.id
                            visitCount++
                        }
                    }

                    updateCountToDB(visitInfo, realm)
                }

                override fun onError(exception: Throwable) {
                    super.onError(exception)
                    //TODO: Implementation pending
                    _isLoading.postValue(false)
                }
            })
        }

        realmApp.loginAsync(Credentials.anonymous()) {
            if (it.isSuccess) {
                onUserSuccess(it.get())
            } else {
                _isLoading.postValue(false)
            }
        }
    }

    fun onRefreshCount() {
        _isLoading.postValue(true)

        fun getUpdatedCount(realm: Realm) {
            val visitInfo = realm.where(VisitInfo::class.java).findFirst()
            visitInfo?.let {
                _visitInfo.value = it
                _isLoading.postValue(false)
            }
        }

        fun onUserSuccess(user: User) {
            val config = SyncConfiguration.Builder(user, user.id).build()

            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    getUpdatedCount(realm)
                }

                override fun onError(exception: Throwable) {
                    super.onError(exception)
                    //TODO: Implementation pending
                    _isLoading.postValue(false)
                }
            })
        }

        realmApp.loginAsync(Credentials.anonymous()) {
            if (it.isSuccess) {
                onUserSuccess(it.get())
            } else {
                _isLoading.postValue(false)
            }
        }
    }
}