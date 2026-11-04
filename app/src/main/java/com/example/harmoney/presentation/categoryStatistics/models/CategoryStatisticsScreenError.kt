package com.example.harmoney.presentation.categoryStatistics.models

import androidx.annotation.StringRes
import com.example.harmoney.R

enum class CategoryStatisticsScreenError(@param:StringRes val resId: Int) {
    CHANGE_THEME_ERROR(R.string.toast_changed_theme_error),
    UPDATE_REPORTING_CURRENCY_ERROR(R.string.toast_update_reporting_currency_error),
    GET_SETTINGS_ERROR(R.string.toast_load_settings_error),
    GET_ACCOUNTS_ERROR(R.string.toast_load_accounts_error),
    GET_STATISTICS_ERROR(R.string.toast_load_statistics_error),
    UPDATE_FIRST_DAY_MONTH_ERROR(R.string.toast_update_first_day_month_error),
    SAVE_ACCOUNT_ERROR(R.string.toast_save_selected_account_error)
}
