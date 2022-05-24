import java.io.IOException;

public class Main extends Thread{

    public static void main(String[] args) throws IOException {
        MessageGUI messageGUI = new MessageGUI("CLIENT");
        messageGUI.setUpUI();
        MessageGUI.autoFilled();
    }
}