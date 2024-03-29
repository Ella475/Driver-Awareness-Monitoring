package com.example.driverawarenessdetection;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.example.driverawarenessdetection.client.DriveDataSender;
import com.example.driverawarenessdetection.utils.BaseActivity;
import com.example.driverawarenessdetection.video_processing.awareness_detection.AwarenessManager;
import com.example.driverawarenessdetection.video_processing.awareness_detection.alerts.CommandManager;
import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.CameraSourceWrapper;
import com.example.driverawarenessdetection.video_processing.camera.CameraSourcePreview;
import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;


public class AwarenessDetectionActivity extends BaseActivity {
    private int awarenessPercentage = 100;
    private CircularProgressBar circularProgressBar;
    private TextView awarenessPercentageText;
    private CameraSourceWrapper csw;
    private final Handler handler = new Handler();
    private final int updateEvery = 2000; // 1000 milliseconds == 1 second
    private int percentageCutOff = 20;
    private boolean fatigued = false;
    private AwarenessManager manager = null;
    public CommandManager commandManager;
    boolean asleep = false;
    boolean inattentive = false;
    private volatile boolean stop = false;
    private DriveDataSender driveDataSender;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_detection);
        findViewById(R.id.awareness).getRootView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        CameraSourcePreview preview = findViewById(R.id.camera_source_preview);
        GraphicOverlay graphicOverlay = findViewById(R.id.graphic_overlay);

        csw = new CameraSourceWrapper(preview, graphicOverlay, this);
        csw.start();
        manager = csw.awarenessProcessor.onSuccessDetector.getManager();
        commandManager = new CommandManager(this, percentageCutOff);
        commandManager.start();
        driveDataSender = new DriveDataSender();

        initView();
    }

    @SuppressLint("SetTextI18n")
    private void onPercentageChanged() {
        if (manager != null) {
            awarenessPercentage = (int) (100 * manager.getAwareProbability());
            asleep = manager.isAsleep();
            inattentive = manager.isInattentive();
            // send to DriveDataCollector
            driveDataSender.sendDriveData(awarenessPercentage, asleep, inattentive);
        }

        commandManager.onDetectorNotify(asleep, inattentive, awarenessPercentage);

        if (awarenessPercentage < percentageCutOff) {
            this.circularProgressBar.setBackgroundProgressBarColor(getColor(0xFF, 0x00, 0x00));
        } else {
            this.circularProgressBar.setBackgroundProgressBarColor(getColor(0xA4, 0xAF, 0xF4));
        }
        final long animationDuration = 1000;
        this.circularProgressBar.setProgressWithAnimation((float) awarenessPercentage, animationDuration);
        this.awarenessPercentageText.setText(awarenessPercentage + "%");
    }


    private void initView() {
        this.circularProgressBar = findViewById(R.id.awarenessCircularProgressbar);
        this.awarenessPercentageText = findViewById(R.id.awarenessPercentage);
        this.onPercentageChanged();

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(view -> finishActivity());

        RelativeLayout fatigueBtn = findViewById(R.id.fatigue_btn);
        RelativeLayout normalBtn = findViewById(R.id.normal_btn);
        normalBtn.setBackgroundResource(R.drawable.pressed_push_bg);

        fatigueBtn.setOnClickListener(view -> {
            fatigueBtn.setBackgroundResource(R.drawable.pressed_ac_bg);
            normalBtn.setBackgroundResource(R.drawable.push_bg);
            fatigued = true;
            onFatigueChange();
        });

        normalBtn.setOnClickListener(view -> {
            fatigueBtn.setBackgroundResource(R.drawable.ac_bg);
            normalBtn.setBackgroundResource(R.drawable.pressed_push_bg);
            fatigued = false;
            onFatigueChange();
        });

        handler.postDelayed(new Runnable() {
            public void run() {
                onPercentageChanged();
                if (!stop) {
                    handler.postDelayed(this, updateEvery);
                }
            }
        }, updateEvery);
    }

    private void onFatigueChange() {
        if (manager != null) {
            if (fatigued) {
                percentageCutOff = 40;
                manager.sleep_detector.SLEEP_THRESHOLD = 0.7f;
            } else {
                percentageCutOff = 20;
                manager.sleep_detector.SLEEP_THRESHOLD = 0.5f;
            }
        }
    }

    public void finishActivity() {
        stop = true;
        csw.stop();
        commandManager.stop();
        finish();
    }

    public void onBackPressed() {
        finishActivity();
    }

    @ColorInt
    private int getColor(int R, int G, int B) {
        return (0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }
}
