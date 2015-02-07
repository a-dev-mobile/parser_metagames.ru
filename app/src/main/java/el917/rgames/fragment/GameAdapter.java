package el917.rgames.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import el917.rgames.R;


public class GameAdapter extends BaseAdapter {
    private List<GameItem> gameItemList;
    private Context context;
    private String colorRipple;


    public GameAdapter(String colorRipple, List<GameItem> gameItemList, Context context) {
        this.colorRipple = colorRipple;
        this.gameItemList = gameItemList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return gameItemList.size();
    }

    @Override
    public GameItem getItem(int position) {
        return gameItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder {
        public TextView titleGame;
        public TextView typeGame;
        public TextView releasedData;
        public ImageView thumbnail;
        public MaterialRippleLayout materialRippleLayout;

        ViewHolder() {
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameItem item = gameItemList.get(position);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.row_game, parent, false);
            holder.titleGame = (TextView) convertView.findViewById(R.id.titleGame);
            holder.typeGame = (TextView) convertView.findViewById(R.id.typeGame);
            holder.releasedData = (TextView) convertView.findViewById(R.id.releasedData);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.materialRippleLayout = (MaterialRippleLayout) convertView.findViewById(R.id.ripple);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.titleGame.setText(item.getTitleGame());
        holder.typeGame.setText(item.getTypeGame());
        holder.releasedData.setText("Релиз: " + item.getReleasedData());
        Picasso.with(context).load(item.getThumbnailUrl())
                .error(R.drawable.ic_launcher)
                .into(holder.thumbnail);

        holder.materialRippleLayout.setRippleColor(Color.parseColor(colorRipple));


        return convertView;
    }
}
