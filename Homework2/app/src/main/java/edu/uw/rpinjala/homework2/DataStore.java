package edu.uw.rpinjala.homework2;

import java.util.ArrayList;
import com.badlogic.gdx.audio.analysis.FFT;

public class DataStore {
    // arrays to store sensor data
    private float[] _data;
    private long[] _timestamps;

    // offsets into the arrays that bound the data we're still using
    private int _first;
    private int _next;

    private static final long INTERVAL = 5000; // 5 seconds, in milliseconds
    private static final int FFT_SIZE = 1024;

    // we want to store about 10 seconds of data, but the sampling rate is
    // variable and device-dependent, so let's just allocate way more space than we need
    private static final int DATA_STORE_SIZE = 4096;

    public DataStore() {
        _data = new float[DATA_STORE_SIZE];
        _timestamps = new long[DATA_STORE_SIZE];
        _first = 0;
        _next = 0;
    }

    private int size() {
        return _next - _first;
    }

    private float data(int i) {
        return _data[_first + i];
    }

    private long timestamp(int i) {
        return _timestamps[_first + i];
    }

    public void addDataPoint(float f, long ts) {
        _data[_next] = f;
        _timestamps[_next] = ts;
        _next++;

        removeOldData();
    }

    private void removeOldData() {
        long currentTime = System.currentTimeMillis();

        // shift _first past any stale data
        while (_timestamps[_first] < (currentTime - INTERVAL)) {
            _first++;
        }

        // shift data down to 0 if we're at the end
        if (_next == _data.length) {
            if (_first == 0) {
                // more data than we can handle! we could do some kind of neat dynamic buffer size, but for homework let's just bail out now
                throw new Error("data buffers too small");
            }

            int count = size(); // number of data points we're moving
            for (int i = 0; i < count; i++) {
                _data[i] = _data[i + _first];
                _timestamps[i] = _timestamps[i + _first];
            }

            _first = 0;
            _next = count;
        }
    }

    private float computeSampleRate() {
        if (size() == 0)
            return 0.0f; // no data

        long firstTs = _timestamps[_first];
        long lastTs = _timestamps[_next - 1];
        long elapsed = lastTs - firstTs;

        // (number of samples - 1) / elapsed time
        return (float)(size() - 1) / elapsed;
    }

    public double computeHeartRate() {
        FFT fft = new FFT(FFT_SIZE, computeSampleRate());
        float[] data = new float[size()];
        for (int i = 0; i < data.length; i++)
            data[i] = _data[_first + i];

        fft.forward(data);

        return 0.0; // TODO
    }
}
