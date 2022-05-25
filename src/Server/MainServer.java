package Server;

import GUI.Window;

import javax.swing.text.BadLocationException;
import java.io.IOException;

public class MainServer {
    public static void main(String[] args) {
        Window window = new Window("Server Window");
        window.defaultDestination();
        window.linkButton.setText("關閉");
        window.linkButton.addActionListener(e -> {
            if (!window.linked) {
                window.dispose();
                System.exit(0);
            }
        });
        Receiver receiver = new Receiver(window);
        Thread thread = new Thread(receiver);
        thread.start();
        ImageSender imageSender = new ImageSender(window);
        Thread thread1 = new Thread(imageSender);
        thread1.start();
        ImageReceiver imageReceiver = new ImageReceiver(window);
        Thread thread2 = new Thread(imageReceiver);
        thread2.start();

        while (true) {
            MSGProcessor server = null;
            try {
                server = new MSGProcessor(Receiver.port + 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (Receiver.connected) {
                MSGProcessor temp = server;
                window.send.addActionListener(e -> {
                    if (!window.type.getText().equals("")) {
                        try {
                            window.display("Server(" + temp.SIP + ")", window.type.getText());
                        } catch (BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                        window.revalidate();
                        try {
                            temp.sendMessage(window.type.getText());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        window.type.setText("");
                    }
                });

            }

        }
    }
}
