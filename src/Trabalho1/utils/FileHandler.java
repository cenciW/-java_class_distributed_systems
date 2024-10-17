package src.Trabalho1.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileHandler {

    final private String filePath = "src/Trabalho1/users.txt";
    File file;


    public FileHandler() {
        file = new File(new File(filePath).getAbsolutePath());
    }

public Optional<User> login(User u) {
    List<String> lines;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));){
        lines = Files.readAllLines(Paths.get("src/Trabalho1/users.txt"));
    } catch (IOException e) {
        e.printStackTrace();
        return Optional.empty();
    }

    // Percorre as linhas procurando pelo usuário
    for (int i = 0; i < lines.size(); i++) {
        String[] userData = lines.get(i).split(";");
        String username = userData[0];
        String password = userData[1];
        String isLogged = userData[2];

        // Se o usuário não for encontrado ou já estiver logado
        if (username.equals(u.getUsername())) {
            if (isLogged.equals("1")) {
                // Usuário já está logado
                return Optional.empty(); // Retorna vazio indicando que o usuário não pode logar
            }
            if (password.equals(u.getPassword())) {
                // Se a senha estiver correta, autentica o usuário
                userData[2] = "1"; // Marca como logado
                lines.set(i, String.join(";", userData));

                // Escreve de volta no arquivo com o status atualizado
                try {
                    Files.write(Paths.get("src/Trabalho1/users.txt"), lines);
                } catch (IOException e) {
                    e.printStackTrace();
                    return Optional.empty();
                }
                return Optional.of(u); // Retorna o usuário autenticado
            } else {
                // Senha incorreta
                return Optional.empty(); // Retorna vazio indicando falha de login
            }
        }
    }

    // Se o usuário não foi encontrado
    return Optional.empty();
}

}
