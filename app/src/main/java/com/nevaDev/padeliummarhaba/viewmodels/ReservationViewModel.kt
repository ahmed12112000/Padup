package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevaDev.padeliummarhaba.models.EstablishmentDTO
import com.nevaDev.padeliummarhaba.models.GetBookingList
import com.nevaDev.padeliummarhaba.models.GetEstablishmentDTO
import com.nevaDev.padeliummarhaba.models.InitBookingList
import com.nevaDev.padeliummarhaba.models.InitBookingRequest
import com.nevaDev.padeliummarhaba.models.PlanningDTO
import com.nevaDev.padeliummarhaba.models.ReservationRequest
import com.nevaDev.padeliummarhaba.retrofit.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.threeten.bp.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReservationViewModel : ViewModel() {

    //val errorMessage = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val reservationKey = mutableStateOf("")

    val establishments7 = mutableStateOf<List<EstablishmentDTO>>(emptyList())

     val establishments3 = MutableStateFlow<List<PlanningDTO>>(emptyList())
    val booking: StateFlow<List<PlanningDTO>> = establishments3

    private val establishments4 = MutableStateFlow<List<EstablishmentDTO>>(emptyList())
    val establishments: StateFlow<List<EstablishmentDTO>> = establishments4
    // To hold error messages
     val errorMessage = MutableStateFlow<String?>(null)


    val errorMessage1 = MutableStateFlow("")
    val errorMessage2: StateFlow<String> = errorMessage1.asStateFlow()

    val establishments1 = mutableStateOf<List<GetBookingList>>(emptyList())
    val combinedEstablishments = mutableListOf<Pair<EstablishmentDTO, PlanningDTO>>() // Replace PlanningDTO with your specific DTO if needed

    var lastSelectedDate: LocalDate? = null
    val isOneShot = mutableStateOf(true)

    val availableTimeSlots = mutableStateOf<List<String>>(emptyList())



    fun fetchReservationKeyAndFollowUp(date: LocalDate?) {
        // Check if the selected date has changed
        if (date != lastSelectedDate) {
            isOneShot.value = true
            lastSelectedDate = date
        }
        if (!isOneShot.value) return

        viewModelScope.launch {
            // Proceed if the date is not null
            if (date != null) {
                val formattedDateTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00"
                val requestBody = ReservationRequest(formattedDateTime)

                try {
                    isLoading.value = true

                    // Prepare the request body with the correct JSON media type
                    val jsonMediaType = "application/json;charset=UTF-8".toMediaTypeOrNull()
                    val requestBodyWithContentType =
                        Gson().toJson(requestBody).toRequestBody(jsonMediaType)

                    // Call the API to get the reservation key
                    val response = RetrofitClient.serverApi.getReservationKey(requestBodyWithContentType)

                    // Check if the response is successful
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val key = responseBody?.key

                        // Check if the key is not null or empty
                        if (!key.isNullOrEmpty()) {
                            reservationKey.value = key

                            GetInit(key)

                            fetchAvailableEstablishments(key) { result, error ->
                                if (error != null) {
                                    errorMessage.value = error
                                } else {
                                    establishments4.value = result ?: emptyList()
                                }
                            }

                            fetchInitBookingList(key)
                            fetchGetBookingList(key) { bookings, error ->
                                if (error != null) {
                                    errorMessage.value = error // Update error message
                                } else {
                                    bookings?.let {
                                        // Store the distinct time slots in _establishments2
                                        establishments3.value = it
                                    }
                                }
                            }
                            isOneShot.value = false
                        } else {
                            errorMessage.value = "Invalid response: Missing key"
                        }
                    } else {
                        errorMessage.value = "Error: ${response.message()}"
                    }
                } catch (e: Exception) {
                    errorMessage.value = "Error: ${e.message}"
                } finally {
                    isLoading.value = false
                }
            } else {
                errorMessage.value = "Selected date is null."
            }
        }
    }




    // Function to fetch init search using the reservation key
    // Function to get initial data based on the fetched key
    suspend fun GetInit(key: String) {
        try {
            val jsonMediaType = "application/json".toMediaTypeOrNull()
            val requestBody = key.toRequestBody(jsonMediaType)

            // Make the network request to the searchinit endpoint
            val response = RetrofitClient.serverApi.searchinit(requestBody)

            if (response.isSuccessful) {
                val establishmentData = response.body()
                if (establishmentData != null) {
                    // Handle successful response
                } else {
                    errorMessage.value = "Empty response body"
                }
            } else {
                errorMessage.value = "Error: ${response.message()}"
            }
        } catch (e: Exception) {
            errorMessage.value = "Error: ${e.message}"
        }
    }
    fun GetEstablishmentDTO.toEstablishmentDTO(): EstablishmentDTO {
        return EstablishmentDTO(
            id = this.id,
            name = this.name,
            code = this.code,
            description = this.description ?: "",
            email = this.email ?: "",
            adress = this.address ?: "",
            latitude = this.latitude.toBigDecimal(),
            longitude = this.longitude.toBigDecimal(),
            created = Instant.parse(this.createdDate),
            updated = Instant.now(),
            createdBy = 0L, // Assign a value here as `updated` isn't provided in `GetEstablishmentDTO`
            updatedBy = 0L, // Assign a default or retrieve if needed
            cityId = 0L, // Assign a default or retrieve if needed
            activityId = 0L, // Map as per requirement
            jhiEntityId = 0L, // Map as per requirement
            validated = true, // Map as per requirement
            maxNumberPlayer = this.numberOfPlayer, // Map as per requirement
            timeSpan = this.timeSpan.toBigDecimal(),
            cityName = this.cityName,
            activityName = this.activityName,
            userId = 0L,
            userEmail = "", // Map as per requirement
            activityCode = "", // Map as per requirement
            detailDescription = "", // Map as per requirement
            activityValide = true, // Map as per requirement
            phone = "", // Map as per requirement
            establishmentFeatureDTOList = emptyList(), // Map as per requirement
            contacts = emptyList(), // Map or assign an empty list
            createdByName = "", // Map or assign an empty list
            createdByEmail = "", // Map as per requirement
            logo = this.logo ?: "", // Map as per requirement
            pictures = this.establishmentPictureDTO.map { /* Map to EstablishmentPictureDTO as needed */ },
            facadePict = this.facadeUrl.isNotEmpty(),
            facadePict1 = false,
            facadePict2 = false, // Assign default if needed
            facadePict3 = false, // Assign default if needed
            color = "", // Assign default if needed
            establishmentTypeId = 0L, // Map as per requirement
            establishmentTypeCode = "", // Map as per requirement
            isEvent = false, // Map as per requirement
            isSpace = false, // Map as per requirement
            showAsGold = false, // Map as per requirement
            activityActive = true, // Map as per requirement
            activitySmallIcon = "", // Map as per requirement
            activityIcon = "", // Map as per requirement
            isClient = this.client, // Map as per requirement
            establishmentId = this.establishmentId,
            createdDate = Instant.parse(this.createdDate),
            amount = this.amount,
            currencySymbol = this.currencySymbol,
            plannings = mutableStateOf(this.plannings),
            HappyHours = this.HappyHours,
        )
    }


    private val _establishments = MutableStateFlow<List<EstablishmentDTO>>(emptyList())

    private val _errorMessage = MutableStateFlow<String?>(null)

    fun fetchAvailableEstablishments(key: String?) {
        viewModelScope.launch {
            fetchAvailableEstablishments(key) { result, error ->
                if (error != null) {
                    _errorMessage.value = error
                } else {
                    _establishments.value = result ?: emptyList()
                }
            }
        }
    }


    // Function to fetch available establishments
    suspend fun fetchAvailableEstablishments(key: String?, onResult: (List<EstablishmentDTO>?, String?) -> Unit) {
        if (key.isNullOrEmpty()) {
            onResult(null, "Reservation key is null or empty.")
            return
        }

        try {
            val rawKey = key.trim()
            val requestBody = rawKey
                .toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

            val response = RetrofitClient.serverApi.searchlist(requestBody)

            if (response.isSuccessful && response.body() != null) {
                val establishments = response.body()!!.map { it.toEstablishmentDTO() }
                onResult(establishments, null)
            } else {
                onResult(null, "Failed to fetch establishments. Response code: ${response.code()}")
            }
        } catch (e: Exception) {
            onResult(null, "Error fetching establishments: ${e.message}")
        }
    }

    fun InitBookingList.toPlanningDTO(): PlanningDTO {
        return PlanningDTO(
            from = this.from,
            to = this.to,
            available = this.available,
            openTime = this.openTime,
            closeTime = this.closeTime,
            bookings = this.bookings, // Assuming bookings can be directly used in PlanningDTO
            availableBol = this.availableBol,
            dayWithBooking = this.dayWithBooking,
            fromStr = this.fromStr,
            toStr = this.toStr,
            price = this.price,
            feeTransaction = this.feeTransaction,
            reductionPrice = this.reductionPrice,
            rfeeTransaction = this.rfeeTransaction,
            currencySymbol = this.currencySymbol,
            reductionPriceBol = this.reductionPriceBol,
            secondPrice = this.secondPrice,
            isHappyHours = this.isHappyHours,
            annulationDate = this.annulationDate
        )
    }




    fun loadBookings(key: String?) {
        viewModelScope.launch {
            fetchInitBookingList(key) // Call the updated function to load bookings

            // Optionally, handle any logic after the bookings have been loaded
            // For example, you might want to do something when bookings are successfully loaded.
        }
    }
    suspend fun fetchInitBookingList(key: String?) {
        // Check if the provided key is null or empty
        if (key.isNullOrEmpty()) {
            Log.e("ReservationScreen", "Key is null or empty.")
            return
        }

        try {
            // Create the request object using the provided key
            val request = InitBookingRequest(key.trim())

            // Make the API call to initialize booking
            val response = RetrofitClient.serverApi.initBooking(request)

            // Check if the response is successful and the body is not null
            if (response.isSuccessful && response.body() != null) {
                // Safely cast the response body to List<InitBookingList>
                val initBookingList = response.body()

                // Check if the initBookingList is not null or empty
                if (!initBookingList.isNullOrEmpty()) {
                    // Map to PlanningDTO
                    val bookings = initBookingList.map { it.toPlanningDTO() }

                    // Store the bookings in the establishments3 property
                    establishments3.value = bookings

                    // Log the results
                    bookings.forEach { booking ->
                        Log.d("ReservationScreen", "Booking: ${booking.fromStr}, Price: ${booking.price}")
                    }
                } else {
                    Log.e("ReservationScreen", "Response body is empty or not of type List<InitBookingList>.")
                }
            } else {
                Log.d("ReservationScreen", "Failed to fetch booking list. Response code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ReservationScreen", "Error fetching booking list: ${e.message}")
        }
    }





    suspend fun fetchGetBookingList(key: String?, onResult: (List<PlanningDTO>?, String?) -> Unit) {
        if (key.isNullOrEmpty()) {
            onResult(null, "Reservation key is null or empty.")
            return
        }

        try {
            val rawKey = key.trim()
            val requestBody = rawKey
                .toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

            // Call the API to fetch booking response
            val response = RetrofitClient.serverApi.GetBookingResponse(requestBody)

            if (response.isSuccessful && response.body() != null) {




                // Log each unique time slot detail

            } else {
                Log.e("ReservationScreen", "Failed to fetch bookings. Response code: ${response.code()}")
                onResult(null, "Failed to fetch bookings. Response code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ReservationScreen", "Error fetching bookings: ${e.message}", e)
            onResult(null, "Error fetching bookings: ${e.message}")
        }
    }
}




    // Function to fetch bookings and update establishments2











/*
@Composable
fun TimeSlotSelector(
    timeSlots: List<String>, // Pass the fetched time slots here
    onTimeSlotSelected: (String) -> Unit
) {
    var selectedTime by remember { mutableStateOf("") }

    Column {
        Text(
            text = "Select a Time Slot",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            timeSlots.forEach { time ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            selectedTime = time
                            onTimeSlotSelected(time) // Notify selected time
                        }
                        .background(
                            color = if (time == selectedTime) Color.Blue else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = time,
                        color = if (time == selectedTime) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

*/


