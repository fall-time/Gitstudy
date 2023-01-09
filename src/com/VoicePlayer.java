package com;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class VoicePlayer extends Thread {
    boolean beginFlag=false;
    @Override
    public void run() {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream("voices/bgm.mp3"));
            Player player = new Player(in);
            Runnable inPlay = () ->
            {
                try {
                    player.play();
                    beginFlag=true;

                } catch (JavaLayerException e) {
                    throw new RuntimeException(e);
                }
            };
            new Thread(inPlay).start();
            while (!this.isInterrupted());
            player.close();
        } catch (FileNotFoundException | JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }
    public void boomPlay()  {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream("voices/boom.mp3"));
            Player player = new Player(in);
            player.play(20);
        } catch (JavaLayerException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
