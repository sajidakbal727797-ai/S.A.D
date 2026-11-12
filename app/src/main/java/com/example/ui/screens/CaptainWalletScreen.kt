package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WalletTransaction
import com.example.ui.MainViewModel
import com.example.ui.components.WalletRechargeDialog
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
fun CaptainWalletScreen(
  viewModel: MainViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val captain by viewModel.captain.collectAsState()
  val transactions by viewModel.transactions.collectAsState()
  val showRecharge by viewModel.showRechargeDialog.collectAsState()

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp)
    ) {
      // Top Navigation
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("wallet_back_button")) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "Earnings & Digital Wallet",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "S.A.D Enterprises Partner Settlement",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
      }

      // Wallet Balance Hero Card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = SadMidnight),
          elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("captain_wallet_hero_card")
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top
            ) {
              Column {
                Text(
                  text = "CURRENT WALLET BALANCE",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.LightGray,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "₹${captain.walletBalance.toInt()}",
                  style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = if (captain.isWalletBlocked) SadRed else if (captain.walletBalance < 0) SadAmberPrimary else SadEmerald
                  )
                )
              }

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (captain.isWalletBlocked) SadRed.copy(alpha = 0.2f) else SadEmerald.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, if (captain.isWalletBlocked) SadRed else SadEmerald)
              ) {
                Text(
                  text = if (captain.isWalletBlocked) "SUSPENDED" else "ACTIVE",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (captain.isWalletBlocked) SadRed else SadEmerald
                  ),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Negative limit rule note
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color.White.copy(alpha = 0.08f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = null,
                  tint = SadAmberPrimary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Negative Limit: ₹${captain.negativeLimit.toInt()}. If balance drops lower, incoming rides are locked until recharged.",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFE2E8F0),
                    fontSize = 11.sp
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons (Recharge & Test Negative Lock)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = { viewModel.openRechargeDialog() },
                modifier = Modifier
                  .weight(1f)
                  .height(46.dp)
                  .testTag("recharge_wallet_hero_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = SadAmberPrimary,
                  contentColor = Color.Black
                )
              ) {
                Icon(
                  imageVector = Icons.Default.AddCard,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Recharge Wallet", fontWeight = FontWeight.Bold)
              }

              OutlinedButton(
                onClick = { viewModel.triggerSimulatedNegativeBalance() },
                modifier = Modifier
                  .weight(1f)
                  .height(46.dp)
                  .testTag("test_negative_limit_btn"),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SadRed),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SadRed)
              ) {
                Text("Simulate Lock (-₹180)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // Commission Policy Explanation Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = SadAmberDark,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "S.A.D Enterprises 10% Commission Rule",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "• Cash Trips: You collect 100% of the fare directly from the customer in cash. S.A.D Enterprises automatically deducts our 10% platform commission from this digital wallet.\n• Negative Balance Threshold: You can operate up to -₹150 in credit. Once you reach -₹150, the app prevents accepting new trips until top-up.\n• Razorpay Instant Top-up: Settle and unblock your account within 10 seconds via UPI / Card.",
              style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // Ledger Title
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Wallet Transaction Ledger",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = "${transactions.size} records",
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
      }

      // Transactions List
      items(transactions) { txn ->
        TransactionItemCard(txn = txn)
        Spacer(modifier = Modifier.height(8.dp))
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }

    // Modal Recharge Dialog
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
private fun TransactionItemCard(txn: WalletTransaction) {
  val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
  val dateStr = dateFormat.format(Date(txn.timestamp))

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .background(
              if (txn.isCredit) SadEmerald.copy(alpha = 0.15f) else SadRed.copy(alpha = 0.15f),
              CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (txn.isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
            contentDescription = null,
            tint = if (txn.isCredit) SadEmerald else SadRed,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = txn.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
          )
          Text(
            text = txn.description,
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 11.sp
            )
          )
          Text(
            text = dateStr,
            style = MaterialTheme.typography.labelSmall.copy(
              color = Color.Gray,
              fontSize = 10.sp
            )
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "${if (txn.isCredit) "+" else "-"}₹${txn.amount.toInt()}",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = if (txn.isCredit) SadEmerald else SadRed
          )
        )
        Text(
          text = "Bal: ₹${txn.balanceAfter.toInt()}",
          style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp
          )
        )
      }
    }
  }
}
