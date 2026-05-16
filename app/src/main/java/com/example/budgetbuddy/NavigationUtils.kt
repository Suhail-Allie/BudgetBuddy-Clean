package com.example.budgetbuddy

import android.app.Activity
import android.content.Intent

object NavigationUtils {

    fun openScreen(activity: Activity, intent: Intent) {
        activity.startActivity(intent)
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun closeScreen(activity: Activity) {
        activity.finish()
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}