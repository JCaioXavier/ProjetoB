package Util;

import Entidades.Carrinho;
import Entidades.Produto;

import java.util.List;
import java.util.Scanner;

import static Util.DadosEstaticos.gerarProduto;

public class GerenciadorDeCarrinho {

    public static List<Carrinho> adicionarProduto(List<Carrinho> comprarProduto, int i) {//-------------------------------
        Carrinho addProduto = new Carrinho();
        int id = comprarProduto.size();

        List<Produto> produtos = gerarProduto();

        comprarProduto.add(addProduto);

        addProduto.id_item = id + 1;
        addProduto.id_produtoC = produtos.get(i).id_produto;
        addProduto.nomeC = produtos.get(i).nome;
        addProduto.precoC = produtos.get(i).preco;

        System.out.println("Produto adicionado: " + addProduto.nomeC);

        return comprarProduto;
    }

    public static void removerItem(List<Carrinho> carrininho, int indexProduto) {//--------------------------------------
        if (indexProduto >= 0 && indexProduto < carrininho.size()) {
            carrininho.remove(indexProduto);

            // Atualiza os IDs dos produtos após a remoção
            for (int i = 0; i < carrininho.size(); i++) {
                carrininho.get(i).id_item = i + 1;
            }
            System.out.println("Produto removido com sucesso!");
        } else {
            System.out.println("Não existe produto com esse ID!");
        }
    }

    public static int pegarItem(List<Carrinho> produtos) {//PEGAR ID DO ITEM ---------------------------------------------
        Scanner scanner = new Scanner(System.in);
        int indexProduto;
        do {
            indexProduto = scanner.nextInt();
            if (indexProduto == 1) {
                indexProduto--;
                break;
            }
            indexProduto--;

            if ((produtos.size() < indexProduto) || (0 >= indexProduto)) {
                System.out.println("Não existe Produto com esse id! Digite novamente.");
            }
        } while ((produtos.size() < indexProduto) || (0 >= indexProduto));
        return indexProduto;
    }

    public static double precoTotal(List<Carrinho> carrininho) {
        double total = 0;

        for (int i = 0; i < carrininho.size(); i++) {
            total += carrininho.get(i).precoC;
        }
        return total;
    }
}