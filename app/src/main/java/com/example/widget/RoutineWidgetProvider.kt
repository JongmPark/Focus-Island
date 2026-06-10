package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.RoutineDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutineWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateAllWidgets(context, appWidgetManager)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // If we receive our custom update action, we force an update
        if (intent.action == ACTION_UPDATE_WIDGET) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            updateAllWidgets(context, appWidgetManager)
        }
    }

    companion object {
        const val ACTION_UPDATE_WIDGET = "com.example.widget.ACTION_UPDATE_WIDGET"

        fun triggerUpdate(context: Context) {
            val intent = Intent(context, RoutineWidgetProvider::class.java).apply {
                action = ACTION_UPDATE_WIDGET
            }
            context.sendBroadcast(intent)
        }

        private fun updateAllWidgets(context: Context, appWidgetManager: AppWidgetManager) {
            val component = ComponentName(context, RoutineWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(component)

            val db = RoutineDatabase.getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val routines = db.routineDao().getAllRoutinesList()
                    val totalRoutines = routines.size
                    val completedRoutines = routines.count { it.isCompleted }
                    val remaining = totalRoutines - completedRoutines
                    val percent = if (totalRoutines > 0) (completedRoutines * 100) / totalRoutines else 0

                    for (appWidgetId in appWidgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_layout)

                        // Clear and setup click intent to launch MainActivity
                        val mainIntent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        // Use FLAG_IMMUTABLE to meet Android 12+ security requirements
                        val pendingIntent = PendingIntent.getActivity(
                            context,
                            0,
                            mainIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                        views.setOnClickPendingIntent(R.id.widget_title, pendingIntent)
                        views.setOnClickPendingIntent(R.id.widget_progress, pendingIntent)

                        // Update dynamic progress statistics
                        views.setTextViewText(R.id.widget_progress, "$percent% Completed")
                        val activeText = if (remaining == 0 && totalRoutines > 0) {
                            "All routines completed! 🏝️"
                        } else {
                            "$remaining of $totalRoutines active"
                        }
                        views.setTextViewText(R.id.widget_remaining, activeText)

                        // Handle rows binding
                        if (totalRoutines == 0) {
                            views.setViewVisibility(R.id.widget_empty_text, View.VISIBLE)
                            views.setViewVisibility(R.id.widget_row1, View.GONE)
                            views.setViewVisibility(R.id.widget_row2, View.GONE)
                            views.setViewVisibility(R.id.widget_row3, View.GONE)
                        } else {
                            views.setViewVisibility(R.id.widget_empty_text, View.GONE)
                            
                            // Row 1
                            if (totalRoutines > 0) {
                                val r = routines[0]
                                views.setViewVisibility(R.id.widget_row1, View.VISIBLE)
                                views.setTextViewText(R.id.widget_row1_title, r.title)
                                views.setImageViewResource(
                                    R.id.widget_row1_icon,
                                    if (r.isCompleted) R.drawable.ic_widget_checked else R.drawable.ic_widget_unchecked
                                )
                            } else {
                                views.setViewVisibility(R.id.widget_row1, View.GONE)
                            }

                            // Row 2
                            if (totalRoutines > 1) {
                                val r = routines[1]
                                views.setViewVisibility(R.id.widget_row2, View.VISIBLE)
                                views.setTextViewText(R.id.widget_row2_title, r.title)
                                views.setImageViewResource(
                                    R.id.widget_row2_icon,
                                    if (r.isCompleted) R.drawable.ic_widget_checked else R.drawable.ic_widget_unchecked
                                )
                            } else {
                                views.setViewVisibility(R.id.widget_row2, View.GONE)
                            }

                            // Row 3
                            if (totalRoutines > 2) {
                                val r = routines[2]
                                views.setViewVisibility(R.id.widget_row3, View.VISIBLE)
                                views.setTextViewText(R.id.widget_row3_title, r.title)
                                views.setImageViewResource(
                                    R.id.widget_row3_icon,
                                    if (r.isCompleted) R.drawable.ic_widget_checked else R.drawable.ic_widget_unchecked
                                )
                            } else {
                                views.setViewVisibility(R.id.widget_row3, View.GONE)
                            }
                        }

                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
