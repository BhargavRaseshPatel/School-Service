package com.example.new_scs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.new_scs.R;

import java.util.ArrayList;

public class ListAdapterSection extends ArrayAdapter<String> {

    public ListAdapterSection(Context context, ArrayList<String> arrayList) {
        super(context, R.layout.sample_principal_class_section, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sample_principal_class_section,parent, false);
        }
        TextView section = convertView.findViewById(R.id.textView22);
        section.setText(getItem(position));
        return super.getView(position, convertView, parent);

    }
}
