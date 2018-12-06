package MainWindows;

import Utilities.Aircraft;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import javafx.collections.ObservableList;
import sun.awt.Symbol;


public class MapViewr {

    private MapView mapView;
    private ArcGISMap map;
    private GraphicsOverlay graphicsOverlay;

    public MapView getMapView() {
        return mapView;
    }

    public void updatePlane(ObservableList<Aircraft> aircraftObservableList){
        graphicsOverlay.getGraphics().clear();
        SimpleMarkerSymbol planeSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF0000, 12);
        for(int i = 0 ; i < aircraftObservableList.size() ; ++i){
            double lat = aircraftObservableList.get(i).getLatitude();
            double log = aircraftObservableList.get(i).getLongitude();
            Graphic plane = new Graphic(lat,log);
            plane.setSymbol(planeSymbol);
            graphicsOverlay.getGraphics().add(plane);
        }
    }
    public MapViewr() {
        /**
         * Create Graphics Overlay
         */
        // create new graphics overlay and add it to the map view
        graphicsOverlay = new GraphicsOverlay();
        /**
         * Initialize View
         */
        mapView = new MapView();
        map = new ArcGISMap(Basemap.createOpenStreetMap());
        mapView.setMap(map);
        mapView.getGraphicsOverlays().add(graphicsOverlay);
    }
}
