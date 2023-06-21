import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UserUI extends Application {
    private User user;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(this.getParameters().getRaw().get(0));
    }
}
