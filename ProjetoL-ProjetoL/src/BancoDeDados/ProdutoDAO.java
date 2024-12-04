package BancoDeDados;

import Entidades.Carrinho;
import Entidades.Produto;

import java.sql.*;
import java.util.Scanner;

import static BancoDeDados.CarrinhoDAO.totalCarrinhoClienteDAO;
import static BancoDeDados.CarrinhoDAO.totalCarrinhoFuncionarioDAO;
import static BancoDeDados.ProdutoIngredienteDAO.verificarPi;
import static Util.RevisarOpcao.simOuNao;

public class ProdutoDAO {

    public static void produtosDAO() {
        String sql = "SELECT id_produto, nome_produto, preco FROM piramide.produtos ORDER BY id_produto ASC";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Produto: " + rs.getString("id_produto") +
                        "\nNome: " + rs.getString("nome_produto") +
                        "\nPreço: " + rs.getDouble("preco"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int pegarProdutoDAO() {
        Scanner scan = new Scanner(System.in);
        boolean produtoExistente = false;
        int idProduto = -1;

        do {
            idProduto = scan.nextInt();

            String sql = "SELECT nome_produto, estoque, preco FROM piramide.produtos WHERE id_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto); // Define apenas o primeiro parâmetro
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

    public static int pegarIdProduto (String nome_produto){
        int idProuto = 0;

        String sql = "SELECT id_produto FROM piramide.produtos WHERE nome_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome_produto);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idProuto = rs.getInt("id_produto");
            } else {
                System.out.println("Produto não achado");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return idProuto;
    }


    public static void inserirProdutoDAO(Produto novo) {

        String nome_produto = novo.nome;
        int estoque = novo.estoque;
        double preco = novo.preco;

        String sql = "INSERT INTO piramide.produtos (nome_produto, estoque, preco) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome_produto);
            pstmt.setInt(2, estoque);
            pstmt.setDouble(3, preco);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produto inserido com sucesso!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static String checkadordeProdutosDAO(Produto novoProduto ) {
        Scanner scanner = new Scanner(System.in);
        boolean produtoExistente = true;

        do {

            System.out.print("Digite o nome do produto: ");
            novoProduto.nome = scanner.nextLine().trim();

            if (novoProduto.nome.isEmpty()) {
                System.out.println("Produto não pode ser vazio ou apenas espaços! Digite novamente.");
                continue;
            }
            String sql = "SELECT nome_produto FROM piramide.produtos WHERE nome_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, novoProduto.nome);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Nome já cadastrado! Digite novamente.");
                } else {
                    produtoExistente = false;
                }

            } catch (SQLException e) {
                System.out.println("Erro ao verificar Nome: " + e.getMessage());
            }

        } while (produtoExistente);

        return novoProduto.nome;
    }

    public static void updateProdutosDAO(Produto update) {

        int idp = update.id_produto;

        String nome = update.nome;
        int estoque = update.estoque;
        double preco = update.preco;

        System.out.println(update);

        String sql = "UPDATE piramide.produtos SET nome_produto = ?, estoque = ?, preco = ? WHERE id_produto = " + idp;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, estoque);
            pstmt.setDouble(3, preco);


            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produto editado com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static void removerProdutoDAO(int id) {

        String sql = "DELETE FROM piramide.produtos WHERE id_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate(); // Executa a consulta DELETE

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int tamanhoProdutoDAO() {
        String sql = "SELECT COUNT(*) as total FROM piramide.produtos";
        int totalProdutos = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                totalProdutos = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return totalProdutos;
    }
    public static void estoque(Produto produtoAtual,int idp){
        int quantidade = 0;
        Scanner scanner = new Scanner(System.in);

        do{
            try {
                System.out.print("Digite o estoque do produto: ");
                String novoEstoqueString = scanner.nextLine();

                Integer.parseInt(novoEstoqueString);

                if (!novoEstoqueString.isEmpty()) {
                    quantidade = Integer.parseInt(novoEstoqueString); // Atu
                    break;
                } else if (produtoAtual.estoque < 0) {
                    System.out.println("Estoque não pode ser menor que 0! Digite novamente.");
                }
            } catch (NumberFormatException erro) {
                System.out.println("[ERROR]");
            }
        }while (true);

        produtoAtual.estoque = quantidade;

        String sql = "UPDATE piramide.produtos SET estoque = ? WHERE id_produto = " + idp;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, produtoAtual.estoque);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Estoque adicionado com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //FUNÇÃO PRA PEGAR O TODOS OS ITEMS DE UM PEDIDO, RODA O FOR DE ACORDO COM A QUANTIDADE DE ITEMS Q TEM NO PEDIDO, ASSIM PEGANDO TODOS
    public static void selectIdProdutoeQuantidade(int idPedido){
        //PEGA O NUMERO DE LINHAS DE ACORDO COM O ID DO PEDIDO
        String sql = "SELECT COUNT(*) FROM piramide.detalhes_pedidos WHERE id_pedido = ?";
        int contagem = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPedido);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    contagem = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        //FAZ UM LOOP ATÉ CHEGAR O VALOR DO COUNT(CONTAGEM)
        for(int i = 1; i  <= contagem; i++){

            sql = "SELECT numero_linha, id_produto, quantidade " +
                    "FROM ( " +
                    "    SELECT ROW_NUMBER() OVER (ORDER BY id_produto ASC) AS numero_linha, " +
                    //CRIA UMA TABELA TEMPORARIA COM O NOME NUMERO_LINHA PARA FICAR MAIS FÁCIL DE CONTROLAR JÁ Q A GENTE N TEM ID SERIAL PARA SE BASEAR
                    "           id_produto, quantidade " +
                    //PEGA O ID_PRODUTO E QUANTIDADE
                    "    FROM piramide.detalhes_pedidos " +
                    "    WHERE id_pedido = ? " +
                    ") AS subquery " +
                    "WHERE numero_linha = ?";
                    //FAZ O COMPARATIVO ENTRE ID_PEDIDO E NUMERO DA LINHA (SUBQUERY PQ ESSA TABELA É TEMPORARIA)

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idPedido);
                pstmt.setInt(2, i);
                //NUMERO DA LINHA PRA COMPARAR

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int id_produto = rs.getInt("id_produto");
                        //PEGA O ID
                        int quantidade = rs.getInt("quantidade");
                        //PEGA A QUANTIDADE

                        darBaixaEstoqueProduto(id_produto, quantidade);
                        //FUNÇÃO PARA ALTERAR O ESTOQUE
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erro no a: " + e.getMessage());
            }
        }
    }

