package GUI;

import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.javadocmd.simplelatlng.LatLng;

public class StationGUI extends BaseGUI {

    @Override
    public Graphic getText() {
        return Text;
    }

    @Override
    public Graphic getObject() {
        return object;
    }
    public StationGUI(String name, LatLng position) {
        super(name,position);
        symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE,0xFF000000 ,12);
        objectSymbol= new TextSymbol();
        objectSymbol.setSize(15);
        objectSymbol.setText(name);
        objectSymbol.setColor(0xFF000000);
        object.setSymbol(symbol);
        Text.setSymbol(objectSymbol);
    }
}
