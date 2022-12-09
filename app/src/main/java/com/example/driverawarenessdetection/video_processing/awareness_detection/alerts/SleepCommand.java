package com.example.driverawarenessdetection.video_processing.awareness_detection.alerts;

import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.MsgReader;

public class SleepCommand implements CommandInterface {

    MsgReader reader;

    SleepCommand (MsgReader reader){
        this.reader = reader;
    }

    @Override
    public void OnPositiveCommand() {
        reader.speak("Keep staying alert as you are");
    }

    @Override
    public void OnNegativeCommand() {
        reader.speak("wake up!");
    }
}
