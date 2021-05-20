import gui.LoginController;
import gui.MessageAlert;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Bug;
import model.Developer;
import model.Tester;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import persistence.repository.BugRepository;
import persistence.repository.DeveloperRepository;
import persistence.repository.TesterRepository;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Main extends Application {

    private static SessionFactory sessionFactory;
    private Properties props;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Properties props = new Properties();
        try {
            props.load(Main.class.getResourceAsStream("/prop.properties"));
            System.out.println("Server properties set. ");
            props.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find properties " + e);
            return;
        }


        initialize();
        DeveloperRepository developerRepository = new DeveloperRepository(props, sessionFactory);
        TesterRepository testerRepository = new TesterRepository(props, sessionFactory);
        BugRepository bugRepository=new BugRepository(props,sessionFactory);

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            AnchorPane root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.initiateLoginProcedure(stage,developerRepository,testerRepository,bugRepository);
            stage.setScene(new Scene(root, 420, 400));
            stage.show();


        } catch (Exception e) {
            MessageAlert.showWarningMessage(null, "Error opening the application");
        }
    }


    static void initialize() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exceptie " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null)
            sessionFactory.close();
    }
}

