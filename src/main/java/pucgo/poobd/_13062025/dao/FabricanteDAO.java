package pucgo.poobd._13062025.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pucgo.poobd._13062025.model.Endereco;
import pucgo.poobd._13062025.model.Fabricante;

public class FabricanteDAO {
    private final Connection conn;
    private final EnderecoDAO enderecoDAO;

    public FabricanteDAO(Connection conn) {
        this.conn = conn;
        this.enderecoDAO = new EnderecoDAO(conn);
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table if not exists fabricante (
                        id integer primary key autoincrement,
                        nome text not null,
                        cnpj text not null,
                        endereco_fk integer,
                        foreign key (endereco_fk) references endereco(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Fabricante fabricante) {
        String sql = """
                insert into fabricante (nome, cnpj, endereco_fk)
                values (?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, fabricante.getNome());
            ps.setString(2, fabricante.getCnpj());
            ps.setLong(3, fabricante.getEndereco().getId());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    fabricante.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Fabricante> obterPorId(long id) {
        Optional<Fabricante> fabricanteOpt = Optional.empty();
        String sql = """
                select * from fabricante where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Optional<Endereco> endereco = enderecoDAO.obterPorId(rs.getLong("endereco_fk"));
                if (endereco.isPresent()) {
                    Fabricante fabricante = new Fabricante(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            endereco.get()
                    );
                    fabricanteOpt = Optional.of(fabricante);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fabricanteOpt;
    }

    public List<Fabricante> obterTodos() {
        List<Fabricante> fabricantes = new ArrayList<>();

        String sql = """
                select * from fabricante
                """;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Optional<Endereco> endereco = enderecoDAO.obterPorId(rs.getLong("endereco_fk"));
                if (endereco.isPresent()) {
                    Fabricante fabricante = new Fabricante(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            endereco.get()
                    );
                    fabricantes.add(fabricante);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fabricantes;
    }

    public void atualizarPorId(long id, Fabricante fabricante) {
        String sql = """
                update fabricante
                set nome = ?,
                    cnpj = ?,
                    endereco_fk = ?
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fabricante.getNome());
            ps.setString(2, fabricante.getCnpj());
            ps.setLong(3, fabricante.getEndereco().getId());
            ps.setLong(4, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from fabricante
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