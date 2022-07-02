package Descartados;/*
    5 Propriedades de uma árvore T Rubro Negra:
    - Cada no em T é preto ou vermelho;
    - O nó raiz de T é preto;
    - Cada null node é preto (null node são os nós folha. Eles não contém nenhuma chave. Quando se procura uma chave
        que não está presente na árvore, chegou-se ao null node);
    - Se um nó é vermelho, ambos os filhos são pretos. Isso significa que dois nós em um caminho não podem ser nós vermelhos;
    - Cada caminho de um nó raiz para um null node tem o mesmo número de nós pretos.
 */

public class RBTree {
    class RBNode {
        int dado;
        RBNode pai;
        RBNode left;
        RBNode right;
        int cor; // 1 . Vermelho, 0 . Preto
    }

    public RBTree() {       //Definindo null node sempre com cor preta
        TNULL = new RBNode();
        TNULL.cor = 0;
        TNULL.left = null;
        TNULL.right = null;
        raiz = TNULL;
    }

    private RBNode raiz;
    private RBNode TNULL;  //Null node da base da árvore

    public RBNode getTNULL() {
        return TNULL;
    }

    // Pre-Ordem
    public void preorder() {
        preorder(raiz);
    }

    private void preorder(RBNode node) {        //Visualização da arvore em pre-ordem (valor + cor)
        if (node != TNULL) {
            String corNo = node.cor == 1 ? "VERMELHO" : "PRETO";
            System.out.print(node.dado + " (" + corNo + ")   ");
            preorder(node.left);
            preorder(node.right);
        }
    }

    // --- BUSCA ---
    public RBNode busca(int chave){
        RBNode noAtual = raiz;
        while(noAtual != null){
            if(noAtual.dado == chave){
                break;
            }
            noAtual = noAtual.dado < chave ? noAtual.right : noAtual.left;
        }
        if(noAtual != null){
            System.out.println("No " +noAtual.dado+ " encontrado");
        }else{
            System.out.println("No nao encontrado na arvore");
        }
        return noAtual;
    }


    /*
        --- INSERÇÃO ---
        - Insere a chave da árvore em sua posição apropriada e corrige a árvore com o método ajustaNoAoInserir
     */
    public void insere(int chave) {
        RBNode node = new RBNode();
        node.pai = null;
        node.dado = chave;
        node.left = TNULL;
        node.right = TNULL;
        node.cor = 1;       //Novo nó inicialmente vermelho

        RBNode y = null;
        RBNode x = this.raiz;

        while (x != TNULL) {
            y = x;
            if (node.dado < x.dado) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        // Se y é pai de x
        node.pai = y;
        if (y == null) {
            raiz = node;
        } else if (node.dado < y.dado) {
            y.left = node;
        } else if (node.dado > y.dado) {
            y.right = node;
        } else {
            System.out.println("Nó não encontrado");
            return;
        }

        // Se novo no é raiz, apenas retorna
        if (node.pai == null){
            node.cor = 0;
            return;
        }

        // Se avô null, apenas retorna
        if (node.pai.pai == null) {
            return;
        }

        // Conserta a árvore
        ajustaNoAoInserir(node);
    }


    // Consertando a árvore RN ao inserir um elemento
    private void ajustaNoAoInserir(RBNode k){
        RBNode tio;
        while (k.pai.cor == 1) {
            if (k.pai == k.pai.pai.right) {     //Pai do no a ser inserido é filho direito do avo
                tio = k.pai.pai.left;           //Tio é filho esquerdo do avo
                if (tio.cor == 1) {             //Caso em questão - Pai e Tio vermelhos: mudar cor de pai, tio e avo.
                    tio.cor = 0;
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    k = k.pai.pai;
                } else {
                    /*
                        Caso em questao - Pai é filho direito de avô e nó K é filho esquerdo do pai:
                        Rotação dupla a esquerda (rotação a direita no pai + rotação a esquerda no avo)

                     */
                    if (k == k.pai.left) {
                        k = k.pai;
                        rightRotate(k);
                    }
                    /*
                        Caso em questao - Pai é filho direito de avô e nó K é filho direito do pai:
                        Rotação a esquerda no avo e recolori pai e avo

                     */
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    rotacaoEsquerda(k.pai.pai);
                }
            } else {
                tio = k.pai.pai.right;          //Tio é filho direito do avo
                if (tio.cor == 1) {             //Caso em questão - Pai e Tio vermelhos: mudar cor de pai, tio e avo.
                    tio.cor = 0;
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    k = k.pai.pai;
                } else {
                    /*
                        Caso em questao - Pai é filho direito de avô e nó K é filho esquerdo do pai:
                        Rotação dupla a esquerda (rotação a direita no pai + rotação a esquerda no avo)

                     */
                    if (k == k.pai.right) {
                        k = k.pai;
                        rotacaoEsquerda(k);
                    }
                    /*
                        Caso em questao - Pai é filho direito de avô e nó K é filho direito do pai:
                        Rotação a esquerda no avo e recolori pai e avo

                     */
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    rightRotate(k.pai.pai);
                }
            }
            if (k == raiz) {
                break;
            }
        }
        raiz.cor = 0;
    }

    /*
        A rotação para a esquerda no nó x faz com que x desça na direção esquerda e, como resultado, seu filho direito sobe
     */
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

    /*
        A rotação à direita no nó x faz com que x desça na direção certa e, como resultado, seu filho esquerdo sobe.
     */
    public void rightRotate(RBNode x) {
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