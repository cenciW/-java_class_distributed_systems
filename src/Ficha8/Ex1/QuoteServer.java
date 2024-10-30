package src.Ficha8.Ex1;

import java.io.*;
import java.net.*;
import java.util.Date;

public class QuoteServer {
    private static final String fullFileName = System.getProperty("user.dir") + "\\src\\Ficha8\\Ex1\\phrases.txt";

    public static void main(String[] args) {
//        System.out.println("\n" + fullFileName);

        String phrase = new Date().toString();
        BufferedReader br = null;
        boolean noMorePhrases = false;

        //tentando instanciar o buffered reader com a leitura do arquivo ja
        try {
            br = new BufferedReader(new FileReader(fullFileName));
            System.out.println("Arquivo aberto!");
        } catch (FileNotFoundException e) {
            //nao encontrado
            System.err.println("Não foi possível abrir o arquivo, em: " + fullFileName);
            noMorePhrases = true;
            //send datatime
            phrase = new Date().toString();
        }

        //caso tenha acabado as linhas do arquivo, ele vai retornar a data
        System.out.println("Data: " + phrase);
        //gravamos la no começo
        // data ou mesmo a frase


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
            while (!exit) {
                if (!noMorePhrases && br != null) {
                    phrase = getNextQuote(br);
                    if (phrase.equals("Não há mais frases. Adeus!\n")) {
                        noMorePhrases = true;
                    }
                } else {
                    phrase = "As frases acabaram. Data atual: " + new Date().toString(); // Agora enviaremos a data após as frases acabarem
                }
                //aqui ja temos o pacote que o cliente envia, nao só a ligação
                //de onde o pacote veio para poder devolver o pacote
                socket.receive(packet);
                //tenho que responder para esse endereço, por isso utilizando o get address
                InetAddress address = packet.getAddress();
                //obtendo a porta para conseguir comunicar
                int port = packet.getPort();
                //alterar o pacote para responder ao cliente - mudar o buffer
                //buffer é um array de bytes, logo precisamos converter a frase em um array de bytes
                buffer = phrase.getBytes();

                //calcular quantos caracteres tem para não preencher o buffer de caracteres 'nuls'

                packet = new DatagramPacket(buffer, buffer.length, address, port);
                //enviando  pacote
                socket.send(packet);

                System.out.println("Enviando pacote para o cliente: " + packet.getAddress().getHostAddress() + ":" + packet.getPort());

                // O loop continua mesmo depois de "Adeus", permitindo que o servidor envie a data aos novos clientes
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
