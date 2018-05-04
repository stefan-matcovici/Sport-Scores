package ro.uaic.info.tppa.sportscores;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;

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
import ro.uaic.info.tppa.sportscores.activities.competitions.continental.LatestEvents;
import ro.uaic.info.tppa.sportscores.activities.competitions.countries.LeagueActivity;
import ro.uaic.info.tppa.sportscores.models.livescores.InternationalCompetition;
import ro.uaic.info.tppa.sportscores.models.sportsdb.LeagueList;
import ro.uaic.info.tppa.sportscores.utils.DrawerUtil;
import ro.uaic.info.tppa.sportscores.utils.LivescoresHttpUtils;
import ro.uaic.info.tppa.sportscores.utils.SportsDbHttpUtils;

public class SelectorActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.livematchesId)
    CardView liveMatchesCardView;

    @BindView(R.id.internalCompetitionsId)
    CardView internalCompetitionsCardView;

    @BindView(R.id.internationalCompetitionsId)
    CardView intermationalCompetitionsCardView;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        ButterKnife.bind(this);
        DrawerUtil.getDrawer(this, toolbar);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String sport = prefs.getString("default_sport", "");

        internalCompetitionsCardView.setOnClickListener(v -> {

            ProgressDialog progress = new ProgressDialog(SelectorActivity.this);
            progress.setMessage("Please Wait...");
            progress.setIndeterminate(false);
            progress.setCancelable(false);

            progress.show();

            SportsDbHttpUtils.get("all_leagues.php", null, new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        progress.dismiss();
                        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SelectorActivity.this);
                        final String sport = prefs.getString("default_sport", "");

                        LeagueList leagueListItems = objectMapper.readValue(response.toString(), LeagueList.class);
                        leagueListItems.filterLeaguesBySport(sport);

                        new MaterialDialog.Builder(SelectorActivity.this)
                                .title("Select league")
                                .items(leagueListItems.getLeaguesNames())
                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        Intent intent = new Intent(SelectorActivity.this, LeagueActivity.class);
                                        intent.putExtra("league", leagueListItems.getLeagues().get(which));
                                        startActivity(intent);
                                        return true;
                                    }
                                })
                                .positiveText("Choose")
                                .show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
//
        });

        intermationalCompetitionsCardView.setOnClickListener(v -> {
            LivescoresHttpUtils.get(sport.toLowerCase() + "/international_competitions", null, new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("asd", "---------------- this is response : " + response);
                    try {
                        InternationalCompetition[] internationalCompetitions = objectMapper.readValue(response.toString(), InternationalCompetition[].class);

                        new MaterialDialog.Builder(SelectorActivity.this)
                                .title("Select league")
                                .items(Arrays.stream(internationalCompetitions).map(InternationalCompetition::getName).collect(Collectors.toList()))
                                .itemsCallbackSingleChoice(-1, (dialog, view, which, text) -> {
                                    Intent intent = new Intent(SelectorActivity.this, LatestEvents.class);
                                    intent.putExtra("internationalCompetition", internationalCompetitions[which]);
                                    startActivity(intent);
                                    return true;
                                })
                                .positiveText("Choose")
                                .show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        liveMatchesCardView.setOnClickListener(v -> {
            Intent intent = new Intent(SelectorActivity.this, LiveEventsActivity.class);
            startActivity(intent);
        });
    }

}
