Ravi Pinjala
Ubiquitous Computing
Assignment 2
https://github.com/rxpinjala/cse590p-ubicomp-hw2

For this assignment, I implemented a heart rate monitor using camera data. To use it, place your finger over the camera, and then hit the "Start!" button in the app. The app will collect samples until it has enough to compute a reasonable FFT, and then display the heart rate and update it once per second with new data. There's also a large debugging display that shows the RGB values from the camera, the samples of the red component over time (in red), and the FFT spectrum (in green) once we have enough data to display it. For best results, use the app in very bright light; I've found that I get pretty consistent output when using direct sunlight. 

