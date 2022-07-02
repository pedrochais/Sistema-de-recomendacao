/* 5 Propriedades de uma árvore T Rubro Negra:
* - Cada no em T é preto ou vermelho;
* - O nó raiz de T é preto;
* - Cada null node é preto (null node são os nós folha. Eles não contém nenhuma chave. Quando se procura uma chave que não está presente na árvore, chegou-se ao null node);
* - Se um nó é vermelho, ambos os filhos são pretos. Isso significa que dois nós em um caminho não podem ser nós vermelhos;
* - Cada caminho de um nó raiz para um null node tem o mesmo número de nós pretos.
* */
import java.util.ArrayList;

public class RBTreeAdaptado {
    private ArrayList<String> keys = new ArrayList<>();
    private RBNode raiz;
    private RBNode TNULL;  //Null node da base da árvore

    class RBNode {
        String chave;
        ArrayList<ParOcorrenciasID> pares;
        RBNode pai;
        RBNode left;
        RBNode right;
        int cor; // 1 . Vermelho, 0 . Preto

        public String getChave() {
            return chave;
        }

        public ArrayList<ParOcorrenciasID> getPares() {
            return pares;
        }
    }

    public RBTreeAdaptado() {
        TNULL = new RBNode();
        TNULL.cor = 0;
        TNULL.left = null;
        TNULL.right = null;
        raiz = TNULL;
    }

    public RBNode getTNULL() {
        return TNULL;
    }

    // Pegar todas as chaves contidas na árvore
    public ArrayList<String> getKeys() {
        this.keys.clear();
        preorder();
        return this.keys;
    }

    // Visualização da arvore em pre-ordem (valor + cor)
    public void printPreorder() {
        printPreorder(raiz);
    }

    public void printPreorder(RBNode node) {
        if (node != TNULL) {
            String corNo = node.cor == 1 ? "VERMELHO" : "PRETO";
            System.out.print(node.chave + " (" + corNo + ")   ");
            printPreorder(node.left);
            printPreorder(node.right);
        }
    }

    // Identificar as chaves em pre-ordem
    public void preorder() {
        preorder(raiz);
    }

    private void preorder(RBNode node) {
        if (node != TNULL) {
            this.keys.add(node.chave);
            preorder(node.left);
            preorder(node.right);
        }
    }

    public RBNode busca(String chave) {
        RBNode noAtual = raiz;
        while (noAtual != TNULL) {
            if (noAtual.chave.equals(chave)) { // Usando método equals() para fazer a comparação de strings
                break;
            }

            if ((noAtual.chave).compareTo(chave) < 0) {
                /* Se a chave do nó atual for precede alfabeticamente a chave que está sendo buscada
                 * Busca na subárvore à esquerda
                 * */
                noAtual = noAtual.right;
            } else {
                /* Caso a chave do nó atual sucede alfabeticamente a chave que está sendo buscada
                 * Busca na subárvore à direita
                 * */
                noAtual = noAtual.left;
            }
        }
        if (noAtual != TNULL) {
            //System.out.println("Nó " +noAtual.chave+ " encontrado");
        } else {
            //System.out.println("Nó nao encontrado na arvore");
        }
        return noAtual;
    }

    // Insere a chave da árvore em sua posição apropriada e corrige a árvore com o método ajustaNoAoInserir
    public void insere(String chave, ArrayList<ParOcorrenciasID> pares) {
        RBNode node = new RBNode();
        node.pai = null;
        node.chave = chave;
        node.pares = pares;
        node.left = TNULL;
        node.right = TNULL;
        node.cor = 1; //Novo nó inicialmente vermelho

        RBNode y = null;
        RBNode x = this.raiz;

        while (x != TNULL) {
            y = x;
            if (node.chave.compareTo(x.chave) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        // Se y é pai de x
        node.pai = y;
        if (y == null) {
            raiz = node;
        } else if (node.chave.compareTo(y.chave) < 0) {
            y.left = node;
        } else if (node.chave.compareTo(y.chave) > 0) {
            y.right = node;
        } else {
            //System.out.println("Nó já existe na árvore");
            return;
        }

        // Se novo no é raiz, apenas retorna
        if (node.pai == null) {
            node.cor = 0;
            return;
        }

        // Se avô é null, apenas retorna
        if (node.pai.pai == null) {
            return;
        }

        // Conserta a árvore
        ajustaNoAoInserir(node);
    }

    // Consertando a árvore RN ao inserir um elemento
    private void ajustaNoAoInserir(RBNode k) {
        RBNode tio;
        while (k.pai.cor == 1) {
            if (k.pai == k.pai.pai.right) { // Pai do no a ser inserido é filho direito do avo
                tio = k.pai.pai.left; // Tio é filho esquerdo do avo
                if (tio.cor == 1) { // Caso em questão - Pai e Tio vermelhos: mudar cor de pai, tio e avo.
                    tio.cor = 0;
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    k = k.pai.pai;
                } else {
                    /* Caso em questao - Pai é filho direito de avô e nó K é filho esquerdo do pai:
                    * Rotação dupla a esquerda (rotação a direita no pai + rotação a esquerda no avo)
                    * */
                    if (k == k.pai.left) {
                        k = k.pai;
                        rotacaoDireita(k);
                    }
                    /* Caso em questao - Pai é filho direito de avô e nó K é filho direito do pai:
                    * Rotação a esquerda no avo e recolori pai e avo
                    * */
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    rotacaoEsquerda(k.pai.pai);
                }
            } else {
                tio = k.pai.pai.right; // Tio é filho direito do avo
                if (tio.cor == 1) { // Caso em questão - Pai e Tio vermelhos: mudar cor de pai, tio e avo.
                    tio.cor = 0;
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    k = k.pai.pai;
                } else {
                    /* Caso em questao - Pai é filho direito de avô e nó K é filho esquerdo do pai:
                    * Rotação dupla a esquerda (rotação a direita no pai + rotação a esquerda no avo)
                    * */
                    if (k == k.pai.right) {
                        k = k.pai;
                        rotacaoEsquerda(k);
                    }
                    /* Caso em questao - Pai é filho direito de avô e nó K é filho direito do pai:
                    * Rotação a esquerda no avo e recolori pai e avo
                    * */
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    rotacaoDireita(k.pai.pai);
                }
            }
            if (k == raiz) {
                break;
            }
        }
        raiz.cor = 0;
    }

    // A rotação para a esquerda no nó x faz com que x desça na direção esquerda e, como resultado, seu filho direito sobe
    public void rotacaoEsquerda(RBNode x) {
        RBNode y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            this.raiz = y;
        } else if (x == x.pai.left) {
            x.pai.left = y;
        } else {
            x.pai.right = y;
        }
        y.left = x;
        x.pai = y;
    }

    // A rotação à direita no nó x faz com que x desça na direção certa e, como resultado, seu filho esquerdo sobe.
    public void rotacaoDireita(RBNode x) {
        RBNode y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            this.raiz = y;
        } else if (x == x.pai.right) {
            x.pai.right = y;
        } else {
            x.pai.left = y;
        }
        y.right = x;
        x.pai = y;
    }
}