package com.example.harmoney.domain.account.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.repository.AccountRepository
import com.example.harmoney.domain.account.api.useCase.UpdateAccountUserOrderUseCase
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account

class UpdateAccountUserOrderUseCaseImpl(private val repository: AccountRepository) :
    UpdateAccountUserOrderUseCase {
    override suspend fun execute(
        from: Int,
        to: Int,
        oldAccounts: List<Account>
    ): Resource<Unit, AccountFailure> {
        if (from == to) return Resource.Success(Unit)

        val newList: MutableList<Account> = oldAccounts.toMutableList()
        val item = newList.removeAt(from)
        newList.add(to, item)

        val prev = newList.getOrNull(to - 1)
        val next = newList.getOrNull(to + 1)

        return repository
            .updateAccountUserOrder(item.id, prev, next)
    }
}
