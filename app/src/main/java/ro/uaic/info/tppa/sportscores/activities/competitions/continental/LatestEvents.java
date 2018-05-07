package ro.uaic.info.tppa.sportscores.activities.competitions.continental;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.SelectorActivity;
import ro.uaic.info.tppa.sportscores.activities.competitions.countries.ResultsActivity;
import ro.uaic.info.tppa.sportscores.adapters.EventListAdapter;
import ro.uaic.info.tppa.sportscores.adapters.InternationalEventListAdapter;
import ro.uaic.info.tppa.sportscores.models.livescores.Commentary;
import ro.uaic.info.tppa.sportscores.models.livescores.InternationalCompetition;
import ro.uaic.info.tppa.sportscores.models.livescores.InternationalEvent;
import ro.uaic.info.tppa.sportscores.models.sportsdb.Event;
import ro.uaic.info.tppa.sportscores.utils.LivescoresHttpUtils;

public class LatestEvents extends AppCompatActivity {

    @BindView(R.id.events_list)
    ListView listview;

    private ObjectMapper objectMapper = new ObjectMapper();
    private DatabaseReference mDatabase;
    private ProgressDialog progress;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_events);

        ButterKnife.bind(this);

        Intent incomingIntent = getIntent();
        InternationalCompetition internationalCompetition = (InternationalCompetition) incomingIntent.getExtras().getSerializable("internationalCompetition");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String sport = prefs.getString("default_sport", "");

        progress = new ProgressDialog(LatestEvents.this);
        progress.setMessage("Please Wait...");
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        progress.show();

        DatabaseReference databaseReference = mDatabase.child("competitions").child(sport.toLowerCase()).child(internationalCompetition.getKey()).child("events");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progress.dismiss();
                if (dataSnapshot.exists()) {
                    List<InternationalEvent> eventList = new ArrayList<>();

                    for (DataSnapshot event_snapshot : dataSnapshot.getChildren()) {
                        eventList.add(event_snapshot.getValue(InternationalEvent.class));
                    }

                    final ArrayAdapter<InternationalEvent> eventArrayAdapter = new InternationalEventListAdapter(LatestEvents.this, eventList, false);


                    listview.setAdapter(eventArrayAdapter);
                    listview.setOnItemClickListener((parent, view, position, id) -> {
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("link", eventList.get(position).getScoreLink());
                        requestParams.setUseJsonStreamer(true);

                        PostMethodDemo postMethodDemo = new PostMethodDemo();
                        postMethodDemo.execute("http://54.246.237.22/commentaries");


//                        LivescoresHttpUtils.post("/commentaries", requestParams, new JsonHttpResponseHandler() {
//                            @RequiresApi(api = Build.VERSION_CODES.N)
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                                progress.dismiss();
//                                try {
//                                    final Commentary[] commentaries = objectMapper.readValue(response.toString(), Commentary[].class);
//
//                                    new MaterialDialog.Builder(LatestEvents.this)
//                                            .title("Summary")
//                                            .items(Arrays.asList(commentaries))
//                                            .show();
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                                progress.dismiss();
//                            }
//                        });
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    class RetrieveCommentariesTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progress.show();
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("http://54.246.237.22/commentaries");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);

                JSONObject params = new JSONObject();
                try {
                    params.put("link", "http://www.livescore.com/soccer/england/premier-league/tottenham-hotspur-vs-watford/1-2523101/");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                urlConnection.getOutputStream().write(params.toString().getBytes("UTF-8"));

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            progress.dismiss();
            Log.i("INFO", response);

            final Commentary[] commentaries;
            try {
                commentaries = objectMapper.readValue(response, Commentary[].class);
                new MaterialDialog.Builder(LatestEvents.this)
                        .title("Summary")
                        .items(Arrays.asList(commentaries))
                        .show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class PostMethodDemo extends AsyncTask<String , Void ,String> {
        String server_response;

        protected void onPreExecute() {
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream ());

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("link" , "http://www.livescore.com/soccer/england/premier-league/tottenham-hotspur-vs-watford/1-2523101/");

                    wr.writeBytes(obj.toString());
                    Log.e("JSON Input", obj.toString());
                    wr.flush();
                    wr.close();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Response", "" + server_response);

            progress.dismiss();

            final Commentary[] commentaries;
            try {
                commentaries = objectMapper.readValue(server_response, Commentary[].class);
                new MaterialDialog.Builder(LatestEvents.this)
                        .title("Summary")
                        .items(Arrays.asList(commentaries))
                        .show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
