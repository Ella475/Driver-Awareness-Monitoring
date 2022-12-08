package com.example.driverawarenessdetection.video_processing.awareness_detection.alerts;

import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.MsgReader;

public class TakeBreakCommand implements CommandInterface {
    MsgReader reader;

    TakeBreakCommand (MsgReader reader){
        this.reader = reader;
    }


    @Override
    public void OnPositiveCommand() {
    }

    @Override
    public void OnNegativeCommand() {
        reader.speak("You are driving a long time. Consider taking a break!");
    }
}
