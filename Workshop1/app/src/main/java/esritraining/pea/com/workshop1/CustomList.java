package esritraining.pea.com.workshop1;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import esritraining.pea.com.workshop1.model.CustomAdapter;
import esritraining.pea.com.workshop1.model.CustomModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomList extends Fragment {

    private CustomAdapter adapter;

    public CustomList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        adapter = new CustomAdapter(getActivity().getApplicationContext(), genModels());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomModel model = (CustomModel) adapter.getItem(position);
                Toast.makeText(getActivity().getApplicationContext(), String.format("%s %s",
                        model.getName(), model.getAddress()), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private ArrayList<CustomModel> genModels() {
        ArrayList<CustomModel> models = new ArrayList<>();
        models.add(new CustomModel("A", "123 A BBB"));
        models.add(new CustomModel("B", "123 B BBB"));
        models.add(new CustomModel("C", "123 C BBB"));
        return models;
    }


}
