package com.kezhi.shike

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private val ink = Color.rgb(28, 40, 58)
    private val muted = Color.rgb(108, 121, 138)
    private val green = Color.rgb(25, 143, 102)
    private val paper = Color.rgb(247, 249, 250)
    private lateinit var root: LinearLayout
    private lateinit var todayValue: TextView
    private lateinit var totalValue: TextView
    private lateinit var recordsBox: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = paper
        window.navigationBarColor = paper
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        buildUi()
        refresh()
    }

    private fun buildUi() {
        val scroll = ScrollView(this)
        scroll.setBackgroundColor(paper)
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(22), dp(22), dp(28))
        }
        scroll.addView(root)
        setContentView(scroll)

        root.addView(text("克制时刻", 28, ink, true))
        root.addView(text("记录你每一次主动选择", 14, muted, false).apply {
            setPadding(0, dp(5), 0, dp(22))
        })

        val stats = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val todayCard = card()
        todayCard.addView(text("今日成功", 13, muted, false))
        todayValue = text("0", 34, ink, true)
        todayCard.addView(todayValue)
        todayCard.addView(text("次", 13, muted, false))
        val totalCard = card()
        totalCard.addView(text("累计记录", 13, muted, false))
        totalValue = text("0", 34, ink, true)
        totalCard.addView(totalValue)
        totalCard.addView(text("次", 13, muted, false))
        stats.addView(todayCard, LinearLayout.LayoutParams(0, dp(128), 1f).apply { marginEnd = dp(7) })
        stats.addView(totalCard, LinearLayout.LayoutParams(0, dp(128), 1f).apply { marginStart = dp(7) })
        root.addView(stats)

        val checkin = Button(this).apply {
            text = "✓   我克服了，记录一次"
            textSize = 16f
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundTintList(android.content.res.ColorStateList.valueOf(green))
            setOnClickListener {
                CheckInStore.addCheckIn(this@MainActivity)
                Toast.makeText(this@MainActivity, "记录成功。每一次选择都算数。", Toast.LENGTH_SHORT).show()
                refresh()
                CheckInWidgetProvider.updateAll(this@MainActivity)
            }
        }
        root.addView(checkin, LinearLayout.LayoutParams(-1, dp(58)).apply {
            topMargin = dp(18)
            bottomMargin = dp(20)
        })

        root.addView(text("提醒自己", 17, ink, true))
        root.addView(text("你不必拒绝所有娱乐。只需要在冲动出现时，重新做一次选择。", 14, muted, false).apply {
            setPadding(0, dp(8), 0, dp(22))
        })

        root.addView(text("最近记录", 17, ink, true))
        recordsBox = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(8), dp(14), dp(8))
            setBackgroundColor(Color.WHITE)
        }
        root.addView(recordsBox, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(10) })

        root.addView(text("数据仅保存在本机 · 云同步接口已预留", 12, muted, false).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(24), 0, dp(8))
        })
    }

    private fun refresh() {
        todayValue.text = CheckInStore.todayCount(this).toString()
        totalValue.text = CheckInStore.total(this).toString()
        recordsBox.removeAllViews()
        val events = CheckInStore.readEvents(this).take(12)
        if (events.isEmpty()) {
            recordsBox.addView(text("还没有记录。下一次克服冲动，就从这里开始。", 14, muted, false).apply {
                setPadding(0, dp(12), 0, dp(12))
            })
        } else {
            events.forEachIndexed { index, time ->
                val line = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    setPadding(0, dp(12), 0, dp(12))
                }
                val dot = TextView(this).apply {
                    text = "●"
                    textSize = 13f
                    setTextColor(green)
                }
                line.addView(dot)
                line.addView(text("  成功克服一次", 14, ink, false), LinearLayout.LayoutParams(0, -2, 1f))
                line.addView(text(CheckInStore.formatTime(time), 12, muted, false))
                recordsBox.addView(line)
                if (index < events.lastIndex) {
                    val divider = View(this).apply { setBackgroundColor(Color.rgb(235, 239, 242)) }
                    recordsBox.addView(divider, LinearLayout.LayoutParams(-1, dp(1)))
                }
            }
        }
    }

    private fun card() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16), dp(15), dp(12), dp(12))
        setBackgroundColor(Color.WHITE)
    }

    private fun text(value: String, size: Int, color: Int, bold: Boolean): TextView =
        TextView(this).apply {
            text = value
            textSize = size.toFloat()
            setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
