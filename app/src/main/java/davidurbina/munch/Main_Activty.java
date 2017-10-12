package davidurbina.munch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main_Activty extends AppCompatActivity {

    ProgressBar bar;
    String Response;
    CardAdapter adpater;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    String myLatitude;
    String myLongitude;
    JSONArray jsonArray;
    JSONArray bufferArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bufferArray = new JSONArray();
        Intent intent = getIntent();

        myLatitude =/* "30";//*/ intent.getStringExtra("Lat");
        myLongitude = /*"-37";//*/intent.getStringExtra("Long");

        //Request Data
        RetrieveData retrieveData = new RetrieveData();
        retrieveData.execute("GetLocations");
        try {
            Response = retrieveData.get();
            Log.i("Yo","Hey");
            try {
                jsonArray = new JSONArray(Response);
                for(int i = 0; i < jsonArray.length(); i++){
                    String distance = "";
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String lat = jsonObject.getString("latitude");
                    String long_ = jsonObject.getString("longitude");
                    retrieveData = new RetrieveData();
                    retrieveData.execute("GetLocationDistance", myLatitude, myLongitude, lat, long_);
                    try {
                        Response = retrieveData.get();
                        JSONObject googleAPIResponse = new JSONObject(Response);
                        distance = googleAPIResponse.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
                    } catch (Exception e){
                        e.printStackTrace();//Log.d("Distance Calc Error", e.toString());
                    }
                    jsonObject.put("distance",distance);
                    bufferArray.put(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        //Init Components
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adpater = new CardAdapter(getApplicationContext(), "Locations",bufferArray, myLatitude, myLongitude);
        recyclerView.setAdapter(adpater);
        bar = (ProgressBar) findViewById(R.id.loadingBar);
        bar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        //
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent.putExtra("EXIT", true);
                        //startActivity(intent);
                        //finish();
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        return;
    }
}
