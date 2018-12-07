package MainWindows;

import GUI.AirplaneGUI;
import GUI.StationGUI;
import Utilities.Aircraft;
import Utilities.Airport;
import Utilities.ScheduleFactory;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.javadocmd.simplelatlng.LatLng;
import javafx.collections.ObservableList;

import java.util.List;


public class MapViewr {

    private MapView mapView;
    private ArcGISMap map;
    private GraphicsOverlay planesOverlay;
    private GraphicsOverlay airportOverlay;

    public MapView getMapView() {
        return mapView;
    }

    public void updatePlane(ObservableList<Aircraft> aircraftObservableList){
        planesOverlay.getGraphics().clear();
        //SimpleMarkerSymbol planeSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF0000, 12);
        for(int i = 0 ; i < aircraftObservableList.size() ; ++i){
            double lat = aircraftObservableList.get(i).getLatitude();
            double log = aircraftObservableList.get(i).getLongitude();
            LatLng position = new LatLng(lat,log);
            //Graphic plane = new Graphic(lat,log);
            //plane.setSymbol(planeSymbol);
            AirplaneGUI airplaneGUI = new AirplaneGUI(aircraftObservableList.get(i).getName(),position);
            planesOverlay.getGraphics().add(airplaneGUI.getText());
            planesOverlay.getGraphics().add(airplaneGUI.getObject());
        }
    }
    public MapViewr() {
        /**
         * Create Graphics Overlay
         */
        planesOverlay = new GraphicsOverlay();
        airportOverlay = new GraphicsOverlay();
        /**
         * Initialize View
         */
        mapView = new MapView();
        map = new ArcGISMap(Basemap.createTopographic());
        mapView.setMap(map);
        /**
         * Add graphics overlay for airport
         */
        mapView.getGraphicsOverlays().add(airportOverlay);
        /**
         * Add graphics overlay for plane
         */
        mapView.getGraphicsOverlays().add(planesOverlay);

        /**
         * draw airports
         */

        drawAirport();
    }
    private void drawAirport(){
        List<Airport> airports = ScheduleFactory.getAllAirport();
        for(int i = 0 ; i < airports.size() ; ++i){
            StationGUI stationGUI = new StationGUI(airports.get(i).getName(),airports.get(i).getLocation());
            airportOverlay.getGraphics().add(stationGUI.getObject());
            airportOverlay.getGraphics().add(stationGUI.getText());
        }
    }
}
