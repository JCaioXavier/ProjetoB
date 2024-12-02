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
import static BancoDeDados.ProdutoIngredienteDAO.verificarPi;
import static Util.RevisarOpcao.simOuNao;

public class CarrinhoDAO {
    public static void carrinhoClienteDAO(int idCliente) {
        String sql = "SELECT dp.id_carrinho, p2.nome_produto, dp.quantidade, dp.preco " +
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
            //System.out.println("\nPedido: " + rs.getInt("id_pedido") +
            //" Data: " + rs.getDate("data_pedido"));
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

    /*public static void adicionarCarrinhoClienteDAO(int idCliente) {
        Carrinho novoCarrinho = new Carrinho();
        Scanner scanner = new Scanner(System.in);

        int checkador = 0, item, quantidade = 0, positivo, Condicao = 0, quantidadeSELECT = 0;
        double total = 0, preco = 0, opcsd = 0, opqmpc = 0, subtrairTotal = 0;
        boolean loop = true;

        novoCarrinho.id_cliente = idCliente;

        while (loop) {
            try{
                do {
                    System.out.println("Produtos disponíveis:");
                    produtosDAO();

                    System.out.println("Selecione o número do item que deseja adicionar no pedido: ");
                    item = pegarPedidoProdutoDAO();

                    opqmpc = quantidadeMaximaProdutoCarrinhoCliente(item, idCliente);

                    if(opqmpc == 2){
                        opcsd = carrinhoSemDuplicataCliente(item, idCliente);
                    }

                    if(opcsd == 1){
                        loop = false;
                        break;
                    }else if(opcsd == 2){
                        Condicao = 1;
                        novoCarrinho.id_produto = item;

                        System.out.print("Qual a quantidade desse item no pedido? ");
                        quantidade = Integer.parseInt(scanner.nextLine());

                        checkador = checkadorEstoque(item, quantidade, quantidadeSELECT);
                        //PEGA O ID DO PRODUTO E A QUANTIDADE INFORMADA PELO USUARIO E CHECKA SE TEM ESTOQUE DE DETERMINADA QUANTIA
                        //CASO TENHA, RETORNA 1, CASO NÃO, RETORNA 2

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
    }*/

    public static void adicionarCarrinhoClienteDAO(int idCliente) {
        Carrinho novoCarrinho = new Carrinho();
        Scanner scanner = new Scanner(System.in);

        int checkador = 0, item, quantidade = 0, positivo, Condicao = 0, quantidadeSELECT = 0, condiçãoB = 0, opqmpc = 0;
        double total = 0, preco = 0, opcsd = 0, subtrairTotal = 0;
        boolean loop = true, loop1 = true;

        novoCarrinho.id_cliente = idCliente;

        while (loop) {
            try {
                System.out.println("Produtos disponíveis:");
                produtosDAO();

                System.out.println("Selecione o número do item que deseja adicionar no pedido: ");
                item = pegarPedidoProdutoDAO();

                opqmpc = quantidadeMaximaProdutoCarrinhoCliente(item, idCliente);//  0

                int identificar = verificarPi(item, opqmpc); // 1


                if (opqmpc == 2) {
                    //opcsd = carrinhoSemDuplicataCliente(item, idCliente);
                }else if(opqmpc == 0 && identificar == 0){
                    System.out.println("Produto indisponivel!");
                    break;
                }





                System.out.print("Qual a quantidade desse item no pedido? ");
                quantidade = Integer.parseInt(scanner.nextLine());

                if(quantidade <= 0){
                    System.out.println("Quantidade inválida. Deve ser maior que zero.");
                    Condicao = 0;
                }

                if (opqmpc == 0 && identificar == 1){
                    checkador = verificarPi(item, quantidade);
                    if (checkador == 2){
                        Condicao = 0;
                    }else {
                        Condicao = 1;
                    }
                }


                if (Condicao == 1) {
                    System.out.println("\nTem certeza que deseja adicionar " + quantidade + " unidades desse item ao pedido?");
                    int confirmacao = simOuNao();

                    if (confirmacao == 1) {
                        novoCarrinho.id_produto = item;
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

        if (Condicao == 1) {
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

    public static void AAAAAAAAAAAAAAA(int idCliente) {
        Carrinho novoCarrinho = new Carrinho();
        Scanner scanner = new Scanner(System.in);

        int checkador = 0, item, quantidade = 0, positivo, Condicao = 0, quantidadeSELECT = 0, resposta = 0;
        double total = 0, preco = 0, opcsd = 0, opqmpc = 0, subtrairTotal = 0;
        boolean loop = true;

        novoCarrinho.id_cliente = idCliente;

        while (loop) {
            try{
                do {
                    System.out.println("Produtos disponíveis:");
                    produtosDAO();

                    System.out.println("Selecione o número do item que deseja adicionar no pedido: ");
                    item = pegarPedidoProdutoDAO();

                    int retorno = selectEstoqueProduto(item, idCliente);

                    if(retorno == 1){
                        loop = false;
                        break;
                    }

                    opqmpc = quantidadeMaximaProdutoCarrinhoCliente(item, idCliente);

                    if(opqmpc == 2){
                        opcsd = carrinhoSemDuplicataCliente(item, idCliente);

                    }

                    if(opcsd == 1){
                        loop = false;
                        break;
                    }else if(opcsd == 2){
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

    public static int novoCarrinhoDAO(int idUsuario) {
        Carrinho novoCarrinho = new Carrinho();

        novoCarrinho.id_cliente = idUsuario;

        String sql = "INSERT INTO piramide.carrinhos (id_cliente) VALUES (?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, idUsuario);

            int rowsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir o carrinho: " + e.getMessage());
        }
        return idUsuario;
    }

    public static int pegarItemCarrinho (int idCliente) {
        Scanner scan = new Scanner(System.in);
        boolean produtoExistente = false;
        int idProduto = 0;

        do {
            idProduto = scan.nextInt();

            String sql = "SELECT * FROM piramide.carrinhos WHERE id_carrinho = ? and id_cliente = ? ";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto); // Define apenas o primeiro parâmetro
                pstmt.setInt(2, idCliente); // Define apenas o primeiro parâmetro
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    produtoExistente = true;
                } else {
                    System.out.println("Produto não encontrado! Digite um ID válido.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } while (!produtoExistente);

        return idProduto;
    }

    public static double removerItemCarrinhoClienteDAO(int idCliente) {
        Scanner scan = new Scanner(System.in);
        int linhaCarrinho = 0, itemIndex = 0, idItemCarrinho = 0, quantidade;
        double total = 0, preco = 0, totalFinal = 0;

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
        }

        if(linhaCarrinho > 0){
            itemIndex = scan.nextInt();

            if (itemIndex < 1 || itemIndex > linhaCarrinho) {
                System.out.println("Número inválido. Tente novamente.");
                return 0;
            }

            sql = "SELECT id_carrinho FROM piramide.carrinhos WHERE id_cliente = ? ORDER BY id_carrinho ASC LIMIT 1 OFFSET ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idCliente);
                pstmt.setInt(2, itemIndex - 1);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    idItemCarrinho = rs.getInt("id_carrinho");
                } else {
                    System.out.println("Item não encontrado1.");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
            }

            sql = "SELECT preco, quantidade, total FROM piramide.carrinhos WHERE id_carrinho = ? AND id_cliente = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idItemCarrinho);
                pstmt.setInt(2, idCliente);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    preco = rs.getDouble("preco");
                    quantidade = rs.getInt("quantidade");
                    total = rs.getDouble("total");

                    System.out.println(preco);
                    System.out.println(quantidade);
                    System.out.println(total);
                    totalFinal = (preco * quantidade);
                } else {
                    System.out.println("Item não encontrado2.");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
            }

            sql = "DELETE FROM piramide.carrinhos WHERE id_carrinho = ? AND id_cliente = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idItemCarrinho); // ID do item a ser removido
                pstmt.setInt(2, idCliente); // ID do cliente

                // Executa a exclusão e verifica se algo foi excluído
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Item removido do carrinho com sucesso!");
                } else {
                    System.out.println("Nenhum item encontrado para exclusão.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
            }
        }else {
            System.out.println("O carrinho está vazio!");
        }

        return totalFinal;
    }

    public static void removerUltimoItemCarrinhoClienteDAO(int idCliente) {
        // Consulta para pegar o último carrinho do cliente
        String sqlCarrinho = "SELECT MAX(id_carrinho) AS ultimo_carrinho FROM piramide.carrinhos WHERE id_cliente = ?";

        // SQL para remover o item do carrinho
        String sqlRemoverItem = "DELETE FROM piramide.carrinhos WHERE id_carrinho = ? AND id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection()) {

            // Primeiro, obter o ID do último carrinho
            try (PreparedStatement pstmtCarrinho = conn.prepareStatement(sqlCarrinho)) {
                pstmtCarrinho.setInt(1, idCliente); // Passa o idCliente para a consulta de carrinho

                try (ResultSet rsCarrinho = pstmtCarrinho.executeQuery()) {
                    if (rsCarrinho.next()) {
                        int idCarrinho = rsCarrinho.getInt("ultimo_carrinho");

                        // Verifica se há um carrinho para o cliente
                        if (idCarrinho != 0) {
                            // Em seguida, prepara e executa a remoção do item do carrinho
                            try (PreparedStatement pstmtRemoverItem = conn.prepareStatement(sqlRemoverItem)) {
                                pstmtRemoverItem.setInt(1, idCarrinho);  // Passa o idCarrinho para a remoção
                                pstmtRemoverItem.setInt(2, idCliente);   // Passa o idCliente para a remoção
                                int rowsAffected = pstmtRemoverItem.executeUpdate();

                                if (rowsAffected > 0) {
                                    System.out.println("Último item do carrinho removido com sucesso!");
                                } else {
                                    System.out.println("Nenhum item foi removido");
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
        }
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
        //PEGA O ID DO PEDIDO E CONTA QUANTAS LINHAS TEM NO PEDIDO, PASSA O VALOR PARA A VARIÁVEL
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

        int checkador = 0, item, quantidade = 0, positivo, Condicao = 0, quantidadeSELECT = 0;
        double total = 0, preco = 0, opcsd = 0, opqmpc = 0;
        boolean loop = true;

        novoCarrinho.id_funcionario = idFuncionario;

        while (loop) {
            try{
                do {
                    System.out.println("Produtos disponíveis:");
                    produtosDAO();

                    System.out.println("Selecione o número do item que deseja adicionar no pedido: ");
                    item = pegarPedidoProdutoDAO();

                    opqmpc = quantidadeMaximaProdutoCarrinhoFuncionario(item, idFuncionario);

                    if(opqmpc == 2){
                        opcsd = carrinhoSemDuplicataFuncionario(item, idFuncionario);
                    }

                    if(opcsd == 1){
                        loop = false;
                        break;
                    }else if(opcsd == 2){
                        Condicao = 1;
                        novoCarrinho.id_produto = item;

                        System.out.print("Qual a quantidade desse item no pedido? ");
                        quantidade = Integer.parseInt(scanner.nextLine());

                        checkador = checkadorEstoque(item, quantidade, quantidadeSELECT);
                        //PEGA O ID DO PRODUTO E A QUANTIDADE INFORMADA PELO USUARIO E CHECKA SE TEM ESTOQUE DE DETERMINADA QUANTIA
                        //CASO TENHA, RETORNA 1, CASO NÃO, RETORNA 2

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

            total = totalCarrinhoFuncionarioDAO(idFuncionario);

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
        int linhaCarrinho = 0, itemIndex = 0, idItemCarrinho = 0, quantidade;
        double total = 0, preco = 0, totalFinal = 0;

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
        }

        if(linhaCarrinho > 0){
            itemIndex = scan.nextInt();

            if (itemIndex < 1 || itemIndex > linhaCarrinho) {
                System.out.println("Número inválido. Tente novamente.");
                return 0;
            }

            sql = "SELECT id_carrinho FROM piramide.carrinhos WHERE id_funcionario = ? ORDER BY id_carrinho ASC LIMIT 1 OFFSET ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idFuncionario);
                pstmt.setInt(2, itemIndex - 1);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    idItemCarrinho = rs.getInt("id_carrinho");
                } else {
                    System.out.println("Item não encontrado1.");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
            }

            sql = "SELECT preco, quantidade, total FROM piramide.carrinhos WHERE id_carrinho = ? AND id_funcionario = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idItemCarrinho);
                pstmt.setInt(2, idFuncionario);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    preco = rs.getDouble("preco");
                    quantidade = rs.getInt("quantidade");
                    total = rs.getDouble("total");

                    System.out.println(preco);
                    System.out.println(quantidade);
                    System.out.println(total);
                    totalFinal = (preco * quantidade);
                } else {
                    System.out.println("Item não encontrado2.");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
            }

            sql = "DELETE FROM piramide.carrinhos WHERE id_carrinho = ? AND id_funcionario = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idItemCarrinho); // ID do item a ser removido
                pstmt.setInt(2, idFuncionario); // ID do cliente

                // Executa a exclusão e verifica se algo foi excluído
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Item removido do carrinho com sucesso!");
                } else {
                    System.out.println("Nenhum item encontrado para exclusão.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao remover item do carrinho: " + e.getMessage());
            }
        }else {
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

    public static void carrinhoFuncionarioDAO(int idFuncionario) {
        String sql = "SELECT dp.id_carrinho, p2.nome_produto, dp.quantidade, dp.preco " +
                "FROM piramide.funcionarios f " +
                "JOIN piramide.carrinhos dp ON dp.id_funcionario = f.id_funcionario " +
                "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                "WHERE f.id_funcionario = ? " +
                "ORDER BY dp.id_carrinho ASC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da consulta
            stmt.setInt(1, idFuncionario);

            ResultSet rs = stmt.executeQuery();
            //System.out.println("\nPedido: " + rs.getInt("id_pedido") +
            //" Data: " + rs.getDate("data_pedido"));
            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Produto: " + rs.getString("nome_produto") +
                        "\nQuantidade: " + rs.getInt("quantidade") +
                        "\nPreço: " + rs.getDouble("preco"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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