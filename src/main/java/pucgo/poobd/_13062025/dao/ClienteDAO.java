package pucgo.poobd._13062025.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pucgo.poobd._13062025.model.Cliente;
import pucgo.poobd._13062025.model.Endereco;

public class ClienteDAO {
    private final Connection conn;
    private final EnderecoDAO enderecoDAO;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public ClienteDAO(Connection conn) {
        this.conn = conn;
        this.enderecoDAO = new EnderecoDAO(conn);
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table if not exists cliente (
                        id integer primary key autoincrement,
                        nome text not null,
                        cpf text not null,
                        telefone text not null,
                        data_nascimento text not null,
                        endereco_fk integer,
                        foreign key (endereco_fk) references endereco(id)
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(Cliente cliente) {
        String sql = """
                insert into cliente (nome, cpf, telefone, data_nascimento, endereco_fk)
                values (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getTelefone());
            ps.setString(4, cliente.getDataNascimento().format(formatter));
            ps.setLong(5, cliente.getEndereco().getId());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Cliente> obterPorId(long id) {
        Optional<Cliente> clienteOpt = Optional.empty();
        String sql = """
                select * from cliente where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Optional<Endereco> endereco = enderecoDAO.obterPorId(rs.getLong("endereco_fk"));
                if (endereco.isPresent()) {
                    Cliente cliente = new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        LocalDate.parse(rs.getString("data_nascimento"), formatter),
                        endereco.get(),
                        null // empresa
                    );
                    clienteOpt = Optional.of(cliente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clienteOpt;
    }

    public List<Cliente> obterTodos() {
        List<Cliente> clientes = new ArrayList<>();

        String sql = """
                select * from cliente
                """;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Optional<Endereco> endereco = enderecoDAO.obterPorId(rs.getLong("endereco_fk"));
                if (endereco.isPresent()) {
                    Cliente cliente = new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        LocalDate.parse(rs.getString("data_nascimento"), formatter),
                        endereco.get(),
                        null // empresa
                    );
                    clientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public void atualizarPorId(long id, Cliente cliente) {
        String sql = """
                update cliente
                set nome = ?,
                    cpf = ?,
                    telefone = ?,
                    data_nascimento = ?,
                    endereco_fk = ?
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getTelefone());
            ps.setString(4, cliente.getDataNascimento().format(formatter));
            ps.setLong(5, cliente.getEndereco().getId());
            ps.setLong(6, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from cliente
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