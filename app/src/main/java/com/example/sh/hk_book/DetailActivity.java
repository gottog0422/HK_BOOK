package com.example.sh.hk_book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sh.hk_book.data.Content;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.iv_income_expense)
    ImageView iv_income_expense;
    @BindView(R.id.txt_income_expense)
    TextView txt_income_expense;
    @BindView(R.id.txt_cost)
    TextView txt_cost;
    @BindView(R.id.btn_delete)
    Button btn_delete;

    Integer pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Integer kind = intent.getIntExtra("kind", -1);
        Integer cost = intent.getIntExtra("cost", -1);
        pos = intent.getIntExtra("pos", -1);


        if (kind == Content.INCOME) {
            iv_income_expense.setBackgroundResource(R.drawable.shape_income_oval);
            txt_income_expense.setText(R.string.income);
        } else {
            iv_income_expense.setBackgroundResource(R.drawable.shape_expense_oval);
            txt_income_expense.setText(R.string.expense);
        }

        txt_cost.setText(cost.toString());

    }

    @OnClick(R.id.btn_delete)
    public void click_delete() {
        Intent intent1 = getIntent();
        intent1.putExtra("delPos", pos);
        setResult(RESULT_OK, intent1);
        finish();
    }
}
