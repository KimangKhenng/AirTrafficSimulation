package GUI;

import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.javadocmd.simplelatlng.LatLng;

public abstract class BaseGUI {
    public BaseGUI(String name, LatLng position) {
        object = new Graphic(position.getLatitude(),position.getLongitude());
        Text = new Graphic(position.getLatitude() - 0.1 ,position.getLongitude());
    }

    protected Graphic object;
    protected Graphic Text;
    protected TextSymbol objectSymbol;
    protected SimpleMarkerSymbol symbol;
    public abstract Graphic getText();
    public abstract Graphic getObject();

}
