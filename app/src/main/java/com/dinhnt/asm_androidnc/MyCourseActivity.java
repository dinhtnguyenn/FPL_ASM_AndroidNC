package com.dinhnt.asm_androidnc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dinhnt.asm_androidnc.adapter.GetAllCourseAdapter;
import com.dinhnt.asm_androidnc.hepler.QuanLyLichHocSQLite;
import com.dinhnt.asm_androidnc.models.KhoaHoc;
import com.dinhnt.asm_androidnc.services.GetAllCourseServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MyCourseActivity extends AppCompatActivity {
    ArrayList<KhoaHoc> alKhoaHoc = new ArrayList<>();
    QuanLyLichHocSQLite quanLyLichHocSQLite;
    ListView listViewAllCourseRegister;
    Button btnCapture;
    File imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);

        listViewAllCourseRegister = findViewById(R.id.listViewAllCourseRegister);
        btnCapture = findViewById(R.id.btnCapture);

        quanLyLichHocSQLite = new QuanLyLichHocSQLite(this);

        IntentFilter filterGetAllCourse = new IntentFilter("GetAllCourseServices");
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllCourseRegisterReceiver, filterGetAllCourse);

        IntentFilter filterRegisterCourse = new IntentFilter("unRegisterAndRegisterCourseServices");
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllCourseRegisterReceiver, filterRegisterCourse);

        Intent intent = new Intent(this, GetAllCourseServices.class);
        //thay ?????i mssv ph?? h???p v???i ch???c n??ng ????ng nh???p/????ng k??
        intent.putExtra("masv", "ps00709");
        intent.putExtra("isMine", true);
        intent.setAction("GetAllCourseServices");
        startService(intent);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDetailCourse();
            }
        });
    }

    private final BroadcastReceiver getAllCourseRegisterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                String action = intent.getStringExtra("action");
                switch (action) {
                    case "GetAllCourseServices":
                    case "unRegisterAndRegisterCourseServices":
                        alKhoaHoc.clear();
                        ArrayList<KhoaHoc> alCourseRegister = (ArrayList<KhoaHoc>) intent.getSerializableExtra("allCourseRegister");

                        for (KhoaHoc khdk : alCourseRegister) {
                            khdk.setCheck(true);
                            alKhoaHoc.add(khdk);
                        }

                        GetAllCourseAdapter adapter = new GetAllCourseAdapter(alKhoaHoc, MyCourseActivity.this, quanLyLichHocSQLite);
                        listViewAllCourseRegister.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getAllCourseRegisterReceiver);
    }

    private void ShareDetailCourse() {
        String content = "DinhNT ???? chia s??? nh???ng kh??a h???c ???? ????ng k??\n\n";
        for (KhoaHoc khdk : alKhoaHoc) {
            content = content.concat(khdk.getTenkh())
                    .concat("\nL???ch h???c: ")
                    .concat(khdk.getLichhoc())
                    .concat("\nL???ch thi: ")
                    .concat(khdk.getLichthi())
                    .concat("\n-----------\n");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_SUBJECT, "DinhNT ???? chia s???");
        intent.setType("text/plain");

        startActivity(Intent.createChooser(intent, "Chia s??? n???i dung th??ng qua"));
    }
}