package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CaptainProfile
import com.example.model.DriverKycStatus
import com.example.model.RideRecord
import com.example.model.RideStatus
import com.example.model.VehicleCategory
import com.example.ui.MainViewModel
import com.example.ui.theme.SadAmberDark
import com.example.ui.theme.SadAmberPrimary
import com.example.ui.theme.SadBlue
import com.example.ui.theme.SadEmerald
import com.example.ui.theme.SadMidnight
import com.example.ui.theme.SadRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminPlatformScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier,
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val adminTabs = listOf(
    "1. Captains & Wallets",
    "2. Live Fleet Trips",
    "3. Commission Config",
    "4. Web Portal Blueprint"
  )

  val platformConfig by viewModel.platformConfig.collectAsState()
  val allCaptains by viewModel.allCaptains.collectAsState()
  val allRides by viewModel.allRides.collectAsState()

  // Computed platform statistics
  val totalGmv = allRides.sumOf { it.fare.totalFare } + 3840.0
  val totalCommissionRevenue = allRides.sumOf { it.fare.commissionAmount } + 384.0
  val deficitCaptainsCount = allCaptains.count { it.isWalletBlocked }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Admin Header Hero Bar
    Surface(
      color = SadMidnight,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .background(SadAmberPrimary, RoundedCornerShape(10.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = null,
                tint = SadMidnight,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "S.A.D ENTERPRISES ADMIN",
                style = MaterialTheme.typography.titleMedium.copy(
                  color = Color.White,
                  fontWeight = FontWeight.Black,
                  letterSpacing = 0.5.sp
                )
              )
              Text(
                text = "Super-Admin Control Tower & Fleet Dispatch",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray, fontSize = 11.sp)
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = SadEmerald.copy(alpha = 0.2f),
            border = BorderStroke(1.dp, SadEmerald)
          ) {
            Text(
              text = "HQ LIVE",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = SadEmerald
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Platform KPI Badges
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          AdminMetricPill(title = "Total GMV", value = "₹${totalGmv.toInt()}", color = SadAmberPrimary)
          AdminMetricPill(title = "S.A.D 10% Cut", value = "₹${totalCommissionRevenue.toInt()}", color = SadEmerald)
          AdminMetricPill(title = "Captains", value = "${allCaptains.size}", color = SadBlue)
          AdminMetricPill(title = "In Deficit", value = "$deficitCaptainsCount", color = if (deficitCaptainsCount > 0) SadRed else SadEmerald)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Gemini AI Fleet Intelligence Action
        val aiReport by viewModel.adminReportText.collectAsState()
        val isReportLoading by viewModel.isAdminReportLoading.collectAsState()

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.White.copy(alpha = 0.08f),
          border = BorderStroke(1.dp, SadAmberPrimary.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = null,
                  tint = SadAmberPrimary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Gemini 3.8 Flash Fleet AI Analytics",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                )
              }

              Button(
                onClick = { viewModel.generateAdminAiReport() },
                enabled = !isReportLoading,
                colors = ButtonDefaults.buttonColors(
                  containerColor = SadAmberPrimary,
                  contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp)
              ) {
                if (isReportLoading) {
                  CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Analyzing...", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                } else {
                  Text(if (aiReport == null) "Generate AI Report" else "Regenerate", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            aiReport?.let { report ->
              Spacer(modifier = Modifier.height(10.dp))
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = SadMidnight.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = report,
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFE2E8F0),
                    lineHeight = 18.sp
                  ),
                  modifier = Modifier.padding(10.dp)
                )
              }
            }
          }
        }
      }
    }

    // Scrollable Admin Tabs
    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = SadAmberDark,
      edgePadding = 12.dp,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = SadAmberDark
        )
      }
    ) {
      adminTabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
              )
            )
          },
          modifier = Modifier.testTag("admin_tab_$index")
        )
      }
    }

    // Tab Body
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      when (selectedTab) {
        0 -> {
          // Tab 1: Captains Fleet & Wallet Balances
          item {
            Text(
              text = "Registered Captains & Live Wallet Status",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "Monitor balances, manage negative deficits (-₹150 limit), and approve KYC.",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Spacer(modifier = Modifier.height(12.dp))
          }

          items(allCaptains) { cap ->
            AdminCaptainCard(
              captain = cap,
              onAdjustWallet = { amount ->
                viewModel.adminAdjustCaptainWallet(cap.id, amount, "Admin Settlement Credit")
              },
              onToggleKyc = { newStatus ->
                viewModel.adminToggleCaptainKyc(cap.id, newStatus)
              }
            )
            Spacer(modifier = Modifier.height(10.dp))
          }
        }

        1 -> {
          // Tab 2: Live Fleet Trips
          item {
            Text(
              text = "Live Trip Dispatch & Revenue Audit",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "Real-time stream of all customer bookings, fares, and S.A.D 10% commission deductions.",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Spacer(modifier = Modifier.height(12.dp))
          }

          items(allRides) { ride ->
            AdminRideAuditCard(ride = ride)
            Spacer(modifier = Modifier.height(10.dp))
          }
        }

        2 -> {
          // Tab 3: Commission Engine & Rate Card
          item {
            AdminCommissionEngineCard(
              config = platformConfig,
              onSaveCommission = { percent -> viewModel.adminUpdateCommission(percent) },
              onSaveNegativeLimit = { limit -> viewModel.adminUpdateNegativeLimit(limit) }
            )
          }
        }

        3 -> {
          // Tab 4: Web Admin Portal Blueprint
          item {
            AdminWebPortalBlueprintCard()
          }
        }
      }

      item { Spacer(modifier = Modifier.height(30.dp)) }
    }
  }
}

