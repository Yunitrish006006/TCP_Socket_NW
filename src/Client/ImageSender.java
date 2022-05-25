package Client;

import GUI.Window;

import javax.swing.*;
import java.io.IOException;

public class ImageSender implements Runnable{
    private final Window window;
    private String Destination;
    public static MSGProcessor processor;

    public ImageSender(Window window,String destination) throws IOException {
        this.window = window;
        this.Destination = destination;
        processor = new MSGProcessor(destination,Receiver.Port+1);
    }

    @Override
    public void run() {
        window.send.addActionListener(e -> {
            if(!window.ImagePathText.getText().equals("")) {
                try {
                    String imagePathText = window.ImagePathText.getText();
                    window.ImagePathText.setText("");
                    processor.sendMessage(window.imagePathCache);
                    window.display("Client(" + processor.CIP + ")", new ImageIcon(imagePathText),imagePathText);
                    processor.sendImage(imagePathText);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "No such file", "file error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}
