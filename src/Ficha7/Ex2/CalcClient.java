//package src.Ficha7.Ex2;
//
//import src.Ficha7.utils.CalcModel;
//
//import java.io.*;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class CalcClient {
//
//    //supostas 3 máquinas que podem receber clientes
//    private static final String [] hostNames = {"localhostas", "localhost", "localhost3"};
//    //portas disponíveis para conexão
//    private static final int [] ports = {6000, 6001, 6002};
//    //máximo número de tentativas
//    private static final int maxTries = 5;
//    private static int hostNamesIndex = 0;
//    private static int portsIndex = 0;
//    // -
//
//    private static Socket connectToServer(String hostName, int port) {
//        //logic to connect to the server
//        try {
//            return new Socket(
//                    hostName, port
//            );
//        } catch (IOException e) {
//            System.err.println("Erro ao criar o socket: " + e.getMessage() + " porta: " + port);
//        }
//        return null;
//    }
//
//    public static void main(String[] args) {
//        while (true) {
//            if(hostNamesIndex == hostNames.length){
//                System.err.println("Não foi possível se conectar com nenhum hostname");
//                System.exit(0);
//            }
//            try (Socket socket = connectToServer(hostNames[hostNamesIndex], ports[portsIndex]);
//            ){
//                break;
//            } catch (IOException e) {
//                System.err.println("A conexão com o host:" + hostNames[hostNamesIndex] + " falhou.");
//
//                if(portsIndex == (ports.length - 1)){
//                    hostNamesIndex++;
//                    portsIndex = 0;
//                }else{
//                    portsIndex++;
//                }
//            }
//        }
//        System.out.println("Conectado com êxito.");
//        try (
//                Socket clientSocket = connectToServer(hostNames[hostNamesIndex], ports[portsIndex]);
//                Scanner sc = new Scanner(System.in);
//                ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());
//                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
//        ) {
//            CalcModel cm;
//
//            //envia operando 1
//            System.out.print("Digite o operando 1: ");
//            double operando1 = Double.parseDouble(sc.nextLine());
//
////            out.println(operando1);
//
//            //envia operador
//            System.out.print("Digite o operador: ");
//            String operador = sc.nextLine();
//
////            out.println(operador);
//
//            //envia operando 2
//            System.out.print("Digite o operando 2: ");
//            double operando2 = Double.parseDouble(sc.nextLine());
//
////            out.println(operando2);
//            try{
//                cm = new CalcModel(operando1, operando2, operador);
//
//                //enviando o objeto completo
//                objOut.writeObject(cm);
//
//                double resultado = Double.parseDouble(in.readLine());
//
//                System.out.println(cm.toString());
//                System.out.println("Resultado: " + resultado);
//            }catch (Exception e){
//                System.err.println("Erro ao enviar o objeto: " + e.getMessage());
//
//            }
//
//
//
//
//            //recebe o resultado
////            out.println(resultado);
//            //se nao respeitar a sintaxe: “<operando1> <operador> <operando2>” o cliente deve ser avisao
//            //Os operadores reconhecidos pelo servidor são: +, -, *, x, X, /, :;
//            //O servidor deve terminar a ligação ao cliente quando receber a palavra “Sair” ou “sair”.
//            System.out.println(in.readLine());
//
//
//        } catch(IOException e) {
//            System.err.println("Ocorreu um erro ao criar os buffers de comunicação " + e.getMessage());
//        }
//    }
//
//
//}



package src.Ficha7.Ex2;

import src.Ficha7.utils.CalcModel;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CalcClient {

    //supostas 3 máquinas que podem receber clientes
    private static final String [] hostNames = {"localhost", "localhost2", "localhost3"};
    //portas disponíveis para conexão
    private static final int [] ports = {6000, 6001, 6002};
    //máximo número de tentativas
    private static final int maxTries = 5;
    private static int hostNamesIndex = 0;
    private static int portsIndex = 0;
    private static String textCalc;
    // -

    private static Socket connectToServer(String hostName, int port) {
        // lógica para conectar ao servidor
        try {
            return new Socket(hostName, port);
        } catch (IOException e) {
            System.err.println("Erro ao criar o socket: " + e.getMessage() + " porta: " + port);
        }
        return null;
    }

    public static void main(String[] args) {
        Socket clientSocket = null;

        // Tentar conectar ao servidor
        while (clientSocket == null) {
            if (hostNamesIndex == hostNames.length) {
                System.err.println("Não foi possível se conectar com nenhum hostname");
                System.exit(0);
            }
            clientSocket = connectToServer(hostNames[hostNamesIndex], ports[portsIndex]);
            if (clientSocket == null) {
                System.err.println("A conexão com o host:" + hostNames[hostNamesIndex] + " falhou.");
                if (portsIndex == (ports.length - 1)) {
                    hostNamesIndex++;
                    portsIndex = 0;
                } else {
                    portsIndex++;
                }
            }
        }

        System.out.println("Conectado com êxito.");

        // Comunicação com o servidor
        try (
                Scanner sc = new Scanner(System.in);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
//                ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream objIn = new ObjectInputStream(clientSocket.getInputStream())
        ) {

            do{
                CalcModel cm;

                System.out.println("Entre com os valores desejados...");
                System.out.println("Siga o padrão para que funcione corretamente.");
                System.out.print("<operando1> <operador> <operando2>\n:>");
                textCalc = sc.nextLine();

                out.println(textCalc);

                //tentar receber o objeto de resposta
                try{
                    cm = (CalcModel) objIn.readObject();
                    System.out.println("Para a operação: {" + cm.toString() + "}");

                    System.out.println("O resultado é: " + cm.getResultado() + "\n\n");

                }catch (ClassNotFoundException e) {
                    System.err.println("Erro na recepção do objeto: " + e.getMessage());
                }
            }while(!textCalc.equalsIgnoreCase("Sair"));



        } catch (IOException e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
        }
    }
}

