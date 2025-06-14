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
import pucgo.poobd._13062025.model.Empresa;
import pucgo.poobd._13062025.model.Endereco;
import pucgo.poobd._13062025.model.Supervisor;
import pucgo.poobd._13062025.model.Vendedor;

public class VendedorDAO {
    private final Connection conn;
    private final EnderecoDAO enderecoDAO;
    private final EmpresaDAO empresaDAO;
    private final SupervisorDAO supervisorDAO;

    public VendedorDAO() throws SQLException {
        this.conn = DatabaseFactory.getConnection();
        this.enderecoDAO = new EnderecoDAO();
        this.empresaDAO = new EmpresaDAO();
        this.supervisorDAO = new SupervisorDAO();
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table vendedor (
                        id integer primary key autoincrement,
                        id_vendedor integer not null,
                        nome text not null,
                        cpf text not null,
                        telefone text not null,
                        endereco_id integer,
                        empresa_id integer,
                        supervisor_id integer,
                        foreign key (endereco_id) references endereco(id),
                        foreign key (empresa_id) references empresa(id),
                        foreign key (supervisor_id) references supervisor(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Vendedor vendedor) throws SQLException {
        String sql = """
                insert into vendedor (id_vendedor, nome, cpf, telefone, endereco_id, empresa_id, supervisor_id)
                values (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, vendedor.getIdVendedor());
            ps.setString(2, vendedor.getNome());
            ps.setString(3, vendedor.getCpf());
            ps.setString(4, vendedor.getTelefone());
            ps.setLong(5, vendedor.getEndereco().getId());
            ps.setLong(6, vendedor.getEmpresa().getId());
            ps.setLong(7, vendedor.getSupervisor() != null ? vendedor.getSupervisor().getId() : 0);

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vendedor.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<Vendedor> obterTodos() throws SQLException {
        List<Vendedor> vendedores = new ArrayList<>();
        String sql = "select * from vendedor";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Optional<Endereco> enderecoOpt = enderecoDAO.obterPorId(rs.getLong("endereco_id"));
                Optional<Empresa> empresaOpt = empresaDAO.obterPorId(rs.getLong("empresa_id"));
                Supervisor supervisor = null;
                try {
                    supervisor = supervisorDAO.obterPorId(rs.getLong("supervisor_id"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (enderecoOpt.isPresent() && empresaOpt.isPresent()) {
                    Vendedor vendedor = new Vendedor(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        enderecoOpt.get(),
                        empresaOpt.get(),
                        rs.getLong("id_vendedor"),
                        supervisor
                    );
                    vendedores.add(vendedor);
                }
            }
        }
        return vendedores;
    }

    public Optional<Vendedor> obterPorId(long id) throws SQLException {
        String sql = "select * from vendedor where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Optional<Endereco> enderecoOpt = enderecoDAO.obterPorId(rs.getLong("endereco_id"));
                Optional<Empresa> empresaOpt = empresaDAO.obterPorId(rs.getLong("empresa_id"));
                Supervisor supervisor = null;
                try {
                    supervisor = supervisorDAO.obterPorId(rs.getLong("supervisor_id"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (enderecoOpt.isPresent() && empresaOpt.isPresent()) {
                    Vendedor vendedor = new Vendedor(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        enderecoOpt.get(),
                        empresaOpt.get(),
                        rs.getLong("id_vendedor"),
                        supervisor
                    );
                    return Optional.of(vendedor);
                }
            }
        }
        return Optional.empty();
    }

    public void atualizarPorId(long id, Vendedor vendedor) throws SQLException {
        String sql = """
                update vendedor
                set id_vendedor = ?,
                    nome = ?,
                    cpf = ?,
                    telefone = ?,
                    endereco_id = ?,
                    empresa_id = ?,
                    supervisor_id = ?
                where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, vendedor.getIdVendedor());
            ps.setString(2, vendedor.getNome());
            ps.setString(3, vendedor.getCpf());
            ps.setString(4, vendedor.getTelefone());
            ps.setLong(5, vendedor.getEndereco().getId());
            ps.setLong(6, vendedor.getEmpresa().getId());
            ps.setLong(7, vendedor.getSupervisor() != null ? vendedor.getSupervisor().getId() : 0);
            ps.setLong(8, id);

            ps.executeUpdate();
        }
    }

    public void deletarPorId(long id) throws SQLException {
        String sql = """
                delete from vendedor
                where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
} 