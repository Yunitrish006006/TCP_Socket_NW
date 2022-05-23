import javax.swing.text.Document;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

//sender
public class ServerEntity {

    public int port;

    public ServerSocket serverSocket;

    public ServerEntity(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public void send(String msg) throws IOException {
        Socket socket = serverSocket.accept();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        socket.close();
    }

    public void send_byte(byte[] buf) throws IOException {
        Socket socket = serverSocket.accept();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(buf);
        outputStream.close();
        socket.close();
    }
    public static void main(String[] args) throws IOException {
        System.out.println("To Send : ");
        ServerEntity server = new ServerEntity(5555);
        server.start();

        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        server.send(msg);
        System.out.printf("You have sent: "+ msg);
    }

}
