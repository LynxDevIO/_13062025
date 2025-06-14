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
import pucgo.poobd._13062025.model.Fabricante;
import pucgo.poobd._13062025.model.Produto;
import pucgo.poobd._13062025.model.Subcategoria;

public class ProdutoDAO {
    private final Connection conn;
    private final CategoriaDAO categoriaDAO;
    private final SubcategoriaDAO subcategoriaDAO;
    private final FabricanteDAO fabricanteDAO;

    public ProdutoDAO(Connection conn) {
        this.conn = conn;
        this.categoriaDAO = new CategoriaDAO(conn);
        this.subcategoriaDAO = new SubcategoriaDAO(conn);
        this.fabricanteDAO = new FabricanteDAO(conn);
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table if not exists produto (
                        id integer primary key autoincrement,
                        nome text not null,
                        descricao text,
                        valor_unitario real not null,
                        quantidade_estoque integer not null,
                        categoria_fk integer,
                        subcategoria_fk integer,
                        fabricante_fk integer,
                        fk_fabricante_id integer,
                        fk_subcategoria_id integer,
                        foreign key (fk_fabricante_id) references fabricante(id),
                        foreign key (fk_subcategoria_id) references subcategoria(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Produto produto) {
        String sql = """
                insert into produto (nome, descricao, valor_unitario, quantidade_estoque, 
                                   categoria_fk, subcategoria_fk, fabricante_fk, 
                                   fk_fabricante_id, fk_subcategoria_id)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getDescricao());
            ps.setDouble(3, produto.getValorUnitario());
            ps.setLong(4, produto.getQuantidadeEstoque());
            ps.setLong(5, produto.getCategoria().getId());
            ps.setLong(6, produto.getSubcategoria().getId());
            ps.setLong(7, produto.getFabricante().getId());
            ps.setLong(8, produto.getFabricante().getId());
            ps.setLong(9, produto.getSubcategoria().getId());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Produto> obterPorId(long id) {
        Optional<Produto> produtoOpt = Optional.empty();
        String sql = "select * from produto where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Optional<Categoria> categoria = categoriaDAO.obterPorId(rs.getLong("categoria_fk"));
                Optional<Subcategoria> subcategoria = subcategoriaDAO.obterPorId(rs.getLong("subcategoria_fk"));
                Optional<Fabricante> fabricante = fabricanteDAO.obterPorId(rs.getLong("fabricante_fk"));
                if (categoria.isPresent() && subcategoria.isPresent() && fabricante.isPresent()) {
                    Produto produto = new Produto(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("valor_unitario"),
                        rs.getLong("quantidade_estoque"),
                        categoria.get(),
                        subcategoria.get(),
                        fabricante.get()
                    );
                    produtoOpt = Optional.of(produto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtoOpt;
    }

    public List<Produto> obterTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "select * from produto";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Optional<Categoria> categoria = categoriaDAO.obterPorId(rs.getLong("categoria_fk"));
                Optional<Subcategoria> subcategoria = subcategoriaDAO.obterPorId(rs.getLong("subcategoria_fk"));
                Optional<Fabricante> fabricante = fabricanteDAO.obterPorId(rs.getLong("fabricante_fk"));
                if (categoria.isPresent() && subcategoria.isPresent() && fabricante.isPresent()) {
                    Produto produto = new Produto(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("valor_unitario"),
                        rs.getLong("quantidade_estoque"),
                        categoria.get(),
                        subcategoria.get(),
                        fabricante.get()
                    );
                    produtos.add(produto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public void atualizarPorId(long id, Produto produto) {
        String sql = """
                update produto
                set nome = ?,
                    descricao = ?,
                    valor_unitario = ?,
                    quantidade_estoque = ?,
                    categoria_fk = ?,
                    subcategoria_fk = ?,
                    fabricante_fk = ?,
                    fk_fabricante_id = ?,
                    fk_subcategoria_id = ?
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getDescricao());
            ps.setDouble(3, produto.getValorUnitario());
            ps.setLong(4, produto.getQuantidadeEstoque());
            ps.setLong(5, produto.getCategoria().getId());
            ps.setLong(6, produto.getSubcategoria().getId());
            ps.setLong(7, produto.getFabricante().getId());
            ps.setLong(8, produto.getFabricante().getId());
            ps.setLong(9, produto.getSubcategoria().getId());
            ps.setLong(10, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from produto
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