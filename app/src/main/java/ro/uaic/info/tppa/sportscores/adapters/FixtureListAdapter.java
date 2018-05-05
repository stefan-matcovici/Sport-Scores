package ro.uaic.info.tppa.sportscores.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.models.sportsdb.Event;
import ro.uaic.info.tppa.sportscores.utils.DateUtils;

public class FixtureListAdapter extends ArrayAdapter<Event> {

    private Context mContext;
    private List<Event> eventsList;

    public FixtureListAdapter(@NonNull Context context, List<Event> list) {
        super(context, 0 , list);
        mContext = context;
        eventsList = list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_event,parent,false);

        Event currentEvent = eventsList.get(position);

        TextView homeTeamName = listItem.findViewById(R.id.homeTeam_name);
        homeTeamName.setText(currentEvent.getHomeTeam());

        TextView score = listItem.findViewById(R.id.score);
        score.setText(" vs ");

        TextView awayTeamName = listItem.findViewById(R.id.awayTeam_name);
        awayTeamName.setText(currentEvent.getAwayTeam());

        TextView round = listItem.findViewById(R.id.round);
        round.setText("round " + currentEvent.getRound());

        TextView time = listItem.findViewById(R.id.datetime);
        time.setText(DateUtils.getDisplayableDateTime(currentEvent.getDate(), currentEvent.getTime()));

        return listItem;
    }
}
