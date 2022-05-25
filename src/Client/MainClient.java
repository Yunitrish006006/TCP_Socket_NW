package Client;

import GUI.Window;

import javax.swing.text.BadLocationException;
import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException {
        Window window = new Window("Client Window",1);
        window.defaultDestination();
        MSGProcessor processor;
        window.connect.addActionListener(e -> {
            if(!window.connected) {
                System.out.println(window.IPText.getText());
                window.connect.setText("離線");
                window.connected = true;
            }
        });
        while (!window.connected) {
            window.content.setText("connecting...");
        }
        window.revalidate();

        processor = new MSGProcessor(window.IPText.getText(),Integer.parseInt(window.PortText.getText()));
        Receiver.Port = Integer.parseInt(window.PortText.getText())+1;
        Receiver receiver = new Receiver(window,window.IPText.getText());
        ImageReceiver imageReceiver = new ImageReceiver(window,window.IPText.getText());

        Thread thread = new Thread(receiver);
        thread.start();
        Thread thread2 = new Thread(imageReceiver);
        thread2.start();
        ImageSender imageSender = new ImageSender(window,window.IPText.getText());
        Thread thread3 = new Thread(imageSender);
        thread3.start();

        MSGProcessor fc1 = processor;
        window.send.addActionListener(e -> {
            if (!window.type.getText().equals("")) {
                try {
                    window.display("Client(" + fc1.CIP + ")", window.type.getText());
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
                window.revalidate();
                try {
                    fc1.send(window.type.getText());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                window.type.setText("");
            }
        });
        MSGProcessor temp = processor;
        window.connect.addActionListener(e -> {
            if(window.connected){
                try {
                    temp.send("disconnected!!!");
                    ImageSender.processor.send("disconnected!!!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                window.connected = false;
                window.dispose();
                System.exit(0);
            }
        });
    }
}
