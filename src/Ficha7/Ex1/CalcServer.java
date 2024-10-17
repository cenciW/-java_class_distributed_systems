package src.Ficha7.Ex1;

//import src.Ficha5.Ex1.ServerThread;
import src.utils.InputValidation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;


public class CalcServer {
    private final static int N_THREADS = 10;
    private final static int SOCKET_TIMEOUT = 50000;
    private final static Phaser phaser = new Phaser();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //validateIntBetween is inclusive one, so the 6000 and the 6002 are included
        int portNumber = InputValidation.validateIntBetween(sc, "Digite a porta que o servidor vai escutar (entre 6000 e 6002): ", 6000, 6002);

        try(
                ServerSocket serverSocket = new ServerSocket(portNumber);
                ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
                )
        {
            System.out.println("O servidor está ativo na porta: " + portNumber + "\n");

            //loop
            while(true){
                System.out.println("À espera de clientes...");
                try
                        {
                        Socket socket = serverSocket.accept();
                        serverSocket.setSoTimeout(SOCKET_TIMEOUT);
                        //criar thread
                        System.out.println("Cliente: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " está conectado.");
                        executor.execute(
                                new CalcClientThread(socket)
                        );
                } catch (IOException e){
                    System.err.println("Erro ao criar socket ou ao criar a Thread do cliente: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Erro de I/O ao criar o ServerSocket: " + e.getMessage());
            System.exit(1);
        }

    }
}
