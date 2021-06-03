package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Bug;
import model.Developer;
import model.StatusType;
import observer.Observer;
import persistence.repository.BugRepository;


public class DeveloperController implements Observer {

    private BugRepository bugRepository;
    private Developer curentUser;
    ObservableList<Bug> modelBug = FXCollections.observableArrayList();

    @FXML
    TextField descriptionTextField;

    @FXML
    ListView bugsListView;


    public void initiate(Developer developer, BugRepository bugRepository) {
        this.curentUser = developer;
        this.bugRepository = bugRepository;
        this.bugRepository.addObserver(this);
        getBugs();

    }

    private void getBugs() {
        bugsListView.setItems(modelBug);
        try {
            modelBug.setAll(bugRepository.getUnfinishedBugs());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseBug(ActionEvent actionEvent) {
        Bug bug = (Bug) bugsListView.getSelectionModel().getSelectedItem();
        if (bug != null) {
            if (bug.getStatusType() == StatusType.Assigned) {
                MessageAlert.showWarningMessage(null, "Another person is working on this bug");
            } else {
                bug.setAssignedDeveloper(curentUser.getUsername());
                bug.setStatusType(StatusType.Assigned);
                bugRepository.update(bug);
            }

        } else {
            MessageAlert.showWarningMessage(null, "Please select a bug from the list");

        }
    }

    public void solveBug(ActionEvent actionEvent) {
        Bug bug = (Bug) bugsListView.getSelectionModel().getSelectedItem();
        if (bug != null) {
            if (bug.getStatusType() == StatusType.Assigned) {
                if (bug.getAssignedDeveloper().equals(curentUser.getUsername())) {
                    String solutionDescription = descriptionTextField.getText();
                    if (solutionDescription.equals("")) {
                        MessageAlert.showWarningMessage(null, "Please enter a description");
                    } else {
                        bug.setStatusType(StatusType.Finished);
                        bug.setSolutionDescription(solutionDescription);
                        bugRepository.update(bug);
                        descriptionTextField.clear();
                    }
                } else {
                    MessageAlert.showWarningMessage(null, "Another person is working on this bug");
                }
            } else {
                MessageAlert.showWarningMessage(null, "You have to choose the bug first");
            }
        } else {
            MessageAlert.showWarningMessage(null, "Please select a bug from the list");

        }

    }

    public void getFinished(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/finishedWindow.fxml"));
            Parent root = loader.load();
            FinishedBugsController finishedBugsController = loader.getController();
            finishedBugsController.initiate(bugRepository);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 350));

            stage.show();

        } catch (Exception ex) {
            MessageAlert.showWarningMessage(null, "Error");
        }
    }


    @Override
    public void update() {
        modelBug.setAll(bugRepository.getUnfinishedBugs());

    }
}