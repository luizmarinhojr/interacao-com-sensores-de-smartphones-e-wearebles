package com.example.domabusiness

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domabusiness.services.NotificationReaderService
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var ttsReady = false

    private lateinit var btnVoiceCommand: Button
    private lateinit var btnReadNotifications: Button
    private lateinit var btnEmergencyAlert: Button
    private lateinit var btnHelp: Button
    private lateinit var tvStatus: TextView

    private lateinit var vibrator: Vibrator

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_RECORD_AUDIO = 1
    }

    // Launcher para reconhecimento de voz
    private val speechRecognizerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                val command = matches[0]
                processVoiceCommand(command)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        initializeServices()
        requestPermissions()
        setupClickListeners()

        // Bem-vindo
        speakOut("Bem-vindo ao assistente Doma. Como posso ajudá-lo?")
    }

    private fun initializeViews() {
        btnVoiceCommand = findViewById(R.id.btn_voice_command)
        btnReadNotifications = findViewById(R.id.btn_read_notifications)
        btnEmergencyAlert = findViewById(R.id.btn_emergency_alert)
        btnHelp = findViewById(R.id.btn_help)
        tvStatus = findViewById(R.id.tv_status)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private fun initializeServices() {
        tts = TextToSpeech(this, this)
    }

    private fun requestPermissions() {
        // Solicita permissão de áudio
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO
            )
        }
    }

    private fun setupClickListeners() {
        btnVoiceCommand.setOnClickListener {
            vibrate()
            speakOut("Aguardando comando")
            startVoiceRecognition()
        }

        btnReadNotifications.setOnClickListener {
            vibrate()
            readNotifications()
        }

        btnEmergencyAlert.setOnClickListener {
            vibrate()
            triggerEmergencyAlert()
        }

        btnHelp.setOnClickListener {
            vibrate()
            provideHelp()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("pt", "BR"))

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts.setLanguage(Locale.US)
            }

            ttsReady = true
            updateStatus("Sistema Pronto", true)
            Log.i(TAG, "TTS pronto!")
        } else {
            Log.e(TAG, "Falha ao inicializar TTS")
            updateStatus("Erro no TTS", false)
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga seu comando")
        }

        try {
            speechRecognizerLauncher.launch(intent)
        } catch (e: Exception) {
            speakOut("Erro ao iniciar reconhecimento de voz")
            Toast.makeText(this, "Reconhecimento de voz não disponível", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processVoiceCommand(command: String) {
        Log.d(TAG, "Comando recebido: $command")
        val lowerCommand = command.lowercase()

        when {
            lowerCommand.contains("notificação") || lowerCommand.contains("mensagem") -> {
                speakOut("Lendo notificações")
                readNotifications()
            }
            lowerCommand.contains("emergência") || lowerCommand.contains("ajuda") ||
                    lowerCommand.contains("socorro") -> {
                speakOut("Ativando alerta de emergência")
                triggerEmergencyAlert()
            }
            lowerCommand.contains("instrução") || lowerCommand.contains("ajuda") ||
                    lowerCommand.contains("como usar") -> {
                provideHelp()
            }
            lowerCommand.contains("hora") || lowerCommand.contains("horas") -> {
                speakCurrentTime()
            }
            lowerCommand.contains("repetir") -> {
                speakOut("Repetindo última mensagem")
            }
            else -> {
                speakOut("Comando não reconhecido. Diga: notificações, emergência, ajuda, ou hora")
            }
        }
    }

    private fun readNotifications() {
        if (!isNotificationServiceEnabled()) {
            speakOut("Permissão de notificações não concedida. Por favor, ative nas configurações")
            showNotificationPermissionDialog()
            return
        }

        speakOut("Verificando notificações")

        val notificationService = getNotificationService()
        notificationService?.readPendingNotifications()
    }

    private fun getNotificationService(): NotificationReaderService? {
        return null
    }

    private fun triggerEmergencyAlert() {
        vibrateLong()

        val alertMessage = "Atenção! Alerta de emergência ativado. " +
                "Procure a saída de emergência mais próxima. " +
                "Mantenha a calma e siga as instruções da equipe de segurança."

        repeat(3) {
            speakOut(alertMessage)
        }

        Toast.makeText(this, "ALERTA DE EMERGÊNCIA ATIVADO", Toast.LENGTH_LONG).show()
    }

    private fun provideHelp() {
        val helpMessage = "Assistente Doma. " +
                "Botão verde: ative comando de voz. " +
                "Botão azul: leia notificações. " +
                "Botão vermelho: alerta de emergência. " +
                "Botão laranja: ajuda e instruções. " +
                "Comandos de voz disponíveis: notificações, emergência, ajuda, hora."

        speakOut(helpMessage)
    }

    private fun speakCurrentTime() {
        val currentTime = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(java.util.Date())
        speakOut("São $currentTime")
    }

    private fun speakOut(text: String) {
        if (ttsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utterance_${System.currentTimeMillis()}")
            Log.i(TAG, "Falando: $text")
        } else {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }

    private fun vibrateLong() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 500, 200, 500), -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(1000)
        }
    }

    private fun updateStatus(message: String, success: Boolean) {
        tvStatus.text = message
        tvStatus.setTextColor(
            ContextCompat.getColor(this, if (success) android.R.color.holo_green_light else android.R.color.holo_red_light)
        )
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val packageName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (name in names) {
                val componentName = ComponentName.unflattenFromString(name)
                if (componentName != null && TextUtils.equals(packageName, componentName.packageName)) {
                    return true
                }
            }
        }
        return false
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissão Necessária")
            .setMessage("Para ler notificações em voz alta, é necessário conceder permissão. Deseja abrir as configurações?")
            .setPositiveButton("Sim") { _, _ ->
                try {
                    // Tenta abrir as configurações de notificações
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao abrir configurações de notificação", e)

                    // Fallback: tenta abrir configurações gerais
                    try {
                        val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(settingsIntent)
                        Toast.makeText(
                            this,
                            "Navegue até Apps > Permissões Especiais > Acesso a Notificações",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e2: Exception) {
                        Toast.makeText(
                            this,
                            "Não foi possível abrir configurações. Ative manualmente em: Configurações > Apps > Doma Assistente > Permissões",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .setNegativeButton("Agora não") { _, _ ->
                speakOut("Permissão de notificações não concedida. Algumas funcionalidades não estarão disponíveis.")
            }
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speakOut("Permissão de áudio concedida")
                } else {
                    speakOut("Permissão de áudio negada. Comando de voz não funcionará")
                }
            }
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