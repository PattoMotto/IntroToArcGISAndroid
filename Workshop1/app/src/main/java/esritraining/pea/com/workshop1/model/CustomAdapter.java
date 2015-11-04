package esritraining.pea.com.workshop1.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esritraining.pea.com.workshop1.R;

/**
 * Created by esri5141 on 22/10/2558.
 */
public class CustomAdapter extends BaseAdapter{
    private List<CustomModel> models;
    private Context context;
    public CustomAdapter(Context context,List<CustomModel> models) {
        this.models = new ArrayList<>(models);
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_list_item,parent,false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name_textView);
        TextView address = (TextView) convertView.findViewById(R.id.address_textView);
        name.setText(models.get(position).getName());
        address.setText(models.get(position).getAddress());
        return convertView;
    }
}
