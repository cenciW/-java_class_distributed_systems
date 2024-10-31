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
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))
        ) {
            User userLogged;
            do{
                //login e senha
                System.out.print("Login:> ");
                String login = sc.nextLine();
                System.out.print("Senha:> ");
                String password = sc.nextLine();

                //enviando login e senha para o server
                out.println(login);
                out.println(password);

                //recebendo a resposta da auth do servidor
                String statusAuth = in.readLine();

                if(statusAuth.equalsIgnoreCase("INICIADO")){
                    System.out.println("O jogo já iniciou, você não pode mais se conectar.");
                    System.exit(0);
                }

                //se o que vier de resposta do servidor for: "FINALIZADO" significa que o jogo acabou.
                if(statusAuth.equalsIgnoreCase("FINALIZADO")){
                    System.out.println("O jogo já foi finalizado, não pode mais se conectar.");
                    return;
                }

                //se o que vier de resposta do servidor for: "AUTENTICADO" significa que o jogador pode jogar.
                if(statusAuth.equalsIgnoreCase("AUTENTICADO")){
                    //autenticado
                    userLogged = new User(login, password);
                    System.out.println(userLogged.getUsername() + " autenticado com sucesso.");
                    break;
                } else if(statusAuth.equalsIgnoreCase("ERRO")){
                    //erro
                    //mexer com attemps
                    attempts--;
                    if(attempts == 0){
                        //nao tem mais tentativa de login
                        System.out.println("Tentativas encerradas.");
                        System.exit(0);
                    } else{
                        if(statusAuth.equalsIgnoreCase("ERRO")){
                            System.out.println("O usuário já está logado.");
                        }
                        System.out.println("Tentativas restantes: " + attempts);
                    }

                }


            }while (true);

            //while do jogo
            System.out.println("Aguarde o jogo começar...");
            System.out.println(in.readLine());

            String guessFromClient ="";
            do{
                String hiddenWord = in.readLine();
                //envia pro servidor a palavra ou a letra
                System.out.println("A palavra é: " + hiddenWord);


                //lendo o palpite
                System.out.println("Digite 'desisto' para sair do jogo.");
                System.out.print(":>");
                guessFromClient = sc.nextLine();


                //mandando palpite pro server
                out.println(guessFromClient);

                //recebe o feedback do server thread
                String feedback = in.readLine();

                //se o que vier no feedback começar com GANHOU.
                if (feedback.startsWith("GANHOU:")) {
                    //temos um vencedor
                    String palavraFinal = feedback.split(":")[1]; // Extrai a palavra final
                    System.out.println("Parabéns! Você ganhou. A palavra é: " + palavraFinal);
                    return;
                } else {
                    //se nao começar com GANHOU.
                    switch(feedback) {
                        //acerto parcial, ou seja, o server enviou para o cliente: PARCIAL
                        //significa que o cliente acertou uma letra
                        case "PARCIAL":
                            System.out.println("Acertou o palpite: " + guessFromClient + "\n");
                            break;
                        //erro, ou seja, o server enviou para o cliente: ERRO
                        //significa que o cliente inseriu mais de uma palavra, ou mais de uma letra.
                        //portanto está com erro e nao podemos continuar
                        case "ERRO":
                            System.out.println("Errou o palpite.");
                            break;
                        //finalizado, ou seja, o server enviou para o cliente: FINALIZADO.
                        //significa que algum cliente já acertou o palpite e por isso o jogo foi finalizado
                        //portanto ninguém pode mais se conectar
                        case "FINALIZADO":
                            System.out.println("O jogo já foi finalizado, não pode mais se conectar.\n");
                        //caso padrão, se nao cair em nenhum dos cases, cai aqui e apenas printa o que o servidor enviou para cá (cliente)
                        case "DESISTENCIA":
                            System.out.println("Obrigado por tentar, que pena que desistiu.");
                        default:
                            System.out.println(feedback);
                            return;
                    }
                }
            } while (!guessFromClient.equalsIgnoreCase("desisto"));

        } catch (UnknownHostException e) {
            System.err.println("Erro de Host desconhecido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de input/output: " + e.getMessage());
        }
    }
}
