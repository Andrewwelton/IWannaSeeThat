package me.andrewwelton.iwannaseethat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Andrew on 7/3/2015.
 */
public class NavListAdapter extends RecyclerView.Adapter <NavListAdapter.NavListViewHolder> {

    private final LayoutInflater inflater;
    List<Info> data = Collections.emptyList();

    public NavListAdapter(Context context, List<Info> listItems) {
        inflater = LayoutInflater.from(context);
        data = listItems;
    }

    @Override
    public NavListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.nav_list_row, viewGroup, false);
        NavListViewHolder viewHolder = new NavListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NavListViewHolder viewHolder, int i) {
        Info current = data.get(i);
        viewHolder.title.setText(current.title);
        viewHolder.icon.setImageResource(current.iconID);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NavListViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public NavListViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.navItemText);
            icon = (ImageView) itemView.findViewById(R.id.navItemIcon);

        }
    }
}
