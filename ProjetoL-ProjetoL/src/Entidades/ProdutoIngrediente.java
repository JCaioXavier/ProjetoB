package Entidades;

public class ProdutoIngrediente {

    public int id_produto_ingrediente;
    public int id_produto;
    public int id_ingrediente;
    public int quantidade;

    public String toString() {
        return "\nid_produto_ingrediente: " + id_produto_ingrediente + "\n" +
                "id_produto: " + id_produto + "\n" +
                "id_ingrediente" + id_ingrediente + "\n" +
                "quantidade" + quantidade;
    }
}