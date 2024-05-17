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

public class Sub_4 extends Fragment {

    public EditText sub4_1, sub4_2, sub4_3, sub4_4;
    String sub4_1_v, sub4_2_v, sub4_3_v, sub4_4_v;
    ItemViewModel viewModel;

    public Sub_4() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_4, container, false);
        sub4_1 = view.findViewById(R.id.sub4_1);
        sub4_2 = view.findViewById(R.id.sub4_2);
        sub4_3 = view.findViewById(R.id.sub4_3);
        sub4_4 = view.findViewById(R.id.sub4_4);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        Button btn = view.findViewById(R.id.button11);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub4_1_v = sub4_1.getText().toString().trim();
                sub4_2_v = sub4_2.getText().toString().trim();
                sub4_3_v = sub4_3.getText().toString().trim();
                sub4_4_v = sub4_4.getText().toString().trim();

                if(sub4_1_v.equals("") || sub4_2_v.equals("") || sub4_3_v.equals("") || sub4_4_v.equals("")){
                    Toast.makeText(getActivity(), "Enter every subject fields", Toast.LENGTH_SHORT).show();
                }else {
                    String all = sub4_1_v+","+sub4_2_v+","+sub4_3_v+","+sub4_4_v+",";
                    viewModel.setData(all);
                }
            }
        });
    }
}