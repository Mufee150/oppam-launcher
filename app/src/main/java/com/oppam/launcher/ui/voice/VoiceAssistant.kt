package com.oppam.launcher.ui.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.*

/**
 * Voice Assistant using Android TextToSpeech
 * Speaks Malayalam responses to help elderly users
 * 
 * Features:
 * - Initialization with Malayalam locale
 * - Predefined Malayalam responses
 * - Custom text-to-speech
 * - Utterance tracking
 */
class VoiceAssistant(private val context: Context) {
    
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    
    companion object {
        private const val TAG = "VoiceAssistant"
        private const val SPEECH_RATE = 0.8f // Slower speech for elderly users
        private const val PITCH = 1.0f
    }
    
    /**
     * Initialize the TextToSpeech engine
     * @param onInitComplete Callback when initialization is complete
     */
    fun initialize(onInitComplete: (Boolean) -> Unit = {}) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Try to set Malayalam locale
                val result = textToSpeech?.setLanguage(Locale("ml", "IN"))
                
                if (result == TextToSpeech.LANG_MISSING_DATA || 
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w(TAG, "Malayalam not supported, falling back to English")
                    textToSpeech?.setLanguage(Locale.ENGLISH)
                }
                
                // Set speech parameters for elderly users
                textToSpeech?.setSpeechRate(SPEECH_RATE) // Slower
                textToSpeech?.setPitch(PITCH)
                
                // Set up utterance progress listener
                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        Log.d(TAG, "Speech started: $utteranceId")
                    }
                    
                    override fun onDone(utteranceId: String?) {
                        Log.d(TAG, "Speech completed: $utteranceId")
                    }
                    
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        Log.e(TAG, "Speech error: $utteranceId")
                    }
                })
                
                isInitialized = true
                onInitComplete(true)
                Log.i(TAG, "VoiceAssistant initialized successfully")
            } else {
                isInitialized = false
                onInitComplete(false)
                Log.e(TAG, "Failed to initialize TextToSpeech")
            }
        }
    }
    
    /**
     * Speak a predefined Malayalam response
     */
    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        if (!isInitialized) {
            Log.w(TAG, "VoiceAssistant not initialized yet")
            return
        }
        
        textToSpeech?.speak(text, queueMode, null, "OppamVoice_${System.currentTimeMillis()}")
    }
    
    /**
     * Convenience methods for common responses
     */
    fun sayWelcome() = speak(MalayalamResponses.WELCOME)
    
    fun sayNoFear() = speak(MalayalamResponses.NO_FEAR)
    
    fun sayScamDetected() = speak(MalayalamResponses.SCAM_DETECTED)
    
    fun sayCaregiverAlerted() = speak(MalayalamResponses.CAREGIVER_ALERTED)
    
    fun sayYouAreSafe() = speak(MalayalamResponses.YOU_ARE_SAFE)
    
    fun sayHangUpNow() = speak(MalayalamResponses.HANG_UP_NOW)
    
    fun sayOkay() = speak(MalayalamResponses.OKAY)
    
    /**
     * Stop current speech
     */
    fun stop() {
        textToSpeech?.stop()
    }
    
    /**
     * Check if currently speaking
     */
    fun isSpeaking(): Boolean {
        return textToSpeech?.isSpeaking ?: false
    }
    
    /**
     * Release resources - must be called when done
     */
    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        isInitialized = false
        Log.i(TAG, "VoiceAssistant shutdown")
    }
}
