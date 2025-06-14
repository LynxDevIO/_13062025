package pucgo.poobd._13062025.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pucgo.poobd._13062025.model.Empresa;
import pucgo.poobd._13062025.model.Endereco;
import pucgo.poobd._13062025.model.Supervisor;

public class SupervisorDAO {
    private final Connection conn;
    private final EnderecoDAO enderecoDAO;
    private final EmpresaDAO empresaDAO;

    public SupervisorDAO(Connection conn) {
        this.conn = conn;
        this.enderecoDAO = new EnderecoDAO(conn);
        this.empresaDAO = new EmpresaDAO(conn);
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table if not exists supervisor (
                        id integer primary key autoincrement,
                        id_supervisor integer not null,
                        nome text not null,
                        cpf text not null,
                        telefone text not null,
                        endereco_id integer,
                        empresa_id integer,
                        foreign key (endereco_id) references endereco(id),
                        foreign key (empresa_id) references empresa(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Supervisor supervisor) throws SQLException {
        String sql = """
                insert into supervisor (id_supervisor, nome, cpf, telefone, endereco_id, empresa_id)
                values (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, supervisor.getIdSupervisor());
            ps.setString(2, supervisor.getNome());
            ps.setString(3, supervisor.getCpf());
            ps.setString(4, supervisor.getTelefone());
            ps.setLong(5, supervisor.getEndereco().getId());
            ps.setLong(6, supervisor.getEmpresa().getId());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    supervisor.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<Supervisor> obterTodos() throws SQLException {
        List<Supervisor> supervisores = new ArrayList<>();
        String sql = "select * from supervisor";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Optional<Endereco> enderecoOpt = enderecoDAO.obterPorId(rs.getLong("endereco_id"));
                Optional<Empresa> empresaOpt = empresaDAO.obterPorId(rs.getLong("empresa_id"));
                if (enderecoOpt.isPresent() && empresaOpt.isPresent()) {
                    Supervisor supervisor = new Supervisor(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("telefone"),
                            enderecoOpt.get(),
                            empresaOpt.get(),
                            rs.getLong("id_supervisor")
                    );
                    supervisores.add(supervisor);
                }
            }
        }
        return supervisores;
    }

    public Supervisor obterPorId(long id) throws SQLException {
        String sql = "select * from supervisor where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Optional<Endereco> enderecoOpt = enderecoDAO.obterPorId(rs.getLong("endereco_id"));
                Optional<Empresa> empresaOpt = empresaDAO.obterPorId(rs.getLong("empresa_id"));
                if (enderecoOpt.isPresent() && empresaOpt.isPresent()) {
                    return new Supervisor(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("telefone"),
                            enderecoOpt.get(),
                            empresaOpt.get(),
                            rs.getLong("id_supervisor")
                    );
                }
            }
        }
        return null;
    }

    public void atualizarPorId(long id, Supervisor supervisor) throws SQLException {
        String sql = """
                update supervisor
                set id_supervisor = ?,
                    nome = ?,
                    cpf = ?,
                    telefone = ?,
                    endereco_id = ?,
                    empresa_id = ?
                where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, supervisor.getIdSupervisor());
            ps.setString(2, supervisor.getNome());
            ps.setString(3, supervisor.getCpf());
            ps.setString(4, supervisor.getTelefone());
            ps.setLong(5, supervisor.getEndereco().getId());
            ps.setLong(6, supervisor.getEmpresa().getId());
            ps.setLong(7, id);

            ps.executeUpdate();
        }
    }

    public void deletarPorId(long id) throws SQLException {
        String sql = """
                delete from supervisor
                where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
} 