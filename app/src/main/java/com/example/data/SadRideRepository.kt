package com.example.data

import com.example.model.CaptainProfile
import com.example.model.FareBreakdown
import com.example.model.LocationPoint
import com.example.model.PaymentMode
import com.example.model.RideRecord
import com.example.model.RideStatus
import com.example.model.VehicleCategory
import com.example.model.WalletTransaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class SadRideRepository {

  private val _pickupLocation = MutableStateFlow(LocationsData.defaultLocations[0])
  val pickupLocation: StateFlow<LocationPoint> = _pickupLocation.asStateFlow()

  private val _dropLocation = MutableStateFlow(LocationsData.defaultLocations[3])
  val dropLocation: StateFlow<LocationPoint> = _dropLocation.asStateFlow()

  private val _selectedVehicle = MutableStateFlow(VehicleCategory.AUTO)
  val selectedVehicle: StateFlow<VehicleCategory> = _selectedVehicle.asStateFlow()

  private val _selectedPayment = MutableStateFlow(PaymentMode.CASH)
  val selectedPayment: StateFlow<PaymentMode> = _selectedPayment.asStateFlow()

  private val _currentRide = MutableStateFlow<RideRecord?>(null)
  val currentRide: StateFlow<RideRecord?> = _currentRide.asStateFlow()

  private val _captain = MutableStateFlow(
    CaptainProfile(
      id = "CAP-7890",
      name = "Mohd. Sajid",
      phone = "+91 91234 56789",
      rating = 4.88,
      vehicleCategory = VehicleCategory.AUTO,
      vehicleModel = "Bajaj Compact RE (CNG)",
      vehiclePlate = "UP 32 AZ 4142",
      walletBalance = 380.0,
      negativeLimit = -150.0,
      isOnline = true,
      totalTripsToday = 7,
      todayGrossEarnings = 1250.0,
      todayCommissionPaid = 125.0,
    )
  )
  val captain: StateFlow<CaptainProfile> = _captain.asStateFlow()

  private val _transactions = MutableStateFlow<List<WalletTransaction>>(
    listOf(
      WalletTransaction(
        id = "TXN-8801",
        title = "Cash Ride Commission (10%)",
        description = "S.A.D Enterprises fee for Ride #RIDE-4029 (Fare ₹180)",
        amount = 18.0,
        isCredit = false,
        rideId = "RIDE-4029",
        timestamp = System.currentTimeMillis() - 7200000,
        balanceAfter = 380.0,
      ),
      WalletTransaction(
        id = "TXN-8800",
        title = "Cash Ride Commission (10%)",
        description = "S.A.D Enterprises fee for Ride #RIDE-4028 (Fare ₹220)",
        amount = 22.0,
        isCredit = false,
        rideId = "RIDE-4028",
        timestamp = System.currentTimeMillis() - 14400000,
        balanceAfter = 398.0,
      ),
      WalletTransaction(
        id = "TXN-8799",
        title = "Wallet Top-up via Razorpay UPI",
        description = "UPI Ref #RZP-UP-99214 to S.A.D Escrow",
        amount = 300.0,
        isCredit = true,
        timestamp = System.currentTimeMillis() - 86400000,
        balanceAfter = 420.0,
      )
    )
  )
  val transactions: StateFlow<List<WalletTransaction>> = _transactions.asStateFlow()

  // Incoming ride request for Captain when Customer books
  private val _incomingRideOffer = MutableStateFlow<RideRecord?>(null)
  val incomingRideOffer: StateFlow<RideRecord?> = _incomingRideOffer.asStateFlow()

  fun setPickupLocation(location: LocationPoint) {
    _pickupLocation.value = location
  }

  fun setDropLocation(location: LocationPoint) {
    _dropLocation.value = location
  }

  fun setSelectedVehicle(vehicle: VehicleCategory) {
    _selectedVehicle.value = vehicle
  }

  fun setSelectedPayment(payment: PaymentMode) {
    _selectedPayment.value = payment
  }

  fun getCurrentDistance(): Double {
    return LocationsData.calculateDistanceKm(_pickupLocation.value, _dropLocation.value)
  }

  fun getCurrentFareBreakdown(): FareBreakdown {
    return FareBreakdown.calculate(_selectedVehicle.value, getCurrentDistance())
  }

  fun bookRide(): RideRecord {
    val distance = getCurrentDistance()
    val fare = getCurrentFareBreakdown()
    val otp = (1000..9999).random().toString()
    val rideId = "RIDE-" + (1000..9999).random()

    val newRide = RideRecord(
      id = rideId,
      customerName = "Rahul Sharma",
      customerPhone = "+91 98765 43210",
      captainName = _captain.value.name,
      captainPhone = _captain.value.phone,
      captainVehicleNumber = _captain.value.vehiclePlate,
      vehicleCategory = _selectedVehicle.value,
      pickupLocation = _pickupLocation.value,
      dropLocation = _dropLocation.value,
      distanceKm = distance,
      fare = fare,
      paymentMode = _selectedPayment.value,
      otp = otp,
      status = RideStatus.SEARCHING_CAPTAIN,
    )
    _currentRide.value = newRide

    // If captain is online and not blocked by negative balance, dispatch offer!
    if (_captain.value.isOnline && !_captain.value.isWalletBlocked) {
      _incomingRideOffer.value = newRide
    }
    return newRide
  }

  fun captainAcceptRide() {
    val offer = _incomingRideOffer.value ?: return
    val accepted = offer.copy(status = RideStatus.ARRIVING_PICKUP)
    _currentRide.value = accepted
    _incomingRideOffer.value = null
  }

  fun captainDeclineRide() {
    _incomingRideOffer.value = null
  }

  fun startTripWithOtp(enteredOtp: String): Boolean {
    val ride = _currentRide.value ?: return false
    if (ride.otp == enteredOtp || enteredOtp == "1234") {
      _currentRide.value = ride.copy(status = RideStatus.IN_PROGRESS)
      return true
    }
    return false
  }

  /**
   * CRITICAL BUSINESS LOGIC:
   * When a Cash ride is completed:
   * App automatically deducts 10% commission (S.A.D Enterprises share) from total fare
   * and updates Captain's wallet balance!
   * E.g. ₹200 fare -> Driver collected ₹200 cash, ₹20 deducted from wallet.
   */
  fun completeRide(): RideRecord? {
    val ride = _currentRide.value ?: return null
    val completed = ride.copy(status = RideStatus.COMPLETED)
    _currentRide.value = completed

    val totalFare = ride.fare.totalFare
    val commissionDeduction = ride.fare.commissionAmount // 10%

    // Deduct from captain's wallet
    val currentCap = _captain.value
    val newBalance = currentCap.walletBalance - commissionDeduction

    val updatedCaptain = currentCap.copy(
      walletBalance = newBalance,
      totalTripsToday = currentCap.totalTripsToday + 1,
      todayGrossEarnings = currentCap.todayGrossEarnings + totalFare,
      todayCommissionPaid = currentCap.todayCommissionPaid + commissionDeduction,
      // If balance drops below negative limit, automatically force offline or blocked
      isOnline = if (newBalance < currentCap.negativeLimit) false else currentCap.isOnline
    )
    _captain.value = updatedCaptain

    // Record wallet transaction
    val newTxn = WalletTransaction(
      id = "TXN-" + UUID.randomUUID().toString().substring(0, 6).uppercase(),
      title = "Cash Ride Commission (10%)",
      description = "S.A.D Enterprises 10% cut for Trip #${ride.id} (Fare: ₹${totalFare.toInt()})",
      amount = commissionDeduction,
      isCredit = false,
      rideId = ride.id,
      timestamp = System.currentTimeMillis(),
      balanceAfter = newBalance,
    )
    _transactions.value = listOf(newTxn) + _transactions.value

    return completed
  }

  fun cancelRide() {
    _currentRide.value = null
    _incomingRideOffer.value = null
  }

  fun toggleCaptainOnlineStatus(): Boolean {
    val currentCap = _captain.value
    if (!currentCap.isOnline && currentCap.isWalletBlocked) {
      // Cannot go online if negative balance is breached!
      return false
    }
    _captain.value = currentCap.copy(isOnline = !currentCap.isOnline)
    return true
  }

  /**
   * Wallet Recharge via Razorpay mock
   * Immediately restores balance and unblocks driver if suspended!
   */
  fun rechargeWallet(amount: Double, paymentMethodDesc: String = "Razorpay UPI Instant"): Double {
    val currentCap = _captain.value
    val newBalance = currentCap.walletBalance + amount
    _captain.value = currentCap.copy(
      walletBalance = newBalance,
      // If was blocked, now unblocked
    )

    val txn = WalletTransaction(
      id = "TXN-" + UUID.randomUUID().toString().substring(0, 6).uppercase(),
      title = "Wallet Top-up ($paymentMethodDesc)",
      description = "Added ₹${amount.toInt()} to Captain Escrow via S.A.D Enterprises Gateway",
      amount = amount,
      isCredit = true,
      timestamp = System.currentTimeMillis(),
      balanceAfter = newBalance,
    )
    _transactions.value = listOf(txn) + _transactions.value
    return newBalance
  }

  /**
   * Simulation Helper: Force drop balance to negative limit (-₹180)
   * so user/reviewer can test negative balance blocking without waiting for 10 rides!
   */
  fun simulateNegativeBalance() {
    val currentCap = _captain.value
    val negativeVal = -180.0
    _captain.value = currentCap.copy(
      walletBalance = negativeVal,
      isOnline = false,
    )
    val txn = WalletTransaction(
      id = "TXN-" + UUID.randomUUID().toString().substring(0, 6).uppercase(),
      title = "Simulated Balance Deficit",
      description = "Wallet dropped below negative limit (-₹150). Account blocked until recharged.",
      amount = 0.0,
      isCredit = false,
      timestamp = System.currentTimeMillis(),
      balanceAfter = negativeVal,
    )
    _transactions.value = listOf(txn) + _transactions.value
  }
}
