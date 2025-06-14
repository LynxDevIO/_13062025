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
import pucgo.poobd._13062025.model.Cliente;
import pucgo.poobd._13062025.model.Empresa;
import pucgo.poobd._13062025.model.FormaPagamento;
import pucgo.poobd._13062025.model.ItemPedido;
import pucgo.poobd._13062025.model.Pedido;
import pucgo.poobd._13062025.model.Vendedor;

public class PedidoDAO {
    private final Connection conn;
    private final EmpresaDAO empresaDAO;
    private final VendedorDAO vendedorDAO;
    private final ClienteDAO clienteDAO;
    private final FormaPagamentoDAO formaPagamentoDAO;
    private final ItemPedidoDAO itemPedidoDAO;

    public PedidoDAO() throws SQLException {
        this.conn = DatabaseFactory.getConnection();
        this.empresaDAO = new EmpresaDAO();
        this.vendedorDAO = new VendedorDAO();
        this.clienteDAO = new ClienteDAO();
        this.formaPagamentoDAO = new FormaPagamentoDAO();
        this.itemPedidoDAO = new ItemPedidoDAO();
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table pedido (
                        id integer primary key autoincrement,
                        empresa_id integer,
                        vendedor_id integer,
                        cliente_id integer,
                        descricao text,
                        aprovado boolean,
                        forma_pagamento_id integer,
                        valor_total real,
                        foreign key (empresa_id) references empresa(id),
                        foreign key (vendedor_id) references vendedor(id),
                        foreign key (cliente_id) references cliente(id),
                        foreign key (forma_pagamento_id) references forma_pagamento(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Pedido pedido) throws SQLException {
        String sql = """
                insert into pedido (empresa_id, vendedor_id, cliente_id, descricao, aprovado, forma_pagamento_id, valor_total)
                values (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, pedido.getEmpresa().getId());
            ps.setLong(2, pedido.getVendedor().getId());
            ps.setLong(3, pedido.getCliente().getId());
            ps.setString(4, pedido.getDescricao());
            ps.setBoolean(5, pedido.isAprovado());
            ps.setLong(6, pedido.getFormaPagamento().getId());
            ps.setDouble(7, pedido.getValorTotal());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedido.setId(generatedKeys.getLong(1));
                }
            }

            for (ItemPedido item : pedido.getItens()) {
                item.setPedido(pedido);
                itemPedidoDAO.criar(item);
            }
        }
    }

    public List<Pedido> obterTodos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "select * from pedido";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Optional<Empresa> empresaOpt = empresaDAO.obterPorId(rs.getLong("empresa_id"));
                Optional<Vendedor> vendedorOpt = vendedorDAO.obterPorId(rs.getLong("vendedor_id"));
                Optional<Cliente> clienteOpt = clienteDAO.obterPorId(rs.getLong("cliente_id"));
                Optional<FormaPagamento> formaPagamentoOpt = formaPagamentoDAO.obterPorId(rs.getLong("forma_pagamento_id"));
                if (empresaOpt.isPresent() && vendedorOpt.isPresent() && clienteOpt.isPresent() && formaPagamentoOpt.isPresent()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getLong("id"));
                    pedido.setEmpresa(empresaOpt.get());
                    pedido.setVendedor(vendedorOpt.get());
                    pedido.setCliente(clienteOpt.get());
                    pedido.setDescricao(rs.getString("descricao"));
                    pedido.setAprovado(rs.getBoolean("aprovado"));
                    pedido.setFormaPagamento(formaPagamentoOpt.get());
                    pedido.setValorTotal(rs.getDouble("valor_total"));
                    pedido.setItens(itemPedidoDAO.obterTodosPorIdPedido(pedido.getId()));
                    pedidos.add(pedido);
                }
            }
        }
        return pedidos;
    }

    public Optional<Pedido> obterPorId(long id) throws SQLException {
        String sql = "select * from pedido where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Optional<Empresa> empresaOpt = empresaDAO.obterPorId(rs.getLong("empresa_id"));
                Optional<Vendedor> vendedorOpt = vendedorDAO.obterPorId(rs.getLong("vendedor_id"));
                Optional<Cliente> clienteOpt = clienteDAO.obterPorId(rs.getLong("cliente_id"));
                Optional<FormaPagamento> formaPagamentoOpt = formaPagamentoDAO.obterPorId(rs.getLong("forma_pagamento_id"));
                if (empresaOpt.isPresent() && vendedorOpt.isPresent() && clienteOpt.isPresent() && formaPagamentoOpt.isPresent()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getLong("id"));
                    pedido.setEmpresa(empresaOpt.get());
                    pedido.setVendedor(vendedorOpt.get());
                    pedido.setCliente(clienteOpt.get());
                    pedido.setDescricao(rs.getString("descricao"));
                    pedido.setAprovado(rs.getBoolean("aprovado"));
                    pedido.setFormaPagamento(formaPagamentoOpt.get());
                    pedido.setValorTotal(rs.getDouble("valor_total"));
                    pedido.setItens(itemPedidoDAO.obterTodosPorIdPedido(pedido.getId()));
                    return Optional.of(pedido);
                }
            }
        }
        return Optional.empty();
    }

    public void atualizarPorId(long id, Pedido pedido) throws SQLException {
        String sql = """
                update pedido
                set empresa_id = ?,
                    vendedor_id = ?,
                    cliente_id = ?,
                    descricao = ?,
                    aprovado = ?,
                    forma_pagamento_id = ?,
                    valor_total = ?
                where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, pedido.getEmpresa().getId());
            ps.setLong(2, pedido.getVendedor().getId());
            ps.setLong(3, pedido.getCliente().getId());
            ps.setString(4, pedido.getDescricao());
            ps.setBoolean(5, pedido.isAprovado());
            ps.setLong(6, pedido.getFormaPagamento().getId());
            ps.setDouble(7, pedido.getValorTotal());
            ps.setLong(8, id);

            ps.executeUpdate();

            List<ItemPedido> itensAntigos = itemPedidoDAO.obterTodosPorIdPedido(id);
            for (ItemPedido item : itensAntigos) {
                itemPedidoDAO.deletarPorId(item.getId());
            }

            for (ItemPedido item : pedido.getItens()) {
                item.setPedido(pedido);
                itemPedidoDAO.criar(item);
            }
        }
    }

    public void deletarPorId(long id) throws SQLException {
        String sql = """
                delete from pedido
                where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
