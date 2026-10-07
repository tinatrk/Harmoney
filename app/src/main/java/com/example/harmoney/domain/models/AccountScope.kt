package com.example.harmoney.domain.models

sealed interface AccountScope {
    val id: Long
    val accounts: List<Account>

    data class Single(val account: Account) : AccountScope {
        override val id: Long
            get() = account.id

        override val accounts: List<Account>
            get() = listOf(account)
    }

    data class Group(
        val type: AccountGroupType,
        override val accounts: List<Account>
    ) : AccountScope {
        override val id: Long
            get() = type.id
    }
}
