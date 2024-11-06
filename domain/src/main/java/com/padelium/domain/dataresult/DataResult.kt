package com.padelium.domain.dataresult

sealed class DataResult {
    class Success(val result: Any) : DataResult()
    data object Loading : DataResult()
    class Failure(val exception: Exception?,val errorCode: Int?,val errorMessage: String) : DataResult()
}
