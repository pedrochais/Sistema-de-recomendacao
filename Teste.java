import java.util.ArrayList;

public class Teste {
    public static void main(String args[]){
        AVLAdaptado arvore = new AVLAdaptado();

//        String s1 = "amoeba";
//        String s2 = "beheli";
//
//        int saida = ((s1).compareTo(s2));
//        System.out.println(saida);

        ArrayList<ParOcorrenciasID> lista1 = new ArrayList<>();
        ArrayList<ParOcorrenciasID> lista2 = new ArrayList<>();
        ArrayList<ParOcorrenciasID> lista3 = new ArrayList<>();
        lista1.add(new ParOcorrenciasID(1, 2));
        lista1.add(new ParOcorrenciasID(1, 3));
        lista2.add(new ParOcorrenciasID(3, 5));
        lista2.add(new ParOcorrenciasID(2, 4));
        lista3.add(new ParOcorrenciasID(1, 2));
        lista3.add(new ParOcorrenciasID(1, 1));

        arvore.insere("amoeba", lista1);
        arvore.insere("amoeba", lista1);
        arvore.insere("behelit", lista2);
        arvore.insere("teste", lista3);


        arvore.busca("pedro");
        arvore.busca("behelit");
        arvore.busca("amoeba");
        arvore.busca("teste");

        arvore.preorder();


        //System.out.println(arvore.pegaRaiz("").getChave());

    }
}
