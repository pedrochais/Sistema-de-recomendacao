public class Termo {
    private String termo;
    private int quantidade_ocorrencias;
    private int id_produto;

    public String getTermo() {
        return termo;
    }

    public int getQuantidadeOcorrencias() {
        return quantidade_ocorrencias;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public void setQuantidadeOcorrencias(int quantidade_ocorrencias) {
        this.quantidade_ocorrencias = quantidade_ocorrencias;
    }

    public void setIdProduto(int id_produto) {
        this.id_produto = id_produto;
    }

    public int getIdProduto() {
        return id_produto;
    }
}
