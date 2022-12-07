package com.example.driverawarenessdetection.video_processing.awareness_detection.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class MsgReader implements TextToSpeech.OnInitListener {
    private final TextToSpeech textToSpeech;
    private final Context c;

    public MsgReader(Context context) {
        textToSpeech = new TextToSpeech(context, this);
        textToSpeech.setSpeechRate(0.75f);
        c = context;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)
            textToSpeech.setLanguage(Locale.US);
    }

    public void stop() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public void speak(String msg) {
        textToSpeech.speak(msg, TextToSpeech.QUEUE_ADD, null, null);
    }
}