package com.loveplusplus.update.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Hot_Patching  extends Activity {
	private String Patching_app_name;
	private String Patching_app_version;
	private String Patching_app_introduction;
	String [] name=new String[]{"AAA","BBB","CCC"};
    String [] version=new String[]{"1.0.0","2.0.0","1.2.4"};
	private ListView Patching_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hot__patching);
		ListView Patching_list= (ListView) findViewById(R.id.listView2);
		ArrayList<HashMap<String,Object>> listItem=new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<3;i++) {
			HashMap<String, Object> map=new HashMap<String,Object>();
			map.put("Itemname", name[i]);
			map.put("Itemversion",version[i]);
			listItem.add(map);
		}
		SimpleAdapter listItemAdapter =new SimpleAdapter(this,listItem,
				R.layout.hot_patching_view,
				new String[]{"Itemname","Itemversion"},
				new int[] {R.id.Patchingname,R.id.Patchingversion}
				);
		Patching_list.setAdapter(listItemAdapter);
    Button test= (Button) findViewById(R.id.button5);
		test.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String[] imformation=Analysis();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
   protected String[] Analysis() throws IOException {
	   byte Buffer[] = new byte[1024];
	   ByteArrayOutputStream outputStream = null;
	   FileInputStream in=new FileInputStream("sdcard/Android/data/com.loveplusplus.update.sample/cache/Patch_info.txt");
	   int len = in.read(Buffer);
	   outputStream = new ByteArrayOutputStream();
	   outputStream.write(Buffer,0,len);
	   String whole_info= new String(outputStream.toByteArray());
	   Log.d("好消息！！",whole_info);
	   char[] char_info=whole_info.toCharArray();
	   String name_info="";
	   int r=0;
	   for(int i=5;i<char_info.length;i++)
	   {
		   name_info=name_info+char_info[i];
		   if(char_info[i]==';')
		   {
			   r=i;
			   break;
		   }
		}
	   int rr=r+9;
	   String version_info="";
	   for(int i=rr;i<char_info.length;i++)
	   {
		   version_info=version_info+char_info[i];
		   if(char_info[i]==';')
		   {
			   break;
		   }
	   }
	   Log.d("好消息1！！",name_info);
	   Log.d("好消息2！！",version_info);
	   String[] result=new String[2];
	   result[0]=name_info;
	   result[1]=version_info;
   return result;
   }

}
