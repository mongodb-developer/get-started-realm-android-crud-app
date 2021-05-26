package com.mongodb.hellorealm.ui.updateviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mongodb.hellorealm.ui.home.model.VisitInfo
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

class UpdateViewsViewModel(private val realmApp: App) : ViewModel() {

    private val _visitInfo = MutableLiveData<VisitInfo>()
    val visitInfo: LiveData<Int> = Transformations.map(_visitInfo) {
        it.visitCount
    }

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun updateViewCount(count: Int) {

        _isLoading.postValue(true)

        fun onUserSuccess(user: User) {
            val config = SyncConfiguration.Builder(user, user.id).build()

            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    val visitInfo = realm.where(VisitInfo::class.java).findFirst()
                    if (visitInfo == null) {
                        realm.executeTransactionAsync {
                            val info = VisitInfo().apply {
                                visitCount = count
                            }
                            _visitInfo.postValue(info)
                            it.insert(info)

                        }
                    } else {
                        realm.beginTransaction()
                        visitInfo.apply {
                            visitCount += count
                            _visitInfo.postValue(this)
                        }

                        realm.commitTransaction()
                    }
                    _isLoading.postValue(false)
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