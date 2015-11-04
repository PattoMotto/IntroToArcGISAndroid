package esritraining.pea.com.workshop3;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.esri.core.geometry.Point;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import java.util.ArrayList;
import java.util.HashMap;

import esritraining.pea.com.workshop3.model.CustomAdapter;
import esritraining.pea.com.workshop3.model.CustomModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomList extends Fragment {
    private static final String username = "";
    private static final String password = "";
    private static final String url ="http://.../query" ;
    private static final String where = "NAME LIKE '%ร้านอาหาร%'" ;
    private static final String outfield = "NAME||ADDRESS" ;
    private CustomAdapter adapter;
    private View view;
    private ListView listView;

    public CustomList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_custom_list, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomModel model = (CustomModel) adapter.getItem(position);
                Map fragment = new Map();
                Bundle bundle = new Bundle();
                bundle.putString(Map.name, model.getName());
                bundle.putString(Map.address, model.getAddress());
                bundle.putDouble(Map.x, model.getX());
                bundle.putDouble(Map.y, model.getY());
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_layout, fragment).addToBackStack(null).commit();
            }
        });

        Button search = (Button) view.findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchEditText = (EditText) view.findViewById(R.id.searchEditText);

                queryPOI(searchEditText.getText().toString());
            }
        });


        return view;
    }

    private void queryPOI(String name){
        HashMap<String,String> qparam = new HashMap<>();
        qparam.put(MapServiceAsync.URL,url);
        qparam.put(MapServiceAsync.WHERE,String.format("NAME LIKE '%%%s%%'",name));
        qparam.put(MapServiceAsync.OUTFIELD,outfield);
        UserCredentials credentials = new UserCredentials();
        credentials.setUserAccount(username, password);
        MapServiceAsync mapServiceAsync = new MapServiceAsync(credentials, new MapServiceAsync.MapServiceCallBack() {
            @Override
            public void callback(FeatureResult result) {
                ArrayList<CustomModel> models = new ArrayList<>();
                if(result!=null){
                    for(Object obj : result){
                        if(obj instanceof Feature){
                            Feature feature = (Feature) obj;
                            java.util.Map<String,Object> attr =  feature.getAttributes();
                            if(attr.get("NAME") != null && attr.get("ADDRESS")!=null){
                                models.add(
                                        new CustomModel(attr.get("NAME").toString(), attr.get("ADDRESS").toString(),
                                                ((Point)feature.getGeometry()).getX(),((Point)feature.getGeometry()).getY())
                                );
                            }
                        }
                    }
                }else{
                    Log.d("DEBUG", "EMPTY");
                }
                adapter = new CustomAdapter(getActivity().getApplicationContext(), models);
                listView.setAdapter(adapter);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mapServiceAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,qparam);
        } else {
            mapServiceAsync.execute(qparam);
        }
    }

}
