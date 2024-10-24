package src.Ficha8.Ex2;

import java.io.IOException;
import java.net.*;

public class QuoteClient {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[256];
            InetAddress addres = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, addres, 4322);
            //enviou o pacote
            socket.send(packet);

            //agora precisa ficar esperando para receber
            socket.receive(packet);
            //mostrar o que recebi
            String phraseReceived = new String(packet.getData());
            System.out.println(phraseReceived);

        } catch (SocketException e) {
            System.err.println("Erro ao tentar criar o socket: " + e.getMessage());
        } catch (UnknownHostException e) {
            System.err.println("Nome de servidor desconhecido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao enviar o pacote: " + e.getMessage());
        }
    }
}
