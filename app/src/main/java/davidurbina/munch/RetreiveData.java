package davidurbina.munch;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by davidurbina on 03/04/16.
 */
class RetrieveData extends AsyncTask<String, Void, String> {

    RequestBody formBody;
    Request request;
    String mResult;
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("Retrieve Data","Success");

        //String response = "";
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        //RequestBody body = RequestBody.create(mediaType, "instanceID=123");

        if(params[0] == "Register") {
            formBody = new FormEncodingBuilder()
                    .add("instanceID", params[1])
                    .build();

            request = new Request.Builder()
                    .url("http://34.215.96.14:3000/register")
                    .post(formBody)
                    .addHeader("authorization", "Basic RGF2aWQ6cGFzc3dvcmQ=")
                    .addHeader("cache-control", "no-cache")
                            //.post(formBody)
                    .build();
        } else if(params[0]=="GetLocations"){
            formBody = new FormEncodingBuilder()
                    .add("instanceID", params[0])
                    .build();
            request = new Request.Builder()
                    .url("http://34.215.96.14:3000/locations")
                    .get()
                    .addHeader("authorization", "Basic RGF2aWQ6cGFzc3dvcmQ=")
                    .addHeader("cache-control", "no-cache")
                            //.post(formBody)
                    .build();
        } else if (params[0] == "GetLocationDistance"){
            request = new Request.Builder()
                    .url("http://maps.google.com/maps/api/directions/json?origin="+params[1]+","+params[2]+"&destination="+params[3]+","+params[4]+"&sensor=false&units=metric")
                    .get()
                    .build();
        } else if(params[0]=="GetCategories"){
            formBody = new FormEncodingBuilder()
                    .add("username", params[1])
                    .build();

            request = new Request.Builder()
                    .url("http://34.215.96.14:3000/categories")
                    .post(formBody)
                    .build();
        } else if(params[0]=="GetItems"){
        formBody = new FormEncodingBuilder()
                .add("username", params[1])
                .add("category",params[2])
                .build();

        request = new Request.Builder()
                .url("http://34.215.96.14:3000/items")
                .post(formBody)
                .build();
        }

        try {
            Response response = client.newCall(request).execute();
            Log.d("Response",response.toString());
            return response.body().string();
        } catch (IOException e) {
            Log.d("Response","Error Unable to regiter");
            return "Error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            mResult = result;
        } catch (Exception e) {
            Log.d("Result", String.valueOf(e));
            e.printStackTrace();
        }
    }
}