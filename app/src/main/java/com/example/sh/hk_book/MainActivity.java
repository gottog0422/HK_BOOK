package com.example.sh.hk_book;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sh.hk_book.adapter.ListAdapter;
import com.example.sh.hk_book.bus.BusProvider;
import com.example.sh.hk_book.data.Content;
import com.example.sh.hk_book.db.DB_Manager;
import com.example.sh.hk_book.event.Event_delete;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {
    static int ADD = 0;
    static int DEL = 1;

    Integer mYear;
    Integer mMonth;
    Integer mDay;

    Bus bus = BusProvider.getInstance().getBus();
    ArrayList<Content> items = new ArrayList<>();
    ListAdapter lvAdapter;
    DB_Manager dbManager;

    @BindView(R.id.lv_main)
    ListView lv_main;
    @BindView(R.id.btn_add)
    ImageView btn_add;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.btn_left)
    ImageView btn_left;
    @BindView(R.id.btn_right)
    ImageView btn_right;

    @BindView(R.id.tv_expense_day)
    TextView tv_expense_day;
    @BindView(R.id.tv_expense_month)
    TextView tv_expense_month;
    @BindView(R.id.tv_income_day)
    TextView tv_income_day;
    @BindView(R.id.tv_income_month)
    TextView tv_income_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bus.register(this);


        dbManager = new DB_Manager(MainActivity.this, "household.db", null, 1);

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        refreshList(mYear, mMonth + 1, mDay);


        inputDate(mYear, mMonth + 1, mDay);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
                .check();
    }

    public void refreshList(Integer year, Integer month, Integer day) {
        items = dbManager.getDataList(year, month, day);
        lvAdapter = new ListAdapter(items, dbManager);
        lv_main.setAdapter(lvAdapter);
        get_Sum();
    }

    public void inputDate(Integer year, Integer month, Integer day) {
        txt_title.setText(year + "/" + month + "/" + day);
    }

    @OnClick(R.id.btn_add)
    public void click_bt_add() {
        Intent intent = new Intent(MainActivity.this, InputActivity.class);
        intent.putExtra("year", mYear);
        intent.putExtra("month", mMonth);
        intent.putExtra("day", mDay);
        startActivityForResult(intent, ADD);
    }

    @OnItemClick(R.id.lv_main)
    public void item_click(int i) {
        Content item = items.get(i);

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("kind", item.getKind());
        intent.putExtra("cost", item.getCost());
        intent.putExtra("pos", i);
        startActivityForResult(intent, DEL);
    }

    @OnClick(R.id.txt_title)
    public void click_txt_title() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                inputDate(year, month + 1, day);
                mYear = year;
                mMonth = month;
                mDay = day;

                refreshList(mYear, mMonth + 1, mDay);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, dateSetListener,
                mYear, mMonth, mDay);
        dialog.show();
    }

    @OnClick(R.id.btn_right)
    public void click_btn_right() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, mYear);
        calendar1.set(Calendar.MONTH, mMonth);
        calendar1.set(Calendar.DAY_OF_MONTH, mDay);
        calendar1.add(Calendar.DAY_OF_MONTH, 1);
        mYear = calendar1.get(Calendar.YEAR);
        mMonth = calendar1.get(Calendar.MONTH);
        mDay = calendar1.get(Calendar.DAY_OF_MONTH);

        refreshList(mYear, mMonth + 1, mDay);
        inputDate(mYear, mMonth + 1, mDay);
    }

    @OnClick(R.id.btn_left)
    public void click_btn_left() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, mYear);
        calendar1.set(Calendar.MONTH, mMonth);
        calendar1.set(Calendar.DAY_OF_MONTH, mDay);
        calendar1.add(Calendar.DAY_OF_MONTH, -1);
        mYear = calendar1.get(Calendar.YEAR);
        mMonth = calendar1.get(Calendar.MONTH);
        mDay = calendar1.get(Calendar.DAY_OF_MONTH);

        refreshList(mYear, mMonth + 1, mDay);
        inputDate(mYear, mMonth + 1, mDay);
    }

    public void get_Sum() {
        tv_income_day.setText(String.valueOf(dbManager.get_today_income(mYear, mMonth + 1, mDay)));
        tv_income_month.setText(String.valueOf(dbManager.get_month_income(mYear, mMonth + 1)));

        tv_expense_day.setText(String.valueOf(dbManager.get_today_expense(mYear, mMonth + 1, mDay)));
        tv_expense_month.setText(String.valueOf(dbManager.get_month_expense(mYear, mMonth + 1)));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ADD) {
                Integer kind = data.getIntExtra("kind", -1);
                Integer cost = data.getIntExtra("cost", -1);
                Integer year = data.getIntExtra("year", -1);
                Integer month = data.getIntExtra("month", -1);
                Integer day = data.getIntExtra("day", -1);

                dbManager.insertData(cost, kind, year, month + 1, day);
                inputDate(year, month + 1, day);
                refreshList(year, month + 1, day);
            } else if (requestCode == DEL) {
                int delPos = data.getIntExtra("delPos", -1);
                dbManager.deleteData(items.get(delPos).getId());
                items.remove(delPos);
                lvAdapter.notifyDataSetChanged();
                get_Sum();
            }
        }
    }

    @Subscribe
    public void event_delete(Event_delete event) {
        get_Sum();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
