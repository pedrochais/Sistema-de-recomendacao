import java.util.ArrayList;

public class AVLAdaptado{
    private Node raiz;
    private ArrayList<String> keys = new ArrayList<>();

    public class Node{
        String chave;
        ArrayList<ParOcorrenciasID> pares;
        int altura;
        Node left;
        Node right;

        Node(String chave, ArrayList<ParOcorrenciasID> pares){
            this.chave = chave;
            this.pares = pares;
        }

        public String getChave() {
            return chave;
        }

        public ArrayList<ParOcorrenciasID> getPares() {
            return pares;
        }
    }

    public Node getRaiz(String chave){
        return raiz;
    }

    public ArrayList<String> getKeys() {
        this.keys.clear();
        inorder(raiz);
        return this.keys;
    }

    // Identificar as chaves em ordem
    public void inorder() {
        inorder(raiz);
    }

    private void inorder(Node r) {
        if (r != null) {
            inorder(r.left);
            this.keys.add(r.getChave());
            inorder(r.right);
        }
    }

    public Node busca(String chave){
        Node noAtual = raiz;
        while(noAtual != null){
            if(noAtual.chave.equals(chave)){ // Usando método equals() para fazer a comparação de strings
                break;
            }

            if((noAtual.chave).compareTo(chave) < 0){
                /* Se a chave do nó atual for precede alfabeticamente a chave que está sendo buscada
                * Busca na subárvore à esquerda
                * */
                noAtual = noAtual.right;
            }else{
                /* Caso a chave do nó atual sucede alfabeticamente a chave que está sendo buscada
                * Busca na subárvore à direita
                * */
                noAtual = noAtual.left;
            }
        }

        return noAtual;
    }

    public void insere(String chave, ArrayList<ParOcorrenciasID> pares){
        raiz = insere(raiz, chave, pares);
    }

    private Node insere(Node node, String chave, ArrayList<ParOcorrenciasID> pares){
        if(node == null){ // No sem filho esquerdo e direito -> insere diretamente
            return new Node(chave, pares);
        }else if(((node.chave).compareTo(chave)) > 0){ // Inserção á esquerda
            node.left = insere(node.left, chave, pares);
        }else if(((node.chave).compareTo(chave)) < 0){ // Inserção á direita
            node.right = insere(node.right, chave, pares);
        }else{
            //throw new RuntimeException("Chave ja existe no nó");
            //System.out.println("A chave já existe na árvore");
        }
        return rebalanceamento(node);
    }

    /* Em uma árvore desbalanceada, pelo menos um nó tem fator de balanceamento igual a 2 ou -2.
    * - Quando o fb é 2, a subarvore que tem z como raiz pode estar em um dos seguintes estados, considerando y como filho direito de z
    *     - Caso 1: a altura do filho direito de y (no x)é maior que a altura do filho esquerdo. Assim, podemos balancear a árvore com uma rotação a esquerda do no z
    *     - Caso 2: a altura do filho direito de y é mnor que a altura do filho esquerdo (no x). Essa situação precisa de uma combinação de rotações: gira-se o no y para a direita fazendo a arvore entrar no Caso 1 e faz uma rotação a esquerda em z
    * - Quando o fb é -2
    * */
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

    /* Assumindo que temos uma árvore no qual y é a raiz, x filho esquerdo de y e z filho direito de x, temos x < z < y
    * Após a rotação a direita, temos uma árvore no qual x será a raiz, y o filho direito da raiz e z o filho equerdo de y
    * */
    private Node rotacaoDireita(Node y){
        Node x = y.left;
        Node z = x.right;
        x.right = y;
        y.left = z;
        atualizaAltura(y);
        atualizaAltura(x);
        return x;
    }

    /* Assumindo que temos uma árvore no qual y é a raiz, x filho direito de y e z filho esquerdo de x, temos y < z < x
    * Após a rotação a esquerda, temos uma árvore no qual x será a raiz, y o filho esquerdo da raiz e z o filho direito de y
    * */
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

    private int altura(String chave){
        return raiz == null ? -1 : raiz.altura;
    }

    private int getFB(Node node) {
        return (node== null) ? 0 : altura(node.right) - altura(node.left);
    }
}