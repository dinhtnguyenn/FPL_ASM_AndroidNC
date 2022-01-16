package com.dinhnt.asm_androidnc.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.dinhnt.asm_androidnc.hepler.QuanLyLichHocSQLite;
import com.dinhnt.asm_androidnc.models.KhoaHoc;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class GetAllCourseServices extends IntentService {

    public GetAllCourseServices() {
        super("GetAllCourseServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            QuanLyLichHocSQLite quanLyLichHocSQLite = new QuanLyLichHocSQLite(getApplicationContext());

            Intent i = new Intent("GetAllCourseServices");
            final String action = intent.getAction();
            String masv = intent.getStringExtra("masv");
            boolean isMine = intent.getBooleanExtra("isMine", false);

            if (!isMine) {
                ArrayList<KhoaHoc> allCourse = quanLyLichHocSQLite.getAllCourse();
                i.putExtra("allCourse", allCourse);
            }

            ArrayList<KhoaHoc> allCourseRegister = quanLyLichHocSQLite.getAllCourseRegister(masv);
            i.putExtra("allCourseRegister", allCourseRegister);

            quanLyLichHocSQLite.close();

            i.putExtra("action", action);
            i.putExtra("resultCode", Activity.RESULT_OK);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
    }
}
