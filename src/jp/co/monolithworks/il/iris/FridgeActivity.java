package jp.co.monolithworks.il.iris;

import android.os.Bundle;
import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.graphics.*;
import android.util.*;

import java.util.*;

public class FridgeActivity extends Activity {

    public List<ConsumeLimit_Items> consumelimit_list;
    private Context mContext = this;
    private boolean isGridLayout = false;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_result);
        //setContentView(R.layout.fridge_grid);
        
        consumelimit_list = new LinkedList<ConsumeLimit_Items>();
        item_read(mContext);

        setListView();
    }

    private List<ConsumeLimit_Items> item_read(Context c){
        List<ConsumeLimit_Items> consumelimit_list = new LinkedList<ConsumeLimit_Items>();
        DB db = new DB(c);
        List<Map<String,String>> items = db.query();

        for(Map<String,String> item : items){
            ConsumeLimit_Items ci = new ConsumeLimit_Items();
            ci.category = item.get("category_name");
            ci.consumelimit = item.get("consume_limit");
            ci.jan_code = item.get("jan_code");
            String thumbnailFileName = item.get("bar_code");
            ci.thumbnaimFileName = thumbnailFileName;
            Bitmap bmp = BitmapManager.readBitmap(thumbnailFileName, this.getApplicationContext());
            ImageView imageView = new ImageView(this.getApplicationContext());
            imageView.setImageBitmap(bmp);
            ci.thumb = imageView;
            ci.thumb_bmp = bmp;
            consumelimit_list.add(ci);
        }
        return consumelimit_list;
    }

    private void setListView(){

        //XXX
        final TextView tv = (TextView)findViewById(R.id.listview_empty);

        if(consumelimit_list.size() != 0){
        	//XXX
            tv.setVisibility(View.GONE);
            ListView lv = (ListView)findViewById(R.id.result_listView);
            lv.setAdapter(new LimitAdapter(this,consumelimit_list));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            
            //GridView gv = (GridView)findViewById(R.id.fridge_gridView);
            //gv.setAdapter(new LimitAdapter(this,consumelimit_list));
            //gv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    GridView gridView = (GridView) parent;
                    ConsumeLimit_Items item = consumelimit_list.get(position);
                    String[] code = {item.thumbnaimFileName};
                    DB db = new DB(FridgeActivity.this);
                    db.delete(code);
                    item_read(mContext);
                    consumelimit_list.remove(position);
                    if(consumelimit_list.size() != 0){
                        gridView.setAdapter(new LimitAdapter(FridgeActivity.this,consumelimit_list));
                    }else{
                    	//XXX
                        tv.setText("冷蔵庫の中にはぞうが入っています。");
                        tv.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else{
        	//XXX
            tv.setText("冷蔵庫の中にはぞうが入っています。");
            tv.setVisibility(View.VISIBLE);
        }

    }

    private class LimitAdapter extends ArrayAdapter<ConsumeLimit_Items> {
        private LayoutInflater inflater;
        private Activity activity;
        public  List<ConsumeLimit_Items> consumelimit_list;
        public LimitAdapter() {
          super(FridgeActivity.this, R.layout.result_item);

          activity = FridgeActivity.this;
        }

        public LimitAdapter(Context context,List<ConsumeLimit_Items> object) {
            super(context,0,object);
            this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private class ViewHolder {
            ImageButton delete;
            ImageView imageview1;
            ImageView imageview2;
            TextView textview1;
            TextView textview2;
            int position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ConsumeLimit_Items limit_items = this.getItem(position);
            ViewHolder holder;


            if (convertView == null) {
                convertView = inflater.inflate(R.layout.result_item, parent, false);
                holder = new ViewHolder();
                holder.position = position;
                holder.delete = (ImageButton) convertView.findViewById(R.id.deleteButton);
                holder.textview1 = (TextView) convertView.findViewById(R.id.category);
                holder.textview2 = (TextView) convertView.findViewById(R.id.consumelimit);
                holder.imageview1 = (ImageView) convertView.findViewById(R.id.thumbnail);
                holder.imageview2 = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.position = position;
            }

            holder.delete.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        FridgeActivity fa = FridgeActivity.this;
                        ConsumeLimit_Items item = limit_items;
                        String[] code = {item.jan_code};
                        DB db = new DB(fa);
                        db.delete(code);
                        fa.setListView(); 
                        }
                });
            holder.textview1.setText(limit_items.category);
            holder.textview2.setText("後" + limit_items.consumelimit + "日");
            holder.imageview1.setImageBitmap(limit_items.thumb_bmp);
            holder.imageview2.setImageResource(R.drawable.cabbage);
            //new ImageLoader(holder, position, card).executeParallel();
            return convertView;
        }
    }

    private class ConsumeLimit_Items{
        String category;
        String consumelimit;
        String jan_code;
        ImageView thumb;
        ImageView icon;
        Bitmap thumb_bmp;
        String thumbnaimFileName;

        //public ConsumeLimit_Items(String category,String consumelimit,ImageView thumb,ImageView icon){
        public ConsumeLimit_Items(){
            //this.category = category;
            //this.consumelimit = consumelimit;
            //this.thumb = thumb;
            //this.icon = icon;
        }

        public String getCategory(){
            return category;
        }

        public String getConsumeLimit(){
            return consumelimit;
        }

        public String getJan_code(){
            return jan_code;
        }

        public ImageView getThumb(){
            return thumb;
        }

        public ImageView getIcon(){
            return icon;
        }

        public Bitmap getThumb_bmp(){
            return thumb_bmp;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fridge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();

        switch (item.getItemId()) {
        case R.id.menu_grid:
            isGridLayout = true;
            break;
        case R.id.menu_list:
            isGridLayout = false;
            break;
        case R.id.menu_settings:
            intent.setClass(this, SettingActivity.class);
            startActivity(intent);
            break;
        case R.id.menu_scan:
            intent.setClass(this, ScanActivity.class);
            startActivity(intent);
            break;
        }
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(isGridLayout==true){
            menu.findItem(R.id.menu_grid).setVisible(false);
            menu.findItem(R.id.menu_list).setVisible(true);
        }else{
            menu.findItem(R.id.menu_grid).setVisible(true);
            menu.findItem(R.id.menu_list).setVisible(false);
        }
        return true;
    }
}