package com.cookandroid.k_project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    Button registerbtn, loginbtn;
    EditText edit_id,edit_pw;
    SQLiteDatabase sqlDB;
    String id,pw;
    myDBHelper myHelper;
    static int cursorindex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        registerbtn = (Button) findViewById(R.id.btnregister);
        loginbtn = (Button) findViewById(R.id.btnlogin);
        edit_id = (EditText) findViewById(R.id.edit_id);
        edit_pw = (EditText) findViewById(R.id.edit_pw);
        myHelper = new myDBHelper(this);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {

            int state;

            @Override
            public void onClick(View view) {
                id = edit_id.getText().toString().trim();
                pw = edit_pw.getText().toString().trim();

                if (id.equals(""))
                    edit_id.setError("아이디를 입력해주세요.");
                else if (pw.equals(""))
                    edit_pw.setError("비밀번호를 입력해주세요.");
                else {

                    sqlDB = myHelper.getWritableDatabase();
                    Cursor cursor;
                    cursor = sqlDB.rawQuery("SELECT * FROM loginTBL;", null);

                    while (cursor.moveToNext()) {
                        String buffId = cursor.getString(0);
                        String buffPw = cursor.getString(1);
                        if(id.equals(buffId)){
                            if(pw.equals(buffPw))
                            {
                                cursorindex = cursor.getPosition();
                                state = 1;
                                break;
                            }
                            else
                                state = 2;
                        }
                        else
                            state=3;
                    }
                    if (state ==1) {
                        Intent intent = new Intent(getApplicationContext(), Header.class);
                        startActivity(intent);
                    } else if (state ==2) {
                        Toast.makeText(getApplicationContext(), "비밀번호를 잘못입력하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else  if(state ==3){
                        Toast.makeText(getApplicationContext(), "아이디를 잘못입력하셨습니다.", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });

    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "weloDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE loginTBL ( ID CHAR(20) PRIMARY KEY,PW CHAR(20),NAME CHAR(20),NUMBER CHAR(20),EMAIL CHAR(50),HOMEADDRESS CHAR(200),JOBADDRESS CHAR(200),BIRTH CHAR(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS loginTBL");
            onCreate(db);

        }
    }
}
