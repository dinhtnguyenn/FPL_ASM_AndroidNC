package com.dinhnt.asm_androidnc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dinhnt.asm_androidnc.R;
import com.dinhnt.asm_androidnc.hepler.QuanLyLichHocSQLite;
import com.dinhnt.asm_androidnc.models.KhoaHoc;
import com.dinhnt.asm_androidnc.services.unRegisterAndRegisterCourseServices;

import java.util.ArrayList;

public class GetAllCourseAdapter extends BaseAdapter {

    ArrayList<KhoaHoc> alKhoaHoc = new ArrayList<>();
    Context context;
    QuanLyLichHocSQLite quanLyLichHoc;

    public GetAllCourseAdapter(ArrayList<KhoaHoc> alKhoaHoc, Context context, QuanLyLichHocSQLite quanLyLichHoc) {
        this.alKhoaHoc = alKhoaHoc;
        this.context = context;
        this.quanLyLichHoc = quanLyLichHoc;
    }

    @Override
    public int getCount() {
        return alKhoaHoc.size();
    }

    @Override
    public Object getItem(int i) {
        return alKhoaHoc.get(i);
    }

    @Override
    public long getItemId(int i) {
        return alKhoaHoc.get(i).getId();
    }

    class ViewOfItem {
        TextView txtTenKH, txtNoiDungKH;
        Button btnKH;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewOfItem viewOfItem;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();


        if (view == null) {
            view = inflater.inflate(R.layout.item_course, null);
            viewOfItem = new ViewOfItem();
            viewOfItem.txtTenKH = view.findViewById(R.id.txtTenKH);
            viewOfItem.txtNoiDungKH = view.findViewById(R.id.txtNoiDungKH);
            viewOfItem.btnKH = view.findViewById(R.id.btnKH);

            view.setTag(viewOfItem);
        } else {
            viewOfItem = (ViewOfItem) view.getTag();
        }

        viewOfItem.txtTenKH.setText(alKhoaHoc.get(i).getTenkh());
        String noiDung = alKhoaHoc.get(i).getLichhoc() + " | Thi ngày " + alKhoaHoc.get(i).getLichthi();
        viewOfItem.txtNoiDungKH.setText(noiDung);

        if(alKhoaHoc.get(i).isCheck()){
            viewOfItem.btnKH.setText("Hủy đăng ký khóa học");
            viewOfItem.btnKH.setBackgroundColor(Color.parseColor("#D65418"));
        }else{
            viewOfItem.btnKH.setText("Đăng ký khóa học");
            viewOfItem.btnKH.setBackgroundColor(Color.parseColor("#1884D6"));
        }


        //đăng ký/hủy khóa học
        viewOfItem.btnKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, unRegisterAndRegisterCourseServices.class);
                //thay đổi mssv phù hợp với chức năng đăng nhập/đăng ký
                intent.putExtra("masv", "ps00709");
                intent.putExtra("makh", alKhoaHoc.get(i).getId());

                //đăng ký khóa học - true | hủy đăng ký khóa học - false
                intent.putExtra("isRegister", !alKhoaHoc.get(i).isCheck());

                intent.setAction("unRegisterAndRegisterCourseServices");
                context.startService(intent);
            }
        });
        return view;
    }
}
