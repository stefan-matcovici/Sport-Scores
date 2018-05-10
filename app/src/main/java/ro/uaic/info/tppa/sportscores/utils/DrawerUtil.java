package ro.uaic.info.tppa.sportscores.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import ro.uaic.info.tppa.sportscores.LiveEventsActivity;
import ro.uaic.info.tppa.sportscores.PreferencesActivity;
import ro.uaic.info.tppa.sportscores.R;
import ro.uaic.info.tppa.sportscores.SelectorActivity;

public class DrawerUtil {
    public static void getDrawer(final AppCompatActivity activity, Toolbar toolbar) {

        PrimaryDrawerItem drawerItemFootball = new PrimaryDrawerItem().withIdentifier(R.string.football)
                .withName(R.string.football).withIcon(R.mipmap.ic_football);
        PrimaryDrawerItem drawerItemBasketball = new PrimaryDrawerItem().withIdentifier(R.string.basketball)
                .withName(R.string.basketball).withIcon(R.mipmap.ic_basketball);
        PrimaryDrawerItem drawerItemTennis = new PrimaryDrawerItem().withIdentifier(R.string.tennis)
                .withName(R.string.tennis).withIcon(R.mipmap.ic_tennis);


        SecondaryDrawerItem drawerItemSettings = new SecondaryDrawerItem().withIdentifier(4)
                .withName(R.string.settings).withIcon(R.mipmap.ic_settings_black_24dp);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        final String sport = prefs.getString("default_sport", "");

        System.out.println(sport);

        Drawable drawable = null;

        if (sport.equals(activity.getResources().getString(R.string.football))) {
            drawable = activity.getResources().getDrawable(R.drawable.header_football);
        } else if (sport.equals(activity.getResources().getString(R.string.basketball))) {
            drawable = activity.getResources().getDrawable(R.drawable.header_basketball);
        } else if (sport.equals(activity.getResources().getString(R.string.tennis))) {
            drawable = activity.getResources().getDrawable(R.drawable.header_tennis);
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(drawable)
                .build();


        activity.setSupportActionBar(toolbar);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withActionBarDrawerToggle(true)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        drawerItemFootball,
                        drawerItemBasketball,
                        drawerItemTennis,
                        new DividerDrawerItem(),
                        drawerItemSettings
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("default_sport", activity.getResources().getString((int)drawerItem.getIdentifier()));
                        editor.apply();

                        if (drawerItem.getIdentifier() == 4) {
                            Intent intent = new Intent(activity, PreferencesActivity.class);
                            view.getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(activity, SelectorActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        return true;
                    }

                })
                .build();

        result.closeDrawer();

    }
}
