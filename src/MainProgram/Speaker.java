package MainProgram;

import guru.ttslib.TTS;
import java.util.Locale;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

class Speaker {

    private static final TTS tts = new TTS();
    private static Synthesizer synthesizer;

    static void start(String word) {
        Speaker.initiate();
        if (word != null && word.length() > 0) {
            new Thread(() -> {
                Interface.setIsSpeaking(true);
                synthesizer.speakPlainText(word, null);
                Interface.setIsSpeaking(false);
            }).start();
        }
    }

    public static void initiate() {
        if (synthesizer == null) {
            try {
                System.setProperty("freetts.voices",
                        "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
                Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
                synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
                synthesizer.allocate();
            } catch (IllegalArgumentException | EngineException e) {
            }
        }
    }

}