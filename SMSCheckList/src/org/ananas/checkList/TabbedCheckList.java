package org.ananas.checkList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: AnaNas
 * Date: 24.07.13
 * Time: 15:29
 */
public class TabbedCheckList extends FragmentActivity implements ActionBar.TabListener {
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    ListView lvMain;
    ClipboardManager clipboard;
    private ArrayList<ListItemData> listItemData = new ArrayList<ListItemData>();

    final int DIALOG_PASTE = 1;
    final int DIALOG_CREATE = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(), listItemData);
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

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


    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }


    /**
     * Обработка кнопки вставки из буфера
     *
     * @param v
     */
    public void btnPaste_click(View v) {
        showDialog(DIALOG_PASTE);
        lvMain = (ListView) findViewById(R.id.checkListView);
        hideKeyboard();
    }

    /**
     * Обработка кнопки создать
     *
     * @param v
     */
    public void btnCreate_click(View v) {
        showDialog(DIALOG_CREATE);
        lvMain = (ListView) findViewById(R.id.checkListView);
        hideKeyboard();
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
                    pasteData(listItemData, false);  // если согласились перезаписать данные, то вставляем их из буфера
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    pasteData(listItemData, true);  // если согласились добавить данные, то вставляем их из буфера
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
                    createData(listItemData);  // если согласились перезаписать данные, то вставляем их из буфера
                    break;
                //негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    /**
     * Вставка данных из буфера и формирование списка
     */
    protected void pasteData(ArrayList<ListItemData> listItemData, boolean addText) {
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
            if (addText)
                pasteData = srcText + "," + item.getText().toString();
            else {
                pasteData = item.getText().toString();
            }
        }
        srcText = pasteData;
        if (!srcText.equals("")) {
            ed.setText(srcText);
            setListItems(listItemData, srcText);   //Создаем список
        }
        hideKeyboard();
    }

    /**
     * Создаем список из EditText
     */
    protected void createData(ArrayList<ListItemData> listItemData) {
        EditText ed = (EditText) findViewById(R.id.textToParse);
        String srcText = ed.getText().toString();
        ed.setText(srcText);
        setListItems(listItemData, srcText);   //Создаем список
        hideKeyboard();
    }

    /**
     * Заполнение ceckListView
     *
     * @param srcText
     */
    public void setListItems(ArrayList<ListItemData> lid, String srcText) {
        String[] listItemsPrepare = srcText.replace(" и ", ",").replace(".", ",").replace("Купи", "").replace("купи", "").split(",");
        lid.clear();
        for (int i = 0; i < listItemsPrepare.length; i++) {
            if (listItemsPrepare[i].trim().length() > 0) {
                ListItemData itemLabel = new ListItemData(listItemsPrepare[i].trim(), false);
                if (isDistinct(lid, itemLabel.getItem()))     //Убираем дублирование в списке
                {
                    lid.add(itemLabel);
                }
            }
        }
        Collections.sort(lid);

        lvMain = (ListView) findViewById(R.id.checkListView);
        if (lvMain != null) {
            lvMain.invalidateViews();
        }
    }

    void hideKeyboard() {
        ListView lvMain = (ListView) findViewById(R.id.checkListView);
        if (lvMain != null) {
            lvMain.requestFocus(View.FOCUS_DOWN);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            EditText ed = (EditText) findViewById(R.id.textToParse);
            imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
        }
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
