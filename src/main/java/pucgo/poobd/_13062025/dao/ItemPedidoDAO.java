package pucgo.poobd._13062025.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pucgo.poobd._13062025.database.DatabaseFactory;
import pucgo.poobd._13062025.model.ItemPedido;
import pucgo.poobd._13062025.model.Pedido;
import pucgo.poobd._13062025.model.Produto;

public class ItemPedidoDAO {
    private final Connection conn;
    private final ProdutoDAO produtoDAO;
    private final PedidoDAO pedidoDAO;

    public ItemPedidoDAO() throws SQLException {
        this.conn = DatabaseFactory.getConnection();
        this.produtoDAO = new ProdutoDAO();
        this.pedidoDAO = new PedidoDAO();
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table item_pedido (
                        id integer primary key autoincrement,
                        valor_total real not null,
                        quantidade integer not null,
                        pedido_fk integer,
                        produto_fk integer,
                        foreign key (pedido_fk) references pedido(id),
                        foreign key (produto_fk) references produto(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(ItemPedido itemPedido) {
        String sql = """
                insert into item_pedido (valor_total, quantidade, pedido_fk, produto_fk)
                values (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, itemPedido.getValorTotal());
            ps.setLong(2, itemPedido.getQuantidade());
            ps.setLong(3, itemPedido.getPedido().getId());
            ps.setLong(4, itemPedido.getProduto().getId());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    itemPedido.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<ItemPedido> obterPorId(long id) {
        Optional<ItemPedido> itemPedidoOpt = Optional.empty();
        String sql = "select * from item_pedido where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Optional<Produto> produto = produtoDAO.obterPorId(rs.getLong("produto_fk"));
                Optional<Pedido> pedido = pedidoDAO.obterPorId(rs.getLong("pedido_fk"));
                if (produto.isPresent() && pedido.isPresent()) {
                    ItemPedido itemPedido = new ItemPedido(
                        rs.getLong("id"),
                        produto.get(),
                        pedido.get(),
                        rs.getLong("quantidade"),
                        rs.getDouble("valor_total")
                    );
                    itemPedidoOpt = Optional.of(itemPedido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemPedidoOpt;
    }

    public List<ItemPedido> obterTodosPorIdPedido(long pedidoId) {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = "select * from item_pedido where pedido_fk = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, pedidoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Optional<Produto> produto = produtoDAO.obterPorId(rs.getLong("produto_fk"));
                Optional<Pedido> pedido = pedidoDAO.obterPorId(rs.getLong("pedido_fk"));
                if (produto.isPresent() && pedido.isPresent()) {
                    ItemPedido itemPedido = new ItemPedido(
                        rs.getLong("id"),
                        produto.get(),
                        pedido.get(),
                        rs.getLong("quantidade"),
                        rs.getDouble("valor_total")
                    );
                    itens.add(itemPedido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itens;
    }

    public void atualizarPorId(long id, ItemPedido itemPedido) {
        String sql = """
                update item_pedido
                set valor_total = ?,
                    quantidade = ?,
                    pedido_fk = ?,
                    produto_fk = ?
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, itemPedido.getValorTotal());
            ps.setLong(2, itemPedido.getQuantidade());
            ps.setLong(3, itemPedido.getPedido().getId());
            ps.setLong(4, itemPedido.getProduto().getId());
            ps.setLong(5, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from item_pedido
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
