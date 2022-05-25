package Server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MSGProcessor {
    private ServerSocket serverSocket;
    private Socket socket;
    public InetAddress CIP;
    public InetAddress SIP;
    public MSGProcessor(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.socket = this.serverSocket.accept();
        CIP = socket.getInetAddress();
        SIP = socket.getLocalAddress();
    }
    public void send(String msg) throws Exception {
        this.socket = this.serverSocket.accept();
        OutputStream os = this.socket.getOutputStream();
        os.write(msg.getBytes(StandardCharsets.UTF_8));
        os.close();
        socket.close();
    }
    public StringBuffer read() throws Exception {
        this.socket = this.serverSocket.accept();
        InputStream in = this.socket.getInputStream();
        StringBuffer buf = new StringBuffer();
        try {
            while (true) {
                int x = in.read();
                if (x==-1) break;
                buf.append((char) ((byte) x));
            }
        }
        catch (Exception e) {in.close();}
        socket.close();
        return buf;
    }
    ///
    public void sendImage(String path) throws IOException {
        this.socket = this.serverSocket.accept();
        OutputStream os = socket.getOutputStream();
            FileInputStream fis = new FileInputStream(path);
                byte[] b = new byte[1024];
                int len;
                while((len=fis.read(b))!=-1){
                    os.write(b,0,len);
                }
            fis.close();
        os.close();
        socket.close();
    }
    public void saveImage(String path) throws IOException {
        this.socket = this.serverSocket.accept();
        InputStream is = socket.getInputStream();
            FileOutputStream fos = new FileOutputStream(path);
                byte[] b = new byte[1024];
                int len;
                while((len = is.read(b))!=-1){
                    fos.write(b,0,len);
                }
            fos.flush();
        is.close();
        socket.close();
    }
}
