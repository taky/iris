package jp.co.monolithworks.il.iris;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class ResultActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);

        List<ResultData> list = new ArrayList<ResultData>();
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_search);
        Bitmap cabbage = BitmapFactory.decodeResource(getResources(), R.drawable.cabbage);

        //ScanData scanData = ScanData.getScanData();
        //Bitmap thumbnail = scanData.thumbnail;

        	for (int i = 0; i<10; i++){
        		list.add(new ResultData(bm,cabbage,"白菜",String.format("あと%d日", i)));
        	}

        	ListView lv = (ListView)findViewById(R.id.result_listView);
        	lv.setAdapter(new ResultAdapter(this,list));

        	lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					ListView listView = (ListView) parent;

					Intent intent = new Intent();
					intent.setClass(ResultActivity.this, DetailActivity.class);
					startActivity(intent);
				}

        	});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.menu_settings:
                intent.setClass(ResultActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_fridge:
            	intent.setClass(ResultActivity.this, FridgeActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_scan:
            	intent.setClass(ResultActivity.this, ScanActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
