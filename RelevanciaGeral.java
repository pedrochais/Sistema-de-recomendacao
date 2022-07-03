import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelevanciaGeral {
    private Map<Integer, Double> lista_descricao_relevancia = new HashMap<>();
    Scanner input = new Scanner(System.in);
    private int quantidade_produtos_dataset_geral; // [LEMBRETE] Está considerando a linha com a identificação das colunas
    private int quantidade_produtos_dataset_atual;
    private String estrutura_nome;
    private Object indice_invertido;

    public Map<Integer, Double> getDescricaoRelevancia() {
        return lista_descricao_relevancia;
    }

    public void setTAD(Object indice_invertido){
        this.indice_invertido = indice_invertido;
//        if (indice_invertido instanceof Map) {
//            System.out.println("Hash");
//
//        } else if (indice_invertido instanceof AVLAdaptado) {
//            System.out.println("Limpando AVL");
//
//
//        } else if (indice_invertido instanceof RBTreeAdaptado) {
//            System.out.println("Limpando RBTree");
//
//
//        } else {
//            System.out.println("[setTAD] Não identificou nenhum tipo");
//        }
    }

    public void limparIndice(){
        lista_descricao_relevancia = new HashMap<>(); // Limpar lista onde são guardadas as chaves
        if (indice_invertido instanceof Map) {
            this.indice_invertido = new HashMap<>();
        } else if (indice_invertido instanceof AVLAdaptado) {
            this.indice_invertido = new AVLAdaptado();
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            this.indice_invertido = new RBTreeAdaptado();
        } else {
            System.out.println("[limparIndice] Não identificou nenhum tipo");
        }
    }

    /* Antes de iniciar o cálculo da relevância devem ser observados os seguintes pontos
     * 1. Todas as letras devem estar minúsculas
     * 2. Todos os caracteres especiais devem ser retirados
     * */
    public RelevanciaGeral(Object indice_invertido) {
        this.indice_invertido = indice_invertido;
        if (indice_invertido instanceof Map) {
            this.estrutura_nome = "HashMap";
        } else if (indice_invertido instanceof AVLAdaptado) {
            this.estrutura_nome = "Árvore AVL";
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            this.estrutura_nome = "Árvore Rubro-Negra";
        } else {
            System.out.println("[IndiceInvertidoGeral] Não identificou nenhum tipo");
        }
    }

    public boolean calcular(String termos_consulta[], ArrayList<String> dataset) {

        for (String t : termos_consulta){
            System.out.println("Termo: "+t);
        }

        System.out.println(termos_consulta.length);
        this.lista_descricao_relevancia = new HashMap<>();

        //MODIFICAÇÕES FEITAS ATÉ AQUI

        // Laço para verificar se o termo existe no índice invertido
        ArrayList<String> termos_validos = new ArrayList<>();
        for (String termo : termos_consulta) {
            if (getListPairs(termo) != null) { // Se houver uma lista associada ao termo
                // Adiciona o termo na lista de termos válidos
                termos_validos.add(termo);
            }
        }


        // Convertendo lista para um vetor
        String termos[] = termos_validos.toArray(new String[0]);

        if (termos.length == 0) {
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

//                System.out.printf("Relevância do elemento de ID %d: %.3f\n", id, rel);
//                System.out.println("Frase: " + dataset.get(id));
//                System.out.println();

                lista_descricao_relevancia.put(id, rel);
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

            // Calculo da relevância
            for (Integer id_produto : lista_auxiliar) { // Para cada par (descrição) associado ao termo será feito o cálculo da relevância
                inserirDados(dataset.size() - 1, lista_auxiliar.size()); // Determinando o valor para N e d
                double rel = calcularRelevancia(dataset.get(id_produto), termos);

//                System.out.printf("Relevância do elemento de ID %d: %.3f\n", id_produto, rel);
//                System.out.println("Frase: " + dataset.get(id_produto));
//                System.out.println();

                lista_descricao_relevancia.put(id_produto, rel);
            }
        }

        return true;
    }

    public void printDescricaoRelevancia(ArrayList<String> nomes_produtos, Double limiar) {
        List<Map.Entry<Integer, Double>> list = new ArrayList<>(this.lista_descricao_relevancia.entrySet());
        list.sort(Map.Entry.comparingByValue());
        ArrayList<Integer> list_id = new ArrayList<>();
        int contador;
        boolean continuar = true;
        /*
        System.out.println("\n[Lista completa]");
        contador = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            System.out.println("ID: " + list.get(i).getKey() + " / Relevância: " + list.get(i).getValue());

            contador++;
            if(contador == 10){
                while(true){
                    System.out.println("[AVISO] A lista a ser impressa possui mais de 10 itens ("+(list.size() - 1)+" restante(s)), deseja continular? (S/N)");
                    String entrada = input.nextLine();
                    if(entrada.equals("S") || entrada.equals("s")){
                        continuar = true;
                        break;
                    }else if (entrada.equals("N") || entrada.equals("n")){
                        continuar = false;
                        break;
                    }
                }
            }
            if (!continuar) break;
        }
        */
        System.out.println("\nLista de recomendações (limiar = "+limiar+")\n");
        continuar = true;
        contador = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getValue() > limiar) {
                list_id.add((Integer) list.get(i).getKey());
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getValue() > limiar) {
                //System.out.println("ID: " + list.get(i).getKey() + " / Relevância: " + list.get(i).getValue());
                System.out.println("[PRODUTO]: "+nomes_produtos.get(list.get(i).getKey()));
                System.out.println("[RELEVÂNCIA]: "+list.get(i).getValue()+"\n");
            }

            contador++;
            if(contador == 10){
                while(true){
                    System.out.println("[AVISO] A lista a ser impressa possui mais de 10 itens ("+(list.size() - 1)+" restante(s)), deseja continular? (S/N)");
                    String entrada = input.nextLine();
                    if(entrada.equals("S") || entrada.equals("s")){
                        continuar = true;
                        break;
                    }else if (entrada.equals("N") || entrada.equals("n")){
                        continuar = false;
                        break;
                    }
                }
            }
            if (!continuar) break;
        }

