package com.mongodb.hellosyncrealm.ui.deleteviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mongodb.hellosyncrealm.ui.home.model.VisitInfo
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

class DeleteViewsViewModels(private val realmApp: App) : ViewModel() {

    private val _visitInfo = MutableLiveData<VisitInfo>()
    val visitInfo: LiveData<Int> = Transformations.map(_visitInfo) {
        it.visitCount
    }

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun deleteViewCount(count: Int) {
        _isLoading.postValue(true)

        fun onUserSuccess(user: User) {
            val config = SyncConfiguration.Builder(user, user.id).build()

            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    val visitInfo = realm.where(VisitInfo::class.java).findFirst()
                    if (visitInfo != null) {
                        realm.beginTransaction()
                        visitInfo.apply {
                            visitCount = if (visitCount.minus(count) >= 0)
                                visitCount.minus(count)
                            else
                                0
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