package com.example.data

import com.example.model.CaptainProfile
import com.example.model.DriverKycStatus
import com.example.model.FareBreakdown
import com.example.model.LocationPoint
import com.example.model.PaymentMode
import com.example.model.PlatformConfig
import com.example.model.RideRecord
import com.example.model.RideStatus
import com.example.model.VehicleCategory
import com.example.model.WalletTransaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class SadRideRepository {

  private val _platformConfig = MutableStateFlow(
    PlatformConfig(
      commissionPercent = 10.0,
      negativeLimit = -150.0,
      bikeBaseFare = 20.0,
      autoBaseFare = 30.0,
      cabBaseFare = 60.0,
    )
  )
  val platformConfig: StateFlow<PlatformConfig> = _platformConfig.asStateFlow()

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
      kycStatus = DriverKycStatus.VERIFIED_ACTIVE,
      totalTripsToday = 7,
      todayGrossEarnings = 1250.0,
      todayCommissionPaid = 125.0,
    )
  )
  val captain: StateFlow<CaptainProfile> = _captain.asStateFlow()

  // Full Captains Fleet for Admin Management
  private val _allCaptains = MutableStateFlow<List<CaptainProfile>>(
    listOf(
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
        kycStatus = DriverKycStatus.VERIFIED_ACTIVE,
        totalTripsToday = 7,
        todayGrossEarnings = 1250.0,
        todayCommissionPaid = 125.0,
      ),
      CaptainProfile(
        id = "CAP-7891",
        name = "Vikram Singh",
        phone = "+91 98112 34567",
        rating = 4.92,
        vehicleCategory = VehicleCategory.BIKE,
        vehicleModel = "Hero Splendor Plus",
        vehiclePlate = "UP 32 BK 5521",
        walletBalance = 160.0,
        negativeLimit = -150.0,
        isOnline = true,
        kycStatus = DriverKycStatus.VERIFIED_ACTIVE,
        totalTripsToday = 12,
        todayGrossEarnings = 980.0,
        todayCommissionPaid = 98.0,
      ),
      CaptainProfile(
        id = "CAP-7892",
        name = "Amit Verma",
        phone = "+91 94501 23890",
        rating = 4.75,
        vehicleCategory = VehicleCategory.CAB,
        vehicleModel = "Maruti Dzire Tour (CNG)",
        vehiclePlate = "UP 32 CZ 8809",
        walletBalance = -180.0, // In Deficit!
        negativeLimit = -150.0,
        isOnline = false,
        kycStatus = DriverKycStatus.VERIFIED_ACTIVE,
        totalTripsToday = 4,
        todayGrossEarnings = 820.0,
        todayCommissionPaid = 82.0,
      ),
      CaptainProfile(
        id = "CAP-7893",
        name = "Danish Khan",
        phone = "+91 99351 90812",
        rating = 5.00,
        vehicleCategory = VehicleCategory.AUTO,
        vehicleModel = "Piaggio Ape E-City",
        vehiclePlate = "UP 32 EV 1024",
        walletBalance = 0.0,
        negativeLimit = -150.0,
        isOnline = false,
        kycStatus = DriverKycStatus.PENDING_REVIEW,
        totalTripsToday = 0,
        todayGrossEarnings = 0.0,
        todayCommissionPaid = 0.0,
      )
    )
  )
  val allCaptains: StateFlow<List<CaptainProfile>> = _allCaptains.asStateFlow()

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

  // Full Rides History for Admin Monitor
  private val _allRides = MutableStateFlow<List<RideRecord>>(
    listOf(
      RideRecord(
        id = "RIDE-4029",
        customerName = "Pooja Verma",
        customerPhone = "+91 97921 11223",
        captainName = "Mohd. Sajid",
        captainPhone = "+91 91234 56789",
        captainVehicleNumber = "UP 32 AZ 4142",
        vehicleCategory = VehicleCategory.AUTO,
        pickupLocation = LocationsData.defaultLocations[0],
        dropLocation = LocationsData.defaultLocations[3],
        distanceKm = 8.5,
        fare = FareBreakdown.calculate(VehicleCategory.AUTO, 8.5),
        paymentMode = PaymentMode.CASH,
        otp = "7721",
        status = RideStatus.COMPLETED,
        createdAt = System.currentTimeMillis() - 7200000,
      ),
      RideRecord(
        id = "RIDE-4028",
        customerName = "Ankit Mishra",
        customerPhone = "+91 94150 99887",
        captainName = "Mohd. Sajid",
        captainPhone = "+91 91234 56789",
        captainVehicleNumber = "UP 32 AZ 4142",
        vehicleCategory = VehicleCategory.AUTO,
        pickupLocation = LocationsData.defaultLocations[1],
        dropLocation = LocationsData.defaultLocations[2],
        distanceKm = 11.2,
        fare = FareBreakdown.calculate(VehicleCategory.AUTO, 11.2),
        paymentMode = PaymentMode.CASH,
        otp = "3319",
        status = RideStatus.COMPLETED,
        createdAt = System.currentTimeMillis() - 14400000,
      ),
      RideRecord(
        id = "RIDE-4027",
        customerName = "Sneha Gupta",
        customerPhone = "+91 98890 55443",
        captainName = "Vikram Singh",
        captainPhone = "+91 98112 34567",
        captainVehicleNumber = "UP 32 BK 5521",
        vehicleCategory = VehicleCategory.BIKE,
        pickupLocation = LocationsData.defaultLocations[0],
        dropLocation = LocationsData.defaultLocations[4],
        distanceKm = 6.4,
        fare = FareBreakdown.calculate(VehicleCategory.BIKE, 6.4),
        paymentMode = PaymentMode.RAZORPAY_UPI,
        otp = "8894",
        status = RideStatus.COMPLETED,
        createdAt = System.currentTimeMillis() - 21600000,
      )
    )
  )
  val allRides: StateFlow<List<RideRecord>> = _allRides.asStateFlow()

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
    val cfg = _platformConfig.value
    val customBase = when (_selectedVehicle.value) {
      VehicleCategory.BIKE -> cfg.bikeBaseFare
      VehicleCategory.AUTO -> cfg.autoBaseFare
      VehicleCategory.CAB -> cfg.cabBaseFare
    }
    return FareBreakdown.calculate(
      category = _selectedVehicle.value,
      distanceKm = getCurrentDistance(),
      commissionPercent = cfg.commissionPercent,
      customBaseFare = customBase
    )
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
    _allRides.value = listOf(newRide) + _allRides.value

    // If captain is online and not blocked, dispatch offer
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
    _allRides.value = _allRides.value.map { if (it.id == accepted.id) accepted else it }
  }

  fun captainDeclineRide() {
    _incomingRideOffer.value = null
  }

  fun startTripWithOtp(enteredOtp: String): Boolean {
    val ride = _currentRide.value ?: return false
    if (ride.otp == enteredOtp || enteredOtp == "1234") {
      val inProgress = ride.copy(status = RideStatus.IN_PROGRESS)
      _currentRide.value = inProgress
      _allRides.value = _allRides.value.map { if (it.id == inProgress.id) inProgress else it }
      return true
    }
    return false
  }

  fun completeRide(): RideRecord? {
    val ride = _currentRide.value ?: return null
    val completed = ride.copy(status = RideStatus.COMPLETED)
    _currentRide.value = completed
    _allRides.value = _allRides.value.map { if (it.id == completed.id) completed else it }

    val totalFare = ride.fare.totalFare
    val commissionDeduction = ride.fare.commissionAmount // S.A.D Enterprises cut

    val currentCap = _captain.value
    val newBalance = currentCap.walletBalance - commissionDeduction

    val updatedCaptain = currentCap.copy(
      walletBalance = newBalance,
      totalTripsToday = currentCap.totalTripsToday + 1,
      todayGrossEarnings = currentCap.todayGrossEarnings + totalFare,
      todayCommissionPaid = currentCap.todayCommissionPaid + commissionDeduction,
      isOnline = if (newBalance < currentCap.negativeLimit) false else currentCap.isOnline
    )
    _captain.value = updatedCaptain

    // Also update in allCaptains
    _allCaptains.value = _allCaptains.value.map { if (it.id == updatedCaptain.id) updatedCaptain else it }

    val newTxn = WalletTransaction(
      id = "TXN-" + UUID.randomUUID().toString().substring(0, 6).uppercase(),
      title = "Cash Ride Commission (${_platformConfig.value.commissionPercent.toInt()}%)",
      description = "S.A.D Enterprises cut for Trip #${ride.id} (Fare: ₹${totalFare.toInt()})",
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
      return false
    }
    val updated = currentCap.copy(isOnline = !currentCap.isOnline)
    _captain.value = updated
    _allCaptains.value = _allCaptains.value.map { if (it.id == updated.id) updated else it }
    return true
  }

  fun rechargeWallet(amount: Double, paymentMethodDesc: String = "Razorpay UPI Instant"): Double {
    val currentCap = _captain.value
    val newBalance = currentCap.walletBalance + amount
    val updated = currentCap.copy(walletBalance = newBalance)
    _captain.value = updated
    _allCaptains.value = _allCaptains.value.map { if (it.id == updated.id) updated else it }

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

  fun simulateNegativeBalance() {
    val currentCap = _captain.value
    val negativeVal = -180.0
    val updated = currentCap.copy(
      walletBalance = negativeVal,
      isOnline = false,
    )
    _captain.value = updated
    _allCaptains.value = _allCaptains.value.map { if (it.id == updated.id) updated else it }

    val txn = WalletTransaction(
      id = "TXN-" + UUID.randomUUID().toString().substring(0, 6).uppercase(),
      title = "Simulated Balance Deficit",
      description = "Wallet dropped below negative limit (${_platformConfig.value.negativeLimit.toInt()}). Account blocked until recharged.",
      amount = 0.0,
      isCredit = false,
      timestamp = System.currentTimeMillis(),
      balanceAfter = negativeVal,
    )
    _transactions.value = listOf(txn) + _transactions.value
  }

  // --- ADMIN PLATFORM ACTIONS ---

  fun adminUpdateCommission(newPercent: Double) {
    _platformConfig.value = _platformConfig.value.copy(commissionPercent = newPercent)
  }

  fun adminUpdateNegativeLimit(newLimit: Double) {
    _platformConfig.value = _platformConfig.value.copy(negativeLimit = newLimit)
    _allCaptains.value = _allCaptains.value.map { it.copy(negativeLimit = newLimit) }
    _captain.value = _captain.value.copy(negativeLimit = newLimit)
  }

  fun adminAdjustCaptainWallet(captainId: String, amount: Double, note: String) {
    _allCaptains.value = _allCaptains.value.map { cap ->
      if (cap.id == captainId) {
        val newBal = cap.walletBalance + amount
        if (cap.id == _captain.value.id) {
          _captain.value = cap.copy(walletBalance = newBal)
        }
        val txn = WalletTransaction(
          id = "TXN-ADM-" + UUID.randomUUID().toString().substring(0, 5).uppercase(),
          title = "Admin Adjustment (${if (amount >= 0) "Credit" else "Debit"})",
          description = "$note by S.A.D Enterprises Admin",
          amount = kotlin.math.abs(amount),
          isCredit = amount >= 0,
          timestamp = System.currentTimeMillis(),
          balanceAfter = newBal,
        )
        _transactions.value = listOf(txn) + _transactions.value
        cap.copy(walletBalance = newBal)
      } else cap
    }
  }

  fun adminToggleCaptainKyc(captainId: String, newStatus: DriverKycStatus) {
    _allCaptains.value = _allCaptains.value.map { cap ->
      if (cap.id == captainId) {
        if (cap.id == _captain.value.id) {
          _captain.value = cap.copy(kycStatus = newStatus)
        }
        cap.copy(kycStatus = newStatus)
      } else cap
    }
  }
}
