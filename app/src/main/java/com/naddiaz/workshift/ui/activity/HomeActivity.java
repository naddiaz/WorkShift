package com.naddiaz.workshift.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.fragment.CalendarFragment;

import br.liveo.Model.HelpLiveo;
import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.navigationliveo.NavigationLiveo;

public class HomeActivity extends NavigationLiveo implements OnItemClickListener {

    private static final String ACTUAL_FRAGMENT = "actual_fragment";

    private HelpLiveo mHelpLiveo;

    @Override
    public void onInt(Bundle bundle) {
        // User Information
        this.userName.setText("Néstor Álvarez");
        this.userEmail.setText("naddiaz.92@gmail.com");
        this.userPhoto.setImageResource(R.drawable.ic_action_profile);
        this.userBackground.setImageResource(R.drawable.ic_user_background_first);

        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.addSubHeader(getString(R.string.menu_subheaher_schedule));
        mHelpLiveo.add(getString(R.string.menu_item_calendar), R.drawable.ic_action_calendar);
        mHelpLiveo.addSubHeader(getString(R.string.menu_subheaher_options));
        mHelpLiveo.add(getString(R.string.menu_item_insert), R.drawable.ic_action_add);
        mHelpLiveo.add(getString(R.string.menu_item_personalized), R.drawable.ic_action_personalize);
        mHelpLiveo.addSeparator();

        with(this).startingPosition(2)
                .addAllHelpItem(mHelpLiveo.getHelp())
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .footerItem(R.string.action_settings, R.drawable.ic_action_settings)
                .build();
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        switch (position){
            default:
                mFragment = CalendarFragment.newInstance(mHelpLiveo.get(position).getName());
                break;
        }
        if (mFragment != null){
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_side_left, R.anim.slide_side_rigth)
                    .replace(R.id.container, mFragment).commit();
        }
        setElevationToolBar(position != 2 ? 15 : 0);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };
}
