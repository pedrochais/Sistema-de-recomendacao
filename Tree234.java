import java.util.ArrayList;

public class Tree234 {
    /**
     * Uma árvore 2-3-4 é uma árvore que possui:
     * Filhos: min = 2; max = 2t = 4;
     * Chaves: min = t - 1 = 2 - 1 = 1; max = 2t - 1 = 4 - 1 = 3
     */

    // ---- CLASSE PARA UM NÓ DA ÁRVORE ----
    class BTreeNode {
        private static final int ORDER = 4;   //grau maximo
        private int numItems;   //Nº de itens (dados) que um nó possui
        private BTreeNode pai;  //Nó pai

        private BTreeNode arrayFilhos[] = new BTreeNode[ORDER];  //Array para armazenar os nós filhos (no máximo 4)
        private dadoItem arrayItens[] = new dadoItem[ORDER - 1];   //Array para armazenar os itens (dados) (um nó pode ter até 3 itens)

        // MÉTODOS USADOS NO PROCESSO DE SPLIT E REORGANIZAÇÃO DOS DADOS NA ÁRVORE AO USAR A INSERÇÃO DE UM DADO

        // Conectando nós filhos (split)
        public void conectaFilho(int posicaoFilho, BTreeNode filho) {
            arrayFilhos[posicaoFilho] = filho;
            if (filho != null) {
                filho.pai = this;
            }
        }

        // Desconecta filho do no cheio e retorna o nó filho de forma temporaria (split)
        public BTreeNode desconectaFilho(int posicaoFilho) {
            BTreeNode noTemporario = arrayFilhos[posicaoFilho];
            arrayFilhos[posicaoFilho] = null;
            return noTemporario;
        }

        // Pega o nó filho
        public BTreeNode pegaFilho(int posicaoFilho) {
            return arrayFilhos[posicaoFilho];
        }

        // Pega o nó pai
        public BTreeNode pegaPai() {
            return pai;
        }

        // Determina se o nó é folha
        public boolean eFolha() {
            if (arrayFilhos[0] == null) {
                return true;
            } else {
                return false;
            }
        }

        // Pega a quantidade de itens (dados) em um nó
        public int qtdItens() {
            return numItems;
        }

        // Pega um item (dados) do nó
        public dadoItem pegaItem(int i) {
            return arrayItens[i];
        }

        // Verificando se o nó está totalmente preenchido
        public boolean estaCheio() {
            if (numItems == ORDER - 1) {
                return true;
            } else {
                return false;
            }
        }

        public dadoItem itemEncontrado;

        public dadoItem getItemEncontrado() {
            return itemEncontrado;
        }

        //Encontrando a localização do item (dado) em um nó - usado no metodo de busca
        public int encontraItem(String chave) {
            for (int j = 0; j < ORDER - 1; j++) {
                if (arrayItens[j] == null) {          //Posição do nó tiver vazia
                    break;
                } else if (arrayItens[j].chave.equals(chave)) {    //na posição do no tiver um elemento com o mesmo valor que ta sendo buscado, retorna a posicao
                    this.itemEncontrado = arrayItens[j];
                    return j;
                }
            }
            return -1;      //Não achou nenhum elemento com o valor procurado
        }

        // insere o item no nó - usado no metodo de inserção
        public int insereItem(dadoItem item) {
            numItems++;
            String chave = item.chave;
            for (int j = ORDER - 2; j >= 0; j--) {
                if (arrayItens[j] == null) {
                    continue;
                } else {
                    String ValorExistenteNo = arrayItens[j].chave;     //Colocando o item já existente no nó em uma variável para comparar com o valor a ser inserido no nó
                    if (chave.compareTo(ValorExistenteNo) < 0) {               // Se for maior que o item recém inserido
                        arrayItens[j + 1] = arrayItens[j];            // Ordena os dados no nó
                    } else if (chave.compareTo(ValorExistenteNo) > 0) {
                        arrayItens[j + 1] = item;                     // Se for menor que o valor já existente no nó, apenas insere
                        return j + 1;                                 //Retorna a posição do nó o qual o dado foi inserido
                    } else {
                        System.out.println("Nó já existe");
                    }
                }
            }

            /*
                Se o nó estiver completamente vazio ou os outros itens já existentes no nó forem maiores
                que o item de dado a ser inserido , Coloca o item de dados a ser inserido na primeira posição do nó
            */

            arrayItens[0] = item;
            return 0;
        }

