package com.example.harmoney.data.account.converter

import com.example.harmoney.data.account.entity.AccountEntity
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.MoneyAmount
import java.time.Instant

class AccountDBConverterImpl : AccountDBConverter {
    override fun map(account: Account): AccountEntity {
        return AccountEntity(
            id = account.id,
            name = account.name,
            currencyId = account.currency.id,
            initialBalanceMinorUnits = account.initialBalance.minorUnits,
            isArchived = account.isArchived,
            createdAtMillis = account.createdAt.toEpochMilli(),
            userOrder = account.userOrder
        )
    }

    override fun map(account: AccountEntity, balanceMinorUnits: Long): Account {
        return Account(
            id = account.id,
            name = account.name,
            currency = Currency.fromId(account.currencyId),
            initialBalance = MoneyAmount(minorUnits = account.initialBalanceMinorUnits),
            currentBalance = MoneyAmount(balanceMinorUnits),
            isArchived = account.isArchived,
            createdAt = Instant.ofEpochMilli(account.createdAtMillis),
            userOrder = account.userOrder
        )
    }

    override fun map(accounts: List<Account>): List<AccountEntity> {
        return accounts.map { map(it) }
    }
}
