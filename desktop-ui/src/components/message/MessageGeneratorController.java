package components.message;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageGeneratorController {

    @FXML private Label messageLabel;

    public void setMessageLabelText(String newMessage) {
        this.messageLabel.setText(newMessage);
    }
}
