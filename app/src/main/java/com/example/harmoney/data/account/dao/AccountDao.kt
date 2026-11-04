package com.example.harmoney.data.account.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.harmoney.data.account.dto.AccountBalanceDto
import com.example.harmoney.data.account.entity.AccountEntity
import com.example.harmoney.domain.models.CategoryType
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)

    @Query("DELETE FROM account")
    suspend fun deleteAllAccounts()

    @Query("SELECT * FROM account WHERE id = :id ")
    fun getAccount(id: Long): AccountEntity?

    @Query("SELECT * FROM account WHERE name = :name AND currencyId = :currencyId")
    fun getAccount(name: String, currencyId: Long): AccountEntity?

    @Query("SELECT * FROM account ORDER BY createdAtMillis ASC")
    fun getAccountListSortedByCreatedAt(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM account ORDER BY name COLLATE LOCALIZED ASC")
    fun getAccountListSortedByName(): Flow<List<AccountEntity>>

    @Query(
        """
        SELECT * FROM account 
        ORDER BY 
                CASE WHEN :ascending = 1 THEN userOrder END ASC,
                CASE WHEN :ascending = 0 THEN userOrder END DESC
    """
    )
    fun getAccountListSortedByUserOrder(ascending: Boolean): Flow<List<AccountEntity>>

    @Query("UPDATE account SET userOrder = :newUserOrder WHERE id = :accountId")
    suspend fun updateAccountUserOrder(accountId: Long, newUserOrder: Double)

    @Query("SELECT MAX(userOrder) FROM account")
    suspend fun getMaxUserOrder(): Double?

    /** Сохраняет `account`, задавая ему самый большой `userOrder` среди всех accounts.
     * Т.е. account будет в конце списка при сортировке по возрастанию
     * или в начале списка при сортировке по убыванию */
    @Transaction
    suspend fun insertAccountWithAutoUserOrder(
        account: AccountEntity,
        userOrderStep: Double,
    ) {
        val maxUserOrder = getMaxUserOrder() ?: 0.0
        val newUserOrder = maxUserOrder + userOrderStep

        insertAccount(account.copy(userOrder = newUserOrder))
    }

    @Query(
        """
        SELECT
            account.id AS accountId,
            account.currencyId AS currencyId,
            account.initialBalanceMinorUnits
                + COALESCE(
                    SUM(
                        CASE
                            WHEN category.typeId = :incomeTypeId
                                THEN `transaction`.moneyAmountMinorUnits
                            WHEN category.typeId = :expenseTypeId
                                THEN -`transaction`.moneyAmountMinorUnits
                            ELSE 0
                        END
                    ), 
                    0
                ) AS balanceMinorUnits
            
        FROM account
        
        LEFT JOIN `transaction`
            ON `transaction`.accountId = account.id
        LEFT JOIN category
            ON `transaction`.categoryId = category.id
            
        WHERE account.id IN (:accountIds)
        
        GROUP BY account.id, account.currencyId, account.initialBalanceMinorUnits
            
    """
    )
    fun observeBalance(
        accountIds: List<Long>,
        incomeTypeId: Long = CategoryType.INCOME.id,
        expenseTypeId: Long = CategoryType.EXPENSES.id
    ): Flow<List<AccountBalanceDto>>
}
