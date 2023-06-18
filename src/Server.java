import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {

    ServerSocket serverSocket;
    Socket socket;

    BufferedReader bufferedReader;
    PrintWriter printWriter;
    public Server() {
        try{
            serverSocket = new ServerSocket(7777);
            System.out.println("Server ready to accept connection");
            System.out.println("Server waiting...");
            socket = serverSocket.accept();
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
                while(true){
                    try{
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        printWriter.println(br.readLine());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }

        };
        new Thread(r1).start();
    }

    private void startReading() {
        Runnable r2 = ()->{
                System.out.println("Reader started...");
                while(true){
                    try {
                        String msg = bufferedReader.readLine();
                        if (msg.equals("exit")) {
                            System.out.println("client ended the chat!!");
                            break;
                        }
                        System.out.println("Client:" + msg);
                    }
                    catch (Exception e){
                            e.printStackTrace();
                        }
                }

        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Starting Server..");
        new Server();
    }
}
