package pucgo.poobd._13062025.dao;

import pucgo.poobd._13062025.database.DatabaseFactory;
import pucgo.poobd._13062025.model.ItemPedido;
import pucgo.poobd._13062025.model.Pedido;
import pucgo.poobd._13062025.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemPedidoDAO {
    private final Connection conn;

    public ItemPedidoDAO() throws SQLException {
        this.conn = DatabaseFactory.getConnection();
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table itens_pedido (
                        id integer primary key autoincrement,
                        produto_fk integer not null,
                        pedido_fk integer not null,
                        quantidade integer not null,
                        valor_total real not null,
                        foreign key (produto_fk) references produto (id),
                        foreign key (pedido_fk) references pedido (id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(ItemPedido item) {
        String sql = """
                insert into itens_pedido (
                    produto_fk,
                    pedido_fk,
                    quantidade,
                    valor_total
                )
                values (
                    ?,
                    ?,
                    ?,
                    ?
                )
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, item.getProduto().getId());
            ps.setLong(2, item.getPedido().getId());
            ps.setLong(3, item.getQuantidade());
            ps.setDouble(4, item.getValorTotal());
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<ItemPedido> obterPorId(long id) {
        Optional<ItemPedido> itemOpt = Optional.empty();
        String sql = """
                select * itens_pedido where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery(sql);
            if (rs != null) {
                PedidoDAO pedidoDAO = new PedidoDAO();
                ProdutoDAO produtoDAO = new ProdutoDAO();

                Optional<Produto> produto = produtoDAO.obterPorId(rs.getLong(1));
                Optional<Pedido> pedido = pedidoDAO.obterPorId(rs.getLong(2));

                ItemPedido item = new ItemPedido(
                        rs.getLong(0),
                        produto.orElse(null),
                        pedido.orElse(null),
                        rs.getLong(3),
                        rs.getDouble(4)
                );
                itemOpt = Optional.of(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemOpt;
    }

    public List<ItemPedido> obterTodos() {
        List<ItemPedido> itens = new ArrayList<>();

        String sql = """
                select * from item_pedido
                """;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                PedidoDAO pedidoDAO = new PedidoDAO();
                ProdutoDAO produtoDAO = new ProdutoDAO();

                Optional<Produto> produto = produtoDAO.obterPorId(rs.getLong(1));
                Optional<Pedido> pedido = pedidoDAO.obterPorId(rs.getLong(2));

                ItemPedido item = new ItemPedido(
                        rs.getLong(0),
                        produto.orElse(null),
                        pedido.orElse(null),
                        rs.getLong(3),
                        rs.getDouble(4)
                );

                itens.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itens;
    }

    public void atualizarPorId(long id, Pedido pedido) {
        String sql = """
                update pedidos
                set empresa = ?,
                set vendedor = ?,
                set cliente = ?,
                set forma_de_pagamento = ?,
                set descricao = ?,
                set pedido_aprovado = ?,
                set valor_total = ?,
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, pedido.getEmpresa());
            ps.setLong(2, pedido.getVendedor());
            ps.setLong(3, pedido.getCliente());
            ps.setLong(4, pedido.getFormaPagamento());
            ps.setBoolean(5, pedido.isAprovado());
            ps.setDouble(6, pedido.getValorTotal());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from pedidos
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
