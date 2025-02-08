package com.padelium.domain.usecases

import com.padelium.domain.dto.CreditPayResponse
import com.padelium.domain.repositories.ICreditPayRepository


class CreditPayUseCase(private val repository: ICreditPayRepository) {
    suspend fun execute(): List<CreditPayResponse> {
        return repository.GetCreditPay()
    }
}
