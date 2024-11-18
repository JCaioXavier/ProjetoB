package Entidades;

import java.util.List;

public class Mesa {
    public int id_mesa;
    public List<String> produtos;
    public double total;

    public String toString() {
        return "\nMesa: " + id_mesa + "\n" +
                "Produtos: " + produtos + "\n" +
                "Total: R$ " + total + "\n";
    }
}
