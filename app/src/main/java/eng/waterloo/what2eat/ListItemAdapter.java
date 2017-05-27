package eng.waterloo.what2eat;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListItemAdapter extends ArrayAdapter<RestaurantObject> {

    public ListItemAdapter(Activity context, ArrayList<RestaurantObject> ItemObjectList){
        super(context, 0, ItemObjectList);
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox,checkbox1;
        protected RadioGroup mgroup;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RestaurantObject currentItemObject = getItem(position);

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_item, parent, false);

        }

        // Find the ImageView in the list_item.xml layout with the ID list_item_icon

        TextView nameView = (TextView) listItemView.findViewById(R.id.name);
        nameView.setText(currentItemObject.getName());

        return listItemView;
    }


}
