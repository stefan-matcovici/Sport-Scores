package ro.uaic.info.tppa.sportscores;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import ro.uaic.info.tppa.sportscores.adapters.InternationalEventListAdapter;
import ro.uaic.info.tppa.sportscores.models.firebase.Subscription;
import ro.uaic.info.tppa.sportscores.models.livescores.InternationalEvent;
import ro.uaic.info.tppa.sportscores.utils.DrawerUtil;
import ro.uaic.info.tppa.sportscores.utils.LivescoresHttpUtils;

public class LiveEventsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.events_list)
    public ListView mListView;

    private ObjectMapper objectMapper = new ObjectMapper();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ButterKnife.bind(this);
        DrawerUtil.getDrawer(this, mToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                        channelName, NotificationManager.IMPORTANCE_HIGH));
            }
        }

        LivescoresHttpUtils.get("/soccer/live_events", null, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                final InternationalEvent[] eventList;
                try {
                    eventList = objectMapper.readValue(response.toString(), InternationalEvent[].class);

                    final ArrayAdapter<InternationalEvent> eventArrayAdapter = new InternationalEventListAdapter(LiveEventsActivity.this, Arrays.asList(eventList), true);
                    mListView.setAdapter(eventArrayAdapter);
                    mListView.setOnItemClickListener((parent, view, position, id) -> {
                        Subscription subscription = new Subscription();
                        subscription.setMatchId(eventList[position].getId());

                        DatabaseReference subscriptions = mDatabase.child("subscriptions").child(subscription.getUserId());
                        DatabaseReference child = subscriptions.child(subscription.getMatchId());

                        child.setValue(0);
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
