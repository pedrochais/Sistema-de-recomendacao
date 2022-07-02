import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* LEMBRETES
 * O programa tá pegando a palavra 'description' na linha 0 (não precisa)
 * */

public class Main {
    public static final int C = 15;

    public static void main(String args[]) {
        Arquivo arquivo = new Arquivo();
        ArrayList<String> dataset = new ArrayList<>();

        System.out.println("[AVISO]: Considerando apenas palavras com quantidade de caracteres mais ou igual a "+C);
        div();

        // TAD's
        Map<String, ArrayList<ParOcorrenciasID>> hash = new HashMap<>();
        AVLAdaptado avl = new AVLAdaptado();
        RBTreeAdaptado rb = new RBTreeAdaptado();

        // Índices invertidos
        IndiceInvertidoGeral indice_hash = new IndiceInvertidoGeral(hash);
        IndiceInvertidoGeral indice_avl = new IndiceInvertidoGeral(avl);
        IndiceInvertidoGeral indice_rb = new IndiceInvertidoGeral(rb);

        // Relevância
        RelevanciaGeral relevancia_hash = new RelevanciaGeral(hash);
        RelevanciaGeral relevancia_avl = new RelevanciaGeral(avl);
        RelevanciaGeral relevancia_rb = new RelevanciaGeral(rb);

        dataset = arquivo.ler("vic.csv");
//        dataset.add("");
//        dataset.add("O homem é o único animal que pensa que pensa.");
//        dataset.add("Quem pensa? O animal homem.");
//        dataset.add("Se o homem pensa, logo existe. O homem existe porque pensa ou pensa porque existe?");

        indice_hash.construir(dataset);
        indice_hash.printarEstrutura();
        div();
        indice_avl.construir(dataset);
        indice_avl.printarEstrutura();
        div();
        indice_rb.construir(dataset);
        indice_rb.printarEstrutura();

        String termos[] = {"animal"};

        div();
        relevancia_hash.calcular(termos, dataset);
        div();
        relevancia_avl.calcular(termos, dataset);
        div();
        relevancia_rb.calcular(termos, dataset);


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

    public static void div() {
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}

