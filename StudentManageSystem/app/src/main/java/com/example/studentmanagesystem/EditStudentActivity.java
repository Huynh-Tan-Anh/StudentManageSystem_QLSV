package com.example.studentmanagesystem;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class EditStudentActivity extends Activity {

    private Spinner spnEditClassCode;
    private EditText edtEditStudentCode;
    private EditText edtEditStudentName;
    private RadioGroup rdgEditStudentGender;
    private EditText edtEditStudentBirthday;
    private EditText edtEditStudentAddress;
    private Button btnSaveEditStudent;
    private Button btnClearEditStudent;
    private Button btnCloseEditStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        // Initialize UI elements and set up event listeners
        initUI();
    }

    private void initUI() {
        spnEditClassCode = findViewById(R.id.spnEditClassCode);
        edtEditStudentCode = findViewById(R.id.edtEditStudentCode);
        edtEditStudentName = findViewById(R.id.edtEditStudentName);
        rdgEditStudentGender = findViewById(R.id.rdgEditStudentGender);
        edtEditStudentBirthday = findViewById(R.id.edtEditStudentBirthday);
        edtEditStudentAddress = findViewById(R.id.edtEditStudentAddress);
        btnSaveEditStudent = findViewById(R.id.btnSaveEditStudent);
        btnClearEditStudent = findViewById(R.id.btnClearEditStudent);
        btnCloseEditStudent = findViewById(R.id.btnCloseEditStudent);

        // Set up event listeners for buttons
        btnSaveEditStudent.setOnClickListener(v -> saveStudentDetails());
        btnClearEditStudent.setOnClickListener(v -> clearFields());
        btnCloseEditStudent.setOnClickListener(v -> finish());
    }

    private void saveStudentDetails() {
        // Add code to save student details
    }

    private void clearFields() {
        // Add code to clear input fields
    }

}