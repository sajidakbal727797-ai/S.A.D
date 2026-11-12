package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.FareBreakdown
import com.example.model.VehicleCategory
import com.example.ui.theme.SadAmberDark
import com.example.ui.theme.SadAmberPrimary
import com.example.ui.theme.SadEmerald
import com.example.ui.theme.SadMidnight

@Composable
fun FareBreakdownDialog(
  breakdown: FareBreakdown,
  vehicleCategory: VehicleCategory,
  onDismiss: () -> Unit,
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("fare_breakdown_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .background(SadAmberPrimary.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = SadAmberDark,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column {
              Text(
                text = "Fare Breakdown",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "${vehicleCategory.displayName} • ${breakdown.distanceKm} km",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }
          }
          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_fare_dialog")) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Breakdown Line Items
        FareLineItem(
          title = "Base Fare",
          subtitle = "Fixed flag-down fee for ${vehicleCategory.displayName}",
          amount = "₹${breakdown.baseFare.toInt()}"
        )
        FareLineItem(
          title = "Distance Fare",
          subtitle = "${breakdown.distanceKm} km @ ₹${vehicleCategory.perKmRate}/km",
          amount = "₹${breakdown.distanceCharge.toInt()}"
        )
        FareLineItem(
          title = "Time & Traffic Charges",
          subtitle = "~${breakdown.estimatedMins} mins @ ₹${vehicleCategory.perMinRate}/min",
          amount = "₹${breakdown.timeCharge.toInt()}"
        )
        FareLineItem(
          title = "Platform Convenience Fee",
          subtitle = "Safety telemetry & 24/7 support",
          amount = "₹${breakdown.platformFee.toInt()}"
        )
        FareLineItem(
          title = "Taxes (GST 5%)",
          subtitle = "Govt. tax compliant receipt",
          amount = "₹${breakdown.gstTax.toInt()}"
        )

        HorizontalDivider(
          modifier = Modifier.padding(vertical = 12.dp),
          color = MaterialTheme.colorScheme.outlineVariant
        )

        // Total
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Estimated Total Fare",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "Includes all toll/taxes & S.A.D guarantee",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
          Text(
            text = "₹${breakdown.totalFare.toInt()}",
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.ExtraBold,
              color = SadAmberDark
            )
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Transparency Box: Explaining the 10% Commission split
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = SadEmerald,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column {
              Text(
                text = "S.A.D Enterprises Fair Driver Split",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "Captain earns 90% (₹${breakdown.driverEarnings.toInt()}) • S.A.D Platform fee only 10% (₹${breakdown.commissionAmount.toInt()}).",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("confirm_fare_breakdown_button"),
          colors = ButtonDefaults.buttonColors(containerColor = SadMidnight, contentColor = SadAmberPrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Got It, Proceed to Booking", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun FareLineItem(title: String, subtitle: String, amount: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 11.sp
        )
      )
    }
    Text(
      text = amount,
      style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
    )
  }
}
