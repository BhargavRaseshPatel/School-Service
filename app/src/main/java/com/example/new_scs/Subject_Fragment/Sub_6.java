package com.example.new_scs.Subject_Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.new_scs.Data.ItemViewModel;
import com.example.new_scs.R;

import org.jetbrains.annotations.NotNull;

public class Sub_6 extends Fragment {

    public Sub_6() {
        // Required empty public constructor
    }
    EditText sub6_1,sub6_2,sub6_3,sub6_4,sub6_5,sub6_6;
    String sub6_1_v,sub6_2_v,sub6_3_v,sub6_4_v,sub6_5_v,sub6_6_v;
    ItemViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_6, container, false);
        sub6_1 = view.findViewById(R.id.sub6_1);
        sub6_2 = view.findViewById(R.id.sub6_2);
        sub6_3 = view.findViewById(R.id.sub6_3);
        sub6_4 = view.findViewById(R.id.sub6_4);
        sub6_5 = view.findViewById(R.id.sub6_5);
        sub6_6 = view.findViewById(R.id.sub6_6);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        Button btn = view.findViewById(R.id.button12);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub6_1_v = sub6_1.getText().toString();
                sub6_2_v = sub6_2.getText().toString();
                sub6_3_v = sub6_3.getText().toString();
                sub6_4_v = sub6_4.getText().toString();
                sub6_5_v = sub6_5.getText().toString();
                sub6_6_v = sub6_6.getText().toString();

                if(sub6_1_v.equals("") || sub6_2_v.equals("") || sub6_3_v.equals("") || sub6_4_v.equals("") || sub6_5_v.equals("") || sub6_6_v.equals("")){
                    Toast.makeText(getActivity(), "Enter every subject fields", Toast.LENGTH_SHORT).show();
                }else {
                    String all = sub6_1_v + "," + sub6_2_v + "," + sub6_3_v + "," + sub6_4_v + "," + sub6_5_v + "," + sub6_6_v + ",";
                    viewModel.setData(all);
                }
            }
        });
    }
}