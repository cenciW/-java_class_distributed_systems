package src.Ficha5.Ex1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Phaser;

public class ServerThread extends Thread {
    private final Socket clientSocket;
    private final Phaser phaser;

    public ServerThread(Socket clientSocket, Phaser phaser) {

        this.clientSocket = clientSocket;
        this.phaser = phaser;
        //register to the thread can wait
        phaser.register();

    }

    public void run() {
        //initializing buffer and writer
        try(PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ){

            //what the client will send
            //we receive and convert into integer
            int value = Integer.parseInt(in.readLine());

            //converting value to chips
            int currentChips = value/5;


            //sending message
            System.out.println("Thread Name: " + this.getName() + ": O Cliente investiu: "+ value + " euros. E obteve: "
                    + currentChips + " chips");

            //sending to the Client - Information the number of chips
            out.println(currentChips);

            //stay waiting for the Time Exception
            //without the synchronized we can't wait, because it gets lost
            synchronized (this) {
                try {
                    wait();  // Thread will wait here until it's notified
                } catch (InterruptedException e) {
                    System.err.println("Thread: " + this.getName() +
                            ": vai ser informado que o jogo vai começar");
                    out.println("Terminou o período para conexão de jogadores novos. Faça sua Aposta.");
                }
            }


            //game definition
            //time to game
            int number = 0, chipsToBet = 0;
            while(true){
                //number of chips to bet
                number = Integer.parseInt(in.readLine());
                System.out.println("Thread Name: " + this.getName() + ": O Cliente quer apostar no número: " + number);
                chipsToBet = Integer.parseInt(in.readLine());
                System.out.println("Thread Name: " + this.getName() + ": O Cliente quer apostar " + chipsToBet + " chips, no número: "
                        + number);

                //decrement my value of chips when bet
                currentChips -= chipsToBet;

                //TODO: bet logics

                //instancing the Random Class
                Random rand = new Random();
                //the numbers who we can bet in roulette
                Server.EXTRACTED_NUMBER = rand.nextInt(1,37);

                //wait all the bets - all clients
                //arrive and await to go
                phaser.arriveAndAwaitAdvance();


                System.out.println("Thread " + this.getName() + ": O número extraído foi " + Server.EXTRACTED_NUMBER + " chips");

                //check if win or not
                if(Server.EXTRACTED_NUMBER == number){
                    currentChips += chipsToBet * 35;
                }

                //send the response to the client
                out.println(Server.EXTRACTED_NUMBER);
                //returning to the client the result of bet
                out.println(currentChips);

                //sair or continuar.
                String text = in.readLine();

                if(text.equals("Sair")){
                    System.out.println("Thread " + this.getName() +
                            ": O cliente saiu do jogo");
                    break;

                }else {
                    System.out.println("Thread " + this.getName() +
                            ": O cliente vai continuar jogando");
                }

                if(Server.is_game_ended){
                    System.out.println("Thread " + this.getName() +
                            ": O jogo terminou.");
                    break;
                }
            }

        }catch (Exception e){
            //exceptions
            System.err.println("Thread: " + this.getName() +
                    ": Erro de criação dos buffers do socket" +
                    ". ERRO: " +
                    e.getMessage());
            phaser.arriveAndDeregister();
            System.exit(3);
        }
        //he can leave just for leave
        phaser.arriveAndDeregister();
    }

}
