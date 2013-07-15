package org.ananas.checkList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyActivity extends Activity {
    List<ListItemData> lid = new ArrayList<ListItemData>();
    final int DIALOG_PASTE = 1;
    final int DIALOG_CREATE = 2;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
////инициализация
//        tabHost.setup();
//        TabHost.TabSpec tabSpec;
////создаем вкладку и указываем тег
//        tabSpec = tabHost.newTabSpec("tag1");
////указываем id компонента из FrameLayout, он и станет содержимым
//        tabSpec.setContent(R.id.tabView1);
////добавляем в корневой элемент
//        tabHost.addTab(tabSpec);
//        tabSpec = tabHost.newTabSpec("tag2");
////указываем название и картинку
////в нашем случае вместо картинки идет xml-файл,
////который определяет картинку посостоянию вкладки
//        tabSpec.setContent(R.id.tabView2);
////создаем View из layout-файла
//        View v = getLayoutInflater().inflate(R.layout.tab_header, null);
////и устанавливаем его, как заголовок
//        tabSpec.setIndicator(v);
////вторая вкладка будет выбрана по умолчанию
//        tabHost.setCurrentTabByTag("tag2");
////обработчик переключения вкладок
//        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            public void onTabChanged(String tabId) {
//                Toast.makeText(getBaseContext(), "tabId=" + tabId, Toast.LENGTH_SHORT).show();
//            }
//        });
        // находим список
        ListView lvMain = (ListView) findViewById(R.id.listView);
        setList(lvMain);
        lvMain.requestFocus();
    }

    public void btnClose_click(View v) {
        this.finish();
    }

    /**
     * Вставка данных из буфера и формирование списка
     */
    protected void pasteData(boolean addText) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "";
        ClipData.Item item = null;
        EditText ed = (EditText) findViewById(R.id.textToParse);
        String srcText = "";
        if (!ed.getText().toString().equals("")) {
            srcText = ed.getText().toString();
        }
        ;
        if (clipboard.hasText() && (clipboard.getPrimaryClip().getItemCount() > 0)) {
            item = clipboard.getPrimaryClip().getItemAt(0);
            if (addText) pasteData = srcText + "," + item.getText().toString();
            else pasteData = item.getText().toString();
        }
        srcText = pasteData;
        if (!srcText.equals("")) {
            ed.setText(srcText);
            setListItems(srcText);   //Создаем список
        }
    }

    /**
     * Создаем список из EditText
     */
    protected void createData() {
        EditText ed = (EditText) findViewById(R.id.textToParse);
        String srcText = ed.getText().toString();
        ed.setText(srcText);
        setListItems(srcText);   //Создаем список
    }

    /**
     * Заполнение ListView
     *
     * @param srcText
     */
    private void setListItems(String srcText) {
        String[] listItemsPrepare = srcText.replace(" и ", ",").replace(".", ",").replace("Купи", "").replace("купи", "").split(",");
        lid.clear();
        for (int i = 0; i < listItemsPrepare.length; i++) {
            if (listItemsPrepare[i].trim().length() > 0) {
                lid.add(new ListItemData(listItemsPrepare[i].trim(), false));
            }
        }
        sortList(lid);

        ListView lvMain = (ListView) findViewById(R.id.listView);
        lvMain.invalidateViews();
    }

    /**
     * Обработка кнопки вставки из буфера
     *
     * @param v
     */
    public void btnPaste_click(View v) {
        showDialog(DIALOG_PASTE);
    }

    /**
     * Обработка кнопки создать
     *
     * @param v
     */
    public void btnCreate_click(View v) {
        showDialog(DIALOG_CREATE);
    }

    /**
     * Popup dialog
     *
     * @param id
     * @return
     */
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_PASTE) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            //заголовок
            adb.setTitle(R.string.listcreationcb);
            //сообщение
            adb.setMessage(R.string.append_data);
            //иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            //кнопка отрицательного ответа
            adb.setNegativeButton(R.string.cancel, pasteDialogListener);
            //кнопка положительного ответа
            adb.setPositiveButton(R.string.recreate, pasteDialogListener);
            //кнопка положительного ответа
            adb.setNeutralButton(R.string.add, pasteDialogListener);
            //создаем диалог
            return adb.create();
        }
        if (id == DIALOG_CREATE) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            //заголовок
            adb.setTitle(R.string.listcreation);
            //сообщение
            adb.setMessage(R.string.delete_data);
            //иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            //кнопка отрицательного ответа
            adb.setNegativeButton(R.string.no, createDialogListener);
            //кнопка положительного ответа
            adb.setPositiveButton(R.string.yes, createDialogListener);
            //создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    /**
     * Обработчик кнопок диалогового окна
     */
    DialogInterface.OnClickListener pasteDialogListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                //положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    pasteData(false);  // если согласились перезаписать данные, то вставляем их из буфера
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    pasteData(true);  // если согласились перезаписать данные, то вставляем их из буфера
                    break;
                //негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    /**
     * Обработчик кнопок диалогового окна
     */
    DialogInterface.OnClickListener createDialogListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                //положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    createData();  // если согласились перезаписать данные, то вставляем их из буфера
                    break;
                //негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    private static void sortList(List<ListItemData> aItems) {
        Collections.sort(aItems);
    }

    private void setList(ListView lv) {
        sortList(lid);
        // создаем адаптер
        ArrayAdapter<ListItemData> adapter = new CustomAdapter(this, R.layout.checked_list_item, R.id.item_text, lid);
        // присваиваем адаптер списку
        lv.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void showToast(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
