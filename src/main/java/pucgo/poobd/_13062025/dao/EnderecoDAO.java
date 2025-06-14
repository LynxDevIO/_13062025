package pucgo.poobd._13062025.dao;

import pucgo.poobd._13062025.database.DatabaseFactory;
import pucgo.poobd._13062025.model.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderecoDAO {
    private final Connection conn;

    public EnderecoDAO() throws SQLException {
        this.conn = DatabaseFactory.getConnection();
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table endereco (
                        id integer primary key autoincrement,
                        rua text not null,
                        complemento text,
                        bairro text not null,
                        cidade text not null,
                        estado text not null,
                        cep text not null
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Endereco endereco) {
        String sql = """
                insert into endereco (rua, complemento, bairro, cidade, estado, cep)
                values (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, endereco.getRua());
            ps.setString(2, endereco.getComplemento());
            ps.setString(3, endereco.getBairro());
            ps.setString(4, endereco.getCidade());
            ps.setString(5, endereco.getEstado());
            ps.setString(6, endereco.getCep());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    endereco.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Endereco> obterPorId(long id) {
        Optional<Endereco> enderecoOpt = Optional.empty();
        String sql = """
                select * from endereco where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getLong("id"),
                        rs.getString("rua"),
                        rs.getString("complemento"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep")
                );
                enderecoOpt = Optional.of(endereco);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enderecoOpt;
    }

    public List<Endereco> obterTodos() {
        List<Endereco> enderecos = new ArrayList<>();

        String sql = """
                select * from endereco
                """;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getLong("id"),
                        rs.getString("rua"),
                        rs.getString("complemento"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep")
                );
                enderecos.add(endereco);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enderecos;
    }

    public void atualizarPorId(long id, Endereco endereco) {
        String sql = """
                update endereco
                set rua = ?,
                    complemento = ?,
                    bairro = ?,
                    cidade = ?,
                    estado = ?,
                    cep = ?
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, endereco.getRua());
            ps.setString(2, endereco.getComplemento());
            ps.setString(3, endereco.getBairro());
            ps.setString(4, endereco.getCidade());
            ps.setString(5, endereco.getEstado());
            ps.setString(6, endereco.getCep());
            ps.setLong(7, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from endereco
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