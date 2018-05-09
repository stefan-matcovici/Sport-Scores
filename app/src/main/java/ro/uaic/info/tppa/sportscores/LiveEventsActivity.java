package ro.uaic.info.tppa.sportscores;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

        ProgressDialog progress = new ProgressDialog(LiveEventsActivity.this);
        progress.setMessage("Please Wait...");
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        progress.show();


        LivescoresHttpUtils.get("/soccer/live_events", null, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                final InternationalEvent[] eventList;
                try {
                    progress.dismiss();
                    eventList = objectMapper.readValue(response.toString(), InternationalEvent[].class);

                    InternationalEventListAdapter internationalEventListAdapter = new InternationalEventListAdapter(LiveEventsActivity.this, Arrays.asList(eventList), true);
                    final ArrayAdapter<InternationalEvent> eventArrayAdapter = internationalEventListAdapter;
                    mListView.setAdapter(eventArrayAdapter);
                    Subscription subscription = new Subscription();

                    mListView.setOnItemClickListener((parent, view, position, id) -> {
                        InternationalEvent internationalEvent = internationalEventListAdapter.getEventsList().get(position);

                        if (internationalEvent.subscribed) {
                            CharSequence text = "Unsubscribed";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(LiveEventsActivity.this, text, duration);
                            toast.show();

                            subscription.setMatchId(eventList[position].getId());
                            DatabaseReference subscriptions = mDatabase.child("subscriptions").child(subscription.getUserId());
                            DatabaseReference child = subscriptions.child(subscription.getMatchId());
                            child.removeValue((databaseError, databaseReference) -> {
                                toast.show();
                            });

                            internationalEvent.subscribed = false;
                            internationalEventListAdapter.notifyDataSetChanged();

                        } else {
                            CharSequence text = "Subscribed";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(LiveEventsActivity.this, text, duration);
                            toast.show();

                            subscription.setMatchId(eventList[position].getId());
                            DatabaseReference subscriptions = mDatabase.child("subscriptions").child(subscription.getUserId());
                            DatabaseReference child = subscriptions.child(subscription.getMatchId());

                            String minute = internationalEvent.getMinute();
                            if (minute.contains("'")) {
                                String min = internationalEvent.getMinute().substring(0, minute.indexOf("'"));
                                child.setValue(Integer.valueOf(min));
                            }
                            else {
                                child.setValue(0);
                            }

                            internationalEvent.subscribed = true;
                            internationalEventListAdapter.notifyDataSetChanged();
                        }


                    });

                    DatabaseReference subs = mDatabase.child("subscriptions").child(subscription.getUserId());
                    subs.addListenerForSingleValueEvent(new ValueEventListener() {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progress.dismiss();

                            for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                                String matchId = item_snapshot.getKey();
                                for (InternationalEvent internationalEvent : internationalEventListAdapter.getEventsList()) {
                                    if (matchId.equals(internationalEvent.getId())) {
                                        internationalEvent.subscribed = true;
                                    }
                                }

                                internationalEventListAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
