package com.example.harmoney.core.util

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg strParams: String): String
}
