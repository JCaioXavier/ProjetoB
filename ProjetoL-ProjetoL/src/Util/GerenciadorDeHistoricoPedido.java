package Util;

import Entidades.Carrinho;
import Entidades.Cliente;
import Entidades.HistoricoPedido;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeHistoricoPedido {
    public static List<HistoricoPedido> adicionarHistoricoPedido(List<HistoricoPedido> listaHistorico, List<Carrinho> listaCarrinho, List<Cliente> clientes, double total, int id_Cliente) {

        HistoricoPedido addHistorico = new HistoricoPedido();

        addHistorico.produtos = new ArrayList<>();

        listaHistorico.add(addHistorico);

        addHistorico.id_historicoPedido = listaHistorico.size();

        System.out.println("Número de itens no carrinho: " + listaCarrinho.size());

        for (int i = 0; i < listaCarrinho.size(); i++) {
            addHistorico.produtos.add(listaCarrinho.get(i).nomeC); // Adiciona o produto à lista
            System.out.println("Produto adicionado: " + listaCarrinho.get(i).nomeC);
        }

        addHistorico.total = total;
        addHistorico.endereco = clientes.get(id_Cliente - 1).endereco;

        System.out.println(listaHistorico);

        return listaHistorico;
    }
}
