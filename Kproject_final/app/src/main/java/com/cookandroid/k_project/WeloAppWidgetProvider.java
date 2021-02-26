package com.cookandroid.k_project;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class WeloAppWidgetProvider extends AppWidgetProvider {
    int data_today=((Header)Header.header).today;
    int data_yesterday =((Header)Header.header).yesterday;
    int data = ((Header)Header.header).data;
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context,Header.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.app_widget_button,pendingIntent);

            Intent intent2 = new Intent(Intent.ACTION_VIEW,Uri.parse("https://nid.naver.com/login/privacyQR"));
            PendingIntent pendingIntent2 = PendingIntent.getActivity(context,0,intent2,0);
            views.setOnClickPendingIntent(R.id.qr,pendingIntent2);

            views.setTextViewText(R.id.tv_number,Integer.toString(data_today));
            if(data_today>data_yesterday)
                views.setTextViewText(R.id.tv_info,"어제보다 "+Integer.toString(data_today-data_yesterday)+"명 증가했습니다!");
            else if(data_today<data_yesterday)
                views.setTextViewText(R.id.tv_info,"어제보다 "+Integer.toString(data_today-data_yesterday)+"명 감소했습니다!");
            else
                views.setTextViewText(R.id.tv_info,"어제와 확진자수가 동일합니다!");
            if(data_today>=1 && data_today<=30){
                views.setImageViewResource(R.id.image_icon, R.drawable.step1);
            }
            else if (data_today>=31 && data_today<=60) {
                views.setImageViewResource(R.id.image_icon, R.drawable.step2);
            }
            else if (data_today>=61 && data_today<=100){
                views.setImageViewResource(R.id.image_icon, R.drawable.step3);
            }
            else if (data_today>=100){
                views.setImageViewResource(R.id.image_icon, R.drawable.step4);
            }
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }

    public int changedata(int yesterday, int today)
    {
        data_today=today;
        data_yesterday=yesterday;
        return today-yesterday;
    }
}
