package davidurbina.munch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Category_Activity extends AppCompatActivity {


    ProgressBar bar;
    String Response;
    CardAdapter adpater;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    String location;
    String category;
    String listType;
    JSONArray jsonArray;
    JSONObject jsonObject;
    RetrieveData retrieveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        location = intent.getStringExtra("location");

        retrieveData = new RetrieveData();
        retrieveData.execute("GetCategories", location);

        try {
            Response = retrieveData.get();
            try {
                jsonObject = new JSONObject(Response);
                jsonArray = jsonObject.getJSONArray("categories");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //Init Components
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        bar = (ProgressBar) findViewById(R.id.loadingBar);
        bar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adpater = new CardAdapter(getApplicationContext(),"Categories", jsonArray, location, "");
//      adpater = new CardAdapter(getApplicationContext(),"Items", jsonArray, "", "");
        recyclerView.setAdapter(adpater);
    }
}
