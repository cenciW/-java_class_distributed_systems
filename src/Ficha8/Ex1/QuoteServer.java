package src.Ficha8.Ex1;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.Date;

public class QuoteServer {
    private static final String fullFileName = System.getProperty("user.dir") + "\\src\\Ficha8\\Ex1\\phrases.txt";


    public static void main(String[] args) {
//        System.out.println("\n" + fullFileName);

        String phrase = new Date().toString();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fullFileName));
            //se tudo correr bem, envio a frase


            //se não for bem, preciso enviar apenas a data


        } catch (FileNotFoundException e) {
            //nao encontrado
            System.err.println("Não foi possível abrir o arquivo, em: " + fullFileName);
            //send datatime

        }

        //caso tenha acabado as linhas do arquivo, ele vai retornar a data
        //gravamos la no começo
        // data ou mesmo a frase
        System.out.println(phrase);

        //agora a parte dos sockets udp, para enviarmos a frase ao cliente

        //buffer com 256 bits
        byte[] buffer = new byte[256];

        //instanciando o packet com o tamanho do buffer definido
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try (
                DatagramSocket socket = new DatagramSocket(4332);
        ) {
            //aqui temos o socket funcionando
            boolean exit = false;
            while (!phrase.equals("Não há mais frases. Adeus!\n") && !exit) {
                if (br != null) {
                    phrase = getNextQuote(br);
                } else {
                    //enviar a data, por nao ter mais frases
                    exit = true;
                }
                //aqui ja temos o pacote que o cliente envia, nao só a ligação
                //de onde o pacote veio para poder devolver o pacote
                socket.receive(packet);
                //tenho que responder para esse endereç, por isso utilizando o get addres
                InetAddress address = packet.getAddress();
                //obtendo a porta para conseguir comunicar
                int port = packet.getPort();
                //alterar o pacote para responder ao cliente - mudar o buffer
                //buffer é um array de bytes, logo precisamos converter a frase em um array de bytes
                buffer = phrase.getBytes();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                //enviando  pacote
                socket.send(packet);

                //saber quando quer terminar?
                //enviar até nao ter mais frases
            }


        } catch (SocketException e) {
            System.err.println("Erro ao tentar abrir o socket, na porta 4332: " + e.getMessage());
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Erro ao receber o pacote do cliente: " + e.getMessage());
            System.exit(3);
        }


    }

    private static String getNextQuote(BufferedReader br) {
        String line;
        //lendo a linha do arquivo
        try {
            if ((line = br.readLine()) != null) {
                return line;
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo.");
        }
        return "Não há mais frases. Adeus!\n";
    }
}
