package com.example.sh.hk_book.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sh.hk_book.R;
import com.example.sh.hk_book.bus.BusProvider;
import com.example.sh.hk_book.data.Content;
import com.example.sh.hk_book.db.DB_Manager;
import com.example.sh.hk_book.event.Event_delete;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends BaseAdapter {
    DecimalFormat df = new DecimalFormat("#,###");
    ArrayList<Content> items;
    DB_Manager db_manager;

    public ListAdapter(ArrayList<Content> items, DB_Manager db_manager) {
        this.items = items;
        this.db_manager = db_manager;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_content, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        Content item = (Content) getItem(i);

        if (item.getKind() == Content.INCOME) {
            holder.iv_income_expense.setBackgroundResource(R.drawable.shape_income_oval);
            holder.txt_income_expense.setText(R.string.income);
        } else if (item.getKind() == Content.EXPENSE) {
            holder.iv_income_expense.setBackgroundResource(R.drawable.shape_expense_oval);
            holder.txt_income_expense.setText(R.string.expense);
        }

        holder.txt_cost.setText(toNumFormat(item.getCost()));

        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(viewGroup.getContext());
                alertDialog.setTitle("경고");
                alertDialog.setMessage("정말 삭제하시겠습니까?");
                alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ps) {
                        db_manager.deleteData(items.get(i).getId());
                        items.remove(i);

                        Event_delete event_delete = new Event_delete();
                        BusProvider.getInstance().getBus().post(event_delete);

                        notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ps) {

                    }
                });
                alertDialog.show();
            }
        });


        return view;
    }

    public String toNumFormat(int num) {

        return df.format(num);
    }

    static class ViewHolder {
        @BindView(R.id.iv_income_expense)
        ImageView iv_income_expense;
        @BindView(R.id.txt_cost)
        TextView txt_cost;
        @BindView(R.id.txt_income_expense)
        TextView txt_income_expense;
        @BindView(R.id.btn_del)
        ImageView btn_del;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
