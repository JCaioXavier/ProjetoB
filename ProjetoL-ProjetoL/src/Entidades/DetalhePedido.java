package Entidades;

public class DetalhePedido {
    public int id_detalhe_pedido;
    public int id_pedido;
    public int id_item;
    public int quantidade;
    public double preco;

    public String toString() {
        return "\nPedido: " + id_pedido + "\n" +
                "Cliente: " + id_detalhe_pedido + "\n" +
                "Tipo: " + id_item + "\n" +
                "Total: R$ " + quantidade + "\n" +
                "Data: " + preco + "\n";
    }
}
