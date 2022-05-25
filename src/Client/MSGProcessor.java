package Client;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
public class MSGProcessor {
    private Socket socket;
    public String Destination;
    public int Port;
    public InetAddress CIP;

    public MSGProcessor(String destination,int port) throws IOException {
        this.Destination = destination;
        this.Port = port;
        this.socket = new Socket(destination,port);
        this.CIP = socket.getInetAddress();
    }
    public void send(String msg) throws Exception {
        this.socket = new Socket(Destination, Port);
        OutputStream os = this.socket.getOutputStream();
        os.write((msg).getBytes(StandardCharsets.UTF_8));
        os.close();
    }
    public StringBuffer read() throws Exception {
        this.socket = new Socket(Destination, Port);
        InputStream in = this.socket.getInputStream();
        StringBuffer buf = new StringBuffer();
        try {
            while (true) {
                int x = in.read();
                if (x==-1) break;
                buf.append((char)((byte) x));
            }
        }
        catch (Exception e) {in.close();}
        return buf;
    }
    ////
    public void sendImage(String path) throws IOException {
        this.socket = new Socket(Destination, Port);
        OutputStream os = socket.getOutputStream();
        FileInputStream fis = new FileInputStream(new File(path));
        byte[] b = new byte[1024];
        int len;
        while((len=fis.read(b))!=-1){
            os.write(b,0,len);
        }
        fis.close();
        os.close();
    }
    public void saveImage(String path) throws IOException {
        this.socket = new Socket(Destination, Port);
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream(new File(path));
        byte[] b = new byte[1024];
        int len;
        while((len = is.read(b))!=-1) {
            fos.write(b,0,len);
        }
        System.out.println("[Debug] get file");
        fos.close();
        is.close();
    }
}
