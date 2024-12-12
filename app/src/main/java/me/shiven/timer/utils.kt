package me.shiven.timer

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

@SuppressLint("DefaultLocale")
fun timeFormatHelper(durationInSeconds: Int): String {
    return if(durationInSeconds > 60){
        String.format("%d:%02d", durationInSeconds / 60, durationInSeconds % 60)
    } else {
        durationInSeconds.toString()
    }
}

fun getSystemAnimationScale(context: Context): Float {
    return try {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE
        )
    } catch (e: Settings.SettingNotFoundException) {
        1.0f
    }
}