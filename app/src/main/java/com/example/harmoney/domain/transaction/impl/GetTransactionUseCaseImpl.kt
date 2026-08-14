package com.example.harmoney.domain.transaction.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.transaction.api.repository.TransactionRepository
import com.example.harmoney.domain.transaction.api.useCase.GetTransactionUseCase
import com.example.harmoney.domain.transaction.models.TransactionFailure

class GetTransactionUseCaseImpl(private val repository: TransactionRepository) :
    GetTransactionUseCase {
    override suspend fun execute(transactionId: Long): Resource<Transaction, TransactionFailure> {
        return repository.getTransaction(transactionId)
    }
}
