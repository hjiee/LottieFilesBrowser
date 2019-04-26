package com.googry.lottiefilesbrowser.ext

import com.google.gson.Gson
import com.googry.lottiefilesbrowser.BuildConfig

fun String.extractNumber(): String {
    return replace(Regex("[^\\d]"), "")
}

fun String.extractCapEnglish(): String {
    return replace(Regex("[^A-Z\\s]"), "")
}

fun String.extractCapEnglishNumber(): String {
    return replace(Regex("[^A-Z0-9\\s]"), "")
}

fun String.extractEngAddress(): String {
    return replace(Regex("[^A-Za-z0-9 \\-,.\\s/]"), "")
}

fun String.masking(): String {
    return replace(Regex("[^-()+.@\\s]"), "◼")
}

fun String.needUpdate(): Boolean {
    val origin = BuildConfig.VERSION_NAME.split(".").map { if (it.isEmpty()) 0 else it.toInt() }
    val remote = this.split(".").map { if (it.isEmpty()) 0 else it.toInt() }

    return when {
        remote[0] > origin[0] -> true
        remote[0] < origin[0] -> false
        else -> when {
            remote[1] > origin[1] -> true
            remote[1] < origin[1] -> false
            else -> remote[2] > origin[2]
        }
    }
}

inline fun <reified T> String.fromJson() = Gson().fromJson<T>(this)