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

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class unRegisterAndRegisterCourseServices extends IntentService {

    public unRegisterAndRegisterCourseServices() {
        super("unRegisterAndRegisterCourseServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            QuanLyLichHocSQLite quanLyLichHocSQLite = new QuanLyLichHocSQLite(getApplicationContext());

            Intent i = new Intent("unRegisterAndRegisterCourseServices");
            final String action = intent.getAction();
            String masv = intent.getStringExtra("masv");
            int makh = intent.getIntExtra("makh", -1);
            boolean isRegister = intent.getBooleanExtra("isRegister", false);

            if(isRegister){
                quanLyLichHocSQLite.registerCourse(masv, makh); //đăng ký
            }else {
                quanLyLichHocSQLite.unRegisterCourse(masv, makh); //hủy đăng ký
            }

            ArrayList<KhoaHoc> allCourse = quanLyLichHocSQLite.getAllCourse();
            ArrayList<KhoaHoc> allCourseRegister = quanLyLichHocSQLite.getAllCourseRegister(masv);
            quanLyLichHocSQLite.close();
            i.putExtra("allCourse", allCourse);
            i.putExtra("allCourseRegister", allCourseRegister);
            i.putExtra("action",action);
            i.putExtra("resultCode", Activity.RESULT_OK);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
    }
}
