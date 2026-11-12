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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.SadAmberDark
import com.example.ui.theme.SadAmberPrimary
import com.example.ui.theme.SadBlue
import com.example.ui.theme.SadEmerald
import com.example.ui.theme.SadMidnight
import com.example.ui.theme.SadRed

@Composable
fun BlueprintHubScreen(modifier: Modifier = Modifier) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf(
    "1. App Architecture",
    "2. Database Schema",
    "3. FlutterFlow Guide",
    "4. Commission & Wallet",
    "5. Fare Breakdown Formula"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Header
    Surface(
      color = SadMidnight,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AccountTree,
            contentDescription = null,
            tint = SadAmberPrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "S.A.D ENTERPRISES • Master Technical Blueprint",
            style = MaterialTheme.typography.titleMedium.copy(
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          )
        }
        Text(
          text = "Complete Architecture, Database Schema, No-Code Guide & Wallet Algorithm",
          style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray, fontSize = 11.sp)
        )
      }
    }

    // Tab Bar
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
      tabs.forEachIndexed { index, title ->
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
          modifier = Modifier.testTag("blueprint_tab_$index")
        )
      }
    }

    // Tab Content Area
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      when (selectedTab) {
        0 -> item { AppArchitectureContent() }
        1 -> item { DatabaseSchemaContent() }
        2 -> item { FlutterFlowGuideContent() }
        3 -> item { CommissionWalletContent() }
        4 -> item { FareFormulaContent() }
      }
      item { Spacer(modifier = Modifier.height(30.dp)) }
    }
  }
}

@Composable
private fun AppArchitectureContent() {
  Column {
    BlueprintCard(
      title = "Part 1: Dual App Architecture (Do Apps Model)",
      badge = "System Flow",
      icon = Icons.Default.PhoneAndroid
    ) {
      Text(
        text = "S.A.D Enterprises mobility platform consists of two synchronized native client apps backed by a unified cloud database (Firebase / Supabase):",
        style = MaterialTheme.typography.bodyMedium
      )

      Spacer(modifier = Modifier.height(12.dp))

      SectionHeader("A. S.A.D Ride (Customer App)")
      BulletPoint("1. Phone Authentication: Firebase Phone Auth / Twilio OTP (Quick 4-digit code login without password).")
      BulletPoint("2. Live Interactive Map: Google Maps Flutter SDK with current GPS location, route polylines, and nearby active captain markers.")
      BulletPoint("3. Location Autocomplete: Google Places API for pickup and destination search.")
      BulletPoint("4. Vehicle Class Matrix: Real-time selection among S.A.D Bike (solo), S.A.D Auto (3-wheeler), and S.A.D Cab (AC sedan/hatch).")
      BulletPoint("5. Dynamic Fare & Transparency: Upfront transparent calculation with base fare, distance rates, platform charge, and 5% GST breakdown.")
      BulletPoint("6. Dual Payment Gateway: Cash payment or Razorpay (UPI Apps: GPay, PhonePe, Paytm, and Cards).")
      BulletPoint("7. Secure Ride Verification: Random 4-digit OTP shown to customer, which driver MUST enter to begin trip.")

      Spacer(modifier = Modifier.height(14.dp))

      SectionHeader("B. S.A.D Captain (Driver App)")
      BulletPoint("1. Driver Status Switch: One-tap Online/Offline toggle with real-time background location broadcasting.")
      BulletPoint("2. Floating Dispatch Alert: Instant modal card with 15-second countdown timer showing pickup distance, drop location, gross fare, and net earnings.")
      BulletPoint("3. In-App Navigation: Direct deep-link to Google Maps / Waze turn-by-turn navigation to pickup and destination.")
      BulletPoint("4. Trip Lifecycle: Arrive at pickup -> Input Customer OTP -> Start Trip -> Reach Destination -> Collect Cash/Verify Online Payment -> Complete Trip.")
      BulletPoint("5. Digital Wallet & 10% Auto-Deduction: Instant deduction of 10% platform fee from driver wallet on cash trips.")
      BulletPoint("6. Negative Balance Enforcement: Blocks ride requests if wallet falls below -₹150 until recharged via Razorpay.")
    }
  }
}

