package com.nevaDev.padeliummarhaba.viewmodels

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.data.dto.SaveBookingRequestDTO
import com.padelium.data.dto.TransformedBookingData
import com.padelium.domain.dataresult.DataResultBooking



import javax.inject.Inject

/*
// Your Activity or Fragment class
class YourActivityOrFragment : AppCompatActivity() {

    @Inject
    lateinit var getBookingViewModel: GetBookingViewModel
    @Inject
    lateinit var saveBookingViewModel: SaveBookingViewModel  // Inject the SaveBookingViewModel
    @Inject
    lateinit var apiService: PadeliumApi  // Assuming you have an ApiService to make API calls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observe the booking data
        getBookingViewModel.dataResultBooking.observe(this) { result ->
            when (result) {
                is DataResultBooking.Success -> {
                    // Once you have the successful result, transform it
                    val bookingResponse = result.data

                    // Transform the GetBookingResponseDTO to SaveBookingRequestDTO
                    val saveBookingRequests = bookingResponse.map { booking ->
                        // Map the response to SaveBookingRequestDTO
                        SaveBookingRequestDTO(
                            transformedBookingData = transformToSaveBookingRequest(booking)  // Custom transformation function
                        )
                    }

                    // Call the SaveBooking API using SaveBookingViewModel
                    saveBookingViewModel.saveBooking(saveBookingRequests)
                }
                is DataResultBooking.Failure -> {
                    // Handle failure (e.g., show error message)
                    Log.e("GetBooking", "Error: ${result.errorMessage}")
                }
                is DataResultBooking.Loading -> {
                    // Show loading indicator if necessary
                }
            }
        }

        // Trigger the GetBooking API call (pass the appropriate key)
        getBookingViewModel.getBooking("your_booking_key")
    }

    // Define the transformation function here
     fun transformToSaveBookingRequest(booking: GetBookingResponseDTO): TransformedBookingData {
        return TransformedBookingData(
            establishmentDTO = booking.establishmentDTO,
            amount = booking.amount,
            decimalNumber = booking.decimalNumber,
            currencySymbol = booking.currencySymbol,
            facadeUrl = booking.facadeUrl,
            openTime = booking.openTime,
            closeTime = booking.closeTime,
            searchDate = booking.searchDate,
            from = booking.from,
            to = booking.to,
            numberOfPlayer = booking.numberOfPlayer,
            description = booking.description,
            currencyId = booking.currencyId,
            mgAmount = booking.mgAmount,
            totalFeed = booking.totalFeed,
            moyFeed = booking.moyFeed,
            bookingAnnulationDTOSet = booking.bookingAnnulationDTOSet,
            secondAmount = booking.secondAmount,
            secondAamount = booking.secondAamount,
            key = booking.key,
            HappyHours = booking.HappyHours,
            withSecondPrice = booking.withSecondPrice,
            reductionAmount = booking.reductionAmount,
            reductionSecondAmount = booking.reductionSecondAmount,
            payFromAvoir = booking.payFromAvoir,
            reduction = booking.reduction,
            reductionaAmount = booking.reductionaAmount,
            reductionaSecondAmount = booking.reductionaSecondAmount,
            start = booking.start,
            end = booking.end,
            amountfeeTrans = booking.amountfeeTrans,
            samountfeeTrans = booking.samountfeeTrans,
            ramountfeeTrans = booking.ramountfeeTrans,
            rsamountfeeTrans = booking.rsamountfeeTrans,
            couponCode = booking.couponCode,
            establishmentPacksDTO = booking.establishmentPacksDTO,
            establishmentPacksId = booking.establishmentPacksId,
            plannings = booking.plannings,
            users = booking.users,
            isClient = booking.isClient,
            secondReduction = booking.secondReduction,
            aamount = booking.aamount,
            EstablishmentPictureDTO = booking.EstablishmentPictureDTO
        )
    }
}

*/