package edu.uw.rpinjala.homework2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

import org.opencv.core.*;
import org.opencv.android.*;

public class MainActivity extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "HW2";

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Failed to init opencv");
        }
    }

    private CameraBridgeViewBase _openCvCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        _openCvCameraView.setCvCameraViewListener(this);
        _openCvCameraView.enableView();
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
        double red = components.val[0];
        return frame.rgba();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }
}