    //PEGA O ESTOQUE DO PRODUTO E SUBTRAI PELA QUANTIDADE SELECIONADA PELO USUARIO DANDO UPDATE NA TABELA
    public static void darBaixaEstoqueProduto(int id_produto, int quantidade){
        int estoque = 0;

        String sql = "SELECT estoque FROM piramide.produtos WHERE id_produto = ?";
        //DA SELECT NO ESTOQUE DO PRODUTO PASSANDO O ID_PRODUTO NO PARÂMETRO

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_produto);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estoque = rs.getInt("estoque");
                    //PEGA O ESTOQUE
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro no b: " + e.getMessage());
        }

        sql = "UPDATE piramide.produtos SET estoque = ? WHERE id_produto = ?";
        //DA UPDATE NA TABELA, PASSANDO AGR O NOVO ESTOQUE

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, estoque - quantidade);
            //SUBTRAI O ESTOQUE PELA QUANTIDADE
            pstmt.setInt(2, id_produto);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro no c: " + e.getMessage());
        }
    }

    public static int checkadorEstoque(int idProduto, int quantidade, int quantidadeSELECT){
        String sql = "SELECT estoque FROM piramide.produtos WHERE id_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);
            //PASSA O ID DO PARÂMETRO PARA O SELECT

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int estoque = rs.getInt("estoque");

                    if (quantidade > estoque || quantidadeSELECT > estoque || quantidadeSELECT + quantidade > estoque) {
                        System.out.println("Quantidade indisponível!");
                        return 2;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro no a: " + e.getMessage());
        }
        return 1;
    }

    public static int carrinhoSemDuplicataCliente(int idProduto, int idCliente){
        //PEGA O NUMERO DE LINHAS DE ACORDO COM O ID DO PEDIDO
        String sql = "SELECT id_produto FROM piramide.carrinhos WHERE id_produto = ? AND id_cliente = ?";
        int idProdutoSelect = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);
            pstmt.setInt(2, idCliente);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Item já contém dentro do carrinho, deseja acrescentar mais do mesmo?");

                    int simOuNao = simOuNao();

                    if(simOuNao == 1){
                        idProdutoSelect = rs.getInt(1);
                        selectDadosCarrinhoSemDuplicataCliente(idProdutoSelect, idCliente, idProduto);
                        return 1;
                    }else if(simOuNao == 2){
                        return 3;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }
        return 2;
    }

    public static void selectDadosCarrinhoSemDuplicataCliente(int idProdutoSelect, int idCliente, int idProduto){
        Scanner scanner = new Scanner(System.in);
        Carrinho novoCarrinho = new Carrinho();

        int checkador, quantidade;
        String sql = "SELECT quantidade, total, preco FROM piramide.carrinhos WHERE id_produto = ? AND id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProdutoSelect);
            pstmt.setInt(2, idCliente);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int quantidadeSELECT = rs.getInt(1);
                    double totalSELECT = rs.getDouble(2);
                    double precoSELECT = rs.getDouble(3);

                    do {
                        System.out.print("Qual a quantidade que deseja adicionar? ");
                        quantidade = Integer.parseInt(scanner.nextLine());

                        checkador = checkadorEstoque(idProduto, quantidade, quantidadeSELECT);
                        //checkador = verificarPi(idProdutoSelect, quantidade + quantidadeSELECT);


                    }while(quantidade <= 0 || checkador == 2);

                    updateDadosCarrinhoSemDuplicataCliente(idProdutoSelect, idCliente, quantidade, precoSELECT, quantidadeSELECT, totalSELECT);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }
    }

    public static void updateDadosCarrinhoSemDuplicataCliente(int idProduto, int idCliente, int quantidade, double precoSELECT, int quantidadeSELECT, double totalSELECT){
        String sql = "UPDATE piramide.carrinhos SET quantidade = ?, total = ?, preco = ? WHERE id_produto = ? AND id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantidadeSELECT + quantidade);
            totalSELECT = totalSELECT + (precoSELECT * quantidade);
            pstmt.setDouble(2, totalSELECT);
            pstmt.setDouble(3, precoSELECT);
            pstmt.setInt(4, idProduto);
            pstmt.setInt(5, idCliente);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

    }

    public static int quantidadeMaximaProdutoCarrinhoCliente(int idProduto, int idCliente){
        //PEGA O NUMERO DE LINHAS DE ACORDO COM O ID DO PEDIDO
        String sql = "SELECT c.quantidade, pi.id_produto_ingrediente " +
                "FROM piramide.carrinhos c " +
                "JOIN piramide.produtos p ON p.id_produto = c.id_produto " +
                "JOIN piramide.produtos_ingredientes pi ON pi.id_produto = p.id_produto " +
                "WHERE c.id_produto = ? AND c.id_cliente = ?";
        int idProdutoIngrediente = 0, estoqueProduto = 0, quantidade = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);
            pstmt.setInt(2, idCliente);


            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    quantidade = rs.getInt(1);
                    idProdutoIngrediente = rs.getInt(2);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        sql = "SELECT estoque FROM piramide.produtos WHERE id_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    estoqueProduto = rs.getInt(1);

                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        if(estoqueProduto == quantidade){
            System.out.println("Produto indisponivel!");

            return 0;
        }

        return 2;
    }

    public static int quantidadeMaximaProdutoCarrinhoFuncionario(int idProduto, int idFuncionario){
        //PEGA O NUMERO DE LINHAS DE ACORDO COM O ID DO PEDIDO
        String sql = "SELECT quantidade FROM piramide.carrinhos WHERE id_produto = ? AND id_funcionario = ?";
        int idProdutoSelect = 0, estoqueProduto = 0, quantidade = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);
            pstmt.setInt(2, idFuncionario);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    quantidade = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        sql = "SELECT estoque FROM piramide.produtos WHERE id_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    estoqueProduto = rs.getInt(1);

                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        if(estoqueProduto == quantidade){
            System.out.println("Produto indisponivel!");

            return 1;
        }

        return 2;
    }

    public static int carrinhoSemDuplicataFuncionario(int idProduto, int idFuncionario){
        //PEGA O NUMERO DE LINHAS DE ACORDO COM O ID DO PEDIDO
        String sql = "SELECT id_produto FROM piramide.carrinhos WHERE id_produto = ? AND id_funcionario = ?";
        int idProdutoSelect = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);
            pstmt.setInt(2, idFuncionario);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Item já contém dentro do carrinho, deseja acrescentar mais do mesmo?");

                    int simOuNao = simOuNao();

                    if(simOuNao == 1){
                        idProdutoSelect = rs.getInt(1);
                        selectDadosCarrinhoSemDuplicataFuncionario(idProdutoSelect, idFuncionario, idProduto);
                        return 1;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }
        return 2;
    }

    //LEMBRAR DO FUNCIONARIO
    public static void selectDadosCarrinhoSemDuplicataFuncionario(int idProdutoSelect, int idFuncionario, int idProduto){
        Scanner scanner = new Scanner(System.in);

        int checkador, quantidade;
        String sql = "SELECT quantidade, total, preco FROM piramide.carrinhos WHERE id_produto = ? AND id_funcionario = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProdutoSelect);
            pstmt.setInt(2, idFuncionario);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int quantidadeSELECT = rs.getInt(1);
                    double totalSELECT = rs.getDouble(2);
                    double precoSELECT = rs.getDouble(3);

                    do {
                        System.out.print("Qual a quantidade que deseja adicionar? ");
                        quantidade = Integer.parseInt(scanner.nextLine());

                        checkador = checkadorEstoque(idProduto, quantidade, quantidadeSELECT);

                    }while(quantidade <= 0 || checkador == 2);

                    updateDadosCarrinhoSemDuplicataFuncionario(idProdutoSelect, idFuncionario, quantidade, precoSELECT, quantidadeSELECT, totalSELECT);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }
    }

    public static void updateDadosCarrinhoSemDuplicataFuncionario(int idProduto, int idFuncionario, int quantidade, double precoSELECT, int quantidadeSELECT, double totalSELECT){

        int checkador = checkadorEstoque(idProduto, idFuncionario, quantidadeSELECT);

        if(checkador == 1){
            String sql = "UPDATE piramide.carrinhos SET quantidade = ?, total = ?, preco = ? WHERE id_produto = ? AND id_funcionario = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, quantidadeSELECT + quantidade);
                totalSELECT = totalSELECT + (precoSELECT * quantidade);
                pstmt.setDouble(2, totalSELECT);
                pstmt.setDouble(3, precoSELECT);
                pstmt.setInt(4, idProduto);
                pstmt.setInt(5, idFuncionario);

                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Erro ao contar produtos: " + e.getMessage());
            }
        }
    }

    public static int selectEstoqueProdutoCliente(int idProduto, int id_cliente){
        Scanner scanner = new Scanner(System.in);
        //PEGA O NUMERO DE LINHAS DE ACORDO COM O ID DO PEDIDO
        int idProdutoSelect = 0, estoqueProduto = 0, quantidade = 0, simOuNao, idProdutoIngrediente, confirmacao = 0, resposta = 0;
        int quantidadeCarrinho = 0, confirmação = 0, idCarrinho, condiçãoItemDuplicata = 0, idDetalhePedido = 0;

        String sql = "SELECT id_produto FROM piramide.carrinhos WHERE id_cliente = ? ORDER BY id_carrinho DESC LIMIT 1";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    idProdutoSelect = rs.getInt(1);

                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        sql = "SELECT id_produto_ingrediente FROM piramide.produtos_ingredientes WHERE id_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProdutoSelect);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    idDetalhePedido = rs.getInt(1);

                    condiçãoItemDuplicata = 1;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        sql = "SELECT estoque FROM piramide.produtos WHERE id_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    estoqueProduto = rs.getInt(1);

                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        if(estoqueProduto == 0){
            sql = "SELECT quantidade FROM piramide.carrinhos WHERE id_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto);

                // Executa a consulta
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {

                        quantidadeCarrinho = rs.getInt(1);
                    }
                }
            }catch (SQLException e) {
                System.out.println("Erro ao contar produtos: " + e.getMessage());
            }

            sql = "SELECT id_produto_ingrediente FROM piramide.produtos_ingredientes WHERE id_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto);

                // Executa a consulta
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {

                        idProdutoIngrediente = rs.getInt(1);

                        do {
                            System.out.print("Qual a quantidade desse item no pedido? ");
                            quantidade = Integer.parseInt(scanner.nextLine());

                            confirmacao = verificarPi(idProduto, quantidade);

                            if(confirmacao == 3){
                                break;
                            }

                        }while(confirmacao != 1);

                        if(confirmacao == 3){
                            return 1;
                        }


                        if(condiçãoItemDuplicata == 1){
                            System.out.println("Item já contém dentro do carrinho, deseja acrescentar mais do mesmo?");
                            resposta = simOuNao();

                            if(resposta == 1){
                                selectCountProdutoCliente(idProduto, quantidade, id_cliente, condiçãoItemDuplicata);
                            }

                        }else{
                            System.out.println("Tem certeza que deseja adicionar " + quantidade + " unidades desse item ao pedido?");
                            resposta = simOuNao();

                            if(resposta == 1){
                                selectCountProdutoCliente(idProduto, quantidade, id_cliente, condiçãoItemDuplicata);
                            }
                        }
                        return 1;
                    }
                }

            } catch (SQLException e) {
                System.out.println("Erro ao contar produtos: " + e.getMessage());
            }
        }
        return 2;
    }

    public static int selectEstoqueProdutoFuncionario(int idProduto, int id_funcionario){
        Scanner scanner = new Scanner(System.in);
        //PEGA O NUMERO DE LINHAS DE ACORDO COM O ID DO PEDIDO
        int idProdutoSelect = 0, estoqueProduto = 0, quantidade = 0, simOuNao, idProdutoIngrediente, confirmacao = 0, resposta = 0, quantidadeCarrinho = 0, confirmação = 0;

        String sql = "SELECT estoque FROM piramide.produtos WHERE id_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    estoqueProduto = rs.getInt(1);

                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        if(estoqueProduto == 0){
            sql = "SELECT quantidade FROM piramide.carrinhos WHERE id_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto);

                // Executa a consulta
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {

                        quantidadeCarrinho = rs.getInt(1);
                    }
                }
            }catch (SQLException e) {
                System.out.println("Erro ao contar produtos: " + e.getMessage());
            }

            sql = "SELECT id_produto_ingrediente FROM piramide.produtos_ingredientes WHERE id_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto);

                // Executa a consulta
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {

                        idProdutoIngrediente = rs.getInt(1);

                        do {
                            System.out.print("Qual a quantidade desse item no pedido? ");
                            quantidade = Integer.parseInt(scanner.nextLine());

                            confirmacao = verificarPi(idProduto, quantidade);

                            if(confirmacao == 3){
                                break;
                            }

                        }while(confirmacao != 1);

                        if(confirmacao == 3){
                            return 1;
                        }

                        System.out.println("Tem certeza que deseja adicionar " + quantidade + " unidades desse item ao pedido?");
                        resposta = simOuNao();

                        if (resposta == 1) {
                            selectCountProdutoFuncionario(idProduto, quantidade, id_funcionario);
                        }
                        return 1;
                    }
                }

            } catch (SQLException e) {
                System.out.println("Erro ao contar produtos: " + e.getMessage());
            }
        }
        return 2;
    }

    public static void selectCountProdutoCliente(int idProduto, int quantidadeReduzir, int id_cliente, int condiçãoItemDuplicata){
        String sql = "SELECT COUNT(*) FROM piramide.produtos_ingredientes WHERE id_produto = ?";
        int contagem = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    contagem = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        for(int i = 1; i  <= contagem; i++){
            sql = "SELECT numero_linha, quantidade, id_ingrediente " +
                    "FROM ( " +
                    "    SELECT ROW_NUMBER() OVER (ORDER BY id_produto_ingrediente ASC) AS numero_linha, " +
                    "           quantidade, id_ingrediente " +
                    "    FROM piramide.produtos_ingredientes " +
                    "    WHERE id_produto = ? " +
                    ") AS subquery " +
                    "WHERE numero_linha = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto);
                pstmt.setInt(2, i);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int quantidade = rs.getInt("quantidade");
                        int id_ingrediente = rs.getInt("id_ingrediente");

                        if(condiçãoItemDuplicata == 1){
                            semDuplicataDarBaixaEstoqueIngredienteCliente(id_ingrediente, (quantidadeReduzir * quantidade), idProduto, id_cliente, quantidadeReduzir, contagem);
                        }else{
                            darBaixaEstoqueIngredienteCliente(id_ingrediente, (quantidadeReduzir * quantidade), idProduto, id_cliente, quantidadeReduzir, contagem);
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erro no a: " + e.getMessage());
            }
        }
    }

    public static void selectCountProdutoFuncionario(int idProduto, int quantidadeReduzir, int id_funcionario){
        String sql = "SELECT COUNT(*) FROM piramide.produtos_ingredientes WHERE id_produto = ?";
        int contagem = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduto);

            // Executa a consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    contagem = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }

        for(int i = 1; i  <= contagem; i++){
            sql = "SELECT numero_linha, quantidade, id_ingrediente " +
                    "FROM ( " +
                    "    SELECT ROW_NUMBER() OVER (ORDER BY id_produto_ingrediente ASC) AS numero_linha, " +
                    "           quantidade, id_ingrediente " +
                    "    FROM piramide.produtos_ingredientes " +
                    "    WHERE id_produto = ? " +
                    ") AS subquery " +
                    "WHERE numero_linha = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto);
                pstmt.setInt(2, i);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int quantidade = rs.getInt("quantidade");

                        int id_ingrediente = rs.getInt("id_ingrediente");

                        darBaixaEstoqueIngredienteFuncionario(id_ingrediente, (quantidadeReduzir * quantidade), idProduto, id_funcionario, quantidadeReduzir, contagem);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erro no a: " + e.getMessage());
            }
        }
    }

    private static int contador = 0;

    public static void darBaixaEstoqueIngredienteCliente(int id_ingrediente, int quantidadeReduzir, int id_produto, int id_cliente, int quantidade, int contagem) {
        int estoque = 0, quantidadeSelectCarrinho = 0;
        double precoProduto = 0;

        // Incrementa o contador aqui dentro da lógica de processamento
        contador++;

        // Consultar o estoque
        String sql = "SELECT estoque FROM piramide.carrinhos_ingredientes WHERE id_ingrediente = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_ingrediente);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estoque = rs.getInt("estoque");
                }
            }

            sql = "UPDATE piramide.carrinhos_ingredientes SET estoque = ? WHERE id_ingrediente = ?";
            try (PreparedStatement updatePstmt = conn.prepareStatement(sql)) {
                updatePstmt.setInt(1, estoque - quantidadeReduzir); // Subtrai a quantidade
                updatePstmt.setInt(2, id_ingrediente); // ID do ingrediente

                updatePstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Erro ao atualizar o estoque na tabela temporária: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar estoque: " + e.getMessage());
        }

        // Obtém o total do carrinho do cliente
        double total = totalCarrinhoClienteDAO(id_cliente);

        // Consulta o preço do produto
        sql = "SELECT preco, estoque FROM piramide.produtos WHERE id_produto = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_produto);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    precoProduto = rs.getDouble("preco");
                    int estoqueProduto = rs.getInt("estoque");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro no a: " + e.getMessage());
        }

        // Inserção no carrinho quando o contador atingir a contagem
        if(contador == contagem){
            sql = "INSERT INTO piramide.carrinhos (id_cliente, id_produto, preco, quantidade, total) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id_cliente);
                pstmt.setInt(2, id_produto);
                pstmt.setDouble(3, precoProduto);
                pstmt.setInt(4, quantidade);
                pstmt.setDouble(5, total + (precoProduto * quantidade));

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Produto inserido com sucesso no pedido!");
                } else {
                    System.out.println("Nenhuma linha foi inserida. Verifique os dados.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao inserir o pedido: " + e.getMessage());
            }

            contador = 0;
        }
    }

    public static void semDuplicataDarBaixaEstoqueIngredienteCliente(int id_ingrediente, int quantidadeReduzir, int id_produto, int id_cliente, int quantidade, int contagem) {
        int estoque = 0, quantidadeSelectCarrinho = 0;
        double precoProduto = 0;

        // Incrementa o contador aqui dentro da lógica de processamento
        contador++;

        // Consultar o estoque
        String sql = "SELECT estoque FROM piramide.carrinhos_ingredientes WHERE id_ingrediente = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_ingrediente);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estoque = rs.getInt("estoque");
                }
            }

            sql = "UPDATE piramide.carrinhos_ingredientes SET estoque = ? WHERE id_ingrediente = ?";
            try (PreparedStatement updatePstmt = conn.prepareStatement(sql)) {
                updatePstmt.setInt(1, estoque - quantidadeReduzir); // Subtrai a quantidade
                updatePstmt.setInt(2, id_ingrediente); // ID do ingrediente

                updatePstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Erro ao atualizar o estoque na tabela temporária: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar estoque: " + e.getMessage());
        }

        // Obtém o total do carrinho do cliente
        double total = totalCarrinhoClienteDAO(id_cliente);

        // Consulta o preço do produto
        sql = "SELECT preco, estoque FROM piramide.produtos WHERE id_produto = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_produto);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    precoProduto = rs.getDouble("preco");
                    int estoqueProduto = rs.getInt("estoque");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro no a: " + e.getMessage());
        }

        sql = "SELECT preco, estoque FROM piramide.produtos WHERE id_produto = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_produto);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    precoProduto = rs.getDouble("preco");
                    int estoqueProduto = rs.getInt("estoque");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro no a: " + e.getMessage());
        }

        sql = "SELECT quantidade FROM piramide.carrinhos WHERE id_produto = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_produto);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    quantidadeSelectCarrinho = rs.getInt("quantidade");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro no a: " + e.getMessage());
        }

        // Inserção no carrinho quando o contador atingir a contagem
        if(contador == contagem){
            sql = "UPDATE piramide.carrinhos SET quantidade = ?, total = ? WHERE id_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, quantidade + quantidadeSelectCarrinho);
                pstmt.setDouble(2, total + (precoProduto * quantidade));
                pstmt.setInt(3, id_produto);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Produto inserido com sucesso no pedido!");
                } else {
                    System.out.println("Nenhuma linha foi inserida. Verifique os dados.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao inserir o pedido: " + e.getMessage());
            }

            contador = 0;
        }
    }

    public static void darBaixaEstoqueIngredienteFuncionario(int id_ingrediente, int quantidadeReduzir, int id_produto, int id_funcionario, int quantidade, int contagem) {
        int estoque = 0;
        double precoProduto = 0;

        // Incrementa o contador aqui dentro da lógica de processamento
        contador++;

        // Consultar o estoque
        String sql = "SELECT estoque FROM piramide.carrinhos_ingredientes WHERE id_ingrediente = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_ingrediente);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estoque = rs.getInt("estoque");
                }
            }

            sql = "UPDATE piramide.carrinhos_ingredientes SET estoque = ? WHERE id_ingrediente = ?";
            try (PreparedStatement updatePstmt = conn.prepareStatement(sql)) {
                updatePstmt.setInt(1, estoque - quantidadeReduzir); // Subtrai a quantidade
                updatePstmt.setInt(2, id_ingrediente); // ID do ingrediente

                updatePstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Erro ao atualizar o estoque na tabela temporária: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar estoque: " + e.getMessage());
        }

        // Obtém o total do carrinho do cliente
        double total = totalCarrinhoFuncionarioDAO(id_funcionario);

        // Consulta o preço do produto
        sql = "SELECT preco, estoque FROM piramide.produtos WHERE id_produto = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_produto);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    precoProduto = rs.getDouble("preco");
                    int estoqueProduto = rs.getInt("estoque");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro no a: " + e.getMessage());
        }

        // Inserção no carrinho quando o contador atingir a contagem
        if(contador == contagem){
            sql = "INSERT INTO piramide.carrinhos (id_funcionario, id_produto, preco, quantidade, total) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id_funcionario);
                pstmt.setInt(2, id_produto);
                pstmt.setDouble(3, precoProduto);
                pstmt.setInt(4, quantidade);
                pstmt.setDouble(5, total + (precoProduto * quantidade));

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Produto inserido com sucesso no pedido!");
                } else {
                    System.out.println("Nenhuma linha foi inserida. Verifique os dados.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao inserir o pedido: " + e.getMessage());
            }

            contador = 0;
        }
    }
}