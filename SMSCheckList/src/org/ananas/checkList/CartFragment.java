package org.ananas.checkList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: AnaNas
 * Date: 25.07.13
 * Time: 12:43
 */
public class CartFragment extends Fragment
{
    public CartFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.cart_list, container, false);

        return rootView;
    }
}