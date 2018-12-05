package MainWindows;


import Utilities.AgentContainer;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.stage.Stage;


public class Main extends Application {


    public static int SCALE_POSITION = 8;


    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/mainWindows.fxml"));

        primaryStage.setTitle("AirTraffic Control Management");
        //primaryStage.setScene(new Scene(root, 800, 600));

        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        primaryStage.show();


    }


    public static void main(String[] args) {


        launch(args);



    }
}
