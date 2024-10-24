package src.Ficha7.Ex1;

import src.Ficha7.utils.CalcModel;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class CalcClientThread extends Thread{
    final private Socket clientSocket;
    final private int SUM = 0;
    final private int DIFF = 1;
    final private int MULT = 2;
    final private int DIVIDE = 3;
    private int operadorFlag;
    private CalcModel calcModel;
    double operando1, operando2, resultado;
    String operador;
    private boolean isEnd;


    public CalcClientThread(Socket clientSocket) {
        System.out.println(this.getName() + ": Ligação efetuada com o cliente.");

        this.clientSocket = clientSocket;
    }

    public void run(){
        try(
//                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                Scanner sc = new Scanner(System.in);
                ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                )
        {
            String input = "";
            while(!input.equalsIgnoreCase("sair")){

//                calcModel = (CalcModel) objIn.readObject();
                String calculoText = in.readLine();
                System.out.println(calculoText);

                if(input.equalsIgnoreCase("sair")){
                    objOut.writeObject("Sair");
                    break;
                }

                String [] splitedMessage = calculoText.split(" ");
                //tratar a mensagem
                operando1 = Double.parseDouble(splitedMessage[0]);
                operador = splitedMessage[1];
                operando2 = Double.parseDouble(splitedMessage[2]);

                //verificar se ta ok
                calcModel = new CalcModel();


                calcModel.setOperando1(operando1);
                calcModel.setOperando2(operando2);
                calcModel.setOperador(operador);



                if(calcModel.getOperador().equals("+")){
                    operadorFlag = 0;
                }

                if(calcModel.getOperador().equals("-")){
                    operadorFlag = 1;
                }

                if(calcModel.getOperador().equals("*") || calcModel.getOperador().equalsIgnoreCase("x")){
                    operadorFlag = 2;
                }

                if(calcModel.getOperador().equals("/") || calcModel.getOperador().equals(":")){
                    operadorFlag = 3;
                }

                switch (operadorFlag) {
                    case SUM:
                        resultado = calcModel.sum(calcModel.getOperando1(), calcModel.getOperando2());
                        break;
                    case DIFF:
                        resultado = calcModel.difference(calcModel.getOperando1(), calcModel.getOperando2());
                        break;
                    case MULT:
                        resultado = calcModel.multiply(calcModel.getOperando1(), calcModel.getOperando2());
                        break;
                    case DIVIDE:
                        resultado = calcModel.divide(calcModel.getOperando1(), calcModel.getOperando2());
                        break;
                    default:
                        resultado = Double.NaN;  // Caso de erro
                        break;
                }

                // Enviar o resultado de volta para o cliente
                calcModel.setResultado(resultado);
                objOut.writeObject(calcModel);

                System.out.println("Thread-NAME: " + this.getName()+ calcModel.toString() + ".\nResultado: " + calcModel.getResultado() + '\n');


                //se nao respeitar a sintaxe: “<operando1> <operador> <operando2>” o cliente deve ser avisao
                //Os operadores reconhecidos pelo servidor são: +, -, *, x, X, /, :;
                //O servidor deve terminar a ligação ao cliente quando receber a palavra “Sair” ou “sair”.
//            String text = in.readLine();
//            System.out.println(Thread.currentThread().getName() + ": " + text);

            }

        } catch (IOException e) {
            System.err.println(this.getName() + ": Erro ao criar os buffers de comunicação.");
            System.exit(2);
        } finally {
        try {
            clientSocket.close();
            System.out.println("Conexão encerrada.");
        } catch (IOException e) {
            System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
        }
    }
}
}
