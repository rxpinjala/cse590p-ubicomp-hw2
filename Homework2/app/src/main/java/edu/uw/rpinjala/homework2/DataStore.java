package edu.uw.rpinjala.homework2;

import java.util.ArrayList;
import com.badlogic.gdx.audio.analysis.FFT;

public class DataStore {
    private ArrayList<Float> _data;
    private ArrayList<Long> _timestamps;

    private static final long INTERVAL = 5000; // 5 seconds, in milliseconds
    private static final int FFT_SIZE = 1024;

    public DataStore() {
        _data = new ArrayList<Float>();
    }

    public void addDataPoint(float f, long ts) {
        _data.add(f);
        _timestamps.add(ts);
        removeOldData();
    }

    private void removeOldData() {
        long currentTime = System.currentTimeMillis();

        // ugh, don't turn this in
        while (_timestamps.get(0) < (currentTime - INTERVAL)) {
            _data.remove(0);
        }
    }

    private float computeSampleRate() {
        long firstTs = _timestamps.get(0);
        long lastTs = _timestamps.get(_timestamps.size() - 1);
        long elapsed = lastTs - firstTs;

        // (number of samples - 1) / elapsed time
        return (float)(_timestamps.size() - 1) / elapsed;
    }

    public double computeHeartRate() {
        FFT fft = new FFT(FFT_SIZE, computeSampleRate());
        float[] data = new float[_data.size()];
        for (int i = 0; i < data.length; i++)
            data[i] = _data.get(i);

        fft.forward(data);

        return 0.0; // TODO
    }
}
