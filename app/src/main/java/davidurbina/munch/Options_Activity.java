package davidurbina.munch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Options_Activity extends AppCompatActivity {

    ProgressBar bar;
    Integer index;
    CardAdapter adpater;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    JSONArray jsonArray;
    ArrayList<String> Choices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        try {
            jsonArray = new JSONArray(intent.getStringExtra("options"));
            if(jsonArray.getJSONObject(index).getBoolean("multiple")){
                Button btn = (Button) findViewById(R.id.btnNext);
                btn.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
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

        adpater = new CardAdapter(getApplicationContext(),"Options", jsonArray, index.toString(), "");
        recyclerView.setAdapter(adpater);
    }

}
