# Microatividades | Interação com sensores de smartphones e wearebles

## Microatividade 1: Implementar a visão geral e melhores práticas
para acesso a sensores

Somente instalação e configuração do Android Studio

## Microatividade 2: Criando um novo projeto no Android Studio

### Material necessário para a prática

- Android Studio: Para o desenvolvimento de aplicativos Android.
- Simulador Android ou iOS: Para testar aplicativos no ambiente simulado.
- Navegador Web: Google Chrome, Firefox, MS Edge, Safari ou Opera.

### Procedimentos 💻

1. Abra o Android Studio e acesse File > New > New Project. A janela New Project vai aparecer.

2. No painel Templates, selecione Wear OS. Em seguida, no painel principal, selecione selecione o modelo "No Activity" e clique em "Next".

3. Em Name, nós vamos utilizar "ListaDeTarefas" para esse exemplo. No campo "Package name", o próprio Android Studio irá sugerir algo baseado no nome do projeto, como "com.example.listadetarefas". Em "Minimum SDK", utilizaremos a API 30: Android 11.0 (R), por ser a mais recente, depois basta clicar em “Finish” e o Android Studio criará o projeto para você.

### Resultados esperados ✨ 

Esta microatividade permitirá que o aluno execute os passos iniciais para criar seu
primeiro aplicativo para Wear OS. Ao seguir esses procedimentos, o aluno terá
configurado um projeto usando um modelo do Android Studio e estará pronto para
iniciar o desenvolvimento do aplicativo.


## Microatividade 3: Arquivos de Lógica e Configurações

### Material necessário para a prática

- Editor de Texto ou IDE: Recomenda-se o uso do VS Code.
- Flutter SDK: Necessário para a utilização da ferramenta Flutter.
- Android Studio: Para o desenvolvimento de aplicativos Android.
- Simulador Android ou iOS: Para testar aplicativos no ambiente simulado.
- Navegador Web: Google Chrome, Firefox, MS Edge, Safari ou Opera.

### Procedimentos 💻

1. Criação da MainActivity: Crie a `MainActivity.java` clicando com o botão direito em "app" e selecione New > Activity > Empty Views Activity..

2. Nome e Layout da Atividade: Na janela, mantenha o nome da atividade como `MainActivity` e o "Layout Name" como `activity_main`.

3. Interface de Usuário: Desenvolva a interface da primeira tela do aplicativo com uma `ListView` e um `Button`.

4. Permissões no AndroidManifest.xml: Localize o `AndroidManifest.xml` na pasta
manifests e adicione as permissões:

````
    <uses-permission android:name="android.permission.BODY_SENSORS"/>
    <uses-permission android:name="android.permission.WAKE_LOCK"/>
````

Isso permite a interação com partes do sistema.

5. Intent-filter para MainActivity: No arquivo maAdicione o elemento `intent-filter`
para especificar as intents que a atividade pode responder, respondendo a intents com
a ação MAIN e a categoria LAUNCHER. Exemplo:

````
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
````

6. Dependências no build.gradle: Na área Gradle Scripts, temos o build.gradle, e lá encontraremos as dependências do projeto.

    Lembre-se de sincronizar o projeto após realizar essas alterações para garantir que as dependências sejam baixadas corretamente.


### Resultados esperados ✨

Nesta microatividade o aluno aprenderá os primeiros passos para criação do aplicativo. No Android Studio precisamos configurar alguns arquivos com informações do aplicativo e do dispositivo para o qual iremos desenvolver.


## Microatividade 4: Criando um emulador

### Material necessário para a prática

- Editor de Texto ou IDE: Recomenda-se o uso do VS Code.
- Flutter SDK: Necessário para a utilização da ferramenta Flutter.
- Android Studio: Para o desenvolvimento de aplicativos Android.
- Simulador Android ou iOS: Para testar aplicativos no ambiente simulado.
- Navegador Web: Google Chrome, Firefox, MS Edge, Safari ou Opera.

### Procedimentos 💻

1. No Android Studio, acesse o Device Manager pelo caminho Tools > Device Manager. É um botão do lado direito da barra de ferramentas que mostra um Android abrindo a cabeça ao lado de um dispositivo com um display roxo.

2. Depois que o Device Manager abrir, provavelmente você verá um emulador já criado e alguns detalhes sobre ele, principalmente o tipo de emulador, a API que está sendo usada e o tipo de CPU. Importante o Device Manager pode abrir como uma janela dentro do Android Studio ou como uma janela flutuante.

3. Para entender melhor entendimento desse processo, vamos criar um novo dispositivo virtual:

    Clique em Create Device, escolha a categoria Wear OS no lado esquerdo. Selecione o hardware que deseja emular (no nosso exemplo, Wear OS Small Round). Clique em Next

4. Escolha o sistema operacional que você deseja emular (por exemplo, Wear OS API 30). Se a imagem do sistema não estiver disponível, clique no link “Download” ao lado do nome para baixá-lo. Após selecionar a imagem do sistema, clique no botão Next.

5. A última tela permite confirmar suas escolhas e oferece opções para configurar algumas outras propriedades, como nome do dispositivo, orientação de inicialização e tamanho da memória RAM. Por enquanto, use os padrões e clique em Finish.

### Resultados esperados ✨

Esta microatividade destaca como criar emuladores de dispositivos Wearable, permitindo testar o funcionamento de aplicativos. Isso é útil para o desenvolvimento e teste de aplicativos Wear OS antes de implantá-los em dispositivos reais.


## Microatividade 5: Fazer capturas de telas com app complementar

### Material necessário para a prática

- Editor de Texto ou IDE: Recomenda-se o uso do VS Code.
- Flutter SDK: Necessário para a utilização da ferramenta Flutter.
- Android Studio: Para o desenvolvimento de aplicativos Android.
- Simulador Android ou iOS: Para testar aplicativos no ambiente simulado.
- Navegador Web: Google Chrome, Firefox, MS Edge, Safari ou Opera.

### Procedimentos 💻

1. Na IU, encontre a tela que você quer capturar.

2. No smartphone Android, ative as Opções do desenvolvedor, se ainda não estiverem ativadas. Para isso, acesse Configurações > Sobre o telefone e toque em Número da versão sete vezes.

3. Abra o app complementar do Wear no smartphone.

4. Toque no botão flutuante de três pontos no canto superior direito para abrir o menu.

5. Toque em “Fazer captura de tela do wearable”. Esta mensagem vai aparecer: Solicitação de captura de tela enviada. Depois, você receberá estas notificações: Pronto para enviar uma captura de tela do relógio e Toque para enviar.

6. Toque na notificação para conferir as opções de envio ou compartilhamento da
captura de tela por Bluetooth, Gmail ou outros meios.

### Resultados esperados ✨

Com esta microatividade o aluno compreenderá outra forma de realizar a captura de tela da UI app para wearables.