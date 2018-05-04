package ro.uaic.info.tppa.sportscores.activities.competitions.countries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.models.sportsdb.LeagueListItem;

public class LeagueActivity extends AppCompatActivity {

    @BindView(R.id.standingsId)
    CardView standingsCardView;

    @BindView(R.id.resultsId)
    CardView resultsCardView;

    @BindView(R.id.futureId)
    CardView futureCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        LeagueListItem league = (LeagueListItem) intent.getExtras().getSerializable("league");

        standingsCardView.setOnClickListener(view -> {
            Intent sendIntent = new Intent(LeagueActivity.this, StandingsActivity.class);
            sendIntent.putExtra("league", league);
            startActivity(sendIntent);
        });

        resultsCardView.setOnClickListener(view -> {
            Intent sendIntent = new Intent(LeagueActivity.this, ResultsActivity.class);
            sendIntent.putExtra("league", league);
            startActivity(sendIntent);
        });

        futureCardView.setOnClickListener(view -> {
            Intent sendIntent = new Intent(LeagueActivity.this, FixtureActivity.class);
            sendIntent.putExtra("league", league);
            startActivity(sendIntent);
        });


    }
}
