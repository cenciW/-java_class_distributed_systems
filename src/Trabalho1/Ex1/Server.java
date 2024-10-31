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

//bloquear as threads para esperar os jogadores chegarem
public class Server {
    //defines

    //número máximo de threads
    private final static int N_THREADS = 10;
    //
    //30 segundos para os jogadores se conectarem
    private final static int WAIT_PLAYERS_TIME = 30000;
    private final static Phaser phaser = new Phaser();
    //    public static volatile int EXTRACTED_NUMBER;
    //configurando timeout
    //1:30 de jogo
    private final static int MAX_GAME_TIME = 90;
    public static volatile boolean is_game_ended = false;
    public static volatile boolean is_game_started = false;
    public static String WORD_CHOOSE;
    public static String WINNER = "";
    public static volatile FileHandler fh = new FileHandler();

    //método para resetar as tentativas de login no .txt
    public static void resetLoginAttempts() {
        final String filePath = "src/Trabalho1/users.txt";
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            //enquanto tiver linha no arquivo continuar lendo.
            while ((line = br.readLine()) != null) {
                //separa por ; e coloca num vetor de strings
                //user1;senha123;attemps
                String[] parts = line.split(";");
                //se tivermos 3 strings no vetor de strings, significa que o usuário escreveu certo e podemos tentar atualizar o log
                if (parts.length == 3) {
                    // Zera as tentativas de login
                    String updatedLine = parts[0] + ";" + parts[1] + ";0\n";
                    updatedContent.append(updatedLine);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro de I/O: " + e.getMessage());
        }

        // Gravar o conteúdo atualizado de volta no arquivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(updatedContent.toString());
        } catch (IOException e) {
            System.err.println("Erro ao gravar no atualizações no arquivo de usuários: " + e.getMessage());
        }
    }

    //'psvm' main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("| Server: FORCA |");

        //Porta que o servidor vai rodar
        int portNumber = InputValidation.validateIntBetween(sc,
                "Introduza o número da porta que o servidor irá escutar (entre 1024 e 65535): ",
                1024, 65535);

        //atribuindo a palavra chave para começar o jogo
        do {
            System.out.print("Digite a palavra para o jogo, a palavra 'desisto' é reservada, não use como palavra chave para o jogo: ");
            WORD_CHOOSE = sc.nextLine().toLowerCase();

        } while (WORD_CHOOSE.equalsIgnoreCase("desisto") || WORD_CHOOSE.trim().isEmpty());

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                ExecutorService executor = Executors.newFixedThreadPool(N_THREADS)
        ) {
            //espera WAIT_PLAYERS_TIME para os clientes se conectarem ao server
            serverSocket.setSoTimeout(WAIT_PLAYERS_TIME);


            while (true) {
                System.out.println("Main: À espera de novos jogadores.");
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Main: Nova ligação de: " + clientSocket.getRemoteSocketAddress());

                    // Executa a nova thread para o cliente conectado
                    executor.execute(new ServerThread(clientSocket));

                } catch (SocketTimeoutException ste) {
                    System.err.println("Main: Acabou o tempo para aceitar novos jogares.");
                    executor.shutdownNow();
                    break;
                }
            }

            //resetando as tentativas de login
            resetLoginAttempts();

            //para controle de auth após o jogo já ter começado
            is_game_started = true;

            //verificar se já deu o tempo máximo do jogo
            if (!executor.awaitTermination(MAX_GAME_TIME, TimeUnit.SECONDS)) {
                is_game_ended = true;
                System.out.println("Main: O tempo de jogo terminou.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("Main: ocorreu um erro em awaitTermination");
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Main: Ocorreu um erro de I/O ao tentar criar o Socket na porta: " + portNumber + ". Erro: " + e.getMessage());
            System.exit(3);
        }
        //fechando scanner
        sc.close();
    }
}


