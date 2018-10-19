package com.example.sh.hk_book;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sh.hk_book.data.Content;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputActivity extends AppCompatActivity {
    private DecimalFormat df = new DecimalFormat("#,###");

    @BindView(R.id.et_cost)
    EditText et_cost;
    @BindView(R.id.btn_deposit)
    Button btn_deposit;
    @BindView(R.id.btn_withdraw)
    Button btn_withdraw;
    @BindView(R.id.txt_input_date)
    TextView txt_input_date;

    Integer mYear;
    Integer mMonth;
    Integer mDay;

    boolean b = true;

    String result = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mYear = intent.getIntExtra("year", -1);
        mMonth = intent.getIntExtra("month", -1);
        mDay = intent.getIntExtra("day", -1);

        inputDate(mYear, mMonth + 1, mDay);

        et_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() == 0) {
                    result = "0";
                    et_cost.setText("0");
                    et_cost.setSelection(1);
                } else {
                    if (!s.toString().equals(result)) {
                        result = df.format(Long.parseLong(s.toString().replaceAll(",", "")));
                        et_cost.setText(result);    // 결과 텍스트 셋팅.
                        et_cost.setSelection(result.length());     // 커서를 제일 끝으로 보냄.
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void inputDate(Integer year, Integer month, Integer day) {
        txt_input_date.setText(year + "/" + month + "/" + day);
    }

    @OnClick(R.id.btn_deposit)
    public void click_deposit() {

        if (et_cost != null) {
            Integer cost = Integer.parseInt(et_cost.getText().toString().replaceAll(",", ""));

            Intent intent = getIntent();
            intent.putExtra("kind", Content.INCOME);
            intent.putExtra("cost", cost);
            intent.putExtra("year", mYear);
            intent.putExtra("month", mMonth);
            intent.putExtra("day", mDay);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @OnClick(R.id.btn_withdraw)
    public void click_withdraw() {
        if (et_cost != null) {
            Integer cost = Integer.parseInt(et_cost.getText().toString().replaceAll(",", ""));

            Intent intent = getIntent();
            intent.putExtra("kind", Content.EXPENSE);
            intent.putExtra("cost", cost);
            intent.putExtra("year", mYear);
            intent.putExtra("month", mMonth);
            intent.putExtra("day", mDay);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @OnClick(R.id.txt_input_date)
    public void click_input_date() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                inputDate(year, month + 1, day);
                mYear = year;
                mMonth = month;
                mDay = day;

            }
        };

        DatePickerDialog dialog = new DatePickerDialog(InputActivity.this, dateSetListener,
                mYear, mMonth, mDay);
        dialog.show();
    }

    @OnClick(R.id.et_cost)
    public void go_right_focus(){
        et_cost.setSelection(result.length());
    }

}
