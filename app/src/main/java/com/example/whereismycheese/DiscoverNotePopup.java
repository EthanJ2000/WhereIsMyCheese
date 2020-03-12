package com.example.whereismycheese;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DiscoverNotePopup {

    private PopupWindow popupWindow;
    private View popupView;
    private Context context;
    private TextView cheeseContent;
    private Button pickUpCheeseButton;
    private Button exitDialogButton;

    public DiscoverNotePopup(Context context) {
        this.context = context;
    }

    public void initPopup(final Marker marker){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.note_discovered, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        //dim background
        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);

        cheeseContent = popupView.findViewById(R.id.cheeseContent);
        cheeseContent.setText(marker.getTitle());
        pickUpCheeseButton = popupView.findViewById(R.id.pickUpCheeseButton);
        pickUpCheeseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.remove();
                popupWindow.dismiss();
            }
        });

        exitDialogButton = popupView.findViewById(R.id.exitDialogButton);
        exitDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
}
