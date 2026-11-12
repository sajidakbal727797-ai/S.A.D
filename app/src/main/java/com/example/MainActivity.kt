package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppMode
import com.example.ui.MainViewModel
import com.example.ui.screens.AdminPlatformScreen
import com.example.ui.screens.BlueprintHubScreen
import com.example.ui.screens.CaptainScreen
import com.example.ui.screens.CaptainWalletScreen
import com.example.ui.screens.CustomerRideScreen
import com.example.ui.screens.SadAiCopilotScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SadAmberDark
import com.example.ui.theme.SadAmberPrimary
import com.example.ui.theme.SadBlue
import com.example.ui.theme.SadEmerald
import com.example.ui.theme.SadMidnight
import com.example.ui.theme.SadRed
import com.example.util.configureAppCheck

enum class NavDestination(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
  CUSTOMER("Ride", Icons.Default.DirectionsCar),
  CAPTAIN("Captain", Icons.Default.SportsMotorsports),
  AI("S.A.D AI", Icons.Default.AutoAwesome),
  WALLET("Wallet", Icons.Default.AccountBalanceWallet),
  ADMIN("Admin", Icons.Default.AdminPanelSettings),
  BLUEPRINT("Guide", Icons.Default.Description),
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Mandated: Configure Firebase App Check with launch intent before any UI or Firebase operations
    configureAppCheck(this, intent)

    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        SadRideAppRoot()
      }
    }
  }
}

@Composable
fun SadRideAppRoot(viewModel: MainViewModel = viewModel()) {
  var currentDestination by remember { mutableStateOf(NavDestination.CUSTOMER) }
  val statusBanner by viewModel.statusBannerMessage.collectAsState()
  val incomingOffer by viewModel.incomingRideOffer.collectAsState()
  val captain by viewModel.captain.collectAsState()
  val allCaptains by viewModel.allCaptains.collectAsState()
  val deficitCount = allCaptains.count { it.isWalletBlocked }

  // Handle Android system Back button
  BackHandler(enabled = currentDestination != NavDestination.CUSTOMER) {
    currentDestination = NavDestination.CUSTOMER
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .testTag("main_scaffold"),
    topBar = {
      // S.A.D Enterprises Corporate Branding TopBar
      Surface(
        color = SadMidnight,
        modifier = Modifier
          .fillMaxWidth()
          .windowInsetsPadding(WindowInsets.statusBars)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .background(SadAmberPrimary, RoundedCornerShape(8.dp)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "S",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = SadMidnight
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "S.A.D ENTERPRISES",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 1.2.sp,
                  color = SadAmberPrimary,
                  fontSize = 10.sp
                )
              )
              Text(
                text = when (currentDestination) {
                  NavDestination.CUSTOMER -> "S.A.D Ride • Customer App"
                  NavDestination.CAPTAIN -> "S.A.D Captain • Driver Partner"
                  NavDestination.AI -> "S.A.D Saathi • Gemini AI Co-Pilot"
                  NavDestination.WALLET -> "Earnings & Digital Wallet"
                  NavDestination.ADMIN -> "Admin Control Tower & Fleet"
                  NavDestination.BLUEPRINT -> "System Blueprint & Guide"
                },
                style = MaterialTheme.typography.titleSmall.copy(
                  color = Color.White,
                  fontWeight = FontWeight.Bold
                )
              )
            }
          }

          // Live Dual-Role Status Pill
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (captain.isWalletBlocked) SadRed.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.12f),
            modifier = Modifier.testTag("top_status_badge")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(
                    if (captain.isWalletBlocked) SadRed else if (captain.isOnline) SadEmerald else Color.Gray,
                    CircleShape
                  )
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (captain.isWalletBlocked) "Deficit ₹${captain.walletBalance.toInt()}" else if (captain.isOnline) "Captain Online" else "Offline",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Color.White,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 11.sp
                )
              )
            }
          }
        }
      }
    },
    bottomBar = {
      NavigationBar(
        containerColor = SadMidnight,
        contentColor = Color.White,
        modifier = Modifier.testTag("bottom_nav_bar")
      ) {
        NavDestination.values().forEach { destination ->
          val isSelected = currentDestination == destination

          NavigationBarItem(
            selected = isSelected,
            onClick = { currentDestination = destination },
            icon = {
              if (destination == NavDestination.CAPTAIN && incomingOffer != null) {
                BadgedBox(
                  badge = {
                    Badge(containerColor = SadAmberPrimary) {
                      Text("1", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                  }
                ) {
                  Icon(imageVector = destination.icon, contentDescription = destination.label)
                }
              } else if (destination == NavDestination.WALLET && captain.isWalletBlocked) {
                BadgedBox(
                  badge = {
                    Badge(containerColor = SadRed) {
                      Text("!", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                  }
                ) {
                  Icon(imageVector = destination.icon, contentDescription = destination.label)
                }
              } else if (destination == NavDestination.ADMIN && deficitCount > 0) {
                BadgedBox(
                  badge = {
                    Badge(containerColor = SadRed) {
                      Text("$deficitCount", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                  }
                ) {
                  Icon(imageVector = destination.icon, contentDescription = destination.label)
                }
              } else {
                Icon(imageVector = destination.icon, contentDescription = destination.label)
              }
            },
            label = {
              Text(
                text = destination.label,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 8.5.sp
                )
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color.Black,
              selectedTextColor = SadAmberPrimary,
              indicatorColor = SadAmberPrimary,
              unselectedIconColor = Color.LightGray,
              unselectedTextColor = Color.LightGray
            ),
            modifier = Modifier.testTag("nav_${destination.name.lowercase()}")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Main Destination Screen
      when (currentDestination) {
        NavDestination.CUSTOMER -> {
          CustomerRideScreen(viewModel = viewModel)
        }
        NavDestination.CAPTAIN -> {
          CaptainScreen(
            viewModel = viewModel,
            onNavigateToWallet = { currentDestination = NavDestination.WALLET }
          )
        }
        NavDestination.AI -> {
          SadAiCopilotScreen(viewModel = viewModel)
        }
        NavDestination.WALLET -> {
          CaptainWalletScreen(
            viewModel = viewModel,
            onBack = { currentDestination = NavDestination.CAPTAIN }
          )
        }
        NavDestination.ADMIN -> {
          AdminPlatformScreen(viewModel = viewModel)
        }
        NavDestination.BLUEPRINT -> {
          BlueprintHubScreen()
        }
      }

      // Floating System Notification Banner (if any)
      statusBanner?.let { message ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = SadMidnight),
          elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
          modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(12.dp)
            .fillMaxWidth()
            .testTag("status_toast_banner")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = message,
              style = MaterialTheme.typography.bodySmall.copy(
                color = SadAmberPrimary,
                fontWeight = FontWeight.SemiBold
              ),
              modifier = Modifier.weight(1f)
            )
            IconButton(
              onClick = { viewModel.clearStatusBanner() },
              modifier = Modifier.size(24.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = Color.LightGray,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }
  }
}
