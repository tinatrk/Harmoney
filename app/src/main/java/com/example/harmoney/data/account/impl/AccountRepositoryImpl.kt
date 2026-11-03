package com.example.harmoney.data.account.impl

import androidx.sqlite.SQLiteException
import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.account.converter.AccountDBConverter
import com.example.harmoney.data.account.dao.AccountDao
import com.example.harmoney.domain.account.api.repository.AccountRepository
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import timber.log.Timber

class AccountRepositoryImpl(
    private val accountDao: AccountDao,
    private val accountDBConverter: AccountDBConverter,
) : AccountRepository {
    // для корректной работы updateAccountUserOrder
    private val sortingDirection = USER_ORDER_IN_ASCENDING_ORDER

    override suspend fun addAccount(account: Account): Resource<Unit, AccountFailure> {
        return try {
            accountDao.insertAccountWithAutoUserOrder(
                account = accountDBConverter.map(account),
                userOrderStep = USER_ORDER_STEP,
            )
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error when inserting account with auto user order")
            Resource.Error(AccountFailure.DatabaseError)
        }
    }

    override suspend fun updateAccount(account: Account): Resource<Unit, AccountFailure> {
        return try {
            accountDao.updateAccount(accountDBConverter.map(account))
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error updating account")
            Resource.Error(AccountFailure.DatabaseError)
        }
    }

    override suspend fun deleteAccount(account: Account): Resource<Unit, AccountFailure> {
        return try {
            accountDao.deleteAccount(accountDBConverter.map(account))
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error deleting account")
            Resource.Error(AccountFailure.DatabaseError)
        }
    }

    override suspend fun getAccount(accountId: Long): Resource<Account, AccountFailure> {
        return try {
            val account = accountDao.getAccount(accountId)
            if (account != null) {
                Resource.Success(
                    accountDBConverter
                        .map(account, account.initialBalanceMinorUnits)//????????
                )
            } else {
                Resource.Error(AccountFailure.BadRequest)
            }
        } catch (e: SQLiteException) {
            Timber.e(e, "Error getting account")
            Resource.Error(AccountFailure.DatabaseError)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAccountList(sortOption: SortOption)
            : Flow<Resource<List<Account>, AccountFailure>> {
        val accountsFlow = when (sortOption) {
            SortOption.ALPHABET -> accountDao.getAccountListSortedByName()

            SortOption.TIME_CREATED -> accountDao.getAccountListSortedByCreatedAt()

            SortOption.USER_ORDER -> accountDao.getAccountListSortedByUserOrder(
                ascending = (sortingDirection == USER_ORDER_IN_ASCENDING_ORDER)
            )
        }

        return accountsFlow
            .distinctUntilChanged()
            .flatMapLatest { accountEntities ->
                if (accountEntities.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    val accountIds = accountEntities.map { it.id }

                    accountDao.observeBalance(accountIds)
                        .distinctUntilChanged()
                        .map { balances ->
                            val balancesByAccountId = balances.associateBy { it.accountId }

                            accountEntities.map { entity ->
                                val balance = balancesByAccountId[entity.id]

                                accountDBConverter.map(
                                    entity,
                                    balance?.balanceMinorUnits ?: entity.initialBalanceMinorUnits
                                )
                            }
                        }
                }
            }
            .distinctUntilChanged()
            .map<List<Account>, Resource<List<Account>, AccountFailure>> { accounts ->
                Resource.Success(accounts)
            }
            .catch { e ->
                Timber.e(e, "Error getting of account list with balance")
                when (e) {
                    is SQLiteException -> emit(Resource.Error(AccountFailure.DatabaseError))
                    else -> throw e
                }
            }
    }

    override suspend fun checkAccountAlreadyExists(
        accountName: String,
        currency: Currency
    ): Resource<Boolean, AccountFailure> {
        return try {
            val account = accountDao.getAccount(name = accountName.trim(), currencyId = currency.id)
            Resource.Success(account != null)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error getting category by name and currency")
            Resource.Error(AccountFailure.DatabaseError)
        }
    }

    /** `accountId` - id `account`, который был размещен на новую позицию в списке
     *
     * `prevAccount` - элемент списка, идущий до `account` (выше/левее) на его новой позиции
     * (независимо от направления сортировки)
     *
     * `nextAccount` - элемент списка, идущий после `account` (ниже/правее) на его новой позиции
     *  (независимо от направления сортировки)
     * */
    override suspend fun updateAccountUserOrder(
        accountId: Long,
        prevAccount: Account?,
        nextAccount: Account?
    ): Resource<Unit, AccountFailure> {
        return try {
            val newUserOrder = when {
                // если список accounts пуст и теперь account первый элемент списка
                prevAccount == null && nextAccount == null -> USER_ORDER_STEP
                // если account переместили в самый верх списка
                prevAccount == null -> nextAccount!!.userOrder - sortingDirection * USER_ORDER_STEP
                // если account переместили в самый низ списка
                nextAccount == null -> prevAccount.userOrder + sortingDirection * USER_ORDER_STEP
                // если account переместили на позицию между двумя другими accounts
                else -> (prevAccount.userOrder + nextAccount.userOrder) / 2.0
            }

            accountDao.updateAccountUserOrder(accountId, newUserOrder)
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error updating account user order")
            Resource.Error(AccountFailure.DatabaseError)
        }
    }

    companion object {
        private const val USER_ORDER_STEP = 100.0
        private const val USER_ORDER_IN_ASCENDING_ORDER = 1 // значение не менять, это множитель
        private const val USER_ORDER_IN_DESCENDING_ORDER = -1 // значение не менять, это множитель
    }
}
