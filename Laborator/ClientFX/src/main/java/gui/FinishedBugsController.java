package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Bug;
import observer.Observer;
import persistence.repository.BugRepository;

public class FinishedBugsController implements Observer {

    private BugRepository bugRepository;
    ObservableList<Bug> modelBug = FXCollections.observableArrayList();

    @FXML
    ListView finishedBugsListView;

    public void initiate(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
        this.bugRepository.addObserver(this);
        getBugs();
    }


    private void getBugs() {
        finishedBugsListView.setItems(modelBug);
        try {
            modelBug.setAll(bugRepository.getFinishedBugs());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        modelBug.setAll(bugRepository.getFinishedBugs());
    }
}
