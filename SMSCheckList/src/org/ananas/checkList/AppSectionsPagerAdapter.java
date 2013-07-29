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
public class AppSectionsPagerAdapter extends FragmentPagerAdapter
{
    ArrayList<ListItemData> lid;
    public AppSectionsPagerAdapter(FragmentManager fm, ArrayList<ListItemData> listItemData)
    {
        super(fm);
        lid = listItemData;
    }

    @Override
    public Fragment getItem(int i)
    {
        switch (i)
        {
            case 0:
                return new CreateListFragment();
            case 1:
                return new CheckListFragment(lid);
            case 2:
                return new CartFragment();
            default:
                return new CreateListFragment();
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {

        switch (position)
        {
            case 0:
                return "1";//this.activity.getString(R.string.edit_list);
            case 1:
                return "2";//this.activity.getString(R.string.check_list);
            case 2:
                return "3";//this.activity.getString(R.string.cart_list);
        }
        return "";
    }
}

