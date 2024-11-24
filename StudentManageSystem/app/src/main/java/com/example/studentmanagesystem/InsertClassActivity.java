package com.example.studentmanagesystem;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertClassActivity extends Activity {
    private Button btnSaveInsertClass, btnClearInsertClass, btnCloseInsertClass;
    private EditText edtClassCode, edtClassName, edtClassNumber;
    private SQLiteDatabase db;

    //Khoi tao widget
    private void initWidget() {
        btnSaveInsertClass = findViewById(R.id.btnSaveInsertClass);
        btnClearInsertClass = findViewById(R.id.btnClearInsertClass);
        btnCloseInsertClass = findViewById(R.id.btnCloseInsertClass);
        edtClassCode = findViewById(R.id.edtClassCode);
        edtClassName = findViewById(R.id.edtClassName);
        edtClassNumber = findViewById(R.id.edtClassNumber);
    }

    //Them lop
    private long saveClass() {
        long result = -1;
        try {
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();

            values.put("code_class", edtClassCode.getText().toString());
            values.put("name_class", edtClassName.getText().toString());
            values.put("number_student", Integer.parseInt(edtClassNumber.getText().toString()));


            result = db.insert("tblclass", null, values);
            if (result != -1) {
                Toast.makeText(getApplicationContext(), "Thêm lớp học thành công!!!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }

    //Xoa all dulieu
    private void clearAll() {
        edtClassCode.setText("");
        edtClassName.setText("");
        edtClassNumber.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class);

        initWidget();

        btnSaveInsertClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Kiểm tra xem các trường có rỗng không
                String classCode = edtClassCode.getText().toString().trim();
                String className = edtClassName.getText().toString().trim();
                String classNumberStr = edtClassNumber.getText().toString().trim();

                if (classCode.isEmpty() || className.isEmpty()) {
                    Toast.makeText(InsertClassActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lưu lớp vào cơ sở dữ liệu
                long result = saveClass();

                Bundle bundle = new Bundle();
                Intent intent = new Intent(InsertClassActivity.this, ClassList.class);
                startActivity(intent);

                if (result != -1) { //Them thanh cong
                    Room room = new Room(String.valueOf(result), edtClassName.getText().toString(), edtClassNumber.getText().toString());
                    bundle.putSerializable("room", room);
                    intent.putExtras(bundle);
                    setResult(ClassList.SAVE_CLASS, intent);
                    Toast.makeText(getApplicationContext(), "Thêm lớp học thành công!!!", Toast.LENGTH_LONG).show();
                    clearAll(); //Xoa sau khi them xong
                }
            }
        });

        btnClearInsertClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });

        btnCloseInsertClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertClassActivity.this, ClassList.class);
                startActivity(intent);
            }
        });
    }


}
