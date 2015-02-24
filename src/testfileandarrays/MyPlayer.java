package testfileandarrays;

import java.io.BufferedInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MyPlayer extends Player{

    public MyPlayer(BufferedInputStream stream) throws JavaLayerException {
        super(stream);
    }

    @Override
    public void play() throws JavaLayerException {
    
        new Thread() {

            @Override
            public void run() {
                try {
                    MyPlayer.super.play();//To change body of generated methods, choose Tools | Templates.
                } catch (JavaLayerException ex) {
                    Logger.getLogger(MyPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }.start();
        
 //To change body of generated methods, choose Tools | Templates.
    }


}
