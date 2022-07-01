import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Arquivo {
    public ArrayList<String> ler(String file_name){
        ArrayList<String> itens = new ArrayList<>();
        //String file_name = "amz.csv";
        int limitador = 697;

        try {
            FileReader file = new FileReader(file_name);
            BufferedReader reader = new BufferedReader(file);

            String linha = reader.readLine();

            while (linha != null) {
                String splitted[] = linha.split(",");
                String regex = "\"([^\"]*)\"";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(linha);

                //System.out.println("ID do produto: "+splitted[0]);
                //System.out.println("Nome do produto: "+splitted[1]);

                if (matcher.find()) {
                    String description = matcher.group(1);
                    //System.out.println("Descrição do produto: "+description+"\n");
                    itens.add(description);
                }else{
                    //System.out.println("Descrição do produto: "+splitted[2]+"\n");
                    itens.add(splitted[2]);
                }

                linha = reader.readLine();

                // Limitar quantidade de produtos sendo impressos
                limitador--;
                if(limitador == 0) break;
            }

            file.close();
        } catch (IOException exception) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", exception.getMessage());
        }

        return itens;
    }
}
