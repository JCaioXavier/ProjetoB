package Entidades;

public class Funcionario {

    public int id_funcionario;
    public String usuario;
    public String senha;
    public String nome;
    public String cpf;
    public String telefone;
    public String endereco;
    public boolean adm;

    public String toString() {
        return "\nId_Funcionario: " + id_funcionario + "\n" +
                "Usuario: " + usuario + "\n" +
                "Senha: " + senha + "\n" +
                "Nome: " + nome + "\n" +
                "Cpf: " + cpf + "\n" +
                "Endereço: " + endereco + "\n" +
                "Telefone: " + telefone + "\n";
    }
}