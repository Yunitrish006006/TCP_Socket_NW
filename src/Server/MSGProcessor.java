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
    public void sendMessage(String msg) throws Exception {
        this.socket = this.serverSocket.accept();
        OutputStream outputStream = this.socket.getOutputStream();
        outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        socket.close();
    }
    public StringBuffer readMessage() throws Exception {
        this.socket = this.serverSocket.accept();
        InputStream inputStream = this.socket.getInputStream();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            while (true) {
                int temp = inputStream.read();
                if (temp==-1) break;
                stringBuffer.append((char) ((byte) temp));
            }
        }
        catch (Exception e) {inputStream.close();}
        socket.close();
        return stringBuffer;
    }
    public void sendImage(String pathOfImage) throws IOException {
        this.socket = this.serverSocket.accept();
        OutputStream outputStream = socket.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(pathOfImage);
                byte[] temp = new byte[1024];int length;
                while((length=fileInputStream.read(temp))!=-1){outputStream.write(temp,0,length);}
            fileInputStream.close();
        outputStream.close();
        socket.close();
    }
    public void saveImage(String pathOfImage) throws IOException {
        this.socket = this.serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(pathOfImage);
                byte[] temp = new byte[1024];int length;
                while((length = inputStream.read(temp))!=-1){fileOutputStream.write(temp,0,length);}
            fileOutputStream.flush();
        inputStream.close();
        socket.close();
    }
}