@Composable
private fun AdminMetricPill(title: String, value: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleSmall.copy(
        fontWeight = FontWeight.ExtraBold,
        color = color
      )
    )
    Text(
      text = title,
      style = MaterialTheme.typography.labelSmall.copy(
        fontSize = 10.sp,
        color = Color.LightGray
      )
    )
  }
}

@Composable
private fun AdminCaptainCard(
  captain: CaptainProfile,
  onAdjustWallet: (Double) -> Unit,
  onToggleKyc: (DriverKycStatus) -> Unit,
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = if (captain.isWalletBlocked) BorderStroke(1.5.dp, SadRed) else null,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = captain.name,
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = when (captain.kycStatus) {
                DriverKycStatus.VERIFIED_ACTIVE -> Color(0xFFDCFCE7)
                DriverKycStatus.PENDING_REVIEW -> Color(0xFFFEF3C7)
                DriverKycStatus.SUSPENDED -> Color(0xFFFEE2E2)
              }
            ) {
              Text(
                text = captain.kycStatus.name.replace("_", " "),
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = when (captain.kycStatus) {
                    DriverKycStatus.VERIFIED_ACTIVE -> Color(0xFF15803D)
                    DriverKycStatus.PENDING_REVIEW -> Color(0xFFB45309)
                    DriverKycStatus.SUSPENDED -> Color(0xFFB91C1C)
                  }
                ),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
          Text(
            text = "${captain.id} • ${captain.vehicleModel} (${captain.vehiclePlate})",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 11.sp
            )
          )
        }

        // Wallet Balance Pill
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "Wallet: ₹${captain.walletBalance.toInt()}",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              color = if (captain.isWalletBlocked) SadRed else if (captain.walletBalance < 0) SadAmberDark else SadEmerald
            )
          )
          Text(
            text = if (captain.isWalletBlocked) "DEFICIT BLOCKED" else if (captain.isOnline) "ONLINE" else "OFFLINE",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = if (captain.isWalletBlocked) SadRed else if (captain.isOnline) SadEmerald else Color.Gray
            )
          )
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colorScheme.outlineVariant
      )

      // Admin Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (captain.kycStatus == DriverKycStatus.PENDING_REVIEW) {
          Button(
            onClick = { onToggleKyc(DriverKycStatus.VERIFIED_ACTIVE) },
            colors = ButtonDefaults.buttonColors(containerColor = SadEmerald, contentColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text("Approve KYC", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        } else {
          OutlinedButton(
            onClick = {
              val next = if (captain.kycStatus == DriverKycStatus.SUSPENDED) DriverKycStatus.VERIFIED_ACTIVE else DriverKycStatus.SUSPENDED
              onToggleKyc(next)
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = if (captain.kycStatus == DriverKycStatus.SUSPENDED) "Unsuspend" else "Suspend",
              fontSize = 11.sp
            )
          }
        }

        // Quick Admin Wallet Top-up / Unblock
        Button(
          onClick = { onAdjustWallet(250.0) },
          colors = ButtonDefaults.buttonColors(containerColor = SadAmberPrimary, contentColor = Color.Black),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Text("+₹250 Credit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun AdminRideAuditCard(ride: RideRecord) {
  val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
  val dateStr = dateFormat.format(Date(ride.createdAt))

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = SadMidnight,
            contentColor = SadAmberPrimary
          ) {
            Text(
              text = ride.vehicleCategory.displayName.replace("S.A.D ", ""),
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = ride.id,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
          )
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = when (ride.status) {
            RideStatus.COMPLETED -> Color(0xFFDCFCE7)
            RideStatus.IN_PROGRESS -> Color(0xFFE0F2FE)
            RideStatus.SEARCHING_CAPTAIN -> Color(0xFFFEF3C7)
            else -> MaterialTheme.colorScheme.surfaceVariant
          }
        ) {
          Text(
            text = ride.status.name,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              color = when (ride.status) {
                RideStatus.COMPLETED -> Color(0xFF15803D)
                RideStatus.IN_PROGRESS -> Color(0xFF0369A1)
                else -> Color(0xFFB45309)
              }
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "👤 ${ride.customerName} (${ride.customerPhone}) → 🚗 ${ride.captainName}",
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
      )
      Text(
        text = "📍 ${ride.pickupLocation.name} → 🏁 ${ride.dropLocation.name} (${ride.distanceKm} km)",
        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
      )

      HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Total Fare: ₹${ride.fare.totalFare.toInt()} (${ride.paymentMode.label})",
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = "S.A.D Cut (10%): ₹${ride.fare.commissionAmount.toInt()}",
          style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.ExtraBold,
            color = SadAmberDark
          )
        )
      }
    }
  }
}

