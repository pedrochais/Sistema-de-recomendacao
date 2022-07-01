import java.util.ArrayList;

public class AVLAdaptado{
    private int quantidade_nos = 0;
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

    private Node raiz;

    public Node busca(String chave){
        Node noAtual = raiz;
        while(noAtual != null){
            if(noAtual.chave.equals(chave)){ // Usando método equals() para fazer a comparação de strings
                break;
            }
            //noAtual = noAtual.chave < chave ? noAtual.right : noAtual.left;
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
        if(noAtual != null){
            //System.out.println("Nó " +noAtual.chave+ " encontrado");
        }else{
            //System.out.println("Nó nao encontrado na arvore");
        }
        return noAtual;
    }

    public void insere(String chave, ArrayList<ParOcorrenciasID> pares){
        raiz = insere(raiz, chave, pares);
    }

    public Node pegaRaiz(String chave){
        return raiz;
    }

    private int altura(String chave){
        return raiz == null ? -1 : raiz.altura;
    }

    public void preorder(){
        preorder(raiz);
    }

//    private void printPreorder(Node r){ // Aqui é onde a árvore vai ser printada
//        if (r != null){
//
//            //System.out.print("{"+r.chave+"}");
//
//            for (ParOcorrenciasID item : r.getPares()) {
//                int id = (Integer) item.getIDProduto();
//                System.out.print(" -> ");
//                System.out.print("(" + item.getOcorrencias() + ", " + id + ")");
//            }
//            System.out.println();
//
//
//            printPreorder(r.left);
//            printPreorder(r.right);
//        }
//    }

    private void preorder(Node r){ // Aqui é onde a árvore vai ser printada
        if (r != null){
            this.keys.add(r.getChave());

            preorder(r.left);
            preorder(r.right);
        }
    }

//    private ArrayList<String> insertKeysList(Node r){
//        if (r != null){
//            this.keys.add(r.getChave());
//
//            preorder(r.left);
//            preorder(r.right);
//        }
//        return keys;
//    }

    public ArrayList<String> getKeys() {
        this.keys.clear();
        preorder(pegaRaiz(""));
        return this.keys;
    }

    private Node insere(Node node, String chave, ArrayList<ParOcorrenciasID> pares){
        if(node == null){                                           //No sem filho esquerdo e direito -> insere diretamente
            this.quantidade_nos++;
            return new Node(chave, pares);
        }else if(((node.chave).compareTo(chave)) > 0){ //Inserção á esquerda
            node.left = insere(node.left, chave, pares);
            this.quantidade_nos++;
        }else if(((node.chave).compareTo(chave)) < 0){ //Inserção á direita
            node.right = insere(node.right, chave, pares);
            this.quantidade_nos++;
        }else{
            //throw new RuntimeException("Chave ja existe no nó");
            //System.out.println("A chave já existe na árvore");
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

    public int getQuantidadeNos() {
        return quantidade_nos;
    }
}