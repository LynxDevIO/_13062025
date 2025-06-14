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

public class EmpresaDAO {
    private final Connection conn;
    private final EnderecoDAO enderecoDAO;

    public EmpresaDAO(Connection conn) {
        this.conn = conn;
        this.enderecoDAO = new EnderecoDAO(conn);
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table if not exists empresa (
                        id integer primary key autoincrement,
                        cnpj text not null unique,
                        nome_fantasia text not null,
                        razao_social text not null,
                        endereco_id integer,
                        foreign key (endereco_id) references endereco(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Empresa empresa) throws SQLException {
        String sql = "insert into empresa (cnpj, nome_fantasia, razao_social, endereco_id) values (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, empresa.getCnpj());
            stmt.setString(2, empresa.getNomeFantasia());
            stmt.setString(3, empresa.getRazaoSocial());
            stmt.setLong(4, empresa.getEndereco().getId());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    empresa.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Empresa> obterTodos() throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "select * from empresa";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Empresa empresa = new Empresa();
                empresa.setId(rs.getLong("id"));
                empresa.setCnpj(rs.getString("cnpj"));
                empresa.setNomeFantasia(rs.getString("nome_fantasia"));
                empresa.setRazaoSocial(rs.getString("razao_social"));
                long enderecoId = rs.getLong("endereco_id");
                Optional<Endereco> enderecoOpt = enderecoDAO.obterPorId(enderecoId);
                enderecoOpt.ifPresent(empresa::setEndereco);
                empresas.add(empresa);
            }
        }
        return empresas;
    }

    public Optional<Empresa> obterPorId(long id) throws SQLException {
        String sql = "select * from empresa where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Empresa empresa = new Empresa();
                    empresa.setId(rs.getLong("id"));
                    empresa.setCnpj(rs.getString("cnpj"));
                    empresa.setNomeFantasia(rs.getString("nome_fantasia"));
                    empresa.setRazaoSocial(rs.getString("razao_social"));
                    long enderecoId = rs.getLong("endereco_id");
                    Optional<Endereco> enderecoOpt = enderecoDAO.obterPorId(enderecoId);
                    enderecoOpt.ifPresent(empresa::setEndereco);
                    return Optional.of(empresa);
                }
            }
        }
        return Optional.empty();
    }

    public void atualizarPorId(long id, Empresa empresa) throws SQLException {
        String sql = "update empresa set cnpj = ?, nome_fantasia = ?, razao_social = ?, endereco_id = ? where id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, empresa.getCnpj());
            stmt.setString(2, empresa.getNomeFantasia());
            stmt.setString(3, empresa.getRazaoSocial());
            stmt.setLong(4, empresa.getEndereco().getId());
            stmt.setLong(5, id);
            
            stmt.executeUpdate();
        }
    }

    public void deletarPorId(long id) throws SQLException {
        String sql = "delete from empresa where id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
