package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RideRecord
import com.example.model.RideStatus
import com.example.ui.MainViewModel
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.components.WalletRechargeDialog
import com.example.ui.theme.SadAmberDark
import com.example.ui.theme.SadAmberPrimary
import com.example.ui.theme.SadBlue
import com.example.ui.theme.SadEmerald
import com.example.ui.theme.SadMidnight
import com.example.ui.theme.SadRed

@Composable
fun CaptainScreen(
  viewModel: MainViewModel,
  onNavigateToWallet: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val captain by viewModel.captain.collectAsState()
  val incomingOffer by viewModel.incomingRideOffer.collectAsState()
  val currentRide by viewModel.currentRide.collectAsState()
  val offerTimer by viewModel.offerTimerSeconds.collectAsState()
  val showRecharge by viewModel.showRechargeDialog.collectAsState()
  val pickup by viewModel.pickupLocation.collectAsState()
  val drop by viewModel.dropLocation.collectAsState()

  var enteredOtp by remember { mutableStateOf("") }
  var otpError by remember { mutableStateOf<String?>(null) }

  Box(modifier = modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // 1. Captain Header Bar (Vehicle, Status & Online Switch)
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SadMidnight),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                  .size(46.dp)
                  .background(SadAmberPrimary.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = SadAmberPrimary,
                  modifier = Modifier.size(26.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = captain.name,
                  style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                  )
                )
                Text(
                  text = "${captain.vehicleModel} • ${captain.vehiclePlate}",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.LightGray,
                    fontSize = 11.sp
                  )
                )
              }
            }

            // Online / Offline Switch
            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = if (captain.isOnline) "ONLINE" else "OFFLINE",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = if (captain.isOnline) SadEmerald else Color.Gray
                )
              )
              Switch(
                checked = captain.isOnline,
                onCheckedChange = { viewModel.toggleCaptainOnline() },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = SadEmerald,
                  checkedTrackColor = SadEmerald.copy(alpha = 0.4f),
                  uncheckedThumbColor = Color.LightGray,
                  uncheckedTrackColor = Color.DarkGray
                ),
                modifier = Modifier.testTag("captain_online_toggle")
              )
            }
          }

          HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = Color.White.copy(alpha = 0.15f)
          )

          // Wallet Balance Quick Bar
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onNavigateToWallet() }
              .testTag("captain_wallet_strip"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = if (captain.isWalletBlocked) SadRed else SadAmberPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "Digital Wallet Balance",
                  style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                )
                Text(
                  text = "₹${captain.walletBalance.toInt()}",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (captain.isWalletBlocked) SadRed else if (captain.walletBalance < 0) SadAmberPrimary else SadEmerald
                  )
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = SadAmberPrimary.copy(alpha = 0.2f),
              modifier = Modifier.clickable { onNavigateToWallet() }
            ) {
              Text(
                text = "Earnings & Ledger >",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = SadAmberPrimary,
                  fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 2. CRITICAL FEATURE: Negative Balance Blocking Warning Banner
      AnimatedVisibility(
        visible = captain.isWalletBlocked,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
          border = BorderStroke(1.5.dp, SadRed),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("negative_balance_block_banner")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = SadRed,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "ACCOUNT RESTRICTED: NEGATIVE BALANCE",
                style = MaterialTheme.typography.titleSmall.copy(
                  color = SadRed,
                  fontWeight = FontWeight.Bold
                )
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Your digital wallet has fallen to ₹${captain.walletBalance.toInt()} which exceeds S.A.D Enterprises negative threshold (₹${captain.negativeLimit.toInt()}). You cannot accept new rides until you recharge.",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF7F1D1D))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
              onClick = { viewModel.openRechargeDialog() },
              colors = ButtonDefaults.buttonColors(containerColor = SadRed, contentColor = Color.White),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("recharge_now_banner_button")
            ) {
              Text("Recharge Wallet Now (Razorpay)", fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 3. Incoming Ride Request Notification Offer Card
      AnimatedVisibility(
        visible = incomingOffer != null && !captain.isWalletBlocked,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        incomingOffer?.let { offer ->
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(2.dp, SadAmberPrimary),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("incoming_ride_offer_card")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = SadAmberDark,
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "New Ride Request!",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                  )
                }

                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = if (offerTimer > 5) SadAmberPrimary else SadRed,
                  contentColor = Color.Black
                ) {
                  Text(
                    text = "${offerTimer}s",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Earnings preview
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(SadMidnight, RoundedCornerShape(10.dp))
                  .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "Cash to Collect",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                  )
                  Text(
                    text = "₹${offer.fare.totalFare.toInt()}",
                    style = MaterialTheme.typography.titleLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = Color.White
                    )
                  )
                }
                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = "Your Net (90%)",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                  )
                  Text(
                    text = "₹${offer.fare.driverEarnings.toInt()}",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = SadEmerald
                    )
                  )
                  Text(
                    text = "10% Commission: -₹${offer.fare.commissionAmount.toInt()}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = SadAmberPrimary,
                      fontSize = 10.sp
                    )
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Pickup & Drop
              Text(
                text = "📍 Pickup: ${offer.pickupLocation.name}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
              )
              Text(
                text = "🏁 Drop: ${offer.dropLocation.name} (${offer.distanceKm} km)",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              )

              Spacer(modifier = Modifier.height(14.dp))

              // Accept & Decline Buttons
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                OutlinedButton(
                  onClick = { viewModel.captainDeclineOffer() },
                  modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .testTag("captain_decline_offer_btn"),
                  shape = RoundedCornerShape(10.dp),
                  border = BorderStroke(1.dp, Color.Gray)
                ) {
                  Text("Decline", color = Color.Gray)
                }

                Button(
                  onClick = { viewModel.captainAcceptOffer() },
                  modifier = Modifier
                    .weight(2f)
                    .height(46.dp)
                    .testTag("captain_accept_offer_btn"),
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = SadEmerald,
                    contentColor = Color.White
                  )
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Accept Ride", fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 4. In-Trip Active Ride Controller
      currentRide?.let { ride ->
        if (ride.status == RideStatus.ARRIVING_PICKUP || ride.status == RideStatus.IN_PROGRESS) {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("captain_active_trip_card")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = if (ride.status == RideStatus.ARRIVING_PICKUP) "Go to Pickup Point" else "Driving to Destination",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = SadBlue
                    )
                  )
                  Text(
                    text = "Customer: ${ride.customerName} • ${ride.customerPhone}",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                  )
                }
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = SadBlue.copy(alpha = 0.15f)
                ) {
                  Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = "Navigate",
                    tint = SadBlue,
                    modifier = Modifier
                      .padding(8.dp)
                      .size(20.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // If Arriving: Needs Customer OTP to start
              if (ride.status == RideStatus.ARRIVING_PICKUP) {
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                      text = "Enter Customer 4-digit OTP to Start Trip:",
                      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.spacedBy(8.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = {
                          if (it.length <= 4) {
                            enteredOtp = it
                            otpError = null
                          }
                        },
                        placeholder = { Text("e.g. ${ride.otp}") },
                        modifier = Modifier
                          .weight(1f)
                          .testTag("captain_otp_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                      )
                      Button(
                        onClick = {
                          val success = viewModel.captainStartTrip(enteredOtp)
                          if (!success) {
                            otpError = "Incorrect OTP! Try ${ride.otp}"
                          }
                        },
                        modifier = Modifier
                          .height(52.dp)
                          .testTag("captain_start_trip_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = SadAmberPrimary, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                      ) {
                        Text("Start Trip", fontWeight = FontWeight.Bold)
                      }
                    }
                    if (otpError != null) {
                      Text(
                        text = otpError ?: "",
                        style = MaterialTheme.typography.labelSmall.copy(color = SadRed),
                        modifier = Modifier.padding(top = 4.dp)
                      )
                    }
                  }
                }
              }

              // If In Progress: Show Complete Cash Ride Button
              if (ride.status == RideStatus.IN_PROGRESS) {
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = Color(0xFFFEF3C7),
                  border = BorderStroke(1.dp, SadAmberDark),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                      text = "Trip in Progress • Destination: ${ride.dropLocation.name}",
                      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = "When completed, you will collect ₹${ride.fare.totalFare.toInt()} cash. App will deduct 10% commission (-₹${ride.fare.commissionAmount.toInt()}) automatically.",
                      style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF78350F), fontSize = 11.sp)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                  onClick = { viewModel.captainCompleteTrip() },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("captain_complete_trip_btn"),
                  colors = ButtonDefaults.buttonColors(containerColor = SadEmerald, contentColor = Color.White),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Collect ₹${ride.fare.totalFare.toInt()} Cash & Complete Trip",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 5. Daily Performance Overview Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Today's Performance",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            CaptainStatItem(title = "Gross Fare", value = "₹${captain.todayGrossEarnings.toInt()}", color = SadAmberDark)
            CaptainStatItem(title = "Trips", value = "${captain.totalTripsToday}", color = SadBlue)
            CaptainStatItem(title = "10% Comm.", value = "₹${captain.todayCommissionPaid.toInt()}", color = SadRed)
            CaptainStatItem(title = "Rating", value = "${captain.rating} ★", color = SadEmerald)
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Quick testing helper button to simulate negative limit!
          OutlinedButton(
            onClick = { viewModel.triggerSimulatedNegativeBalance() },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("simulate_negative_balance_btn"),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, SadRed)
          ) {
            Icon(
              imageVector = Icons.Default.Warning,
              contentDescription = null,
              tint = SadRed,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Test Negative Balance Lock (Simulate -₹180)",
              color = SadRed,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    // Modal Wallet Recharge Dialog
    if (showRecharge) {
      WalletRechargeDialog(
        currentBalance = captain.walletBalance,
        negativeLimit = captain.negativeLimit,
        onRecharge = { amount -> viewModel.rechargeCaptainWallet(amount) },
        onDismiss = { viewModel.closeRechargeDialog() }
      )
    }
  }
}

@Composable
private fun CaptainStatItem(title: String, value: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = color)
    )
    Text(
      text = title,
      style = MaterialTheme.typography.labelSmall.copy(
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )
  }
}
