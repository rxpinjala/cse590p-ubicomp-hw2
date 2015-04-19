package edu.uw.rpinjala.homework2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.core.*;
import org.opencv.android.*;

public class MainActivity extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "HW2";

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Failed to init opencv");
        }
    }

    private DataStore _data;
    private ColorDisplay _display;
    private long _lastUpdate;

    private CameraBridgeViewBase _openCvCameraView;
    private TextView _heartRateView;
    private TextView _dbgText1;
    private TextView _dbgText2;
    private TextView _dbgText3;
    private Button _startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _data = new DataStore();
        _lastUpdate = 0;

        _openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);

        _heartRateView = (TextView)findViewById(R.id.heartRate);

        _dbgText1 = (TextView)findViewById(R.id.dbgText1);
        _dbgText2 = (TextView)findViewById(R.id.dbgText2);
        _dbgText3 = (TextView)findViewById(R.id.dbgText3);

        _startButton = (Button)findViewById(R.id.startRecording);
        _startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        SurfaceView view = (SurfaceView)findViewById(R.id.surfaceView);
        _display = new ColorDisplay(_data, view);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (_openCvCameraView != null)
            _openCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_openCvCameraView != null)
            _openCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        Mat rgba = frame.rgba();

        Scalar components = Core.mean(rgba);
        final double red = components.val[0];
        final double green = components.val[1];
        final double blue = components.val[2];

        runOnUiThread(new Runnable() {
            private final long FFT_UPDATE_INTERVAL = 1000; // wait 1 second between heart rate updates

            public void run() {
                double dataPoint = red;

                long currentTime = System.currentTimeMillis();
                _data.addDataPoint((float)dataPoint, currentTime);
                _display.update();
                _dbgText1.setText("Red: " + red);
                _dbgText2.setText("Green: " + green);
                _dbgText3.setText("Blue: " + blue);

                if ((currentTime - _lastUpdate) < FFT_UPDATE_INTERVAL)
                    return;

                _lastUpdate = currentTime;
                if (_data.canComputeHeartRate()) {
                    double heartRate = _data.computeHeartRate();
                    _heartRateView.setText(String.format("%d", Math.round(heartRate)));
                } else {
                    double percentage = 100.0 * _data.size() / _data.FFT_SIZE;
                    _heartRateView.setText(String.format("%d%%", Math.round(percentage)));
                }

            }
        });

        return rgba;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    public void startRecording() {
        _openCvCameraView.setCvCameraViewListener(this);
        _openCvCameraView.enableView();
        _startButton.setVisibility(View.GONE);
    }
}
