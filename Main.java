import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* LEMBRETES
* O programa tá pegando a palavra 'description' na linha 0 (não precisa)
* */

//Passo a passo para adicionar um novo termo
/*
 * Caso o termo já exista no TAD, será adicionado o ID do produto na lista de pares daquele termo
 * Caso o termo ainda não exista no TAD, ele será adicionado no TAD juntamente com o ID do produto na lista de pares
 * */

public class Main {
    public static final int C = 10;

    public static void main(String args[]) {
        IndiceInvertidoGeral i = new IndiceInvertidoGeral();


        RelevanciaV3 relevancia = new RelevanciaV3();
        RelevanciaAVL relevanciaavl = new RelevanciaAVL();

        ArrayList<String> dataset = new ArrayList<>();
        IndiceInvertido indice_invertido = new IndiceInvertido();
        IndiceInvertidoAVL indice_invertido_avl = new IndiceInvertidoAVL();
        Arquivo arquivo = new Arquivo();
        long inicio, fim;

        System.out.println("[Adicionando descrições na lista]");
        inicio = System.currentTimeMillis();
        dataset = arquivo.ler("amz.csv"); // Ao dataset são adicionadas apenas as descrições
        fim = System.currentTimeMillis();
        System.out.println("[Tempo de execução: " + (fim - inicio) + "ms]");

        div();

        System.out.println("[Iniciando construção do índice invertido (Hash)]");
        inicio = System.currentTimeMillis();
        indice_invertido.construir(dataset);
        fim = System.currentTimeMillis();
        System.out.println("[Tempo de execução: " + (fim - inicio) + "ms]");

        div();

        Map<String, ArrayList<ParOcorrenciasID>> indice = indice_invertido.getIndiceInvertido();
        indice_invertido.printarEstrutura();

        div();

        System.out.println("[Iniciando construção do índice invertido (AVL)]");
        inicio = System.currentTimeMillis();
        indice_invertido_avl.construir(dataset);
        fim = System.currentTimeMillis();
        System.out.println("[Tempo de execução: " + (fim - inicio) + "ms]");

        div();

        indice_invertido_avl.getIndiceInvertido().preorder(); // Printar estrutura

        div();












        // Termo que será consultado
        String termos[] = {"comfortable", "breathable"};



        System.out.print("Termos: ");
        for (String termo : termos) System.out.print(termo + " | ");
        System.out.println();

        div();

        System.out.println("Relevância para HASH");

        relevancia.calcular(termos, indice, dataset);

        div();

        System.out.println("Relevância para AVL");

        relevanciaavl.calcular(termos, indice_invertido_avl.getIndiceInvertido(), dataset);





        i.teste(indice_invertido_avl);


        HashMap<String, ArrayList> tabela = new HashMap<>();

        ArrayList<Integer> ids_1 = new ArrayList<>();
        ArrayList<Integer> ids_2 = new ArrayList<>();

        tabela.put("microfiber", ids_1);
        tabela.put("animal", ids_2);

        tabela.get("animal");






//        String termos[] = {"animal"};
//
//        //dataset.add("O homem é o único animal que pensa que pensa.");
//        //dataset.add("Quem pensa? O animal homem.");
//        //dataset.add("Se o homem pensa, logo existe. O homem existe porque pensa ou pensa porque existe?");
//
//        indice_invertido.construir(dataset);
//
//        System.out.println(indice_invertido.getTermos());
//        System.out.println("[Índice invertido construído]");
//
//        Map<String, ArrayList<ParOcorrenciasID>> indice = indice_invertido.getIndiceInvertido();
//
//        for (String key : indice.keySet()){
//            System.out.println("Chave: "+key);
//            ArrayList<ParOcorrenciasID> lista = indice.get(key);
//
//            for(ParOcorrenciasID item : lista){
//                int id = (Integer)item.getIDProduto() + 1;
//                System.out.println("<"+item.getOcorrencias()+", "+id+">");
//            }
//        }
//


    }

    public static void div(){
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}

