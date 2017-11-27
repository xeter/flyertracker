package org.xeter.flyertracker.android.flyer.list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xeter.flyertracker.android.R;
import org.xeter.flyertracker.android.flyer.TextKeys;
import org.xeter.flyertracker.android.flyer.details.FlyerDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.FlyerCardViewHolder> {

    private List<Flyer> flyers;

    public RecyclerAdapter(List<Flyer> flyers) {
        this.flyers = flyers;
    }

    @Override
    public FlyerCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flyer_list_row_layout, parent, false);

        return new FlyerCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlyerCardViewHolder holder, int position) {

        final String imagePath = flyers.get(position).getImagePath();
        holder.img.setImageURI(Uri.parse(imagePath));
        holder.imgPath = imagePath;
        holder.title.setText(flyers.get(position).getTitle());
        holder.description.setText(flyers.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return flyers.size();
    }

    public void setFilter(List<Flyer> newFlyerList) {
        flyers = new ArrayList<>(newFlyerList);
        notifyDataSetChanged();
    }

    static class FlyerCardViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView title;
        private TextView description;
        private String imgPath;

        public FlyerCardViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.flyerList_flyerCardImg);
            title = itemView.findViewById(R.id.flyerList_flyerTitle);
            description = itemView.findViewById(R.id.flyerList_flyerDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();
                    final Intent intent = new Intent(context, FlyerDetailsActivity.class);
                    intent.putExtra(TextKeys.FLYER_TITLE, title.getText().toString());
                    intent.putExtra(TextKeys.FLYER_DESCRIPTION, description.getText().toString());
                    intent.putExtra(TextKeys.FLYER_IMG_PATH, imgPath);
                    context.startActivity(intent);
                }
            });
        }
    }
}
