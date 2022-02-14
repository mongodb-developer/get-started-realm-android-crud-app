package com.mongodb.hellosyncrealm.ui.deleteviews

import androidx.lifecycle.*
import com.mongodb.hellosyncrealm.ui.home.model.VisitInfo
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import io.realm.mongodb.SyncConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteViewsViewModels(private val realmApp: App) : ViewModel() {

    private val _visitInfo = MutableLiveData<VisitInfo>()
    val visitInfo: LiveData<Int> = Transformations.map(_visitInfo) {
        it.visitCount
    }

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun deleteViewCount(count: Int) {
        _isLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            val user = realmApp.login(Credentials.anonymous())
            val config = SyncConfiguration.Builder(user, user.identity).build()
            val realm = Realm.open(config)
            val visitInfo = realm.write {
                val info = query(VisitInfo::class).first().find()!!
                info.apply {
                    visitCount = if (visitCount.minus(count) >= 0)
                        visitCount.minus(count)
                    else
                        0
                }
            }

            withContext(Dispatchers.Main) {
                _visitInfo.value = visitInfo
            }
        }
    }
}