package ro.uaic.info.tppa.sportscores.models.sportsdb;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;
import java.util.stream.Collectors;

public class LeagueList {
    List<LeagueListItem> leagues;

    public List<LeagueListItem> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<LeagueListItem> leagues) {
        this.leagues = leagues;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterLeaguesBySport(String sport) {
        leagues = leagues.stream().filter(leagueListItem -> leagueListItem.getSport().equals(sport)).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> getLeaguesNames() {
        return leagues.stream().map(LeagueListItem::getLeague).collect(Collectors.toList());
    }
}
