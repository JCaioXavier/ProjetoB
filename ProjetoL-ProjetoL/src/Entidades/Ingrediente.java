package Entidades;

public class Ingrediente {

    public int id_ingrediente;
    public String nome;
    public int estoque;

    public String toString() {
        return "\nid_ingrediente: " + id_ingrediente + "\n" +
                "Nome: " + nome + "\n" +
                "Estoque: " + estoque + "\n";
    }
}