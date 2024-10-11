package src.Ficha5.Ex1;


import src.utils.InputValidation;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//block all the threads to wait all players come
public class Server {
    //defines

    //max number of threads simult
    private final static int N_THREADS = 10;
    private final static int SOCKET_TIMEOUT = 10000;


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
            System.out.println("Cliente ligando.");

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
                            new ServerThread(clientSocket)
                    );

                //use timeout to leave the cycle
                }catch (SocketTimeoutException ste){
//                    throw new SocketTimeoutException(ste.getMessage());
                    System.out.println("Main: Acabou o tempo para aceitar novos jogares: ");
                    executor.shutdown();
                    break;
                }

                Random rand= new Random();
            }
        }catch (Exception e){
            System.err.println("MAIN: Ocorreu um erro de I/O ao tentar criar o Socket na porta: "+ portNumber +". Erro: " + e.getMessage());
            System.exit(2);
        }
    }
}
