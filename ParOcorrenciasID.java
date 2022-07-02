public class ParOcorrenciasID<Integer, N> {
    private int qtd_ocorrencias;
    private N id_produto;

    // Método construtor que inicializa a chave e o valor do elemento ao instanciar
    public ParOcorrenciasID(int qtd_ocorrencias, N id_produto) {
        this.qtd_ocorrencias = qtd_ocorrencias;
        this.id_produto = id_produto;
    }

    public int getOcorrencias() {
        return this.qtd_ocorrencias;
    }

    public N getIDProduto() {
        return this.id_produto;
    }

    public void incrementarOcorrencias() {
        this.qtd_ocorrencias += 1;
    }
}