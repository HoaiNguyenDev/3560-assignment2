import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

public class AdminUI extends Application implements Observer {
    private final Button btnPositivePercent = new Button("Show Positive Percent");
    private final Button btnMessageTotal = new Button("Show Message Total");
    private final Button btnGroupTotal = new Button("Show Group Total");
    private final Button btnUserTotal = new Button("Show User Total");
    private final Button btnUserView = new Button("Open User View");
    private final Button btnAddGroup = new Button("Add Group");
    private final TextArea areaGID = new TextArea("Group ID");
    private final TextArea areaUID = new TextArea("User ID");
    private final Button btnAddUser = new Button("Add User");
    GridPane centerPane = new GridPane();
    BorderPane leftPane = new BorderPane();
    private TreeView<String> treeView = new TreeView<>();
    private final Image groupIcon = new Image("group.png", 20, 20, true, true);
    private final Image userIcon = new Image("user.png", 20, 20, true, true);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void start(Stage primaryStage) throws Exception {
        RootGroup.getInstance().addObserver(this);
        primaryStage.setTitle("Admin Control Panel");
        BorderPane rootPane = new BorderPane();
        Scene scene = new Scene(rootPane);

        updateTreeView();
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        leftPane.setTop(new Label("Tree View"));
        leftPane.setLeft(treeView);
        rootPane.setLeft(leftPane);

        areaUID.setPrefSize(20, 10);
        centerPane.add(areaUID, 0, 0);

        btnAddUser.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centerPane.add(btnAddUser, 1, 0);
        btnAddUser.setOnAction(this::addUser);

        areaGID.setPrefSize(20, 10);
        centerPane.add(areaGID, 0, 1);

        btnAddGroup.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centerPane.add(btnAddGroup, 1, 1);
        btnAddGroup.setOnAction(this::addGroup);

        btnUserView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centerPane.add(btnUserView, 0, 2, 2, 1);
        btnUserView.setOnAction(this::openUserView);

        centerPane.add(new Text(""), 0, 3);

        btnUserTotal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centerPane.add(btnUserTotal, 0, 4);
        btnUserTotal.setOnAction(event -> {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText(RootGroup.getInstance().countUser() + "");
            alert.setTitle("Total user");
            alert.show();
        });

        btnGroupTotal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centerPane.add(btnGroupTotal, 1, 4);
        btnGroupTotal.setOnAction(event -> {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText(RootGroup.getInstance().countGroup() + "");
            alert.setTitle("Total group");
            alert.show();
        });

        btnMessageTotal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centerPane.add(btnMessageTotal, 0, 5);
        btnMessageTotal.setOnAction(event -> {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText(RootGroup.getInstance().countMessage() + "");
            alert.setTitle("Total message");
            alert.show();
        });

        btnPositivePercent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centerPane.add(btnPositivePercent, 1, 5);
        btnPositivePercent.setOnAction(event -> {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText(RootGroup.getInstance().percentPositive() + "");
            alert.setTitle("Percent positive message");
            alert.show();
        });
        centerPane.setHgap(20);
        centerPane.setVgap(20);
        centerPane.setPadding(new Insets(10, 10, 10, 10));
        rootPane.setCenter(centerPane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openUserView(ActionEvent event) {
        List<String> selected = getAllSelected();

        for (String id : selected) {
            if (RootGroup.getInstance().findUser(id) != null)
                Platform.runLater(()->{
                    try {
                        Application application = new UserUI();
                        Stage primaryStage = new Stage();
                        application.start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        }
    }

    private void addUser(ActionEvent event) {
        List<String> selected = getAllSelected();

        if (areaUID.getText().trim().isEmpty()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Choose a group to add user");
            alert.setTitle("ERROR");
            alert.show();
            return;
        }

        if (selected.size() != 1) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Choose only one group to add user");
            alert.setTitle("ERROR");
            alert.show();
            return;
        }
        Group parent = RootGroup.getInstance().findGroup(selected.get(0));
        if (parent == null) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Choose only one GROUP to add group");
            alert.setTitle("ERROR");
            alert.show();
            return;
        }
        User child = RootGroup.getInstance().findUser(areaUID.getText().trim());
        if (child != null) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("User existed");
            alert.setTitle("ERROR");
            alert.show();
            return;
        }
        parent.addUser(new User(areaUID.getText().trim()));
    }

    private void addGroup(ActionEvent event) {
        List<String> selected = getAllSelected();

        if (areaUID.getText().trim().isEmpty()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Choose a group to add group");
            alert.setTitle("ERROR");
            alert.show();
        }
        if (selected.size() != 1) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Choose only one group to add group");
            alert.setTitle("ERROR");
            alert.show();
            return;
        }
        Group parent = RootGroup.getInstance().findGroup(selected.get(0));
        if (parent == null) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Choose only one GROUP to add group");
            alert.setTitle("ERROR");
            alert.show();
            return;
        }
        Group child = RootGroup.getInstance().findGroup(areaGID.getText().trim());
        if (child != null) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Group existed");
            alert.setTitle("ERROR");
            alert.show();
            return;
        }
        NormalGroup newGroup = new NormalGroup(areaGID.getText().trim());
        newGroup.addObserver(this);
        parent.addGroup(newGroup);
    }

    private void updateTreeView() {
        TreeItem<String> root = new TreeItem<>("Root");
        addChild(root, RootGroup.getInstance());
        root.setExpanded(true);
        treeView.setRoot(root);
    }

    private void addChild(TreeItem<String> tree, Group group) {
        TreeItem<String> treeItem;
        for (User u : group.getUsers()) {
            treeItem = new TreeItem<>(u.getUid(), new ImageView(userIcon));
            tree.getChildren().add(treeItem);
        }

        for (NormalGroup g : group.getGroups()) {
            treeItem = new TreeItem<>(g.getGid(), new ImageView(groupIcon));
            treeItem.setExpanded(true);
            tree.getChildren().add(treeItem);
            addChild(treeItem, g);
        }
    }

    private String getLeadSelect() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        return selectedItem == null ? null : selectedItem.getValue();
    }

    private List<String> getAllSelected() {
        return treeView.getSelectionModel().getSelectedItems()
                .stream()
                .map(TreeItem::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Observable o, Object arg) {
        updateTreeView();
    }
}
