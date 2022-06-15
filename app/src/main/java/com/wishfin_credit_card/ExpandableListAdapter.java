package com.wishfin_credit_card;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _radio_question_list; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<CibilQuestionanswergetset>> _radio_option_list;

    public ExpandableListAdapter(Context context, List<String> radio_question_list,
                                 HashMap<String, ArrayList<CibilQuestionanswergetset>> listChildData) {
        this._context = context;
        this._radio_question_list = radio_question_list;
        this._radio_option_list = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._radio_option_list.get(this._radio_question_list.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = _radio_option_list.get(this._radio_question_list.get(groupPosition))
                .get(childPosition).getOptionone();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        RadioButton lblListHeader = convertView
                .findViewById(R.id.rd);
        lblListHeader.setText(childText);

        CibilQuestionanswergetset cibilQuestionanswergetset = new CibilQuestionanswergetset(String.valueOf(groupPosition), String.valueOf(childPosition));

        lblListHeader.setTag(cibilQuestionanswergetset);

        if (Constants.radio_option_list.get(Constants.radio_question_list.get(groupPosition)).get(childPosition).getRadiostatus().equalsIgnoreCase("true")) {
            lblListHeader.setChecked(true);
        } else {
            lblListHeader.setChecked(false);
        }

        lblListHeader.setOnClickListener(view -> {

            CibilQuestionanswergetset object = (CibilQuestionanswergetset) view.getTag();
            int group_pos = Integer.parseInt(object.getQuestion());
            int child_pos = Integer.parseInt(object.getAnswerid());

            for (int i = 0; i < Constants.radio_option_list.get(Constants.radio_question_list.get(group_pos)).size(); i++) {
                if (child_pos == i) {
                    Constants.radio_option_list.get(Constants.radio_question_list.get(group_pos)).get(i).setRadiostatus("true");
                } else {
                    Constants.radio_option_list.get(Constants.radio_question_list.get(group_pos)).get(i).setRadiostatus("false");
                }
            }

            notifyDataSetChanged();

        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._radio_option_list.get(this._radio_question_list.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._radio_question_list.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._radio_question_list.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);

        LinearLayout line = convertView.findViewById(R.id.line2);
        if (groupPosition % 2 == 0) {
            line.setBackgroundColor(Color.parseColor("#f5f5f5"));
        } else {
            line.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if (isExpanded) {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrowe, 0);
        } else {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrowd, 0);
        }

        txtListChild.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

