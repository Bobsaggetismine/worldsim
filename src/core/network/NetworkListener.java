package core.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class NetworkListener implements Runnable {

    private List<Client> _clients;

    private boolean running = false;

    public NetworkListener(List<Client> clients){
        _clients = clients;
    }

    @Override
    public void run() {
        ServerSocket socket;
        try {
            socket = new ServerSocket(8192);
            running = true;
            while(running){
                try {
                    Socket clientSocket = socket.accept();
                    _clients.add(new Client(clientSocket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
