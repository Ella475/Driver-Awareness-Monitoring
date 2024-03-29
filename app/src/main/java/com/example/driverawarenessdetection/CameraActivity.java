package com.example.driverawarenessdetection;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;


import com.example.driverawarenessdetection.utils.BaseActivity;
import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.CameraSourceWrapper;
import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.MsgReader;
import com.example.driverawarenessdetection.video_processing.camera.CameraSourcePreview;
import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;

import java.util.Objects;


public class CameraActivity extends BaseActivity {

    private MsgReader reader;
    private CameraSourceWrapper csw;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(getSupportActionBar()).hide();

        CameraSourcePreview preview = findViewById(R.id.camera_source_preview);
        GraphicOverlay graphicOverlay = findViewById(R.id.graphic_overlay);

        csw = new CameraSourceWrapper(preview, graphicOverlay, this);
        csw.start();
        initCalibration();
    }

    public void initCalibration() {
        Button calibrationBtn = findViewById(R.id.calibrationBtn);
        calibrationBtn.setBackgroundTintMode(null);
        reader = new MsgReader(CameraActivity.this);
        calibrationBtn.setOnClickListener(v -> {
            reader.speak(getString(R.string.start_calibration));
            reader.speak(getString(R.string.look_at_road));
            reader.speak(getString(R.string.count));
            csw.awarenessProcessor.onSuccessDetector.getManager().onCalibration();
            reader.speak(getString(R.string.finish));
        });
    }

    public void finishActivity() {
        reader.stop();
        csw.stop();
        finish();
    }

    public void onBackPressed() {
        finishActivity();
    }
}
