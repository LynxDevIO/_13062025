package pucgo.poobd._13062025.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pucgo.poobd._13062025.model.Categoria;
import pucgo.poobd._13062025.model.Subcategoria;

public class SubcategoriaDAO {
    private final Connection conn;
    private final CategoriaDAO categoriaDAO;

    public SubcategoriaDAO(Connection conn) {
        this.conn = conn;
        this.categoriaDAO = new CategoriaDAO(conn);
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table if not exists subcategoria (
                        id integer primary key autoincrement,
                        descricao text,
                        categoria_fk integer,
                        fk_categoria_id integer,
                        foreign key (fk_categoria_id) references categoria(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Subcategoria subcategoria) {
        String sql = """
                insert into subcategoria (descricao, categoria_fk, fk_categoria_id)
                values (?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, subcategoria.getDescricao());
            ps.setLong(2, subcategoria.getCategoria().getId());
            ps.setLong(3, subcategoria.getCategoria().getId());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    subcategoria.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Subcategoria> obterPorId(long id) {
        Optional<Subcategoria> subcategoriaOpt = Optional.empty();
        String sql = """
                select * from subcategoria where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Optional<Categoria> categoria = categoriaDAO.obterPorId(rs.getLong("categoria_fk"));
                if (categoria.isPresent()) {
                    Subcategoria subcategoria = new Subcategoria(
                            rs.getLong("id"),
                            rs.getString("descricao"),
                            categoria.get()
                    );
                    subcategoriaOpt = Optional.of(subcategoria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subcategoriaOpt;
    }

    public List<Subcategoria> obterTodos() {
        List<Subcategoria> subcategorias = new ArrayList<>();

        String sql = """
                select * from subcategoria
                """;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Optional<Categoria> categoria = categoriaDAO.obterPorId(rs.getLong("categoria_fk"));
                if (categoria.isPresent()) {
                    Subcategoria subcategoria = new Subcategoria(
                            rs.getLong("id"),
                            rs.getString("descricao"),
                            categoria.get()
                    );
                    subcategorias.add(subcategoria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subcategorias;
    }

    public void atualizarPorId(long id, Subcategoria subcategoria) {
        String sql = """
                update subcategoria
                set descricao = ?,
                    categoria_fk = ?,
                    fk_categoria_id = ?
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subcategoria.getDescricao());
            ps.setLong(2, subcategoria.getCategoria().getId());
            ps.setLong(3, subcategoria.getCategoria().getId());
            ps.setLong(4, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from subcategoria
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