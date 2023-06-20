import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame{
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    //Declare components for textGUI
    private JLabel header = new JLabel("Client Space");
    private JTextArea messageArea = new JTextArea();
    private JTextArea messageInput = new JTextArea();
    private Font font = new Font("Times New Roman",Font.PLAIN,20)

            ;
    public Client() {
        try{
            System.out.println("Sending request to Server...");
            socket = new Socket("127.0.0.1",7778);
            System.out.println("Connection created");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startWriting();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println(e.getKeyCode());

                if(e.getKeyCode() == 10){
                    String msg = messageInput.getText();
                    printWriter.println(msg);
                    printWriter.flush();
                    messageArea.append("Me:"+msg+"\n");
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }

    private void createGUI() {
        //Creation of GUI
        this.setTitle("Client Messenger[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //components font
        header.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        //set frame layout
        this.setLayout(new BorderLayout());
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        messageInput.setBorder(BorderFactory.createEtchedBorder());
        messageArea.setEditable(false);


        //adding components to frame
        this.add(header,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
    }

    private void startWriting() {
        Runnable r1 = ()->{

            System.out.println("Writer started...");
            try
            {
                while(!socket.isClosed()){
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String msg = br.readLine();
                    printWriter.println(msg);
                    printWriter.flush();
                    if(msg.equals("exit")){
                        socket.close();
                        break;
                    }

                }

            }
            catch (Exception e){
                System.out.println("Connection is Closed now");
            }

        };
        Thread t1 = new Thread(r1);
        t1.start();

    }

    private void startReading() {
        Runnable r2 = ()->{
            System.out.println("Reader started...");
            try {
                while (!socket.isClosed()) {

                    String msg = bufferedReader.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("server ended the chat!!");
                        JOptionPane.showMessageDialog(this,"server ended the chat!!");
                        socket.close();
                        messageInput.setEnabled(false);
                        break;
                    }
                    messageArea.append("Server:" + msg + "\n");
                    //System.out.println("Server:" + msg);

                }
            }
            catch (Exception e){
                System.out.println("Connection is Closed now");
            }

        };
        Thread t2 = new Thread(r2);
        t2.start();

    }

    public static void main(String[] args) {
        System.out.println("Starting client..");
        new Client();
    }
}
