package Server;

import GUI.Window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.IOException;

public class ImageReceiver implements Runnable{
    Window window;

    public ImageReceiver(Window window){
        this.window = window;
    }
    @Override
    public void run() {
        while(true) {
            MSGProcessor processor = null;
            try {processor = new MSGProcessor(Receiver.port+2);} catch (IOException ignored) {}
            StringBuffer stringBuffer = new StringBuffer();
            while (Receiver.connected && !stringBuffer.toString().equals("disconnected!!!")) {
                try {
                    stringBuffer = processor.readMessage();
                    processor.saveImage(stringBuffer.toString());
                    window.display("Server[" + processor.SIP + "]", new ImageIcon(stringBuffer.toString()), stringBuffer.toString());
                } catch (Exception exception) {
                    try {
                        window.display("[Debug]",exception.toString());
                    } catch (BadLocationException ignored) {}
                }
            }
        }
    }
}
