package com.kezhi.shike

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CheckInStore {
    private const val PREFS = "kezhi_shike_store"
    private const val KEY_EVENTS = "events"
    private const val KEY_LAST_SUCCESS = "last_success"
    private const val MAX_EVENTS = 5000

    fun addCheckIn(context: Context, at: Long = System.currentTimeMillis()): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val events = readEvents(context).toMutableList()
        events.add(at)
        val bounded = events.takeLast(MAX_EVENTS)
        prefs.edit()
            .putString(KEY_EVENTS, bounded.joinToString(","))
            .putLong(KEY_LAST_SUCCESS, at)
            .apply()
        return bounded.size
    }

    fun readEvents(context: Context): List<Long> {
        val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_EVENTS, "") ?: ""
        if (raw.isBlank()) return emptyList()
        return raw.split(",").mapNotNull { it.toLongOrNull() }.sortedDescending()
    }

    fun total(context: Context): Int = readEvents(context).size

    fun todayCount(context: Context): Int {
        val now = System.currentTimeMillis()
        return readEvents(context).count { isSameLocalDay(it, now) }
    }

    fun formatTime(epochMillis: Long): String {
        return SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date(epochMillis))
    }

    private fun isSameLocalDay(a: Long, b: Long): Boolean {
        val fmt = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return fmt.format(Date(a)) == fmt.format(Date(b))
    }
}
