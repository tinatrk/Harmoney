package com.example.harmoney.core.util

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes resId: Int): String
}
