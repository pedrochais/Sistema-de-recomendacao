import java.util.*;
/* LEMBRETES
 * O programa tá pegando a palavra 'description' na linha 0 (não precisa)
 * */

public class Main {
    public static final int C = 10;
    public static final String regex = "[-.,+=“”*&:®…()•\u00AD–—’;%$#@|!?_\"\']";

    public static void main(String args[]) {
        Arquivo arquivo = new Arquivo();
        ArrayList<String> dataset = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        long inicio, fim;

        System.out.println("[AVISO]: Considerando apenas palavras com quantidade de caracteres maior ou igual a " + C);

        // TAD's
        Map<String, ArrayList<ParOcorrenciasID>> hash = new HashMap<>();
        AVLAdaptado avl = new AVLAdaptado();
        RBTreeAdaptado rb = new RBTreeAdaptado();

        // Índices invertidos
        IndiceInvertidoGeral indice_hash = new IndiceInvertidoGeral(hash);
        IndiceInvertidoGeral indice_avl = new IndiceInvertidoGeral(avl);
        IndiceInvertidoGeral indice_rb = new IndiceInvertidoGeral(rb);

        ArrayList<IndiceInvertidoGeral> indices = new ArrayList<>();
        indices.add(indice_hash);
        indices.add(indice_avl);
        indices.add(indice_rb);

        // Relevância
        RelevanciaGeral relevancia_hash = new RelevanciaGeral(hash);
        RelevanciaGeral relevancia_avl = new RelevanciaGeral(avl);
        RelevanciaGeral relevancia_rb = new RelevanciaGeral(rb);

        ArrayList<RelevanciaGeral> relevancias = new ArrayList<>();
        relevancias.add(relevancia_hash);
        relevancias.add(relevancia_avl);
        relevancias.add(relevancia_rb);

        // Vetor que contém os nomes dos arquivos dos datasets
        String vetor_datasets[] = {"Amazon", "Victoria's Secret", "Btemptd", "Calvin Klein", "Hanky Panky", "American Eagle", "Macy's", "Nordstrom", "Topshop USA"};
        List<String> lista_datasets = Arrays.asList(vetor_datasets);

        // Dataset inicial
        String file_name = "Amazon.csv";
        dataset = arquivo.ler("Datasets/" + file_name);

        String termos[] = {"comfortable", "flattering", "embroidery"};
        Double limiar = 0.0;


        // Menu principal
        while (true) {
            int contador_opcoes = 1;
            System.out.println("===========================================================================================");
            System.out.println("[Dataset atual: " + file_name + "]");
            System.out.println("[Termo(s): " + imprimirTermos(termos) + "]"); //Aqui vai ter que imprimir uma lista com os termos
            System.out.println("[Limiar: " + limiar + "]");
            div();
            System.out.println("(A). Escolher novo dataset");
            System.out.println("(B). Inserir termo(s) para consulta");
            System.out.println("(C). Inserir limiar para a recomendação");
            div();
            for (IndiceInvertidoGeral item : indices) {
                if (item.getIndiceConstruido()) {
                    System.out.println("(" + contador_opcoes + "). Imprimir índice invertido do TAD " + item.getEstruturaNome());
                } else {
                    System.out.println("(" + contador_opcoes + "). Construir índice invertido usando " + item.getEstruturaNome() + "");
                }
                contador_opcoes++;
            }
            div();
            for (RelevanciaGeral item : relevancias) {
                System.out.println("(" + contador_opcoes + "). Calcular a relevância usando " + item.getEstruturaNome());
                contador_opcoes++;
            }
            String entrada = input.nextLine();

            /* Opção para escolher qual dataset será usado
             * */
            if (entrada.equals("A") || entrada.equals("a")) {
                while (true) {
                    contador_opcoes = 1;
                    div();
                    System.out.println("LISTA DE DATASETS DISPONÍVEIS");
                    for (String item : lista_datasets) {
                        System.out.println("(" + contador_opcoes + "). " + item);
                        contador_opcoes++;
                    }
                    System.out.println("(0). Voltar");

                    entrada = input.nextLine();
                    if (entrada.equals("0")) break;

                    int op_int;

                    try {
                        op_int = Integer.parseInt(entrada);
                        lista_datasets.get(op_int - 1);
                    } catch (ArrayIndexOutOfBoundsException exception_1) {
                        System.out.println("[ERRO] A opção não existe na lista");
                        continue;
                    } catch (NumberFormatException exception_2) {
                        System.out.println("[ERRO] Use apenas caracteres numéricos");
                        continue;
                    }

                    if (lista_datasets.contains(lista_datasets.get(op_int - 1))) {
                        System.out.println("[AVISO] Lendo dataset " + lista_datasets.get(op_int - 1) + " e adicionando descrições em uma ArrayList.");
                        dataset = arquivo.ler("Datasets/" + lista_datasets.get(op_int - 1) + ".csv");
                        file_name = lista_datasets.get(op_int - 1);
                        break;
                    }
                }
            } else if (entrada.equals("B") || entrada.equals("b")) {
                while (true) {
                    div();
                    System.out.println("INSERIR TERMOS PARA CONSULTA");
                    System.out.print("Insira a quantidade de termos: ");
                    entrada = input.nextLine();
                    int op_int;

                    try {
                        op_int = Integer.parseInt(entrada);
                    } catch (NumberFormatException exception_2) {
                        System.out.println("[ERRO] Use apenas caracteres numéricos");
                        continue;
                    }

                    termos = new String[op_int];

                    for (int i = 0; i < op_int; i++) { // Verificar se há termos iguais aqui
                        System.out.print((i + 1) + "° termo: ");
                        String termo = input.nextLine();
                        termos[i] = termo;
                    }
                    break;

                }
            } else if (entrada.equals("C") || entrada.equals("c")) {
                while (true) {
                    div();
                    System.out.println("INSERIR LIMIAR PARA RECOMENDAÇÃO");
                    System.out.print("Novo limiar: ");
                    entrada = input.nextLine();
                    try {
                        limiar = Double.parseDouble(entrada);
                    } catch (NumberFormatException exception_1) {
                        System.out.println("[ERRO] Use apenas caracteres numéricos");
                        continue;
                    }
                    break;
                }
            } else {

                int op_int;
                try {
                    op_int = Integer.parseInt(entrada);
                } catch (NumberFormatException exception_2) {
                    System.out.println("[ERRO] Insira uma opção válida");
                    continue;
                }

                System.out.println();

                for (int i = 0; i < indices.size(); i++) {
                    if ((op_int - 1) == i) {
                        IndiceInvertidoGeral indice = indices.get(i);

                        if(indice.getIndiceConstruido()){
                            indice.printarEstrutura();
                        }else{
                            inicio = System.currentTimeMillis();
                            indice.construir(dataset);
                            fim = System.currentTimeMillis();

                            System.out.println("[Tempo de construção do índice invertido (" + indice.getEstruturaNome() + "): " + (fim - inicio) + "ms]");
                        }
                    }
                }
                for (int i = indices.size(); i < indices.size() + relevancias.size(); i++) {
                    if ((op_int - 1) == i) {
                        if(indices.get(i - indices.size()).getIndiceConstruido()){
                            RelevanciaGeral relevancia = relevancias.get((i - indices.size()));

                            inicio = System.currentTimeMillis();
                            relevancia.calcular(termos, dataset);
                            fim = System.currentTimeMillis();

                            System.out.println("[Termo(s) escolhido(s) para a consulta: " + imprimirTermos(termos) + "]");
                            System.out.println("[Tempo de execução do cálculo (" + relevancia.getEstruturaNome() + "): " + (fim - inicio) + "ms]");
                        }else{
                            System.out.println("[AVISO] Para calcular a relevância para este TAD é necessário primeiramente construir o índice invertido.");
                        }
                    }
                }
                System.out.println("[Pressione ENTER para continuar]");
                String stop = input.nextLine();
            }
        }
    }

    public static String imprimirTermos(String termos[]) {
        String s = "";
        for (int i = 0; i < termos.length; i++) {
            if (i == termos.length - 1) s += termos[i];
            else s += termos[i] + ", ";
        }
        return s;
    }

    public static void div() {
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}

