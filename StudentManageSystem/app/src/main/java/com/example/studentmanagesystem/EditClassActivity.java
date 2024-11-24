package com.example.studentmanagesystem;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditClassActivity extends Activity {
    private TextView edtEditClassCode;
    private TextView edtEditClassName, edtEditClassNumber;
    private Button btnSave, btnClear, btnClose;
    private Room room; // The room object to be edited
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class); // Ensure this matches your layout file name

        // Initialize views
        edtEditClassCode = findViewById(R.id.edtEditClassCode);
        edtEditClassName = findViewById(R.id.edtEditClassName);
        edtEditClassNumber = findViewById(R.id.edtEditClassNumber);
        btnSave = findViewById(R.id.btnSaveInsertClass);
        btnClear = findViewById(R.id.btnClearInsertClass);
        btnClose = findViewById(R.id.btnCloseInsertClass);

        // Get the Room object from the intent
        Intent intent = getIntent();
        room = (Room) intent.getSerializableExtra("room");


        // Populate fields with existing room data
        if (room != null) {
            edtEditClassCode.setText(room.getCode_class());
            edtEditClassName.setText(room.getName_class());
            edtEditClassNumber.setText(room.getClass_number());
        }

        // Set up button click listeners
        btnSave.setOnClickListener(v -> saveChanges());
        btnClear.setOnClickListener(v -> clearFields());
        btnClose.setOnClickListener(v -> finish()); // Close activity without saving
    }

    private void saveChanges() {
        // Get updated values
        String classCode = edtEditClassCode.getText().toString();
        String className = edtEditClassName.getText().toString();
        String classSize = edtEditClassNumber.getText().toString();

        // Validate inputs
        if (
                classCode.isEmpty() ||
                        className.isEmpty() ) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Open database and update the record
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        String updateQuery = "UPDATE tbiclass SET name = ?, size = ? WHERE id_class = ?";
        db.execSQL(updateQuery, new Object[]{className,  //classCode
                });

        //Return updated room object to ClassList
        room.setName_class(className);
        room.setClass_number(classSize);

        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("room", room);
        resultIntent.putExtra("data", bundle);
        setResult(ClassList.SAVE_CLASS, resultIntent);
        finish(); // Close this activity
    }

    private void clearFields() {
        edtEditClassCode.setText("");
        edtEditClassName.setText("");
        edtEditClassNumber.setText("0"); // Resetting to default value
    }
}