package com.hwy.stock;

import java.io.BufferedReader;
import java.io.IOException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	JSONObject stocklist;
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
  
	
		
		
   	    	groups.clear();
   	    	childs.clear();


	Cursor clist = db.query("Stock", null,null, null, null, null, "stock_id DESC");
	
	int ttlamount=0;
	int ttlprofit=0;
	int temcount=0;
	for(clist.moveToFirst();!clist.isAfterLast();clist.moveToNext()){

        
        try{
        	
        JSONObject jsonObj = stocklist.getJSONObject(clist.getString(1).toUpperCase()); 
        
		Map<String, String> group = new HashMap<String, String>();
		group.put("Stocknonname", jsonObj.getString("stockno")+"\n"+jsonObj.getString("name"));
		group.put("Price", jsonObj.getString("price"));
		group.put("Changeprice", jsonObj.getString("change"));
		group.put("Changepercent", jsonObj.getString("changepercent"));
		if(Float.parseFloat(jsonObj.getString("price"))>Float.parseFloat(jsonObj.getString("preprice"))){
		group.put("greenredcolor", "+");
		}else if(Float.parseFloat(jsonObj.getString("price"))<Float.parseFloat(jsonObj.getString("preprice"))){
		group.put("greenredcolor", "-");
		}else{group.put("greenredcolor", "");}
		
		int profit = (int) (clist.getFloat(2)*(Float.parseFloat (jsonObj.getString("price"))-clist.getFloat(3)));
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
			  child1Data1.put("OPEN", jsonObj.getString("openprice"));
	    	  child1Data1.put("YOPEN", jsonObj.getString("preprice"));
	    	  child1Data1.put("DAYRANGE", jsonObj.getString("daylow")+"~"+jsonObj.getString("dayhig"));
			  child1Data1.put("Fitytwow", jsonObj.getString("52W"));
			  int AMOUNT =  (int) (clist.getFloat(2)*Float.parseFloat(jsonObj.getString("price")));
	    	  child1Data1.put("AMOUNT", String.valueOf(AMOUNT));
	    	  child1Data1.put("BUYPRICE", clist.getString(3));
	    	  child1Data1.put("STOCKQTY", clist.getString(2));
	    	  
	 
	    	  
    	  child1.add(child1Data1);
    	  childs.add(child1);
    	  ttlamount+=AMOUNT;
    	  ttlprofit+=profit;
    	  temcount++;
        } catch (Exception e) {
                e.printStackTrace();
        }
        
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
	
       	clist.close();
	}//end setuplist

	
	
	
	public JSONObject loadmultlestock(String stockno) {

		String Stocklink="http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20html%20where%20url%3D%22http%3A%2F%2Fhk.finance.yahoo.com%2Fquotes%2F"+stockno+"%2Fview%2Fdv%22%20and%20xpath%3D'%2F%2Fdiv%5B%40id%3D%22yfi_summary_table_container%22%5D%2F%2Fh2%7C%2F%2Fdiv%5B%40class%3D%22hd%20first%22%5D%2Fp%2Fa%7C%2F%2Fdiv%5B%40class%3D%22table%22%5D%2F%2Ftable%2Ftr%2Ftd%5Bnot(contains(.%2C%5C'x%5C'))%5D%2F%2Ftext()'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		//String Stocklink="http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20html%20where%20url%20in%20("+stockno+")%20and%20xpath%3D'%2F%2Fdiv%5B%40class%3D%22title%22%5D%2F%2Fh2%7C%2F%2Fdiv%5B%40class%3D%22yfi_rt_quote_summary_rt_top%22%5D%2F%2Fspan%2F%2Ftext()%7C%2F%2Fdiv%5B%40id%3D%22yfi_quote_summary_data%22%5D%2F%2Ftd%2F%2Ftext()'&format=json";
        //String Stocklink="http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20html%20where%20url%20in%20("+stockno+")%20and%20xpath%3D'%2F%2Fdiv%5B%40class%3D%22title%22%5D%2F%2Fh2%7C%2F%2Fdiv%5B%40class%3D%22yfi_rt_quote_summary_rt_top%22%5D%2F%2Fspan%2F%2Ftext()%7C%2F%2Fdiv%5B%40id%3D%22yfi_quote_summary_data%22%5D%2F%2Ftd%2F%2Ftext()'&format=json";
		//String Stocklink="http://hk.finance.yahoo.com/d/quotes.csv?s="+stockno+"&lang=zh-Hant-HK&region=HK&f=snl1c6p2hgopw&d=t";
			//"http://hq.sinajs.cn/list="+localcode+finalstockno;
		
         JSONObject Stockdetailjo = new JSONObject();  

		
		
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
		    	

 	           try { 
			    	
			    	JSONObject jsonResponse = new JSONObject(result).getJSONObject("query");
			    	JSONObject returnresult = jsonResponse.getJSONObject("results");
			    	System.out.println("returnresult"+returnresult);
			    	
			       	String arr = returnresult.getString("h2");
			       	
			       	System.out.print(returnresult.getString("h2")+"ccccccccccccccc");

			       	if( returnresult.getString("h2").substring(0, 1).equals("[")){
			    	//getString("h2").replaceAll(" \\(", "XX").replaceAll("\\)","").replaceAll("\"","").replaceAll("\\[","").replaceAll("\\]","").replaceAll("\\.,",".").replaceAll("\\p{P}", " ");
			    		System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");

			    		JSONArray retem = returnresult.getJSONArray("h2");
			    		
			    	  	JSONArray restnotemarray = returnresult.getJSONArray("a");
				    	
				    	result = returnresult.getString("content");
				    	result= result.replaceAll(" ", "").replaceAll("%\\)\\n\\n", "%)XX").replaceAll("\\)\\n\\n", ")XY").replaceAll("\\n+","XX").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("不適用不適用","不適用XX不適用").replaceAll("不適用-不適用","不適用XX-XX不適用");
				    	result=result.substring(2, result.length() - 2);
		 	   		 


			 	   		String[] stocklistsplit = result.split("XY");
			 	   		
			 	   		System.out.println(result+"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
			 	   		System.out.println(retem+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			 	   		
		 	   		String[][] stockdetailplit = new String[stocklistsplit.length][];
		 	   		for (int i = 0; i < stocklistsplit.length; i++) {  
		 	   			stockdetailplit[i]= stocklistsplit[i].split("XX");
		 	   			
		 	   			JSONObject StockOBJ = new JSONObject();  
		 	   		    
		 	   		   JSONObject temjsonarray = restnotemarray.getJSONObject(i);
		 	   		    
		 	   		    StockOBJ.put("name",retem.get(i));
		 	   		    StockOBJ.put("stockno" , temjsonarray.getString("content"));
		 	   		    StockOBJ.put("price" , stockdetailplit[i][0]);
		 	   		    StockOBJ.put("change" , stockdetailplit[i][6]);
		 	   		    StockOBJ.put("changepercent" , stockdetailplit[i][7]);
		 	   		    StockOBJ.put("date" , stockdetailplit[i][4]);
		 	   		    StockOBJ.put("preprice" , stockdetailplit[i][9]);
		 	   		    StockOBJ.put("openprice" , stockdetailplit[i][11]);
		 	   		    StockOBJ.put("daylow" , stockdetailplit[i][1]);
		 	   		    StockOBJ.put("dayhig" , stockdetailplit[i][3]);
		 	   		    StockOBJ.put("52W" , stockdetailplit[i][5]);
		 	   		    StockOBJ.put("volume" , stockdetailplit[i][8]);
		 	   		    StockOBJ.put("marketcap" , stockdetailplit[i][12]);
		 	   		    StockOBJ.put("pe" , stockdetailplit[i][13]);
		 	   		    StockOBJ.put("profit" , stockdetailplit[i][14]);

		 	   		    System.out.println(temjsonarray.getString("content"));
		 	   		    Stockdetailjo.put(temjsonarray.getString("content"), StockOBJ);  
		 	   			}
			    	} else{
			    		
			    		
			    		System.out.println("GGggggggggggggggggggggggggggggggggggggggggggggggggggggGGGGGGGGG");

			    		String retem = returnresult.getString("h2");
			    		
			    		JSONObject restnotemObj = returnresult.getJSONObject("a");
				    	
				    	result = returnresult.getString("content");
				    	result= result.replaceAll(" ", "").replaceAll("%\\)\\n\\n", "%)XX").replaceAll("\\)\\n\\n", ")XY").replaceAll("\\n+","XX").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("不適用不適用","不適用XX不適用").replaceAll("不適用-不適用","不適用XX-XX不適用");
				    	result=result.substring(2, result.length() - 2);
		 	   		 
			 	   		
			 	   		System.out.println(result+"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
			 	   		System.out.println(retem+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		 	   		    
			 	   		JSONObject StockOBJ = new JSONObject();  
			 	   		
			 			String[] stocksplit = result.split("XX");


		 	   		    StockOBJ.put("name",retem);
		 	   		    StockOBJ.put("stockno" , restnotemObj.getString("content"));
		 	   		    StockOBJ.put("price" , stocksplit[0]);
		 	   		    StockOBJ.put("change" , stocksplit[6]);
		 	   		    StockOBJ.put("changepercent" , stocksplit[7]);
		 	   		    StockOBJ.put("date" , stocksplit[4]);
		 	   		    StockOBJ.put("preprice" , stocksplit[9]);
		 	   		    StockOBJ.put("openprice" , stocksplit[11]);
		 	   		    StockOBJ.put("daylow" , stocksplit[1]);
		 	   		    StockOBJ.put("dayhig" , stocksplit[3]);
		 	   		    StockOBJ.put("52W" , stocksplit[5]);
		 	   		    StockOBJ.put("volume" , stocksplit[8]);
		 	   		    StockOBJ.put("marketcap" , stocksplit[12]);
		 	   		    StockOBJ.put("pe" , stocksplit[13]);
		 	   		    StockOBJ.put("profit" , stocksplit[14]);

		 	   		    System.out.println(restnotemObj.getString("content"));
		 	   		    Stockdetailjo.put(restnotemObj.getString("content"), StockOBJ);  
		 	   						    	
			    	}
			    	
			  
	 	   		
		        } catch (JSONException e) {   
		            System.out.println("Json parse error"+e);   
		            e.printStackTrace();   
		        }  
		        
		        
			 	System.out.println(result);   
 
 	    }catch(Exception e){
 	    	removeDialog(0);
 	    	  System.out.println(e);
 	    }
         return Stockdetailjo;

		
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
      		String temur =clist.getString(1)+",";
      		Liststock+=temur+",";
      	}//end if

   
      
      	if(Liststock!=null&&Liststock.length()>1){
      		
          	Liststock = Liststock.substring(0, Liststock.length() - 1);

      	t=new Thread() {
               public void run() {
              	 
         		Looper.prepare();  

         		
         	 stocklist =loadmultlestock(Liststock);
         			Message m = new Message();
      	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
      	     		m.what = MEG_INVALIDATE;
      	     		handler.sendMessage(m); 
  
      			Looper.loop();
              	
               }
         };      
         t.start();
      	
  	}else{
  		
  		TextView textViewloading = (TextView) findViewById(R.id.textViewloading);
  		textViewloading.setText("");
  	}//end if
      	clist.close();
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
		
		@Override
		protected void onPause() {
		super.onPause();
		runbackgroup=false;
		}
		
		@Override
		protected void onResume() {
		super.onResume();
		runbackgroup=true;
		}
		
}
