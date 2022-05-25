package Client;

import GUI.Window;

import javax.swing.text.BadLocationException;
import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException {
        Window window = new Window("Client Window");
        window.defaultDestination();
        MSGProcessor processor;
        window.linkButton.addActionListener(e -> {
            if(!window.linked) {
                System.out.println(window.IPText.getText());
                window.linkButton.setText("離線");
                window.linked = true;
            }
        });
        while (!window.linked) {System.out.println(" ");}
        window.revalidate();

        processor = new MSGProcessor(window.IPText.getText(),Integer.parseInt(window.PortText.getText()));
        Receiver.Port = Integer.parseInt(window.PortText.getText())+1;
        Receiver receiver = new Receiver(window,window.IPText.getText());
        ImageReceiver imageReceiver = new ImageReceiver(window,window.IPText.getText());
        MSGProcessor temp1 = processor;
        window.send.addActionListener(e -> {
            if (!window.type.getText().equals("")) {
                try {
                    window.display("Client(" + temp1.CIP + ")", window.type.getText());
                } catch (BadLocationException ignored) {}
                window.revalidate();
                try {
                    temp1.sendMessage(window.type.getText());
                } catch (Exception ignored) {}
                window.type.setText("");
            }
        });
        MSGProcessor temp = processor;
        window.linkButton.addActionListener(e -> {
            if(window.linked){
                try {
                    temp.sendMessage("disconnected!!!");
                    ImageSender.processor.sendMessage("disconnected!!!");
                } catch (Exception ignored) {}
                window.linked = false;
                window.dispose();
                System.exit(0);
            }
        });

        Thread receiver_thread = new Thread(receiver);
        receiver_thread.start();
        Thread imageReceiver_thread = new Thread(imageReceiver);
        imageReceiver_thread.start();
        ImageSender imageSender = new ImageSender(window,window.IPText.getText());
        Thread imageSender_thread = new Thread(imageSender);
        imageSender_thread.start();
    }
}
