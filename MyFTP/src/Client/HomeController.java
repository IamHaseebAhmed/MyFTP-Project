package Client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class HomeController extends Credentials {

    @FXML
    private Button connectBtn, disconnectBtn, fileUploadBtn, downloadFile;

    @FXML
    private TextField userName, userPass, serverIP, fileNameToUpload, fileNameToReceive;

    @FXML
    private Pane statusColor;

    @FXML
    private Label statusMessage, loggingTxt;

    Socket soc;
    TransferFileClient tfc;

    @FXML
    void connect(ActionEvent event) throws Exception {

        String username = userName.getText();
        String password = userPass.getText();
        String ip_to_connect = serverIP.getText();
        Credentials cd = new Credentials();

        if ( username.equals(cd.getUSERNAME()) && password.equals(cd.getPASSWORD()) ) {
            soc = new Socket(ip_to_connect, cd.getPORT());
            tfc = new TransferFileClient(soc);
            loggingTxt.setText("Client Connected to Server at " + ip_to_connect + "\n");
        }
        else {
            System.out.println("Invalid Credentials !!");
            return;
        }

        // CONNECTED: Sets the connection status color to green
        statusColor.setStyle("-fx-background-color: green;");
        statusMessage.setText("CONNECTED");
    }

    @FXML
    void disconnect(ActionEvent event) throws Exception {

        loggingTxt.setText("Client disconnected with Server" + "\n");
        tfc.dout.writeUTF("DISCONNECT");
        statusColor.setStyle("-fx-background-color: red;");
        statusMessage.setText("DISCONNECTED");
    }

    @FXML
    void UploadFile(ActionEvent event) throws Exception {

        try {
            String fileName = fileNameToUpload.getText().trim();
            tfc.dout.writeUTF("SEND");
            tfc.SendFile(fileName);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    void DownloadFile(ActionEvent event) throws Exception {

        try {
            String fileName = fileNameToReceive.getText().trim();
            tfc.dout.writeUTF("GET");
            tfc.ReceiveFile(fileName);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}