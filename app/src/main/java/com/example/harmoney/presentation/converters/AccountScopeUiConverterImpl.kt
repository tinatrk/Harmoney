package com.example.harmoney.presentation.converters

import com.example.harmoney.R
import com.example.harmoney.core.util.StringProvider
import com.example.harmoney.domain.models.AccountGroupType
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.Money
import com.example.harmoney.presentation.models.AccountScopeUi

class AccountScopeUiConverterImpl(
    private val stringProvider: StringProvider,
    private val numbersFormatter: NumbersFormatter,
    private val convertedTotalFormatter: ConvertedTotalFormatter,
) : AccountScopeUiConverter {
    override fun singleMap(scope: AccountScope.Single, balance: Money): AccountScopeUi {
        return AccountScopeUi(
            id = scope.id,
            name = getScopeTitle(scope, balance.currency.symbol),
            balance = numbersFormatter.moneyToStringWithCurrency(moneyInMinorUnits = balance),
            balanceOriginals = null,
            isArchivedMark = getIsArchiveMark(scope),
        )
    }

    override fun groupMap(scope: AccountScope.Group, balance: ConvertedTotal): AccountScopeUi {
        return AccountScopeUi(
            id = scope.id,
            name = getScopeTitle(scope, balance.total.currency.symbol),
            balance = numbersFormatter.moneyToStringWithCurrency(moneyInMinorUnits = balance.total),
            balanceOriginals = convertedTotalFormatter.getOriginalsStr(balance),
            isArchivedMark = getIsArchiveMark(scope)
        )
    }

    private fun getScopeTitle(scope: AccountScope, currencySymbol: String): String {
        val name = when (scope) {
            is AccountScope.Single -> scope.account.name
            is AccountScope.Group -> {
                when (scope.type) {
                    AccountGroupType.NOT_ARCHIVED ->
                        stringProvider.getString(R.string.title_account_scope_not_archived)

                    AccountGroupType.ALL ->
                        stringProvider.getString(R.string.title_account_scope_all)
                }
            }
        }

        return "$name$ACCOUNT_NAME_SEPARATOR[$currencySymbol]"
    }

    private fun getIsArchiveMark(scope: AccountScope): Boolean {
        return when (scope) {
            is AccountScope.Single -> scope.account.isArchived
            else -> false
        }
    }

    companion object {
        private const val ACCOUNT_NAME_SEPARATOR = " "
    }
}
