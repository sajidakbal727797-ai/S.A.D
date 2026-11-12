package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.AiResponseState
import com.example.ui.MainViewModel
import com.example.ui.theme.SadAmberDark
import com.example.ui.theme.SadAmberPrimary
import com.example.ui.theme.SadBlue
import com.example.ui.theme.SadEmerald
import com.example.ui.theme.SadMidnight
import com.example.ui.theme.SadRed

@Composable
fun SadAiCopilotScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier,
) {
  val aiState by viewModel.aiState.collectAsState()
  var inputPrompt by remember { mutableStateOf("") }
  val keyboardController = LocalSoftwareKeyboardController.current

  val suggestionChips = listOf(
    "🚗 Best ride for 3 people with bags?",
    "💰 Why is S.A.D 10% commission fairer for drivers?",
    "⚡ How does negative wallet limit protect cash rides?",
    "📍 Lucknow Airport to Charbagh travel time & route",
    "🛡️ How does 4-digit Ride OTP keep passengers safe?",
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Hero Card
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
                .size(42.dp)
                .background(SadAmberPrimary, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = SadMidnight,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "S.A.D Saathi • Gemini AI",
                style = MaterialTheme.typography.titleMedium.copy(
                  color = Color.White,
                  fontWeight = FontWeight.ExtraBold
                )
              )
              Text(
                text = "Mobility Intelligence & Route Advisor",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray, fontSize = 11.sp)
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White.copy(alpha = 0.15f)
          ) {
            Text(
              text = "gemini-3.8-flash",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = SadAmberPrimary
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    // Scrollable Conversation & Suggestion Area
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      item {
        Text(
          text = "Ask S.A.D AI Assistant:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      // Quick Suggestions Horizontal Carousel
      item {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
          items(suggestionChips) { chip ->
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
              modifier = Modifier.clickable {
                inputPrompt = chip.substring(3).trim()
                viewModel.askAiCopilot(inputPrompt)
              }
            ) {
              Text(
                text = chip,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
      }

      // AI Response State Display
      item {
        when (val state = aiState) {
          is AiResponseState.Idle -> {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  imageVector = Icons.Default.Psychology,
                  contentDescription = null,
                  tint = SadAmberDark,
                  modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "How can S.A.D Saathi assist you?",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Ask anything about S.A.D Bike, Auto, or Cab fares, driver earnings with 10% commission, routes across Lucknow, or safety guidelines.",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                  ),
                  modifier = Modifier.padding(horizontal = 8.dp)
                )
              }
            }
          }

          is AiResponseState.Loading -> {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                CircularProgressIndicator(
                  modifier = Modifier.size(24.dp),
                  color = SadAmberDark,
                  strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                  text = "Gemini is analyzing S.A.D mobility routes & rates...",
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                )
              }
            }
          }

          is AiResponseState.Streaming -> {
            ResponseCard(
              header = "Gemini AI (Streaming Live...)",
              text = state.accumulatedText,
              isStreaming = true,
              onClear = { viewModel.clearAiResponse() }
            )
          }

          is AiResponseState.Success -> {
            ResponseCard(
              header = "S.A.D Saathi Answer:",
              text = state.responseText,
              isStreaming = false,
              onClear = { viewModel.clearAiResponse() }
            )
          }

          is AiResponseState.Error -> {
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
              border = BorderStroke(1.5.dp, SadRed),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = SadRed,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "AI Generation Error",
                    style = MaterialTheme.typography.titleSmall.copy(
                      color = SadRed,
                      fontWeight = FontWeight.Bold
                    )
                  )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = state.errorMessage,
                  style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF7F1D1D))
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                  onClick = { viewModel.askAiCopilot(inputPrompt.ifEmpty { "Explain S.A.D Ride fare calculation" }) },
                  colors = ButtonDefaults.buttonColors(containerColor = SadRed),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("Retry Query", fontSize = 12.sp, color = Color.White)
                }
              }
            }
          }
        }
      }
    }

    // Bottom Input Bar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 6.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = inputPrompt,
          onValueChange = { inputPrompt = it },
          placeholder = { Text("Ask S.A.D Saathi (Hindi / English)...", fontSize = 13.sp) },
          singleLine = true,
          modifier = Modifier
            .weight(1f)
            .testTag("ai_input_textfield"),
          shape = RoundedCornerShape(12.dp),
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
          keyboardActions = KeyboardActions(onSend = {
            if (inputPrompt.isNotBlank()) {
              keyboardController?.hide()
              viewModel.askAiCopilot(inputPrompt)
            }
          })
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
          onClick = {
            if (inputPrompt.isNotBlank()) {
              keyboardController?.hide()
              viewModel.askAiCopilot(inputPrompt)
            }
          },
          enabled = inputPrompt.isNotBlank() && aiState !is AiResponseState.Loading,
          modifier = Modifier
            .height(52.dp)
            .testTag("ai_send_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = SadAmberPrimary,
            contentColor = Color.Black
          )
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun ResponseCard(
  header: String,
  text: String,
  isStreaming: Boolean,
  onClear: () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.5.dp, SadAmberDark),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = SadAmberDark,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = header,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = SadAmberDark
            )
          )
        }
        if (!isStreaming) {
          IconButton(onClick = onClear, modifier = Modifier.size(24.dp)) {
            Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = "Clear",
              tint = Color.Gray,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colorScheme.outlineVariant
      )

      Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
          lineHeight = 22.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      )
    }
  }
}
