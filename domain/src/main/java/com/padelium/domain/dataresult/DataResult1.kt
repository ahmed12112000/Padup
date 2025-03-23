package com.padelium.domain.dataresult

sealed class DataResult2<out T> {
    data object Loading : DataResult2<Nothing>()
    data class Success<T>(val data: T) : DataResult2<T>()

    // FIX: Remove <T> from Failure
    data class Failure(
        val exception: Exception?,
        val errorCode: Int?,
        val errorMessage: String
    ) : DataResult2<Nothing>()  // Failure does not contain T
}

