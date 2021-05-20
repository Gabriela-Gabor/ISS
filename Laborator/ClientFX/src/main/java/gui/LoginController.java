package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Bug;
import model.Developer;
import model.Tester;
import org.hibernate.SessionFactory;
import persistence.repository.BugRepository;
import persistence.repository.DeveloperRepository;
import persistence.repository.TesterRepository;

import java.util.Properties;

public class LoginController {

    private DeveloperRepository developerRepository;
    private TesterRepository testerRepository;
    private BugRepository bugRepository;
    private Stage stageMain;

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordTextField;


    public void initiateLoginProcedure(Stage stageMain,DeveloperRepository developerRepository,TesterRepository testerRepository,BugRepository bugRepository){
        this.stageMain = stageMain;
        this.developerRepository = developerRepository;
        this.testerRepository = testerRepository;
        this.bugRepository=bugRepository;

    }


    public void checkUser(ActionEvent actionEvent) {

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (username != "" && password != "") {
            Developer developer = developerRepository.findOne(username);
            if (developer != null) {
                if (developer.getPassword().equals(password)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/developerWindow.fxml"));
                        Parent root = loader.load();
                        DeveloperController developerController = loader.getController();
                        developerController.initiate(developer,bugRepository);

                        Stage stage = new Stage();
                        stage.setTitle("Window for " + developer.getName());
                        stage.setScene(new Scene(root, 600, 350));

                        //stageMain.close();
                        stage.show();

                    } catch (Exception ex) {
                        MessageAlert.showWarningMessage(null, "Error");
                    }

                } else {

                    MessageAlert.showWarningMessage(null, "wrong password. please try again!");
                }
            } else {
                Tester tester = testerRepository.findOne(username);
                if (tester != null) {
                    if (tester.getPassword().equals(password)) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/testerWindow.fxml"));
                            Parent root = loader.load();
                            TesterController testerController = loader.getController();
                            testerController.initiate(tester,bugRepository);

                            Stage stage = new Stage();
                            stage.setTitle("Window for " + tester.getName());
                            stage.setScene(new Scene(root, 600, 350));

                            //stageMain.close();
                            stage.show();

                        } catch (Exception ex) {
                            MessageAlert.showWarningMessage(null, "Error");
                        }


                    } else {
                        MessageAlert.showWarningMessage(null, "wrong password. please try again!");
                    }

                }else{
                    MessageAlert.showWarningMessage(null, "wrong user. please try again!");
                }

            }

        } else {
            MessageAlert.showWarningMessage(null, "Please enter a valid username and password!");
        }


    }
}
