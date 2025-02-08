package com.padelium.domain.dataresult

sealed class DataResult1 {
    class Success(val data: Unit) : DataResult1()  // Success with Unit as no data is returned
    object Loading : DataResult1()  // Loading state
    class Failure(val exception: Exception?, val errorCode: Int?, val errorMessage: String) : DataResult1()
}

