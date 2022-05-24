import java.io.IOException;

public class Main extends Thread{

    public static void main(String[] args) {
        ThreadServer server = new ThreadServer();
        server.start();
    }
}