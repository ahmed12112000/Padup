package com.padelium.domain.dto


// Updated SaveBookingRequest class to hold the list directly
data class SaveBookingRequest(
    val bookings: List<GetBookingResponse>
)



/*

data class SaveBookingRequest3(
    val aamount: BigDecimal,
    val amount: BigDecimal,
    val amountfeeTrans: BigDecimal,
    val isClient:Boolean = true,
    val currencyId:Long,
    val currencySymbol:String,
    val decimalNumber: Int,
    val description: String,
    val end: String,

    val Establishment: EstablishmentBasicDTO,

     val HappyHours: List<happyHoursBasicDTO>,
     val establishmentPictureDTO: List<EstablishmentPictureBasicDTO>,

    val facadeUrl: String,


    val mgAmount: BigDecimal,
    val moyFeed: Double,
    val numberOfPart:Int,
    val payFromAvoir: Boolean,

    val plannings: List<PlanningBasicDTO>,


    val reductionSecondAmount: BigDecimal,
    val reductionaSecondAmount: BigDecimal,
    val rsamountfeeTrans: BigDecimal,
    val samountfeeTrans: BigDecimal,
    val searchDate: String,
    val secondAamount: BigDecimal,
    val secondAmount: BigDecimal,
    val secondReduction: Int,
    val start: String,
    val closeTime: String,
    val to: String,
    val from: String,
    val totalFeed: Int,

    val withSecondPrice: Boolean,

    // val usersIds:List<Long>,
    // val sharedExtrasIds: List<Long>,
    // val sharedExtrasIds: List<Long>,
    //  val privateExtrasIds:List<Long>,

    //val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    //  val bookingAnnulationDTOSet: List<bookingAnnulationDTOSet>,


)

data class SaveBookingRequest2(
    val establishmentDTO: EstablishmentDTO?,
    val amount: Double,
    val decimalNumber: Int,
    val currencySymbol: String,
    val facadeUrl: String,
    val openTime: Instant,
    val closeTime: Instant,
    val searchDate: String,
    val from: String,
    val to: String,
    val numberOfPlayer: Int,
    val description: String,
    val currencyId: Long,
    val mgAmount: BigDecimal,
    val totalFeed: Int,
    val moyFeed: Double,
    val bookingAnnulationDTOSet: List<Unit>,
    val secondAmount: BigDecimal,
    val secondAamount: BigDecimal,
    val key: String?,
    val HappyHours: List<HappyHours>,

    val withSecondPrice: Boolean,
    val reductionAmount: BigDecimal,
    val reductionSecondAmount: BigDecimal,
    val payFromAvoir: Boolean,
    val reduction: Int,
    val reductionaAmount: BigDecimal,
    val reductionaSecondAmount: BigDecimal,
    val start: String,
    val end: String,
    val amountfeeTrans: BigDecimal,
    val samountfeeTrans: BigDecimal,
    val ramountfeeTrans: BigDecimal,
    val rsamountfeeTrans: BigDecimal,
    val couponCode: String,
    val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    val establishmentPacksId: Long,
    val plannings: List<PlanningDTO>,
    val users: List<Long>,
    val isClient: Boolean = true,
    val secondReduction: Int,
    val aamount: BigDecimal,
    val EstablishmentPictureDTO: List<EstablishmentPictureDTO>,
)
*/