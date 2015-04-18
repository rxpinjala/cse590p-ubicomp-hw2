package edu.uw.rpinjala.homework2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
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

    private CameraBridgeViewBase _openCvCameraView;
    private TextView _heartRateView;
    private TextView _dbgText1;
    private TextView _dbgText2;
    private TextView _dbgText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _data = new DataStore();

        _openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        _openCvCameraView.setCvCameraViewListener(this);
        _openCvCameraView.enableView();

        _heartRateView = (TextView)findViewById(R.id.heartRate);

        _dbgText1 = (TextView)findViewById(R.id.dbgText1);
        _dbgText2 = (TextView)findViewById(R.id.dbgText2);
        _dbgText3 = (TextView)findViewById(R.id.dbgText3);

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
            public void run() {
                double dataPoint = red * 3 / (red + green + blue);

                _data.addDataPoint((float)dataPoint, System.currentTimeMillis());
                if (_data.canComputeHeartRate()) {
                    double heartRate = _data.computeHeartRate();
                    _heartRateView.setText(String.format("%.1f", heartRate));
                }

                _display.update();

                _dbgText1.setText("Red: " + red);
                _dbgText2.setText("Green: " + green);
                _dbgText3.setText("Blue: " + blue);
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
}
