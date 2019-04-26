package com.vv.netjson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.display);
        /*1*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /*2*/
        GitHubService service = retrofit.create(GitHubService.class);
        /*3*/
        Call<List<Repo>> repos = service.listRepos("vv73");
        /*4*/
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                List<Repo> repos = response.body();
                for (Repo r : repos){
                    tv.append(r.getName() + "\n");
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {

            }
        });
    }



    public void getTime(View view) {
        new GetHTTP().execute("http://date.jsontest.com");
    }

    class GetHTTP extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String content = "";
            try {
                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                Scanner in = new Scanner(connection.getInputStream());
                while (in.hasNext()) {
                    content += in.nextLine();
                }
            } catch (IOException e) {
                content = "ERROR";
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            TextView tv = findViewById(R.id.display);
            s = "";
            try {
                JSONObject obj = new JSONObject(s);
                s += obj.getInt("milliseconds_since_epoch");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tv.setText(s);
        }
    }
}
