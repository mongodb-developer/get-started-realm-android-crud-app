package com.mongodb.hellorealm.ui.home.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class VisitInfo : RealmObject() {

    @PrimaryKey
    var _id = UUID.randomUUID().toString()

    var visitCount: Int = 0

}

fun VisitInfo.updateCount(): VisitInfo {
    return apply {
        visitCount++
    }
}