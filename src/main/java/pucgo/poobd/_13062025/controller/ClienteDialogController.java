package pucgo.poobd._13062025.controller;

import java.sql.Connection;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pucgo.poobd._13062025.dao.ClienteDAO;
import pucgo.poobd._13062025.model.Cliente;
import pucgo.poobd._13062025.model.Endereco;

public class ClienteDialogController {
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField telefoneField;
    @FXML private DatePicker dataNascimentoPicker;
    @FXML private TextField enderecoField;
    
    private Stage dialogStage;
    private Cliente cliente;
    private boolean salvarClicked = false;
    private Connection conn;
    private ClienteDAO clienteDAO;

    public void setConnection(Connection conn) {
        this.conn = conn;
        this.clienteDAO = new ClienteDAO(conn);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        
        if (cliente != null) {
            nomeField.setText(cliente.getNome());
            cpfField.setText(cliente.getCpf());
            telefoneField.setText(cliente.getTelefone());
            dataNascimentoPicker.setValue(cliente.getDataNascimento());
            enderecoField.setText(cliente.getEndereco().toString());
        }
    }

    public boolean isSalvarClicked() {
        return salvarClicked;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @FXML
    private void handleSalvar() {
        if (isInputValid()) {
            cliente = new Cliente();
            cliente.setNome(nomeField.getText());
            cliente.setCpf(cpfField.getText());
            cliente.setTelefone(telefoneField.getText());
            cliente.setDataNascimento(dataNascimentoPicker.getValue());
            String[] partes = enderecoField.getText().split(",");
            if (partes.length == 6) {
                Endereco endereco = new Endereco(0, partes[0].trim(), partes[1].trim(), partes[2].trim(), partes[3].trim(), partes[4].trim(), partes[5].trim());
                cliente.setEndereco(endereco);
            }
            try {
                clienteDAO.criar(cliente);
                salvarClicked = true;
                dialogStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (cpfField.getText() == null || cpfField.getText().length() == 0) {
            errorMessage += "CPF inválido!\n";
        }
        if (telefoneField.getText() == null || telefoneField.getText().length() == 0) {
            errorMessage += "Telefone inválido!\n";
        }
        if (dataNascimentoPicker.getValue() == null) {
            errorMessage += "Data de nascimento inválida!\n";
        }
        if (enderecoField.getText() == null || enderecoField.getText().length() == 0) {
            errorMessage += "Endereço inválido!\n";
        }
        return errorMessage.length() == 0;
    }
} 