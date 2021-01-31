package chattingapplication;

import static chattingapplication.Client.screen;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame implements ActionListener {
    
    JPanel header;
    JTextField message;
    JButton send;
    static JTextArea screen;
    
    static ServerSocket skt;
    static Socket s;
    
    static DataInputStream din;
    static DataOutputStream dout;
    
    Server() {
        
        header = new JPanel();
        header.setLayout(null);
        header.setBackground(new Color(7, 94, 84));
        header.setBounds(0, 0, 450, 70);
        add(header);
        
        ImageIcon arrow1 = new ImageIcon(ClassLoader.getSystemResource("chattingapplication/icons/arrow.png"));
        Image arrow2 = arrow1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon arrow3 = new ImageIcon(arrow2);
        JLabel backArrow = new JLabel(arrow3);
        backArrow.setBounds(5, 17, 30, 30); 
        header.add(backArrow);
        
        backArrow.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });
        
        ImageIcon dustin1 = new ImageIcon(ClassLoader.getSystemResource("chattingapplication/icons/dustin.jpg"));
        Image dustin2 = dustin1.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        ImageIcon dustin3 = new ImageIcon(dustin2);
        JLabel senderImage = new JLabel(dustin3);
        senderImage.setBounds(40, 5, 60, 60); 
        header.add(senderImage);
        
        JLabel senderName = new JLabel("Dustin");
        senderName.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        senderName.setForeground(Color.WHITE);
        senderName.setBounds(110, 15, 100, 20);
        header.add(senderName);
        
        JLabel senderStatus = new JLabel("Active now");
        senderStatus.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        senderStatus.setForeground(Color.WHITE);
        senderStatus.setBounds(110, 35, 100, 20);
        header.add(senderStatus);
        
        ImageIcon video1 = new ImageIcon(ClassLoader.getSystemResource("chattingapplication/icons/video.png"));
        Image video2 = video1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon video3 = new ImageIcon(video2);
        JLabel videoIcon = new JLabel(video3);
        videoIcon.setBounds(310, 20, 30, 30); 
        header.add(videoIcon);
        
        ImageIcon phone1 = new ImageIcon(ClassLoader.getSystemResource("chattingapplication/icons/phone.png"));
        Image phone2 = phone1.getImage().getScaledInstance(30, 35, Image.SCALE_DEFAULT);
        ImageIcon phone3 = new ImageIcon(phone2);
        JLabel phoneIcon = new JLabel(phone3);
        phoneIcon.setBounds(360, 18, 30, 35); 
        header.add(phoneIcon);
        
        ImageIcon dots1 = new ImageIcon(ClassLoader.getSystemResource("chattingapplication/icons/dots.png"));
        Image dots2 = dots1.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon dots3 = new ImageIcon(dots2);
        JLabel dotsIcon = new JLabel(dots3);
        dotsIcon.setBounds(410, 20, 10, 25); 
        header.add(dotsIcon);
        
        screen = new JTextArea();
        screen.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        screen.setEditable(false);
        screen.setLineWrap(true);
        screen.setWrapStyleWord(true);
        screen.setBounds(5, 75, 440, 570);
        add(screen);
        
        message = new JTextField();
        message.setFont(new Font("SAN_SERIF", Font.PLAIN, 18));
        message.setBounds(5, 650, 320, 45);
        add(message);
        
        send = new JButton("Send");
        send.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        send.setForeground(Color.WHITE);
        send.setBounds(330, 650, 113, 45);
        send.setBackground(new Color(7, 94, 84));
        send.addActionListener(this);
        add(send);
        
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        setSize(450, 700);
        setLocation(400, 200);
        setUndecorated(true);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try {
            String out = message.getText();
            screen.setText(screen.getText()+"\n\t\t\t\t\t"+out);
            dout.writeUTF(out);
            message.setText("");
        } catch(Exception e) {}
    }
    
    public static void main (String[] args) {
        new Server().setVisible(true);
        
        String inputMessage = "";
        
        try {
            skt = new ServerSocket(6000);
            s = skt.accept();
            
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            
            inputMessage = din.readUTF();
            screen.setText(screen.getText()+"\n"+inputMessage);
            
            skt.close();
            s.close();
            
        } catch (Exception e) {}
    }

}
