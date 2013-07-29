package org.ananas.checkList;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * User: AnaNas
 * Date: 25.07.13
 * Time: 13:52
 */
public class CheckListFragment extends Fragment {
    ArrayList<ListItemData> _lid;
    ArrayList<ListItemData> _cid;
    ClipboardManager clipboard;

    public CheckListFragment(ArrayList<ListItemData> listItemData, ArrayList<ListItemData> cartItemData) {
        _lid = listItemData;
        _cid = cartItemData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.check_list, container, false);

        // находим список
        ListView lvMain = (ListView) rootView.findViewById(R.id.checkListView);
        setList(getActivity().getApplicationContext(), lvMain, _lid, _cid);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasText()) {
            pasteData(_lid, false);
        }
        return rootView;
    }

    public void setList(Context context, ListView lv, ArrayList<ListItemData> lid, ArrayList<ListItemData> cid) {
        if (lv != null) {
            Collections.sort(lid);
            // создаем адаптер
            ArrayAdapter<ListItemData> adapter = new CheckListAdapter(context, R.layout.checked_list_item, R.id.item_text, lid, cid);
            // присваиваем адаптер списку
            lv.setAdapter(adapter);
        }
    }

    /**
     * Вставка данных из буфера и формирование списка
     */
    protected void pasteData(ArrayList<ListItemData> listItemData, boolean addText) {
        String pasteData = "";
        ClipData.Item item = null;
        EditText ed = (EditText) getActivity().findViewById(R.id.textToParse);
        String srcText = "";
        if (!ed.getText().toString().equals("")) {
            srcText = ed.getText().toString();
        }

        if (clipboard.hasText() && (clipboard.getPrimaryClip().getItemCount() > 0)) {
            item = clipboard.getPrimaryClip().getItemAt(0);
            if (addText)
                pasteData = srcText + "," + item.getText().toString();
            else {
                pasteData = item.getText().toString();
            }
        }
        srcText = pasteData;
        if (!srcText.equals("")) {
            ed.setText(srcText);
            ((TabbedCheckList) getActivity()).setListItems(listItemData, srcText);   //Создаем список
        }
        ((TabbedCheckList) getActivity()).hideKeyboard();
    }
}