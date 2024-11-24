package com.example.studentmanagesystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentListActivity extends Activity {
    private Button btnOpenStudent;
    private ListView lstStudent;
    private SQLiteDatabase db;
    private ArrayList<Student> studentList = new ArrayList<>();
    private MyAdapterStudent adapter;
    private int posSelected = -1; //Giu vi tri tren listview

    //Khai bao cac bien nhan ket qua
    public static final int OPEN_STUDENT = 113;
    public static final int EDIT_STUDENT = 114;
    public static final int SAVE_STUDENT = 115;

    private void getStudentList(){
        try {
            studentList.add(new Student("Mã lớp: ", "Tên Lớp", "Mã Sinh viên",
                    "MSSV", "Tên Sinh viên: ", "Ngay sinh: ",
                    "Giới tính: ", "Địa chỉ", "Số điện thoại"));

            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.rawQuery("select tblclass.id_class, tblclass.name_class,"+
                    " tblstudent.id_student, tblstudent.code_student, tblstudent.name_student," +
                    " tblstudent.birthday, tblstudent.gender_student, tblstudent.address_student," +
                    " tblstudent.phone_student from tblclass, tblstudent" +
                    " where tblclass.id_class = tblstudent.id_class", null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                studentList.add(new Student(
                        c.getString(0).toString(), c.getString(1).toString(),
                        c.getString(2).toString(), c.getString(3).toString(),
                        c.getString(4).toString(), c.getString(5).toString(),
                        c.getString(6).toString(), c.getString(7).toString(),
                        c.getString(8).toString()
                ));
                c.moveToNext();
            };

            adapter = new MyAdapterStudent(this, android.R.layout.simple_list_item_1, studentList);
            lstStudent.setAdapter(adapter);

        }catch (Exception exception){
            Toast.makeText(getApplication(), "Lỗi"+exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Xac nhan xoa
    public void confirmDelete(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Xác nhận để xóa sinh viên..!!!");
        alertDialogBuilder.setIcon(R.drawable.question);

        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xóa sinh viên?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
                String id_student = studentList.get(posSelected).getId_student();
                if(db.delete("tblstudent", "id_student = ?", new String[]{id_student}) != -1) {
                    studentList.remove(posSelected);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplication(), "Xoa sinh vien thanh cong", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplication(), "Xóa Sinh vien thất bại !!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Khong dong y", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.mnustudent, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        posSelected = info.position;
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (info == null) return super.onContextItemSelected(item);

        posSelected = info.position;
        int id = item.getItemId();

        if (item.getItemId() == R.id.mnueditstudent) {
            Toast.makeText(getApplication(),"Edit student informations ...",Toast.LENGTH_LONG).show();
            Student student = studentList.get(posSelected);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(StudentListActivity.this, EditStudentActivity.class);
            bundle.putSerializable("student", student);
            intent.putExtras(bundle);
            startActivityForResult(intent, EDIT_STUDENT);
            return true;
        } else if (item.getItemId() == R.id.mnudeletestudent) {
            confirmDelete();
            return true;
        } else if (item.getItemId() == R.id.mnuclosestudent) {
            Notify.exit(this);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SAVE_STUDENT){
            Bundle bundle = data.getBundleExtra("data");
            Student student = (Student) bundle.getSerializable("student");
            if (requestCode == OPEN_STUDENT){
                studentList.add(student);
            } else if (requestCode == EDIT_STUDENT){
                studentList.set(posSelected,student);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        btnOpenStudent = findViewById(R.id.btnOpenStudent);
        lstStudent = findViewById(R.id.lstStudent);

        getStudentList();

        registerForContextMenu(lstStudent);

        //Initialize database
        db = openOrCreateDatabase(Login.DATABASE_NAME,MODE_PRIVATE, null);
        getStudentList();

        btnOpenStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị thông báo
                Toast.makeText(StudentListActivity.this, "Đang đến Thêm Sinh vien", Toast.LENGTH_SHORT).show();

                // Khởi động activity thêm sv
                Intent intent = new Intent(StudentListActivity.this, InsertStudentActivity.class);
                startActivity(intent);
            }
        });


        lstStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                posSelected = position;
                confirmDelete();
                return false;
            }
        });
    }

//    private  void populateStudentList(){
//        studentList.clear();
//        studentList.add(new Student("Mã lớp: ", "Tên Lớp", "Mã Sinh viên",
//                "MSSV", "Tên Sinh viên: ", "Ngay sinh: ",
//                "Giới tính: ", "Địa chỉ", "Số điện thoại"));
//        try (Cursor c = db.query("tblstudent", null, null, null,
//                null, null, null)) {
//            if (c.moveToFirst()) {
//                do {
//                    studentList.add(new Student(c.getString(0), c.getString(1), c.getString(2),
//                            c.getString(3),  c.getString(4),c.getString(5),  c.getString(6),
//                            c.getString(7),c.getString(8)));
//                } while (c.moveToNext());
//            }
//        } catch (Exception ex) {
//            Toast.makeText(getApplicationContext(), "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show();
//        }
//
//        adapter = new MyAdapterStudent(this, android.R.layout.simple_list_item_1, studentList);
//        lstStudent.setAdapter(adapter);
//    }

}
