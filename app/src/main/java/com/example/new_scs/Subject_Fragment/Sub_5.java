package com.example.new_scs.Subject_Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.new_scs.Data.ItemViewModel;
import com.example.new_scs.R;

import org.jetbrains.annotations.NotNull;

public class Sub_5 extends Fragment {

    EditText sub5_1, sub5_2, sub5_3, sub5_4, sub5_5;
    String sub5_1_v, sub5_2_v, sub5_3_v, sub5_4_v, sub5_5_v;
    ItemViewModel viewModel;

    public Sub_5() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_5, container, false);
        sub5_1 = view.findViewById(R.id.sub5_1);
        sub5_2 = view.findViewById(R.id.sub5_2);
        sub5_3 = view.findViewById(R.id.sub5_3);
        sub5_4 = view.findViewById(R.id.sub5_4);
        sub5_5 = view.findViewById(R.id.sub5_5);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        Button btn = view.findViewById(R.id.button10);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub5_1_v = sub5_1.getText().toString();
                sub5_2_v = sub5_2.getText().toString();
                sub5_3_v = sub5_3.getText().toString();
                sub5_4_v = sub5_4.getText().toString();
                sub5_5_v = sub5_5.getText().toString();

                if (sub5_1_v.equals("") || sub5_2_v.equals("") || sub5_3_v.equals("") || sub5_4_v.equals("") || sub5_5_v.equals("")) {
                    Toast.makeText(getActivity(), "Enter every subject fields", Toast.LENGTH_SHORT).show();
                } else {
                    String all = sub5_1_v + "," + sub5_2_v + "," + sub5_3_v + "," + sub5_4_v + "," + sub5_5_v + ",";
                    viewModel.setData(all);
                }
            }
        });
    }
}