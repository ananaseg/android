package org.ananas.checkList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: AnaNas
 * Date: 25.07.13
 * Time: 12:42
 */
public class CreateListFragment extends Fragment
{
    public CreateListFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.create_list, container, false);

        return rootView;
    }
}
