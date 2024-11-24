package com.example.studentmanagesystem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapterStudent extends ArrayAdapter<Student> {
    ArrayList<Student> studentList = new ArrayList<>();
    public MyAdapterStudent(Context context, int resource, ArrayList<Student> studentList) {
        super(context, resource, studentList);
        this.studentList = studentList;

    };
    public View getView(int position, View converView, ViewGroup parent) {
        View v = converView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.my_student, null);
        ImageView imgStudent = (ImageView) v.findViewById(R.id.imgStudent);
        TextView txtclassstudent = (TextView) v.findViewById(R.id.txtStudentClass);
        TextView txtnamestudent = (TextView) v.findViewById(R.id.txtStudentName);
        TextView txtbirthdays = (TextView) v.findViewById(R.id.txtStudentBirthday);
        TextView txtgenders = (TextView) v.findViewById(R.id.txtStudentGender);
        TextView txtaddresses = (TextView) v.findViewById(R.id.txtStudentAddress);

        if (position == 0) {
            txtclassstudent.setBackgroundColor(Color.WHITE);
            txtnamestudent.setBackgroundColor(Color.WHITE);
            txtbirthdays.setBackgroundColor(Color.WHITE);
            txtgenders.setBackgroundColor(Color.WHITE);
            txtaddresses.setBackgroundColor(Color.WHITE);

        }
        imgStudent.setImageResource(R.drawable.student);
        txtclassstudent.setText("Mã lớp: " + studentList.get(position).getName_class());
        txtnamestudent.setText("Tên sinh viên: " + studentList.get(position).getName_student());
        txtbirthdays.setText("Ngày sinh: " + studentList.get(position).getBirthday());
        txtgenders.setText("Giới tính: " + studentList.get(position).getGender_student());
        txtaddresses.setText("Địa chỉ: " + studentList.get(position).getAddress_student());
        return v;
    }
}
