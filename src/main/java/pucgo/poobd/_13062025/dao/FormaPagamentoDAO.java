package pucgo.poobd._13062025.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pucgo.poobd._13062025.model.FormaPagamento;

public class FormaPagamentoDAO {
    private final Connection conn;

    public FormaPagamentoDAO(Connection conn) {
        this.conn = conn;
    }

    public void inicializar() {
        try(Statement st = conn.createStatement()) {
            String sql = """
                    create table if not exists forma_pagamento (
                        id integer primary key autoincrement,
                        descricao text not null
                    )
                    """;

            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criar(FormaPagamento formaPagamento) throws SQLException {
        String sql = """
                insert into forma_pagamento (descricao)
                values (?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, formaPagamento.getDescricao());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    formaPagamento.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<FormaPagamento> obterTodos() throws SQLException {
        List<FormaPagamento> formasPagamento = new ArrayList<>();
        String sql = "select * from forma_pagamento";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                FormaPagamento formaPagamento = new FormaPagamento();
                formaPagamento.setId(rs.getLong("id"));
                formaPagamento.setDescricao(rs.getString("descricao"));
                formasPagamento.add(formaPagamento);
            }
        }
        return formasPagamento;
    }

    public Optional<FormaPagamento> obterPorId(long id) throws SQLException {
        String sql = "select * from forma_pagamento where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                FormaPagamento formaPagamento = new FormaPagamento();
                formaPagamento.setId(rs.getLong("id"));
                formaPagamento.setDescricao(rs.getString("descricao"));
                return Optional.of(formaPagamento);
            }
        }
        return Optional.empty();
    }

    public void atualizarPorId(long id, FormaPagamento formaPagamento) throws SQLException {
        String sql = """
                update forma_pagamento
                set descricao = ?
                where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, formaPagamento.getDescricao());
            ps.setLong(2, id);

            ps.executeUpdate();
        }
    }

    public void deletarPorId(long id) throws SQLException {
        String sql = """
                delete from forma_pagamento
                where id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
} 