package ro.uaic.info.tppa.sportscores.activities.competitions.countries;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.models.sportsdb.LeagueListItem;
import ro.uaic.info.tppa.sportscores.models.sportsdb.TableList;
import ro.uaic.info.tppa.sportscores.models.sportsdb.TableListItem;
import ro.uaic.info.tppa.sportscores.utils.SportsDbHttpUtils;

public class StandingsActivity extends AppCompatActivity {

    @BindView(R.id.tableStandings)
    TableLayout tableLayout;

    private ObjectMapper objectMapper = new ObjectMapper();
    private static String TAG = "STANDINGS_ACTIVITY";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        LeagueListItem league = (LeagueListItem) intent.getExtras().getSerializable("league");

        tableLayout.setStretchAllColumns(true);
        progressDialog = new ProgressDialog(this);

        ProgressDialog progress = new ProgressDialog(StandingsActivity.this);
        progress.setMessage("Please Wait...");
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        progress.show();

        SportsDbHttpUtils.get("lookuptable.php?l=" + league.getId(), null, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                progress.dismiss();

                TableList tableList = null;
                try {
                    tableList = objectMapper.readValue(response.toString(), TableList.class);
                    System.out.println(tableList);
                    fillTable(tableList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void fillTable(TableList tableList) {
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize = 0, mediumTextSize = 0;

        Log.d(TAG, "fillTable: tableList");

        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        List<TableListItem> data = tableList.getTable();

        int rows = data.size();
        TextView textSpacer = null;

        tableLayout.removeAllViews();

        // -1 means heading row
        for (int i = -1; i < rows; i++) {
            TableListItem row = null;
            if (i > -1)
                row = data.get(i);
            else {
                textSpacer = new TextView(this);
                textSpacer.setText("");

            }
            // data columns
            final TextView tv1;
            final TextView tv2;
            final TextView tv3;
            final TextView tv4;
            if (i == -1) {
                tv1 = getHeaderTextView(textSize, "Place", smallTextSize);
                tv2 = getHeaderTextView(textSize, "Name", smallTextSize);
                tv3 = getHeaderTextView(textSize, "Played", smallTextSize);
                tv4 = getHeaderTextView(textSize, "Points", smallTextSize);
            }
            else {
                tv1 = getTextView(textSize, String.valueOf(i + 1));
                tv2 = getTextView(textSize, row.getName());
                tv3 = getTextView(textSize, row.getPlayed());
                tv4 = getTextView(textSize, row.getTotal());
            }


            // add table row
            final TableRow tr = new TableRow(this);
            tr.setId(i + 1);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setLayoutParams(trParams);

            tr.setFocusable(true);
            tr.setFocusableInTouchMode(true);


            tr.addView(tv1);
            tr.addView(tv2);
            tr.addView(tv3);
            tr.addView(tv4);

            tableLayout.addView(tr, trParams);

            if (i > -1) {
                tr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        TableRow tr = (TableRow) v;
                        //do whatever action is needed
                    }
                });
            }



        }
    }

    @NonNull
    private TextView getTextView(int textSize, String played2) {
        final TextView tv3 = new TextView(this);
        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tv3.setGravity(Gravity.LEFT);

        tv3.setPadding(5, 15, 0, 15);


        tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
        tv3.setText(played2);
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        return tv3;
    }

    @NonNull
    private TextView getHeaderTextView(int textSize, String played2, float smallTextSize) {
        final TextView tv3 = new TextView(this);
        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tv3.setGravity(Gravity.LEFT);

        tv3.setPadding(5, 15, 0, 15);


        tv3.setText(played2);
        tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);

        return tv3;
    }
}
