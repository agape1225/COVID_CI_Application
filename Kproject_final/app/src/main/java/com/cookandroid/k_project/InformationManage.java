package com.cookandroid.k_project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.HashMap;


public class InformationManage extends Fragment {
    Header Header;
    Context ct;
    DrawerLayout drawerLayout;
    TextView tvname, tvid, tvbirth, tvemail, tvhomead, tvcompanyad, tvnew, tvnewck, tvnotice, tvnewhome, tvnewcompany;
    EditText etnew, etnewck, etpr, etnewhome, etnewcompany;
    Button btnpassword, btnhome, btncompany, btncheck;
    SQLiteDatabase sqlDB;
    myDBHelper myHelper;
    TextView tvuser;
    String pw;
    String sname = "";
    String sid = "";
    String spw = "";
    String snumber = "";
    String semail = "";
    String shomead = "";
    String sjobad = "";
    String sbirth = "";
    String result = "";
    Cursor cursor;
    View dialogView, LoginView, dialogView_home, dialogView_company;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Header = (Header) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Header = null;
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.informationmanage, container, false);
        ct = container.getContext();

        drawerLayout = rootview.findViewById(R.id.drawer_layout);
        tvname = rootview.findViewById(R.id.tvname);
        tvid = rootview.findViewById(R.id.tvid);
        tvbirth = rootview.findViewById(R.id.tvbirth);
        tvemail = rootview.findViewById(R.id.tvemail);
        tvhomead = rootview.findViewById(R.id.tvhomead);
        tvcompanyad = rootview.findViewById(R.id.tvcompanyad);

        btnpassword = rootview.findViewById(R.id.btnpassword);
        btnhome = rootview.findViewById(R.id.btnhome);
        btncompany = rootview.findViewById(R.id.btncompany);

        myHelper = new myDBHelper(Header);

        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT * FROM loginTBL;", null);

        cursor.moveToPosition(Login.cursorindex);
        sid = cursor.getString(0);
        spw = cursor.getString(1);
        sname = cursor.getString(2);
        snumber = cursor.getString(3);
        semail = cursor.getString(4);
        shomead = cursor.getString(5);
        sjobad = cursor.getString(6);
        sbirth = cursor.getString(7);

        result += sid + "   " + spw + "   " + sname + "   " + snumber + "   " + semail + "   " + shomead + "   " + sjobad + "   " + sbirth + "\r\n";

        tvname.setText(sname);
        tvid.setText(sid);
        tvbirth.setText(sbirth);
        tvemail.setText(semail);
        tvhomead.setText(shomead);
        tvcompanyad.setText(sjobad);
        cursor.close();
        sqlDB.close();


        btnpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialogView = (View) View.inflate(ct, R.layout.dialogpw, null);
                btncheck = (Button) dialogView.findViewById(R.id.btnck);
                tvnew = (TextView) dialogView.findViewById(R.id.tvnew);
                tvnewck = (TextView) dialogView.findViewById(R.id.tvnewck);
                tvnotice = (TextView) dialogView.findViewById(R.id.tvnotice);
                etnew = (EditText) dialogView.findViewById(R.id.etnew);
                etnewck = (EditText) dialogView.findViewById(R.id.etnewck);
                etpr = (EditText) dialogView.findViewById(R.id.etpr);

                btncheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (spw.contentEquals(etpr.getText().toString())) {
                            tvnotice.setText("");
                            tvnew.setVisibility(View.VISIBLE);
                            tvnewck.setVisibility(View.VISIBLE);
                            etnew.setVisibility(View.VISIBLE);
                            etnewck.setVisibility(View.VISIBLE);
                        } else {
                            tvnotice.setText("현재 비밀번호가 일치하지 않습니다.");
                        }
                    }
                });
                AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                dlg.setTitle("비밀번호 변경");
                dlg.setView(dialogView);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqlDB = myHelper.getWritableDatabase();
                        cursor.moveToPosition(Login.cursorindex);
                        if (!(etnew.getText().toString().replace(" ", "").equals("") ||
                                etpr.getText().toString().replace(" ", "").equals("") ||
                                etnewck.getText().toString().replace(" ", "").equals(""))) {
                            if (etnew.getText().toString().equals(etnewck.getText().toString())) {
                                tvnotice.setText("");
                                sqlDB.execSQL("UPDATE loginTBL SET PW='" + etnew.getText().toString() + "'WHERE ID ='" + sid + "';");
                                cursor = sqlDB.rawQuery("SELECT * FROM loginTBL;", null);
                                cursor.moveToPosition(Login.cursorindex);
                                spw = cursor.getString(1);
                                AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                                dlg.setTitle("비밀번호 변경 완료");
                                dlg.setMessage("비밀번호를 성공적으로 변경했습니다.");
                                dlg.setPositiveButton("확인", null);
                                dlg.show();
                            } else {
                                AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                                dlg.setTitle("비밀번호 변경 불가");
                                dlg.setMessage("비밀번호가 일치하지 않습니다.");
                                dlg.setPositiveButton("확인", null);
                                dlg.show();
                            }
                        } else {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                            dlg.setTitle("비밀번호 변경 불가");
                            dlg.setMessage("비밀번호가 입력되지 않았습니다.");
                            dlg.setPositiveButton("확인", null);
                            dlg.show();
                        }

                        cursor.close();
                        sqlDB.close();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView_home = (View) View.inflate(ct, R.layout.dialoghome, null);
                tvnewhome = (TextView) dialogView_home.findViewById(R.id.tvnewhome);
                etnewhome = (EditText) dialogView_home.findViewById(R.id.etnewhome);

                AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                dlg.setTitle("집주소 변경");
                dlg.setView(dialogView_home);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqlDB = myHelper.getWritableDatabase();
                        cursor.moveToPosition(Login.cursorindex);
                        if (!etnewhome.getText().toString().replace(" ", "").equals("")) {
                            sqlDB.execSQL("UPDATE loginTBL SET HOMEADDRESS='" + etnewhome.getText().toString() + "'WHERE ID ='" + sid + "';");
                            cursor = sqlDB.rawQuery("SELECT * FROM loginTBL;", null);
                            cursor.moveToPosition(Login.cursorindex);
                            tvhomead.setText(cursor.getString(5));
                            AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                            dlg.setTitle("집주소 변경 완료");
                            dlg.setMessage("집주소를 성공적으로 변경했습니다.");
                            dlg.setPositiveButton("확인", null);
                            dlg.show();
                        } else {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                            dlg.setTitle("집주소 변경 불가");
                            dlg.setMessage("집주소가 변경되지 않았습니다.");
                            dlg.setPositiveButton("확인", null);
                            dlg.show();
                        }

                        cursor.close();
                        sqlDB.close();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });
        btncompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView_company = (View) View.inflate(ct, R.layout.dialogcompany, null);
                tvnewcompany = (TextView) dialogView_company.findViewById(R.id.tvnewcompany);
                etnewcompany = (EditText) dialogView_company.findViewById(R.id.etnewcompany);

                AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                dlg.setTitle("회사(학교)주소 변경");
                dlg.setView(dialogView_company);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqlDB = myHelper.getWritableDatabase();
                        cursor.moveToPosition(Login.cursorindex);
                        if (etnewcompany.getText().toString() == "")
                            sqlDB.execSQL("UPDATE loginTBL SET JOBADDRESS='" + tvcompanyad + "'WHERE ID ='" + sid + "';");
                        else {
                            sqlDB.execSQL("UPDATE loginTBL SET JOBADDRESS='" + etnewcompany.getText().toString() + "'WHERE ID ='" + sid + "';");
                            cursor = sqlDB.rawQuery("SELECT * FROM loginTBL;", null);
                            cursor.moveToPosition(Login.cursorindex);
                            tvcompanyad.setText(cursor.getString(6));
                        }
                        cursor.close();
                        sqlDB.close();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });
        return rootview;
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