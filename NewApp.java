import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class NewApp extends JFrame implements Runnable,ActionListener {
    JTextField textField;
    JTextArea textArea;
    JButton send;


    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    Thread chat;
    NewApp(){
        textField = new JTextField();
        textField.setBounds(50,50,100,30);

        textArea = new JTextArea();
        textArea.setBounds(200,50,180,120);

        send = new JButton("Send");
        send.setBounds(200,230,70,40);
        send.addActionListener(this);

        try {

            socket = new Socket("localhost",12000);

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {

        }

        add(textField);
        add(textArea);
        add(send);

        chat = new Thread(this);
        chat.setDaemon(true);
        chat.start();


        setTitle("New App");
        setSize(500,500);
        setLayout(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new NewApp();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = textField.getText();
        textArea.append("NewApp:"+msg+"\n");
        textField.setText("");

        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        } catch (IOException ex) {

        }

    }

    @Override
    public void run() {
        while(true){
            try {
                String msg = dataInputStream.readUTF();
                textArea.append("ChatApp:"+msg+"\n ");
            } catch (IOException e) {

            }
        }
    }
}