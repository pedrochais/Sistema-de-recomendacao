import java.util.*;

public class IndiceInvertidoGeral {
    private Object indice_invertido;
    private String estrutura_nome;
    private boolean indice_construido;
    private String termos;

    public IndiceInvertidoGeral(Object indice_invertido) {
        this.indice_invertido = indice_invertido;
        this.indice_construido = false;
        termos = "";
        if (indice_invertido instanceof Map) {
            this.estrutura_nome = "HashMap";
        } else if (indice_invertido instanceof AVLAdaptado) {
            this.estrutura_nome = "Árvore AVL";
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            this.estrutura_nome = "Árvore Rubro-Negra";
        } else if (indice_invertido instanceof Tree234) {
            this.estrutura_nome = "Árvore 2-3-4";
        } else {
            System.out.println("[IndiceInvertidoGeral] Não identificou nenhum tipo");
        }
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
            conteudo = conteudo.replaceAll(Main.regex, ""); // (2)
            conteudo = conteudo.replaceAll(" ", " "); // (2)
            conteudo = conteudo.replaceAll("/", " "); // (2)

            // Identificar todos os termos e adicioná-los na lista de termos
            identificarTermos(conteudo, i);
        }
        this.indice_construido = true;
    }

    private void identificarTermos(String frase, int id_atual) {
        String termos[] = frase.split(" ");

        for (String termo : termos) {
            // Caso o termo atual não tenha X termos ou mais passa para o próximo
            if (!(termo.length() >= Main.C)) continue;

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

    public void printarEstrutura(ArrayList<String> nomes_produtos, Integer modo) { // Verificar a estrutura
        if (indice_invertido instanceof Map)
            System.out.println("Imprimindo índice invertido do Hash");
        else if (indice_invertido instanceof AVLAdaptado)
            System.out.println("Imprimindo índice invertido da Árvore AVL");
        else if (indice_invertido instanceof RBTreeAdaptado)
            System.out.println("Imprimindo índice invertido da Árvore Rubro-Negra");
        else if (indice_invertido instanceof Tree234)
            System.out.println("Imprimindo índice invertido da Árvore 2-3-4");
        else System.out.println("[printarEstrutura] Não identificou nenhum tipo");

        if (modo == 1) {
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
        } else if (modo == 2) {
            for (String key : getKeys()) {
                System.out.println("[CHAVE]: " + key);
                ArrayList<ParOcorrenciasID> lista = getListPairs(key);

                for (ParOcorrenciasID item : lista) {
                    int id = (Integer) item.getIDProduto();
                    System.out.println("- [PRODUTO]: " + nomes_produtos.get(id));
                    System.out.println("- [OCORRENCIAS]: " + item.getOcorrencias());
                }
                System.out.println();
            }
        }


        System.out.println("Quantidade de termos no índice: " + getKeys().size());
    }

    public void limparIndice() {
        if (indice_invertido instanceof Map) {
            this.indice_invertido = new HashMap<>();
        } else if (indice_invertido instanceof AVLAdaptado) {
            this.indice_invertido = new AVLAdaptado();
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            this.indice_invertido = new RBTreeAdaptado();
        } else if (indice_invertido instanceof Tree234) {
            this.indice_invertido = new Tree234();
        } else {
            System.out.println("[limparIndice] Não identificou nenhum tipo");
        }
    }

    private ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList<>();
        if (indice_invertido instanceof Map) {
            Map<String, ArrayList<ParOcorrenciasID>> indice = (Map) this.indice_invertido;
            for (String key : indice.keySet()) keys.add(key);
            Collections.sort(keys, Comparator.naturalOrder());
            return keys;
        } else if (indice_invertido instanceof AVLAdaptado) {
            AVLAdaptado indice = (AVLAdaptado) this.indice_invertido;
            return indice.getKeys();
        } else if (indice_invertido instanceof RBTreeAdaptado) {
            RBTreeAdaptado indice = (RBTreeAdaptado) this.indice_invertido;
            return indice.getKeys();
        } else if (indice_invertido instanceof Tree234) {
            Tree234 indice = (Tree234) this.indice_invertido;
            return indice.getKeysList();
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
        } else if (indice_invertido instanceof Tree234) {
            Tree234 indice = (Tree234) this.indice_invertido;
            if ((indice.busca(termo)) != -1) return true;
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
        } else if (indice_invertido instanceof Tree234) {
            Tree234 indice = (Tree234) this.indice_invertido;
            if (indice.busca(termo) != -1) {
                return indice.getNoEncontrado().getItemEncontrado().getPares();
            } else {
                return null;
            }
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
        } else if (indice_invertido instanceof Tree234) {
            Tree234 indice = (Tree234) this.indice_invertido;
            indice.insert(termo, ParesOcorrenciasID);
        } else {
            System.out.println("[insertTerm] Não identificou nenhum tipo");
        }
    }

    public String getEstruturaNome() {
        return estrutura_nome;
    }

    public boolean getIndiceConstruido() {
        return indice_construido;
    }

    public String getTermos() {
        return termos;
    }

    public Object getIndiceInvertido() {
        return this.indice_invertido;
    }

    public void setIndiceConstruído(boolean bool) {
        this.indice_construido = bool;
    }
}
