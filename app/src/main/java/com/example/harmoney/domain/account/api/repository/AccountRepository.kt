package com.example.harmoney.domain.account.api.repository

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun addAccount(account: Account): Resource<Unit, AccountFailure>

    suspend fun updateAccount(account: Account): Resource<Unit, AccountFailure>

    suspend fun deleteAccount(account: Account): Resource<Unit, AccountFailure>

    suspend fun getAccount(accountId: Long): Resource<Account, AccountFailure>

    fun getAccountList(sortOption: SortOption): Flow<Resource<List<Account>, AccountFailure>>

    suspend fun checkAccountAlreadyExists(
        accountName: String,
        currency: Currency
    ): Resource<Boolean, AccountFailure>

    suspend fun updateAccountUserOrder(
        accountId: Long,
        prevAccount: Account?,
        nextAccount: Account?
    ): Resource<Unit, AccountFailure>
}
