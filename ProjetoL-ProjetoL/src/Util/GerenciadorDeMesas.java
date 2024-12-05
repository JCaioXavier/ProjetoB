package Util;

import Entidades.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GerenciadorDeMesas {
    Scanner scanner = new Scanner(System.in);

    public static List<Mesa> adicionarMesaPedido(List<Mesa> mesas, List<Carrinho> listaCarrinho, double total) {

        Mesa addMesa = new Mesa();

        addMesa.produtos = new ArrayList<>();

        mesas.add(addMesa);

        addMesa.id_mesa = mesas.size();

        System.out.println("Número de itens no carrinho: " + listaCarrinho.size());

        addMesa.total = total;

        System.out.println(mesas);

        return mesas;
    }

    public static void editarProduto(List<Mesa> mesas, int idMesa) {
        Scanner scanner = new Scanner(System.in);
        int simOuNao;

        if (idMesa >= 0 && idMesa < mesas.size()) {
            Mesa mesaAtual = mesas.get(idMesa);

            System.out.println("\nProduto atual: " + mesaAtual);
        }

    }
}
