package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.SadRideRepository
import com.example.model.DriverKycStatus
import com.example.model.FareBreakdown
import com.example.model.VehicleCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read app name string resource`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("S.A.D Ride", appName)
  }

  @Test
  fun `verify fare breakdown and 10 percent commission calculation`() {
    val distanceKm = 10.0
    val breakdown = FareBreakdown.calculate(VehicleCategory.AUTO, distanceKm)

    // Base fare: 30, Dist: 10 * 11 = 110, Platform: 5
    assertTrue(breakdown.totalFare > 100.0)
    // 10% commission
    assertEquals(Math.round(breakdown.totalFare * 0.10).toDouble(), breakdown.commissionAmount, 0.01)
    assertEquals(breakdown.totalFare - breakdown.commissionAmount, breakdown.driverEarnings, 0.01)
  }

  @Test
  fun `verify cash ride completion deducts 10 percent commission from captain wallet`() {
    val repo = SadRideRepository()
    val initialBalance = repo.captain.value.walletBalance

    // Book ride
    repo.bookRide()
    repo.captainAcceptRide()
    repo.startTripWithOtp("1234")

    // Complete ride
    val completed = repo.completeRide()
    val expectedCommission = completed!!.fare.commissionAmount

    val updatedBalance = repo.captain.value.walletBalance
    assertEquals(initialBalance - expectedCommission, updatedBalance, 0.01)

    // Verify transaction recorded
    val latestTxn = repo.transactions.value.first()
    assertFalse(latestTxn.isCredit)
    assertEquals(expectedCommission, latestTxn.amount, 0.01)
  }

  @Test
  fun `verify negative balance threshold blocks captain and recharge unblocks`() {
    val repo = SadRideRepository()
    assertFalse(repo.captain.value.isWalletBlocked)

    // Simulate balance dropping to -180 (threshold is -150)
    repo.simulateNegativeBalance()
    assertTrue(repo.captain.value.isWalletBlocked)
    assertFalse(repo.captain.value.isOnline)

    // Cannot go online while blocked
    val onlineAllowed = repo.toggleCaptainOnlineStatus()
    assertFalse(onlineAllowed)

    // Recharge wallet with ₹250
    repo.rechargeWallet(250.0)
    assertFalse(repo.captain.value.isWalletBlocked)
    assertTrue(repo.captain.value.walletBalance > 0)
  }

  @Test
  fun `verify admin controls for commission and captain wallet credit`() {
    val repo = SadRideRepository()

    // 1. Admin updates commission to 12%
    repo.adminUpdateCommission(12.0)
    assertEquals(12.0, repo.platformConfig.value.commissionPercent, 0.01)

    // 2. Admin adds ₹500 adjustment to a captain
    val captainId = "CAP-7890"
    val beforeBal = repo.captain.value.walletBalance
    repo.adminAdjustCaptainWallet(captainId, 500.0, "Bonus Incentive")
    assertEquals(beforeBal + 500.0, repo.captain.value.walletBalance, 0.01)

    // 3. Admin suspends captain
    repo.adminToggleCaptainKyc(captainId, DriverKycStatus.SUSPENDED)
    assertEquals(DriverKycStatus.SUSPENDED, repo.captain.value.kycStatus)
    assertTrue(repo.captain.value.isWalletBlocked)
  }
}
