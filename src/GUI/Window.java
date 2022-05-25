package GUI;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class Window extends JFrame{
    public boolean connected = false;
    private final int ID;
    public JTextField IPText;
    public JTextField PortText;
    public JTextPane content;
    public JTextPane type;
    public JButton connect;
    public JButton send;
    public String img_path = "";
    public JLabel Path;
    public Window(String Title, int ID) {
        this.ID = ID;
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
        connect = new JButton("連線");
        connect.setBounds(266, 7, 64, 21);
        container.add(connect);
        max_y = 29;
        /*--------------------message box----------------------*/
        content = new JTextPane();
        content.setBounds(10, max_y+3, 325, max_y+250);
        content.setEditable(false);
        JScrollPane in_content = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
                Path.setText(chooser.getSelectedFile().getAbsolutePath());
                img_path = chooser.getSelectedFile().getName();
            }
        });
        container.add(selectImage);

        send = new JButton("傳送");
        send.setBounds(70, max_y+30, 60, 23);
        container.add(send);

        Path = new JLabel("");
        Path.setBounds(133,max_y+30,80,23);
        container.add(Path);

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
    public void display(String user, String message) throws BadLocationException {
        content.insertComponent(new JLabel(user+": "+message));
        this.revalidate();
    }
    public void display(String user, ImageIcon imageIcon) {
        content.insertComponent(new JLabel(user+":"));
        content.insertIcon(imageIcon);
        content.insertComponent(new JLabel("\n"));
        this.revalidate();
//        content.insertComponent(new JLabel(imageIcon));
    }
}
