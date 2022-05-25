package GUI;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.Objects;

public class Window extends JFrame{
    public boolean linked = false;
    public String info="";
    public JTextField IPText;
    public JTextField PortText;
    public JTextPane content;
    public JTextPane type;
    public JButton linkButton;
    public JButton send;
    public String imagePathCache = "";
    public JLabel ImagePathText;

    public Window(String Title) {
        super.setLocation(300,200);
        super.setVisible(true);
        super.setResizable(false);
        super.setTitle(Title);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setUI();
    }
    public void defaultDestination() {
        IPText.setText("localhost");
        PortText.setText("5501");
    }
    public void setUI() {
        int max_y;
        /*----------------------frame--------------------------*/
        Container container = super.getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(110, 110, 110));
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
        linkButton = new JButton("連線");
        linkButton.setBounds(266, 7, 64, 21);
        container.add(linkButton);
        max_y = 29;
        /*--------------------message box----------------------*/
        content = new JTextPane();
        content.setContentType("text/html");
        content.setBackground(new Color(83, 203, 194));
        content.setBounds(10, max_y+3, 325, max_y+250);
        content.setEditable(false);
        JScrollPane in_content = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        in_content.setBounds(10, max_y+3, 325, max_y+250);
        container.add(in_content);
        max_y += 253;
        /*--------------------operations-----------------------*/
        JButton selectImage = new JButton("圖片");
        selectImage.setBounds(10, max_y+30, 60, 23);
        selectImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            int c = chooser.showOpenDialog(selectImage);
            if (c == JFileChooser.APPROVE_OPTION) {
                ImagePathText.setText(chooser.getSelectedFile().getAbsolutePath());
                imagePathCache = chooser.getSelectedFile().getName();
            }
        });
        container.add(selectImage);

        send = new JButton("傳送");
        send.setBounds(70, max_y+30, 60, 23);
        container.add(send);

        ImagePathText = new JLabel("");
        ImagePathText.setBounds(133,max_y+30,80,23);
        container.add(ImagePathText);

        JButton Exit = new JButton("結束");
        Exit.setBounds(275, max_y+30, 60, 23);
        container.add(Exit);
        max_y+=56;
        /*----------------------type box-----------------------*/
        type = new JTextPane();
        type.setBounds(10, max_y, 325, 121);
        type.setBackground(new Color(83, 203, 194));
        container.add(type);
        /*-----------------------final-------------------------*/
        super.setContentPane(container);
        super.setSize(360,506);
    }
    public void display(String user, String message) throws BadLocationException {
        info+=user+":"+message.replaceAll("\n","<br>");
        content.setText(info);
        this.revalidate();
    }
    public void display(String user, ImageIcon imageIcon,String img_path) {
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();
        while (width>content.getWidth()) {
            width = width*4/5;
            height = height*4/5;
        }
        Image image = imageIcon.getImage();
        image.getScaledInstance(width,height,Image.SCALE_SMOOTH);
        imageIcon.setImage(image);
        if(user.startsWith("Client")) {
            info+="<p>"+user+":</p>";
            if(Objects.equals(img_path, "")) {
                info+="<p>no path</p>";
            }
            else {
                info+="<img src='file:\\"+img_path+"' alt='"+img_path+"' width='"+width+"' height='"+height+"'><br>";
            }

        }
        else if(user.startsWith("Server")){
            info+="<p>"+user+":</p>";
            if(Objects.equals(img_path, "")) {
                info+="<p>no path</p>";
            }
            else {
                info+="<img src='file:"+img_path+"' alt='"+img_path+"' width='"+width+"' height='"+height+"'><br>";
            }

        }
        content.setText(info);
        this.revalidate();
    }
}
