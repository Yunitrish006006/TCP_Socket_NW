import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ThreadServer extends java.lang.Thread {
    boolean started = false;
    boolean send = false;
    MessageGUI messageGUI = new MessageGUI("Messenger");
    public ServerEntity server = new ServerEntity(5555);

    public void run() {
        if(!started) {
            try {
                server.start();
                messageGUI.setUpUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            started = true;
        }
        else {

            try {
                server.send(messageGUI.type.getDocument().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("type:"+messageGUI.type);
            if(messageGUI.is_send) {

                System.out.println("send!!");
                try {
                    server.send(messageGUI.type.getDocument().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                messageGUI.UpToWindow(messageGUI.type.getDocument(), messageGUI.content.getDocument());
                try {
                    BufferedImage bImage = ImageIO.read(new File(messageGUI.img_path));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, "jpg", bos );
                    byte [] data = bos.toByteArray();
                    server.send_byte(data);
                    server.send(messageGUI.type.getDocument().toString());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                messageGUI.type.setText("");
            }
            ClientEntity client = new ClientEntity(5555,"127.0.0.1");
            try {
                byte[] x = client.receive().getBytes();
                String p = Arrays.toString(x);
//                MessageGUI.UpToWindow((Document) (p),messageGUI.content.getDocument());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
