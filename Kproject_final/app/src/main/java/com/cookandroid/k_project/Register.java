package com.cookandroid.k_project;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    Button finishbtn, agreement, findaddress, btnoverck;
    EditText id, pw, pwok, name, number1,number2,number3, houseaddress, jobaddress, birth,email;
    CheckBox agree;
    SQLiteDatabase sqlDB;
    myDBHelper myDBHelper;
    String phone_number;
    String _id="";
    boolean idover=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        agree = (CheckBox) findViewById(R.id.checkagree);
        agreement = (Button) findViewById(R.id.btnagreement);
        finishbtn = (Button) findViewById(R.id.finishbtn);
        id = (EditText) findViewById(R.id.edit_id);
        pw = (EditText) findViewById(R.id.edit_pw);
        pwok = (EditText) findViewById(R.id.edit_pwok);
        name = (EditText) findViewById(R.id.edit_name);
        number1 = (EditText) findViewById(R.id.edit_number1);
        number2 = (EditText) findViewById(R.id.edit_number2);
        number3 = (EditText) findViewById(R.id.edit_number3);
        email=(EditText)findViewById(R.id.edit_email);
        houseaddress = (EditText) findViewById(R.id.edit_houseaddress);
        jobaddress = (EditText) findViewById(R.id.edit_jobaddress);
        birth = (EditText) findViewById(R.id.edit_birth);

        btnoverck= (Button) findViewById(R.id.btnoverck);
        myDBHelper=new myDBHelper(this);

        btnoverck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idover=false;
                sqlDB = myDBHelper.getReadableDatabase();
                Cursor cursor;
                cursor =sqlDB.rawQuery("SELECT * FROM loginTBL;",null);
                if( cursor.getCount()==0){
                    if(id.getText().toString().replace(" ", "").equals("")) {
                        Toast.makeText(getApplicationContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        cursor.moveToFirst();
                        return;
                    }
                    else{
                        idover =true;
                        _id= id.getText().toString();
                        Toast.makeText(getApplicationContext(), "사용 가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    while(cursor.moveToNext()){
                        if(id.getText().toString().replace(" ", "").equals("")){
                            Toast.makeText(getApplicationContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            cursor.moveToFirst();
                            return;
                        }
                        else{
                            if(cursor.getString(0).contentEquals(id.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "이미 존재하는 ID 입니다.", Toast.LENGTH_SHORT).show();
                                cursor.moveToFirst();
                                id.setText("");
                                return;
                            }
                            else{
                                idover =true;
                                _id= id.getText().toString();
                                Toast.makeText(getApplicationContext(), "사용 가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!idover||!_id.contentEquals(id.getText().toString())){
                    Toast.makeText(getApplicationContext(), "ID 중복 확인을 해주세요", Toast.LENGTH_SHORT).show();
                } else if (pw.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (pwok.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else if (!pw.getText().toString().equals(pwok.getText().toString())){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if (name.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (number1.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (number2.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (number3.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if (email.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else if (houseaddress.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "집주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (birth.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else if(birth.getText().length() !=8){
                    Toast.makeText(getApplicationContext(), "생년월일을 8글자로 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if ((agree.isChecked() == false)) {
                    Toast.makeText(getApplicationContext(), "동의해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    phone_number = number1.getText().toString()+number2.getText().toString()+number3.getText().toString();
                    sqlDB = myDBHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO loginTBL VALUES( '" + id.getText().toString() + "' , '" + pw.getText().toString() + "' , '" + name.getText().toString() + "' , '" + phone_number + "', '" + email.getText().toString() + "', '" + houseaddress.getText().toString() + "', '" + jobaddress.getText().toString() + "', '" + birth.getText().toString() + "');");
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "로그인 해주세요", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
        agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Register.this);
                dlg.setTitle("개인정보 활용 동의서");
                dlg.setMessage("상기 본인은 본 WeLo앱이 본인에 대한 위치기반서비스를 실시하기 위해 다음의 개인정보를 제공하고 활용하는 것에 동의한다.\n" +
                        "제공하는 개인정보항목\n" +
                        "-개인식별정보:이름, 주민번호, 회원번호\n -연락처: 주소, 이메일,유/무선 전화번호(가입 당시 정보 및 이후 변경된 정보 포함)\n" +
                        "-접촉정보: 회원가입일자, 위치정보 등" +
                        "\n 보유 및 이용기간\n" +"1년"+
                        "\n 1. 정보수집 \n \n\t-기본정보\n\t-개인이력\n\t-위치");
                dlg.setPositiveButton("확인",null);
                dlg.show();
            }
        });
    }


}