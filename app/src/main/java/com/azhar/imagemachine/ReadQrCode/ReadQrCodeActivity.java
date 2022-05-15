package com.azhar.imagemachine.ReadQrCode;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.MenuItem;

import android.view.View;

import android.widget.Toast;

import com.azhar.imagemachine.Machine.DetailMachineActivity;

import com.azhar.imagemachine.databinding.ActivityReadQrCodeBinding;
import com.azhar.imagemachine.db.DbClient;

import com.azhar.imagemachine.models.Machine;
import com.budiyev.android.codescanner.CodeScanner;

import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.List;


public class ReadQrCodeActivity extends AppCompatActivity {


    private static final int REQUEST_CAMERA_PERMISSION = 201;


    private CodeScanner mCodeScanner;

    private ActivityReadQrCodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadQrCodeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("QrCode Scan");

        checkReqPermission();



        mCodeScanner = new CodeScanner(this, binding.scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        getMachineId(result.getText());
                    }
                });
            }
        });
        binding.scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkReqPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
               ) {

            ActivityCompat.requestPermissions(ReadQrCodeActivity.this,
                    new String[] { Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (permissions.length > 0 && grantResults.length > 0) {
                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }



                if (flag == false) {
                    Toast.makeText(getApplicationContext(), "Some Permission not allow", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some Permission not allow", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getMachineId(String qrCodeNumber){

        try{

            class GetMachineId extends AsyncTask<Void, Void, List<Machine>> {

                @Override
                protected List<Machine> doInBackground(Void... voids) {
                    List<Machine> mcList = null;


                    mcList = DbClient
                            .getInstance(getApplicationContext())
                            .getAppDatabase()
                            .machineDao()
                            .getIdMachine(qrCodeNumber);

                    return mcList;
                }

                @Override
                protected void onPostExecute(List<Machine> mcData) {
                    super.onPostExecute(mcData);


                    Integer mMachineId = mcData.get(0).machineId;
                    String mMachineName = mcData.get(0).machineName;
                    String mMachineType = mcData.get(0).machineType;
                    String mMintenanceDate = mcData.get(0).maintenanceDate;
                    String mQrcode = mcData.get(0).qrCode;

                    Intent i = new Intent(ReadQrCodeActivity.this, DetailMachineActivity.class);
                    i.putExtra("MC_ID",mMachineId);
                    i.putExtra("MC_NAME", mMachineName);
                    i.putExtra("MC_TYPE", mMachineType);
                    i.putExtra("MC_MAINTENANCE_DATE", mMintenanceDate);
                    i.putExtra("MC_QR_CODE", mQrcode);
                    startActivity(i);


                }
            }

            GetMachineId getMcId = new GetMachineId();
            getMcId.execute();


        }catch (Exception e){
            Toast.makeText(ReadQrCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}