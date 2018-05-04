package ro.uaic.info.tppa.sportscores.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.models.livescores.InternationalEvent;

public class InternationalEventListAdapter extends ArrayAdapter<InternationalEvent> {
    private Context mContext;
    private List<InternationalEvent> eventsList;
    private boolean subscribable;

    public InternationalEventListAdapter(@NonNull Context context, List<InternationalEvent> list, boolean b) {
        super(context, 0, list);
        mContext = context;
        eventsList = list;
        subscribable = b;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_event, parent, false);

        InternationalEvent currentEvent = eventsList.get(position);

        TextView homeTeamName = listItem.findViewById(R.id.homeTeam_name);
        homeTeamName.setText(currentEvent.getHomeTeam());

        TextView score = listItem.findViewById(R.id.score);
        score.setText(currentEvent.getHomeScore() + " : " + currentEvent.getAwayScore());

        TextView awayTeamName = listItem.findViewById(R.id.awayTeam_name);
        awayTeamName.setText(currentEvent.getAwayTeam());


        return listItem;
    }
}
