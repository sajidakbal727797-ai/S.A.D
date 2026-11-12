package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.SadAmberDark
import com.example.ui.theme.SadAmberPrimary
import com.example.ui.theme.SadEmerald
import com.example.ui.theme.SadMidnight

@Composable
fun WalletRechargeDialog(
  currentBalance: Double,
  negativeLimit: Double,
  onRecharge: (Double) -> Unit,
  onDismiss: () -> Unit,
) {
  var selectedAmount by remember { mutableDoubleStateOf(250.0) }
  val quickPicks = listOf(100.0, 250.0, 500.0, 1000.0)

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("wallet_recharge_dialog")
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
                .background(SadEmerald.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = SadEmerald,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column {
              Text(
                text = "Recharge Digital Wallet",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "S.A.D Captain Partner Portal",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }
          }
          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_recharge_dialog")) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Balance status summary card
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Current Wallet Balance",
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
              Text(
                text = "₹${currentBalance.toInt()}",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (currentBalance < negativeLimit) Color.Red else if (currentBalance < 0) SadAmberDark else SadEmerald
                )
              )
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (currentBalance < negativeLimit) Color(0xFFFEE2E2) else Color(0xFFECFDF5)
            ) {
              Text(
                text = if (currentBalance < negativeLimit) "SUSPENDED" else "ACTIVE",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (currentBalance < negativeLimit) Color(0xFFB91C1C) else Color(0xFF047857)
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Select Top-Up Amount",
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Amount Selection Pills
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          quickPicks.forEach { amount ->
            val isSelected = selectedAmount == amount
            Surface(
              shape = RoundedCornerShape(10.dp),
              border = BorderStroke(
                1.5.dp,
                if (isSelected) SadAmberDark else MaterialTheme.colorScheme.outlineVariant
              ),
              color = if (isSelected) SadAmberPrimary.copy(alpha = 0.15f) else Color.Transparent,
              modifier = Modifier
                .weight(1f)
                .clickable { selectedAmount = amount }
                .testTag("recharge_amount_${amount.toInt()}")
            ) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(vertical = 12.dp)
              ) {
                Text(
                  text = "₹${amount.toInt()}",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                    color = if (isSelected) SadAmberDark else MaterialTheme.colorScheme.onSurface
                  )
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Gateway Badge
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = Color(0xFFF0FDF4),
          border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.ElectricBolt,
              contentDescription = null,
              tint = Color(0xFF15803D),
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
              text = "Razorpay Instant Settlement • Instant Account Unblock",
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                color = Color(0xFF15803D),
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = { onRecharge(selectedAmount) },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("confirm_wallet_recharge_button"),
          colors = ButtonDefaults.buttonColors(
            containerColor = SadMidnight,
            contentColor = SadAmberPrimary
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Payment,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.size(8.dp))
          Text(
            text = "Pay ₹${selectedAmount.toInt()} via Razorpay",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        }
      }
    }
  }
}
