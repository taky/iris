package jp.co.monolithworks.il.iris;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class ResultAdapter extends ArrayAdapter<ResultData>{
    LayoutInflater mInflater;
    ResultActivity mContext;

    int meet,fish,vegetable,drink,fruit,ham;
    int count=100;

    public ResultAdapter(ResultActivity context,List<ResultData> objects){
        super(context,0,objects);
        mContext = context;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Resources res = getContext().getResources();
        meet = res.getColor(R.color.meet);
        fish = res.getColor(R.color.fish);
        vegetable = res.getColor(R.color.vegetable);
        drink = res.getColor(R.color.drink);
        fruit = res.getColor(R.color.fruit);
        ham = res.getColor(R.color.ham);
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            convertView = this.mInflater.inflate(R.layout.result_item,parent,false);
            holder = new ViewHolder();

            ImageView thumbnailView,iconView;
            TextView categoryText,consumelimitText;

            holder.thumbnailView = (ImageView)convertView.findViewById(R.id.thumbnail);
            holder.iconView = (ImageView)convertView.findViewById(R.id.icon);
            holder.categoryText = (TextView)convertView.findViewById(R.id.category);
            holder.consumelimitText = (TextView)convertView.findViewById(R.id.consumelimit);
            holder.frameLayout = (FrameLayout)convertView.findViewById(R.id.f_layout);
            holder.delete = (Button)convertView.findViewById(R.id.deleteButton);
            holder.edit = (Button)convertView.findViewById(R.id.editButton);

            switch(count){
            case 0:
                holder.frameLayout.setBackgroundColor(meet);
                break;
            case 1:
                holder.frameLayout.setBackgroundColor(fish);
                break;
            case 2:
                holder.frameLayout.setBackgroundColor(vegetable);
                break;
            case 3:
                holder.frameLayout.setBackgroundColor(drink);
                break;
            case 4:
                holder.frameLayout.setBackgroundColor(fruit);
                break;
            case 5:
                holder.frameLayout.setBackgroundColor(ham);
                break;
            }

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final ResultData data = (ResultData)getItem(position);
        final int transferPosition = position;

        holder.thumbnailView.setImageBitmap(data.thumbnailBitmap);
        holder.iconView.setImageBitmap(data.iconBitmap);
        holder.categoryText.setText(data.categoryText);
        holder.categoryText.setTypeface( Typeface.DEFAULT_BOLD, Typeface.BOLD );
        holder.consumelimitText.setText(String.format("あと%s日",data.consumelimitText));
        holder.consumelimitText.setTypeface( Typeface.DEFAULT_BOLD, Typeface.BOLD );
        holder.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ScanData sd = ScanData.getScanData();
                sd.lists.remove(transferPosition);
                mContext.setListView();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(mContext, "edit", Toast.LENGTH_SHORT).show();
                ScanData sd = ScanData.getScanData();
                ResultData rd = sd.lists.get(transferPosition);
                
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView thumbnailView,iconView;
        TextView categoryText,consumelimitText;
        FrameLayout frameLayout;
        Button delete;
        Button edit;
    }

}