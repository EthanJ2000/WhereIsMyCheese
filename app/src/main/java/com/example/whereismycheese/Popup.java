package com.example.whereismycheese;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Popup {
    private EditText cheeseMessage;
    private Button addCheeseButton;
    private Button exitDialogButton;
    private PopupWindow popupWindow;
    private View popupView;
    private Context context;
    private LatLng latLng;
    private GoogleMap mMap;
    public static Marker marker;

    public Popup(Context context, LatLng latLng, GoogleMap mMap) {
        this.context = context;
        this.latLng = latLng;
        this.mMap = mMap;
    }

    public void initPopup() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.dialog_note, null);

        cheeseMessage = popupView.findViewById(R.id.noteText);
        addCheeseButton = popupView.findViewById(R.id.saveCheeseButton);
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

        //handle addCheeseButton click
        addCheeseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String markerTitle = cheeseMessage.getText().toString().trim();
                if (!markerTitle.isEmpty()) {
                    marker = mMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.cheesy_note))
                            .title(cheeseMessage.getText().toString()));
                    popupWindow.dismiss();
                    LocationService locationService = new LocationService();
                    locationService.populateGeofenceList(marker);
                    locationService.addGeofences(context);
                }
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
