package com.example.driverawarenessdetection.video_processing.awareness_detection.alerts;

import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.MsgReader;

public class AwarenessCommand implements CommandInterface{

    MsgReader reader;

    AwarenessCommand (MsgReader reader){
        this.reader = reader;
    }

    @Override
    public void OnPositiveCommand() {
        reader.speak("Your awareness level is high! Well done!");
    }

    @Override
    public void OnNegativeCommand() {
        reader.speak("Your awareness level is very low!");
    }
}

