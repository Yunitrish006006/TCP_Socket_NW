package Server;

import GUI.Window;

import javax.swing.text.BadLocationException;
import java.io.IOException;

public class Receiver implements Runnable{
    private Window window;
    public static boolean connected = true;
    public static int port= 5501;
    public Receiver(Window window){
        this.window = window;
    }
    @Override
    public void run() {
        while(true) {
            System.out.println("ReceiveMain");
            MSGProcessor server = null;
            try {
                server = new MSGProcessor(port);
                window.IPText.setText(server.CIP.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            connected = true;
            while (connected) {
                StringBuffer msg = null;
                try {
                    msg = server.readMessage();
                    if (msg.toString().equals("disconnected!!!")) {
                        port += 8;
                        connected = false;
                        window.PortText.setText(port+"");
                        System.out.println("now port: " + port);
                        window.revalidate();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    window.display("Client(" + server.CIP + ")", msg.toString());
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                window.revalidate();
            }

        }
    }
}
