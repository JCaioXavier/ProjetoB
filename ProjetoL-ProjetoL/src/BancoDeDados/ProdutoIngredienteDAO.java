package BancoDeDados;

import Entidades.Carrinho;
import Entidades.Pedido;
import Entidades.ProdutoIngrediente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static BancoDeDados.ConexaoBD.*;
import static BancoDeDados.ConexaoBD.conn;
import static BancoDeDados.IngredienteDAO.*;

import static BancoDeDados.ProdutoDAO.*;
import static Util.RevisarOpcao.*;

public class ProdutoIngredienteDAO {

    public static ProdutoIngrediente selecionarIngrediente(String nome){
        ProdutoIngrediente novoPI = new ProdutoIngrediente();
        boolean opcaoBoolean = true, loop = true;
        Scanner scanner = new Scanner(System.in);

        int opcaoUm = 0, opcaoDois = 0;

        ingredientesDAO();

        do {
            do {
                System.out.println("Selecione o id do ingrediente que deseja adicionar ao produto: ");
                novoPI.id_ingrediente = pegarIngredienteDAO();
                System.out.println("Tem certeza que quer adicionar o ingrediente n°" + novoPI.id_ingrediente + " ao produto " + nome + "?");

                opcaoUm = simOuNao();

                if(opcaoUm == 1){
                    opcaoBoolean = false;
                }

            }while(opcaoBoolean);


            do{
                try {
                    System.out.println("Qual a quantidade de ingrediente que vai no produto?");

                    String novaQuantidadeString = scanner.nextLine();

                    Integer.parseInt(novaQuantidadeString);

                    if (!novaQuantidadeString.isEmpty()) {
                        novoPI.quantidade = Integer.parseInt(novaQuantidadeString); // Atu
                        break;
                    } else if (novoPI.quantidade < 0) {
                        System.out.println("Quantidade não pode ser menor que 0! Digite novamente.");
                    }
                } catch (NumberFormatException erro) {
                    System.out.println("[ERROR]");
                }
            }while (true);

            novoPI.id_produto = pegarIdProduto(nome);


            String sql = "INSERT INTO piramide.produtos_ingredientes (id_ingrediente, quantidade, id_produto) VALUES (?, ?, ?)";
            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, novoPI.id_ingrediente);
                pstmt.setInt(2, novoPI.quantidade);
                pstmt.setInt(3, novoPI.id_produto);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Produto inserido com sucesso!");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            ProdutosIngredientes(nome);

            System.out.println("\nDeseja selecionar outro ingrediente?");

            opcaoDois = simOuNao();

            if (opcaoDois == 1) {
                opcaoBoolean = true;
            }else{
                loop = false;
            }

        }while(loop);

        return novoPI;
    }

    public static void ProdutosIngredientes(String nome_produto) {
        System.out.println("\nNome do Produto: " +nome_produto);

        String sql = "SELECT nome_ingrediente, quantidade " +
                "FROM piramide.ingredientes i " +
                "JOIN piramide.produtos_ingredientes pi ON i.id_ingrediente = pi.id_ingrediente " +
                "JOIN piramide.produtos p ON pi.id_produto = p.id_produto " +
                "WHERE nome_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da consulta
            stmt.setString(1, nome_produto);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Ingrediente: " + rs.getString("nome_ingrediente") +
                        "\nQuantidade: " + rs.getInt("quantidade"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static int verificarPi(int produto, int quantidade) {
        String sql = """
            SELECT pi.id_ingrediente, pi.quantidade AS qtd_ingrediente, 
                   i.estoque AS estoque_ingrediente
            FROM piramide.produtos_ingredientes pi
            JOIN piramide.carrinhos_ingredientes i ON i.id_ingrediente = pi.id_ingrediente
            WHERE pi.id_produto = ?
            """;

            try (Connection conn = ConexaoBD.getConnection();
                    PreparedStatement pstmtIngredientes = conn.prepareStatement(sql)){

                pstmtIngredientes.setInt(1, produto);

                try (ResultSet rsIngredientes = pstmtIngredientes.executeQuery()) {
                    boolean primeiroIngrediente = true;
                    int quantidadeMaximaDisponivel = 0;

                    while (rsIngredientes.next()) {
                        int qtdIngredientePorProduto = rsIngredientes.getInt("qtd_ingrediente");
                        int estoqueIngrediente = rsIngredientes.getInt("estoque_ingrediente");

                        int quantidadePorIngrediente = estoqueIngrediente / qtdIngredientePorProduto;

                        if (primeiroIngrediente) {
                            quantidadeMaximaDisponivel = quantidadePorIngrediente;
                            primeiroIngrediente = false;
                        } else {
                            quantidadeMaximaDisponivel = Math.min(quantidadeMaximaDisponivel, quantidadePorIngrediente);
                        }
                    }

                    if (quantidade > quantidadeMaximaDisponivel) {
                        if(quantidadeMaximaDisponivel == 0){
                            return 3;
                        }
                        System.out.println("Só está disponível adicionar " + quantidadeMaximaDisponivel + " no momento.");

                        return 2;
                    }else{
                        return 1;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao processar pedido: " + e.getMessage());
                e.printStackTrace();
            }
            return -1;
    }
}










