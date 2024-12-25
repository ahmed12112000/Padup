package com.padelium.domain.dataresult

sealed class Resulta {

    class Success(val data: Any) : Resulta()
    data object Loading : Resulta()
    class Failure(val exception: Exception?,val errorCode: Int?,val statusCode: Int, val errorMessage: String) : Resulta()
}