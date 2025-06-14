package pucgo.poobd._13062025.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pucgo.poobd._13062025.dao.EmpresaDAO;
import pucgo.poobd._13062025.model.Empresa;
import pucgo.poobd._13062025.model.Endereco;

public class EmpresaDialogController {
    @FXML private TextField nomeField;
    @FXML private TextField cnpjField;
    @FXML private TextField enderecoField;
    
    private Stage dialogStage;
    private Empresa empresa;
    private boolean salvarClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        
        if (empresa != null) {
            nomeField.setText(empresa.getRazaoSocial());
            cnpjField.setText(empresa.getCnpj());
            enderecoField.setText(empresa.getEndereco().toString());
        }
    }

    public boolean isSalvarClicked() {
        return salvarClicked;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    @FXML
    private void handleSalvar() {
        if (isInputValid()) {
            empresa = new Empresa();
            empresa.setRazaoSocial(nomeField.getText());
            empresa.setCnpj(cnpjField.getText());
            String[] partes = enderecoField.getText().split(",");
            if (partes.length == 6) {
                Endereco endereco = new Endereco(0, partes[0].trim(), partes[1].trim(), partes[2].trim(), partes[3].trim(), partes[4].trim(), partes[5].trim());
                empresa.setEndereco(endereco);
            }
            try {
                EmpresaDAO empresaDAO = new EmpresaDAO();
                empresaDAO.criar(empresa);
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
        if (cnpjField.getText() == null || cnpjField.getText().length() == 0) {
            errorMessage += "CNPJ inválido!\n";
        }
        if (enderecoField.getText() == null || enderecoField.getText().length() == 0) {
            errorMessage += "Endereço inválido!\n";
        }
        return errorMessage.length() == 0;
    }
} 