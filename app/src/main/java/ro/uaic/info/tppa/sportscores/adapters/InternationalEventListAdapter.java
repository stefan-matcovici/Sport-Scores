package ro.uaic.info.tppa.sportscores.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import ro.uaic.info.tppa.sportscores.utils.DateUtils;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_event, parent, false);

        InternationalEvent currentEvent = eventsList.get(position);

        TextView homeTeamName = listItem.findViewById(R.id.homeTeam_name);
        homeTeamName.setText(currentEvent.getHomeTeam());

        TextView awayTeamName = listItem.findViewById(R.id.awayTeam_name);
        awayTeamName.setText(currentEvent.getAwayTeam());

        TextView round = listItem.findViewById(R.id.round);
        round.setText(currentEvent.getHeader().getStage());

        TextView time = listItem.findViewById(R.id.datetime);
        time.setText(currentEvent.getHeader().getDate());

        TextView minute = listItem.findViewById(R.id.minute);
        minute.setText(currentEvent.getMinute());

        String[] awayScores = currentEvent.getAwayScore().split("\n");
        String[] homeScores = currentEvent.getHomeScore().split("\n");

        TextView score = listItem.findViewById(R.id.score);
        score.setText(awayScores[0] + " : " + homeScores[0]);

        StringBuilder scr = new StringBuilder();
        for (int i=1; i< awayScores.length; i++) {
            if (!awayScores[i].equals("(") && !awayScores[i].equals(")") ){
                scr.append("(").append(homeScores[i]).append(" : ").append(awayScores[i]).append(") ");
            }

        }

        TextView scoreDetail = listItem.findViewById(R.id.scoreDetail);
        scoreDetail.setText(scr.toString());

        if (currentEvent.subscribed) {
            listItem.findViewById(R.id.header).setBackgroundColor(Color.rgb(227, 218, 114));
        }
        else {
            listItem.findViewById(R.id.header).setBackgroundColor(Color.LTGRAY);
        }


        return listItem;
    }

    public List<InternationalEvent> getEventsList() {
        return eventsList;
    }

    public void setEventsList(List<InternationalEvent> eventsList) {
        this.eventsList = eventsList;
    }
}
