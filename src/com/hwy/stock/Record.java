package com.hwy.stock;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Record extends Activity {

	ArrayList<HashMap<String,String>> list;
	//sql
	final DbHelper helper = new DbHelper(this, "Stock.db", null, 1);
	private SQLiteDatabase db;
	ListView listview;
	Recordadapter adapter;

	
	
	Button buttonDellist;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.record);

	
	listview = (ListView) findViewById(R.id.recordlistview);

	
		if (db==null){
   		 db = helper.getWritableDatabase();
   		}
 		
		
		
		
	      //冊除表
        buttonDellist = (Button) findViewById(R.id.buttonbsedit);
        
        buttonDellist.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
            		 dellistbox();
                      
              }});
		
		
		
		populateList();
		
	
		   
	        // Getting adapter by passing xml data ArrayList
	        adapter=new Recordadapter(this, list);
	 
		listview.setAdapter(adapter);



			listview.setOnItemClickListener(new OnItemClickListener() {
	 
	            @Override
	            public void onItemClick(final AdapterView<?> arg0,//The AdapterView where the click happened 
                        View arg1,//The view within the AdapterView that was clicked
                        final int arg2,//The position of the view in the adapter
                        long arg3//The row id of the item that was clicked
                        ) {
	 


	            	
	    			if(buttonDellist.getText().toString().trim().equals(Record.this.getString(R.string.recodfinish))){
	    				
	    				AlertDialog.Builder builder = new AlertDialog.Builder(Record.this);
	    				
	    				final String temstno =list.get(arg2).get("Stockno");
	    				builder.setMessage(Record.this.getString(R.string.wordcfmdel)+temstno+"?");
	    				builder.setTitle(R.string.wordnotice);
	    				builder.setPositiveButton(Record.this.getString(R.string.wordcxl), new OnClickListener() {
	    					public void onClick(DialogInterface dialog, int which) {
	    					
	    						
	    					}});
	    				builder.setNegativeButton(R.string.wordcfm, new OnClickListener() {

	    					public void onClick(DialogInterface dialog, int which) {
	    						System.out.println(list.get(arg2).get("record_id"));
	    						
	    						db.delete("Record",	//資料表名稱
	    								"record_id=" + list.get(arg2).get("record_id"),			//WHERE
	    								null				//WHERE的參數
	    								);
	    						

	    						
	    						list.remove(arg2);
	    						
	    						Toast.makeText(Record.this,Record.this.getString(R.string.recoddeletesus)+temstno+Record.this.getString(R.string.recodrecord), Toast.LENGTH_SHORT).show();
	    						
	    						adapter.notifyDataSetChanged();
	    						}			
	    						});
	    				  builder.create().show();
	    			
	    				
	    	        }     	
	            }


	        });
		
		
		
	}//end create
	
	
	private void populateList() {
		
		
		 list =new ArrayList<HashMap<String,String>>();
		 
		 

			Cursor clist2 = db.query("Record", null,null, null, null, null, "record_id DESC");


			for(clist2.moveToFirst();!clist2.isAfterLast();clist2.moveToNext()){


				HashMap<String,String>  temp = new HashMap<String,String>();
				temp.put("record_id",clist2.getString(0));
				temp.put("Stockno",clist2.getString(1));
				temp.put("Date", clist2.getString(6));
				

				if(clist2.getString(2).trim().equals("1")){
					temp.put("Type", Record.this.getString(R.string.recoditembuy));
					temp.put("Bsprice", String.format("%.2f", clist2.getFloat(4)));
				}else if(clist2.getString(2).trim().equals("2")){
					temp.put("Type", Record.this.getString(R.string.stockchildsell));
					temp.put("Bsprice", String.format("%.2f", clist2.getFloat(5)));
					int p =Math.round((Float.parseFloat(clist2.getString(5))-Float.parseFloat(clist2.getString(4)))*Float.parseFloat(clist2.getString(3)));
					if(p>0){
					temp.put("Bsprofit", "+"+p);
					}else if (p<0){
					temp.put("Bsprofit", ""+p);	
					}
				}else if(clist2.getString(2).trim().equals("4")){
					temp.put("Type", Record.this.getString(R.string.expanablereduce));
					temp.put("Bsprice", String.format("%.2f", clist2.getFloat(5)));
					Float p =(clist2.getFloat(5)-clist2.getFloat(4))*clist2.getFloat(3);
					temp.put("Bsprofit", String.format("%.2f", p));
				}else {
					temp.put("Type", Record.this.getString(R.string.recodedit));
					temp.put("Bsprice", String.format("%.2f", clist2.getFloat(4)));
				}
				
				temp.put("Bsqty", clist2.getString(3));
				list.add(temp);
			
			}
			clist2.close();
	}
	
	

    private void dellistbox(){
    	
    	final int size = listview.getChildCount();
    	
        if(buttonDellist.getText().toString().trim().equals(Record.this.getString(R.string.recodedit))){
            buttonDellist.setText(R.string.recodfinish);   
            
         	for(int i = 0; i < size; i++) {
          	  ViewGroup gridChild = (ViewGroup) listview.getChildAt(i);
         
          	  ImageView imageViewdelete = (ImageView) gridChild.findViewById(R.id.imageViewdelete);
          	imageViewdelete.setVisibility(View.VISIBLE);    		        		   
          	}
        }else{
      	  buttonDellist.setText(R.string.recodedit);   
      	  
       	for(int i = 0; i < size; i++) {
        	  ViewGroup gridChild = (ViewGroup) listview.getChildAt(i);
       
        	  ImageView imageViewdelete = (ImageView) gridChild.findViewById(R.id.imageViewdelete);
        	  imageViewdelete.setVisibility(View.GONE);    		        		   
        	}
        }
        
      	
    }
	
	@Override
	protected void onDestroy() {
	super.onDestroy();
	if (db != null)
	{
	db.close();
	}
	}
	
}