@Composable
private fun DatabaseSchemaContent() {
  Column {
    BlueprintCard(
      title = "Part 2: Database (Backend) Structure & Table Columns",
      badge = "Postgres / Firestore",
      icon = Icons.Default.Storage
    ) {
      Text(
        text = "The system requires 5 primary relational tables (or Firestore collections) to maintain transactions, audit integrity, and live geo-tracking:",
        style = MaterialTheme.typography.bodyMedium
      )

      Spacer(modifier = Modifier.height(12.dp))

      TableSpecCard(
        tableName = "1. users (Customers Table)",
        columns = listOf(
          "user_id: STRING (Primary Key, UUID / Firebase UID)",
          "name: STRING (Customer full name)",
          "phone: STRING (Unique, +91 format with OTP verified flag)",
          "email: STRING (Optional)",
          "profile_pic: STRING (Image URL / Storage path)",
          "rating: FLOAT (Average user rating, default 5.0)",
          "emergency_contact: STRING (SOS contact number)",
          "created_at: TIMESTAMP"
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      TableSpecCard(
        tableName = "2. drivers / captains (Captains Table)",
        columns = listOf(
          "driver_id: STRING (Primary Key, UUID)",
          "name: STRING (Driver full name)",
          "phone: STRING (Unique verified mobile number)",
          "vehicle_id: STRING (Foreign Key referencing vehicles table)",
          "is_online: BOOLEAN (true/false toggle)",
          "current_latitude: DOUBLE (Live GPS lat)",
          "current_longitude: DOUBLE (Live GPS lon)",
          "wallet_balance: DOUBLE (Current digital balance, default ₹0.0)",
          "negative_limit: DOUBLE (Default -150.00)",
          "is_blocked: BOOLEAN (True if wallet_balance < negative_limit)",
          "rating: FLOAT (Average driver rating, e.g. 4.88)",
          "aadhar_no: STRING (KYC document identifier)",
          "driving_license_no: STRING (KYC verification)",
          "status: STRING (PENDING_APPROVAL, ACTIVE, SUSPENDED)"
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      TableSpecCard(
        tableName = "3. vehicles (Vehicles Table)",
        columns = listOf(
          "vehicle_id: STRING (Primary Key)",
          "driver_id: STRING (Foreign Key referencing drivers)",
          "category: ENUM ('BIKE', 'AUTO', 'CAB')",
          "model_name: STRING (e.g. 'Bajaj Compact RE', 'Hero Splendor', 'Maruti Dzire')",
          "registration_number: STRING (Unique plate, e.g. 'UP 32 BK 7890')",
          "rc_document_url: STRING (Registration certificate image)",
          "insurance_expiry: DATE",
          "capacity: INTEGER (1 for Bike, 3 for Auto, 4 for Cab)"
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      TableSpecCard(
        tableName = "4. rides (Trip Orders Table)",
        columns = listOf(
          "ride_id: STRING (Primary Key, e.g. 'RIDE-1049')",
          "customer_id: STRING (Foreign Key referencing users)",
          "driver_id: STRING (Foreign Key referencing drivers, nullable when searching)",
          "vehicle_category: ENUM ('BIKE', 'AUTO', 'CAB')",
          "pickup_address: STRING (Human readable location)",
          "pickup_lat: DOUBLE & pickup_lng: DOUBLE",
          "drop_address: STRING (Human readable location)",
          "drop_lat: DOUBLE & drop_lng: DOUBLE",
          "distance_km: DOUBLE (Calculated route distance)",
          "duration_minutes: INTEGER",
          "base_fare: DOUBLE",
          "distance_fare: DOUBLE",
          "platform_fee: DOUBLE (Convenience fee)",
          "tax_amount: DOUBLE (5% GST)",
          "total_fare: DOUBLE (Grand total payable by user)",
          "commission_amount: DOUBLE (10% platform share for S.A.D Enterprises)",
          "driver_share: DOUBLE (90% driver earnings)",
          "payment_mode: ENUM ('CASH', 'RAZORPAY_UPI', 'RAZORPAY_CARD')",
          "payment_status: ENUM ('PENDING', 'PAID')",
          "otp_code: STRING (4-digit security PIN, e.g. '4291')",
          "status: ENUM ('REQUESTED', 'ACCEPTED', 'ARRIVED', 'IN_TRIP', 'COMPLETED', 'CANCELLED')",
          "created_at: TIMESTAMP & completed_at: TIMESTAMP"
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      TableSpecCard(
        tableName = "5. wallet_transactions (Audit & Settlement Table)",
        columns = listOf(
          "transaction_id: STRING (Primary Key, UUID)",
          "driver_id: STRING (Foreign Key referencing drivers)",
          "ride_id: STRING (Foreign Key, nullable for top-ups)",
          "type: ENUM ('COMMISSION_DEDUCTION', 'WALLET_RECHARGE', 'BONUS', 'PENALTY')",
          "amount: DOUBLE (Positive for recharge, negative/positive magnitude)",
          "is_credit: BOOLEAN (True = Recharge, False = 10% Deduction)",
          "balance_before: DOUBLE",
          "balance_after: DOUBLE",
          "razorpay_payment_id: STRING (For tracking online top-ups)",
          "created_at: TIMESTAMP"
        )
      )
    }
  }
}

@Composable
private fun FlutterFlowGuideContent() {
  Column {
    BlueprintCard(
      title = "Part 3: Step-by-Step Execution Guide on FlutterFlow / No-Code",
      badge = "Build Roadmap",
      icon = Icons.Default.Code
    ) {
      Text(
        text = "Follow this exact sequence in FlutterFlow to build both apps smoothly without getting blocked:",
        style = MaterialTheme.typography.bodyMedium
      )

      Spacer(modifier = Modifier.height(12.dp))

      SectionHeader("Step 1: Firebase Project & Authentication Setup")
      BulletPoint("• Create Firebase project in Google Cloud Console.")
      BulletPoint("• Enable Firebase Authentication -> Phone Provider (add SMS test numbers for development).")
      BulletPoint("• In FlutterFlow -> Settings -> Firebase -> Connect project and generate Firestore rules.")

      Spacer(modifier = Modifier.height(10.dp))

      SectionHeader("Step 2: First Screen to Design (Customer App)")
      BulletPoint("• Screen 1: 'PhoneLoginScreen' (Phone input field + Country code + 'Send OTP' button).")
      BulletPoint("• Screen 2: 'OtpVerificationScreen' (4-digit PinCode widget + 'Verify' action).")
      BulletPoint("• Screen 3: 'HomeScreen' (GoogleMap widget taking top 40% + DraggableScrollableSheet for destination search).")

      Spacer(modifier = Modifier.height(10.dp))

      SectionHeader("Step 3: Google Maps & Places API Integration")
      BulletPoint("• Generate Google Maps Android & iOS API Keys from Google Cloud Console.")
      BulletPoint("• Enable: Maps SDK for Android, Maps SDK for iOS, Places API, and Directions API.")
      BulletPoint("• In FlutterFlow Settings -> Integrations -> Google Maps: Paste the API Keys.")
      BulletPoint("• Add Google Places Autocomplete action on destination textfield to set Drop Lat/Lng.")

      Spacer(modifier = Modifier.height(10.dp))

      SectionHeader("Step 4: Vehicle Selection & Fare Breakdown Modal")
      BulletPoint("• Create Component: 'FareBreakdownBottomSheet'.")
      BulletPoint("• Define Custom Function: 'calculateRideFare(category, distanceKm)' that outputs the breakdown struct.")
      BulletPoint("• Trigger modal when user taps 'Fare Details' link before confirming.")

      Spacer(modifier = Modifier.height(10.dp))

      SectionHeader("Step 5: Captain App Setup & First Screens")
      BulletPoint("• Screen 1: 'CaptainHomeScreen' with Online/Offline Switch.")
      BulletPoint("• Add Condition: If `driverDoc.wallet_balance < -150`, set Switch Disabled and display 'Recharge Required' Banner.")
      BulletPoint("• Screen 2: 'EarningsAndWalletScreen' showing wallet card, recharge button, and list view of `wallet_transactions`.")
      BulletPoint("• Component: 'IncomingRequestAlert' as a bottom sheet with 15s timer action.")

      Spacer(modifier = Modifier.height(10.dp))

      SectionHeader("Step 6: Razorpay Payment Gateway Setup")
      BulletPoint("• Sign up at Razorpay Dashboard (Get Test Key Id & Secret).")
      BulletPoint("• In FlutterFlow: Use Custom Action 'razorpayCheckout' or API call to `/v1/orders`.")
      BulletPoint("• On payment success -> update `drivers.wallet_balance += rechargeAmount` and create record in `wallet_transactions`.")
    }
  }
}

@Composable
private fun CommissionWalletContent() {
  Column {
    BlueprintCard(
      title = "Part 4: Driver Wallet & 10% Commission Technical Workflow",
      badge = "Financial Logic",
      icon = Icons.Default.Paid
    ) {
      Text(
        text = "This is the core financial engine for S.A.D Enterprises to ensure guaranteed revenue collection on cash rides without manual collection:",
        style = MaterialTheme.typography.bodyMedium
      )

      Spacer(modifier = Modifier.height(12.dp))

      SectionHeader("1. Cash Ride Workflow (10% Automated Cut)")
      BulletPoint("• Customer pays ₹200 in CASH directly to the Captain at the end of the trip.")
      BulletPoint("• Captain taps 'Collect ₹200 Cash & Complete Trip'.")
      BulletPoint("• Cloud Function calculates: commission = totalFare * 0.10 (₹20).")
      BulletPoint("• Driver's Digital Wallet balance is reduced by ₹20:")
      CodeBlock("new_balance = current_balance - (total_fare * 0.10);\n// Example: ₹300 - ₹20 = ₹280")
      BulletPoint("• Transaction log entry is saved with `type = COMMISSION_DEDUCTION`.")

      Spacer(modifier = Modifier.height(12.dp))

      SectionHeader("2. Negative Balance Threshold & Account Lock")
      BulletPoint("• Threshold configured at: -₹150.00 (Configurable via Admin portal).")
      BulletPoint("• If driver operates multiple cash trips without recharging, their wallet balance dips (e.g. -₹20, -₹70, -₹160).")
      BulletPoint("• As soon as `wallet_balance < -150.00`:")
      BulletPoint("   - `is_blocked` flag is set to TRUE.")
      BulletPoint("   - Driver app status is switched to OFFLINE automatically.")
      BulletPoint("   - Backend dispatch algorithm skips this driver for incoming ride requests.")
      BulletPoint("   - App displays blocking alert: 'Account Suspended: Negative Balance. Recharge via Razorpay to continue.'")

      Spacer(modifier = Modifier.height(12.dp))

      SectionHeader("3. Razorpay Wallet Recharge Workflow")
      BulletPoint("• Driver taps 'Recharge' -> Chooses ₹100, ₹250, ₹500, or ₹1000.")
      BulletPoint("• Razorpay checkout opens -> Driver pays via Google Pay / PhonePe / UPI.")
      BulletPoint("• Webhook / Client callback verifies payment signature.")
      BulletPoint("• Backend executes: `wallet_balance += recharge_amount`.")
      BulletPoint("• If `wallet_balance >= -150`, `is_blocked = false` immediately, allowing driver to go online!")

      Spacer(modifier = Modifier.height(12.dp))

      SectionHeader("Firebase Cloud Function / Supabase Edge Function Code:")
      CodeBlock(
        """// Complete cash trip & deduct 10% commission
exports.onRideCompleted = functions.firestore
  .document('rides/{rideId}')
  .onUpdate(async (change, context) => {
    const after = change.after.data();
    const before = change.before.data();

    if (before.status !== 'COMPLETED' && after.status === 'COMPLETED') {
      const driverRef = db.collection('drivers').doc(after.driver_id);
      const commission = Math.round(after.total_fare * 0.10); // 10%

      await db.runTransaction(async (t) => {
        const driverDoc = await t.get(driverRef);
        const currentBal = driverDoc.data().wallet_balance || 0;
        const newBal = currentBal - commission;
        const negativeLimit = driverDoc.data().negative_limit || -150;
        const isBlocked = newBal < negativeLimit;

        t.update(driverRef, {
          wallet_balance: newBal,
          is_blocked: isBlocked,
          is_online: isBlocked ? false : driverDoc.data().is_online
        });

        // Insert audit log
        t.set(db.collection('wallet_transactions').doc(), {
          driver_id: after.driver_id,
          ride_id: context.params.rideId,
          type: 'COMMISSION_DEDUCTION',
          amount: commission,
          is_credit: false,
          balance_after: newBal,
          created_at: admin.firestore.FieldValue.serverTimestamp()
        });
      });
    }
  });"""
      )
    }
  }
}

@Composable
private fun FareFormulaContent() {
  Column {
    BlueprintCard(
      title = "Part 5: Standard Fare Calculation Formula",
      badge = "Pricing Model",
      icon = Icons.Default.Speed
    ) {
      Text(
        text = "S.A.D Enterprises uses a transparent, government-compliant dynamic fare formula for Bike, Auto, and Cab:",
        style = MaterialTheme.typography.bodyMedium
      )

      Spacer(modifier = Modifier.height(12.dp))

      CodeBlock(
        """Total Fare = Base Fare + (Distance in Km * Per Km Rate) 
             + (Trip Duration in Mins * Per Min Rate) 
             + Platform Convenience Fee (₹5) 
             + GST Tax (5%)

Driver Earnings = Total Fare - 10% Platform Commission
S.A.D Enterprises Share = Total Fare * 10%"""
      )

      Spacer(modifier = Modifier.height(14.dp))

      SectionHeader("Rate Card Configuration:")
      BulletPoint("• S.A.D Bike: Base Fare ₹20 | ₹7.50/km | ₹1.00/min | Min Fare ₹25")
      BulletPoint("• S.A.D Auto: Base Fare ₹30 | ₹11.00/km | ₹1.50/min | Min Fare ₹40")
      BulletPoint("• S.A.D Cab: Base Fare ₹60 | ₹16.00/km | ₹2.00/min | Min Fare ₹90")

      Spacer(modifier = Modifier.height(12.dp))

      SectionHeader("Sample 8 km Auto Trip Calculation:")
      BulletPoint("• Base Fare: ₹30")
      BulletPoint("• Distance (8 km * ₹11): ₹88")
      BulletPoint("• Time (~18 mins * ₹1.5): ₹27")
      BulletPoint("• Platform Fee: ₹5")
      BulletPoint("• Subtotal: ₹150")
      BulletPoint("• GST (5% of ₹150): ₹7.50")
      BulletPoint("• Final Fare to Customer: ₹158 (Rounded)")
      BulletPoint("• 10% S.A.D Enterprises Commission: ₹16 (deducted from Captain wallet)")
      BulletPoint("• Net Captain Earnings: ₹142")
    }
  }
}

@Composable
private fun BlueprintCard(
  title: String,
  badge: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  content: @Composable () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .background(SadAmberPrimary.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = SadAmberDark,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
        }
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = SadMidnight,
          contentColor = SadAmberPrimary
        ) {
          Text(
            text = badge,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.outlineVariant
      )

      content()
    }
  }
}

@Composable
private fun TableSpecCard(tableName: String, columns: List<String>) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(
        text = tableName,
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          color = SadBlue
        )
      )
      Spacer(modifier = Modifier.height(6.dp))
      columns.forEach { col ->
        Text(
          text = "• $col",
          style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        )
      }
    }
  }
}

@Composable
private fun SectionHeader(title: String) {
  Text(
    text = title,
    style = MaterialTheme.typography.bodyMedium.copy(
      fontWeight = FontWeight.Bold,
      color = SadAmberDark
    ),
    modifier = Modifier.padding(vertical = 4.dp)
  )
}

@Composable
private fun BulletPoint(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.bodySmall.copy(
      color = MaterialTheme.colorScheme.onSurface,
      lineHeight = 18.sp
    ),
    modifier = Modifier.padding(vertical = 2.dp)
  )
}

@Composable
private fun CodeBlock(code: String) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = Color(0xFF0F172A),
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp)
  ) {
    Text(
      text = code,
      style = MaterialTheme.typography.bodySmall.copy(
        fontFamily = FontFamily.Monospace,
        fontSize = 11.sp,
        color = Color(0xFF38BDF8),
        lineHeight = 16.sp
      ),
      modifier = Modifier.padding(12.dp)
    )
  }
}
