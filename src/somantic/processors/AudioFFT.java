package somantic.processors;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JProgressBar;

/**
 * Object of this class audioInputStream able to deliver FFT results for sound,
 * also like array of affects based on 35hZ freq. *
 */
public class AudioFFT {

    private final Minim m;
    private final AudioInput in;
    private final FFT fft;
    private int band;
    private List<Integer> matrix;
    private final Algorytm flattenAlgo;
    private ArrayList<Integer> arrayOfAffects;

    public AudioFFT() {
        this.band = 0;
        matrix = new ArrayList<Integer>();
        m = new Minim(this);
        //AudioStream audioInputStream = m.getInputStream(1, 8, 44.100f, 16);
        m.debugOff();
        in = m.getLineIn(1);
        fft = new FFT(in.bufferSize(), in.sampleRate());
        flattenAlgo = new Algorytm();
        arrayOfAffects = new ArrayList();
    }

    /**
     * method will prepare data based on FFT analyse of the audio signal for
     * the future purposes
     */
    public void analizuj(JProgressBar jp) {
        Interface.setIsListening(true);
        try {
            fft.forward(in.mix);
            band = (int) flattenAlgo.licz(fft.getBand(35) * 1000);
            for (int i = 0; i < 1000; i++) {
                arrayOfAffects.add((int) (fft.getBand(i) * 1000));
            }
            jp.setValue(band);
            Interface.setVolume(band);
            matrix.add(band);
            for (int i = 0; matrix.size() > 100; i++) {
                matrix.remove(0);
            }
        } catch (Exception e) {
            //System.err.println(e);
        }
        Interface.setIsListening(false);
    }

    public List<Integer> getMatrix() {
        List<Integer> m = matrix;
        return m;
    }

    public ArrayList<Integer> getArrayOfAffects() {
        ArrayList<Integer> e = (ArrayList<Integer>) arrayOfAffects.clone();
        arrayOfAffects.clear();
        return e;
    }

    public int getBand() {
        return Integer.parseInt(String.valueOf(band).replace(".", ""));
    }
}