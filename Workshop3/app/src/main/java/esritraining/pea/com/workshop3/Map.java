package esritraining.pea.com.workshop3;


import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Map extends Fragment {

    private static final String username = "";
    private static final String password = "";
    private static final String baseMapService = "http://.../MapServer" ;
    private static final String dynamicMapService = "http://.../MapServer" ;
    public static final String name = "name";
    public static final String address = "address";
    public static final String x = "x";
    public static final String y = "y";
    private Callout callout;
    public Map() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        final MapView mapView = (MapView) view.findViewById(R.id.map);
        UserCredentials credentials = new UserCredentials();
        credentials.setUserAccount(username, password);
        ArcGISTiledMapServiceLayer basemap = new ArcGISTiledMapServiceLayer(baseMapService, credentials);
        mapView.addLayer(basemap);


        ArcGISDynamicMapServiceLayer dynamicMapServiceLayer = new ArcGISDynamicMapServiceLayer(dynamicMapService,null,credentials);
        mapView.addLayer(dynamicMapServiceLayer);


        Bundle argument = getArguments();
        final String poiName = argument.getString(name);
        String poiAddress = argument.getString(address);
        final Point point = new Point(argument.getDouble(x), argument.getDouble(y));
        Symbol symbol = new SimpleMarkerSymbol(Color.RED,25,SimpleMarkerSymbol.STYLE.TRIANGLE);
        Graphic graphic = new Graphic(point,symbol);
        final GraphicsLayer graphicsLayer = new GraphicsLayer();
        graphicsLayer.addGraphic(graphic);
        mapView.addLayer(graphicsLayer);

        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (o == mapView && status == STATUS.INITIALIZED) {
                    toastIt("MapView loaded");

                    callout = mapView.getCallout();
                    callout.setContent(createCallOutView(poiName));
                    callout.show(point);
                    mapView.zoomToResolution(point, 10);
                }
            }
        });

        return view;
    }
    private View createCallOutView(String text){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.callout,null);
        TextView textView = (TextView) view.findViewById(R.id.callout_textView);
        textView.setText(text);
        return view;
    }

    private void toastIt(String toastText) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

}
