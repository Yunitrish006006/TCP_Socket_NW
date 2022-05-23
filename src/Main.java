import java.io.IOException;

public class Main extends Thread{

    public static void main(String[] args) throws IOException {
        MessageGUI messageGUI = new MessageGUI("Messenger");
        messageGUI.setUpUI();
        ThreadServer server = new ThreadServer();
        server.start();
    }
}