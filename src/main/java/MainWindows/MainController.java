package MainWindows;

import GUI.AgentInterface;
import Utilities.AgentContainer;
import Utilities.Aircraft;
import Utilities.ScheduleFactory;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class MainController  {

    private AgentContainer container;
    @FXML
    private TableView<Aircraft> flyStatus;
    @FXML
    private TableColumn<Aircraft,String> aircraftCol;
    @FXML
    private TableColumn<Aircraft,String> originCol;
    @FXML
    private TableColumn<Aircraft,String> destinationCol;
    @FXML
    private TableColumn<Aircraft,Double> latitudeCol;
    @FXML
    private TableColumn<Aircraft,Double> longitudeCol;
    @FXML
    private TableColumn<Aircraft,Double> speedCol;
    @FXML
    private TableColumn<Aircraft,String> statusCol;
    @FXML
    private TableColumn<Aircraft,Integer> wayPoint;
    @FXML
    private TableColumn<Aircraft,Double> totalCol;
    @FXML
    private TableColumn<Aircraft,Double> remainingCol;

    @FXML
    private StackPane mapViewPane;

    @FXML
    private VBox vBox;

    /**
     * ArcGIS Map
     */
    private MapViewr mapViewr;




    @FXML
    private void initialize(){
        /**
         * Initialize Map
         */
        mapViewr = new MapViewr();
        mapViewPane.getChildren().setAll(mapViewr.getMapView());

        /**
         * Set property of table column
         */
        aircraftCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        originCol.setCellValueFactory(new PropertyValueFactory<>("origin"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        latitudeCol.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitudeCol.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        speedCol.setCellValueFactory(new PropertyValueFactory<>("speed"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        wayPoint.setCellValueFactory(new PropertyValueFactory<>("wayPoint"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
        remainingCol.setCellValueFactory(new PropertyValueFactory<>("remainingDistance"));

        flyStatus.setItems(getAircrafts());
        updateInfo();
        container = new AgentContainer();
    }
    private ObservableList<Aircraft> getAircrafts(){
        return FXCollections.observableArrayList(ScheduleFactory.getAllAircraft());
    }

    public void updateInfo(){
        /**
         * Timer to update GUI
         */
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                flyStatus.getItems().clear();
                flyStatus.setItems(AgentInterface.getInstance().getData());
                mapViewr.updatePlane(AgentInterface.getInstance().getData());
            }
        };
        Timer timer = new Timer("Timer");

        long delay  = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }



}
