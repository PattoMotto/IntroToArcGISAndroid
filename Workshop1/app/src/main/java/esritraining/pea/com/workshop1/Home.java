package esritraining.pea.com.workshop1;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        toastIt("Welcome!!!");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new CustomTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new CustomTask().execute();
        }
    }

    private void toastIt(String toastText) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

    class CustomTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            toastIt("After sleep 5sec");
        }

    }
}
