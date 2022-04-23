package com.dev_app.gps_adrar.drawer_menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev_app.gps_adrar.R;
import com.dev_app.gps_adrar.testing.MainActivityTEST;
import com.dev_app.gps_adrar.testing.MapsActivityTEST;

public class adapter_drawr extends RecyclerView.Adapter<adapter_drawr.viewHolder> {

    Context context;

    public adapter_drawr(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new viewHolder(view, name);
    }

    private final String[] name =
            {
                    "Hospital",
                    "Station",
                    "ATM",
                    "Airport",
                    "Mosque",
                    "hotel",
                    "bus station",
                    "",
                    "",
                    "",
                    "",
                    "Log out"
            };

    private final int[] img =
            {
                    R.drawable.icon_hospital,
                    R.drawable.icon_station,
                    R.drawable.icon_atm,
                    R.drawable.icon_airplane,
                    R.drawable.icon_mosque,
                    R.drawable.icon_hotel,
                    R.drawable.icon_bus_station,
                    0,
                    0,
                    0,
                    0,
                    R.drawable.ic_login
            };

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.icon.setImageResource(img[position]);
        holder.title.setText(name[position]);

    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;

        public viewHolder(@NonNull View itemView, String[] name) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    MapsActivityTEST activityGps = new MapsActivityTEST();

                    switch (name[position]) {

                        case "Hospital":
                            activityGps.ClickHospital();
                            break;

                        case "Station":
                            activityGps.ClickStation();
                            break;

                        case "ATM":
                            activityGps.ClickAtm();
                            break;

                        case "Airport":
                            activityGps.ClickAirplane();
                            break;

                        case "Mosque":
                            activityGps.ClickMosqi();
                            break;

                        case "hotel":
                            activityGps.ClickHotel();
                            break;

                        case "bus station":
                            activityGps.ClickBus();
                            break;

                        case "Log out":
                            
                            break;

                    }

                }
            });

        }
    }
}
