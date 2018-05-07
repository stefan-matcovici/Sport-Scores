package ro.uaic.info.tppa.sportscores.activities.competitions.continental;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
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

        ProgressDialog progress = new ProgressDialog(LatestEvents.this);
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

                        ProgressDialog progress = new ProgressDialog(LatestEvents.this);
                        progress.setMessage("Please Wait...");
                        progress.setIndeterminate(false);
                        progress.setCancelable(false);

                        progress.show();

                        LivescoresHttpUtils.post("/commentaries", requestParams, new JsonHttpResponseHandler() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                progress.dismiss();
                                try {
                                    final Commentary[] commentaries = objectMapper.readValue(response.toString(), Commentary[].class);

                                    new MaterialDialog.Builder(LatestEvents.this)
                                            .title("Summary")
                                            .items(Arrays.asList(commentaries))
                                            .show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                progress.dismiss();
                            }
                        });
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        Query query = mDatabase.child("competitions").child(sport.toLowerCase()).orderByChild("name").equalTo(internationalCompetition.getName());
//        query


//        competitions.addValueEventListener(new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                progress.dismiss();
//                List<InternationalEvent> events = new ArrayList<>();
//
//                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
//                    Event event = new Event();
//                    event.setAwayScore(item_snapshot.child("name").getValue().toString());
//                    event.setHomeScore(item_snapshot.child("link").getValue().toString());
////
////
////                    String awayScore;
////                    String awayTeam;
////                    String homeScore;
////                    String homeTeam;
////                    String id;
////                    String scoreLink;
////                    ro.uaic.info.tppa.sportscores.models.livescores.Header header;
////                    internationalCompetitions.add(competition);
//                }
//
//                final ArrayAdapter<InternationalEvent> eventArrayAdapter = new InternationalEventListAdapter(LatestEvents.this, Arrays.asList(eventList), false);
//                listview.setAdapter(eventArrayAdapter);
//
////                new MaterialDialog.Builder(SelectorActivity.this)
////                        .title("Select league")
////                        .items(internationalCompetitions.stream().map(InternationalCompetition::getName).collect(Collectors.toList()))
////                        .itemsCallbackSingleChoice(-1, (dialog, view, which, text) -> {
////                            Intent intent = new Intent(SelectorActivity.this, LatestEvents.class);
////                            intent.putExtra("internationalCompetition", internationalCompetitions.get(which));
////                            startActivity(intent);
////                            return true;
////                        })
////                        .positiveText("Choose")
////                        .show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println(databaseError);
//            }
//        });


        RequestParams requestParams = new RequestParams();
        requestParams.put("link", internationalCompetition.getLink());
        requestParams.setUseJsonStreamer(true);
        LivescoresHttpUtils.post("/international_events", requestParams, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                final InternationalEvent[] eventList;
                try {
                    eventList = objectMapper.readValue(response.toString(), InternationalEvent[].class);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
