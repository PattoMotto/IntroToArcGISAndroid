package esritraining.pea.com.workshop2;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISImageServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnPanListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.android.toolkit.map.OnCalloutClickListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.ProjectionTransformation;
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
    private static final String url ="http://.../query" ;
    private static final String where = "NAME LIKE '%ร้านอาหาร%'" ;
    private static final String outfield = "NAME||ADDRESS" ;
    private boolean moved;
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

        Point bkkPoint = new Point(100.3529157,13.7244426);
        Point webmerPoint = (Point) GeometryEngine.project(bkkPoint, SpatialReference.create(SpatialReference.WKID_WGS84), SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR));
        Symbol symbol = new SimpleMarkerSymbol(Color.RED,25,SimpleMarkerSymbol.STYLE.DIAMOND);

        Graphic graphic = new Graphic(webmerPoint,symbol);

        final GraphicsLayer graphicsLayer = new GraphicsLayer();
        graphicsLayer.addGraphic(graphic);
        mapView.addLayer(graphicsLayer);

        moved = false;
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (o == mapView && status == STATUS.INITIALIZED) {
                    toastIt("MapView loaded");
                }
            }
        });
        mapView.setOnPanListener(new OnPanListener() {
            @Override
            public void prePointerMove(float v, float v1, float v2, float v3) {
                if(!moved) {
                    toastIt("PointerMove");
                    moved = true;
                }
            }

            @Override
            public void postPointerMove(float v, float v1, float v2, float v3) {

            }

            @Override
            public void prePointerUp(float v, float v1, float v2, float v3) {

            }

            @Override
            public void postPointerUp(float v, float v1, float v2, float v3) {
                toastIt("PointerUp");
                moved = false;
            }
        });
        mapView.setOnLongPressListener(new OnLongPressListener() {
            @Override
            public boolean onLongPress(float v, float v1) {
                toastIt("Long pressed.");
                return false;
            }
        });

        callout = mapView.getCallout();
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                int[] gId = graphicsLayer.getGraphicIDs(x, y, 2);
                if (gId != null && gId.length > 0) {
                    callout.setContent(createCallOutView("Hello"));
                    callout.show((Point) graphicsLayer.getGraphic(gId[0]).getGeometry());
                }
                else {
                    if(callout!=null && callout.isShowing())
                        callout.hide();
                }

            }
        });

        HashMap<String,String> qparam = new HashMap<>();
        qparam.put(MapServiceAsync.URL,url);
        qparam.put(MapServiceAsync.WHERE,where);
        qparam.put(MapServiceAsync.OUTFIELD,outfield);
        MapServiceAsync mapServiceAsync = new MapServiceAsync(credentials, new MapServiceAsync.MapServiceCallBack() {
            @Override
            public void callback(FeatureResult result) {
                if(result!=null){
                    for(Object obj : result){
                        if(obj instanceof Feature){
                            Feature feature = (Feature) obj;
                            java.util.Map<String,Object> attr =  feature.getAttributes();
                            Symbol asyncSymbol = new SimpleMarkerSymbol(Color.BLUE,25,SimpleMarkerSymbol.STYLE.TRIANGLE);
                            Graphic asyncGraphic = new Graphic(feature.getGeometry(),asyncSymbol);
                            graphicsLayer.addGraphic(asyncGraphic);
                            toastIt(String.format("%s %s", attr.get("NAME"), attr.get("ADDRESS")));
                        }
                    }
                }else{
                    toastIt("No result");
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mapServiceAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,qparam);
        } else {
            mapServiceAsync.execute(qparam);
        }

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
