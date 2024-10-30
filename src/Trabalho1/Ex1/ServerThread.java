package src.Trabalho1.Ex1;

import src.Trabalho1.utils.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.Phaser;

import static src.Trabalho1.Ex1.Server.WORD_CHOOSE;
import static src.Trabalho1.Ex1.Server.is_game_ended;

public class ServerThread extends Thread {
    private final Socket clientSocket;
    private final Phaser phaser;
    User user;

    public ServerThread(Socket clientSocket, Phaser phaser) {
        this.clientSocket = clientSocket;
        this.phaser = phaser;
        phaser.register();
    }

    public void run() {
        System.out.println("Thread: " + this.getName() + " iniciada para o cliente: " + clientSocket.getRemoteSocketAddress());

        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            //while da auth
            boolean auth = false;
            while (!auth) {

                if(is_game_ended){
                    System.out.println("O usuário: " + clientSocket.getRemoteSocketAddress() + ". Não pode conectar-se. O jogo já foi finalizado");
                    out.println("FINALIZADO");
                    break;
                }

                //logica do login
                String login = in.readLine();
                String password = in.readLine();

                Optional<User> userData = Server.fh.login(new User(login, password));



                //se for null
                if (userData.isPresent()) {
                    //tenho usuário
                    auth = true;
                    System.out.println("Autenticado");
                    out.println("AUTENTICADO");
                    user = userData.get();
                } else {
                    out.println("ERRO");
                    System.out.println("Erro");
                }
            }

            String hiddenWord = ("-".repeat(WORD_CHOOSE.length()));

            StringBuilder sbHiddenWord = new StringBuilder(hiddenWord);

//            System.out.println(hiddenWord);

            while (true) {
                //server thread pro client a palavra camuf
                out.println(sbHiddenWord);


                //ouvir o palpite
                String guess = in.readLine();

                if (Server.is_game_ended) {
                    if (Server.WINNER.isEmpty()) {
                        //nao tem vencedor
                        out.println("O jogo finalizou-se por tempo.");
                    } else {
                        out.println("O usuário: " + Server.WINNER + " GANHOU!!!");
                        System.out.println("Thread: " + this.getName() + ". O usuário: " + Server.WINNER + " GANHOU!!!");
                        //tem um vencedor e o jogo acabou por isso
                    }
                    break;
                }

                boolean correctGuess = false;
                boolean isWon = false;

                System.out.println("Thread: " + this.getName() + ". O usuário: " + user.getUsername() + " enviou o paplite: " + guess);

                //processar o palpite
                if (guess.length() > 1) {
                    // é uma palavra
                    if (WORD_CHOOSE.equals(guess)) {
//                        correctGuess = true;
                        isWon = true;

                    }

                } else {

                    //é uma letra
                    //percorrer wordchoose
                    for (int i = 0; i < WORD_CHOOSE.length(); i++) {
                        if (WORD_CHOOSE.charAt(i) == guess.charAt(0)) {
                            //achei uma letra
                            sbHiddenWord.setCharAt(i, guess.charAt(0));
                            correctGuess = true;
                        }
                    }
                }
                if(sbHiddenWord.toString().equals(WORD_CHOOSE)){
                    isWon = true;
                }

                //completamente certo / parcialmente certo / incorreto
                if (isWon) {
                    out.println("GANHOU: " + WORD_CHOOSE);
                    System.out.println("O Jogador: " + user.getUsername() + " deu o palpite: {" + guess + "} e venceu. Palavra escolhida: " + WORD_CHOOSE);
                    Server.is_game_ended = true;
                    Server.WINNER = user.getUsername();
                } else if (correctGuess) {
                    out.println("PARCIAL");
                    System.out.println("O Jogador: " + user.getUsername() + " deu o palpite: {" + guess + "} e acertou 1 letra. Palavra escolhida: " + WORD_CHOOSE);
                } else {
                    out.println("ERRO");
                    System.out.println("O Jogador: " + user.getUsername() + " deu o palpite: {" + guess + "} e errou. Palavra escolhida: " + WORD_CHOOSE);
                }
            }


        } catch (IOException e) {
            System.err.println("Thread: " + this.getName() + " - Erro de I/O: " + e.getMessage());
//            throw new RuntimeException(e);
        } finally {
            System.out.println("Thread: " + this.getName() + " encerrada.");
            phaser.arriveAndDeregister(); // Fim da thread
        }
    }
}
