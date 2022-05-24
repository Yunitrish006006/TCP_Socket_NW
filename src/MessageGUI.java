import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class MessageGUI extends JFrame{
    private final JFileChooser jFileChooser = new JFileChooser(new File("."));
    static public JTextPane type;
    static public JTextPane content;
    static public boolean is_send = false;
    static public boolean img_selected = false;
    static public String img_path = "";
    static public JTextField PortText;
    static public JTextField IPText;
    static public JButton selectImage;
    static public DataOutputStream output;
    static public Socket sc_msg, sc_img;
    /*------------image values---------------*/
    static int image_num = 0;
    /*-------------text values---------------*/
    /*---------------------------------------*/
    public MessageGUI(String Title) {
        super.setLocation(0,0);
        super.setVisible(true);
        super.setResizable(false);
        super.setTitle(Title);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void myStyle(JTextPane jTextPane) {
        SimpleAttributeSet aSet = new SimpleAttributeSet();
        StyleConstants.setForeground(aSet, Color.DARK_GRAY);
        StyleConstants.setBackground(aSet, Color.RED);
        StyleConstants.setFontFamily(aSet, "lucida bright italic");
        StyleConstants.setFontSize(aSet, 20);
        StyleConstants.setAlignment(aSet, StyleConstants.ALIGN_LEFT);
        jTextPane.setCharacterAttributes(aSet,true);
    }
    void UpToWindow(Document source, Document dest) {
        try {
//            dest.remove(0, dest.getLength());

            ElementIterator iterator = new ElementIterator(source);
            Element element;

            while ((element = iterator.next()) != null) {
                if (element.isLeaf()) {
                    int start = element.getStartOffset();
                    int end = element.getEndOffset();
                    String text = source.getText(start, end - start);
                    dest.insertString(dest.getLength(),"client: " + text, element.getAttributes());
                }
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUpUI() throws IOException {
        int max_y;
        /*----------------------frame--------------------------*/
        Container container = super.getContentPane();
        container.setLayout(null);
        /*--------------------------ip-------------------------*/
        JLabel IPLabel = new JLabel("IP");
        IPLabel.setBounds(10, 10, 14, 15);
        container.add(IPLabel);

        IPText = new JTextField();
        IPText.setBounds(24, 8, 112, 21);
        IPText.setColumns(10);
        container.add(IPText);
        /*-----------------------port--------------------------*/
        JLabel PortLabel = new JLabel("Port");
        PortLabel.setBounds(138, 10, 28, 15);
        container.add(PortLabel);

        PortText = new JTextField();
        PortText.setColumns(10);
        PortText.setBounds(168, 8, 96, 21);
        container.add(PortText);
        /*------------------connect button---------------------*/
        JButton connect = new JButton("連線");
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*單程
                try {
                    tcpClient = new CLIENT(IPText.getText(), PortText.getText());
                    //成功連線訊息
                    String welcomeString = tcpClient.receive();
                    content.setText(content.getText()+welcomeString+"\n");
                    //等到傳訊息才能再次更改ip
                    IPText.setEditable(false);
                    PortText.setEditable(false);
                    type.setEditable(false);
                    connect.setEnabled(false);
                }catch (Exception exception){
                    content.setText(content.getText()+"Server連線失敗！"+exception.getMessage()+"\n");
                }*/

                try {

                    sc_msg = new Socket(IPText.getText(),Integer.parseInt(PortText.getText()));
                    sc_img = new Socket(IPText.getText(), 5501);

                    new rcvImg();
                    new rcvMsg();

                    IPText.setEditable(false);
                    PortText.setEditable(false);
                    type.setEditable(true);
                    connect.setEnabled(false);
                }catch (Exception exception){
                    content.setText(content.getText()+"server connect failed！\n"+exception.getMessage()+"\n");
                }
            }
        });
        connect.setBounds(266, 7, 64, 21);
        container.add(connect);
        max_y = 29;
        /*--------------------message box----------------------*/
        content = new JTextPane();
        content.setBounds(10, max_y+3, 325, max_y+250);
        content.setEditable(false);
        JScrollPane in_content = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        in_content.setBounds(10, max_y+3, 325, max_y+250);
        container.add(in_content);
        max_y += 253;
        /*--------------------operations-----------------------*/
         selectImage = new JButton("圖片");
        selectImage.setBounds(10, max_y+30, 60, 23);
        selectImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //開啟檔案選擇器對話方塊
                int status = jFileChooser.showOpenDialog(selectImage);
                if (status == JFileChooser.APPROVE_OPTION) {
                    try { //被選中的檔案儲存為檔案物件
                        File file = jFileChooser.getSelectedFile();
                        if(file!=null) {
                            img_path = file.getCanonicalPath();
                            ImageIcon imageIcon = new ImageIcon ( file.getCanonicalPath() );
                            Image image = imageIcon.getImage();
                            int image_width = imageIcon.getIconWidth();
                            int image_height = imageIcon.getIconHeight();
                            while(image_width > 300) {
                                image_width = image_width*9/10;
                                image_height = image_height*9/10;
                            }
                            Image resized_image = image.getScaledInstance(image_width,image_height,Image.SCALE_SMOOTH);
                            type.insertIcon(new ImageIcon(resized_image));
                        }
                    } catch (FileNotFoundException e1) {
                        System.out.println("系統沒有找到此檔案");
                        e1.printStackTrace();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                img_selected = true;
            }
        });
        container.add(selectImage);

        JButton send = new JButton("傳送");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                UpToWindow(type.getStyledDocument(), content.getStyledDocument());
                new sendMsg(type.getText());
                type.setText("");

            }
        });
        send.setBounds(70, max_y+30, 60, 23);
        container.add(send);

        JButton Exit = new JButton("結束");
        Exit.setBounds(275, max_y+30, 60, 23);
        container.add(Exit);
        max_y+=56;
        /*----------------------type box-----------------------*/
        type = new JTextPane();
        type.setBounds(10, max_y, 325, 121);
        container.add(type);
        /*-----------------------final-------------------------*/
        super.setContentPane(container);
        super.setSize(360,506);
    }
    static public void autoFilled() {
        PortText.setText("5500");
        IPText.setText("localhost");
    }
    static public class rcvImg extends Thread{
        public rcvImg(){ new Thread(this).start();}

        public void run(){
            while (true) {
                try {
                    BufferedInputStream bufferStream;
                    InputStream ins = sc_img.getInputStream();
                    bufferStream = new BufferedInputStream(ins);
                    byte[] buffer = new byte[1024];
                    ByteArrayOutputStream OutputBuffer = new ByteArrayOutputStream();
                    int InputBuffer_length;
                    while ((InputBuffer_length = bufferStream.read(buffer)) > 0) {OutputBuffer.write(buffer, 0, InputBuffer_length);}
                    OutputStream out = new FileOutputStream("CacheImage-" + image_num +".jpg");
                    OutputBuffer.writeTo(out);
                    out.close();
                    sc_img.shutdownInput();
                    // in.close();
                    content.insertIcon(new ImageIcon(".CacheImage-" + image_num +".jpg"));
                    image_num++;
                    content.setCaretPosition(content.getDocument().getLength());
                } catch (Exception e) {
                    //content.setText(content.getText()+"Image Receive Error:"+e);
                }
            }
        }
    }
    static public class rcvMsg extends Thread{
        public rcvMsg(){ new Thread(this).start();}

        public void run(){
            while (true){
                try{
                    String str = "";
                    DataInputStream input;
                    input = new DataInputStream(sc_msg.getInputStream());
                    str = input.readUTF();
                    if (!str.equals("")) {
                        content.setText(content.getText()+"\nServer: "+str);
                    }
                }catch (Exception e){

                }
            }


        }
    }
    static public class sendMsg extends Thread{

        private String str;

        public sendMsg(String msg)
        {
            str = msg;
            new Thread(this).start();
        }
        public void run(){
            if(img_selected){
                img_selected = false;
                try {
                    type.setText("");
                    if (!type.getText().equals("")) {
                        output = new DataOutputStream(sc_msg.getOutputStream());
                        output.writeUTF(str);
                        content.getDocument().insertString(content.getDocument().getLength(), "Client IP: ",null);
                        content.insertIcon(new ImageIcon(img_path));
                        content.setCaretPosition(content.getDocument().getLength());
                    }
                } catch (Exception e) {
                    System.out.println("訊息傳送失敗");
                }
            }
            else {
                if(!str.equals("")){
                    try {
                        output = new DataOutputStream(sc_msg.getOutputStream());
                        output.writeUTF(str);
                    } catch (IOException e) {
                       System.out.println("訊息傳送失敗"+e);
                    }
                }
            }

        }
    }

}
