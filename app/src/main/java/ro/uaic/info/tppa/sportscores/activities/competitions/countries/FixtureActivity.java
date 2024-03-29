package ro.uaic.info.tppa.sportscores.activities.competitions.countries;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.SelectorActivity;
import ro.uaic.info.tppa.sportscores.adapters.FixtureListAdapter;
import ro.uaic.info.tppa.sportscores.models.sportsdb.Event;
import ro.uaic.info.tppa.sportscores.models.sportsdb.EventList;
import ro.uaic.info.tppa.sportscores.models.sportsdb.LeagueListItem;
import ro.uaic.info.tppa.sportscores.utils.SportsDbHttpUtils;

public class FixtureActivity extends AppCompatActivity {

    @BindView(R.id.events_list)
    ListView listview;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        LeagueListItem league = (LeagueListItem) intent.getExtras().getSerializable("league");

        ProgressDialog progress = new ProgressDialog(FixtureActivity.this);
        progress.setMessage("Please Wait...");
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        progress.show();

        SportsDbHttpUtils.get("eventsnextleague.php?id=" + league.getId(), null, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                progress.dismiss();

                EventList eventList = null;
                try {
                    eventList = objectMapper.readValue(response.toString(), EventList.class);

                    final ArrayAdapter<Event> eventArrayAdapter = new FixtureListAdapter(FixtureActivity.this, eventList.getEvents());
                    listview.setAdapter(eventArrayAdapter);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
