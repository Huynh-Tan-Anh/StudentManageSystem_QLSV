package com.example.studentmanagesystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ClassList extends Activity {
    private ListView lstClass;
    private Button btnOpenClass, btnBack;
    private ArrayList<Room> classList = new ArrayList<>();
    private MyAdapterClass adapter;
    private SQLiteDatabase db;
    private int posSelected = -1;

    //Cac bien nhan ket qua tra ve tu ACTIVITY
    public static final int OPEN_CLASS = 113;
    public static final int EDIT_CLASS = 114;
    public static final int SAVE_CLASS = 115;

    //lay danh sach lop
    private void getClassList(){
        try {
            classList.add(new Room("Mã lớp", "Tên lớp", "Sĩ số"));
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.query("tblclass", null, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()){
                classList.add(new Room(c.getInt(0)+"", c.getString(1).toString(), c.getString(2).toString(), c.getInt(3)+""));
                c.moveToNext();
            }
            adapter = new MyAdapterClass(this, android.R.layout.simple_list_item_1, classList);
            lstClass.setAdapter(adapter);
        }
        catch (Exception ex){
            Toast.makeText(getApplication(), "Lỗi"+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //xac nhan xoa lop hoc
    public void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Xác nhận để xóa lớp học..!!!");
        alertDialogBuilder.setIcon(R.drawable.question);

        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xóa lớp học?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
                String id_class = classList.get(posSelected).getCode_class();
                if (db.delete("tblclass", "id_class=?", new String[]{id_class}) != -1) {
                    classList.remove(posSelected); //xoa lop ra khoi danh sach
                    adapter.notifyDataSetChanged(); //cap nhat adapter
                    Toast.makeText(getApplication(), "Xóa lớp học thành công !!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), "Xóa lớp học thất bại !!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclass, menu);
       // getMenuInflater().inflate(R.menu.mnuclass, menu);
        AdapterView.AdapterContextMenuInfo in4 = (AdapterView.AdapterContextMenuInfo) menuInfo;
        posSelected = in4.position;

    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterView.AdapterContextMenuInfo in4 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (in4 == null) return super.onContextItemSelected(item);//null?

        posSelected = in4.position; //luu vi tri da chon
        int id = item.getItemId();

        if (id == R.id.mnueditclass) {
            Toast.makeText(getApplication(),"Edit class informations ...",Toast.LENGTH_LONG).show();
            Room room = classList.get(posSelected);
            Intent intent = new Intent(ClassList.this, EditClassActivity.class);
            intent.putExtra("room", room);
            //startActivityForResult(intent, EDIT_CLASS);
            startActivity(intent);
            return true;
        } else if (id == R.id.mnudeleteclass) {
            confirmDelete();
            return true;
        } else if (id == R.id.mnucloseclass) {
            Notify.exit(this);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_CLASS && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra("data");
            Room room = (Room) bundle.getSerializable("room");
            if (requestCode == OPEN_CLASS) {
                classList.add(room);
            } else if (requestCode == EDIT_CLASS) {
                classList.set(posSelected, room);
            }
            adapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        lstClass = findViewById(R.id.lstClass);
        btnOpenClass = findViewById(R.id.btnOpenClass);
        btnBack = findViewById(R.id.back);

        getClassList();

        //Dang ky listview lam menu context
        registerForContextMenu(lstClass);

        // Initialize database
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        populateClassList();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassList.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btnOpenClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị thông báo
                Toast.makeText(ClassList.this, "Đang đến Thêm lớp học", Toast.LENGTH_SHORT).show();

                // Khởi động activity thêm lớp học
                Intent intent = new Intent(ClassList.this, InsertClassActivity.class);
                startActivity(intent);
            }
        });

        //gan su kien cho listview de lay vi tri xoa 1 doi tuong khoi arrLis
        lstClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posSelected = position;
                return false;
            }
        });
    }

    private void populateClassList() {
        classList.clear(); // Clear the existing list
        classList.add(new Room("Mã lớp", "Tên lớp", "Sỉ số"));

        try (Cursor c = db.query("tblclass", null, null, null, null, null, null)) {
            if (c.moveToFirst()) {
                do {
                    classList.add(new Room(String.valueOf(c.getInt(0)), c.getString(1), String.valueOf(c.getInt(2))));
                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        adapter = new MyAdapterClass(this, android.R.layout.simple_list_item_1, classList);
        lstClass.setAdapter(adapter);
    }

}