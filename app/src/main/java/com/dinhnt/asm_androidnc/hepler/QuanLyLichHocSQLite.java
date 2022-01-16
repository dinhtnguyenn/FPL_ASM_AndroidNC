package com.dinhnt.asm_androidnc.hepler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dinhnt.asm_androidnc.models.KhoaHoc;

import java.util.ArrayList;

public class QuanLyLichHocSQLite extends SQLiteOpenHelper {

    SQLiteDatabase db = this.getReadableDatabase();

    private static final String DATABASE_NAME = "quanlylichhoc";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SINHVIEN = "sinhvien";
    private static final String KEY_MASV = "masv";
    private static final String KEY_HOTEN = "hoten";
    private static final String KEY_NGAYSINH = "ngaysinh";
    private static final String KEY_DIACHI = "diachi";
    private static final String KEY_SDT = "sdt";

    private static final String TABLE_KHOAHOC = "khoahoc";
    private static final String KEY_MAKH = "makh";
    private static final String KEY_TENKH = "tenkh";
    private static final String KEY_LICHHOC = "lichhoc";
    private static final String KEY_LICHTHI = "lichthi";

    private static final String TABLE_THONGTINDANGKY = "thongtindangky";
    private static final String KEY_ID = "id";


    public QuanLyLichHocSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tạo bảng sinh viên
        String sqlSinhVien = "create table " + TABLE_SINHVIEN +
                "(" +
                KEY_MASV + " text primary key, " +
                KEY_HOTEN + " text, " +
                KEY_NGAYSINH + " text," +
                KEY_DIACHI + " text," +
                KEY_SDT + " text" +
                ")";
        db.execSQL(sqlSinhVien);

        //tạo bảng khóa học
        String sqlKhoaHoc = "create table " + TABLE_KHOAHOC +
                "(" +
                KEY_MAKH + " integer primary key autoincrement, " +
                KEY_TENKH + " text, " +
                KEY_LICHHOC + " text," +
                KEY_LICHTHI + " text" +
                ")";
        db.execSQL(sqlKhoaHoc);

        //tạo bảng thông tin đăng ký môn học
        String sqlTTDangKy = "create table " + TABLE_THONGTINDANGKY +
                "(" +
                KEY_ID + " integer primary key autoincrement, " +
                KEY_MASV + " text, " +
                KEY_MAKH + " integer" +
                ")";
        db.execSQL(sqlTTDangKy);

        //data khóa học
        String phanLoai = "INSERT INTO " + TABLE_KHOAHOC + " (" + KEY_MAKH + ", " + KEY_TENKH + ", "
                + KEY_LICHHOC + ", " + KEY_LICHTHI + ") " +
                "Values ('1', 'Lập trình Android', 'Ca2 - 246', '22/02/2022')," +
                "('2', 'Lập trình Java 1', 'Ca4 - 357', '05/03/2022'), " +
                "('3', 'Xây dựng trang web', 'Ca4 - 246', '05/03/2022'), " +
                "('4', 'Cơ sở dữ liệu', 'Ca3 - 246', '10/04/2022'), " +
                "('5', 'Lập trình Java 2', 'Ca1 - 357', '21/03/2022'), " +
                "('6', 'Thiết kế giao diện trên Android', 'Ca6 - 357', '22/03/2022'), " +
                "('7', 'Công nghệ phần mềm', 'Ca2 - 357', '08/05/2022'), " +
                "('8', 'CTDL và GT', 'Ca5 - 357', '05/03/2022'), " +
                "('9', 'Lập trình Javascript', 'Ca4 - 246', '17/03/2022'), " +
                "('10', 'Nhập môn lập trình', 'Ca1 - 246', '01/03/2022')";
        db.execSQL(phanLoai);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_SINHVIEN);
        onCreate(db);

        db.execSQL("drop table if exists " + TABLE_KHOAHOC);
        onCreate(db);

        db.execSQL("drop table if exists " + TABLE_THONGTINDANGKY);
        onCreate(db);
    }

    //lấy danh sách khóa học
    public ArrayList<KhoaHoc> getAllCourse() {
        Cursor cursor = db.rawQuery("select * from " + TABLE_KHOAHOC, null);

        ArrayList<KhoaHoc> list = new ArrayList<>();
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                list.add(new KhoaHoc(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        return list;
    }

    //Đăng ký khóa học mới
    public void registerCourse(String masv, int makh) {
        ContentValues values = new ContentValues();
        values.put(KEY_MASV, masv);
        values.put(KEY_MAKH, makh);

        db.insert(TABLE_THONGTINDANGKY, null, values);
    }

    //Hủy khóa học đã đăng ký
    public void unRegisterCourse(String masv, int makh) {
        db.delete(TABLE_THONGTINDANGKY, "masv=? and makh=?", new String[]{masv, String.valueOf(makh)});
    }

    //xem danh sách khóa học & lịch thi đã đăng ký
    public ArrayList<KhoaHoc> getAllCourseRegister(String masv) {
        Cursor cursor = db.rawQuery("select * from " + TABLE_KHOAHOC + " kh," + TABLE_THONGTINDANGKY
                                + " tt where kh.makh = tt.makh and tt.masv like '" + masv + "'", null);

        ArrayList<KhoaHoc> list = new ArrayList<>();
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                list.add(new KhoaHoc(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        return list;
    }
}