//        Collections.sort(list_id);
//        System.out.println("\n[Lista ordenada dos ID's recomendados]");
//
//        for (Integer item : list_id){
//            System.out.print(item+", ");
//        }
        System.out.println();
    }

    private double calcularRelevancia(String descricao, String termos_consulta[]) {
        String conteudo;
        conteudo = descricao.toLowerCase(Locale.ROOT);
        conteudo = conteudo.replaceAll(Main.regex, "");
        conteudo = conteudo.replaceAll(" ", " ");
        conteudo = conteudo.replaceAll("/", " ");
        float soma_pesos = 0;

        String palavras[] = descricao.split(" ");
        int quantidade_palavras_distintas = identificarNumeroDePalavrasDistintas(conteudo);

//        System.out.println(termos_consulta.length);
        for (int i = 0; i < termos_consulta.length; i++) {
            double peso = calcularPesoTermo(conteudo, termos_consulta[i]);
            //System.out.println("Cálculo do peso ("+termos_consulta[i]+"): "+peso);
            //soma_pesos += calcularPesoTermo(descricao, termos_consulta[i]);
            soma_pesos += peso;
        }

        return (soma_pesos / quantidade_palavras_distintas);
    }

    private double calcularPesoTermo(String descricao, String termo) {
        int quantidade_ocorrencias = ocorrenciasTermo(descricao, termo);
        //System.out.println("Quantidade de ocorrencias do termo na frase: "+quantidade_ocorrencias);
        return (quantidade_ocorrencias * log(2, quantidade_produtos_dataset_geral) / quantidade_produtos_dataset_atual);
    }

    private int ocorrenciasTermo(String conteudo, String termo) {
        Matcher m = Pattern.compile("(" + termo + ")", Pattern.DOTALL).matcher(conteudo);
        int ocorrencias = 0;
        while (m.find()) ocorrencias++;

        return ocorrencias;
    }

    private int identificarNumeroDePalavrasDistintas(String conteudo) {
        String palavras[] = conteudo.split(" ");

        String palavras_identificadas = "";

        for (String palavra : palavras) {
            if (!palavras_identificadas.contains(palavra)) {
                palavras_identificadas += palavra + " ";
            }
        }

        int palavras_distintas = palavras_identificadas.split(" ").length;

        //System.out.println("Número de palavras distintas na frase: "+palavras_distintas);

        return palavras_distintas;
    }

    private ArrayList<ParOcorrenciasID> getListPairs(String termo) {
        if (indice_invertido instanceof Map) {
            Map<String, ArrayList<ParOcorrenciasID>> indice = (Map) this.indice_invertido;
            return indice.get(termo);
        } else if (indice_invertido instanceof AVLAdaptado) {
            AVLAdaptado indice = (AVLAdaptado) this.indice_invertido;
            try {
                ArrayList<ParOcorrenciasID> pares = indice.busca(termo).getPares();
                return pares;
            } catch (NullPointerException exception) {
                System.out.println("[ERRO]: " + exception);
                System.out.println("[CORREÇÃO]: Retornando null.");
                return null;
            }
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            RBTreeAdaptado indice = (RBTreeAdaptado) this.indice_invertido;
            return indice.busca(termo).getPares();
        } else {
            System.out.println("[getListPairs] Não identificou nenhum tipo");
            return null;
        }
    }

    private void inserirDados(int qt_geral, int qt_atual) {
        this.quantidade_produtos_dataset_atual = qt_atual;
        this.quantidade_produtos_dataset_geral = qt_geral;
    }

    public void getDados() {
        System.out.println("N = " + this.quantidade_produtos_dataset_geral + " / d = " + this.quantidade_produtos_dataset_atual);

    }

    private double log(double base, double valor) {
        return Math.log(valor) / Math.log(base);
    }

    public String getEstruturaNome() {
        return estrutura_nome;
    }

    public void resetarIndice(Object indice_invertido){
        this.indice_invertido = indice_invertido;
    }
}

