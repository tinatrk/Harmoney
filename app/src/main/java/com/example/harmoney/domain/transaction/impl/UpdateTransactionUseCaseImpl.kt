package com.example.harmoney.domain.transaction.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.transaction.api.repository.TransactionRepository
import com.example.harmoney.domain.transaction.api.useCase.UpdateTransactionUseCase
import com.example.harmoney.domain.transaction.models.TransactionFailure

class UpdateTransactionUseCaseImpl(val repository: TransactionRepository) :
    UpdateTransactionUseCase {
    override suspend fun execute(transaction: Transaction): Resource<Unit, TransactionFailure> {
        return repository.updateTransaction(transaction)
    }
}
