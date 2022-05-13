package com.azhar.imagemachine.Machine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.azhar.imagemachine.R;
import com.azhar.imagemachine.adapters.ListMachineAdapter;
import com.azhar.imagemachine.databinding.ActivityListMachineBinding;
import com.azhar.imagemachine.databinding.AddMachineLayoutBinding;
import com.azhar.imagemachine.db.DbClient;
import com.azhar.imagemachine.models.Machine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListMachineActivity extends AppCompatActivity {

    private ActivityListMachineBinding binding;
    AddMachineLayoutBinding bindingDialogPopup;
    String currentDateandTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListMachineBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.rvListMachine.setHasFixedSize(true);
        binding.rvListMachine.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getMachineData("machine_name");




        binding.addMachineData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mTitle = "Add New Machine";
                loadPopupAddMachine(mTitle, null, "", "");
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()){
            case R.id.shortMachineName:
                getMachineData("machine_name");
                return true;

            case R.id.shortMachineType:
                getMachineData("machine_type");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadPopupAddMachine(String mTitle, Integer mcId, String mMachineName, String mMachineType){


        bindingDialogPopup = AddMachineLayoutBinding
                .inflate(LayoutInflater.from(ListMachineActivity.this));
        AlertDialog mAlertbuilder = new AlertDialog.Builder(ListMachineActivity.this).create();
        mAlertbuilder.setView(bindingDialogPopup.getRoot());

        bindingDialogPopup.tvTitleDialog.setText(mTitle);

        if (mTitle.equals("Edit or Delete")){
            bindingDialogPopup.btnUpdate.setVisibility(View.VISIBLE);
            bindingDialogPopup.btnDelete.setVisibility(View.VISIBLE);
            bindingDialogPopup.btnAddMachineData.setVisibility(View.GONE);

            bindingDialogPopup.edtMachineNameInput.setText(mMachineName);
            bindingDialogPopup.edtMachineTypeInput.setText(mMachineType);


        }else{
            bindingDialogPopup.btnUpdate.setVisibility(View.GONE);
            bindingDialogPopup.btnDelete.setVisibility(View.GONE);
            bindingDialogPopup.btnAddMachineData.setVisibility(View.VISIBLE);
        }

        bindingDialogPopup.btnAddMachineData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mMachineName = bindingDialogPopup.edtMachineNameInput.getText().toString();
                String mMachineType = bindingDialogPopup.edtMachineTypeInput.getText().toString();

                boolean valid = validationInput(mMachineName, mMachineType);

                if (valid){
                    getDateSystem();

                    saveMachineData(mMachineName, mMachineType);

                    mAlertbuilder.dismiss();
                }

            }
        });

        bindingDialogPopup.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mMachineName = bindingDialogPopup.edtMachineNameInput.getText().toString();
                String mMachineType = bindingDialogPopup.edtMachineTypeInput.getText().toString();

                boolean valid = validationInput(mMachineName, mMachineType);

                if (valid){
                    getDateSystem();

                    updateMachineData(mcId, mMachineName, mMachineType);

                    mAlertbuilder.dismiss();
                }
            }
        });

        bindingDialogPopup.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMachineData(mcId);
                mAlertbuilder.dismiss();
            }
        });

        mAlertbuilder.show();


    }

    private void saveMachineData(String mMachineName, String mMachineType){

        try {

            class SaveMachine extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {
                    Machine machine = new Machine();
                    machine.setMachineName(mMachineName);
                    machine.setMachineType(mMachineType);
                    machine.setMaintenanceDate(currentDateandTime);

                    DbClient.getInstance(getApplicationContext()).getAppDatabase()
                            .machineDao()
                            .insert(machine);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    getMachineData("machine_name");
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                }
            }

            SaveMachine mcSave = new SaveMachine();
            mcSave.execute();

        }catch (Exception e){
            Toast.makeText(ListMachineActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void getMachineData(String shortType){

        try{

            class getMachines extends AsyncTask<Void, Void, List<Machine>> {

                @Override
                protected List<Machine> doInBackground(Void... voids) {
                    List<Machine> mcList = null;

                    if (shortType.equals("machine_name")){
                       mcList = DbClient
                                .getInstance(getApplicationContext())
                                .getAppDatabase()
                                .machineDao()
                                .getAllMachineByName();
                        
                    }else{
                        mcList = DbClient
                                .getInstance(getApplicationContext())
                                .getAppDatabase()
                                .machineDao()
                                .getAllMachineByType();
                    }


                    return mcList;
                }

                @Override
                protected void onPostExecute(List<Machine> tasks) {
                    super.onPostExecute(tasks);
                    ListMachineAdapter adapter = new ListMachineAdapter(ListMachineActivity.this, tasks);
                    binding.rvListMachine.setAdapter(adapter);
                }
            }

            getMachines getMc = new getMachines();
            getMc.execute();

        }catch (Exception e){
            Toast.makeText(ListMachineActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void updateMachineData(Integer mcId, String mMachineName, String mMachineType){
        try {

            class UpdateMachine extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {
                    Machine machine = new Machine();
                    machine.setMachineId(mcId);
                    machine.setMachineName(mMachineName);
                    machine.setMachineType(mMachineType);

                    DbClient.getInstance(getApplicationContext()).getAppDatabase()
                            .machineDao()
                            .update(machine);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    getMachineData("machine_name");
                    Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_LONG).show();
                }
            }

            UpdateMachine mcUpdate = new UpdateMachine();
            mcUpdate.execute();

        }catch (Exception e){
            Toast.makeText(ListMachineActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteMachineData(Integer mcId){
        try {

            class DeleteMachine extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {
                    Machine machine = new Machine();
                    machine.setMachineId(mcId);

                    DbClient.getInstance(getApplicationContext()).getAppDatabase()
                            .machineDao()
                            .delete(machine);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    getMachineData("machine_name");
                    Toast.makeText(getApplicationContext(), "Delete Success", Toast.LENGTH_LONG).show();
                }
            }

            DeleteMachine mcDelete = new DeleteMachine();
            mcDelete.execute();

        }catch (Exception e){
            Toast.makeText(ListMachineActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validationInput(String mMachineName, String mMachineType){

        if (mMachineName.equals("")){
            Toast.makeText(ListMachineActivity.this, "Fill Machine Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mMachineType.equals("")){
            Toast.makeText(ListMachineActivity.this, "Fill Machine Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getDateSystem() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());
    }
}