package esritraining.pea.com.workshop2;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.android.runtime.ArcGISRuntime;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map fragment = new Map();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_layout,fragment);
        fragmentTransaction.commit();
//        ArcGISRuntime.setClientId("");
    }
}
