package com.oppam.launcher.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.mutableStateOf
import java.util.Locale

/**
 * Voice Input Helper for Malayalam Speech Recognition
 * Uses Android SpeechRecognizer to convert speech to text
 */
class VoiceInputHelper(private val context: Context) {
    
    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = mutableStateOf(false)
    
    data class VoiceInputResult(
        val success: Boolean,
        val text: String = "",
        val error: String = ""
    )
    
    /**
     * Start listening for Malayalam speech input
     */
    fun startListening(
        onResult: (VoiceInputResult) -> Unit
    ) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onResult(VoiceInputResult(false, error = "Speech recognition not available"))
            return
        }
        
        // Create recognizer if not exists
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        }
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ml-IN") // Malayalam - India
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ml-IN")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "ml-IN")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "ആരോഗ്യ വിവരം പറയുക")
        }
        
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening.value = true
            }
            
            override fun onBeginningOfSpeech() {
                // User started speaking
            }
            
            override fun onRmsChanged(rmsdB: Float) {
                // Volume level changed
            }
            
            override fun onBufferReceived(buffer: ByteArray?) {
                // Partial audio data received
            }
            
            override fun onEndOfSpeech() {
                isListening.value = false
            }
            
            override fun onError(error: Int) {
                isListening.value = false
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No speech match"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Unknown error"
                }
                onResult(VoiceInputResult(false, error = errorMessage))
            }
            
            override fun onResults(results: Bundle?) {
                isListening.value = false
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    val recognizedText = matches[0]
                    onResult(VoiceInputResult(true, text = recognizedText))
                } else {
                    onResult(VoiceInputResult(false, error = "No speech recognized"))
                }
            }
            
            override fun onPartialResults(partialResults: Bundle?) {
                // Can be used to show interim results
            }
            
            override fun onEvent(eventType: Int, params: Bundle?) {
                // Reserved for future use
            }
        })
        
        speechRecognizer?.startListening(intent)
    }
    
    /**
     * Stop listening
     */
    fun stopListening() {
        speechRecognizer?.stopListening()
        isListening.value = false
    }
    
    /**
     * Check if currently listening
     */
    fun isListening(): Boolean {
        return isListening.value
    }
    
    /**
     * Parse health-related keywords from Malayalam text
     * Examples:
     * - "തലവേദന" → "Headache"
     * - "രക്തസമ്മർദ്ദം" → "Blood pressure"
     * - "പഞ്ചസാര" → "Sugar"
     */
    fun parseHealthKeywords(text: String): Map<String, String> {
        val keywords = mutableMapOf<String, String>()
        val lowerText = text.lowercase()
        
        // Symptoms
        when {
            lowerText.contains("തലവേദന") || lowerText.contains("headache") -> 
                keywords["symptom"] = "തലവേദന (Headache)"
            lowerText.contains("കാൽ വേദന") || lowerText.contains("leg pain") -> 
                keywords["symptom"] = "കാൽ വേദന (Leg pain)"
            lowerText.contains("ക്ഷീണം") || lowerText.contains("tired") -> 
                keywords["symptom"] = "ക്ഷീണം (Tired)"
            lowerText.contains("വയറുവേദന") || lowerText.contains("stomach") -> 
                keywords["symptom"] = "വയറുവേദന (Stomach pain)"
        }
        
        // Medications
        when {
            lowerText.contains("ഗുളിക") || lowerText.contains("tablet") || lowerText.contains("എടുത്തു") -> 
                keywords["medication"] = "മരുന്ന് എടുത്തു (Took medicine)"
        }
        
        // Health metrics
        when {
            lowerText.contains("രക്തസമ്മർദ്ദം") || lowerText.contains("blood pressure") -> 
                keywords["metric"] = "രക്തസമ്മർദ്ദം (Blood pressure)"
            lowerText.contains("പഞ്ചസാര") || lowerText.contains("sugar") -> 
                keywords["metric"] = "പഞ്ചസാര (Sugar)"
        }
        
        return keywords
    }
    
    /**
     * Clean up resources
     */
    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        isListening.value = false
    }
}
