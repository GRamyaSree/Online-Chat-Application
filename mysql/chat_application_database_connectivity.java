package chatting_application;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class TwoPersonChatApp8822 {

    private JFrame frame1;
    private JFrame frame2;
    private JTextArea chatArea1;
    private JTextArea chatArea2;
    private JTextField inputField1;
    private JTextField inputField2;
    private Connection connection;

    public TwoPersonChatApp8822() {
        try {
            // Update the connection URL to include SSL properties
            String url = "jdbc:mysql://localhost:3306/chat_app_db?useSSL=true&verifyServerCertificate=false";
            connection = DriverManager.getConnection(url, "root", "raya666");
            createTable();
            loadPreviousChats();
        } catch (Exception e) {
            e.printStackTrace();
        }

        createUI();
    }

    private void createUI() {
        // Frame for user 1
        frame1 = new JFrame("Server Chat");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setSize(400, 300);
        frame1.setLayout(new BorderLayout());

        // Set icon for user 1 frame
        ImageIcon user1Icon = new ImageIcon("C:/Users/ADMIN/OneDrive/Pictures/Documents/pawankalyan.jpg");
        frame1.setIconImage(user1Icon.getImage());

        // Chat area for user 1
        chatArea1 = new JTextArea();
        chatArea1.setEditable(false);
        chatArea1.setForeground(Color.MAGENTA);
        chatArea1.setBackground(Color.WHITE);
        frame1.add(new JScrollPane(chatArea1), BorderLayout.CENTER);

        // Input panel for user 1
        JPanel inputPanel1 = new JPanel(new BorderLayout());
        inputField1 = new JTextField();
        inputField1.setForeground(Color.BLACK);
        inputField1.setBackground(Color.YELLOW);
        inputPanel1.add(inputField1, BorderLayout.CENTER);

        // Send button for user 1
        JButton sendButton1 = new JButton("Send");
        sendButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("SERVER", inputField1.getText(), chatArea1, chatArea2);
                inputField1.setText("");
            }
        });
        inputPanel1.add(sendButton1, BorderLayout.EAST);

        // Set font style and increase the font size for user 1
        Font boldFont1 = new Font(chatArea1.getFont().getFamily(), Font.BOLD, 20);
        chatArea1.setFont(boldFont1);

        frame1.add(inputPanel1, BorderLayout.SOUTH);

        // Image for user 1
        ImageIcon user1Image = new ImageIcon("C:/Users/ADMIN/Downloads/It’s hard to im f5a4a778-4d49-49c8-a010-d2244fcc910a.png");
        JLabel user1ImageLabel = new JLabel(user1Image);
        frame1.add(user1ImageLabel, BorderLayout.WEST);

        // ... (remaining code for user 1)

        // Frame for user 2
        frame2 = new JFrame("Client Chat");
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(400, 300);
        frame2.setLayout(new BorderLayout());

        // Set icon for user 2 frame
        ImageIcon user2Icon = new ImageIcon("C:/Users/ADMIN/OneDrive/Pictures/Documents/alluarjun.jpg");
        frame2.setIconImage(user2Icon.getImage());

        // Chat area for user 2
        chatArea2 = new JTextArea();
        chatArea2.setEditable(false);
        chatArea2.setForeground(Color.WHITE);
        chatArea2.setBackground(Color.BLACK);
        frame2.add(new JScrollPane(chatArea2), BorderLayout.CENTER);

        // Input panel for user 2
        JPanel inputPanel2 = new JPanel(new BorderLayout());
        inputField2 = new JTextField();
        inputField2.setForeground(Color.BLACK);
        inputField2.setBackground(Color.WHITE);
        inputPanel2.add(inputField2, BorderLayout.CENTER);

        // Send button for user 2
        JButton sendButton2 = new JButton("Send");
        sendButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("CLIENT", inputField2.getText(), chatArea2, chatArea1);
                inputField2.setText("");
            }
        });
        inputPanel2.add(sendButton2, BorderLayout.EAST);

        // Set font style and increase the font size for user 2
        Font boldFont2 = new Font(chatArea2.getFont().getFamily(), Font.BOLD, 20);
        chatArea2.setFont(boldFont2);

        frame2.add(inputPanel2, BorderLayout.SOUTH);

        // Image for user 2
        ImageIcon user2Image = new ImageIcon("C:/Users/ADMIN/Downloads/Blue and white 1a4ce5f5-27c6-410d-a8da-7eac0480ec5d.png");
        JLabel user2ImageLabel = new JLabel(user2Image);
        frame2.add(user2ImageLabel, BorderLayout.EAST);

        // ... (remaining code for user 2)

        // Display the frames
        frame1.setVisible(true);
        frame2.setVisible(true);
    }

    private void sendMessage(String sender, String message, JTextArea senderChatArea, JTextArea receiverChatArea) {
        senderChatArea.append(message + "\n");
        
        // Determine the correct receiver's chat area based on the sender
        JTextArea actualReceiverChatArea = (sender.equals("SERVER")) ? chatArea2 : chatArea1;
        actualReceiverChatArea.append(message + "\n");
        
        saveMessageToDatabase(sender, (sender.equals("SERVER")) ? "CLIENT" : "SERVER", message);
    }


    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS chats (\n"
                + "    id INT AUTO_INCREMENT PRIMARY KEY,\n"
                + "    sender VARCHAR(255),\n"
                + "    receiver VARCHAR(255),\n"
                + "    message TEXT,\n"
                + "    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n"
                + ")";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.execute();
            System.out.println("Table 'chats' created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }
    private void loadPreviousChats() {
        String sql = "SELECT sender, receiver, message FROM chats ORDER BY timestamp ASC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String sender = rs.getString("sender");
                String receiver = rs.getString("receiver");
                String message = rs.getString("message");

                if ("SERVER".equals(sender)) {
                    if (chatArea1 != null) {
                        chatArea1.append(message + "\n");
                    } else {
                        System.err.println("Error: chatArea1 is null");
                    }
                } else {
                    if (chatArea2 != null) {
                        chatArea2.append(message + "\n");
                    } else {
                        System.err.println("Error: chatArea2 is null");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveMessageToDatabase(String sender, String receiver, String message) {
        String sql = "INSERT INTO chats (sender, receiver, message) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, sender);
            pstmt.setString(2, receiver);
            pstmt.setString(3, message);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->new TwoPersonChatApp8822());
    }
}
