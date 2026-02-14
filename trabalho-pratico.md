# Trabalho Prático | Lidando com sensores em dispositivos móveis

Nesta atividade a seguir compreenderemos que os apps do Wear OS podem funcionar
como um dos principais frameworks para o desenvolvimento de aplicações mobile. Um
aplicativo Wearable pode ter várias especialidades, desde entretenimento e
comunicação.

## Contextualização

Para uma melhoria na eficiência e na comunicação interna, a empresa “Doma" quer desenvolver um aplicativo Wear OS para assistência aos funcionários que têm necessidades especiais, uma forma de solidificar a interação entre os mesmos.

Assim, com os aplicativos wearables podem usar áudio para fornecer informações em tempo real, como leitura de mensagens de texto, notificações, lembretes e respostas a comandos de voz. Isso pode ser especialmente útil para pessoas com deficiência visual.

Além de serem úteis para treinamento e educação. Aplicativos podem usar áudio para fornecer instruções, dicas e feedbacks durante o aprendizado ou a prática de novas habilidades.

Outra funcionalidade que a empresa quer adotar, é um aplicativo wearable que pode usar o áudio para fornecer alertas de segurança, como notificações de emergência, alertas de tempestades, notícias importantes ou informações críticas.

## Roteiro de prática 📝

### Material necessário para a prática

- Editor de texto ou IDE sendo opções sugeridas: VS Code;
- Flutter SDK, o arquivo que permite utilizar a ferramenta;
- Android Studio e/ou xCode;
- Simulador Android ou iOS.
- Navegador Web: Google Chrome, Firefox, MS Edge, Safari ou Opera.

### Procedimentos 💻

1. Configuração do Ambiente:

    - Certifique-se de ter seu ambiente configurado.

    - Prepare um ambiente de simulação para Wear OS ou conecte um dispositivo wearable real.


2. Implementação de Saídas de áudio 🔊:

- AudioDeviceInfo.TYPE_BUILTIN_SPEAKER, em dispositivos com um alto-falante integrado.

- AudioDeviceInfo.TYPE_BLUETOOTH_A2DP quando um fone de ouvido Bluetooth estiver pareado e conectado.

- Utilize o método getDevices() com o valor de FEATURE_AUDIO_OUTPUT para enumerar todas as saídas de áudio:

    ````
    import android.content.Context
    import android.media.AudioDeviceInfo
    import android.media.AudioManager
    import android.content.pm.PackageManager


    class AudioHelper(context: Context) {
        private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        fun audioOutputAvailable(type: Int): Boolean {
            if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
                    return false
        }
            return audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS).any{it.type == type }
        }
    }

    // Exemplo de uso
    fun main() {
        val audioHelper = AudioHelper(context) // Substitua 'context' pelo contexto atual do seu aplicativo
        val isSpeakerAvailable = audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)
        // True if the device has a speaker
        val isBluetoothHeadsetConnected = audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)
        // True if a Bluetooth headset is connected
    }
    ````

3. Detecção Dinâmica de Dispositivos de Áudio:

    Seu app pode registrar um callback para detectar quando isso acontece usando registerAudioDeviceCallback:

    ````
    // Supondo que 'audioManager' já tenha sido inicializado
    audioManager.registerAudioDeviceCallback(object : AudioDeviceCallback() {
        override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
            super.onAudioDevicesAdded(addedDevices)

            if (audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                // Um fone de ouvido Bluetooth acabou de ser conectado
            }
        }

        override fun onAudioDevicesRemoved(removedDevices: Array<outAudioDeviceInfo>?) {
            super.onAudioDevicesRemoved(removedDevices)
            if (!audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                // Um fone de ouvido Bluetooth não está mais conectado
            }
        }
    }, null)

    

    fun audioOutputAvailable(type: Int): Boolean {
        // Implementação da função audioOutputAvailable

        // Retorna verdadeiro se o tipo de dispositivo de áudio especificado estiver disponível
    }
    ````

4. Facilitando a Conexão Bluetooth:

Se o app exigir que um fone de ouvido seja conectado para continuar, em vez de mostrar uma mensagem de erro, ofereça a opção de direcionar o usuário diretamente às configurações do Bluetooth para facilitar a conexão. Para isso, envie uma intent com ACTION_BLUETOOTH_SETTINGS:

    ````
    val intent = with (Intent(Settings.ACTION_BLUETOOTH_SETTINGS)) {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        putExtra("EXTRA_CONNECTION_ONLY", true)
        putExtra("EXTRA_CLOSE_ON_CONNECT", true)
        putExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 1)
    }

    startActivity(intent)
    ````

5. Reprodução de Áudio:

- Depois de detectar uma saída de áudio adequada, o processo para tocar áudio no Wear OS é o mesmo usado em dispositivos móveis ou outros dispositivos.

6. Uso de Alto-falantes em Dispositivos Wear OS:

Para dispositivos Wear OS que incluem alto-falantes, incorpore funcionalidades de áudio para enriquecer a experiência do usuário. Exemplos de uso incluem alarmes de relógio com notificações sonoras, apps de
fitness com instruções de voz para exercícios, e apps educativos com feedback auditivo.

<br>

## Resultados esperados ✨

Ao concluir esta missão, os alunos terão desenvolvido um aplicativo Wear OS que proporciona uma comunicação eficaz e assistência para funcionários com necessidades especiais. O aplicativo deverá ser capaz de ler mensagens e notificações em voz alta, responder a comandos de voz e fornecer alertas de segurança e instruções através de áudio. Este aplicativo não apenas melhora a eficiência e a comunicação interna na empresa "Doma", mas também demonstra a aplicação prática de tecnologias wearables para criar soluções acessíveis e inclusivas no local de trabalho.