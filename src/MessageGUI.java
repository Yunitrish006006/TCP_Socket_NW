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

        Container container = super.getContentPane();
        container.setLayout(null);

        content = new JTextPane();
        content.setBounds(10, 69, 325, 242);
        content.setEditable(false);
        JScrollPane in_content = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        in_content.setBounds(10, 69, 325, 242);
        container.add(in_content);

        type = new JTextPane();
        type.setBounds(10, 354, 325, 101);
        container.add(type);

        JButton selectImage = new JButton("圖片");
        selectImage.setBounds(10, 321, 85, 23);
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
                 System.out.println("hi");
            }
        });
        send.setBounds(120, 321, 85, 23);
        container.add(send);

        JButton Exit = new JButton("結束");
        Exit.setBounds(229, 321, 85, 23);
        container.add(Exit);

        JLabel IPLabel = new JLabel("IP");
        IPLabel.setBounds(10, 32, 85, 15);
        container.add(IPLabel);

        JLabel PortLabel = new JLabel("Port");
        PortLabel.setBounds(181, 32, 85, 15);
        container.add(PortLabel);

        JTextField IPText = new JTextField();
        IPText.setBounds(62, 29, 112, 21);
        getContentPane().add(IPText);
        IPText.setColumns(10);

        JTextField PortText = new JTextField();
        PortText.setColumns(10);
        PortText.setBounds(239, 29, 96, 21);
        getContentPane().add(PortText);
        super.setContentPane(container);
        super.setSize(360,506);
    }
}
