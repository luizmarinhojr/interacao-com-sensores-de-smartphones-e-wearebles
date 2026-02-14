package com.example.domabusiness

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var ttsReady = false
    private lateinit var btnSpeech: Button

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnSpeech = findViewById(R.id.btn_speech)

        // Inicializa TTS
        tts = TextToSpeech(this, this)

        // Configura botão
        btnSpeech.setOnClickListener {
            speakOut("Hello my friend")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Idioma não suportado")
                Toast.makeText(this, "Idioma não suportado", Toast.LENGTH_SHORT).show()
            } else {
                ttsReady = true
                Log.i(TAG, "TTS pronto!")
            }
        } else {
            Log.e(TAG, "Falha ao inicializar TTS")
            Toast.makeText(this, "Erro ao inicializar TTS", Toast.LENGTH_SHORT).show()
        }
    }

    private fun speakOut(text: String) {
        if (ttsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
            Log.i(TAG, "Falando: $text")
        } else {
            Toast.makeText(this, "TTS não está pronto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}