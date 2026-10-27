package com.kezhi.shike

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CheckInReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        CheckInStore.addCheckIn(context)
        CheckInWidgetProvider.updateAll(context)
        // Keep feedback brief; widget remains usable without opening the app.
        Toast.makeText(context, "记录成功。每一次选择都算数。", Toast.LENGTH_SHORT).show()
    }
}
