package Descartados;

public class AVL{
    private class Node{
        int chave;
        int altura;
        Node left;
        Node right;

        Node(int chave){
            this.chave = chave;
        }
    }

    private Node raiz;

    public void imprimir(){
        preorder(pegaRaiz(1));
    }

    public Node busca(int chave){
        Node noAtual = raiz;
        while(noAtual != null){
            if(noAtual.chave == chave){
                break;
            }
            noAtual = noAtual.chave < chave ? noAtual.right : noAtual.left;
        }
        if(noAtual != null){
            System.out.println("No " +noAtual.chave+ " encontrado");
        }else{
            System.out.println("No nao encontrado na arvore");
        }
        return noAtual;
    }

    public void insere(int chave){
        raiz = insere(raiz, chave);
    }

    private Node pegaRaiz(int chave){
        return raiz;
    }

    private int altura(int chave){
        return raiz == null ? -1 : raiz.altura;
    }

    private void preorder(){
        preorder(raiz);
    }

    private void preorder(Node r){
        if (r != null){
            System.out.print(r.chave +" ");
            preorder(r.left);
            preorder(r.right);
        }
    }

    private Node insere(Node node, int chave){
        if(node == null){                                           //No sem filho esquerdo e direito -> insere diretamente
            return new Node(chave);
        }else if(node.chave > chave){                               //Inserção á esquerda
            node.left = insere(node.left, chave);
        }else if(node.chave < chave){                               //Inserção á esquerda
            node.right = insere(node.right, chave);
        }else{
            throw new RuntimeException("Chave ja existe no no");
        }
        return rebalanceamento(node);
    }

    /*
        Em uma árvore desbalanceada, pelo menos um nó tem fator de balanceamento igual a 2 ou -2.
        - Quando o fb é 2, a subarvore que tem z como raiz pode estar em um dos seguintes estados, considerando y como filho direito de z

            - Caso 1: a altura do filho direito de y (no x)é maior que a altura do filho esquerdo. Assim, podemos balancear a árvore com uma rotação a esquerda do no z
            - Caso 2: a altura do filho direito de y é mnor que a altura do filho esquerdo (no x). Essa situação precisa de uma combinação de rotações:
                        gira-se o no y para a direita fazendo a arvore entrar no Caso 1 e faz uma rotação a esquerda em z

         - Quando o fb é -2
     */
    private Node rebalanceamento(Node z) {
        atualizaAltura(z);
        int fb = getFB(z);
        if (fb > 1) {
            if (altura(z.right.right) > altura(z.right.left)) {
                z = rotacaoEsquerda(z);
            } else {
                z.right = rotacaoDireita(z.right);
                z = rotacaoEsquerda(z);
            }
        } else if (fb < -1) {
            if (altura(z.left.left) > altura(z.left.right)) {
                z = rotacaoDireita(z);
            } else {
                z.left = rotacaoEsquerda(z.left);
                z = rotacaoDireita(z);
            }
        }
        return z;
    }

    /*
        Assumindo que temos uma árvore no qual y é a raiz, x filho esquerdo de y e z filho direito de x, temos x < z < y
        Após a rotação a direita, temos uma árvore no qual x será a raiz, y o filho direito da raiz e z o filho equerdo de y
     */
    private Node rotacaoDireita(Node y){
        Node x = y.left;
        Node z = x.right;
        x.right = y;
        y.left = z;
        atualizaAltura(y);
        atualizaAltura(x);
        return x;
    }

    /*
    Assumindo que temos uma árvore no qual y é a raiz, x filho direito de y e z filho esquerdo de x, temos y < z < x
    Após a rotação a esquerda, temos uma árvore no qual x será a raiz, y o filho esquerdo da raiz e z o filho direito de y
 */
    private Node rotacaoEsquerda(Node y){
        Node x = y.right;
        Node z = x.left;
        x.left = y;
        y.right = z;
        atualizaAltura(y);
        atualizaAltura(x);
        return x;
    }

    private void atualizaAltura(Node node) {
        node.altura = 1 + Math.max(altura(node.left), altura(node.right));
    }

    private int altura(Node node) {
        return node == null ? -1 : node.altura;
    }

    private int getFB(Node node) {
        return (node== null) ? 0 : altura(node.right) - altura(node.left);
    }
}