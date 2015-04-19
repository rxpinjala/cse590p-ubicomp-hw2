Ravi Pinjala
Ubiquitous Computing
Assignment 2
https://github.com/rxpinjala/cse590p-ubicomp-hw2

For this assignment, I implemented a heart rate monitor using camera data. To use it, place your finger over the camera, and then hit the "Start!" button in the app. The app will collect samples until it has enough to compute a reasonable FFT, and then display the heart rate and update it once per second with new data. There's also a large debugging display that shows the RGB values from the camera, the samples of the red component over time (in red), and the FFT spectrum (in green) once we have enough data to display it. For best results, use the app in very bright light; I've found that I get pretty consistent output when using direct sunlight. 

The heart rate detection is based solely on a FFT computed over the red component of the average color in the image. Instead of having a separate band-pass filter, I just have the code that interprets the FFT result ignore values that are outside of the plausible frequencies for a heart rate. I'm using a relatively small FFT size (128 entries), because the sampling rate is very low (I only get 7.5 fps from the camera), and I preferred to get a result more quickly. (A future possible enhancement would be to do a dynamic FFT size, where we could compute a preliminary result based on the first 128 entries, and progressively grow the FFT to increase precision as we get more data.)

The code is split across three files:
- DataStore.java has the core logic around storing timestamped samples, and computing FFTs based on them
- ColorDisplay.java has the code that draws a representation of the data store to a SurfaceView
- MainActivity.java has all of the Android UI plumbing