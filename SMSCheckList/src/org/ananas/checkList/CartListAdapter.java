package org.ananas.checkList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: AnaNas
 * Date: 10.07.13
 * Time: 11:29
 */
public class CartListAdapter extends ArrayAdapter<ListItemData> {
    private List<ListItemData> listData;
    private List<ListItemData> cartList;

    public CartListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CartListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CartListAdapter(Context context, int textViewResourceId, ListItemData[] objects) {
        super(context, textViewResourceId, objects);
    }

    public CartListAdapter(Context context, int resource, int textViewResourceId, ListItemData[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public CartListAdapter(Context context, int textViewResourceId, List<ListItemData> objects) {
        super(context, textViewResourceId, objects);
    }

    public CartListAdapter(Context context, int resource, int textViewResourceId, List<ListItemData> objects, List<ListItemData> cartObjects) {
        super(context, resource, textViewResourceId, cartObjects);
        listData = objects;
        cartList = cartObjects;
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(cartList);
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        final ListItemData lid = getItem(position);
        if (convertView == null)
            view = View.inflate(getContext(), R.layout.checked_list_item, null);
        else
            view = convertView;

        TextView title = (TextView) view.findViewById(R.id.item_text);
        title.setText(lid.item);
        final int i = position;
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lid.checked) {
                    lid.checked = false;
                    cartList.remove(i);
                    if (isDistinct(listData, lid.item)) {
                        listData.add(lid);
                    }
                    //showToast(lid.item+" в списке...");
                } else {
                    lid.checked = true;
                }
                notifyDataSetChanged();
            }
        });
        if (lid.checked) {
            title.setTextColor(Color.GREEN);
        } else {
            title.setTextColor(Color.WHITE);
        }
        CheckBox check = (CheckBox) view.findViewById(R.id.checkBox);
        check.setChecked(lid.checked);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartList.get(i).checked = isChecked;
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT);
        toast.setDuration(1);
        toast.show();
    }

    private boolean isDistinct(List<ListItemData> lid, String itemLabel) {
        Iterator itr = lid.iterator();
        while (itr.hasNext()) {
            ListItemData listID = (ListItemData) itr.next();
            if (itemLabel.equals(listID.getItem())) {
                return false;
            }
        }
        return true;
    }
}
