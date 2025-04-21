package org.neopool.project.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.WorkManager
import org.neopool.project.widget.NeoPoolRewardTrackerWorker.Companion.UNIQUE_WORK_NAME

class NeoPoolRewardTrackerReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NeoPoolRewardWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        NeoPoolRewardTrackerWorker.scheduleUpdates(context)
        NeoPoolRewardTrackerWorker.requestUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(UNIQUE_WORK_NAME)
    }
}