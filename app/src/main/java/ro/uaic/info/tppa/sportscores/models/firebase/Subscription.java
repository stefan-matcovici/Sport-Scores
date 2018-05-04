package ro.uaic.info.tppa.sportscores.models.firebase;

import com.google.firebase.iid.FirebaseInstanceId;

public class Subscription {
    String userId = FirebaseInstanceId.getInstance().getToken();
    String matchId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
