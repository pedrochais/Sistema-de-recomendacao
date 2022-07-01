import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IndiceInvertidoGeral {
    private Object indice_invertido;
    private String termos;

    public IndiceInvertidoGeral() {
        indice_invertido = new HashMap<>();
        termos = "";
    }

    public void teste(Object indice) {
        if (indice instanceof Map) {
            indice_invertido = (Map<String, ArrayList<ParOcorrenciasID>>)indice;
            System.out.println("é map");
        } else if (indice instanceof AVLAdaptado) {
            indice_invertido = indice;
            System.out.println("é avl");
        }else{
            System.out.println("ué");
        }
    }

//    public void construir(ArrayList<String> dataset) {
//        // Para cada elemento (string) que estiver no dataset
//        /*
//         * 1. Todos os caracteres da string deverão ser minúsculos
//         * 2. Não devem haver caracteres especiais na string
//         * */
//        for (int i = 0; i < dataset.size(); i++) {
//            String conteudo;
//            conteudo = dataset.get(i).toLowerCase(Locale.ROOT); // (1)
//            conteudo = conteudo.replaceAll("[-.,+=*&:®…•\u00AD–—’;%$#@|!?_\"\']", ""); // (2)
//            conteudo = conteudo.replaceAll(" ", " "); // (2)
//            conteudo = conteudo.replaceAll("/", " "); // (2)
//
//            // Identificar todos os termos e adicioná-los na lista de termos
//            identificarTermos(conteudo, i);
//        }
//    }
//
//    private void identificarTermos(String frase, int id_atual) {
//        String termos[] = frase.split(" ");
//        //System.out.println(frase);
//
//        for (String termo : termos) {
//            // Caso o termo atual não tenha 10 termos ou mais passa para o próximo
//            if (!(termo.length() >= 10)) continue;
//
//            //System.out.println("Termo: {"+termo+"} / Tamanho: "+termo.length());
//            if (this.indice_invertido.containsKey(termo)) {
//                // Se termo ja existe na lista
//                /*
//                 * Percorrer lista dentro da chave para tentar encontrar ID existente
//                 * */
//                ArrayList<ParOcorrenciasID> lista = indice_invertido.get(termo);
//
//                // Verificar se ID já existe na lista associada ao termo
//                /*
//                 * 1. Caso exista, a variável de ocorrências do ID será incrementada em mais 1
//                 * 2. Caso não exista, será criado um novo par ocorrência/ID e será adicionado na lista de pares do termo
//                 * */
//                boolean existe_na_lista = false;
//                int indice_ID = 0;
//                int iteracao = 0;
//                for (ParOcorrenciasID item : lista) {
//                    if ((Integer) item.getIDProduto() == id_atual) {
//                        existe_na_lista = true;
//                        indice_ID = iteracao;
//                        break;
//                    }
//                    iteracao++;
//                }
//
//                if (existe_na_lista) {
//                    lista.get(indice_ID).incrementarOcorrencias(); // (Caso 1)
//                } else {
//                    lista.add(new ParOcorrenciasID(1, id_atual)); // (Caso 2)
//                }
//
//            } else {
//                // Caso o termo ainda não exista na lista
//                /*
//                 * 1. Cria uma lista de pares para o termo
//                 * 2. Adiciona o novo termo na lista e associa a ele o par atual
//                 * */
//                ArrayList<ParOcorrenciasID> ParesOcorrenciasID = null;
//
//                this.termos += termo + " ";
//                ParesOcorrenciasID = new ArrayList<>();
//                ParesOcorrenciasID.add(new ParOcorrenciasID(1, id_atual)); // (Passo 1)
//                this.indice_invertido.put(termo, ParesOcorrenciasID); // (Passo 2)
//            }
//        }
//    }
//
//    public void printarEstrutura() { // Verificar a estrutura
//        for (String key : this.indice_invertido.keySet()) {
//            System.out.print("{" + key + "}");
//            ArrayList<ParOcorrenciasID> lista = this.indice_invertido.get(key);
//
//            for (ParOcorrenciasID item : lista) {
//                int id = (Integer) item.getIDProduto();
//                System.out.print(" -> ");
//                System.out.print("(" + item.getOcorrencias() + ", " + id + ")");
//            }
//            System.out.println();
//        }
//    }
//
//    public String getTermos() {
//        return termos;
//    }
//
//    public Map<String, ArrayList<ParOcorrenciasID>> getIndiceInvertido() {
//        return indice_invertido;
//    }
}
