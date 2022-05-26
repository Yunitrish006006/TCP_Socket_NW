package Client;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
/*-------- MSGProcessor contains send/receive message/image method -------*/
public class MSGProcessor {

    private Socket socket;
    public String Destination;
    public int Port;
    public InetAddress CIP;

    /*-------------------Constructor------------------------*/
    public MSGProcessor(String destination,int port) throws IOException {
        this.Destination = destination;
        this.Port = port;
        this.socket = new Socket(destination,port);
        this.CIP = socket.getInetAddress();
    }
    /*---------- Send message through socket ---------------*/
    public void sendMessage(String toSend) throws Exception {
        this.socket = new Socket(Destination, Port);
        OutputStream outputStream = this.socket.getOutputStream();
        outputStream.write((toSend).getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }
    /*--------- Receive message from socket ----------------*/
    public StringBuffer readMessage() throws Exception {
        this.socket = new Socket(Destination, Port);
        InputStream inputStream = this.socket.getInputStream();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            while (true) {
                int input_result = inputStream.read();
                if (input_result==-1) break;
                stringBuffer.append((char)((byte) input_result));
            }
        }
        catch (Exception e) {inputStream.close();}
        return stringBuffer;
    }
    /*--------- Send image through socket ------------------*/
    public void sendImage(String path) throws IOException {
        this.socket = new Socket(Destination, Port);
        OutputStream outputStream = socket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        byte[] temp = new byte[1024];int length;
        while((length=fileInputStream.read(temp))!=-1) {outputStream.write(temp,0,length);}
        fileInputStream.close();
        outputStream.close();
    }
    /*--------- Receive image from socket ------------------*/
    public void saveImage(String path) throws IOException {
        this.socket = new Socket(Destination, Port);
        InputStream inputStream = socket.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
        byte[] temp = new byte[1024];int length;
        while((length = inputStream.read(temp))!=-1) {fileOutputStream.write(temp,0,length);}
        fileOutputStream.close();
        inputStream.close();
    }

}
