package com.cookandroid.k_project;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cookandroid.k_project.Header;
import com.cookandroid.k_project.Model.Articles;
import com.cookandroid.k_project.Model.Headlines;
import com.cookandroid.k_project.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Article extends Fragment {

    Header Header;
    Context ct;
    SQLiteDatabase sqlDB;
    myDBHelper myDBHelper;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText etQuery;
    Button btnSearch;
    Dialog dialog;
    final String API_KEY = "4db70da230ec4eb4a44e363b1baa0ab2";
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();


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

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.article, container, false);
        ct = container.getContext();

        swipeRefreshLayout = rootview.findViewById(R.id.swipeRefresh);
        recyclerView = rootview.findViewById(R.id.recyclerView);

        etQuery = rootview.findViewById(R.id.etQuery);
        btnSearch = rootview.findViewById(R.id.btnSearch);
        dialog = new Dialog(ct);

        recyclerView.setLayoutManager(new LinearLayoutManager(ct));
        final String country = getCountry();

        etQuery.setText("코로나");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson(etQuery.getText().toString(), country, API_KEY);
            }
        });
        retrieveJson(etQuery.getText().toString(), country, API_KEY);


        myDBHelper = new myDBHelper(ct);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!etQuery.getText().toString().equals("")) {
                    sqlDB = myDBHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO groupTBL VALUES( '" + etQuery.getText().toString() + "' );");
                    sqlDB.close();
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson(etQuery.getText().toString(), country, API_KEY);
                        }
                    });
                    retrieveJson(etQuery.getText().toString(), country, API_KEY);
                } else {*/
                retrieveJson(etQuery.getText().toString(), country, API_KEY);
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("", country, API_KEY);
                        }
                    });
                    retrieveJson("", country, API_KEY);
                }
            //}
        });
        class myDBHelper extends SQLiteOpenHelper {
            public myDBHelper(Context context) {
                super(context, "groupDB", null, 1);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE groupTBL (SEARCH CHAR(100));");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS groupTBL");
                onCreate(db);

            }
        }

        return rootview;
    }

    public void retrieveJson(String query ,String country, String apiKey){


        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
        if (!etQuery.getText().toString().equals("")){
            call= ApiClient.getInstance().getApi().getSpecificData(query,apiKey);
        }else{
            call= ApiClient.getInstance().getApi().getHeadlines(country,apiKey);
        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(ct,articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ct, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String getCountry(){
        Locale locale = Locale.KOREA;
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}