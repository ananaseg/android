package org.ananas.checkList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * User: AnaNas
 * Date: 25.07.13
 * Time: 12:39
 */
public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
    ArrayList<ListItemData> lid;
    String[] tabNames = {"", "", ""};
    ArrayList<ListItemData> checkListItemData;
    ArrayList<ListItemData> cartListItemData;

    public AppSectionsPagerAdapter(String[] tabNames, FragmentManager fm, ArrayList<ListItemData> listItemData, ArrayList<ListItemData> cartItemData) {
        super(fm);
        checkListItemData = listItemData;
        cartListItemData = cartItemData;
        this.tabNames = tabNames;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new CreateListFragment();
            case 1:
                return new CheckListFragment(checkListItemData, cartListItemData);
            case 2:
                return new CartFragment(checkListItemData, cartListItemData);
            default:
                return new CreateListFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }
}

