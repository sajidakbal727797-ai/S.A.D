package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LocationPoint
import com.example.model.RideStatus
import com.example.model.VehicleCategory
import com.example.ui.theme.SadAmberDark
import com.example.ui.theme.SadAmberPrimary
import com.example.ui.theme.SadBlue
import com.example.ui.theme.SadEmerald
import com.example.ui.theme.SadMidnight
import com.example.ui.theme.SadRed

@Composable
fun InteractiveMapCanvas(
  pickup: LocationPoint,
  drop: LocationPoint,
  rideStatus: RideStatus,
  vehicleCategory: VehicleCategory,
  modifier: Modifier = Modifier,
) {
  val infiniteTransition = rememberInfiniteTransition(label = "map_anim")
  val pulseRadius by infiniteTransition.animateFloat(
    initialValue = 10f,
    targetValue = 45f,
    animationSpec = infiniteRepeatable(
      animation = tween(1500, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart,
    ),
    label = "pulse_radar",
  )
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.6f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1500, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart,
    ),
    label = "pulse_alpha",
  )
  val vehicleProgress by infiniteTransition.animateFloat(
    initialValue = 0.05f,
    targetValue = 0.95f,
    animationSpec = infiniteRepeatable(
      animation = tween(7000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
    label = "vehicle_progress",
  )

  Box(modifier = modifier.testTag("interactive_map_canvas")) {
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF0F172A))
    ) {
      val width = size.width
      val height = size.height

      // 1. Draw street grid / map blocks
      val roadColor = Color(0xFF1E293B)
      val arteryColor = Color(0xFF334155)

      // Background grid lines (streets)
      val streetSpacing = 55f
      var x = 0f
      while (x <= width) {
        drawLine(
          color = roadColor,
          start = Offset(x, 0f),
          end = Offset(x, height),
          strokeWidth = 3f,
        )
        x += streetSpacing
      }
      var y = 0f
      while (y <= height) {
        drawLine(
          color = roadColor,
          start = Offset(0f, y),
          end = Offset(width, y),
          strokeWidth = 3f,
        )
        y += streetSpacing
      }

      // Major arterial highways
      drawLine(
        color = arteryColor,
        start = Offset(0f, height * 0.35f),
        end = Offset(width, height * 0.38f),
        strokeWidth = 12f,
        cap = StrokeCap.Round,
      )
      drawLine(
        color = arteryColor,
        start = Offset(width * 0.25f, 0f),
        end = Offset(width * 0.32f, height),
        strokeWidth = 10f,
        cap = StrokeCap.Round,
      )
      drawLine(
        color = arteryColor,
        start = Offset(0f, height * 0.72f),
        end = Offset(width, height * 0.68f),
        strokeWidth = 14f,
        cap = StrokeCap.Round,
      )

      // Fixed screen positions for Pickup (Green) and Drop (Red/Gold)
      val pickupPos = Offset(width * 0.30f, height * 0.65f)
      val dropPos = Offset(width * 0.72f, height * 0.28f)

      // Draw Route Path (Bezier road curve)
      val path = Path().apply {
        moveTo(pickupPos.x, pickupPos.y)
        cubicTo(
          width * 0.45f, height * 0.70f,
          width * 0.40f, height * 0.35f,
          dropPos.x, dropPos.y,
        )
      }

      // Route Outer Glow / Shadow
      drawPath(
        path = path,
        color = SadAmberDark.copy(alpha = 0.3f),
        style = Stroke(width = 14f, cap = StrokeCap.Round, join = StrokeJoin.Round),
      )
      // Route Active Highlight
      drawPath(
        path = path,
        color = SadAmberPrimary,
        style = Stroke(
          width = 6f,
          cap = StrokeCap.Round,
          join = StrokeJoin.Round,
          pathEffect = if (rideStatus == RideStatus.SEARCHING_CAPTAIN) {
            PathEffect.dashPathEffect(floatArrayOf(25f, 15f), 0f)
          } else null,
        ),
      )

      // Draw Nearby Captains (Simulated nearby moving cars / bikes / autos)
      val nearbyCaptains = listOf(
        Offset(width * 0.22f, height * 0.52f),
        Offset(width * 0.48f, height * 0.62f),
        Offset(width * 0.62f, height * 0.42f),
        Offset(width * 0.80f, height * 0.55f),
      )
      nearbyCaptains.forEach { capOffset ->
        drawCircle(
          color = Color(0xFF0284C7).copy(alpha = 0.25f),
          radius = 16f,
          center = capOffset,
        )
        drawCircle(
          color = Color(0xFF38BDF8),
          radius = 6f,
          center = capOffset,
        )
      }

      // Draw Pickup Pin & Pulse
      drawCircle(
        color = SadEmerald.copy(alpha = pulseAlpha),
        radius = pulseRadius,
        center = pickupPos,
      )
      drawCircle(
        color = Color.White,
        radius = 11f,
        center = pickupPos,
      )
      drawCircle(
        color = SadEmerald,
        radius = 8f,
        center = pickupPos,
      )

      // Draw Drop Pin
      drawCircle(
        color = SadRed.copy(alpha = 0.3f),
        radius = 16f,
        center = dropPos,
      )
      drawCircle(
        color = Color.White,
        radius = 11f,
        center = dropPos,
      )
      drawCircle(
        color = SadRed,
        radius = 8f,
        center = dropPos,
      )

      // Draw moving vehicle icon along the path if in progress
      val t = if (rideStatus == RideStatus.IN_PROGRESS || rideStatus == RideStatus.ARRIVING_PICKUP) {
        vehicleProgress
      } else 0.5f

      // Approximate point on cubic bezier
      val p0 = pickupPos
      val p1 = Offset(width * 0.45f, height * 0.70f)
      val p2 = Offset(width * 0.40f, height * 0.35f)
      val p3 = dropPos

      val oneMinusT = 1f - t
      val vx = (oneMinusT * oneMinusT * oneMinusT * p0.x) +
          (3 * oneMinusT * oneMinusT * t * p1.x) +
          (3 * oneMinusT * t * t * p2.x) +
          (t * t * t * p3.x)
      val vy = (oneMinusT * oneMinusT * oneMinusT * p0.y) +
          (3 * oneMinusT * oneMinusT * t * p1.y) +
          (3 * oneMinusT * t * t * p2.y) +
          (t * t * t * p3.y)
      val vehiclePoint = Offset(vx, vy)

      if (rideStatus != RideStatus.IDLE) {
        // Vehicle Halo
        drawCircle(
          color = SadAmberPrimary.copy(alpha = 0.35f),
          radius = 22f,
          center = vehiclePoint,
        )
        drawCircle(
          color = Color(0xFF0F172A),
          radius = 14f,
          center = vehiclePoint,
        )
        drawCircle(
          color = SadAmberPrimary,
          radius = 10f,
          center = vehiclePoint,
        )
      }
    }

    // Top Map Pill Badge (S.A.D Live GPS Telemetry)
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SadMidnight.copy(alpha = 0.88f),
      contentColor = Color.White,
      shadowElevation = 4.dp,
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(12.dp)
    ) {
      Text(
        text = "LIVE GPS • S.A.D Mobility Radar",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          letterSpacing = 0.8.sp,
          color = SadAmberPrimary
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
      )
    }

    // Recenter GPS FAB Button
    FloatingActionButton(
      onClick = { /* Recenter map focus */ },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(14.dp)
        .size(42.dp)
        .testTag("recenter_map_button"),
      shape = CircleShape,
      containerColor = SadMidnight,
      contentColor = SadAmberPrimary,
    ) {
      Icon(
        imageVector = Icons.Default.MyLocation,
        contentDescription = "Recenter Map",
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
