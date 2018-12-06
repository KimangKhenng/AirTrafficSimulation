package GUI;

import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.javadocmd.simplelatlng.LatLng;

public class StationGUI {

    public Graphic getText() {
        return Text;
    }

    public Graphic getStation() {
        return station;
    }

    private Graphic station;
    private Graphic Text;
    private TextSymbol airportSymbol;
    private SimpleMarkerSymbol symbol;
    private LatLng position;


    public StationGUI(String name, LatLng position) {

        symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE,0xFF000000 ,12);
        airportSymbol= new TextSymbol();
        airportSymbol.setSize(15);
        airportSymbol.setText(name);
        airportSymbol.setColor(0xFF000000);
        this.position = position;

        station = new Graphic(position.getLatitude(),position.getLongitude());
        station.setSymbol(symbol);

        Text = new Graphic(position.getLatitude() - 0.1 ,position.getLongitude());
        Text.setSymbol(airportSymbol);
    }
}
