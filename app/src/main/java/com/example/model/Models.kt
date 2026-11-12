package com.example.model

enum class VehicleCategory(
  val displayName: String,
  val subtitle: String,
  val baseFare: Double,
  val perKmRate: Double,
  val perMinRate: Double,
  val capacity: Int,
  val avgSpeedKmh: Double,
) {
  BIKE(
    displayName = "S.A.D Bike",
    subtitle = "Fastest for solo commute",
    baseFare = 20.0,
    perKmRate = 7.5,
    perMinRate = 1.0,
    capacity = 1,
    avgSpeedKmh = 30.0,
  ),
  AUTO(
    displayName = "S.A.D Auto",
    subtitle = "Affordable 3-wheeler ride",
    baseFare = 30.0,
    perKmRate = 11.0,
    perMinRate = 1.5,
    capacity = 3,
    avgSpeedKmh = 25.0,
  ),
  CAB(
    displayName = "S.A.D Cab",
    subtitle = "Comfortable AC 4-wheeler",
    baseFare = 60.0,
    perKmRate = 16.0,
    perMinRate = 2.0,
    capacity = 4,
    avgSpeedKmh = 28.0,
  )
}

data class LocationPoint(
  val name: String,
  val address: String,
  val latitude: Double,
  val longitude: Double,
  val category: String = "Popular",
)

data class FareBreakdown(
  val baseFare: Double,
  val distanceKm: Double,
  val distanceCharge: Double,
  val estimatedMins: Int,
  val timeCharge: Double,
  val platformFee: Double,
  val gstTax: Double,
  val totalFare: Double,
  val commissionRatePercent: Double = 10.0,
  val commissionAmount: Double, // 10% deduction for S.A.D Enterprises
  val driverEarnings: Double,   // 90% goes to Captain
) {
  companion object {
    fun calculate(category: VehicleCategory, distanceKm: Double): FareBreakdown {
      val base = category.baseFare
      val distCharge = distanceKm * category.perKmRate
      val estMins = ((distanceKm / category.avgSpeedKmh) * 60).toInt().coerceAtLeast(4)
      val timeCharge = estMins * category.perMinRate
      val platformFee = 5.0
      val subtotal = base + distCharge + timeCharge + platformFee
      val gstTax = (subtotal * 0.05).coerceAtLeast(2.0)
      val rawTotal = subtotal + gstTax
      val roundedTotal = kotlin.math.round(rawTotal)
      val commission = kotlin.math.round(roundedTotal * 0.10) // exactly 10%
      val driverShare = roundedTotal - commission

      return FareBreakdown(
        baseFare = base,
        distanceKm = distanceKm,
        distanceCharge = distCharge,
        estimatedMins = estMins,
        timeCharge = timeCharge,
        platformFee = platformFee,
        gstTax = gstTax,
        totalFare = roundedTotal,
        commissionRatePercent = 10.0,
        commissionAmount = commission,
        driverEarnings = driverShare,
      )
    }
  }
}

enum class PaymentMode(val label: String, val detail: String) {
  CASH("Cash on Trip", "Pay Captain directly at drop location"),
  RAZORPAY_UPI("Razorpay UPI", "Google Pay / PhonePe / Paytm"),
  RAZORPAY_CARD("Razorpay Cards", "Credit / Debit Card via Razorpay"),
}

enum class RideStatus {
  IDLE,
  SEARCHING_CAPTAIN,
  CAPTAIN_ASSIGNED,
  ARRIVING_PICKUP,
  IN_PROGRESS,
  COMPLETED,
  CANCELLED,
}

data class RideRecord(
  val id: String,
  val customerName: String = "Rahul Sharma",
  val customerPhone: String = "+91 98765 43210",
  val captainName: String = "Mohd. Sajid",
  val captainPhone: String = "+91 91234 56789",
  val captainVehicleNumber: String = "UP 32 BK 7890",
  val vehicleCategory: VehicleCategory,
  val pickupLocation: LocationPoint,
  val dropLocation: LocationPoint,
  val distanceKm: Double,
  val fare: FareBreakdown,
  val paymentMode: PaymentMode,
  val otp: String,
  val status: RideStatus,
  val createdAt: Long = System.currentTimeMillis(),
)

data class WalletTransaction(
  val id: String,
  val title: String,
  val description: String,
  val amount: Double,
  val isCredit: Boolean, // true = Recharge, false = 10% Commission deduction
  val rideId: String? = null,
  val timestamp: Long = System.currentTimeMillis(),
  val balanceAfter: Double,
)

data class CaptainProfile(
  val id: String = "CAP-1002",
  val name: String = "Mohd. Sajid",
  val phone: String = "+91 91234 56789",
  val rating: Double = 4.87,
  val vehicleCategory: VehicleCategory = VehicleCategory.AUTO,
  val vehicleModel: String = "Bajaj Compact RE (CNG)",
  val vehiclePlate: String = "UP 32 AZ 4142",
  val walletBalance: Double = 350.0,
  val negativeLimit: Double = -150.0,
  val isOnline: Boolean = true,
  val totalTripsToday: Int = 8,
  val todayGrossEarnings: Double = 1420.0,
  val todayCommissionPaid: Double = 142.0,
) {
  val isWalletBlocked: Boolean
    get() = walletBalance < negativeLimit
}
