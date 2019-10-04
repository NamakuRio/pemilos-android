package io.github.namakurio.pemilos.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.namakurio.pemilos.R;

public class ToastUtils {

    public static void customToast(LayoutInflater inflater, Context context, Activity act, int textColor, String message, int backgroundCard){
        View layout = inflater.inflate(R.layout.toast_custom, (ViewGroup) act.findViewById(R.id.custom_toast_layout_id));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setTextColor(textColor);
        text.setText(message);
        CardView lyt_card = (CardView) layout.findViewById(R.id.lyt_card);
        lyt_card.setBackgroundColor(act.getResources().getColor(backgroundCard));

        android.widget.Toast toast = new android.widget.Toast(context.getApplicationContext());
        toast.setDuration(android.widget.Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
