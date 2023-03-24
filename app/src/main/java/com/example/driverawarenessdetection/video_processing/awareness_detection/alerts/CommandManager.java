package com.example.driverawarenessdetection.video_processing.awareness_detection.alerts;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.MsgReader;

import java.time.Duration;
import java.time.Instant;

public class CommandManager {

    private final TakeBreakCommand breakCommand;
    private final SleepCommand sleepCommand;
    private final AttentionCommand attentionCommand;
    private final AwarenessCommand awarenessCommand;
    public static final AlertsData alertsData = new AlertsData();
    private final int percentageCutOff;
    private Instant lastAlertTime;
    private Thread takeBreakThread;
    private volatile boolean alertDriving = false;

    public CommandManager(Context context, int percentageCutOff) {
        MsgReader reader = new MsgReader(context);
        this.attentionCommand = new AttentionCommand(reader);
        this.sleepCommand = new SleepCommand(reader);
        this.breakCommand = new TakeBreakCommand(reader);
        this.awarenessCommand = new AwarenessCommand(reader);
        this.percentageCutOff = percentageCutOff;
    }

    public void start() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.lastAlertTime = Instant.now();
        }

        startDrivingAlertThread();
    }

    private void startDrivingAlertThread() {
        long delay = getDrivingAlertDelayMillis();
        Log.i("DrivingAlert delay", String.valueOf(delay));
        if (delay == 0) {
            return;
        }
        takeBreakThread = new Thread(() -> {
            try {
                Thread.sleep(delay);
                alertDriving = true;
                Log.i("DrivingAlert", "Finished");
            }
            catch (Exception e){
                System.err.println(e);
            }
        });
        takeBreakThread.start();
    }

    public void stop() {
        if (takeBreakThread != null) {
            takeBreakThread.interrupt();
        }
    }

    private long getDrivingAlertDelayMillis() {
        DrivingAlertTiming t = alertsData.getDurationTimeAlert();
        return (long) t.ordinal() * 3600 * 1000;
    }

    public void asleepCommand(boolean asleep) {
        if (asleep)
            sleepCommand.OnNegativeCommand();
        else
            sleepCommand.OnPositiveCommand();

    }

    public void inattentiveCommand(boolean inattentive) {
        if (inattentive)
            attentionCommand.OnNegativeCommand();
        else
            attentionCommand.OnPositiveCommand();

    }

    public void unawareCommand(boolean unaware) {
        if (!alertsData.getAttentionAlert() || !alertsData.getSleepAlert())
            return;

        if (unaware)
            awarenessCommand.OnNegativeCommand();
        else
            awarenessCommand.OnPositiveCommand();

    }

    public void onDetectorNotify(boolean asleep, boolean inattentive, int awarenessPercentage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long lastNegativeAlert = 10;
            if (Duration.between(lastAlertTime, Instant.now()).getSeconds() > lastNegativeAlert) {
                if (asleep && alertsData.getSleepAlert()) {
                    asleepCommand(true);
                    lastAlertTime = Instant.now();
                }
                else if (inattentive && alertsData.getAttentionAlert()) {
                    inattentiveCommand(true);
                    lastAlertTime = Instant.now();
                }
                else if (awarenessPercentage < percentageCutOff) {
                    unawareCommand(true);
                    lastAlertTime = Instant.now();
                }
            }
            long durationWithoutAlerts = 20 * 60;
            if (Duration.between(lastAlertTime, Instant.now()).getSeconds() > durationWithoutAlerts) {
                if (awarenessPercentage > 90) {
                    unawareCommand(false);
                }
                else {
                    inattentiveCommand(false);
                }
                lastAlertTime = Instant.now();
            }
            if (alertDriving) {
                breakCommand.OnNegativeCommand();
                alertDriving = false;
                startDrivingAlertThread();
            }
        }
    }
}
