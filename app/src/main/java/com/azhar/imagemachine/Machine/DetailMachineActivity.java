package com.azhar.imagemachine.Machine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.azhar.imagemachine.R;
import com.azhar.imagemachine.databinding.ActivityDetailMachineBinding;

public class DetailMachineActivity extends AppCompatActivity {


    private ActivityDetailMachineBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailMachineBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Integer mId = getIntent().getIntExtra("MC_ID", 0);
        String mMachineId = mId.toString();
        String mMachineName = getIntent().getStringExtra("MC_NAME");
        String mMachinetype = getIntent().getStringExtra("MC_TYPE");
        String mMachineMaintenacneDate = getIntent().getStringExtra("MC_MAINTENANCE_DATE");
        String mQrCode = getIntent().getStringExtra("MC_QR_CODE");

        binding.tvMachineId.setText(mMachineId);
        binding.tvMachineName.setText(mMachineName);
        binding.tvMachineType.setText(mMachinetype);
        binding.tvMachineQrCode.setText(mQrCode);

        String[] mDateSparated = mMachineMaintenacneDate.split("-");
        String mYear = mDateSparated[0];
        Integer mmYear = Integer.parseInt(mYear);
        String mMonth = mDateSparated[1];
        Integer mmMonth = Integer.parseInt(mMonth);
        String mDay = mDateSparated[2];
        Integer mmDay = Integer.parseInt(mDay);



        binding.dtPickerMaintenance.updateDate(mmYear,mmMonth,mmDay);


    }
}