import java.io.IOException;

public class Main extends Thread{

    public static void main(String[] args) throws IOException {
        MessageGUI messageGUI = new MessageGUI("MSG");

        messageGUI.setUpUI();
        messageGUI.autoFilled();

    }
}