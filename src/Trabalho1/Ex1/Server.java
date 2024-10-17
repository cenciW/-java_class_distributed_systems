package src.Trabalho1.Ex1;


import src.Trabalho1.utils.FileHandler;
import src.utils.InputValidation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
    private final static int SOCKET_TIMEOUT = 50000;
    private final static Phaser phaser = new Phaser();
    public static volatile int EXTRACTED_NUMBER;
    //configuring timeout
    private final static int MAX_GAME_TIME = 50000;
    public static volatile boolean is_game_ended = false;
    public static String WORD_CHOOSE;
    public static String WINNER = "";
    public static volatile FileHandler fh = new FileHandler();

    public static void resetLoginAttempts() {
        final String filePath = "src/Trabalho1/users.txt";
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    // Zera as tentativas de login
                    String updatedLine = parts[0] + ";" + parts[1] + ";0\n";
                    updatedContent.append(updatedLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gravar o conteúdo atualizado de volta no arquivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(updatedContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //'psvm' makes the main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

//        //server input: portNumber -> server will listen on this port
        int portNumber = InputValidation.validateIntBetween(sc,
                "Introduza o número da porta que o servidor irá escutar (entre 1024 e 65535): ",
                1024, 65535);

        System.out.print("Digite a palavra para o jogo: ");
        WORD_CHOOSE = sc.nextLine().strip();


        //closing the scanner
        sc.close();

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        ) {
            //wait SOCKET_TIMEOUT to clients connect on server
            serverSocket.setSoTimeout(SOCKET_TIMEOUT);
            resetLoginAttempts();

            while (true) {
                System.out.println("Main: À espera de novas ligações.");
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Main: Nova ligação de: " + clientSocket.getRemoteSocketAddress());

                    // Executa a nova thread para o cliente conectado
                    executor.execute(new ServerThread(clientSocket, phaser));

                } catch (SocketTimeoutException ste) {
                    System.out.println("Main: Acabou o tempo para aceitar novos jogares.");
                    executor.shutdownNow();
                    break;
                }
            }

            if (!executor.awaitTermination(MAX_GAME_TIME, TimeUnit.SECONDS)) {
                is_game_ended = true;
                System.out.println("Main: O tempo de jogo terminou.");
            }

        } catch (InterruptedException e) {
            System.out.println("Main: ocorreu um erro em awaitTermination");
            System.exit(2);
        } catch (IOException e) {
            System.err.println("MAIN: Ocorreu um erro de I/O ao tentar criar o Socket na porta: " + portNumber + ". Erro: " + e.getMessage());
            System.exit(3);
        }
    }
}


