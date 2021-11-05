package core.network;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client {
    private Socket _socket;
    public Client(Socket s){
        _socket = s;
    }

    public void send(BufferedImage image) throws IOException {
           OutputStream outputStream = _socket.getOutputStream();
           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
           ImageIO.write(image, "png", byteArrayOutputStream);
           outputStream.write(byteArrayOutputStream.toByteArray());
           outputStream.flush();
    }
    public void close(){
        try {
            _socket.getOutputStream().flush();
            _socket.close();
        } catch (IOException e) {
            System.err.println("Unable to close socket! (was likely already closed)");
            e.printStackTrace();
        }
    }
}
