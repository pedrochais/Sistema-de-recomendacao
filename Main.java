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
        Arquivo arquivo = new Arquivo();
        ArrayList<String> dataset = new ArrayList<>();

        // TAD's
        Map<String, ArrayList<ParOcorrenciasID>> hash = new HashMap<>();
        AVLAdaptado avl = new AVLAdaptado();

        // Índices invertidos
        IndiceInvertidoGeral indice1 = new IndiceInvertidoGeral(hash);
        IndiceInvertidoGeral indice2 = new IndiceInvertidoGeral(avl);

        // Relevância
        RelevanciaGeral relevancia1 = new RelevanciaGeral(hash);
        RelevanciaGeral relevancia2 = new RelevanciaGeral(avl);



        dataset.add("");
        dataset.add("O homem é o único animal que pensa que pensa.");
        dataset.add("Quem pensa? O animal homem.");
        dataset.add("Se o homem pensa, logo existe. O homem existe porque pensa ou pensa porque existe?");

        indice1.construir(dataset);

        System.out.println(indice1.getTermos());

        indice1.printarEstrutura();

        div();

        String termos[] = {"animal"};

        relevancia1.calcular(termos, dataset);
        relevancia1.getDados();



//        // Variáveis
//        long inicio, fim;
//
//
//        // Início do programa
//
//        System.out.println("[Adicionando descrições na lista]");
//        inicio = System.currentTimeMillis();
//        dataset = arquivo.ler("amz.csv"); // Ao dataset são adicionadas apenas as descrições
//        fim = System.currentTimeMillis();
//        System.out.println("[Tempo de execução: " + (fim - inicio) + "ms]");
//
//        div();
//
//        System.out.println("[Iniciando construção do índice invertido (Hash)]");
//        inicio = System.currentTimeMillis();
//        indice1.construir(dataset);
//        fim = System.currentTimeMillis();
//        System.out.println("[Tempo de execução: " + (fim - inicio) + "ms]");
//
//        div();
//
//        indice1.printarEstrutura();
//
//        div();
//
//        System.out.println("[Iniciando construção do índice invertido (AVLAdaptado)]");
//        inicio = System.currentTimeMillis();
//        indice2.construir(dataset);
//        fim = System.currentTimeMillis();
//        System.out.println("[Tempo de execução: " + (fim - inicio) + "ms]");
//
//        div();
//
//        indice2.printarEstrutura();
//
//        div();
//
//        String termos[] = {"comfortable", "breathable"};
//
//        relevancia1.calcular(termos, dataset);
//
//        div();
//
//        relevancia2.calcular(termos, dataset);

    }

    public static void div(){
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}

