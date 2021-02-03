package chattingapplication;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Client implements ActionListener {
    
    JPanel header;
    JTextField message;
    JButton send;
    static JPanel screen;
    static JFrame frame = new JFrame();
    
    static Box vertical = Box.createVerticalBox();
    
    static Socket s;
    
    static DataInputStream din;
    static DataOutputStream dout;
    
    Boolean typing;
    
    Client() {
        
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        header = new JPanel();
        header.setLayout(null);
        header.setBackground(new Color(7, 94, 84));
        header.setBounds(0, 0, 450, 70);
        frame.add(header);
        
        ImageIcon arrow1 = new ImageIcon(ClassLoader.getSystemResource("chattingapplication/icons/arrow.png"));
        Image arrow2 = arrow1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon arrow3 = new ImageIcon(arrow2);
        JLabel backArrow = new JLabel(arrow3);
        backArrow.setBounds(5, 17, 30, 30); 
        header.add(backArrow);
        
        backArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });
        
        ImageIcon steve1 = new ImageIcon(ClassLoader.getSystemResource("chattingapplication/icons/steve.jpg"));
        Image steve2 = steve1.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        ImageIcon steve3 = new ImageIcon(steve2);
        JLabel receiverImage = new JLabel(steve3);
        receiverImage.setBounds(40, 5, 60, 60); 
        header.add(receiverImage);
        
        JLabel receiverName = new JLabel("Steve");
        receiverName.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        receiverName.setForeground(Color.WHITE);
        receiverName.setBounds(110, 15, 100, 20);
        header.add(receiverName);
        
        JLabel receiverStatus = new JLabel("Active now");
        receiverStatus.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        receiverStatus.setForeground(Color.WHITE);
        receiverStatus.setBounds(110, 35, 100, 20);
        header.add(receiverStatus);
        
        Timer t = new Timer(1, (ActionEvent ae) -> {
            if (typing) {
                receiverStatus.setText("Active now");
            }
        });
        
        t.setInitialDelay(2000);
        
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
        
        screen = new JPanel();
        screen.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        //screen.setBounds(5, 75, 440, 570);
        //frame.add(screen);

        JScrollPane scroll = new JScrollPane(screen);
        scroll.setBounds(5, 75, 440, 570);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        ScrollBarUI ui = new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int i) {
                JButton button = super.createDecreaseButton(i);
                button.setBackground(new Color(7, 94, 84));
                button.setForeground(Color.WHITE);
                this.thumbColor = new Color(7, 94, 84);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int i) {
                JButton button = super.createIncreaseButton(i);
                button.setBackground(new Color(7, 94, 84));
                button.setForeground(Color.WHITE);
                this.thumbColor = new Color(7, 94, 84);
                return button;
            }
        };
        
        scroll.getVerticalScrollBar().setUI(ui);
        frame.add(scroll);
        
        message = new JTextField();
        message.setFont(new Font("SAN_SERIF", Font.PLAIN, 18));
        message.setBounds(5, 650, 320, 45);
        frame.add(message);
        
        message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                receiverStatus.setText("typing...");
                t.stop();
                typing = true;
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                typing = false;
                if (!t.isRunning()) {
                    t.start();
                }
            }
        });
        
        
        send = new JButton("Send");
        send.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        send.setForeground(Color.WHITE);
        send.setBounds(330, 650, 113, 45);
        send.setBackground(new Color(7, 94, 84));
        send.addActionListener(this);
        frame.add(send);
        
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(null);
        frame.setSize(450, 700);
        frame.setLocation(1100, 200);
        frame.setUndecorated(true);
        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String out = message.getText();
            sendTextToFile(out);
            JPanel chat = formatLabel(out);
            
            screen.setLayout(new BorderLayout());
            
            JPanel right = new JPanel(new BorderLayout());
            right.add(chat, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            
            screen.add(vertical, BorderLayout.PAGE_START);
            
            //screen.add(chat);
            dout.writeUTF(out);
            message.setText("");
        } catch (Exception e) {
            System.out.println("Exception in actionPerformed method (Client) : "+e);
            e.printStackTrace();
        }
    }
    
    public void sendTextToFile(String message) throws FileNotFoundException {
        try(FileWriter file = new FileWriter("chat.txt", true);
                PrintWriter print = new PrintWriter(new BufferedWriter(file));) {
            print.println("Steve: "+message);
        } catch (Exception e) {
            System.out.println("Exception in sendTextToFile method (Client) : "+e);
            e.printStackTrace();
        }
    }
    
    public static JPanel formatLabel(String out) {
        JPanel chat = new JPanel();
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        
        JLabel text = new JLabel("<html><p style = \"width : 150px\">"+out+"</p></html>");
        text.setFont(new Font("Tahoma", Font.PLAIN, 16));
        text.setBackground(new Color(37, 211, 102));
        text.setOpaque(true);
        text.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        
        chat.add(text);
        chat.add(time);
        return chat;
    }
    
    public static void main (String[] args) {
        new Client().frame.setVisible(true);
        
        String messageInput = "";
        
        try {
            s = new Socket("127.0.0.1", 6000);
            
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            
            while(true) {
                screen.setLayout(new BorderLayout());
                    messageInput = din.readUTF();
                    JPanel chat = formatLabel(messageInput);
                    
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(chat, BorderLayout.LINE_START);
                    vertical.add(left);
                    vertical.add(Box.createVerticalStrut(15));
                    screen.add(vertical, BorderLayout.PAGE_START);
                    frame.validate();
                }
            
        } catch (Exception e) {
            System.out.println("Exception in main method (Client) : "+e);
            e.printStackTrace();
        }
    }

}