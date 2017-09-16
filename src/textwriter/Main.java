package textwriter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main extends Application {
    public javafx.scene.control.TextField tfLink;
    public javafx.scene.control.TextArea taText;
    private ArrayList<String> strings;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setResizable(false); // запрет на изменение размера окна
        primaryStage.setScene(new Scene(root, 480, 640));
        primaryStage.show();
        tfLink = (TextField) root.lookup("#tfLink");
        taText = (TextArea) root.lookup("#taText");
        tfLink.setText("files/text.txt");
    }

    public void clickButtonLoad(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        strings = getText(tfLink.getText());
        for (String value : strings) {
            taText.appendText(value);
            taText.appendText("\n");
        }
    }

    public void clickButtonSave(ActionEvent actionEvent) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileWriter writer = null;
                try {
                    writer = new FileWriter(tfLink.getText(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (String value : taText.getText().split("\n")) {
                    try {
                        writer.write(value);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        writer.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // получаем текст
    private ArrayList<String> getText(String link) throws IOException, ExecutionException, InterruptedException {
        FutureTask<ArrayList<String>> futureTask = new FutureTask<ArrayList<String>>(new Callable<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() throws Exception {
                ArrayList<String> list = new ArrayList<>();
                FileReader reader = new FileReader(link);
                BufferedReader br = new BufferedReader(reader);
                String temp = br.readLine();
                while (temp != null) {
                    list.add(temp);
                    temp = br.readLine();
                }
                return list;
            }
        });
        new Thread(futureTask).start();
        return futureTask.get();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
