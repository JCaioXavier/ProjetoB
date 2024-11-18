//Dados estaticos
package Util;

import Entidades.*;

import java.util.ArrayList;
import java.util.List;

public class DadosEstaticos {

    public static List<Cliente> gerarClientes() {
        Cliente caio = new Cliente();
        caio.id_cliente = 1;
        caio.usuario = "j_caio";
        caio.senha = "Theo123";

        caio.nome = "Jose Caio";
        caio.cpf = "06142556487";
        caio.telefone = "82985446356";
        caio.endereco = "Unicompra farol";

        Cliente isabela = new Cliente();
        isabela.id_cliente = 2;
        isabela.usuario = "isabelasr";
        isabela.senha = "woola123";

        isabela.nome = "Isabela Soares";
        isabela.cpf = "12065895462";
        isabela.telefone = "82997552000";
        isabela.endereco = "Santa teresa";

        List<Cliente> clientes = new ArrayList<>();
        clientes.add(caio);
        clientes.add(isabela);
        return clientes;
    }

    public static List<Funcionario> gerarFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();

        funcionarios.add(gerarAdm());

        Funcionario thiago = new Funcionario();
        thiago.id_funcionario = 2;
        thiago.usuario = "titimoral";
        thiago.senha = "lumbris123";
        thiago.nome = "Thiago Novaes";
        thiago.cpf = "06142556487";
        thiago.telefone = "82998368060";
        thiago.endereco = "Rua Doutor Carlos Gomes de Barros ";
        funcionarios.add(thiago);

