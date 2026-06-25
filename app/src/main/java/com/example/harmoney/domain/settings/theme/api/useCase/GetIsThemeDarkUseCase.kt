package com.example.harmoney.domain.settings.theme.api.useCase

import kotlinx.coroutines.flow.Flow

interface GetIsThemeDarkUseCase {
    fun execute(): Flow<Boolean?>
}
