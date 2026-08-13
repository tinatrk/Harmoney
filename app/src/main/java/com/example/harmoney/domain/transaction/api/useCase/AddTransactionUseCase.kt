package com.example.harmoney.domain.transaction.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.transaction.models.TransactionFailure

interface AddTransactionUseCase {
    suspend fun execute(transaction: Transaction): Resource<Unit, TransactionFailure>
}
