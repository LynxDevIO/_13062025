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
import pucgo.poobd._13062025.model.Categoria;

public class CategoriaDAO {
    private final Connection conn;

    public CategoriaDAO() throws SQLException {
        this.conn = DatabaseFactory.getConnection();
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table categoria (
                        id integer primary key autoincrement,
                        descricao text
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Categoria categoria) {
        String sql = """
                insert into categoria (descricao)
                values (?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoria.getDescricao());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    categoria.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Categoria> obterPorId(long id) {
        Optional<Categoria> categoriaOpt = Optional.empty();
        String sql = """
                select * from categoria where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getLong("id"),
                        rs.getString("descricao"),
                        new ArrayList<>()
                );
                categoriaOpt = Optional.of(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoriaOpt;
    }

    public List<Categoria> obterTodos() {
        List<Categoria> categorias = new ArrayList<>();

        String sql = """
                select * from categoria
                """;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getLong("id"),
                        rs.getString("descricao"),
                        new ArrayList<>()
                );
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorias;
    }

    public void atualizarPorId(long id, Categoria categoria) {
        String sql = """
                update categoria
                set descricao = ?
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.getDescricao());
            ps.setLong(2, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from categoria
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