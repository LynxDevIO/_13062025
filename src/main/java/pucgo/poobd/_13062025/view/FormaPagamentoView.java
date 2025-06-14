package pucgo.poobd._13062025.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import pucgo.poobd._13062025.controller.FormaPagamentoDialogController;
import pucgo.poobd._13062025.model.FormaPagamento;

import java.util.Optional;

public class FormaPagamentoView {
    public static Optional<FormaPagamento> showDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(FormaPagamentoView.class.getResource("/pucgo/poobd/_13062025/view/forma-pagamento-dialog.fxml"));
            DialogPane pane = loader.load();
            FormaPagamentoDialogController controller = loader.getController();
            
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Nova Forma de Pagamento");
            
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                return Optional.ofNullable(controller.getFormaPagamento());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
} 