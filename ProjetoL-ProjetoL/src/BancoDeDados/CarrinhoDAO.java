package BancoDeDados;

import Entidades.Carrinho;
import Entidades.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static BancoDeDados.DetalhePedidoDAO.*;
import static BancoDeDados.ProdutoDAO.*;
import static Util.RevisarOpcao.simOuNao;

public class CarrinhoDAO {
    public static int carrinhoClienteDAO(int idCliente) {
        int idCarrinho = 0, condição = 0;
        String sql = "SELECT id_carrinho FROM piramide.carrinhos WHERE id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da consulta
            stmt.setInt(1, idCliente);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idCarrinho = rs.getInt("id_carrinho");

                condição = 1;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if(condição == 1){
            sql = "SELECT dp.id_carrinho, p2.nome_produto, dp.quantidade, dp.preco " +
                    "FROM piramide.clientes c " +
                    "JOIN piramide.carrinhos dp ON dp.id_cliente = c.id_cliente " +
                    "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                    "WHERE c.id_cliente = ? " +
                    "ORDER BY dp.id_carrinho ASC";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Define o parâmetro da consulta
                stmt.setInt(1, idCliente);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println("===========================");
                    System.out.println("Produto: " + rs.getString("nome_produto") +
                            "\nQuantidade: " + rs.getInt("quantidade") +
                            "\nPreço Unitário: " + rs.getDouble("preco"));
                }
                System.out.println("===========================");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return 2;
        }
        return 1;
    }

    public static int pegarCarrinhoClienteDAO(int idCliente) {
        Carrinho carrinho = new Carrinho();

        int idCarrinho = 0;

        String sql = "SELECT COUNT(*) FROM piramide.carrinhos WHERE id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                rs.getInt(1);
            } else {
                System.out.println("Pedido não encontrado! Digite um ID válido.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco1: " + e.getMessage());
        }

        sql = "SELECT id_carrinho FROM piramide.carrinhos WHERE id_cliente = ? ORDER BY id_carrinho DESC LIMIT 1";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente); // Define o parâmetro com o id do cliente
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idCarrinho = rs.getInt("id_carrinho");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco2: " + e.getMessage());
        }

        return idCarrinho;
    }

    public static void adicionarCarrinhoClienteDAO(int idCliente) {
        Carrinho novoCarrinho = new Carrinho();
        Scanner scanner = new Scanner(System.in);

        int checkador = 0, item, quantidade = 0, positivo, Condicao = 0, quantidadeSELECT = 0, resposta = 0;
        double total = 0, preco = 0, duplicata = 0, quantidadeMax = 0, subtrairTotal = 0;
        boolean loop = true;

        novoCarrinho.id_cliente = idCliente;

        while (loop) {
            try{
                do {
                    System.out.println("Produtos disponíveis:");
                    produtosDAO();

                    System.out.println("Selecione o número do item que deseja adicionar no pedido: ");
                    item = pegarPedidoProdutoDAO();

                    int retorno = selectEstoqueProdutoCliente(item, idCliente);

                    if(retorno == 1){
                        loop = false;
                        break;
                    }

                    quantidadeMax = quantidadeMaximaProdutoCarrinhoCliente(item, idCliente);

                    if(quantidadeMax == 2){
                        duplicata = carrinhoSemDuplicataCliente(item, idCliente);

                    }

                    if(duplicata == 1){
                        loop = false;
                        break;
                    }else if(duplicata == 2){
                        Condicao = 1;
                        novoCarrinho.id_produto = item;

                        System.out.print("Qual a quantidade desse item no pedido? ");
                        quantidade = Integer.parseInt(scanner.nextLine());

                        checkador = checkadorEstoque(item, quantidade, quantidadeSELECT);

                        if (quantidade <= 0) {
                            System.out.println("Quantidade inválida. Deve ser maior que zero.");
                        }
                    }

                }while(quantidade > 0 && checkador != 1);

                if(Condicao == 1){
                    System.out.println("\nTem certeza que deseja adicionar " + quantidade + " unidades desse item ao pedido?");
                    int confirmacao = simOuNao();

                    if (confirmacao == 1) {
                        novoCarrinho.quantidade = quantidade;
                        loop = false;
                    }
                }

            } catch (NumberFormatException e) {
                // Tratamento de entrada inválida (letras, caracteres especiais, etc.)
                System.out.println("Entrada inválida. Por favor, insira um número válido.");
            } catch (Exception e) {
                // Tratamento genérico para outros erros
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
        }

        if(Condicao == 1){
            preco = pegarPedidoPrecoDAO(novoCarrinho.id_produto);
            novoCarrinho.preco = preco;

            total = totalCarrinhoClienteDAO(idCliente);

            String sql = "INSERT INTO piramide.carrinhos (id_produto, quantidade, preco, id_cliente, total) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, novoCarrinho.id_produto);
                pstmt.setInt(2, novoCarrinho.quantidade);
                pstmt.setDouble(3, novoCarrinho.preco);
                pstmt.setInt(4, novoCarrinho.id_cliente);
                pstmt.setDouble(5, novoCarrinho.preco = (novoCarrinho.preco * quantidade) + total);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Produto inserido com sucesso no pedido!");
                } else {
                    System.out.println("Nenhuma linha foi inserida. Verifique os dados.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao inserir o pedido1: " + e.getMessage());
            }
        }
    }


    public static double removerItemCarrinhoClienteDAO(int idCliente) {
        Scanner scan = new Scanner(System.in);
        int linhaCarrinho = 0, itemIndex = 0, idItemCarrinho = 0, quantidadeMaximaDisponivel = 0, idProduto = 0, quantidade = 0;
        double totalFinal = 0;

        // Consulta para verificar a quantidade de itens no carrinho
        String sql = "SELECT COUNT(*) FROM piramide.carrinhos WHERE id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                linhaCarrinho = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
            return 0;
        }

        if (linhaCarrinho > 0) {

            // Exibe os itens no carrinho
            sql = "SELECT id_carrinho FROM piramide.carrinhos WHERE id_cliente = ?";
            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idCliente);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    rs.getInt("id_carrinho");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao listar itens do carrinho: " + e.getMessage());
                return 0;
            }

            System.out.print("Selecione o número do item para remover: ");
            itemIndex = scan.nextInt();

            if (itemIndex < 1 || itemIndex > linhaCarrinho) {
                System.out.println("Número inválido. Tente novamente.");
                return 0;
            }

            sql = "SELECT id_carrinho, id_produto, quantidade FROM piramide.carrinhos WHERE id_cliente = ? ORDER BY id_carrinho ASC LIMIT 1 OFFSET ?";
            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idCliente);
                pstmt.setInt(2, itemIndex - 1);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idItemCarrinho = rs.getInt("id_carrinho");
                    idProduto = rs.getInt("id_produto");
                    quantidade = rs.getInt("quantidade");
                } else {
                    System.out.println("Item não encontrado.");
                    return 0;
                }

            } catch (SQLException e) {
                System.out.println("Erro ao buscar o item: " + e.getMessage());
                return 0;
            }

            sql = """
                SELECT pi.id_ingrediente, pi.quantidade AS qtd_ingrediente, 
                       ci.estoque AS estoque_ingrediente
                FROM piramide.produtos_ingredientes pi
                JOIN piramide.carrinhos_ingredientes ci ON ci.id_ingrediente = pi.id_ingrediente
                WHERE pi.id_produto = ?
                """;

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmtIngredientes = conn.prepareStatement(sql)) {

                pstmtIngredientes.setInt(1, idProduto);

                try (ResultSet rsIngredientes = pstmtIngredientes.executeQuery()) {
                    boolean primeiroIngrediente = true;

                    while (rsIngredientes.next()) {
                        int qtdIngredientePorProduto = rsIngredientes.getInt("qtd_ingrediente");
                        int estoqueIngrediente = rsIngredientes.getInt("estoque_ingrediente");
                        int idIngrediente = rsIngredientes.getInt("id_ingrediente");

                        int quantidadePorIngrediente = estoqueIngrediente / qtdIngredientePorProduto;

                        if (primeiroIngrediente) {
                            quantidadeMaximaDisponivel = quantidadePorIngrediente;
                            primeiroIngrediente = false;
                        } else {
                            quantidadeMaximaDisponivel = Math.min(quantidadeMaximaDisponivel, quantidadePorIngrediente);
                        }

                        // Atualiza o estoque
                        String updateEstoque = "UPDATE piramide.carrinhos_ingredientes SET estoque = ? WHERE id_ingrediente = ?";
                        try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateEstoque)) {
                            pstmtUpdate.setInt(1, estoqueIngrediente + (qtdIngredientePorProduto * quantidade));
                            pstmtUpdate.setInt(2, idIngrediente);

                            int rowsUpdated = pstmtUpdate.executeUpdate();

                            if (rowsUpdated == 0) {
                                System.out.println("Nenhuma linha foi atualizada para o ingrediente " + idIngrediente);
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                System.err.println("Erro ao processar ingredientes: " + e.getMessage());
                return 0;
            }

            // Remove o item do carrinho
            sql = "DELETE FROM piramide.carrinhos WHERE id_carrinho = ? AND id_cliente = ?";
            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idItemCarrinho);
                pstmt.setInt(2, idCliente);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Item removido do carrinho com sucesso!");
                } else {
                    System.out.println("Nenhum item encontrado para exclusão.");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
            }

        } else {
            System.out.println("O carrinho está vazio!");
        }

        return totalFinal;
    }



    public static double totalCarrinhoClienteDAO(int idCarrinho) {
        double total = 0;

        String sql = "SELECT SUM(dp.quantidade * p2.preco) AS total_gasto " +
                "FROM piramide.clientes c " +
                "JOIN piramide.carrinhos dp ON dp.id_cliente = c.id_cliente " +
                "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                "WHERE dp.id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarrinho);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total_gasto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o total do pedido: " + e.getMessage());
        }

        return total;
    }

    public static void totalCarrinhoSubtrairClienteDAO(int idCliente, double subtrairTotal) {
        double totalSum = 0, total = 0, preco = 0, totalFinal = 0;
        int quantidade = 0, idCarrinho = 0;

        String sql = "SELECT SUM(dp.quantidade * p2.preco) AS total_gasto " +
                "FROM piramide.clientes c " +
                "JOIN piramide.carrinhos dp ON dp.id_cliente = c.id_cliente " +
                "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                "WHERE dp.id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalSum = rs.getDouble("total_gasto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o total do pedido: " + e.getMessage());
        }

        sql = "SELECT id_carrinho, quantidade, preco, total FROM piramide.carrinhos WHERE id_cliente = ? ORDER BY id_carrinho DESC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idCarrinho = rs.getInt("id_carrinho");
                    total = rs.getDouble("total");
                    quantidade = rs.getInt("quantidade");
                    preco = rs.getDouble("preco");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o total do pedido: " + e.getMessage());
        }

        sql = "UPDATE piramide.carrinhos SET quantidade = ?, total = ?, preco = ? WHERE id_carrinho = ? AND id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantidade);
            pstmt.setDouble(2, totalSum);
            pstmt.setDouble(3, preco);
            pstmt.setInt(4, idCarrinho);
            pstmt.setInt(5, idCliente);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }
    }

    public static void totalCarrinhoSubtrairFuncionarioDAO(int idFuncionario, double subtrairTotal) {
        double totalSum = 0, total = 0, preco = 0, totalFinal = 0;
        int quantidade = 0, idCarrinho = 0;

        String sql = "SELECT SUM(dp.quantidade * p2.preco) AS total_gasto " +
                "FROM piramide.funcionarios f " +
                "JOIN piramide.carrinhos dp ON dp.id_funcionario = f.id_funcionario " +
                "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                "WHERE dp.id_funcionario = ?;";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalSum = rs.getDouble("total_gasto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o total do pedido: " + e.getMessage());
        }

        sql = "SELECT id_carrinho, quantidade, preco, total FROM piramide.carrinhos WHERE id_funcionario = ? ORDER BY id_carrinho DESC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idCarrinho = rs.getInt("id_carrinho");
                    total = rs.getDouble("total");
                    quantidade = rs.getInt("quantidade");
                    preco = rs.getDouble("preco");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o total do pedido: " + e.getMessage());
        }

        sql = "UPDATE piramide.carrinhos SET quantidade = ?, total = ?, preco = ? WHERE id_carrinho = ? AND id_funcionario = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantidade);
            pstmt.setDouble(2, totalSum);
            pstmt.setDouble(3, preco);
            pstmt.setInt(4, idCarrinho);
            pstmt.setInt(5, idFuncionario);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }
    }

    public static void finalizarCarrinhoClienteDAO(int idCliente, double total) {
        Pedido novoPedido = new Pedido();

        int idPedido = 0;

        novoPedido.tipo = 'E';
        novoPedido.total = total;
        novoPedido.id_cliente = idCliente;

        String sql = "INSERT INTO piramide.pedidos (id_cliente, tipo, total) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            pstmt.setString(2, String.valueOf(novoPedido.tipo));
            pstmt.setDouble(3, total);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir o carrinho: " + e.getMessage());
        }

        sql = "SELECT id_pedido FROM piramide.pedidos WHERE id_cliente = ? ORDER BY id_pedido DESC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idPedido = rs.getInt("id_pedido");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o total do pedido: " + e.getMessage());
        }

        sql = "INSERT INTO piramide.detalhes_pedidos (id_pedido, id_produto, quantidade, preco, total) " +
                "SELECT p.id_pedido, c.id_produto, c.quantidade, c.preco, c.total " +
                "FROM piramide.carrinhos c " +
                "JOIN piramide.pedidos p ON p.id_cliente = c.id_cliente " +
                "WHERE c.id_cliente = ? AND p.id_pedido = ? " +
                "ORDER BY p.id_pedido DESC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            pstmt.setInt(2, idPedido);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir o carrinho: " + e.getMessage());
        }

        sql = "DELETE FROM piramide.carrinhos WHERE id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
        }

        selectIdProdutoeQuantidade(idPedido);
    }

    public static void apagarCarrinhoClienteDAO(int idCliente) {
        String sql = "DELETE FROM piramide.carrinhos WHERE id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);

            pstmt.executeUpdate();

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Carrinho apagado com sucesso!");
            } else {
                System.out.println("Nenhum item encontrado para exclusão.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
        }
    }

    public static void finalizarCarrinhoFuncionarioDAO(int idFuncionario, double total) {
        Pedido novoPedido = new Pedido();

        int idPedido = 0;

        novoPedido.tipo = 'P';
        novoPedido.total = total;
        novoPedido.id_funcionario = idFuncionario;

        String sql = "INSERT INTO piramide.pedidos (id_funcionario, tipo, total) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFuncionario);
            pstmt.setString(2, String.valueOf(novoPedido.tipo));
            pstmt.setDouble(3, total);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir o carrinho: " + e.getMessage());
        }

        sql = "SELECT id_pedido FROM piramide.pedidos WHERE id_funcionario = ? ORDER BY id_pedido DESC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idPedido = rs.getInt("id_pedido");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o total do pedido: " + e.getMessage());
        }

        sql = "INSERT INTO piramide.detalhes_pedidos (id_pedido, id_produto, quantidade, preco, total) " +
                "SELECT p.id_pedido, c.id_produto, c.quantidade, c.preco, c.total " +
                "FROM piramide.carrinhos c " +
                "JOIN piramide.pedidos p ON p.id_funcionario = c.id_funcionario " +
                "WHERE c.id_funcionario = ? AND p.id_pedido = ? " +
                "ORDER BY p.id_pedido DESC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFuncionario);
            pstmt.setInt(2, idPedido);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir o carrinho: " + e.getMessage());
        }

        sql = "DELETE FROM piramide.carrinhos WHERE id_funcionario = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFuncionario);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
        }

        selectIdProdutoeQuantidade(idPedido);
    }

    public static void adicionarCarrinhoFuncionarioDAO(int idFuncionario) {
        Carrinho novoCarrinho = new Carrinho();
        Scanner scanner = new Scanner(System.in);

        int checkador = 0, item, quantidade = 0, positivo, Condicao = 0, quantidadeSELECT = 0, resposta = 0;
        double total = 0, preco = 0, duplicata = 0, quantidadeMax = 0, subtrairTotal = 0;
        boolean loop = true;

        novoCarrinho.id_funcionario = idFuncionario;

        while (loop) {
            try{
                do {
                    System.out.println("Produtos disponíveis:");
                    produtosDAO();

                    System.out.println("Selecione o número do item que deseja adicionar no pedido: ");
                    item = pegarPedidoProdutoDAO();

                    int retorno = selectEstoqueProdutoFuncionario(item, idFuncionario);

                    if(retorno == 1){
                        loop = false;
                        break;
                    }

                    quantidadeMax = quantidadeMaximaProdutoCarrinhoFuncionario(item, idFuncionario);

                    if(quantidadeMax == 2){
                        duplicata = carrinhoSemDuplicataFuncionario(item, idFuncionario);

                    }

                    if(duplicata == 1){
                        loop = false;
                        break;
                    }else if(duplicata == 2){
                        Condicao = 1;
                        novoCarrinho.id_produto = item;

                        System.out.print("Qual a quantidade desse item no pedido? ");
                        quantidade = Integer.parseInt(scanner.nextLine());

                        checkador = checkadorEstoque(item, quantidade, quantidadeSELECT);

                        if (quantidade <= 0) {
                            System.out.println("Quantidade inválida. Deve ser maior que zero.");
                        }
                    }

                }while(quantidade > 0 && checkador != 1);

                if(Condicao == 1){
                    System.out.println("\nTem certeza que deseja adicionar " + quantidade + " unidades desse item ao pedido?");
                    int confirmacao = simOuNao();

                    if (confirmacao == 1) {
                        novoCarrinho.quantidade = quantidade;
                        loop = false;
                    }
                }

            } catch (NumberFormatException e) {
                // Tratamento de entrada inválida (letras, caracteres especiais, etc.)
                System.out.println("Entrada inválida. Por favor, insira um número válido.");
            } catch (Exception e) {
                // Tratamento genérico para outros erros
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
        }

        if(Condicao == 1){
            preco = pegarPedidoPrecoDAO(novoCarrinho.id_produto);
            novoCarrinho.preco = preco;

            total = totalCarrinhoClienteDAO(idFuncionario);

            String sql = "INSERT INTO piramide.carrinhos (id_produto, quantidade, preco, id_funcionario, total) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, novoCarrinho.id_produto);
                pstmt.setInt(2, novoCarrinho.quantidade);
                pstmt.setDouble(3, novoCarrinho.preco);
                pstmt.setInt(4, novoCarrinho.id_funcionario);
                pstmt.setDouble(5, novoCarrinho.preco = (novoCarrinho.preco * quantidade) + total);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Produto inserido com sucesso no pedido!");
                } else {
                    System.out.println("Nenhuma linha foi inserida. Verifique os dados.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao inserir o pedido1: " + e.getMessage());
            }
        }
    }

    public static double removerItemCarrinhoFuncionarioDAO(int idFuncionario) {
        Scanner scan = new Scanner(System.in);
        int linhaCarrinho = 0, itemIndex = 0, idItemCarrinho = 0, quantidadeMaximaDisponivel = 0, idProduto = 0, quantidade = 0;
        double totalFinal = 0;

        // Consulta para verificar a quantidade de itens no carrinho
        String sql = "SELECT COUNT(*) FROM piramide.carrinhos WHERE id_funcionario = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFuncionario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                linhaCarrinho = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
            return 0;
        }

        if (linhaCarrinho > 0) {

            // Exibe os itens no carrinho
            sql = "SELECT id_carrinho FROM piramide.carrinhos WHERE id_funcionario = ?";
            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idFuncionario);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    rs.getInt("id_carrinho");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao listar itens do carrinho: " + e.getMessage());
                return 0;
            }

            System.out.print("Selecione o número do item para remover: ");
            itemIndex = scan.nextInt();

            if (itemIndex < 1 || itemIndex > linhaCarrinho) {
                System.out.println("Número inválido. Tente novamente.");
                return 0;
            }

            sql = "SELECT id_carrinho, id_produto, quantidade FROM piramide.carrinhos WHERE id_cliente = ? ORDER BY id_carrinho ASC LIMIT 1 OFFSET ?";
            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idFuncionario);
                pstmt.setInt(2, itemIndex - 1);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idItemCarrinho = rs.getInt("id_carrinho");
                    idProduto = rs.getInt("id_produto");
                    quantidade = rs.getInt("quantidade");
                } else {
                    System.out.println("Item não encontrado.");
                    return 0;
                }

            } catch (SQLException e) {
                System.out.println("Erro ao buscar o item: " + e.getMessage());
                return 0;
            }

            sql = """
                SELECT pi.id_ingrediente, pi.quantidade AS qtd_ingrediente, 
                       ci.estoque AS estoque_ingrediente
                FROM piramide.produtos_ingredientes pi
                JOIN piramide.carrinhos_ingredientes ci ON ci.id_ingrediente = pi.id_ingrediente
                WHERE pi.id_produto = ?
                """;

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmtIngredientes = conn.prepareStatement(sql)) {

                pstmtIngredientes.setInt(1, idProduto);

                try (ResultSet rsIngredientes = pstmtIngredientes.executeQuery()) {
                    boolean primeiroIngrediente = true;

                    while (rsIngredientes.next()) {
                        int qtdIngredientePorProduto = rsIngredientes.getInt("qtd_ingrediente");
                        int estoqueIngrediente = rsIngredientes.getInt("estoque_ingrediente");
                        int idIngrediente = rsIngredientes.getInt("id_ingrediente");

                        int quantidadePorIngrediente = estoqueIngrediente / qtdIngredientePorProduto;

                        if (primeiroIngrediente) {
                            quantidadeMaximaDisponivel = quantidadePorIngrediente;
                            primeiroIngrediente = false;
                        } else {
                            quantidadeMaximaDisponivel = Math.min(quantidadeMaximaDisponivel, quantidadePorIngrediente);
                        }

                        // Atualiza o estoque
                        String updateEstoque = "UPDATE piramide.carrinhos_ingredientes SET estoque = ? WHERE id_ingrediente = ?";
                        try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateEstoque)) {
                            pstmtUpdate.setInt(1, estoqueIngrediente + (qtdIngredientePorProduto * quantidade));
                            pstmtUpdate.setInt(2, idIngrediente);

                            int rowsUpdated = pstmtUpdate.executeUpdate();

                            if (rowsUpdated == 0) {
                                System.out.println("Nenhuma linha foi atualizada para o ingrediente " + idIngrediente);
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                System.err.println("Erro ao processar ingredientes: " + e.getMessage());
                return 0;
            }

            // Remove o item do carrinho
            sql = "DELETE FROM piramide.carrinhos WHERE id_carrinho = ? AND id_funcionario = ?";
            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idItemCarrinho);
                pstmt.setInt(2, idFuncionario);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Item removido do carrinho com sucesso!");
                } else {
                    System.out.println("Nenhum item encontrado para exclusão.");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
            }

        } else {
            System.out.println("O carrinho está vazio!");
        }

        return totalFinal;
    }

    public static void apagarCarrinhoFuncionarioDAO(int idFuncionario) {
        String sql = "DELETE FROM piramide.carrinhos WHERE id_funcionario = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFuncionario);

            pstmt.executeUpdate();

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Carrinho apagado com sucesso!");
            } else {
                System.out.println("Nenhum item encontrado para exclusão.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
        }
    }

    public static int pegarCarrinhoFuncionarioDAO(int idFuncionario) {
        int idCarrinho = 0;

        String sql = "SELECT COUNT(*) FROM piramide.carrinhos WHERE id_funcionario = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFuncionario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                rs.getInt(1);
            } else {
                System.out.println("Pedido não encontrado! Digite um ID válido.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
        }

        sql = "SELECT id_carrinho FROM piramide.carrinhos WHERE id_funcionario = ? ORDER BY id_carrinho DESC LIMIT 1";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFuncionario); // Define o parâmetro com o id do cliente
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idCarrinho = rs.getInt("id_carrinho");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
        }

        return idCarrinho;
    }

    public static int carrinhoFuncionarioDAO(int idFuncionario) {
        int idCarrinho = 0, condição = 0;
        String sql = "SELECT id_carrinho FROM piramide.carrinhos WHERE id_funcionario = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da consulta
            stmt.setInt(1, idFuncionario);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idCarrinho = rs.getInt("id_carrinho");

                condição = 1;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if(condição == 1){
            sql = "SELECT dp.id_carrinho, p2.nome_produto, dp.quantidade, dp.preco " +
                    "FROM piramide.clientes c " +
                    "JOIN piramide.carrinhos dp ON dp.id_cliente = c.id_cliente " +
                    "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                    "WHERE c.id_cliente = ? " +
                    "ORDER BY dp.id_carrinho ASC";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Define o parâmetro da consulta
                stmt.setInt(1, idFuncionario);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println("===========================");
                    System.out.println("Produto: " + rs.getString("nome_produto") +
                            "\nQuantidade: " + rs.getInt("quantidade") +
                            "\nPreço Unitário: " + rs.getDouble("preco"));
                }
                System.out.println("===========================");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return 2;
        }
        return 1;
    }

    public static double totalCarrinhoFuncionarioDAO(int idCarrinho) {
        double total = 0;

        String sql = "SELECT SUM(dp.quantidade * p2.preco) AS total_gasto " +
                "FROM piramide.funcionarios f " +
                "JOIN piramide.carrinhos dp ON dp.id_funcionario = f.id_funcionario " +
                "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                "WHERE dp.id_funcionario = ?;";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarrinho);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total_gasto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o total do pedido: " + e.getMessage());
        }

        return total;
    }
}
