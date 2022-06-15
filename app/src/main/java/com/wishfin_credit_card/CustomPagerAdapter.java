package com.wishfin_credit_card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomPagerAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;

    public CustomPagerAdapter(Context mcontext, ArrayList<CibilQuestionanswergetset> myadaptlist) {

        this.context = mcontext;
        Constants.multipleinput_question_list = myadaptlist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Constants.multipleinput_question_list.size();
    }

    @Override
    public Object getItem(int i) {
        return Constants.multipleinput_question_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.questionadapter, viewGroup, false);

            holder.tv1 = view.findViewById(R.id.txt_dia);
            holder.answerbox = view.findViewById(R.id.answerbox);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.answerbox.setId(i);
        holder.tv1.setText(Constants.multipleinput_question_list.get(i).getQuestion());

        //we need to update adapter once we finish with editing
        holder.answerbox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    Constants.multipleinput_question_list.get(position).setAsnwer(Caption.getText().toString());
                }
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView tv1;
        EditText answerbox;
        RadioButton ch1, ch2, ch3, ch4, ch5;
        RadioGroup RG;
    }

}
