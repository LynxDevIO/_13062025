package pucgo.poobd._13062025.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pucgo.poobd._13062025.dao.FormaPagamentoDAO;
import pucgo.poobd._13062025.model.FormaPagamento;

public class FormaPagamentoDialogController {
    @FXML private DialogPane dialogPane;
    @FXML private TextField nomeField;
    
    private Stage dialogStage;
    private FormaPagamento formaPagamento;
    private boolean salvarClicked = false;

    @FXML
    public void initialize() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);
        
        nomeField.textProperty().addListener((obs, oldVal, newVal) -> {
            dialogPane.lookupButton(ButtonType.OK).setDisable(newVal.trim().isEmpty());
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
        
        if (formaPagamento != null) {
            nomeField.setText(formaPagamento.getNome());
        }
    }

    public boolean isSalvarClicked() {
        return salvarClicked;
    }

    @FXML
    private void handleSalvar() {
        if (isInputValid()) {
            formaPagamento = new FormaPagamento();
            formaPagamento.setNome(nomeField.getText());

            try {
                FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
                formaPagamentoDAO.criar(formaPagamento);
                salvarClicked = true;
                dialogStage.close();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Mostrar erro
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
        return errorMessage.length() == 0;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }
} 