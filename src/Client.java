import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    public Client() {
        try{
            System.out.println("Sending request to Server...");
            socket = new Socket("127.0.0.1",7778);
            System.out.println("Connection created");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
                        socket.close();
                        break;
                    }
                    System.out.println("Server:" + msg);

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