        // Removendo item  de um nó (split)
        public dadoItem removeItem() {
            dadoItem temp = arrayItens[numItems - 1];
            arrayItens[numItems - 1] = null;
            numItems--;
            return temp;
        }

        //MÉTODO USADO PARA O PROCESSO DE IMPRESSÃO DA ÁRVORE EM TELA

        // Imprime todos os dados de um nó
        public void imprimeNo() {
            for (int j = 0; j < numItems; j++) {
                arrayItens[j].displayItem();
            }
            System.out.println("imprimiu todo mundo no nó");
            System.out.println("/");
        }

        public ArrayList<String> getKeysNode() { // Pega todos os itens do nó e coloca em uma lista para retornar
            ArrayList<String> keysNode = new ArrayList<>();
            for (int j = 0; j < numItems; j++) {
                keysNode.add(arrayItens[j].chave);
            }
            return keysNode;
        }
    }

    // ---- CLASSE PARA ITEM (DADO) ----
    class dadoItem {
        public String chave;
        public ArrayList<ParOcorrenciasID> pares;

        public dadoItem(String chave, ArrayList<ParOcorrenciasID> pares) {
            this.chave = chave;
            this.pares = pares;
        }

        public ArrayList<ParOcorrenciasID> getPares() {
            return pares;
        }

        public String getChave() {
            return chave;
        }

        public void displayItem() {
            System.out.print("/" + chave);
            for (ParOcorrenciasID item : this.pares) {
                int id = (Integer) item.getIDProduto();
                System.out.print(" -> ");
                System.out.print("(" + item.getOcorrencias() + ", " + id + ")");
            }
            System.out.println();
        }
    }


    /*  LOCALIZANDO O NÓ QUE VAI SER INSERIDO O DADO
        Pega o proximo filho do no atual dado o valor a ser inserido:
        - Se o valor for menor que o valor que esta no no atual, pega o primeiro filho do no (nó mais a esquerda)
        - Se o valor for maior que valor que esta no no atual, passa para o proximo filho
     */
    public BTreeNode pegaProximoFilho(BTreeNode noAtual, String valor) {
        int j;
        int numItems = noAtual.qtdItens();
        for (j = 0; j < numItems; j++) {
            if (valor.compareTo(noAtual.pegaItem(j).chave) < 0) {
                return noAtual.pegaFilho(j);
            }
        }
        return noAtual.pegaFilho(j);
    }


    // --- PROCESSO DE SPLIT FEITO QUANDO FOR INSERIR UM ITEM EM UM NÓ QUE JÁ ESTÁ CHEIO ---

    public void split(BTreeNode noCheio) {
        dadoItem itemPenultimo, itemUltimo;           //Dados do nó cheio que serão retirados do nó (último e penúltimo)
        BTreeNode pai, filho2, filho3;
        int itemIndex;

        //Pegando último e penultimo dados do nó cheio. Ao pegar o item, o local que ele estava no nó passa a apontar para null
        itemUltimo = noCheio.removeItem();
        itemPenultimo = noCheio.removeItem();

        //Desconectando os filhos do nó cheio
        filho2 = noCheio.desconectaFilho(2);
        filho3 = noCheio.desconectaFilho(3);

        BTreeNode novoNoDireita = new BTreeNode();         // Novo nó que ficará a direita do pai com os itens do nó que foi splitado. Esse nó será filho direito do pai ao fazer o split
        if (noCheio == root) {                               // Se o nó cheio for raiz, fazer o split da raiz
            root = new BTreeNode();                        // Raiz no vazio
            pai = root;                                    // no vazio passa a ser o novo pai
            root.conectaFilho(0, noCheio);      // Conecta como filho primeiro da nova raiz (vazia) o nó que estava cheio
        } else {
            pai = noCheio.pegaPai();
        }

        itemIndex = pai.insereItem(itemPenultimo);      // Insere no novo pai o penultimo item do no que foi splitado e pega o indice em que esse dado está colocado no novo pai
        int n = pai.qtdItens();                         // Quantidade de itens já inseridos no novo pai
        for (int j = n - 1; j > itemIndex; j--) {
            BTreeNode temp = pai.desconectaFilho(j);
            pai.conectaFilho(j + 1, temp);
        }
        pai.conectaFilho(itemIndex + 1, novoNoDireita);   //Coloca o novoNoDireita na posicao itemIndex+1 e filhos anteriores permanecem

        novoNoDireita.insereItem(itemUltimo);
        novoNoDireita.conectaFilho(0, filho2);
        novoNoDireita.conectaFilho(1, filho3);
    }

