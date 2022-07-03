import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Arquivo {
    public ArrayList<String> ler(String file_name, int modo) {
        ArrayList<String> itens = new ArrayList<>();
//        int limitador = 697;

        try {
            FileReader file = new FileReader(file_name);
            BufferedReader reader = new BufferedReader(file);

            String linha = reader.readLine();

            while (linha != null) {
                String splitted[] = linha.split(",");
                String regex = "\"([^\"]*)\"";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(linha);

                if (modo == 0) { // Pegar nome do produto
                    itens.add(splitted[1]);
                } else if (modo == 1) {
                    if (matcher.find()) {
                        // Pega o conteúdo entre aspas duplas
                        String description = matcher.group(1);
                        itens.add(description);
                    } else {
                        // Pega o conteúdo através do split (caso sem aspas duplas)
                        itens.add(splitted[2]);
                    }
                }

                linha = reader.readLine();

                // Limitar quantidade de produtos sendo impressos
//                limitador--;
//                if(limitador == 0) break;
            }

            file.close();
        } catch (IOException exception) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", exception.getMessage());
        }

        return itens;
    }
}
