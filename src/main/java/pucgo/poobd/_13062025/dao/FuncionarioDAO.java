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
import pucgo.poobd._13062025.model.Endereco;
import pucgo.poobd._13062025.model.Funcionario;
import pucgo.poobd._13062025.model.Vendedor;

public class FuncionarioDAO {
    private final Connection conn;
    private final EnderecoDAO enderecoDAO;

    public FuncionarioDAO() throws SQLException {
        this.conn = DatabaseFactory.getConnection();
        this.enderecoDAO = new EnderecoDAO();
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table Funcionario (
                        id integer primary key autoincrement,
                        nome text not null,
                        cpf text not null,
                        telefone text not null,
                        endereco_fk integer,
                        foreign key (endereco_fk) references endereco(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Funcionario funcionario) {
        String sql = """
                insert into Funcionario (nome, cpf, telefone, endereco_fk)
                values (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, funcionario.getNome());
            ps.setString(2, funcionario.getCpf());
            ps.setString(3, funcionario.getTelefone());
            ps.setLong(4, funcionario.getEndereco().getId());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    funcionario.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Funcionario> obterPorId(long id) {
        Optional<Funcionario> funcionarioOpt = Optional.empty();
        String sql = """
                select * from Funcionario where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Optional<Endereco> endereco = enderecoDAO.obterPorId(rs.getLong("endereco_fk"));
                if (endereco.isPresent()) {
                    // Como Funcionario é abstrato, vamos criar um Vendedor por padrão
                    // O VendedorDAO irá sobrescrever isso se necessário
                    Vendedor vendedor = new Vendedor(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("telefone"),
                            endereco.get(),
                            null,
                            rs.getLong("id"),
                            null
                    );
                    funcionarioOpt = Optional.of(vendedor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarioOpt;
    }

    public List<Funcionario> obterTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();

        String sql = """
                select * from Funcionario
                """;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Optional<Endereco> endereco = enderecoDAO.obterPorId(rs.getLong("endereco_fk"));
                if (endereco.isPresent()) {
                    // Como Funcionario é abstrato, vamos criar um Vendedor por padrão
                    // O VendedorDAO irá sobrescrever isso se necessário
                    Vendedor vendedor = new Vendedor(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("telefone"),
                            endereco.get(),
                            null,
                            rs.getLong("id"),
                            null
                    );
                    funcionarios.add(vendedor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }

    public void atualizarPorId(long id, Funcionario funcionario) {
        String sql = """
                update Funcionario
                set nome = ?,
                    cpf = ?,
                    telefone = ?,
                    endereco_fk = ?
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setString(2, funcionario.getCpf());
            ps.setString(3, funcionario.getTelefone());
            ps.setLong(4, funcionario.getEndereco().getId());
            ps.setLong(5, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from Funcionario
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