package Entidades;

public class Carrinho {
    public int id_item;
    public int id_produtoC;
    public String nomeC;
    public double precoC;

    public String toString() {
        return "\nItem: " + id_item + "\n" +
                "Nome: " + nomeC + "\n" +
                "Preço: " + precoC + "\n";
    }
}