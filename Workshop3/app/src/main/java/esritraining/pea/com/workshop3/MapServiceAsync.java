package esritraining.pea.com.workshop3;


import android.os.AsyncTask;
import android.util.Log;

import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.EsriSecurityException;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by esri5141 on 21/10/2558.
 */
public class MapServiceAsync extends AsyncTask<HashMap<String,String>,Void,FeatureResult> {
    public static final String URL = "url";
    public static final String WHERE = "where";
    public static final String OUTFIELD = "outfield";
    private UserCredentials credentials;
    private MapServiceCallBack callBack;

    public MapServiceAsync(UserCredentials credentials,MapServiceCallBack callBack) {
        this.credentials = credentials;
        this.callBack = callBack;
    }


    @Override
    protected FeatureResult doInBackground(HashMap<String, String>... params) {
        QueryParameters qParam = new QueryParameters();
        Log.d("DEBUG", params[0].get(WHERE) + " " + params[0].get(OUTFIELD) + " " + params[0].get(URL));

        qParam.setOutSpatialReference(SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR));
        qParam.setOutFields(params[0].get(OUTFIELD).split(Pattern.quote("||")));
        qParam.setReturnGeometry(true);
        qParam.setWhere(params[0].get(WHERE));
        try {
            QueryTask queryTask = new QueryTask(params[0].get(URL),credentials);
            FeatureResult outFeature = queryTask.execute(qParam);
            if(outFeature!=null && outFeature.featureCount()>0)
                return outFeature;
            else
                return null;
        } catch (EsriSecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(FeatureResult feature) {
        super.onPostExecute(feature);
        callBack.callback(feature);
    }
    interface MapServiceCallBack{
        void callback(FeatureResult result);
    }
}
