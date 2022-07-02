import java.util.ArrayList;

public class Teste {
    public static void main(String args[]){
        RBTreeAdaptado arvore = new RBTreeAdaptado();

        ArrayList<ParOcorrenciasID> lista1 = new ArrayList<>();
        ArrayList<ParOcorrenciasID> lista2 = new ArrayList<>();
        ArrayList<ParOcorrenciasID> lista3 = new ArrayList<>();
        lista1.add(new ParOcorrenciasID(1, 2));
        lista1.add(new ParOcorrenciasID(1, 3));
        lista2.add(new ParOcorrenciasID(3, 5));
        lista2.add(new ParOcorrenciasID(2, 4));
        lista3.add(new ParOcorrenciasID(1, 2));
        lista3.add(new ParOcorrenciasID(1, 1));

        arvore.insere("behelit", lista1);
        arvore.insere("amoeba", lista2);
        arvore.insere("teste", lista3);
        //arvore.insere("amoeba");
//        arvore.insere("lol");
//        arvore.insere("legal");
        //arvore.preorder();
        //arvore.printPreorder();
        arvore.busca("pedro"); //Há um problema aqui, ele não pode encontrar esse termo mas diz que a chave é null oxe
        arvore.busca("behelit");
        arvore.busca("amoeba");
        arvore.busca("teste");

        for (String t : arvore.getKeys()){
            System.out.println(t);
        }


        //arvore.busca("pedro");
        //arvore.busca("behelit");
        //arvore.busca("amoeba");
        //arvore.busca("teste");

        //arvore.preorder();


        //System.out.println(arvore.pegaRaiz("").getChave());

    }
}
