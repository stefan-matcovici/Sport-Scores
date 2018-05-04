package ro.uaic.info.tppa.sportscores.activities.competitions.continental;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.adapters.InternationalEventListAdapter;
import ro.uaic.info.tppa.sportscores.models.livescores.Commentary;
import ro.uaic.info.tppa.sportscores.models.livescores.InternationalCompetition;
import ro.uaic.info.tppa.sportscores.models.livescores.InternationalEvent;
import ro.uaic.info.tppa.sportscores.utils.LivescoresHttpUtils;

public class LatestEvents extends AppCompatActivity {

    @BindView(R.id.events_list)
    ListView listview;

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_events);

        ButterKnife.bind(this);

        Intent incomingIntent = getIntent();
        InternationalCompetition internationalCompetition = (InternationalCompetition) incomingIntent.getExtras().getSerializable("internationalCompetition");

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

                    final ArrayAdapter<InternationalEvent> eventArrayAdapter = new InternationalEventListAdapter(LatestEvents.this, Arrays.asList(eventList), false);
                    listview.setAdapter(eventArrayAdapter);
                    listview.setOnItemClickListener((parent, view, position, id) -> {
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("link", eventList[position].getScoreLink());
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

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
