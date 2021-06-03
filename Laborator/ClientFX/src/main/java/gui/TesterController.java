package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Bug;
import model.Tester;
import observer.Observer;
import persistence.repository.BugRepository;


public class TesterController implements Observer {

    private BugRepository bugRepository;
    private Tester curentUser;

    ObservableList<Bug> modelBug = FXCollections.observableArrayList();


    @FXML
    TextField nameTextField;

    @FXML
    TextField descriptionTextField;

    @FXML
    ListView bugsListView;

    public void initiate(Tester tester, BugRepository bugRepository) {

        this.curentUser = tester;
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

    public void saveBug(ActionEvent actionEvent) {
        String name = nameTextField.getText();
        String description = descriptionTextField.getText();
        if (name != "" && description != "") {
            Bug bug = new Bug(name, description);
            bugRepository.save(bug);
            nameTextField.clear();
            descriptionTextField.clear();

        } else {
            MessageAlert.showWarningMessage(null, "Please enter a name and description!");
        }
    }

    public void deleteBug(ActionEvent actionEvent) {

        Bug bug = (Bug) bugsListView.getSelectionModel().getSelectedItem();
        if (bug != null) {
            bugRepository.delete(bug.getName());
        } else {
            MessageAlert.showWarningMessage(null, "Please select a bug from the list");

        }
    }

    public void updateBug(ActionEvent actionEvent) {

        Bug bug = (Bug) bugsListView.getSelectionModel().getSelectedItem();
        if (bug != null) {
            String description = descriptionTextField.getText();
            if (!description.equals("")) {
                bug.setDescription(description);
                bugRepository.update(bug);
                descriptionTextField.clear();
            } else {
                MessageAlert.showWarningMessage(null, "Please enter a description");
            }
        } else {
            MessageAlert.showWarningMessage(null, "Please select a bug from the list");

        }

    }

    @Override
    public void update() {
        modelBug.setAll(bugRepository.getUnfinishedBugs());
    }
}