@Composable
private fun AdminCommissionEngineCard(
  config: com.example.model.PlatformConfig,
  onSaveCommission: (Double) -> Unit,
  onSaveNegativeLimit: (Double) -> Unit,
) {
  var commissionSlider by remember { mutableDoubleStateOf(config.commissionPercent) }
  var negativeLimitSlider by remember { mutableDoubleStateOf(config.negativeLimit) }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Tune,
          contentDescription = null,
          tint = SadAmberDark,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Dynamic Commission & Deficit Engine",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Commission Rate Slider
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = "S.A.D Enterprises Commission Rate:", style = MaterialTheme.typography.bodyMedium)
        Text(
          text = "${commissionSlider.toInt()}%",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SadAmberDark)
        )
      }
      Slider(
        value = commissionSlider.toFloat(),
        onValueChange = { commissionSlider = it.toDouble() },
        valueRange = 5f..25f,
        steps = 19,
        colors = SliderDefaults.colors(thumbColor = SadAmberPrimary, activeTrackColor = SadAmberDark)
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Negative Balance Limit Slider
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = "Captain Negative Deficit Lock Threshold:", style = MaterialTheme.typography.bodyMedium)
        Text(
          text = "₹${negativeLimitSlider.toInt()}",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SadRed)
        )
      }
      Slider(
        value = negativeLimitSlider.toFloat(),
        onValueChange = { negativeLimitSlider = it.toDouble() },
        valueRange = -300f..-50f,
        steps = 24,
        colors = SliderDefaults.colors(thumbColor = SadRed, activeTrackColor = SadRed)
      )

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = {
          onSaveCommission(commissionSlider)
          onSaveNegativeLimit(negativeLimitSlider)
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SadMidnight, contentColor = SadAmberPrimary),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Apply Changes to Live Fleet", fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
private fun AdminWebPortalBlueprintCard() {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.AccountBalance,
          contentDescription = null,
          tint = SadBlue,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "FlutterFlow Web Admin Dashboard Architecture",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "For desktop admin management, S.A.D Enterprises operates a responsive Web Portal built in FlutterFlow Web or Retool connected to the same Firebase database:",
        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "1. Role-Based Access Control (RBAC):",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = SadAmberDark)
      )
      Text(
        text = "• Super Admin: Full access to financial escrow, commission sliders, and driver payouts.\n• Fleet Dispatcher: Live map tracking, manual booking dispatch, driver support.\n• Accounts Manager: Razorpay settlement reconciliations and GST tax filing reports.",
        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 17.sp)
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "2. Security Rules for Admin Collections:",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = SadAmberDark)
      )
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF0F172A),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
      ) {
        Text(
          text = """// firestore.rules
match /platform_settings/{settingId} {
  allow read: if request.auth != null;
  allow write: if request.auth.token.role == 'SUPER_ADMIN';
}
match /wallet_transactions/{txnId} {
  allow read: if request.auth != null;
  allow write: if request.auth.token.role == 'SUPER_ADMIN';
}""",
          style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color(0xFF38BDF8),
            lineHeight = 16.sp
          ),
          modifier = Modifier.padding(10.dp)
        )
      }
    }
  }
}
