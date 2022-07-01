

import java.util.Comparator;

public class ParOcorrenciasID <Integer, N> /*implements Comparable<Generic>*/{
    private int qtd_ocorrencias;
    private N id_produto;

    //Método construtor que inicializa a chave e o valor do elemento ao instanciar
    public ParOcorrenciasID(int qtd_ocorrencias, N id_produto){
        this.qtd_ocorrencias = qtd_ocorrencias;
        this.id_produto = id_produto;
    }

    public int getOcorrencias() {
        return this.qtd_ocorrencias;
    }

    public N getIDProduto() {
        return this.id_produto;
    }

    public void incrementarOcorrencias(){
        this.qtd_ocorrencias += 1;
    }
    /*
    //Metodo para fazer a comparação das estruturas Generics através de castings
    public int comparator(Generic<?,?> valorChave){
        if(this.valor instanceof Integer){
            return((Integer) this.valor).compareTo((Integer) valorChave.getValor());
        }else if(this.valor instanceof String){
            return((String) this.valor).compareTo((String) valorChave.getValor());
        }else{
            return((Double) this.valor).compareTo((Double) valorChave.getValor());
        }
    }

    //Metodo para fazer a comparação das estruturas Generics através de castings
    @Override
    public int compareTo(Generic o) {
        if(this.valor instanceof Integer){
            return((Integer) this.valor).compareTo((Integer) o.getValor());
        }else if(this.valor instanceof String){
            return((String) this.valor).compareTo((String) o.getValor());
        }else{
            return((Double) this.valor).compareTo((Double) o.getValor());
        }
    }
    */

}