package pucgo.poobd._13062025;

import java.sql.Connection;

import javafx.application.Application;
import pucgo.poobd._13062025.dao.CategoriaDAO;
import pucgo.poobd._13062025.dao.ClienteDAO;
import pucgo.poobd._13062025.dao.EmpresaDAO;
import pucgo.poobd._13062025.dao.EnderecoDAO;
import pucgo.poobd._13062025.dao.FormaPagamentoDAO;
import pucgo.poobd._13062025.dao.ItemPedidoDAO;
import pucgo.poobd._13062025.dao.PedidoDAO;
import pucgo.poobd._13062025.dao.ProdutoDAO;
import pucgo.poobd._13062025.dao.SubcategoriaDAO;
import pucgo.poobd._13062025.dao.SupervisorDAO;
import pucgo.poobd._13062025.dao.VendedorDAO;
import pucgo.poobd._13062025.database.DatabaseFactory;
import pucgo.poobd._13062025.view.MainView;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseFactory.getConnection();

            EnderecoDAO enderecoDAO = new EnderecoDAO(conn);
            EmpresaDAO empresaDAO = new EmpresaDAO(conn);
            CategoriaDAO categoriaDAO = new CategoriaDAO(conn);
            SubcategoriaDAO subcategoriaDAO = new SubcategoriaDAO(conn);
            ProdutoDAO produtoDAO = new ProdutoDAO(conn);
            FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO(conn);
            ClienteDAO clienteDAO = new ClienteDAO(conn);
            SupervisorDAO supervisorDAO = new SupervisorDAO(conn);
            VendedorDAO vendedorDAO = new VendedorDAO(conn);
            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(conn);
            PedidoDAO pedidoDAO = new PedidoDAO(conn);
            itemPedidoDAO.setPedidoDAO(pedidoDAO);
            pedidoDAO.setItemPedidoDAO(itemPedidoDAO);

            enderecoDAO.inicializar();
            empresaDAO.inicializar();
            categoriaDAO.inicializar();
            subcategoriaDAO.inicializar();
            produtoDAO.inicializar();
            formaPagamentoDAO.inicializar();
            clienteDAO.inicializar();
            supervisorDAO.inicializar();
            vendedorDAO.inicializar();
            pedidoDAO.inicializar();
            itemPedidoDAO.inicializar();

            MainView.setConnection(conn);
            Application.launch(MainView.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            System.exit(1);
        } finally {
            DatabaseFactory.closeConnection();
        }
    }
}
