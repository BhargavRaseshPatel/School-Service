package com.example.new_scs.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Data.ChildrenDataModel;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChildrenDataAdapter extends RecyclerView.Adapter<ChildrenDataAdapter.ViewHolder> {
    Context context;
    ArrayList<ChildrenDataModel> arrayList;
    Boolean aBoolean;

    public ChildrenDataAdapter(Context context, ArrayList<ChildrenDataModel> arrayList,Boolean bool) {
        this.context = context;
        this.arrayList = arrayList;
        aBoolean = bool;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_children_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChildrenDataModel model = arrayList.get(position);
        holder.name.setText(model.getName());
        holder.dateofbirth.setText(model.getDateofbirth());
        holder.mobile.setText(model.getMobile_no());

        if(!aBoolean){
            holder.linearLayout.setEnabled(false);
        }
        else {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(context, model.getKey(), Toast.LENGTH_SHORT).show();
                    final DialogPlus dialogPlus = DialogPlus.newDialog(context).
                            setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_children_detail))
                            .setExpanded(true, 950).create();
                    View view = dialogPlus.getHolderView();

                    EditText name = view.findViewById(R.id.editText_name);
                    EditText dateofbirth = view.findViewById(R.id.editText_dateofbirth);
                    EditText mobile_no = view.findViewById(R.id.editText_mobile_no);
                    Button btnUpdate = view.findViewById(R.id.button23);
                    Button btnRemove = view.findViewById(R.id.button24);

                    name.setText(model.getName());
                    dateofbirth.setText(model.getDateofbirth());
                    mobile_no.setText(model.getMobile_no());

                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arrayList.remove(model);
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", name.getText().toString());
                            map.put("dateofbirth", dateofbirth.getText().toString());
                            map.put("mobile_no", mobile_no.getText().toString());

                            FirebaseFirestore.getInstance().collection("Student").document(model.getKey()).
                                    update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
                            });
                        }
                    });

                    btnRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Delete Student Data").
                                    setMessage("Are you sure to remove student data?");
                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    arrayList.remove(model);
                                    FirebaseFirestore.getInstance().collection("Student").document(model.getKey()).delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    dialogPlus.dismiss();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder.show();
                        }
                    });
                    dialogPlus.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, dateofbirth, mobile;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView77);
            dateofbirth = itemView.findViewById(R.id.textView79);
            mobile = itemView.findViewById(R.id.textView78);
            linearLayout = itemView.findViewById(R.id.ll_children_detail);
        }
    }
}
