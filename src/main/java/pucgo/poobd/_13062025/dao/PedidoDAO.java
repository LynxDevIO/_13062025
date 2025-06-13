package pucgo.poobd._13062025.dao;

import pucgo.poobd._13062025.database.DatabaseFactory;
import pucgo.poobd._13062025.model.ItemPedido;
import pucgo.poobd._13062025.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAO {
    private final Connection conn;

    public PedidoDAO() throws SQLException {
        this.conn = DatabaseFactory.getConnection();
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table pedidos (
                        id integer primary key autoincrement,
                        empresa integer not null,
                        vendedor integer not null,
                        cliente integer not null,
                        forma_de_pagamento integer not null,
                        descricao text,
                        pedido_aprovado boolean default false,
                        valor_total real not null,
                        foreign key (empresa) references empresa (id),
                        foreign key (vendedor) references vendedor (id),
                        foreign key (cliente) references cliente (id),
                        foreign key (forma_de_pagamento) references forma_de_pagamento (id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar() {
        String sql = """
                insert into pedidos (
                    
                )
                values (
                    
                )
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            //ps.setLong();
            // todo
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Pedido> obterPorId(long id) {
        Optional<Pedido> pedidoOpt = Optional.empty();
        String sql = """
                select * produtos where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery(sql);
            if (rs != null) {
                ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
                List<ItemPedido> itens = itemPedidoDAO.obterTodosPorIdPedido(long idPedido);

                Pedido pedido = new Pedido(
                        rs.getLong(0),
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        itens,
                        rs.getLong(6),
                        rs.getDouble(7)
                );
                pedidoOpt = Optional.of(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidoOpt;
    }

    public List<Pedido> obterTodos() {
        List<Pedido> pedidos = new ArrayList<>();

        String sql = """
                select * from pedidos
                """;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
                List<ItemPedido> itens = itemPedidoDAO.obterTodosPorIdPedido(long idPedido);

                Pedido pedido = new Pedido(
                        rs.getLong(0),
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        itens,
                        rs.getLong(6),
                        rs.getDouble(7)
                );

                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
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
