package Descartados;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Relevancia {
    private int quantidade_produtos_dataset_geral;
    private int quantidade_produtos_dataset_atual;

    /* Antes de iniciar o cálculo da relevância devem ser observados os seguintes pntos
    * 1. Todas as letras devem estar minúsculas
    * 2. Todos os caracteres especiais devem ser retirados
    * */

    public double calcular(String descricao, String termos_consulta[]){
        float soma_pesos = 0;

        String palavras[] = descricao.split(" ");
        int quantidade_palavras_distintas = identificarNumeroDePalavrasDistintas(descricao);

        for (int i = 0; i < termos_consulta.length; i++){
            double peso = calcularPesoTermo(descricao, termos_consulta[i]);
            //System.out.println("Cálculo do peso: "+peso);
            //soma_pesos += calcularPesoTermo(descricao, termos_consulta[i]);
            soma_pesos += peso;
        }

        return (soma_pesos/quantidade_palavras_distintas);
    }

    public void inserirDados(int qt_geral, int qt_atual){
        this.quantidade_produtos_dataset_atual = qt_atual;
        this.quantidade_produtos_dataset_geral = qt_geral;
    }

    private double calcularPesoTermo(String descricao, String termo){
        int quantidade_ocorrencias = ocorrenciasTermo(descricao, termo);
        //System.out.println("Quantidade de ocorrencias do termo na frase: "+quantidade_ocorrencias);
        return (quantidade_ocorrencias * log(2, quantidade_produtos_dataset_geral)/quantidade_produtos_dataset_atual);
    }

    private int ocorrenciasTermo(String descricao, String termo){
        Matcher m = Pattern.compile("("+termo+")",Pattern.DOTALL).matcher(descricao);
        int ocorrencias = 0;
        while (m.find()) ocorrencias++;

        return ocorrencias;
    }

    private int identificarNumeroDePalavrasDistintas(String descricao){
        String conteudo;
        conteudo = descricao.toLowerCase(Locale.ROOT);
        conteudo = conteudo.replaceAll("[-.,+=–*&:•—’;%$#@|!?_\"\']", "");
        conteudo = conteudo.replaceAll(" ", " ");
        conteudo = conteudo.replaceAll("/", " ");
        String palavras[] = conteudo.split(" ");

        String palavras_identificadas = "";

        for(String palavra : palavras){
            if (!palavras_identificadas.contains(palavra)){
                palavras_identificadas += palavra+" ";
            }
        }

        int palavras_distintas = palavras_identificadas.split(" ").length;

        return palavras_distintas;
    }

    private void identificarTermosRelevantes(String descricao){
        //String stopwords[] = {""}
    }

    private double log(double base, double valor) {
        return Math.log(valor) / Math.log(base);
    }
}
