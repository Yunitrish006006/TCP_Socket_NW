package Client;

import GUI.Window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainClient {
    public static void main(String[] args) throws IOException {
        /*------------------ open interaction window -----------------*/
        Window window = new Window("Client Window");
        window.defaultDestination();
        MSGProcessor processor;

        /*------------------ user press link button ------------------*/
        window.linkButton.addActionListener(e -> {
            /*-------------- client cancel connection ----------------*/
            if(!window.linked) {
                window.linkButton.setText("離線");
                window.linked = true;
            }
        });
        int sec=30,mss=0,ms=0;
        while (!window.linked) {
            mss++;
            if(mss>100000) {
                ms++;
                mss=0;
            }
            if(ms>10000) {
                ms=0;
                sec--;
                window.content.setText("自動關閉倒數:"+sec);
            }
            if(sec<0) {
                System.exit(0);
            }
        }
        window.revalidate();
        /*----------------- Create processor class -------------------*/
        processor = new MSGProcessor(window.IPText.getText(),Integer.parseInt(window.PortText.getText()));
        /*-------------Create message/image receiver class------------*/
        Receiver.Port = Integer.parseInt(window.PortText.getText())+1;
        Receiver receiver = new Receiver(window,window.IPText.getText());
        ImageReceiver imageReceiver = new ImageReceiver(window,window.IPText.getText());
        MSGProcessor temp = processor;
        /*---------- button action is connect to server --------------*/
        window.linkButton.addActionListener(e -> {
            if(window.linked){
                try {
                    temp.sendMessage("disconnected!!!");
                    ImageSender.processor.sendMessage("disconnected!!!");
                } catch (Exception ignored) {}
                window.linked = false;
                window.dispose();
                System.exit(0);
            }
        });
        MSGProcessor temp1 = processor;
        /*------------ button action is send ------------------------*/
        window.send.addActionListener(e -> {
            if (!window.type.getText().equals("")) {
                try {
                    window.display("Client[" + temp1.CIP + "]", window.type.getText());
                } catch (BadLocationException ignored) {}
                window.revalidate();
                try {
                    temp1.sendMessage(window.type.getText());
                } catch (Exception ignored) {}
                window.type.setText("");
            }
        });
        /*---------- Create & Start message/image thread -----------*/
        Thread receiver_thread = new Thread(receiver);
        receiver_thread.start();
        Thread imageReceiver_thread = new Thread(imageReceiver);
        imageReceiver_thread.start();
        ImageSender imageSender = new ImageSender(window,window.IPText.getText());
        Thread imageSender_thread = new Thread(imageSender);
        imageSender_thread.start();
    }
}
/*-------- MSGProcessor contains send/receive message/image method -------*/
class MSGProcessor {

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
class Receiver implements Runnable{
    private final Window window;
    private String Destination;
    public MSGProcessor processor;
    public static int Port;

    /*---------------------Constructor-----------------------*/
    public Receiver(Window window,String destination) throws IOException {
        this.window = window;
        this.Destination = destination;
        processor = new MSGProcessor(destination, Port);
    }
    /*------------ Override run() method in thread -------------------*/
    /*---------- Invoke this method through thread.start() -----------*/
    @Override
    public void run() {
        StringBuffer stringBuffer = null;
        while(true){
            /******* receive message from server & show in chat *********/
            try {stringBuffer = processor.readMessage();} catch (Exception ignored) {}
            try {window.display("Server["+ processor.Destination +"]",stringBuffer.toString());} catch (BadLocationException ignored) {}
        }
    }
}
class ImageSender implements Runnable{
    private final Window window;
    private String Destination;
    public static MSGProcessor processor;

    /*----------------------Constructor-------------------------------*/
    public ImageSender(Window window,String destination) throws IOException {
        this.window = window;
        this.Destination = destination;
        processor = new MSGProcessor(destination,Receiver.Port+1);
    }
    /*------------- Override run() method in thread ------------------*/
    /*--------- Invoke this method through thread.start() ------------*/
    @Override
    public void run() {
        /*------ when send button pressed start sending image --------*/
        window.send.addActionListener(e -> {
            if(!window.ImagePathText.getText().equals("")) {
                try {
                    /*----------- get image to send ------------------*/
                    String imagePathText = window.ImagePathText.getText();
                    window.ImagePathText.setText("");
                    /*------ send image to server & show in chat -----*/
                    processor.sendMessage(window.imagePathCache);
                    window.display("Client(" + processor.CIP + ")", new ImageIcon(imagePathText),imagePathText);
                    processor.sendImage(imagePathText);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "No such file", "file error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}
class ImageReceiver implements Runnable{
    private Window window;
    private String Destination;
    public MSGProcessor processor;
    /*-------------------- Constructor------------------------*/
    public ImageReceiver(Window window,String destination) throws IOException {
        this.window = window;
        this.Destination = destination;
        processor = new MSGProcessor(destination,Receiver.Port +2);

    }
    /*------------- Override run() method in thread ------------------------*/
    /*--------- Invoke this method through thread.start() ------------------*/
    @Override
    public void run() {
        StringBuffer stringBuffer;
        while(true){
            try {
                stringBuffer = processor.readMessage();
                processor.saveImage(stringBuffer.toString());
                window.display("Server["+ processor.Destination + "]",new ImageIcon(stringBuffer.toString()), stringBuffer.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
