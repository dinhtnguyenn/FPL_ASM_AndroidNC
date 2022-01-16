package com.dinhnt.asm_androidnc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dinhnt.asm_androidnc.adapter.GetAllCourseAdapter;
import com.dinhnt.asm_androidnc.hepler.QuanLyLichHocSQLite;
import com.dinhnt.asm_androidnc.models.KhoaHoc;
import com.dinhnt.asm_androidnc.services.GetAllCourseServices;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ListCourseActivity extends AppCompatActivity {
    ArrayList<KhoaHoc> alKhoaHoc = new ArrayList<>();
    QuanLyLichHocSQLite quanLyLichHocSQLite;
    ListView listViewAllCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course);

        listViewAllCourse = findViewById(R.id.listViewAllCourse);
        quanLyLichHocSQLite = new QuanLyLichHocSQLite(this);

        IntentFilter filterGetAllCourse = new IntentFilter("GetAllCourseServices");
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllCourseReceiver, filterGetAllCourse);

        IntentFilter filterRegisterCourse = new IntentFilter("unRegisterAndRegisterCourseServices");
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllCourseReceiver, filterRegisterCourse);

        Intent intent = new Intent(this, GetAllCourseServices.class);
        //thay đổi mssv phù hợp với chức năng đăng nhập/đăng ký
        intent.putExtra("masv", "ps00709");
        intent.putExtra("isMine", false);
        intent.setAction("GetAllCourseServices");
        startService(intent);
    }

    private final BroadcastReceiver getAllCourseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                String action = intent.getStringExtra("action");
                switch (action){
                    case "GetAllCourseServices":
                    case "unRegisterAndRegisterCourseServices":
                        alKhoaHoc.clear();
                        ArrayList<KhoaHoc> alCourse = (ArrayList<KhoaHoc>) intent.getSerializableExtra("allCourse");
                        ArrayList<KhoaHoc> alCourseRegister = (ArrayList<KhoaHoc>) intent.getSerializableExtra("allCourseRegister");
                        for(KhoaHoc kh : alCourse){
                            for(KhoaHoc khdk : alCourseRegister){
                                if(kh.getId() == khdk.getId()){
                                    kh.setCheck(true);
                                    break;
                                }
                            }
                            alKhoaHoc.add(kh);
                        }

                        GetAllCourseAdapter adapter = new GetAllCourseAdapter(alKhoaHoc, ListCourseActivity.this, quanLyLichHocSQLite);
                        listViewAllCourse.setAdapter(adapter);
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getAllCourseReceiver);
    }
}