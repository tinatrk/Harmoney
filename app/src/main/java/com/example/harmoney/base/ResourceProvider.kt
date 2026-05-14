package com.example.harmoney.base

import android.content.Context
import androidx.annotation.StringRes

class ResourceProvider(private val context: Context) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    fun getString(@StringRes stringResId: Int, vararg params: Any?): String {
        return context.getString(stringResId, *params)
    }
}
