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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LocationsData
import com.example.model.FareBreakdown
import com.example.model.PaymentMode
import com.example.model.RideRecord
import com.example.model.RideStatus
import com.example.model.VehicleCategory
import com.example.ui.MainViewModel
import com.example.ui.components.FareBreakdownDialog
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.theme.SadAmberDark
import com.example.ui.theme.SadAmberPrimary
import com.example.ui.theme.SadBlue
import com.example.ui.theme.SadEmerald
import com.example.ui.theme.SadMidnight
import com.example.ui.theme.SadRed

@Composable
fun CustomerRideScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier,
) {
  val pickup by viewModel.pickupLocation.collectAsState()
  val drop by viewModel.dropLocation.collectAsState()
  val selectedVehicle by viewModel.selectedVehicle.collectAsState()
  val selectedPayment by viewModel.selectedPayment.collectAsState()
  val currentRide by viewModel.currentRide.collectAsState()
  val showFareDialog by viewModel.showFareBreakdownDialog.collectAsState()

  val distance = viewModel.getDistanceKm()
  val fareBreakdown = viewModel.getFareBreakdown()
  val rideStatus = currentRide?.status ?: RideStatus.IDLE

  Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
      // 1. Live Interactive Simulated Map Section (36% screen height)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.38f)
      ) {
        InteractiveMapCanvas(
          pickup = pickup,
          drop = drop,
          rideStatus = rideStatus,
          vehicleCategory = selectedVehicle,
          modifier = Modifier.fillMaxSize()
        )
      }

      // 2. Booking Drawer & Selection Section (62% screen height)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.62f)
          .background(MaterialTheme.colorScheme.background)
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        // If there's an active booking, show Live Ride Status card!
        AnimatedVisibility(
          visible = currentRide != null && rideStatus != RideStatus.IDLE && rideStatus != RideStatus.COMPLETED,
          enter = fadeIn() + expandVertically(),
          exit = fadeOut() + shrinkVertically()
        ) {
          currentRide?.let { ride ->
            ActiveRideCard(
              ride = ride,
              onCancel = { viewModel.cancelCurrentRide() }
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Pickup & Drop Selector Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("location_picker_card")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Pickup
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .background(SadEmerald, CircleShape)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "PICKUP",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SadEmerald
                  )
                )
                Text(
                  text = pickup.name,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
              }
            }

            HorizontalDivider(
              modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
              color = MaterialTheme.colorScheme.outlineVariant
            )

            // Drop
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .background(SadAmberDark, CircleShape)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "WHERE TO (DROP)",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SadAmberDark
                  )
                )
                Text(
                  text = drop.name,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
              }
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
              ) {
                Text(
                  text = "$distance km",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Location Change Chips
            Text(
              text = "Quick Select Drop Location:",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
              items(LocationsData.defaultLocations) { loc ->
                val isSelected = loc.name == drop.name
                Surface(
                  shape = RoundedCornerShape(20.dp),
                  border = BorderStroke(
                    1.dp,
                    if (isSelected) SadAmberDark else MaterialTheme.colorScheme.outlineVariant
                  ),
                  color = if (isSelected) SadAmberPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                  modifier = Modifier
                    .clickable { viewModel.selectDrop(loc) }
                    .testTag("location_chip_${loc.name.take(6)}")
                ) {
                  Text(
                    text = loc.name.split(" ").take(2).joinToString(" "),
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      color = if (isSelected) SadAmberDark else MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Vehicle Category Selector
        Text(
          text = "Choose Vehicle",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          VehicleCategory.values().forEach { category ->
            val isSelected = category == selectedVehicle
            val estimatedFare = FareBreakdown.calculate(category, distance)
            VehicleOptionCard(
              category = category,
              fare = estimatedFare.totalFare.toInt(),
              etaMins = estimatedFare.estimatedMins,
              isSelected = isSelected,
              modifier = Modifier.weight(1f),
              onClick = { viewModel.selectVehicle(category) }
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Fare Breakdown Sheet Banner Link
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.openFareBreakdown() }
            .testTag("open_fare_breakdown_btn")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = SadAmberDark,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "View Detailed Fare Breakdown",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
            }
            Text(
              text = "Base + Km + Tax >",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = SadAmberDark
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Payment Mode Selector
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Payment Mode",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
          )
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PaymentMode.values().forEach { mode ->
              val isSelected = selectedPayment == mode
              Surface(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                  1.dp,
                  if (isSelected) SadAmberDark else MaterialTheme.colorScheme.outlineVariant
                ),
                color = if (isSelected) SadAmberPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                modifier = Modifier
                  .clickable { viewModel.selectPayment(mode) }
                  .testTag("payment_chip_${mode.name}")
              ) {
                Text(
                  text = if (mode == PaymentMode.CASH) "Cash" else "Razorpay",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) SadAmberDark else MaterialTheme.colorScheme.onSurface
                  ),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Primary Book CTA Button
        Button(
          onClick = { viewModel.bookRideFromCustomer() },
          enabled = currentRide == null || currentRide?.status == RideStatus.COMPLETED || currentRide?.status == RideStatus.CANCELLED,
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("book_ride_cta_button"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = SadAmberPrimary,
            contentColor = Color.Black
          )
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Book ${selectedVehicle.displayName}",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
            Text(
              text = "₹${fareBreakdown.totalFare.toInt()} • Confirm",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 16.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }

    // Modal Fare Breakdown Dialog
    if (showFareDialog) {
      FareBreakdownDialog(
        breakdown = fareBreakdown,
        vehicleCategory = selectedVehicle,
        onDismiss = { viewModel.closeFareBreakdown() }
      )
    }
  }
}

@Composable
private fun VehicleOptionCard(
  category: VehicleCategory,
  fare: Int,
  etaMins: Int,
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  val icon: ImageVector = when (category) {
    VehicleCategory.BIKE -> Icons.Default.DirectionsBike
    VehicleCategory.AUTO -> Icons.Default.ElectricRickshaw
    VehicleCategory.CAB -> Icons.Default.DirectionsCar
  }

  Surface(
    shape = RoundedCornerShape(14.dp),
    border = BorderStroke(
      2.dp,
      if (isSelected) SadAmberDark else MaterialTheme.colorScheme.outlineVariant
    ),
    color = if (isSelected) SadAmberPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
    modifier = modifier
      .clickable { onClick() }
      .testTag("vehicle_card_${category.name}")
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = category.displayName,
        tint = if (isSelected) SadAmberDark else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(28.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = category.displayName.replace("S.A.D ", ""),
        style = MaterialTheme.typography.bodySmall.copy(
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
      )
      Text(
        text = "₹$fare",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.ExtraBold,
          color = if (isSelected) SadAmberDark else MaterialTheme.colorScheme.onSurface
        )
      )
      Text(
        text = "${etaMins}m away",
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 10.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    }
  }
}

@Composable
private fun ActiveRideCard(
  ride: RideRecord,
  onCancel: () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SadMidnight),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("active_ride_card")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          val statusText = when (ride.status) {
            RideStatus.SEARCHING_CAPTAIN -> "Locating Nearest Captain..."
            RideStatus.ARRIVING_PICKUP -> "Captain is Arriving at Pickup"
            RideStatus.IN_PROGRESS -> "Trip in Progress"
            else -> "Ride ${ride.status.name}"
          }
          Text(
            text = statusText,
            style = MaterialTheme.typography.titleSmall.copy(
              color = SadAmberPrimary,
              fontWeight = FontWeight.Bold
            )
          )
          Text(
            text = "Trip ID: ${ride.id} • ${ride.vehicleCategory.displayName}",
            style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
          )
        }

        // Highlight OTP
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = SadAmberPrimary,
          contentColor = Color.Black
        ) {
          Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "START OTP",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            )
            Text(
              text = ride.otp,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Captain profile snippet
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .background(Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = ride.captainName,
              style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
              )
            )
            Text(
              text = "${ride.captainVehicleNumber} • Rating 4.88",
              style = MaterialTheme.typography.bodySmall.copy(
                color = Color.LightGray,
                fontSize = 11.sp
              )
            )
          }
        }

        OutlinedButton(
          onClick = onCancel,
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
          border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("cancel_ride_btn")
        ) {
          Text("Cancel", fontSize = 12.sp)
        }
      }
    }
  }
}
