package com.example.driverawarenessdetection.video_processing.awareness_detection.alerts;

import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.MsgReader;

public class AttentionCommand implements CommandInterface {

    MsgReader reader;

    AttentionCommand (MsgReader reader){
        this.reader = reader;
    }

    @Override
    public void OnPositiveCommand() {
        reader.speak("You are attentive to the road, keep up the good driving");

    }

    @Override
    public void OnNegativeCommand() {
        reader.speak("Please pay attention to the road");
    }
}
