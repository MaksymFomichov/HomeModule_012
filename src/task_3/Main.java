package task_3;

import com.alibaba.fastjson.JSON;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Main extends Application {
    private ImageView[][] images = new ImageView[5][5];
    private GridPane gridPane;
    private List<String> listLinks;

    @Override
    public void start(Stage primaryStage) throws IOException {
        String json = null;
        try {
            json = FileUtils.readFromFile("files/links_1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        listLinks = JSON.parseArray(json, String.class);
        primaryStage.setTitle("Pictures");
        Pane root = new BorderPane();
        Scene scene = new Scene(root, 750, 750);
        gridPane = new GridPane();
        ProgressIndicator pi = new ProgressIndicator(0.6);
        BorderPane borderPane = new BorderPane();
        Button button = new Button("UPDATE");
        borderPane.setLeft(button);
        showPictures();
        root.getChildren().addAll(gridPane, borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        button.setOnMouseClicked((javafx.scene.input.MouseEvent event) -> {
            root.getChildren().clear();
            Collections.shuffle(listLinks);
            showPictures();
            root.getChildren().addAll(gridPane, borderPane);
        });
    }

    private void showPictures() {
        final int[] count = {0};
        for (int i = 0; i < images.length; i++) {
            int finalI = i;
            for (int j = 0; j < images.length; j++) {
                int finalJ = j;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                images[finalI][finalJ] = new ImageView(listLinks.get(count[0]));
                                images[finalI][finalJ].setFitHeight(150);
                                images[finalI][finalJ].setFitWidth(150);
                                gridPane.add(images[finalI][finalJ], finalI, finalJ);
                                System.out.println(count[0]);
                                count[0]++;
                            }
                        });
                    }
                }).start();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
