package com.iuce.had;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class PhonesAdapter extends ArrayAdapter<PhoneNumbers> {

    Context mContext;
    
    int mLayoutResourceId;

    public PhonesAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final PhoneNumbers currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final CheckBox checkBox = (CheckBox) row.findViewById(R.id.check_phone);
        checkBox.setText(currentItem.PhoneNumber);
        checkBox.setChecked(false);
        checkBox.setEnabled(true);

        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                if (checkBox.isChecked()) {
//                    checkBox.setEnabled(false);
//                    if (mContext instanceof EmergencyActivity) {
//                        EmergencyActivity activity = (EmergencyActivity) mContext;
//                      //activity.checkItem(currentItem);
//                    }
//                }
            	
            	Toast.makeText(getContext(), currentItem.PhoneNumber, Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }

}