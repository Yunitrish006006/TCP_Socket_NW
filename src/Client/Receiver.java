package Client;
import GUI.Window;
import javax.swing.text.BadLocationException;
import java.io.IOException;
public class Receiver implements Runnable{
    private final Window window;
    private String Destination;
    public MSGProcessor processor;
    public static int Port;

    public Receiver(Window window,String destination) throws IOException {
        this.window = window;
        this.Destination = destination;
        processor = new MSGProcessor(destination, Port);
    }

    @Override
    public void run() {
        StringBuffer msg = null;
        while(true){
            try {
                msg = processor.read();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                window.display("Server("+ processor.Destination +")",msg.toString());
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
