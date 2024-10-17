package src.Ficha7.Ex2;

import java.io.IOException;
import java.net.Socket;

public class CalcClient {

    //supostas 3 máquinas que podem receber clientes
    private static final String [] hostNames = {"localhost1", "localhost2", "localhost3"};
    //portas disponíveis para conexão
    private static final int [] ports = {6000, 6001, 6002};
    //máximo número de tentativas
    private static final int maxTries = 5;
    private int hostNamesIndex = 0;
    private int portsIndex = 0;
    // -

//    private static Socket connectToServer(String hostName, int port) {
//        return new Socket();
//    }
//
//    public static void main(String[] args) {
//        try {
//            while (true){
//                    Socket socket = connectToServer(hostNames[0], ports[0]);
////                Socket socket = connectToServer(hostNames[hostNamesIndex], ports[portsIndex]);
////                hostNamesIndex++;
////                portsIndex++;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


}
