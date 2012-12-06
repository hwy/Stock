package com.hwy.stock;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hwy.stock.library.PullToRefreshBase.OnRefreshListener;
import com.hwy.stock.library.PullToRefreshExpandableListView;

public class Main extends ExpandableListActivity  {

	
	//List view
	List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
	List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();

	ExpandableStocklist expandablestocklist;
	 private PullToRefreshExpandableListView mPullRefreshListView;

	 
	String Liststock = "";
	String stocklist;
	final DbHelper helper = new DbHelper(this, "Stock.db", null, 1);	
	private SQLiteDatabase db;
	Thread t;
	 public static final int MEG_INVALIDATE = 1110; //自訂一個號碼
	 Handler mHandler = new Handler();
	 Boolean runbackgroup =true;
	
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		
        if (db==null){
    		 db = helper.getWritableDatabase();
    		}
        
    	ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

  		if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED 
  			    ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {

  			
  			Toast.makeText(Main.this,Main.this.getString(R.string.mainload), Toast.LENGTH_LONG).show();
  		    startloading();

  			}else {
  			
  			Errorbox();

  		}//end check internet

  		
		 new Thread(new Runnable() {
	  	        @Override
	  	        public void run() {
	  	            // TODO Auto-generated method stub
	  	            while (runbackgroup) {
	  	                try {
	  	                    Thread.sleep(3*6*10000);
	  	                    mHandler.post(new Runnable() {

	  	                        @Override
	  	                        public void run() {
	  	                            // TODO Auto-generated method stub
	  	                        	 startloading();
	  	                        }
	  	                    });
	  	                } catch (Exception e) {
	  	                    // TODO: handle exception
	  	                }
	  	            }
	  	        }
	  	    }).start();
  	
			
    }//end create
    
    
    
    
	//Setup expandable list view
	private void Setuplist(){

		//sql
		if (db==null){
    		 db = helper.getWritableDatabase();
    		}

		
		
		String[] stocklistsplit = stocklist.split("\n");
		String[][] stockdetailplit = new String[stocklistsplit.length][];
		for (int i = 0; i < stocklistsplit.length; i++) {  
			stockdetailplit[i]= stocklistsplit[i].split(",");
			}  
	
		
		
   	    	groups.clear();
   	    	childs.clear();


	Cursor clist = db.query("Stock", null,null, null, null, null, "stock_id DESC");
	
	int ttlamount=0;
	int ttlprofit=0;
	int temcount=0;
	for(clist.moveToFirst();!clist.isAfterLast();clist.moveToNext()){


		Map<String, String> group = new HashMap<String, String>();
		group.put("Stocknonname", clist.getString(1)+"\n"+stockdetailplit[temcount][1]);
		group.put("Price", stockdetailplit[temcount][2]);
		group.put("Changeprice", stockdetailplit[temcount][3]);
		group.put("Changepercent", stockdetailplit[temcount][4]);
		int profit = (int) (clist.getFloat(2)*(Float.parseFloat (stockdetailplit[temcount][2])-clist.getFloat(3)));
		group.put("Profit",String.valueOf(profit));
	    Float profitpercent = profit/(clist.getFloat(2)*clist.getFloat(3))*100;
		group.put("profitpercent", String.format("%.2f", profitpercent)+"%");
	    	  
		   	
		groups.add(group);
	
	    List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
		  Map<String, String> child1Data1 = new HashMap<String, String>();
					
			  child1Data1.put("STOCK_ID", clist.getString(0));
			  child1Data1.put("STOCK_NO", clist.getString(1));
			  child1Data1.put("Qty", clist.getString(2));
	    	  child1Data1.put("Date", clist.getString(4));
			  child1Data1.put("OPEN", stockdetailplit[temcount][6]);
	    	  child1Data1.put("YOPEN", stockdetailplit[temcount][8]);
	    	  child1Data1.put("DAYRANGE", stockdetailplit[temcount][6]+"~"+stockdetailplit[temcount][5]);
			  child1Data1.put("Fitytwow", stockdetailplit[temcount][9]);
			  int AMOUNT =  (int) (clist.getFloat(2)*Float.parseFloat(stockdetailplit[temcount][2]));
	    	  child1Data1.put("AMOUNT", String.valueOf(AMOUNT));
	    	  child1Data1.put("BUYPRICE", clist.getString(3));
	    	  child1Data1.put("STOCKQTY", clist.getString(2));
	    	  
	 
	    	  
    	  child1.add(child1Data1);
    	  childs.add(child1);
    	  ttlamount+=AMOUNT;
    	  ttlprofit+=profit;
    	  temcount++;
	}//end for 
	
	
	TextView textViewttamountt = (TextView) findViewById(R.id.textViewttamountt);
	textViewttamountt.setVisibility(View.VISIBLE);
	
	TextView textViewttamount = (TextView) findViewById(R.id.textViewttamount);
	textViewttamount.setVisibility(View.VISIBLE);
	textViewttamount.setText(Integer.toString(ttlamount));

	
	
	TextView textViewttlprofit = (TextView) findViewById(R.id.textViewttlprofit);
	textViewttlprofit.setVisibility(View.VISIBLE);
	textViewttlprofit.setText(Integer.toString(ttlprofit));
	

	if(Integer.toString(ttlprofit).contains("-")){
		textViewttlprofit.setTextColor(Color.RED);	
	}else{
		textViewttlprofit.setTextColor(Color.GREEN);
	}
	TextView textViewloading = (TextView) findViewById(R.id.textViewloading);
	textViewloading.setText(R.string.mainttlprofit);
	
   	ExpandableListView listView = getExpandableListView();
   	listView.setGroupIndicator(null);

       	expandablestocklist = new ExpandableStocklist(this, groups, childs,db);
    
       	
       	mPullRefreshListView = (PullToRefreshExpandableListView) findViewById(R.id.StockExListView);
       	

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

	            

	      		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

	      		if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED 
	      			    ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {

	      		    startloading();
	   
	      			}else {
	      			
	      			Errorbox();
	      		  mPullRefreshListView.onRefreshComplete();

	      		}//end check internet
            }
        });

       	setListAdapter(expandablestocklist);
	
    
	}//end setuplist

	
	
	
	public String loadmultlestock(String stockno) {

        
		String Stocklink="http://hk.finance.yahoo.com/d/quotes.csv?s="+stockno+"&lang=zh-Hant-HK&region=HK&f=snl1c6p2hgopw&d=t";
			//"http://hq.sinajs.cn/list="+localcode+finalstockno;
		
		System.out.println(Stocklink);
		
		 InputStream is = null;
		 String result;
 	    //http post
 	    try{
 	            HttpClient httpclient = new DefaultHttpClient();
 	            HttpPost httppost = new HttpPost(Stocklink);
 	            HttpResponse response = httpclient.execute(httppost);
 	            HttpEntity entity = response.getEntity();
 	            is = entity.getContent();
   
 	    }catch(Exception e){
	    	  System.out.println(e);

 	    	removeDialog(0);
 	    	//Toast.makeText(Main.this, "連線出現問題,請稍後再試...", Toast.LENGTH_LONG).show(); 	
 	            
 	    }

 	    //convert response to string
 	    try{
 	    	removeDialog(0);
 	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
 	            StringBuilder sb = new StringBuilder();
 	            String line = null;
 	            while ((line = reader.readLine()) != null) {
 	                    sb.append(line + "\n");
 	            }
 	            is.close();
 	            result=sb.toString().trim();

 	        //   String regexp = "var.*?=\"";
 	        // String replace = "";
 	        // result=result.replaceAll(regexp, replace);
 	          result= result.replaceAll("\"", "");
 	 	         
			 	System.out.println(result);   
 
 	    }catch(Exception e){
 	    	removeDialog(0);
		 	  result = "error";
 	    	  System.out.println(e);
 	    }
         return result;

		
	}//end load stock
	
	


	
	

    
    //handler msg
    Handler handler = new Handler() 
    {

          public void handleMessage(Message msg) 
    {
     switch (msg.what) 
  {
  case MEG_INVALIDATE:
	  Setuplist();
	  expandablestocklist.notifyDataSetChanged();
	  mPullRefreshListView.onRefreshComplete();
	  break;
 
          }
      super.handleMessage(msg);
          }

      };//end handler msg
    
	
      public void startloading(){
    	  Liststock="";
          Cursor clist = db.query("Stock", null,null, null, null, null, "stock_id DESC");
      	for(clist.moveToFirst();!clist.isAfterLast();clist.moveToNext()){
      		Liststock+=clist.getString(1)+",";
      	}//end if
      	System.out.println(Liststock);
      
      	if(Liststock!=null){
      	t=new Thread() {
               public void run() {
              	 
         		Looper.prepare();  

         	 stocklist =loadmultlestock(Liststock);
         	   if(stocklist!=null&&!stocklist.equals("error")){
         			Message m = new Message();
      	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
      	     		m.what = MEG_INVALIDATE;
      	     		handler.sendMessage(m); 
         	    }else{
         	    	Toast.makeText(Main.this,Main.this.getString(R.string.interneterror), Toast.LENGTH_LONG).show();	
         	    } 
      			Looper.loop();
              	
               }
         };      
         t.start();
      	
  	}//end if

    	}//end start class
 
      
      
      
     // errorbox
	  private void Errorbox() {
	      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	      dialog.setTitle(R.string.mainps);
	      dialog.setMessage(R.string.mainnointernet);
	      dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
	              public void onClick(DialogInterface dialoginterface, int i){

	            	   }
	              });
	      dialog.show();
	} //end errorbox
	  
	  
	  
      
		@Override
		protected void onDestroy() {
		super.onDestroy();
		runbackgroup=false;
		}
}
