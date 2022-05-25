package Server;

import GUI.Window;

import javax.swing.*;
import java.io.IOException;

public class ImageSender implements Runnable{
    Window window;

    public ImageSender(Window window){
        this.window = window;
    }

    @Override
    public void run() {
        while (true) {
            MSGProcessor processor = null;
            try {
                processor = new MSGProcessor(Receiver.port+3);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MSGProcessor msgProcessor = processor;
            while (Receiver.connected) {
                window.send.addActionListener(e -> {
                    if(!window.ImagePathText.getText().equals("")) {
                        try {
                            String imagePathText = window.ImagePathText.getText();
                            window.ImagePathText.setText("");
                            msgProcessor.sendMessage(window.imagePathCache);
                            System.out.println(window.imagePathCache);
                            window.display("Server(" + msgProcessor.SIP + ")", new ImageIcon(imagePathText), imagePathText);
                            msgProcessor.sendImage(imagePathText);
                        } catch (Exception exception) {
                            JOptionPane.showMessageDialog(null, "No such file", "file error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }
        }
    }
}
