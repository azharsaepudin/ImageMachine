package com.azhar.imagemachine.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azhar.imagemachine.Machine.DetailMachineActivity;
import com.azhar.imagemachine.Machine.ListMachineActivity;
import com.azhar.imagemachine.databinding.ListMachineBinding;
import com.azhar.imagemachine.models.Machine;

import java.util.List;

public class ListMachineAdapter extends RecyclerView.Adapter<ListMachineAdapter.ListMachineViewHolder> {

    Activity activity;
    List<Machine> machineData;
  

    public ListMachineAdapter(Activity activity, List<Machine> machineData){
        this.activity = activity;
        this.machineData = machineData;

    }

    @NonNull
    @Override
    public ListMachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListMachineViewHolder(ListMachineBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListMachineViewHolder holder, int position) {
        holder.bind(machineData.get(position));
    }

    @Override
    public int getItemCount() {
        return machineData != null ? machineData.size() : 0;
    }

    public class ListMachineViewHolder extends RecyclerView.ViewHolder {

        ListMachineBinding listMachineBinding;

        public ListMachineViewHolder(ListMachineBinding listMachineBinding) {
            super(listMachineBinding.getRoot());
            this.listMachineBinding = listMachineBinding;
        }

        public void bind(Machine machine) {
            listMachineBinding.tvMachineName.setText(machine.getMachineName());
            listMachineBinding.tvMachineType.setText(machine.getMachineType());

            listMachineBinding.viewCardMachine.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    int mcId = machine.getMachineId();
                    String mMachineName = machine.getMachineName();
                    String mMachineType = machine.getMachineType();

                    if (activity instanceof ListMachineActivity){
                        String mTitle = "Edit or Delete";
                        ((ListMachineActivity)activity).loadPopupAddMachine(mTitle, mcId, mMachineName, mMachineType);
                    }
                    return false;
                }
            });

            listMachineBinding.viewCardMachine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(view.getContext(), DetailMachineActivity.class);
                    i.putExtra("MC_ID", machine.getMachineId());
                    i.putExtra("MC_NAME", machine.getMachineName());
                    i.putExtra("MC_TYPE", machine.getMachineType());
                    i.putExtra("MC_MAINTENANCE_DATE", machine.getMaintenanceDate());
                    i.putExtra("MC_QR_CODE", machine.getQrCode());
                    view.getContext().startActivity(i);
                }
            });

        }
    }
}
