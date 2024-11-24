package com.example.studentmanagesystem;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    public static final String DATABASE_NAME = "student.db";
    private SQLiteDatabase db;
    private EditText edtUsername, edtPassword;
    private Button btnCloseLogin, btnLogin;

    // Tạo DB
    private void initDB() {
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        try {
            createTableIfNotExists("tbluser", "CREATE TABLE tbluser (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL)");
            createTableIfNotExists("tblclass", "CREATE TABLE tblclass (id_class INTEGER PRIMARY KEY AUTOINCREMENT, code_class TEXT, name_class TEXT, number_student INTEGER)");
            createTableIfNotExists("tblstudent", "CREATE TABLE tblstudent (id_student INTEGER PRIMARY KEY AUTOINCREMENT, id_class INTEGER NOT NULL, code_student TEXT, name_student TEXT, gender_student TEXT, birthday_student TEXT, address_student TEXT)");
        } catch (Exception e) {
            Toast.makeText(this, "Khởi tạo cơ sở dữ liệu không thành công", Toast.LENGTH_LONG).show();
        }
    }

    // Tạo bảng nếu chưa tồn tại
    private void createTableIfNotExists(String tableName, String createTableSQL) {
        if (!isTableExist(db, tableName)) {
            db.execSQL(createTableSQL);
            if (tableName.equals("tbluser")) {
                db.execSQL("INSERT INTO tbluser (username, password) VALUES ('admin', 'admin')");
            }
        }
    }

    // Bảng đã được tạo?
    private boolean isTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // User login?
    private boolean isUser(String username, String password) {
        try {
            db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM tbluser WHERE username=? AND password=?", new String[]{username, password});
            boolean exists = c.getCount() > 0;
            c.close();
            return exists;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnCloseLogin = findViewById(R.id.btnCloseLogin);
        btnLogin = findViewById(R.id.btnLogin);

        initDB();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập tài khoản", Toast.LENGTH_LONG).show();
                    edtUsername.requestFocus();
                } else if (password.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
                    edtPassword.requestFocus();
                } else if (isUser(username, password)) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc activity login
                } else {
                    Toast.makeText(Login.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCloseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}