        return funcionarios;
    }

    private static Funcionario gerarAdm() {
        Funcionario admin = new Funcionario();
        admin.id_funcionario = 1;
        admin.usuario = "admin";
        admin.senha = "admin";
        admin.nome = "Admir Admin";
        admin.cpf = "06142556411";
        admin.telefone = "82818368060";
        admin.endereco = "Rua Bolsonaro Jair ";
        return admin;
    }

    public static List<Produto> gerarProduto() {

        Produto x_Frango = new Produto();
        x_Frango.id_produto = 1;
        x_Frango.nome = "X-Frango";
        x_Frango.estoque = 5;
        x_Frango.preco = 16.00;

        Produto x_Calabresa = new Produto();
        x_Calabresa.id_produto = 2;
        x_Calabresa.nome = "X-Calabresa";
        x_Calabresa.estoque = 5;
        x_Calabresa.preco = 18.00;

        Produto guarana = new Produto();
        guarana.id_produto = 3;
        guarana.nome = "Guaraná";
        guarana.estoque = 5;
        guarana.preco = 5.00;

        List<Produto> produtos = new ArrayList<>();
        produtos.add(x_Frango);
        produtos.add(x_Calabresa);
        produtos.add(guarana);

        return produtos;
    }

    public static List<Mesa> gerarMesa() {

        Mesa mesa1 = new Mesa();
        mesa1.id_mesa = 1;
        mesa1.produtos = null;
        mesa1.total = 0;

        Mesa mesa2 = new Mesa();
        mesa2.id_mesa = 2;
        mesa2.produtos = null;
        mesa2.total = 0;

        Mesa mesa3 = new Mesa();
        mesa3.id_mesa = 3;
        mesa3.produtos = null;
        mesa3.total = 0;

        Mesa mesa4 = new Mesa();
        mesa4.id_mesa = 4;
        mesa4.produtos = null;
        mesa4.total = 0;

        Mesa mesa5 = new Mesa();
        mesa5.id_mesa = 5;
        mesa5.produtos = null;
        mesa5.total = 0;

        List<Mesa> mesas = new ArrayList<>();
        mesas.add(mesa1);
        mesas.add(mesa2);
        mesas.add(mesa3);
        mesas.add(mesa4);
        mesas.add(mesa5);

        return mesas;
    }

    public static List<Carrinho> gerarCarrinho() {
        return new ArrayList<>();
    }

    public static List<Ingrediente> gerarIngrediente() {

        Ingrediente pao = new Ingrediente();
        pao.id_ingrediente = 1;
        pao.nome = "Pão";
        pao.estoque = 20;

        Ingrediente queijo = new Ingrediente();
        queijo.id_ingrediente = 2;
        queijo.nome = "Queijo";
        queijo.estoque = 10;

        Ingrediente alface = new Ingrediente();
        alface.id_ingrediente = 3;
        alface.nome = "Alface";
        alface.estoque = 10;

        Ingrediente tomate = new Ingrediente();
        tomate.id_ingrediente = 4;
        tomate.nome = "Tomate";
        tomate.estoque = 20;

        Ingrediente frango = new Ingrediente();
        frango.id_ingrediente = 5;
        frango.nome = "Frango";
        frango.estoque = 5;

        Ingrediente calabresa = new Ingrediente();
        calabresa.id_ingrediente = 6;
        calabresa.nome = "Calabresa";
        calabresa.estoque = 5;

        List<Ingrediente> ingredientes = new ArrayList<>();
        ingredientes.add(pao);
        ingredientes.add(queijo);
        ingredientes.add(alface);
        ingredientes.add(tomate);
        ingredientes.add(frango);
        ingredientes.add(calabresa);

        return ingredientes;
    }

    public static List<ProdutoIngrediente> gerarProdutoIngrediente() {
        List<ProdutoIngrediente> produtoIngrediente = new ArrayList<>();

        ProdutoIngrediente pao_XFrango = new ProdutoIngrediente();
        pao_XFrango.id_produto_ingrediente = 1;
        pao_XFrango.id_produto = 1;
        pao_XFrango.id_ingrediente = 1;
        pao_XFrango.quantidade = 2;
        produtoIngrediente.add(pao_XFrango);

        ProdutoIngrediente frango_XFrango = new ProdutoIngrediente();
        frango_XFrango.id_produto_ingrediente = 2;
        frango_XFrango.id_produto = 1;
        frango_XFrango.id_ingrediente = 5;
        frango_XFrango.quantidade = 1;
        produtoIngrediente.add(frango_XFrango);

        ProdutoIngrediente queijo_XFrango = new ProdutoIngrediente();
        queijo_XFrango.id_produto_ingrediente = 3;
        queijo_XFrango.id_produto = 1;
        queijo_XFrango.id_ingrediente = 2;
        queijo_XFrango.quantidade = 1;
        produtoIngrediente.add(queijo_XFrango);

        ProdutoIngrediente tomate_XFrango = new ProdutoIngrediente();
        tomate_XFrango.id_produto_ingrediente = 4;
        tomate_XFrango.id_produto = 1;
        tomate_XFrango.id_ingrediente = 4;
        tomate_XFrango.quantidade = 2;
        produtoIngrediente.add(tomate_XFrango);

        ProdutoIngrediente alface_XFrango = new ProdutoIngrediente();
        alface_XFrango.id_produto_ingrediente = 5;
        alface_XFrango.id_produto = 1;
        alface_XFrango.id_ingrediente = 3;
        alface_XFrango.quantidade = 1;
        produtoIngrediente.add(alface_XFrango);

        ProdutoIngrediente pao_XCalabresa = new ProdutoIngrediente();
        pao_XCalabresa.id_produto_ingrediente = 6;
        pao_XCalabresa.id_produto = 2;
        pao_XCalabresa.id_ingrediente = 1;
        pao_XCalabresa.quantidade = 2;
        produtoIngrediente.add(pao_XCalabresa);

        ProdutoIngrediente queijo_XCalabresa = new ProdutoIngrediente();
        queijo_XCalabresa.id_produto_ingrediente = 7;
        queijo_XCalabresa.id_produto = 2;
        queijo_XCalabresa.id_ingrediente = 6;
        queijo_XCalabresa.quantidade = 1;
        produtoIngrediente.add(queijo_XCalabresa);

        ProdutoIngrediente alface_XCalabresa = new ProdutoIngrediente();
        alface_XCalabresa.id_produto_ingrediente = 8;
        alface_XCalabresa.id_produto = 2;
        alface_XCalabresa.id_ingrediente = 3;
        alface_XCalabresa.quantidade = 1;
        produtoIngrediente.add(alface_XCalabresa);

        ProdutoIngrediente tomate_XCalabresa = new ProdutoIngrediente();
        tomate_XCalabresa.id_produto_ingrediente = 9;
        tomate_XCalabresa.id_produto = 2;
        tomate_XCalabresa.id_ingrediente = 4;
        tomate_XCalabresa.quantidade = 1;
        produtoIngrediente.add(tomate_XCalabresa);

        ProdutoIngrediente calabresa_XCalabresa = new ProdutoIngrediente();
        calabresa_XCalabresa.id_produto_ingrediente = 10;
        calabresa_XCalabresa.id_produto = 2;
        calabresa_XCalabresa.id_ingrediente = 6;
        calabresa_XCalabresa.quantidade = 1;
        produtoIngrediente.add(calabresa_XCalabresa);

        return produtoIngrediente;
    }
}