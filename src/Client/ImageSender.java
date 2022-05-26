package Client;

import GUI.Window;

import javax.swing.*;
import java.io.IOException;

public class ImageSender implements Runnable{
    private final Window window;
    private String Destination;
    public static MSGProcessor processor;

    /*----------------------Constructor-------------------------------*/
    public ImageSender(Window window,String destination) throws IOException {
        this.window = window;
        this.Destination = destination;
        processor = new MSGProcessor(destination,Receiver.Port+1);
    }
    /*------------- Override run() method in thread ------------------*/
    /*--------- Invoke this method through thread.start() ------------*/
    @Override
    public void run() {
        /*------ when send button pressed start sending image --------*/
        window.send.addActionListener(e -> {
            if(!window.ImagePathText.getText().equals("")) {
                try {
                    /*----------- get image to send ------------------*/
                    String imagePathText = window.ImagePathText.getText();
                    window.ImagePathText.setText("");
                    /*------ send image to server & show in chat -----*/
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
