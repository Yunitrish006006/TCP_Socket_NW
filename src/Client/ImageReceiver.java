package Client;

import GUI.Window;

import javax.swing.*;
import java.io.IOException;

public class ImageReceiver implements Runnable{
    private Window window;
    private String Destination;
    public MSGProcessor processor;

    public ImageReceiver(Window window,String destination) throws IOException {
        this.window = window;
        this.Destination = destination;
        processor = new MSGProcessor(destination,Receiver.Port +2);

    }

    @Override
    public void run() {
        StringBuffer stringBuffer;
        while(true){
            try {
                stringBuffer = processor.readMessage();
                processor.saveImage(stringBuffer.toString());
                window.display("Server["+ processor.Destination + "]",new ImageIcon(stringBuffer.toString()), stringBuffer.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
