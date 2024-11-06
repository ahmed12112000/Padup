import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevaDev.padeliummarhaba.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class BookingViewModel : ViewModel() {
    private val _availableTimeSlots = mutableStateOf<List<String>>(emptyList())
    //val availableTimeSlots: State<List<String>> = _availableTimeSlots

    val errorMessage = mutableStateOf<String?>(null)

    fun fetchGetBookingList(key: String?) {
        viewModelScope.launch {
            // The existing fetch logic goes here
            if (key.isNullOrEmpty()) {
                errorMessage.value = "Reservation key is null or empty."
                return@launch
            }

            val requestBody = key.trim()
                .toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

            val response = RetrofitClient.serverApi.GetBookingResponse(requestBody)

            if (response.isSuccessful && response.body() != null) {
                Log.d("ReservationScreen", "Bookings fetched successfully.")

                val fullList = response.body()!!
                Log.d("ReservationScreen", "Response body: $fullList") // Log the response

                // Flatten all PlanningDTO items from all BookingItems
                val planningList = fullList.flatMap { bookingItem ->
                    bookingItem.plannings
                }.distinctBy { it.fromStr + it.toStr } // Remove duplicates based on time strings

                if (planningList.isEmpty()) {
                    Log.d("ReservationScreen", "No planning items found.") // Check if planning list is empty
                } else {
                    // Generate time slot list by combining `fromStr` and `toStr`
                    val timeSlots = planningList.map { "${it.fromStr} - ${it.toStr}" }
                    Log.d("ReservationScreen", "Generated time slots: $timeSlots") // Log the generated time slots
                    _availableTimeSlots.value = timeSlots // Update available time slots
                }

            } else {
                Log.d("ReservationScreen", "Failed to fetch bookings. Response code: ${response.code()}")
                errorMessage.value = "Failed to fetch bookings: ${response.message()}"
            }
        }
    }
}
