package Entidades;

import java.util.List;

public class HistoricoPedido {
    public int id_historicoPedido;
    public List<String> produtos;
    public double total;
    public String endereco;

    public String toString() {
        return "\nPedido: " + id_historicoPedido + "\n" +
                "Produtos: " + produtos + "\n" +
                "Total: R$ " + total + "\n" +
                "Endereço: " + endereco + "\n";
    }
}
