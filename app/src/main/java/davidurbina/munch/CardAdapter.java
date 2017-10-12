package davidurbina.munch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by davidurbina on 19/04/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context mContext;
    private String locations;
    View v;
    JSONArray jsonArray;
    JSONObject obj;
    JSONArray optionsArray;

    String myLatitude;
    String myLongitude;
    String desLatitude;
    String desLongitude;
    String imageURL;
    String Cardtype;
    String location;
    Integer index;
    Boolean mutiple;

    public CardAdapter(Context context, String type, JSONArray information, String param1, String param2){
        Cardtype = type;
        mutiple = false;
        mContext = context;
        if(Cardtype.equals("Locations")) {
            myLatitude = param1;
            myLongitude = param2;
        } else if(Cardtype.equals("Categories")){
            location = param1;
        }
        jsonArray = information;
        if(Cardtype.equals("Options")) {
            optionsArray = information;
            index = Integer.valueOf(param1);
            try {
                jsonArray = information.getJSONObject(index).getJSONArray("elements");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if(information.getJSONObject(index).getBoolean("multiple")){
                    mutiple = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if(Cardtype.equals("Locations")){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_view_card_item, viewGroup, false);
        } else if (Cardtype .equals("Categories")|| Cardtype.equals("Items")|| Cardtype.equals("Options")){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycle_view_category, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        String Response ="";

        //Get Object from JSON Array
        try {
            obj = jsonArray.getJSONObject(position);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON", e.toString());
        }

        if(Cardtype.equals("Locations")){
            //Set Information to Card Views
            try {
                holder.tvTitle.setText((obj.getString("name")));

                holder.tvDescription.setText(obj.getString("description"));

                desLatitude = obj.getString("latitude");
                desLongitude = obj.getString("longitude");
                imageURL = obj.getString("image");
                holder.tvDistance.setText(obj.getString("distance"));
                holder.location = obj.getString("user");
            } catch (JSONException e) {
                Log.d("JSON", e.toString());
                e.printStackTrace();
            }
            //Download Image and set it to ImageView
            Uri uri = Uri.parse(imageURL);
            Context context = holder.imageView.getContext();
            Picasso.with(context).load(uri)
                    .into(holder.imageView);
        } else if (Cardtype.equals("Categories")){
            //Set Information to Views
            try {
                holder.tv1.setText(obj.getString("category"));
                holder.category = obj.getString("category");
                holder.location = location;
                holder.items = obj.getJSONArray("items");
            } catch (JSONException e){

            }
        } else if (Cardtype.equals("Items")){
            try {
                NumberFormat format =
                        NumberFormat.getCurrencyInstance(Locale.US);
                String currency = format.format(Double.parseDouble(obj.getString("price")));
                holder.tv1.setText(obj.getString("name"));
                holder.tv2.setText(currency);
                holder.tv3.setText(obj.getString("description"));
                holder.tv3.setVisibility(View.VISIBLE);
                holder.options = obj.getJSONArray("options");

            } catch (JSONException e){

            }
        }
        else if (Cardtype.equals("Options")){
            try {
                NumberFormat format =
                        NumberFormat.getCurrencyInstance(Locale.US);
                String currency = format.format(Double.parseDouble(obj.getString("price")));
                holder.tv1.setText(obj.getString("text"));
                holder.multiple = mutiple;
                holder.tv2.setText(currency);
                if(mutiple) {
                    holder.cb.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e){

            }
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvDescription;
        TextView tvDistance;
        ImageView imageView;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        CheckBox cb;
        String location;
        String category;
        JSONArray items;
        JSONArray options;
        Boolean multiple;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            if(Cardtype.equals("Locations")) {
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
                tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
                imageView = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            } else if (Cardtype.equals("Categories")){
                tv1 = (TextView) itemView.findViewById(R.id.tv1);
                tv2 = (TextView) itemView.findViewById(R.id.tv2);
            } else if (Cardtype.equals("Items")||(Cardtype.equals("Options"))){
                tv1 = (TextView) itemView.findViewById(R.id.tv1);
                tv2 = (TextView) itemView.findViewById(R.id.tv2);
                tv3 = (TextView) itemView.findViewById(R.id.tv3);
                cb = (CheckBox) itemView.findViewById(R.id.checkBox);
            }
        }

        @Override
        public void onClick(View v) {
            if(Cardtype.equals("Locations")){
                Intent intent = new Intent(mContext,Category_Activity.class);
                intent.putExtra("location",location);
                intent.putExtra("listType","Items");
                intent.putExtra("category","Seafood");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } if (Cardtype.equals("Categories")){
                Intent intent = new Intent(mContext,Item_Activtiy.class);
                intent.putExtra("items",items.toString());
                intent.putExtra("listType","Items");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } if (Cardtype.equals("Items")){
                Intent intent = new Intent(mContext,Options_Activity.class);
                intent.putExtra("options", options.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } if (Cardtype.equals("Options")){
                if(multiple){
                }
                if(index+1 < optionsArray.length()) {
                    Intent intent = new Intent(mContext,Options_Activity.class);
                    intent.putExtra("options",optionsArray.toString());
                    intent.putExtra("index", index + 1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {

                }
            }
        }
    }
}

