package Entidades;

public class Produto {

    public int id_produto;
    public String nome;
    public int estoque;
    public double preco;

    public String toString() {
        return "\nId_produto: " + id_produto + "\n" +
                "Nome: " + nome + "\n" +
                "Estoque: " + estoque + "\n" +
                "Preço: " + preco + "\n";
    }
}