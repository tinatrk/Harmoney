package com.example.harmoney.data.account.converter

import com.example.harmoney.data.account.entity.AccountEntity
import com.example.harmoney.domain.models.Account

interface AccountDBConverter {
    fun map(account: Account): AccountEntity

    fun map(account: AccountEntity, balanceMinorUnits: Long): Account

    fun map(accounts: List<Account>): List<AccountEntity>
}
