package edu.uw.rpinjala.homework2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ColorDisplay {
    private DataStore _data;
    private SurfaceView _view;
    private Paint _paintData;
    private Paint _paintFFT;

    public ColorDisplay(DataStore data, SurfaceView view) {
        _data = data;
        _view = view;

        _paintData = new Paint();
        _paintData.setARGB(255, 255, 128, 128); // reddish
        _paintFFT = new Paint();
        _paintFFT.setARGB(255, 128, 255, 128); // greenish

    }

    private float xPosFromTimestamp(long ts, long tsMin, long tsMax, int width) {
        return (float)(ts - tsMin) * width / (tsMax - tsMin);
    }

    private float yPosFromData(double d, int height) {
        return height - ((float)d * height / 255);
    }

    private float xPosFromFftIndex(int i, int fftSize, int width) {
        return (float)i * width / fftSize;
    }

    public void update() {
        SurfaceHolder sh = _view.getHolder();
        Canvas c = sh.lockCanvas();
        if (c == null)
            return;

        // Blank the canvas
        c.drawARGB(255, 0, 0, 0);

        int width = c.getWidth();
        int height = c.getHeight();
        long tsMax = _data.timestamp(_data.size() - 1);
        long tsMin = _data.timestamp(0);

        // Draw the FFT result
        float[] fftResult = _data.lastFftResult();
        if (fftResult != null) {
            int count = fftResult.length / 2; // because the FFT result is mirrored in the latter half
            for (int i = 0; i < count; i++) {
                float x = xPosFromFftIndex(i, count, width);
                float y = height - (fftResult[i] * 5);
                c.drawLine(x, y, x, height, _paintFFT);
            }
        }

        // Draw the sensor data
        int max = _data.size();
        for (int i = 0; i < max; i++) {
            long ts = _data.timestamp(i);
            double d = _data.data(i);

            float xPos = xPosFromTimestamp(ts, tsMin, tsMax, width);
            float yPos = (float)yPosFromData(d, height);

            if (xPos > 0)
                c.drawCircle(xPos, yPos, 2.0f, _paintData);
        }

        sh.unlockCanvasAndPost(c);
    }
}
