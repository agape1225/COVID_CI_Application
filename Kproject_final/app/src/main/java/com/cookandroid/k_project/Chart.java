package com.cookandroid.k_project;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Chart extends Fragment {
    LinearLayout buttongroup;
    Header Header;
    WeloAppWidgetProvider WeloAppWidgetProvider=new WeloAppWidgetProvider();
    public static Context Chart;


    java.util.Date currentTime = Calendar.getInstance().getTime();

    SimpleDateFormat entireFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

    int month = Integer.parseInt(monthFormat.format(currentTime));
    int day = Integer.parseInt(dayFormat.format(currentTime));
    int year = Integer.parseInt(yearFormat.format(currentTime));

    String yearStr = String.valueOf(year);
    String monthStr = String.valueOf(month);
    String yesterdayStr = String.valueOf(day - 5);
    String dayStr = String.valueOf(day);

    Button first, second, third, fourth, coronastate;
    private static final String CHANNEL_ID = "channel_id01";
    public static final int NOTIFICATION_ID = 1;
    int data_today = 100;
    int data_yesterday = 100;
    int[][] dataarray = new int[8][13];
    int i = 0, r = 0;

    private LineChart lineChart;
    TextView date1, date2, date3, date4, date5, data1, data2, data3, data4, data5;
    int[] date = new int[5];

    TextView[] tvList = new TextView[5];
    int[] tvId = {R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5};


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
        Chart = this.getContext();
        super.onCreate(savedInstanceState);
        List<Entry> entries = new ArrayList<>();
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.chart, container, false);
        buttongroup=(LinearLayout)Header.findViewById(R.id.buttongroup);
        buttongroup.setVisibility(View.VISIBLE);
        lineChart = (LineChart) rootview.findViewById(R.id.chart);
        WeloAppWidgetProvider WeloAppWidgetProvider=new WeloAppWidgetProvider();

        date1 = (TextView) rootview.findViewById(R.id.date1);
        date2 = (TextView) rootview.findViewById(R.id.date2);
        date3 = (TextView) rootview.findViewById(R.id.date3);
        date4 = (TextView) rootview.findViewById(R.id.date4);
        date5 = (TextView) rootview.findViewById(R.id.date5);
        data1 = (TextView) rootview.findViewById(R.id.data1);
        data2 = (TextView) rootview.findViewById(R.id.data2);
        data3 = (TextView) rootview.findViewById(R.id.data3);
        data4 = (TextView) rootview.findViewById(R.id.data4);
        data5 = (TextView) rootview.findViewById(R.id.data5);

        parsing(day);


        dataarray[1][12] = dataarray[1][4] - dataarray[1][5];
        dataarray[1][11] = dataarray[1][3] - dataarray[1][4];
        dataarray[1][10] = dataarray[1][2] - dataarray[1][3];
        dataarray[1][9] = dataarray[1][1] - dataarray[1][2];
        dataarray[1][8] = dataarray[1][0] - dataarray[1][1];
        (Header).data=dataarray[1][8]-dataarray[1][9];
        (Header).yesterday=dataarray[1][9];
        (Header).today=dataarray[1][8];

        entries.add(new Entry(1, dataarray[1][12]));
        entries.add(new Entry(2, dataarray[1][11]));
        entries.add(new Entry(3, dataarray[1][10]));
        entries.add(new Entry(4, dataarray[1][9]));
        entries.add(new Entry(5, dataarray[1][8]));

        data1.setText(String.valueOf(dataarray[1][12]));
        data2.setText(String.valueOf(dataarray[1][11]));
        data3.setText(String.valueOf(dataarray[1][10]));
        data4.setText(String.valueOf(dataarray[1][9]));
        data5.setText(String.valueOf(dataarray[1][8]));
        date1.setText((String.valueOf(dataarray[0][5]).substring(4, 8)/*+". "+(String.valueOf(day-1)*/));
        date2.setText((String.valueOf(dataarray[0][4]).substring(4, 8)/*+". "+(String.valueOf(day-1)*/));
        date3.setText((String.valueOf(dataarray[0][3]).substring(4, 8)/*+". "+(String.valueOf(day-1)*/));
        date4.setText((String.valueOf(dataarray[0][2]).substring(4, 8)/*+". "+(String.valueOf(day-1)*/));
        date5.setText((String.valueOf(dataarray[0][1]).substring(4, 8)/*+". "+(String.valueOf(day-1)*/));

        LineDataSet lineDataSet = new LineDataSet(entries, "확진자수");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FF9C41"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FF9C41"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();


        for (int i = 4; i > -1; i--) {
            date[i] = (month * 100) + day;
            day--;

            if (day < 1) {
                month--;
                if (month == 2 || month == 4 || month == 6 || month == 9 || month == 11) {
                    day = 30;
                } else {
                    day = 31;
                }
            }
        }


        for (int i = 0; i < 5; i++) {
            tvList[i] = (TextView) rootview.findViewById(tvId[i]);
        }


        for (int i = 0; i < 5; i++) {
            int buffDay = date[i] % 100;
            int buffMonth = date[i] / 100;

            String stringDay;
            String stringMonth;


            if (buffDay < 10) {
                stringDay = "0" + Integer.toString(buffDay);
            } else {
                stringDay = Integer.toString(buffDay);
            }

            if (buffMonth < 10) {
                stringMonth = "0" + Integer.toString(buffMonth);
            } else {
                stringMonth = Integer.toString(buffMonth);
            }

            tvList[i].setText(stringMonth + "." + stringDay);
        }

        return rootview;
    }

    public void parsing(int xmlday) {

        StrictMode.enableDefaults();

        boolean initem = false, inAddr = false, inChargeTp = false;

        String addr = null, chargeTp = null;
        String nowmonth = monthStr;
        String yestermonth = monthStr;
        String yesteryear = yearStr;
        try {
            if (xmlday - 6 < 0) {
                yestermonth = String.valueOf(month - 1);
                if (month == 4 || month == 6 || month == 9 || month == 11) {
                    xmlday = 30 - 5 + day;
                } else if (month == 2) {
                    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                        xmlday = 29 - 5 + day;
                    } else {
                        xmlday = 28 - 5 + day;
                    }
                } else if (month == 1) {
                    yesteryear = String.valueOf(year - 1);
                    yestermonth = "12";
                    xmlday = 31 - 5 + day;
                } else {
                    xmlday = 31 - 5 + day-1;
                }
            } else {
                xmlday = day - 5;
            }

            String num1 = Integer.toString(xmlday);

            URL url = new URL("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson?serviceKey=VbRNOS7%2B19h0Ei7KLEmnMyjmpiHeXA8zDtRLp37%2FU%2BOLgeEES%2FRNkIeFReEPJfLo%2B8FwWLvzaPx1EZPAfGWndg%3D%3D&pageNo=1&numOfRows=10&startCreateDt=" + yesteryear + yestermonth + num1 + "&endCreateDt=" + yearStr + nowmonth + dayStr);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("decideCnt")) {
                            inAddr = true;
                        }
                        if (parser.getName().equals("stateDt")) {
                            inChargeTp = true;
                        }

                        if (parser.getName().equals("message")) {
                            //Alert message if Error occur
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (inAddr) {
                            addr = parser.getText();
                            dataarray[1][i] = Integer.parseInt(addr);
                            inAddr = false;
                            i++;

                        }
                        if (inChargeTp) {
                            chargeTp = parser.getText();
                            dataarray[0][r] = Integer.parseInt(chargeTp);
                            inChargeTp = false;
                            r++;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            initem = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            //Alert message if Error occur
        }
        if (entireFormat.equals(dataarray[0][0]) == true) {
            parsing(day - 1);
        }
    }

}