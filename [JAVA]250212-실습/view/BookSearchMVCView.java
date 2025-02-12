package booksearchmvc.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BookSearchMVCView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 화면 구성
        // Stage - Scene - Parent(Layout Manager)
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookSearchMVC.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("도서 검색");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
