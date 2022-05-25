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
            MSGProcessor temp = processor;
            while (Receiver.connected) {
                window.send.addActionListener(e -> {
                    if(!window.Path.getText().equals("")) {
                        try {
                            String path = window.Path.getText();
                            window.Path.setText("");
                            temp.send(window.img_path);
                            System.out.println(window.img_path);
                            window.display("Server(" + temp.SIP + ")", new ImageIcon(path), path);
                            temp.sendImage(path);
                        } catch (Exception exception) {
                            JOptionPane.showMessageDialog(null, "No such file", "file error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }
        }
    }
}
