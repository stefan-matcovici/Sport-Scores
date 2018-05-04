package ro.uaic.info.tppa.sportscores.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.models.sportsdb.Event;

public class FixtureListAdapter extends ArrayAdapter<Event> {

    private Context mContext;
    private List<Event> eventsList;

    public FixtureListAdapter(@NonNull Context context, List<Event> list) {
        super(context, 0 , list);
        mContext = context;
        eventsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.fixture_event,parent,false);

        Event currentEvent = eventsList.get(position);

        TextView homeTeamName = listItem.findViewById(R.id.homeTeam_name);
        homeTeamName.setText(currentEvent.getHomeTeam());

        TextView awayTeamName = listItem.findViewById(R.id.awayTeam_name);
        awayTeamName.setText(currentEvent.getAwayTeam());

        TextView homeTeamScore = listItem.findViewById(R.id.time);
        homeTeamScore.setText(currentEvent.getDate() + " at " + currentEvent.getTime());

        return listItem;
    }
}
