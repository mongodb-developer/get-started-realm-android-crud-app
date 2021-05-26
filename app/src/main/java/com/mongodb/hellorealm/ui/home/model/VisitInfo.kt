package com.mongodb.hellorealm.ui.home.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmField
import java.util.*

open class VisitInfo : RealmObject() {

    @PrimaryKey
    @RealmField("_id")
    var id: String = UUID.randomUUID().toString()

    var visitCount: Int = 0

    var partition: String = ""
}

fun VisitInfo.updateCount(): VisitInfo {
    return apply {
        visitCount++
    }
}