import java.util.*;

public class IndiceInvertidoGeral {
    private Object indice_invertido;
    private String termos;

    public IndiceInvertidoGeral(Object indice_invertido) {
        this.indice_invertido = indice_invertido;
        termos = "";
    }

    public void construir(ArrayList<String> dataset) {
        // Para cada elemento (string) que estiver no dataset
        /*
         * 1. Todos os caracteres da string deverão ser minúsculos
         * 2. Não devem haver caracteres especiais na string
         * */
        for (int i = 0; i < dataset.size(); i++) {
            String conteudo;
            conteudo = dataset.get(i).toLowerCase(Locale.ROOT); // (1)
            conteudo = conteudo.replaceAll("[-.,+=*&:®…•\u00AD–—’;%$#@|!?_\"\']", ""); // (2)
            conteudo = conteudo.replaceAll(" ", " "); // (2)
            conteudo = conteudo.replaceAll("/", " "); // (2)

            // Identificar todos os termos e adicioná-los na lista de termos
            identificarTermos(conteudo, i);
        }
    }

    private void identificarTermos(String frase, int id_atual) {
        String termos[] = frase.split(" ");

        for (String termo : termos) {
            // Caso o termo atual não tenha X termos ou mais passa para o próximo
            if (!(termo.length() >= 1)) continue;

            if (indexContainsKey(termo)) {
                // Se termo ja existe na lista
                /*
                 * Percorrer lista dentro da chave para tentar encontrar ID existente
                 * */
                ArrayList<ParOcorrenciasID> lista = getListPairs(termo);

                // Verificar se ID já existe na lista associada ao termo
                /*
                 * 1. Caso exista, a variável de ocorrências do ID será incrementada em mais 1
                 * 2. Caso não exista, será criado um novo par ocorrência/ID e será adicionado na lista de pares do termo
                 * */
                boolean existe_na_lista = false;
                int indice_ID = 0;
                int iteracao = 0;
                for (ParOcorrenciasID item : lista) {
                    if ((Integer) item.getIDProduto() == id_atual) {
                        existe_na_lista = true;
                        indice_ID = iteracao;
                        break;
                    }
                    iteracao++;
                }

                if (existe_na_lista) {
                    lista.get(indice_ID).incrementarOcorrencias(); // (Caso 1)
                } else {
                    lista.add(new ParOcorrenciasID(1, id_atual)); // (Caso 2)
                }

            } else {
                // Caso o termo ainda não exista na lista
                /*
                 * 1. Cria uma lista de pares para o termo
                 * 2. Adiciona o novo termo na lista e associa a ele o par atual
                 * */
                ArrayList<ParOcorrenciasID> ParesOcorrenciasID = null;

                this.termos += termo + " ";
                ParesOcorrenciasID = new ArrayList<>();
                ParesOcorrenciasID.add(new ParOcorrenciasID(1, id_atual)); // (Passo 1)
                insertTerm(termo, ParesOcorrenciasID); // (Passo 2)
            }
        }
    }

    public void printarEstrutura() { // Verificar a estrutura
        for (String key : getKeys()) {
            System.out.print("{" + key + "}");
            ArrayList<ParOcorrenciasID> lista = getListPairs(key);

            for (ParOcorrenciasID item : lista) {
                int id = (Integer) item.getIDProduto();
                System.out.print(" -> ");
                System.out.print("(" + item.getOcorrencias() + ", " + id + ")");
            }
            System.out.println();
        }
    }

    public String getTermos() {
        return termos;
    }

    public Object getIndiceInvertido() {
        return this.indice_invertido;
    }

    private ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList<>();
        if (indice_invertido instanceof Map) {
            Map<String, ArrayList<ParOcorrenciasID>> indice = (Map) this.indice_invertido;
            for(String key : indice.keySet()) keys.add(key);
            return keys;
        } else if (indice_invertido instanceof AVLAdaptado) {
            AVLAdaptado indice = (AVLAdaptado) this.indice_invertido;
            return indice.getKeys();
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            RBTreeAdaptado indice = (RBTreeAdaptado) this.indice_invertido;
            return indice.getKeys();
        } else {
            System.out.println("[getKeys] Não identificou nenhum tipo");
            return null;
        }
    }

    private boolean indexContainsKey(String termo) {
        if (indice_invertido instanceof Map) {
            Map<String, ArrayList<ParOcorrenciasID>> indice = (Map) this.indice_invertido;
            if ((indice.containsKey(termo))) return true;
            else return false;
        } else if (indice_invertido instanceof AVLAdaptado) {
            AVLAdaptado indice = (AVLAdaptado) this.indice_invertido;
            if ((indice.busca(termo)) != null) return true;
            else return false;
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            RBTreeAdaptado indice = (RBTreeAdaptado) this.indice_invertido;
            if ((indice.busca(termo)) != indice.getTNULL()) return true;
            else return false;
        } else {
            System.out.println("[indexContainsKey] Não identificou nenhum tipo");
            return false;
        }
    }

    private ArrayList<ParOcorrenciasID> getListPairs(String termo) {
        if (indice_invertido instanceof Map) {
            Map<String, ArrayList<ParOcorrenciasID>> indice = (Map) this.indice_invertido;
            return indice.get(termo);
        } else if (indice_invertido instanceof AVLAdaptado) {
            AVLAdaptado indice = (AVLAdaptado) this.indice_invertido;
            return indice.busca(termo).getPares();
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            RBTreeAdaptado indice = (RBTreeAdaptado) this.indice_invertido;
            return indice.busca(termo).getPares();
        } else {
            System.out.println("[getListPairs] Não identificou nenhum tipo");
            return null;
        }
    }

    private void insertTerm(String termo, ArrayList<ParOcorrenciasID> ParesOcorrenciasID) {
        if (indice_invertido instanceof Map) {
            Map<String, ArrayList<ParOcorrenciasID>> indice = (Map) this.indice_invertido;
            indice.put(termo, ParesOcorrenciasID);
        } else if (indice_invertido instanceof AVLAdaptado) {
            AVLAdaptado indice = (AVLAdaptado) this.indice_invertido;
            indice.insere(termo, ParesOcorrenciasID);
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            RBTreeAdaptado indice = (RBTreeAdaptado) this.indice_invertido;
            indice.insere(termo, ParesOcorrenciasID);
        } else {
            System.out.println("[insertTerm] Não identificou nenhum tipo");
        }
    }
}
