package org.ananas.checkList;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyActivity extends Activity
{
    List<ListItemData> lid       = new ArrayList<ListItemData>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // находим список
        ListView lvMain = (ListView) findViewById(R.id.listView);
        setList(lvMain);
    }

    public void btnClose_click(View v)
    {
        this.finish();
    }

    public void btnPaste_click(View v)
    {
        ClipboardManager   clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String             pasteData = "";
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        pasteData = item.getText().toString();
        EditText ed = (EditText) findViewById(R.id.textToParse);
        String srcText = ed.getText().toString();
        if (ed.getText().toString().equals(""))
        {
            srcText = pasteData;
        }
        String[] listItemsPrepare = srcText.replace(" и ", ",").replace(".", ",").replace("Купи", "").replace("купи", "").split(",");
        lid.clear();
        for (int i = 0; i < listItemsPrepare.length; i++)
        {
            if (listItemsPrepare[i].trim().length() > 0)
            {
                lid.add(new ListItemData(listItemsPrepare[i].trim(), false));
            }
        }
        sortList(lid);
        ListView lvMain = (ListView) findViewById(R.id.listView);
        lvMain.requestFocus();
    }


    private static void sortList(List<ListItemData> aItems)
    {
        Collections.sort(aItems);
    }

    private void setList(ListView lv)
    {
        sortList(lid);
        // создаем адаптер
        ArrayAdapter<ListItemData> adapter = new CustomAdapter(this, R.layout.checked_list_item, R.id.item_text, lid);
        // присваиваем адаптер списку
        lv.setAdapter(adapter);
    }


}
