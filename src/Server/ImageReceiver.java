package Server;

import GUI.Window;

import javax.swing.*;
import java.io.IOException;

public class ImageReceiver implements Runnable{
    Window window;

    public ImageReceiver(Window window){
        this.window = window;
    }
    @Override
    public void run() {
        while(true) {
            MSGProcessor server = null;
            try {
                server = new MSGProcessor(Receiver.port+2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuffer msg;
            while (Receiver.connected) {
                try {
                    msg = server.read();
                    if (msg.toString().equals("disconnected!!!")) {
                        break;
                    }
                    server.saveImage(msg.toString());
                    window.display("Server(" + server.SIP + ")", new ImageIcon(msg.toString()), msg.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
