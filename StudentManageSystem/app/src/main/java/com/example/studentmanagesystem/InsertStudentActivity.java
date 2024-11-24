package com.example.studentmanagesystem;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class InsertStudentActivity extends Activity {
    Button btnSave, btnClear, btnClose;
    EditText edtCode, edtName, edtAddress, edtBirthday;
    RadioGroup rdigroupGender;
    Spinner spnClassCode;
    SQLiteDatabase db;
    ArrayList<Room> classList = new ArrayList<Room>();
    ArrayAdapter<Room> adapter;

    int posSpinner = -1; // Get the position of the spinner
    int idChecked, gender = 0; // Get gender

    private void initWidget(){
        btnSave = (Button) findViewById(R.id.btnSaveInsertStudent);
        btnClear = (Button) findViewById(R.id.btnClearInsertStudent);
        btnClose = (Button) findViewById(R.id.btnCloseInsertStudent);

        edtCode = (EditText) findViewById(R.id.edtStudentCode);
        edtName = (EditText) findViewById(R.id.edtStudentName);
        edtAddress = (EditText) findViewById(R.id.edtStudentAddress);

        rdigroupGender = (RadioGroup) findViewById(R.id.rdigroupGender);
        spnClassCode = (Spinner) findViewById(R.id.spnClassCode);

    }

    private void getClassList(){
        try {
            //Them tieu de danh sach lop
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.rawQuery("select * from tblclass", null);
            c.moveToFirst();
            while(!c.isAfterLast()) {
                classList.add(new Room(c.getInt(0) + "", c.getString(1).toString(),
                        c.getString(2).toString(), c.getInt(3) + ""));
                c.moveToNext();
            }
            adapter = new ArrayAdapter<Room>(this, android.R.layout.simple_list_item_1, classList);
            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spnClassCode.setAdapter(adapter);
        } catch (Exception ex) {
            Toast.makeText(this, "Loi", Toast.LENGTH_LONG).show();
        }
    }

    //Save student
    private long saveStudent(){
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        idChecked = rdigroupGender.getCheckedRadioButtonId();
        if(idChecked == R.id.rdiMale)
            gender = 1;
        try {
            values.put("id_class", classList.get(posSpinner).getCode_class());
            values.put("code_student", edtCode.getText().toString());
            values.put("name_student", edtName.getText().toString());
            values.put("birthday", edtBirthday.getText().toString());
            values.put("gender_student", gender);
            return db.insert("tblstudent", null, values);
        }
        catch (Exception ex) {
            Toast.makeText(this, "Loi", Toast.LENGTH_LONG).show();
        }
        return -1; //Them khong thanh cong
    }

        //clear student
    private void clearStudent(){
        edtCode.setText("");
        edtName.setText("");
        edtAddress.setText("");
        rdigroupGender.clearCheck();
        spnClassCode.setSelection(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_student);

        initWidget();
        getClassList();

        spnClassCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                posSpinner = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(InsertStudentActivity.this);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = saveStudent();
                if (id != -1) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    Student student = new Student(classList.get(posSpinner).getCode_class(),
                            classList.get(posSpinner).getName_class(), id + "", edtCode.getText().toString(),
                            edtName.getText().toString(), gender + "",
                            edtBirthday.getText().toString(),
                            edtAddress.getText().toString(),
                            spnClassCode.getSelectedItem().toString()
                    );
                    bundle.putSerializable("student", student);
                    intent.putExtra("data", bundle);
                    setResult(StudentListActivity.SAVE_STUDENT, intent);
                    Toast.makeText(getApplication(), "Them sinh vien thanh cong", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStudent();
            }
        });
    }
}
