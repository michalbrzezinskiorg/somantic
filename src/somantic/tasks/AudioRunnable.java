package somantic.tasks;

import somantic.controller.Controller;
import java.util.logging.Level;
import java.util.logging.Logger;
import somantic.processors.AudioFFT;
import somantic.state.State;

public class AudioRunnable implements Runnable {

    private final AudioFFT fft;

    public AudioRunnable(AudioFFT fft) {
        this.fft = fft;
    }

    @Override
    public void run() {
        while (true) {
            fft.analizuj(State.getProgressBar());
            synchronized (this) {
                try {
                    this.wait(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
