package Client;

import GUI.Window;

import javax.swing.*;
import java.io.IOException;

public class ImageReceiver implements Runnable{
    private Window window;
    public MSGProcessor processor;

    public ImageReceiver(Window window,String destination) throws IOException {
        this.window = window;
        processor = new MSGProcessor(destination,Receiver.Port +2);

    }

    @Override
    public void run() {
        StringBuffer msg;
        while(true){
            try {
                msg = processor.read();
                processor.saveImage(msg.toString());
                window.display("Server("+ processor.Destination + ")",new ImageIcon(msg.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
