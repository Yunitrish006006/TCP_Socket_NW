package Server;

import GUI.Window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainServer {
    public static void main(String[] args) {
        /*------------------ open interaction window -----------------*/
        Window window = new Window("Server Window");
        window.defaultDestination();

        /*-------------- user press disconnect button ----------------*/
        window.linkButton.setText("離線");
        window.linkButton.addActionListener(e -> {
            if (!window.linked) {
                window.dispose();
                System.exit(0);
            }
        });
        /*------------ Create & Start message/image thread -----------*/
        Receiver receiver = new Receiver(window);
        Thread thread = new Thread(receiver);
        thread.start();
        ImageSender imageSender = new ImageSender(window);
        Thread imageSender_thread = new Thread(imageSender);
        imageSender_thread.start();
        ImageReceiver imageReceiver = new ImageReceiver(window);
        Thread imageReceiver_thread = new Thread(imageReceiver);
        imageReceiver_thread.start();

        while (true) {
            MSGProcessor server = null;
            try {
                server = new MSGProcessor(Receiver.port + 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*------------Server can send when connected  -----------*/
            while (Receiver.connected) {
                MSGProcessor temp = server;
                window.send.addActionListener(e -> {
                    if (!window.type.getText().equals("")) {
                        try {
                            window.display("Server[" + temp.SIP + "]", window.type.getText());
                        } catch (BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                        window.revalidate();
                        try {
                            temp.sendMessage(window.type.getText());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        window.type.setText("");
                    }
                });

            }

        }
    }
}
/*-------- MSGProcessor contains send/receive message/image method -------*/
class MSGProcessor {
    private ServerSocket serverSocket;
    private Socket socket;
    public InetAddress CIP;
    public InetAddress SIP;

    /*-------------------Constructor------------------------*/
    public MSGProcessor(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.socket = this.serverSocket.accept();
        CIP = socket.getInetAddress();
        SIP = socket.getLocalAddress();
    }
    /*---------- Send message through socket ---------------*/
    public void sendMessage(String msg) throws Exception {
        this.socket = this.serverSocket.accept();
        OutputStream outputStream = this.socket.getOutputStream();
        outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        socket.close();
    }
    /*--------- Receive message from socket ----------------*/
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
    /*--------- Send image through socket ------------------*/
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
    /*--------- Receive image from socket ------------------*/
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
class ImageReceiver implements Runnable{
    Window window;
    /*-------------------- Constructor------------------------*/
    public ImageReceiver(Window window){
        this.window = window;
    }
    /*------------- Override run() method in thread ------------------------*/
    /*--------- Invoke this method through thread.start() ------------------*/
    @Override
    public void run() {
        while(true) {
            MSGProcessor processor = null;
            try {processor = new MSGProcessor(Receiver.port+2);} catch (IOException ignored) {}
            StringBuffer stringBuffer = new StringBuffer();

            while (Receiver.connected && !stringBuffer.toString().equals("disconnected!!!")) {
                try {
                    stringBuffer = processor.readMessage();
                    processor.saveImage(stringBuffer.toString());
                    window.display("Server[" + processor.SIP + "]", new ImageIcon(stringBuffer.toString()), stringBuffer.toString());
                } catch (Exception exception) {
                    try {
                        window.display("[Debug]",exception.toString());
                    } catch (BadLocationException ignored) {}
                }
            }
        }
    }
}
class ImageSender implements Runnable{
    Window window;
    /*----------------------Constructor-------------------------------*/
    public ImageSender(Window window){
        this.window = window;
    }
    /*------------- Override run() method in thread ------------------*/
    /*--------- Invoke this method through thread.start() ------------*/
    @Override
    public void run() {
        while (true) {
            MSGProcessor processor = null;
            try {
                processor = new MSGProcessor(Receiver.port+3);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MSGProcessor msgProcessor = processor;
            while (Receiver.connected) {
                window.send.addActionListener(e -> {
                    if(!window.ImagePathText.getText().equals("")) {
                        try {
                            String imagePathText = window.ImagePathText.getText();
                            window.ImagePathText.setText("");
                            msgProcessor.sendMessage(window.imagePathCache);
                            System.out.println(window.imagePathCache);
                            window.display("Server(" + msgProcessor.SIP + ")", new ImageIcon(imagePathText), imagePathText);
                            msgProcessor.sendImage(imagePathText);
                        }
                        catch (Exception ignored) {}
                    }
                });
            }
        }
    }
}
class Receiver implements Runnable{
    private Window window;
    public static boolean connected = true;
    public static int port= 5501;
    /*---------------------Constructor-----------------------*/
    public Receiver(Window window){
        this.window = window;
    }
    /*------------ Override run() method in thread -------------------*/
    /*---------- Invoke this method through thread.start() -----------*/
    @Override
    public void run() {
        while(true) {
            System.out.println("ReceiveMain");
            MSGProcessor server = null;
            try {
                server = new MSGProcessor(port);
                window.IPText.setText(server.CIP.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            connected = true;
            while (connected) {
                StringBuffer msg = null;
                try {
                    msg = server.readMessage();
                    if (msg.toString().equals("disconnected!!!")) {
                        port+=5;
                        window.PortText.setText(String.valueOf(port));
                        connected = false;
                        window.revalidate();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    window.display("Client[" + server.CIP + "]", msg.toString());
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                window.revalidate();
            }

        }
    }
}