package testfileandarrays;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MyPlayer {

    private Player player;

    public MyPlayer(File file) throws FileNotFoundException {

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        } catch (JavaLayerException | FileNotFoundException ex) {
            Logger.getLogger(MyPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void playTrack() throws JavaLayerException {

        new Thread() {

            @Override
            public void run() {
                try {
                    player.play();
                } catch (JavaLayerException ex) {
                    Logger.getLogger(MyPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }.start();
    }

    public void stop() {

        player.close();

    }
}
