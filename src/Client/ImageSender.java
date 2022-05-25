package Client;

import GUI.Window;

import javax.swing.*;
import java.io.IOException;

public class ImageSender implements Runnable{
    private final Window window;
    public static MSGProcessor processor;

    public ImageSender(Window window,String dest) throws IOException {
        this.window = window;
        processor = new MSGProcessor(dest,Receiver.Port+1);
    }

    @Override
    public void run() {
        window.send.addActionListener(e -> {
            String path = window.Path.getText();
            if(!path.equals("")) {
                try {
                    window.Path.setText("");
                    processor.send(path);
                    System.out.println(path);
                    window.display("Client(" + processor.CIP + ")", new ImageIcon(path));
                    processor.sendImage(path);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "No such file", "file error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}
