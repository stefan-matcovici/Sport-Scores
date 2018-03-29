package ro.uaic.info.tppa.sportscores.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.models.SportEvent;

public class EventListAdapter extends ArrayAdapter<SportEvent> {
    private Context mContext;
    private List<SportEvent> eventsList = new ArrayList<>();

    public EventListAdapter(@NonNull Context context, ArrayList<SportEvent> list) {
        super(context, 0 , list);
        mContext = context;
        eventsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_event,parent,false);

        SportEvent currentEvent = eventsList.get(position);

        TextView homeTeamName = listItem.findViewById(R.id.homeTeam_name);
        homeTeamName.setText(currentEvent.getHomeTeam());

        TextView homeTeamScore = listItem.findViewById(R.id.homeTeam_score);
        homeTeamScore.setText(currentEvent.getHomeGoals());

        return listItem;
    }
}
