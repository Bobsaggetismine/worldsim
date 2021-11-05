package core.network;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class NetworkSender implements Runnable{

    private boolean running = false;
    private ArrayList<Client> _clients;
    private BufferedImage _image;
    public NetworkSender(ArrayList<Client> clients, BufferedImage image) {
        _image = image;
        _clients = clients;
    }
    @Override
    public void run() {
        running = true;
        while(running){
            Client toRemove = null;
            for(var e : _clients){
                try {
                    e.send(_image);

                } catch (IOException ex) {
                    e.close();
                    toRemove = e;
                    ex.printStackTrace();
                }
            }
            if(toRemove != null){
                _clients.remove(toRemove);
                toRemove = null;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
