package com.nevaDev.padeliummarhaba.ui.activities
/*
import androidx.compose.runtime.Composable

@Composable
fun AppNavHost1() {
    composable(
        "PaymentSection1/{amountSelected}/{currencySymbol}/{name}/{time}/{date}",
        arguments = listOf(
            navArgument("amountSelected") { type = NavType.FloatType },
            navArgument("name") { type = NavType.StringType },
            navArgument("time") { type = NavType.StringType },
            navArgument("date") { type = NavType.StringType },
            navArgument("currencySymbol") { type = NavType.StringType }
        )
    ) { backStackEntry ->

        val amountSelected = backStackEntry.arguments?.getFloat("amountSelected")?.toDouble() ?: 0.0
        val currencySymbol = backStackEntry.arguments?.getString("currencySymbol") ?: ""
        val name = backStackEntry.arguments?.getString("name") ?: ""
        val time = backStackEntry.arguments?.getString("time") ?: ""
        val date = backStackEntry.arguments?.getString("date") ?: ""

        // Construct nested DTOs
        val establishment = EstablishmentBasicDTO(
            name = name,
            id = orderId,
            code = "EST-001",

        )

        val plannings = listOf(
            PlanningBasicDTO(
                from = "2024-12-21T10:00:00",
                to = "2024-12-21T11:00:00",
                available = 2,
            )
        )
        val establishmentPictureDTO = listOf(
            EstablishmentPictureBasicDTO(
                id = orderId,
                )
        )

        val HappyHours = listOf(
            happyHoursBasicDTO(
                id = orderId,
                from = "2024-12-21T10:00:00",
                to = "2024-12-21T11:00:00",
            )
        )



        //happyHoursBasicDTO
        // Construct SaveBookingRequest
        val saveBookingRequest = SaveBookingRequest(
            aamount = BigDecimal(amountSelected),
            amount = BigDecimal(amountSelected),
            amountfeeTrans = BigDecimal.ZERO,
            currencyId = 1L,
            currencySymbol = currencySymbol,
            decimalNumber = 2,
            description = "Sample Description",
            end = "2024-12-21T11:00:00",
            Establishment = establishment,
            facadeUrl = "https://example.com/facade",
            mgAmount = BigDecimal.ZERO,
            moyFeed = 0.0,
            numberOfPart = 4,
            payFromAvoir = false,
            plannings = plannings,
            reductionSecondAmount = BigDecimal.ZERO,
            reductionaSecondAmount = BigDecimal.ZERO,
            rsamountfeeTrans = BigDecimal.ZERO,
            samountfeeTrans = BigDecimal.ZERO,
            searchDate = date,
            secondAamount = BigDecimal.ZERO,
            secondAmount = BigDecimal.ZERO,
            secondReduction = 0,
            start = "2024-12-21T10:00:00",
            closeTime = "2024-12-21T18:00:00",
            to = "2024-12-21T11:00:00",
            from = "2024-12-21T10:00:00",
            totalFeed = 0,
            withSecondPrice = false
        )

        // Pass to PaymentSection1
        PaymentSection1(
            selectedDate = LocalDate.now(),
            selectedTimeSlot = time,
            selectedReservation = ReservationOption(name, time, "$amountSelected", date),
            onExtrasUpdate = { extra1, extra2, extra3 -> },
            onPayWithCardClick = { /* Handle pay click */ },
            totalAmount = "$amountSelected",
            navController = navController,
            saveBookingRequest = saveBookingRequest,
            paymentRequest = PaymentRequest(
                amount = "$amountSelected",
                currency = currencySymbol,
                orderId = "12345"
            ),
            viewModel = hiltViewModel(),
            viewModel1 = hiltViewModel(),
            amountSelected = amountSelected,
            currencySymbol = currencySymbol
        )
    }
}
*/