package Entidades;

import java.util.Date;
import java.util.List;

public class Pedido {
    public int id_pedido;
    public int id_cliente;
    public int id_funcionario;
    public char tipo;
    public double total;
    public int data;

    public String toString() {
        return "\nPedido: " + id_pedido + "\n" +
                "Cliente: " + id_cliente + "\n" +
                "Tipo: " + tipo + "\n" +
                "Total: R$ " + total + "\n" +
                "Data: " + data + "\n";
    }
}