    // ---- TAD DA ÁRVORE ----

    private BTreeNode root = new BTreeNode();  //Definindo a raiz

    public BTreeNode noEncontrado;

    // BUSCA  - EXIBE EM TELA A POSIÇÃO QUE O DADO FOI ENCONTRADO EM UM NO DA ÁRVORE
    public int busca(String chave) {
        BTreeNode noAtual = root;
        int posItemEmNo;           //posicao que o item está localizado no nó em que ele foi encontrado
        while (true) {
            if ((posItemEmNo = noAtual.encontraItem(chave)) != -1) {    //Se encontrar um item em algum nó da árvore que seja igual a chave de busca passada
                noEncontrado = noAtual;
                return posItemEmNo;
            } else if (noAtual.eFolha()) {     //Caso ele chegue em um nó folha mais a esquerda
                return -1;
            } else {
                noAtual = pegaProximoFilho(noAtual, chave);    //Pegando o filho do nó atual que tenha o valor da chave
            }
        }
    }

    // --- INSERÇÃO ---
    public void insert(String chave, ArrayList<ParOcorrenciasID> pares) {
        BTreeNode noAtual = root;                               // Partindo da raiz
        dadoItem itemTemp = new dadoItem(chave, pares);                // Variável temporária que armazena o valor a ser inserido em um no da árvore
        while (true) {
            if (noAtual.estaCheio()) {                            // Se o nó estiver cheio, então deve-se fazer o split
                split(noAtual);
                noAtual = noAtual.pegaPai();                    //Pega o pai do no atual
                noAtual = pegaProximoFilho(noAtual, chave);     //Pega o nó filho do pai do no atual que mais se adequa a ter o valor que vai ser inserido,
            } else if (noAtual.eFolha()) {                         // Se nó atual for folha sai do while e insere o item no no atual
                break;
            } else {
                noAtual = pegaProximoFilho(noAtual, chave);
            }
        }
        noAtual.insereItem(itemTemp);                          // Insere item no nó atual
    }


    // --- IMPRESSÃO ---
    public void mostraArvore() {
        mostraNo(root, 0, 0);
    }

    private void mostraNo(BTreeNode thisNode, int nivel, int childNumber) {        //Impressão de cada nó com seu nível na árvore e quantidade de filhos que ele possui
        System.out.println("nivel = " + nivel + " filho = " + childNumber + " ");
        thisNode.imprimeNo();
        int numItems = thisNode.qtdItens();
        for (int j = 0; j < numItems + 1; j++) {
            BTreeNode nextNode = thisNode.pegaFilho(j);

            if (nextNode != null) {
                mostraNo(nextNode, nivel + 1, j);

            } else {
                return;
            }
        }
    }



    private ArrayList<String> keys = new ArrayList<>();

    public void getKeys() {
        keys.clear();
        getKeys(root, 0, 0);
    }


    private void getKeys(BTreeNode thisNode, int nivel, int childNumber) {        //Impressão de cada nó com seu nível na árvore e quantidade de filhos que ele possui
        for (String k : thisNode.getKeysNode()) {// Pega cada item do nó e joga na lista principal 'keys'
            this.keys.add(k);
        }

        int numItems = thisNode.qtdItens();
        for (int j = 0; j < numItems + 1; j++) {
            BTreeNode nextNode = thisNode.pegaFilho(j);

            if (nextNode != null) {
                getKeys(nextNode, nivel + 1, j);
            } else {
                return;
            }
        }
    }

    public ArrayList<String> getKeysList() {
        getKeys();
        return this.keys;
    }

    public BTreeNode getNoEncontrado() {
        return noEncontrado;
    }
}