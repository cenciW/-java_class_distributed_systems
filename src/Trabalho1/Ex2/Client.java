package src.Trabalho1.Ex2;

import src.Trabalho1.utils.User;
import src.utils.InputValidation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static int attempts = 3;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Introduza o hostname do servidor ao qual vai se ligar: ");
        String hostName = sc.nextLine();

        int portNumber = InputValidation.validateIntBetween(sc,
                "Introduza a porta em que o server está escutando (entre 1024 e 65535): ",
                1024, 65535);

        try (
                Socket clientSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            User userLogged;
            do{

                System.out.print("Login:> ");
                String login = sc.nextLine();
                System.out.print("Senha:> ");
                String password = sc.nextLine();

                out.println(login);
                out.println(password);

                String statusAuth = in.readLine();

                if(statusAuth.equalsIgnoreCase("AUTENTICADO")){
                    //autenticado
                    userLogged = new User(login, password);
                    System.out.println(userLogged.getUsername() + " autenticado com sucesso.");
                    System.out.println("Aguardando início do jogo.");
                    break;
                } else if(statusAuth.equalsIgnoreCase("ERRO")){
                    //erro
                    //mexer com attemps
                    attempts--;
                    if(attempts == 0){
                        //nao tem mais tentativa
                        System.out.println("Tentativas encerradas.");
                        System.exit(0);
                    } else{
                        System.out.println("Tentativas restantes: " + attempts);
                    }

                }


            }while (true);

            //while do jogo
            while(true){
                String hiddenWord = in.readLine();
                //envia pro servidor a palavra ou a letra
                System.out.println("A palavra é: " + hiddenWord);

                //lendo o pitaco
                String guessFromClient = sc.nextLine();


                //mandando pitaco pro server
                out.println(guessFromClient);

                //recebe o status do server thread
                String feedback = in.readLine();

                if (feedback.startsWith("GANHOU:")) {
                    String palavraFinal = feedback.split(":")[1]; // Extrai a palavra final
                    System.out.println("Parabéns! Você ganhou. A palavra é: " + palavraFinal);
                    return;
                } else {
                    switch(feedback) {
                        case "PARCIAL":
                            System.out.println("Acertou o palpite: " + guessFromClient + "\n");
                            break;
                        case "ERRO":
                            System.out.println("Errou o palpite.");
                            break;
                        default:
                            System.out.println(feedback);
                            return;
                    }
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Erro de Host desconhecido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de input/output: " + e.getMessage());
        }
    }
}
