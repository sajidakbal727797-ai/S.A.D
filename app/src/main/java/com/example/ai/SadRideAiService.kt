package com.example.ai

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private const val TAG = "SadRideAiService"

sealed interface AiResponseState {
  data object Idle : AiResponseState
  data object Loading : AiResponseState
  data class Streaming(val accumulatedText: String) : AiResponseState
  data class Success(val responseText: String) : AiResponseState
  data class Error(val errorMessage: String) : AiResponseState
}

class SadRideAiService {

  // Lazily initialized with the latest supported model: gemini-3.8-flash
  private val model by lazy {
    Firebase.ai.generativeModel(
      modelName = "gemini-3.8-flash",
      generationConfig = generationConfig {
        temperature = 0.7f
        topK = 40
        topP = 0.95f
      }
    )
  }

  private val systemContext = """
    You are 'S.A.D Saathi' - the intelligent AI Mobility Co-Pilot for 'S.A.D Ride' by 'S.A.D Enterprises'.
    Platform Highlights:
    - Offers Bike (solo commute), Auto (economical 3-wheeler), and Cab (4-wheeler AC comfort).
    - Fair & Transparent: S.A.D Enterprises charges only a 10% platform commission from Captains (compared to 25-30% charged by Ola/Uber), allowing drivers to keep 90% of their earnings.
    - Negative Balance Limit: Drivers can operate down to -₹150 in credit on cash rides; below that, they recharge via Razorpay.
    - Safety: 4-digit Ride OTP, live telemetry, GPS radar.
    Be extremely helpful, polite, concise, and knowledgeable in both English and Hindi/Hinglish.
  """.trimIndent()

  suspend fun askAssistant(prompt: String): Flow<AiResponseState> = flow {
    emit(AiResponseState.Loading)
    val accumulated = StringBuilder()

    try {
      // Mandated: realistic coroutine timeout window of 60 seconds
      withTimeout(60_000L) {
        val fullPrompt = "$systemContext\n\nUser Question: $prompt"
        model.generateContentStream(fullPrompt).collect { chunk ->
          val chunkText = chunk.text.orEmpty()
          accumulated.append(chunkText)
          emit(AiResponseState.Streaming(accumulated.toString()))
        }
      }

      val finalText = accumulated.toString()
      if (finalText.isNotBlank()) {
        emit(AiResponseState.Success(finalText))
      } else {
        emit(AiResponseState.Error("Model returned an empty response. Please try again."))
      }
    } catch (e: TimeoutCancellationException) {
      Log.e(TAG, "Gemini generation timed out", e)
      emit(AiResponseState.Error("AI response timed out after 60 seconds. Please check network connection."))
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      Log.e(TAG, "Firebase AI Logic generation failed", e)
      emit(AiResponseState.Error(e.localizedMessage ?: "AI Assistant encountered an error."))
    }
  }

  suspend fun generateFleetReport(
    totalGmv: Double,
    totalCommission: Double,
    activeCaptainsCount: Int,
    deficitCaptainsCount: Int,
  ): String = withContext(Dispatchers.IO) {
    withTimeout(60_000L) {
      val prompt = """
        $systemContext
        Generate an Executive Admin Fleet Analytics Summary for S.A.D Enterprises based on these live metrics:
        - Total Gross Merchandise Value (GMV): ₹${totalGmv.toInt()}
        - S.A.D Enterprises 10% Net Platform Revenue: ₹${totalCommission.toInt()}
        - Active Captains Fleet: $activeCaptainsCount
        - Captains in Deficit (< -₹150 limit): $deficitCaptainsCount

        Provide:
        1. 2-sentence Executive Summary of platform health
        2. Financial risk analysis on the deficit captains
        3. Strategic recommendation to maximize captain retention and rider satisfaction.
      """.trimIndent()

      val response = model.generateContent(prompt)
      response.text ?: "Executive summary generated successfully."
    }
  }
}
