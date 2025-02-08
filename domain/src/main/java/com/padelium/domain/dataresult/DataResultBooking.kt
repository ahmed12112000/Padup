package com.padelium.domain.dataresult


sealed class DataResultBooking<out T> {
    data class Success<T>(val data: T) : DataResultBooking<T>()
    data object Loading : DataResultBooking<Nothing>()
    class Failure(val exception: Exception?, val errorCode: Int?, val errorMessage: String) : DataResultBooking<Nothing>()
}
