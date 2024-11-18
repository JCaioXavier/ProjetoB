package Entidades;

public class Cliente {

    public int id_cliente;
    public String usuario;
    public String senha;
    public String nome;
    public String cpf;
    public String telefone;
    public String endereco;

    public String toString() {
        return "\nId_cliente: " + id_cliente + "\n" +
                "Usuario: " + usuario + "\n" +
                "Senha: " + senha + "\n" +
                "Nome: " + nome + "\n" +
                "Cpf: " + cpf + "\n" +
                "Endereço: " + endereco + "\n" +
                "Telefone: " + telefone + "\n";
    }
}