package src.Ficha5.Ex1;


import src.utils.InputValidation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

//block all the threads to wait all players come
public class Server {
    //defines

    //max number of threads simult
    private final static int N_THREADS = 10;
    private final static int SOCKET_TIMEOUT = 10000;
    private final static Phaser phaser = new Phaser();
    public static volatile int EXTRACTED_NUMBER;
    private final static int MAX_GAME_TIME = 100;
    public static volatile boolean is_game_ended = false;


    //'psvm' makes the main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //server input: portNumber -> server will listen on this port
        int portNumber = InputValidation.validateIntBetween(sc,
                "Introduza o número da porta que o servidor irá escutar (entre 1024 e 65535): ",
                1024, 65535);


        //closing the scanner
        sc.close();

        try(
                ServerSocket serverSocket = new ServerSocket(portNumber);
                ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        ) {
            //wait SOCKET_TIMEOUT to clients connect on server
            serverSocket.setSoTimeout(SOCKET_TIMEOUT);

            while (true){
                System.out.println("Main: À espera de novas ligações.");
                try{
                    Socket clientSocket = serverSocket.accept();
                    //accepting new connects
                    System.out.println("Main: Nova ligação.");
                    //execute the client threads
                    executor.execute(
                            new ServerThread(clientSocket, phaser)
                    );

                //use timeout to leave the cycle
                }catch (SocketTimeoutException ste){
//                    throw new SocketTimeoutException(ste.getMessage());
                    System.out.println("Main: Acabou o tempo para aceitar novos jogares: ");
                    executor.shutdownNow();
                    break;
                }

            }

            if (!executor.awaitTermination(MAX_GAME_TIME, TimeUnit.SECONDS)){
                is_game_ended = true;
            }

        } catch (InterruptedException e) {
            System.out.println("Main: ocorreu um erro em awaitTermination");
            System.exit(2);
        } catch (IOException e){
            System.err.println("MAIN: Ocorreu um erro de I/O ao tentar criar o Socket na porta: "+ portNumber +". Erro: " + e.getMessage());
            System.exit(3);
        }
    }
}
