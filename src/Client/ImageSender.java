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
            if(!window.Path.getText().equals("")) {
                try {
                    String path = window.Path.getText();
                    window.Path.setText("");
                    processor.send(window.img_path);
                    System.out.println(window.img_path);
                    window.display("Client(" + processor.CIP + ")", new ImageIcon(path));
                    processor.sendImage(path);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "No such file", "file error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}
