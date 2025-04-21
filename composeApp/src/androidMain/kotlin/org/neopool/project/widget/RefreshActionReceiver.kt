package org.neopool.project.widget

import android.content.Context
import android.content.Intent

class RefreshActionReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NeoPoolRewardTrackerWorker.requestUpdate(context)
    }
}