import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelevanciaGeral {
    private int quantidade_produtos_dataset_geral; // [LEMBRETE] Está considerando a linha com a identificação das colunas
    private int quantidade_produtos_dataset_atual;
    private Object indice_invertido;

    /* Antes de iniciar o cálculo da relevância devem ser observados os seguintes pntos
     * 1. Todas as letras devem estar minúsculas
     * 2. Todos os caracteres especiais devem ser retirados
     * */

    public RelevanciaGeral(Object indice_invertido){
        this.indice_invertido = indice_invertido;
    }

    public boolean calcular(String termos_consulta[], ArrayList<String> dataset){
        // Laço para verificar se o termo existe no índice invertido
        ArrayList<String> termos_validos = new ArrayList<>();
        for (String termo : termos_consulta){
            if(getListPairs(termo) != null){
                termos_validos.add(termo);
//                System.out.println(termo);
            }
        }
//        System.out.println("vetor----------------");
        String termos[] = termos_validos.toArray(new String[0]);
//        for (String t : termos) System.out.println(t);

        if (termos.length == 0){
            System.out.println("[Termo(s) não encontrado(s) no índice]");
            return false;
        }

        ArrayList<Integer> lista_auxiliar = new ArrayList<>();
        // Este bloco será executado quando houver apenas um termo na consulta
        if (termos.length == 1) {
            String termo = termos[0];
            for (ParOcorrenciasID par : getListPairs(termo)) { // Para cada par (descrição) associado ao termo será feito o cálculo da relevância
                int id = (Integer) par.getIDProduto();
                inserirDados(dataset.size() - 1, getListPairs(termo).size());

                double rel = calcularRelevancia(dataset.get(id), termos);

                System.out.printf("Relevância do elemento de ID %d: %.3f\n", id, rel);
                System.out.println("Frase: " + dataset.get(id));
                System.out.println();
            }
        } else {
            // Este bloco será executado quando houver dois termos ou mais na consulta
            /*
             * 1. Percorrer todas as listas de todos os termos e verificar se há ID's em comum
             * 2. Na primeira lista adicionará todos os pares dela numa lista auxiliar
             * 3. A partir do segundo termo será verificado quais são os ID's em comum entre as listas
             * */

            boolean primeira_lista_copiada = false;
            ArrayList<Integer> interseccao = new ArrayList<>();

            // Calculo da intersecção [INICIO]
            for (String termo : termos) { // Para cada termo
                if (!primeira_lista_copiada) {
                    for (ParOcorrenciasID par : getListPairs(termo)) { // Para cada lista de cada termo
                        lista_auxiliar.add((Integer) par.getIDProduto()); // Cópia da primeira lista para a lista auxiliar
                    }
                    primeira_lista_copiada = true;
                } else {
                    for (ParOcorrenciasID par : getListPairs(termo)) { // Para cada lista de cada termo
                        for (Integer item : lista_auxiliar) {
                            if (((Integer) par.getIDProduto()).equals(item)) { // Se true é porque o par em questão existe nos dois conjuntos, adiciona-o na lista interseccao
                                interseccao.add((Integer) par.getIDProduto());
                            }
                        }
                    }
                    lista_auxiliar.clear();
                    lista_auxiliar = new ArrayList<>(interseccao);
                    interseccao.clear();
                }
            }
            // Calculo da intersecção [FIM]

            System.out.println("[Cálculo da intersecção finalizado: " + lista_auxiliar + "]");

            // Calculo da relevância [INÍCIO]
            for (Integer id_produto : lista_auxiliar) { // Para cada par (descrição) associado ao termo será feito o cálculo da relevância
                //int id = (Integer) par.getIDProduto();
                inserirDados(dataset.size(), lista_auxiliar.size());

                double rel = calcularRelevancia(dataset.get(id_produto), termos);
                System.out.printf("Relevância do elemento de ID %d: %.3f\n", id_produto, rel);
                System.out.println("Frase: " + dataset.get(id_produto));
            }
            // Calculo da relevância [FIM]
        }

        return true;
    }

    private double calcularRelevancia(String descricao, String termos_consulta[]){
        String conteudo;
        conteudo = descricao.toLowerCase(Locale.ROOT);
        conteudo = conteudo.replaceAll("[-.,+=*&:®…•\u00AD–—’;%$#@|!?_\"\']", "");
        conteudo = conteudo.replaceAll(" ", " ");
        conteudo = conteudo.replaceAll("/", " ");
        float soma_pesos = 0;

        String palavras[] = descricao.split(" ");
        int quantidade_palavras_distintas = identificarNumeroDePalavrasDistintas(conteudo);

        for (int i = 0; i < termos_consulta.length; i++){
            double peso = calcularPesoTermo(conteudo, termos_consulta[i]);
            //System.out.println("Cálculo do peso: "+peso);
            //soma_pesos += calcularPesoTermo(descricao, termos_consulta[i]);
            soma_pesos += peso;
        }

        return (soma_pesos/quantidade_palavras_distintas);
    }

    private double calcularPesoTermo(String descricao, String termo){
        int quantidade_ocorrencias = ocorrenciasTermo(descricao, termo);
        //System.out.println("Quantidade de ocorrencias do termo na frase: "+quantidade_ocorrencias);
        return (quantidade_ocorrencias * log(2, quantidade_produtos_dataset_geral)/quantidade_produtos_dataset_atual);
    }

    private int ocorrenciasTermo(String conteudo, String termo){
        Matcher m = Pattern.compile("("+termo+")",Pattern.DOTALL).matcher(conteudo);
        int ocorrencias = 0;
        while (m.find()) ocorrencias++;

        return ocorrencias;
    }

    private int identificarNumeroDePalavrasDistintas(String conteudo){
        String palavras[] = conteudo.split(" ");

        String palavras_identificadas = "";

        for(String palavra : palavras){
            if (!palavras_identificadas.contains(palavra)){
                palavras_identificadas += palavra+" ";
            }
        }

        int palavras_distintas = palavras_identificadas.split(" ").length;

        System.out.println("Número de palavras distintas na frase: "+palavras_distintas);

        return palavras_distintas;
    }

    private double log(double base, double valor) {
        return Math.log(valor) / Math.log(base);
    }

    private ArrayList<ParOcorrenciasID> getListPairs(String termo) {
        if (indice_invertido instanceof Map) {
            Map<String, ArrayList<ParOcorrenciasID>> indice = (Map) this.indice_invertido;
            return indice.get(termo);
        } else if (indice_invertido instanceof AVLAdaptado) {
            AVLAdaptado indice = (AVLAdaptado) this.indice_invertido;
            return indice.busca(termo).getPares();
        } else {
            System.out.println("[indiceContemChave] Não identificou nenhum tipo");
            return null;
        }
    }

    private void inserirDados(int qt_geral, int qt_atual){
        this.quantidade_produtos_dataset_atual = qt_atual;
        this.quantidade_produtos_dataset_geral = qt_geral;
    }

    public void getDados(){
        System.out.println("N = "+this.quantidade_produtos_dataset_geral+" / d = "+this.quantidade_produtos_dataset_atual);

    }
}

