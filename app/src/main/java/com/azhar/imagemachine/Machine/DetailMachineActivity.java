package com.azhar.imagemachine.Machine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.azhar.imagemachine.adapters.ListImageAdapter;
import com.azhar.imagemachine.databinding.ActivityDetailMachineBinding;
import com.azhar.imagemachine.db.DbClient;
import com.azhar.imagemachine.models.ImageModel;

import java.util.List;

public class DetailMachineActivity extends AppCompatActivity {


    private ActivityDetailMachineBinding binding;



    String mMachineId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailMachineBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Machine");


        Integer mId = getIntent().getIntExtra("MC_ID", 0);
        mMachineId = mId.toString();
        String mMachineName = getIntent().getStringExtra("MC_NAME");
        String mMachinetype = getIntent().getStringExtra("MC_TYPE");
        String mMachineMaintenacneDate = getIntent().getStringExtra("MC_MAINTENANCE_DATE");
        String mQrCode = getIntent().getStringExtra("MC_QR_CODE");

        binding.tvMachineId.setText(mMachineId);
        binding.tvMachineName.setText(mMachineName);
        binding.tvMachineType.setText(mMachinetype);
        binding.tvMachineQrCode.setText(mQrCode);

        if (mMachineMaintenacneDate != null) {

            String[] mDateSparated = mMachineMaintenacneDate.split("-");
            String mYear = mDateSparated[0];
            Integer mmYear = Integer.parseInt(mYear);
            String mMonth = mDateSparated[1];
            Integer mmMonth = Integer.parseInt(mMonth);
            String mDay = mDateSparated[2];
            Integer mmDay = Integer.parseInt(mDay);



        binding.dtPickerMaintenance.updateDate(mmYear,mmMonth,mmDay);

        }



        binding.fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });



        getMachineImage();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == 1 && resultCode == RESULT_OK && null != data){

            if (data.getClipData() != null) {

                int count = data.getClipData().getItemCount();

                if(count > 10){
                    Toast.makeText(DetailMachineActivity.this, "cannot select image more than 10", Toast.LENGTH_SHORT).show();
                    return;
                }


                for (int i = 0; i < count; i++){
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();


                    saveImageUri(mMachineId, imageUri.toString());


                }


                getMachineImage();


            }
        }else if (requestCode == 500){
            getMachineImage();
        }
    }

    private void saveImageUri(String machineId, String photoPath){

        try {
            class SaveImage extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {
                    ImageModel imageModel = new ImageModel();
                    imageModel.setMachineId(machineId);
                    imageModel.setPhoto_path(photoPath);




                    DbClient.getInstance(getApplicationContext()).getAppDatabase()
                            .machineDao()
                            .insertImage(imageModel);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                }
            }

            SaveImage mSaveImage = new SaveImage();
            mSaveImage.execute();

        }catch (Exception e){
            Toast.makeText(DetailMachineActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getMachineImage(){

        try{



            class getMachinesImage extends AsyncTask<Void, Void, List<ImageModel>> {

                @Override
                protected List<ImageModel> doInBackground(Void... voids) {
                    List<ImageModel> mcImageList = null;


                        mcImageList = DbClient
                                .getInstance(getApplicationContext())
                                .getAppDatabase()
                                .machineDao()
                                .getAllMachineImage(mMachineId);

                    return mcImageList;
                }

                @Override
                protected void onPostExecute(List<ImageModel> imgModel) {
                    super.onPostExecute(imgModel);
                    binding.rvImageList.setHasFixedSize(true);
                    binding.rvImageList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

                    ListImageAdapter adapter = new ListImageAdapter(DetailMachineActivity.this, imgModel);
                    binding.rvImageList.setAdapter(adapter);


                }
            }

            getMachinesImage getMcImg = new getMachinesImage();
            getMcImg.execute();

        }catch (Exception e){
            Toast.makeText(DetailMachineActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
          getMachineImage();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}