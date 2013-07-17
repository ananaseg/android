package org.ananas.checkList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Collections;
import java.util.List;

/**
 * User: AnaNas
 * Date: 10.07.13
 * Time: 11:29
 */
public class CustomAdapter extends ArrayAdapter<ListItemData> {
    private List<ListItemData> data;

    public CustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CustomAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CustomAdapter(Context context, int textViewResourceId, ListItemData[] objects) {
        super(context, textViewResourceId, objects);
    }

    public CustomAdapter(Context context, int resource, int textViewResourceId, ListItemData[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public CustomAdapter(Context context, int textViewResourceId, List<ListItemData> objects) {
        super(context, textViewResourceId, objects);
    }

    public CustomAdapter(Context context, int resource, int textViewResourceId, List<ListItemData> objects) {
        super(context, resource, textViewResourceId, objects);
        data = objects;
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(data);
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
                } else {
                    lid.checked = true;
                    showToast(data.get(i).item);
                }
                notifyDataSetChanged();
            }
        });
        if (lid.checked) {
            title.setTextColor(Color.GRAY);
        } else {
            title.setTextColor(Color.WHITE);
        }
        CheckBox check = (CheckBox) view.findViewById(R.id.checkBox);
        check.setChecked(lid.checked);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(i).checked = isChecked;
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
}
