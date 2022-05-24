import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MessageGUI extends JFrame{
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final JFileChooser jFileChooser = new JFileChooser(new File("."));
    public JFrame gui = this;
    public JTextPane type;
    public JTextPane content;
    public boolean is_send = false;
    public String img_path = "";





    public MessageGUI(String Title) {
        super.setLocation(0,0);
        super.setVisible(true);
        super.setResizable(false);
        super.setTitle(Title);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void myStyle(JTextPane jTextPane) throws BadLocationException {
        SimpleAttributeSet aSet = new SimpleAttributeSet();
        StyleConstants.setForeground(aSet, Color.DARK_GRAY);
        StyleConstants.setBackground(aSet, Color.RED);
        StyleConstants.setFontFamily(aSet, "lucida bright italic");
        StyleConstants.setFontSize(aSet, 20);
        StyleConstants.setAlignment(aSet, StyleConstants.ALIGN_LEFT);
        jTextPane.setCharacterAttributes(aSet,true);
    }
    public void theirStyle(JTextPane jTextPane) throws BadLocationException {
        SimpleAttributeSet aSet = new SimpleAttributeSet();
        StyleConstants.setForeground(aSet, Color.DARK_GRAY);
        StyleConstants.setBackground(aSet, Color.RED);
        StyleConstants.setFontFamily(aSet, "lucida bright italic");
        StyleConstants.setFontSize(aSet, 20);
        StyleConstants.setAlignment(aSet, StyleConstants.ALIGN_LEFT);
        jTextPane.setCharacterAttributes(aSet,true);
    }
    static void UpToWindow(Document source, Document dest) {
        try {
//            dest.remove(0, dest.getLength());

            ElementIterator iterator = new ElementIterator(source);
            Element element;
            while ((element = iterator.next()) != null) {
                if (element.isLeaf()) {
                    int start = element.getStartOffset();
                    int end = element.getEndOffset();
                    String text = source.getText(start, end - start);
                    dest.insertString(dest.getLength(), text, element.getAttributes());
                }
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
    public void setUpUI() throws IOException {
        int max_y = 0;
        /*----------------------frame--------------------------*/
        Container container = super.getContentPane();
        container.setLayout(null);
        /*--------------------------ip-------------------------*/
        JLabel IPLabel = new JLabel("IP");
        IPLabel.setBounds(10, 10, 14, 15);
        container.add(IPLabel);

        JTextField IPText = new JTextField();
        IPText.setBounds(24, 8, 112, 21);
        IPText.setColumns(10);
        container.add(IPText);
        /*-----------------------port--------------------------*/
        JLabel PortLabel = new JLabel("Port");
        PortLabel.setBounds(138, 10, 28, 15);
        container.add(PortLabel);

        JTextField PortText = new JTextField();
        PortText.setColumns(10);
        PortText.setBounds(168, 8, 96, 21);
        container.add(PortText);
        /*------------------connect button---------------------*/
        JButton connect = new JButton("連線");
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
        JButton selectImage = new JButton("圖片");
        selectImage.setBounds(10, max_y+30, 60, 23);
        selectImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //開啟檔案選擇器對話方塊
                int status = jFileChooser.showOpenDialog(selectImage);
                //沒有選開啟按鈕結果提示
                if (status != JFileChooser.APPROVE_OPTION) {
                    //#沒有選中檔案
                } else {
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
            }
        });
        container.add(selectImage);

        JButton send = new JButton("傳送");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                is_send = true;
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
}
