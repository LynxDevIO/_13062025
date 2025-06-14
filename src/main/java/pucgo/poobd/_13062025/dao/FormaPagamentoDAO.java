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
import pucgo.poobd._13062025.model.FormaPagamento;

public class FormaPagamentoDAO {
    private final Connection conn;

    public FormaPagamentoDAO() throws SQLException {
        this.conn = DatabaseFactory.getConnection();
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table forma_de_pagamento (
                        id integer primary key autoincrement,
                        nome text not null
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(FormaPagamento formaPagamento) {
        String sql = """
                insert into forma_de_pagamento (nome)
                values (?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, formaPagamento.getNome());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    formaPagamento.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<FormaPagamento> obterPorId(long id) {
        Optional<FormaPagamento> formaPagamentoOpt = Optional.empty();
        String sql = """
                select * from forma_de_pagamento where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                FormaPagamento formaPagamento = new FormaPagamento(
                    rs.getLong("id"),
                    rs.getString("nome")
                );
                formaPagamentoOpt = Optional.of(formaPagamento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return formaPagamentoOpt;
    }

    public List<FormaPagamento> obterTodos() {
        List<FormaPagamento> formasPagamento = new ArrayList<>();

        String sql = """
                select * from forma_de_pagamento
                """;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                FormaPagamento formaPagamento = new FormaPagamento(
                    rs.getLong("id"),
                    rs.getString("nome")
                );
                formasPagamento.add(formaPagamento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return formasPagamento;
    }

    public void atualizarPorId(long id, FormaPagamento formaPagamento) {
        String sql = """
                update forma_de_pagamento
                set nome = ?
                where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, formaPagamento.getNome());
            ps.setLong(2, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorId(long id) {
        String sql = """
                delete from forma_de_pagamento
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