package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiResponseState
import com.example.ai.SadRideAiService
import com.example.data.LocationsData
import com.example.data.SadRideRepository
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppMode(val label: String, val subtitle: String) {
  CUSTOMER_RIDE("S.A.D Ride", "Customer App"),
  CAPTAIN_APP("S.A.D Captain", "Driver Partner App"),
  ADMIN_PLATFORM("Admin", "S.A.D Control Tower"),
  AI_COPILOT("S.A.D Saathi", "Gemini AI Co-Pilot"),
  BLUEPRINT_HUB("Blueprint", "No-Code & Schema Guide"),
}

class MainViewModel(
  private val repository: SadRideRepository = SadRideRepository(),
  private val aiService: SadRideAiService = SadRideAiService(),
) : ViewModel() {

  private val _activeMode = MutableStateFlow(AppMode.CUSTOMER_RIDE)
  val activeMode: StateFlow<AppMode> = _activeMode.asStateFlow()

  val pickupLocation: StateFlow<LocationPoint> = repository.pickupLocation
  val dropLocation: StateFlow<LocationPoint> = repository.dropLocation
  val selectedVehicle: StateFlow<VehicleCategory> = repository.selectedVehicle
  val selectedPayment: StateFlow<PaymentMode> = repository.selectedPayment
  val currentRide: StateFlow<RideRecord?> = repository.currentRide
  val captain: StateFlow<CaptainProfile> = repository.captain
  val transactions: StateFlow<List<WalletTransaction>> = repository.transactions
  val incomingRideOffer: StateFlow<RideRecord?> = repository.incomingRideOffer

  // Admin Data Flows
  val platformConfig: StateFlow<PlatformConfig> = repository.platformConfig
  val allCaptains: StateFlow<List<CaptainProfile>> = repository.allCaptains
  val allRides: StateFlow<List<RideRecord>> = repository.allRides

  // Gemini AI Data Flows
  private val _aiState = MutableStateFlow<AiResponseState>(AiResponseState.Idle)
  val aiState: StateFlow<AiResponseState> = _aiState.asStateFlow()

  private val _adminReportText = MutableStateFlow<String?>(null)
  val adminReportText: StateFlow<String?> = _adminReportText.asStateFlow()

  private val _isAdminReportLoading = MutableStateFlow(false)
  val isAdminReportLoading: StateFlow<Boolean> = _isAdminReportLoading.asStateFlow()

  // UI state flags
  private val _showFareBreakdownDialog = MutableStateFlow(false)
  val showFareBreakdownDialog: StateFlow<Boolean> = _showFareBreakdownDialog.asStateFlow()

  private val _showRechargeDialog = MutableStateFlow(false)
  val showRechargeDialog: StateFlow<Boolean> = _showRechargeDialog.asStateFlow()

  private val _statusBannerMessage = MutableStateFlow<String?>(null)
  val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

  // Captain incoming request countdown (seconds)
  private val _offerTimerSeconds = MutableStateFlow(15)
  val offerTimerSeconds: StateFlow<Int> = _offerTimerSeconds.asStateFlow()

  init {
    viewModelScope.launch {
      incomingRideOffer.collect { offer ->
        if (offer != null) {
          _offerTimerSeconds.value = 15
          for (i in 15 downTo 1) {
            delay(1000)
            if (incomingRideOffer.value == null) break
            _offerTimerSeconds.value = i - 1
          }
          if (incomingRideOffer.value != null && _offerTimerSeconds.value <= 0) {
            repository.captainDeclineRide()
          }
        }
      }
    }
  }

  fun setAppMode(mode: AppMode) {
    _activeMode.value = mode
  }

  fun selectPickup(location: LocationPoint) {
    repository.setPickupLocation(location)
  }

  fun selectDrop(location: LocationPoint) {
    repository.setDropLocation(location)
  }

  fun selectVehicle(category: VehicleCategory) {
    repository.setSelectedVehicle(category)
  }

  fun selectPayment(mode: PaymentMode) {
    repository.setSelectedPayment(mode)
  }

  fun openFareBreakdown() {
    _showFareBreakdownDialog.value = true
  }

  fun closeFareBreakdown() {
    _showFareBreakdownDialog.value = false
  }

  fun openRechargeDialog() {
    _showRechargeDialog.value = true
  }

  fun closeRechargeDialog() {
    _showRechargeDialog.value = false
  }

  fun clearStatusBanner() {
    _statusBannerMessage.value = null
  }

  fun bookRideFromCustomer() {
    val ride = repository.bookRide()
    _statusBannerMessage.value = "Searching for nearby S.A.D Captains... OTP: ${ride.otp}"

    viewModelScope.launch {
      delay(3500)
      if (repository.currentRide.value?.status == RideStatus.SEARCHING_CAPTAIN) {
        if (!repository.captain.value.isWalletBlocked) {
          repository.captainAcceptRide()
          _statusBannerMessage.value = "Captain assigned! ${repository.captain.value.name} is on the way."
        }
      }
    }
  }

  fun captainAcceptOffer() {
    repository.captainAcceptRide()
    _statusBannerMessage.value = "Ride Accepted! Navigate to pickup point."
  }

  fun captainDeclineOffer() {
    repository.captainDeclineRide()
    _statusBannerMessage.value = "Ride Offer Declined."
  }

  fun captainStartTrip(otp: String): Boolean {
    val success = repository.startTripWithOtp(otp)
    if (success) {
      _statusBannerMessage.value = "Trip started! Driving towards destination."
    } else {
      _statusBannerMessage.value = "Invalid OTP! Please check with customer."
    }
    return success
  }

  fun captainCompleteTrip() {
    val completed = repository.completeRide()
    if (completed != null) {
      val fare = completed.fare
      val commission = fare.commissionAmount.toInt()
      _statusBannerMessage.value = "Trip Completed! Total Fare ₹${fare.totalFare.toInt()} collected in Cash. ${fare.commissionRatePercent.toInt()}% S.A.D Commission (-₹$commission) deducted from Wallet."
    }
  }

  fun cancelCurrentRide() {
    repository.cancelRide()
    _statusBannerMessage.value = "Ride has been cancelled."
  }

  fun toggleCaptainOnline(): Boolean {
    val success = repository.toggleCaptainOnlineStatus()
    if (!success) {
      _statusBannerMessage.value = "Cannot Go Online! Wallet balance is below negative limit (${repository.platformConfig.value.negativeLimit.toInt()}). Please recharge wallet."
    } else {
      val isNowOnline = repository.captain.value.isOnline
      _statusBannerMessage.value = if (isNowOnline) "Captain is now ONLINE & ready for rides!" else "Captain is OFFLINE."
    }
    return success
  }

  fun rechargeCaptainWallet(amount: Double) {
    val newBal = repository.rechargeWallet(amount, "Razorpay UPI")
    _showRechargeDialog.value = false
    _statusBannerMessage.value = "Wallet Recharged! +₹${amount.toInt()} added. New Balance: ₹${newBal.toInt()}."
  }

  fun triggerSimulatedNegativeBalance() {
    repository.simulateNegativeBalance()
    _statusBannerMessage.value = "Negative Balance Triggered (-₹180)! Captain is BLOCKED until recharge."
  }

  fun getFareBreakdown(): FareBreakdown {
    return repository.getCurrentFareBreakdown()
  }

  fun getDistanceKm(): Double {
    return repository.getCurrentDistance()
  }

  // --- GEMINI AI ASSISTANT ACTIONS ---

  fun askAiCopilot(prompt: String) {
    viewModelScope.launch {
      aiService.askAssistant(prompt).collect { state ->
        _aiState.value = state
      }
    }
  }

  fun clearAiResponse() {
    _aiState.value = AiResponseState.Idle
  }

  fun generateAdminAiReport() {
    viewModelScope.launch {
      _isAdminReportLoading.value = true
      try {
        val totalGmv = allRides.value.sumOf { it.fare.totalFare } + 3840.0
        val totalCommission = allRides.value.sumOf { it.fare.commissionAmount } + 384.0
        val activeCount = allCaptains.value.count { it.isOnline }
        val deficitCount = allCaptains.value.count { it.isWalletBlocked }

        val report = aiService.generateFleetReport(
          totalGmv = totalGmv,
          totalCommission = totalCommission,
          activeCaptainsCount = activeCount,
          deficitCaptainsCount = deficitCount,
        )
        _adminReportText.value = report
      } catch (e: Exception) {
        _statusBannerMessage.value = "AI Report failed: ${e.localizedMessage}"
      } finally {
        _isAdminReportLoading.value = false
      }
    }
  }

  // --- ADMIN ACTIONS ---

  fun adminUpdateCommission(percent: Double) {
    repository.adminUpdateCommission(percent)
    _statusBannerMessage.value = "Admin: Commission rate set to ${percent.toInt()}% for all future rides."
  }

  fun adminUpdateNegativeLimit(limit: Double) {
    repository.adminUpdateNegativeLimit(limit)
    _statusBannerMessage.value = "Admin: Captain negative deficit limit set to ₹${limit.toInt()}."
  }

  fun adminAdjustCaptainWallet(captainId: String, amount: Double, note: String) {
    repository.adminAdjustCaptainWallet(captainId, amount, note)
    _statusBannerMessage.value = "Admin: Added ₹${amount.toInt()} adjustment to Captain ($captainId)."
  }

  fun adminToggleCaptainKyc(captainId: String, newStatus: DriverKycStatus) {
    repository.adminToggleCaptainKyc(captainId, newStatus)
    _statusBannerMessage.value = "Admin: Captain ($captainId) status updated to ${newStatus.name}."
  }
}
