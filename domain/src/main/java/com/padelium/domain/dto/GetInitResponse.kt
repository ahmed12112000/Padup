package com.padelium.domain.dto

data class GetInitResponse(
    var dateTime: String?,
    var activityId: Long?,
    var cityId: Long?,
    var activityName: String?,
    var establishmentId: Long?,
    var cityName: String?,
    var key: String?,
    var isCity: Boolean,
    var time: String?
)
