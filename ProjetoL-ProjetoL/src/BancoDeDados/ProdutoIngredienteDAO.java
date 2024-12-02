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


        public void darbaixa(int idCarrinho) {
            try (
                    // Estabelecendo a conexão com o banco de dados
                    Connection conn = ConexaoBD.getConnection();

                    // Preparando o statement para o SELECT
                    PreparedStatement selectCarrinhoStmt = conn.prepareStatement(
                            "SELECT c.id_produto, c.quantidade AS qtd_produto, pi.id_ingrediente, pi.quantidade AS qtd_ingrediente, i.quantidade AS estoque_ingrediente " +
                                    "FROM piramide.carrinhos c " +
                                    "JOIN piramide.produtos_ingredientes pi ON pi.id_produto = c.id_produto " +
                                    "JOIN piramide.ingredientes i ON i.id_ingrediente = pi.id_ingrediente " +
                                    "WHERE c.id_carrinho = ?"
                    )
            ) {
                selectCarrinhoStmt.setInt(1, idCarrinho); // Passa o idCarrinho para a consulta

                // Executando a consulta SELECT
                try (ResultSet rs = selectCarrinhoStmt.executeQuery()) {
                    while (rs.next()) {
                        int idIngrediente = rs.getInt("id_ingrediente");
                        int qtdIngredientePorProduto = rs.getInt("qtd_ingrediente");
                        int qtdProdutoCarrinho = rs.getInt("qtd_produto");
                        int estoqueIngrediente = rs.getInt("estoque_ingrediente");

                        // Calculando a quantidade necessária de ingredientes para o produto
                        int quantidadeNecessaria = qtdIngredientePorProduto * qtdProdutoCarrinho;

                        // Verificando se há estoque suficiente
                        if (estoqueIngrediente < quantidadeNecessaria) {
                            System.out.println("Não há ingredientes suficientes para o produto: " +
                                    rs.getInt("id_produto") + ". Ingrediente: " + idIngrediente);
                            return;
                        }
                    }
                }

                // Se todos os ingredientes forem suficientes, atualiza o estoque e adiciona ao carrinho
                try (
                        PreparedStatement updateStmt = conn.prepareStatement(
                                "UPDATE piramide.ingredientes i " +
                                        "SET quantidade = quantidade - ? " +
                                        "FROM piramide.produtos_ingredientes pi " +
                                        "JOIN piramide.produtos p ON p.id_produto = pi.id_produto " +
                                        "WHERE i.id_ingrediente = pi.id_ingrediente " +
                                        "AND p.id_produto = ?"
                        )
                ) {
                    // Executando novamente o SELECT para processar os produtos
                    try (ResultSet rs = selectCarrinhoStmt.executeQuery()) {
                        while (rs.next()) {
                            int idIngrediente = rs.getInt("id_ingrediente");
                            int qtdIngredientePorProduto = rs.getInt("qtd_ingrediente");
                            int qtdProdutoCarrinho = rs.getInt("qtd_produto");

                            // Calculando a quantidade que será subtraída do estoque
                            int quantidadeAserSubtraida = qtdIngredientePorProduto * qtdProdutoCarrinho;

                            // Atualizando o estoque do ingrediente
                            updateStmt.setInt(1, quantidadeAserSubtraida); // Quantidade a ser subtraída
                            updateStmt.setInt(2, rs.getInt("id_produto")); // ID do produto
                            updateStmt.executeUpdate();
                        }
                    }

                    System.out.println("Produtos adicionados ao carrinho e estoque atualizado com sucesso.");
                }

            } catch (SQLException e) {
                System.err.println("Erro ao processar pedido: " + e.getMessage());
            }
        }




    /*public static int  verificarPi() {
        // Consulta SQL para pegar o último id_carrinho
        String sqlCarrinho = "SELECT MAX(id_carrinho) AS ultimo_carrinho FROM piramide.carrinhos";

        // Consulta SQL para verificar os ingredientes do carrinho
        String sqlIngredientes = """
        SELECT c.id_produto, c.quantidade AS qtd_produto, 
               pi.id_ingrediente, pi.quantidade AS qtd_ingrediente, 
               i.estoque AS estoque_ingrediente
        FROM piramide.carrinhos c
        JOIN piramide.produtos_ingredientes pi ON pi.id_produto = c.id_produto
        JOIN piramide.ingredientes i ON i.id_ingrediente = pi.id_ingrediente
        WHERE c.id_carrinho = ?
    """;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmtCarrinho = conn.prepareStatement(sqlCarrinho);
             ResultSet rsCarrinho = pstmtCarrinho.executeQuery()) {

            // Obtém o último id_carrinho
            if (rsCarrinho.next()) {
                int idCarrinho = rsCarrinho.getInt("ultimo_carrinho");

                // Agora, verifica os ingredientes do carrinho encontrado
                try (PreparedStatement pstmtIngredientes = conn.prepareStatement(sqlIngredientes)) {
                    pstmtIngredientes.setInt(1, idCarrinho); // Passa o idCarrinho para a consulta de ingredientes

                    try (ResultSet rsIngredientes = pstmtIngredientes.executeQuery()) {
                        while(rsIngredientes.next()) {
                            int idIngrediente = rsIngredientes.getInt("id_ingrediente");
                            int qtdIngredientePorProduto = rsIngredientes.getInt("qtd_ingrediente");
                            int qtdProdutoCarrinho = rsIngredientes.getInt("qtd_produto");
                            int estoqueIngrediente = rsIngredientes.getInt("estoque_ingrediente");

                            // Calculando a quantidade necessária de ingredientes para o produto
                            int quantidadeNecessaria = qtdIngredientePorProduto * qtdProdutoCarrinho;

                            // Verificando se há estoque suficiente
                            if (estoqueIngrediente < quantidadeNecessaria) {
                                //System.out.println("Não há ingredientes suficientes para o produto: " +
                                        //rsIngredientes.getInt("id_produto") + ". Ingrediente: " + idIngrediente);
                                System.out.println(quantidadeNecessaria);
                                System.out.println(qtdIngredientePorProduto);
                                System.out.println(estoqueIngrediente);
                                int novaquantidade;

                                novaquantidade = estoqueIngrediente - quantidadeNecessaria;
                                System.out.println(novaquantidade);

                                return novaquantidade;
                            }
                        }
                        System.out.println("Todos os ingredientes estão disponíveis para o último carrinho.");
                        return 1;
                    }
                }

            } else {
                System.out.println("Não foi possível encontrar o último carrinho.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao processar pedido: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("esse produto nao tem ingrediente");
        return 2;
    }*/
    public static int verificarPi(int produto, int quantidade) {
        String sql = """
            SELECT pi.id_ingrediente, pi.quantidade AS qtd_ingrediente, 
                   i.estoque AS estoque_ingrediente
            FROM piramide.produtos_ingredientes pi
            JOIN piramide.carrinhos_ingredientes i ON i.id_ingrediente = pi.id_ingrediente
            WHERE pi.id_produto = ?
            """;

            // Agora, verifica os ingredientes do carrinho
            try (Connection conn = ConexaoBD.getConnection();
                    PreparedStatement pstmtIngredientes = conn.prepareStatement(sql)){

                pstmtIngredientes.setInt(1, produto);  // Passa o id_carrinho para a consulta

                try (ResultSet rsIngredientes = pstmtIngredientes.executeQuery()) {
                    boolean primeiroIngrediente = true; // Flag para identificar o primeiro ingrediente
                    int quantidadeMaximaDisponivel = 0; // Variável de controle

                    // Loop para verificar os ingredientes e calcular a quantidade máxima de sanduíches
                    while (rsIngredientes.next()) {
                        int qtdIngredientePorProduto = rsIngredientes.getInt("qtd_ingrediente");
                        int estoqueIngrediente = rsIngredientes.getInt("estoque_ingrediente");


                        int quantidadePorIngrediente = estoqueIngrediente / qtdIngredientePorProduto;

                        // Se for o primeiro ingrediente, inicializa a quantidade máxima
                        if (primeiroIngrediente) {
                            quantidadeMaximaDisponivel = quantidadePorIngrediente;
                            primeiroIngrediente = false; // A partir do próximo, só compara
                        } else {
                            // Ajusta para o menor valor entre os ingredientes
                            quantidadeMaximaDisponivel = Math.min(quantidadeMaximaDisponivel, quantidadePorIngrediente); // 1
                            System.out.println();
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










