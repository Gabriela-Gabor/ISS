package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Bug;
import model.Developer;
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
            modelBug.setAll(bugRepository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        modelBug.setAll(bugRepository.findAll());

    }
}