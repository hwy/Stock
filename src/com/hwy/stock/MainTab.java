package com.hwy.stock;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.InputType;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainTab extends TabActivity implements OnTabChangeListener {

	//sql
	final DbHelper helper = new DbHelper(this, "Stock.db", null, 1);
	private SQLiteDatabase db;
	
	TabHost tabHost;
	Thread t;
	int local = 0;
	
	Bitmap imagebm;
	 Dialog dialogmsg;
	ProgressDialog dialogp;
	ImageView mainImageView;
	ProgressBar progressbarload;

	int chartrange=1;
	
	String imageurl;
	String stockloaddetail;
	 public static final int MEG_TAGCHANGE = 3330; //自訂一個號碼
	 public static final int MEG_INVALIDATE = 1110; //自訂一個號碼
	 
	//chart text view range
	 TextView textViewchartperiod1d; 
	 TextView textViewchartperiod5d; 
	 TextView textViewchartperiod1m; 
	 TextView textViewchartperiod3m; 
	 TextView textViewchartperiod6m; 
	 TextView textViewchartperiod1y; 
	 TextView textViewchartperiod2y; 
	 TextView textViewchartperiod5y; 
	 TextView textViewchartperiodmy; 
	 
	String stockno;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
  		if (db==null){
    		 db = helper.getWritableDatabase();
    		}
  		
		//prevent thread url error
		StrictMode.ThreadPolicy policy = new StrictMode.
		ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		/** TabHost will have Tabs */
		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		/**
		 * TabSpec used to create a new tab. By using TabSpec only we can able
		 * to setContent to the tab. By using TabSpec setIndicator() we can set
		 * name to tab.
		 */

		/** tid1 is firstTabSpec Id. Its used to access outside. */
		TabSpec MainTabSpec = tabHost.newTabSpec("tad1");
		TabSpec RecordTabSpec = tabHost.newTabSpec("tad2");
		TabSpec IndexTabSpec = tabHost.newTabSpec("tad3");

		/** TabSpec setIndicator() is used to set name for the tab. */
		/** TabSpec setContent() is used to set content for a particular tab. */
		// Intent intent = new Intent(this, SecondActivity.class);
		// intent.put("name", "Something1");
		MainTabSpec.setIndicator(MainTab.this.getString(R.string.maintabprofolio)).setContent(	new Intent(this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		RecordTabSpec.setIndicator(MainTab.this.getString(R.string.maintabrecord)).setContent(	new Intent(this, Record.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		IndexTabSpec.setIndicator(MainTab.this.getString(R.string.maintaballindex)).setContent(new Intent(this, Index.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		
		/** Add tabSpec to the TabHost to display. */
		tabHost.addTab(MainTabSpec);
		tabHost.addTab(RecordTabSpec);
		tabHost.addTab(IndexTabSpec);

		setTabColor(tabHost);
		tabHost.setOnTabChangedListener(this);
/*
		for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 60;
		}
*/
		tabHost.setCurrentTab(0);
		
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

		// spinner notice
		Spinner spinnernotice = (Spinner) findViewById(R.id.spinnerlocation);
		ArrayAdapter<CharSequence> adapternotice = ArrayAdapter.createFromResource(this,R.array.location,android.R.layout.simple_dropdown_item_1line);

		adapternotice.setDropDownViewResource(android.R.layout.simple_list_item_1);

		spinnernotice.setAdapter(adapternotice);

		spinnernotice.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView adapterView,
							View view, int position, long id) {
						// startnotice=position;
						// savesetting();
						TextView tv1 = (TextView) view;
						tv1.setTextColor(Color.WHITE);
						local = position;
						EditText editTextStockno = (EditText) findViewById(R.id.editTextStockno);
						
					if(position==3){
						editTextStockno.setInputType(InputType.TYPE_CLASS_TEXT);
					}else{
						editTextStockno.setInputType(InputType.TYPE_CLASS_PHONE);
					}//set input type
					
					//editTextStockno.requestFocus();		
					}

					public void onNothingSelected(AdapterView arg0) {

					}
				});

		// edit text stock no.
		final EditText editTextStockno = (EditText) findViewById(R.id.editTextStockno);
		editTextStockno.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,	KeyEvent event) {
				
				InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
				imm.hideSoftInputFromWindow(MainTab.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
			
				final String stocknoinput =editTextStockno.getText().toString().trim();
				
				if(stocknoinput!=null&&!stocknoinput.equals("")){

				Toast.makeText(MainTab.this, editTextStockno.getText().toString().trim(),Toast.LENGTH_SHORT).show();
	
				showDialog(0);
				t=new Thread() {
	     	           public void run() {
	     	          	 
	     	     		Looper.prepare();  

	    				stockloaddetail = loadstock(stocknoinput);	
	     	     	    if(stockloaddetail!=null&&!stockloaddetail.equals("error")){
	     	     			Message m = new Message();
		     	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
		     	     		m.what = MEG_INVALIDATE;
		     	     		handler.sendMessage(m);	
	     	     	    }else{
	     	     	    	Toast.makeText(MainTab.this,MainTab.this.getString(R.string.interneterror), Toast.LENGTH_LONG).show();	
	     	     	    }
	     	     	    
	     	   
	    				Looper.loop();
	     	          	
	    				
	    				
	     	           }
	     	     };      
	     	     t.start();
	     	     
				}else{
	      	     	  Toast.makeText(MainTab.this,MainTab.this.getString(R.string.expanableentererror), Toast.LENGTH_LONG).show();
    				}
				return false;
			}
		});
		
		
		

	}// end create

	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		setTabColor(tabHost);
	}

	public static void setTabColor(TabHost tabhost) {
		for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
			tabhost.getTabWidget().getChildAt(i)
					.setBackgroundColor(Color.parseColor("#FFFFFF")); // unselected
		}
		tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab())
				.setBackgroundColor(Color.parseColor("#6699FF")); // selected
	}

	
	
	public String loadstock(String stockno) {
        //nameValuePairs.add(new BasicNameValuePair("Stock", stockno));

		
        String finalstockno;

		String localcode;
		
		  switch(local){
      	case 0:{
      		stockno = stockno.replaceAll("\\D+","");

      		finalstockno = String.format("%04d", Integer.parseInt(stockno));
      		localcode="hk";
      		break;
      	}
      	case 1:{
      		stockno = stockno.replaceAll("\\D+","");

      		finalstockno = String.format("%06d", Integer.parseInt(stockno));
      		localcode="ss";
      		break;
      	}
      	case 2:{
      		stockno = stockno.replaceAll("\\D+","");

      		finalstockno = String.format("%06d", Integer.parseInt(stockno));
      		localcode="sz";
      		break;
      	}
      	case 3:{
      		finalstockno =stockno;
      		localcode="";
      		
      		break;
      	}case 4:{
      		stockno = stockno.replaceAll("\\D+","");

      		finalstockno = String.format("%04d", Integer.parseInt(stockno));
      		localcode="tw";
      		break;
      	}
      	default: 
      		localcode="hk";
      		finalstockno = String.format("%04d", Integer.parseInt(stockno));
		  }
      	
        
		String Stocklink="http://hk.finance.yahoo.com/d/quotes.csv?s="+finalstockno+"."+localcode+"&lang=zh-Hant-HK&region=HK&f=snd1t1l1c6p2hgopvj1rew&d=t";
			//"http://hq.sinajs.cn/list="+localcode+finalstockno;
		
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
 	    	removeDialog(0);
 	    	Toast.makeText(this, MainTab.this.getString(R.string.interneterror), Toast.LENGTH_LONG).show(); 	
 	            
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
 	    	  Toast.makeText(this,MainTab.this.getString(R.string.interneterror), Toast.LENGTH_LONG).show();
		 	  result = "error";
 	    	  System.out.println(e);
 	    }
         return result;

		
	}//end load stock
	
	
	public void showstockdialog (String stockdetail){
		/*
		if(localcode.equals("sh")){
			localcode="ss";
		}else if(localcode.equals("hk")){
			stockdetail=stockdetail.replaceFirst(",", "\n");
		}
		*/
		
		String[] stocksplit = stockdetail.split(",");
			
		
		if(!stocksplit[2].equals("N/A")) {
		dialogmsg = new Dialog(MainTab.this);
		dialogmsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogmsg.setContentView(R.layout.stcokdetail);
		
		
		stockno=stocksplit[0];
		
		// set the custom dialogmsg components - text
		TextView textViewstockname = (TextView) dialogmsg.findViewById(R.id.textViewstockname);
		textViewstockname.setText(stocksplit[0]+"\n"+stocksplit[1]);
		
		//open price
		TextView textViewopenprice = (TextView) dialogmsg.findViewById(R.id.textViewopenprice);
		textViewopenprice.setText(stocksplit[9]);

		
		//change price
		TextView textViewchangeprice = (TextView) dialogmsg.findViewById(R.id.textViewchangeprice);
		textViewchangeprice.setText(stocksplit[5]);

		
		
		//change %
		TextView textViewstockchangepercent = (TextView) dialogmsg.findViewById(R.id.textViewstockchangepercent);
		textViewstockchangepercent.setText(stocksplit[6]);

		
	
		
		//yetserday price
		TextView textViewyopenprice = (TextView) dialogmsg.findViewById(R.id.textViewyopenprice);
		textViewyopenprice.setText(stocksplit[10]);

		
		//current price
		TextView textViewcurrentprice = (TextView) dialogmsg.findViewById(R.id.textViewcurrentprice);
		textViewcurrentprice.setText(stocksplit[4]);

		
		//set rase & drop color
		if(stocksplit[5].contains("+")){
			textViewcurrentprice.setTextColor(Color.GREEN);
			textViewchangeprice.setBackgroundColor(Color.GREEN);
			textViewstockchangepercent.setBackgroundColor(Color.GREEN);
			textViewchangeprice.setTextColor(Color.WHITE);
			textViewstockchangepercent.setTextColor(Color.WHITE);

		}else if(stocksplit[5].contains("-")){
			textViewcurrentprice.setTextColor(Color.RED);
			textViewchangeprice.setBackgroundColor(Color.RED);
			textViewstockchangepercent.setBackgroundColor(Color.RED);
			textViewchangeprice.setTextColor(Color.WHITE);
			textViewstockchangepercent.setTextColor(Color.WHITE);

		}
		
		// high price
		TextView textViewhighprice = (TextView) dialogmsg.findViewById(R.id.textViewhighprice);
		textViewhighprice.setText(stocksplit[7]);

		//low price
		TextView textViewlowprice = (TextView) dialogmsg.findViewById(R.id.textViewlowprice);
		textViewlowprice.setText(stocksplit[8]);
		

		//volume
		TextView textViewttvolume = (TextView) dialogmsg.findViewById(R.id.textViewttvolume);
		textViewttvolume.setText(""+Integer.parseInt(stocksplit[11])/10000+"M");
		
		//market cap
		TextView textViewmarketcap = (TextView) dialogmsg.findViewById(R.id.textViewmarketcap);
		textViewmarketcap.setText(stocksplit[12]);
	
		//last trade date
		TextView textViewdate = (TextView) dialogmsg.findViewById(R.id.textViewdate);
		textViewdate.setText(stocksplit[2]);

		SimpleDateFormat  df=new SimpleDateFormat("h:mma Z");

		   try {
			   Date dt=df.parse(stocksplit[3]+" -0500");

			   df.setTimeZone(TimeZone.getDefault());
				//time
				TextView textViewtime = (TextView) dialogmsg.findViewById(R.id.textViewtime);
				textViewtime.setText(df.format(dt).toString().substring(0,7));

		   }  catch( Exception e ) {
				TextView textViewtime = (TextView) dialogmsg.findViewById(R.id.textViewtime);
				textViewtime.setText(stocksplit[3]);
               e.printStackTrace();
           }
		
		

		
		//pe
		TextView textViewpe = (TextView) dialogmsg.findViewById(R.id.textViewpe);
		textViewpe.setText(stocksplit[13]);

		
		//Profit
		TextView textViewprofit = (TextView) dialogmsg.findViewById(R.id.textViewprofit);
		textViewprofit.setText(stocksplit[14]);

		
		//52 week
		TextView textView52hl = (TextView) dialogmsg.findViewById(R.id.textView52hl);
		textView52hl.setText(stocksplit[15]);
	
		
		//1 day chart
		textViewchartperiod1d = (TextView) dialogmsg.findViewById(R.id.textViewchartperiod1d);
		textViewchartperiod1d.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=1;
		    	threadload();
		    }
		});	
		
		//5 day chart
		textViewchartperiod5d = (TextView) dialogmsg.findViewById(R.id.textViewchartperiod5d);
		textViewchartperiod5d.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=2;
		    	threadload();
		    }
		});	
		
		//1 month chart
		textViewchartperiod1m = (TextView) dialogmsg.findViewById(R.id.textViewchartperiod1m);
		textViewchartperiod1m.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=3;
		    	threadload();
		    }
		});	
		
		//3 month chart
		textViewchartperiod3m = (TextView) dialogmsg.findViewById(R.id.textViewchartperiod3m);
		textViewchartperiod3m.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=4;
		    	threadload();
		    }
		});	
		
		//6month chart
		textViewchartperiod6m = (TextView) dialogmsg.findViewById(R.id.textViewchartperiod6m);
		textViewchartperiod6m.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=5;
		    	threadload();
		    }
		});	
		
		//1 year chart
		textViewchartperiod1y = (TextView) dialogmsg.findViewById(R.id.textViewchartperiod1y);
		textViewchartperiod1y.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=6;
		    	threadload();
		    }
		});	
		
		//2 year chart
		textViewchartperiod2y = (TextView) dialogmsg.findViewById(R.id.textViewchartperiod2y);
		textViewchartperiod2y.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=7;
		    	threadload();
		    }
		});	
		
		//5 year chart
		textViewchartperiod5y = (TextView) dialogmsg.findViewById(R.id.textViewchartperiod5y);
		textViewchartperiod5y.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=8;
		    	threadload();
		    }
		});	
		
		//Large chart
		textViewchartperiodmy = (TextView) dialogmsg.findViewById(R.id.textViewchartperiodmy);
		textViewchartperiodmy.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=9;
		    	threadload();
		    }
		});
    	  
		 mainImageView = (ImageView) dialogmsg.findViewById(R.id.imageViewchart);
		 progressbarload = (ProgressBar) dialogmsg.findViewById(R.id.progressBarload);

				 
  	     // imageurl = "http://chart.finance.yahoo.com/t?s="+stocksplit[0];
  	      //thread & set link
  	      threadload();
  	  

  	      
	     ImageView imageViewclose = (ImageView) dialogmsg.findViewById(R.id.imageViewclose);
		// if button is clicked, close the custom dialog

	     imageViewclose.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {

        	     dialogmsg.dismiss();

             }
         });//End img
	     
	     
	     Button buttonstockadd =(Button) dialogmsg.findViewById(R.id.buttonstockadd);  
	       
	     buttonstockadd.setOnClickListener(new Button.OnClickListener()  
	     {  
	       @Override 
	       public void onClick(View v)  
	       {  
	    	   
	           if (db==null){
	      		 db = helper.getWritableDatabase();
	      		}
	           
	         // TODO Auto-generated method stub  
				ContentValues cv = new ContentValues();

				EditText editTextqty = (EditText) dialogmsg.findViewById(R.id.editTextqty);
				EditText editTextstockprice = (EditText) dialogmsg.findViewById(R.id.editTextstockprice);

				String stockqty="0";
				String stockprice="0";
				if(!editTextqty.getText().toString().trim().equals("")&&!editTextstockprice.getText().toString().trim().equals("")){
					stockqty=editTextqty.getText().toString().trim();
					stockprice=editTextstockprice.getText().toString().trim();
				}
				
				Time date = new Time(Time.getCurrentTimezone());
				date.setToNow();
				
				int datem=date.month+1;
				String datenow=date.year+"-"+datem+"-"+date.monthDay;
								

				cv.put("stock_no", stockno);
				cv.put("qty", stockqty);
				cv.put("price", stockprice);
				cv.put("date", datenow);
           	   
				//添加方法
				db.insert("Stock", "", cv);
				cv.clear();
				
								
				cv.put("stock_no", stockno);
				cv.put("bsqty", stockqty);
				cv.put("bprice", stockprice);
				cv.put("bstype", "1");  //1 =buy and 2=sell
				cv.put("bsdate", datenow);
				db.insert("Record", "", cv);
				
				tabHost.setCurrentTab(1);
			    tabHost.setCurrentTab(0);

		
			    dialogmsg.dismiss();
				
				
				
				
	       }  
	     });  
	     
	     
		dialogmsg.show();
		
		}else {
			 Toast.makeText(MainTab.this,MainTab.this.getString(R.string.maintabnocode), Toast.LENGTH_LONG).show();
			 }
		
		
	}//end msg dialog
	
	
	
	
	  //loading box
    @Override
    protected Dialog onCreateDialog(int id) {
          switch (id) {
                case 0: {
                      dialogp = new ProgressDialog(this);
                      dialogp.setMessage(MainTab.this.getString(R.string.maintablinking));
                      dialogp.setIndeterminate(true);
                      dialogp.setCancelable(true);
                      return dialogp;
                }
          }
          return null;
    }
    
    

      
      
    //load imgae
    private Bitmap getBitmapFromUrl(String imgUrl) {
        URL url;
        Bitmap bitmap = null;
        try {
                url = new URL(imgUrl);
                InputStream is = url.openConnection().getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bitmap = BitmapFactory.decodeStream(bis);
                bis.close();
        } catch (MalformedURLException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return bitmap;
}

    
    
    
    
    
    
    //handler msg
    Handler handler = new Handler() 
    {

          public void handleMessage(Message msg) 
    {
     switch (msg.what) 
  {
  case MEG_INVALIDATE:
	  showstockdialog(stockloaddetail);
      break;
  case MEG_TAGCHANGE:
  	    progressbarload.setVisibility(View.INVISIBLE);
 	   	mainImageView.setImageBitmap(imagebm);
 	   
	   	mainImageView.setVisibility(View.VISIBLE);
		 mainImageView.setOnTouchListener(new OnTouchListener()
	        {

	            @Override
	            public boolean onTouch(View v, MotionEvent event)
	            {
	            	
	            	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	            		int x = (int) event.getX();
	            		
	            		if(x>=mainImageView.getWidth()/2){
	            			
	            			chartrange++;
	            			if(chartrange>9){
	            				chartrange=1;
	            			}
	            			
	            			threadload();
	            			
	            			
	            		}else{
	            			
	            			chartrange--;
	            			if(chartrange<=0){
	            				chartrange=9;}
	            			
	            		}
	            		threadload();
	            		
	            	}
	                return false;
	            }
	       });
		 
  break;
          }
      super.handleMessage(msg);
          }

      };//end handler msg
    
    
       // thread & image link
      private void threadload(){
    	   
          switch (chartrange) {  
  
          case 1:
            imageurl="http://chart.finance.yahoo.com/t?s="+stockno;
      		textViewchartperiod1d.setBackgroundColor(Color.CYAN);
      		textViewchartperiod5d.setBackgroundColor(Color.WHITE); 
      		textViewchartperiod1m.setBackgroundColor(Color.WHITE); 
      		textViewchartperiod3m.setBackgroundColor(Color.WHITE); 
      		textViewchartperiod6m.setBackgroundColor(Color.WHITE); 
      		textViewchartperiod1y.setBackgroundColor(Color.WHITE); 
      		textViewchartperiod2y.setBackgroundColor(Color.WHITE); 
      		textViewchartperiod5y.setBackgroundColor(Color.WHITE); 
      		textViewchartperiodmy.setBackgroundColor(Color.WHITE); 
      	    progressbarload.setVisibility(View.VISIBLE);
    	   	mainImageView.setVisibility(View.INVISIBLE);
    	   	break;  
          case 2:
        	  imageurl="http://ichart.yahoo.com/v?s="+stockno+"&t=5d";
        		textViewchartperiod1d.setBackgroundColor(Color.WHITE);
          		textViewchartperiod5d.setBackgroundColor(Color.CYAN); 
          		textViewchartperiod1m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod3m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod6m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod2y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod5y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiodmy.setBackgroundColor(Color.WHITE); 
          	    progressbarload.setVisibility(View.VISIBLE);
        	   	mainImageView.setVisibility(View.INVISIBLE);
        	   	break;  
          case 3:
        	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=1m";
        		textViewchartperiod1d.setBackgroundColor(Color.WHITE);
          		textViewchartperiod5d.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1m.setBackgroundColor(Color.CYAN); 
          		textViewchartperiod3m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod6m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod2y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod5y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiodmy.setBackgroundColor(Color.WHITE); 
          	    progressbarload.setVisibility(View.VISIBLE);
        	   	mainImageView.setVisibility(View.INVISIBLE);
        	   	break;  
          case 4:
        	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=3m";
        		textViewchartperiod1d.setBackgroundColor(Color.WHITE);
          		textViewchartperiod5d.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod3m.setBackgroundColor(Color.CYAN); 
          		textViewchartperiod6m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod2y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod5y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiodmy.setBackgroundColor(Color.WHITE); 
          	    progressbarload.setVisibility(View.VISIBLE);
        	   	mainImageView.setVisibility(View.INVISIBLE);
        	   	 break;  
          case 5:
        	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=6m";
        		textViewchartperiod1d.setBackgroundColor(Color.WHITE);
          		textViewchartperiod5d.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod3m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod6m.setBackgroundColor(Color.CYAN); 
          		textViewchartperiod1y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod2y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod5y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiodmy.setBackgroundColor(Color.WHITE); 
          	    progressbarload.setVisibility(View.VISIBLE);
        	   	mainImageView.setVisibility(View.INVISIBLE);
        	   	break;  
          case 6:
        	  imageurl="http://ichart.finance.yahoo.com/c/bb/m/"+stockno;
        		textViewchartperiod1d.setBackgroundColor(Color.WHITE);
          		textViewchartperiod5d.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod3m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod6m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1y.setBackgroundColor(Color.CYAN); 
          		textViewchartperiod2y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod5y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiodmy.setBackgroundColor(Color.WHITE); 
          	    progressbarload.setVisibility(View.VISIBLE);
        	   	mainImageView.setVisibility(View.INVISIBLE);
        	   	break;  
          case 7:
        	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=2y";
        		textViewchartperiod1d.setBackgroundColor(Color.WHITE);
          		textViewchartperiod5d.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod3m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod6m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod2y.setBackgroundColor(Color.CYAN); 
          		textViewchartperiod5y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiodmy.setBackgroundColor(Color.WHITE); 
          	    progressbarload.setVisibility(View.VISIBLE);
        	   	mainImageView.setVisibility(View.INVISIBLE);
        	   	break;  
          case 8:
        	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=5y";
        		textViewchartperiod1d.setBackgroundColor(Color.WHITE);
          		textViewchartperiod5d.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod3m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod6m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod2y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod5y.setBackgroundColor(Color.CYAN); 
          		textViewchartperiodmy.setBackgroundColor(Color.WHITE); 
          	    progressbarload.setVisibility(View.VISIBLE);
        	   	mainImageView.setVisibility(View.INVISIBLE);
        	   	break;  
          case 9:
        	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=my";
        		textViewchartperiod1d.setBackgroundColor(Color.WHITE);
          		textViewchartperiod5d.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod3m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod6m.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod1y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod2y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiod5y.setBackgroundColor(Color.WHITE); 
          		textViewchartperiodmy.setBackgroundColor(Color.CYAN); 
          	    progressbarload.setVisibility(View.VISIBLE);
        	   	mainImageView.setVisibility(View.INVISIBLE);
        	   	break;  
              
          default:  
              break;  
          }  
    	  
   		 t=new Thread() {
 	           public void run() {
 	          	 
 	     		Looper.prepare();  
 	     		 imagebm =getBitmapFromUrl(imageurl);
 	  			 
 	  	   	    if(imagebm!=null){

 	 			Message m = new Message();
 	 	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
 	 	     		m.what = MEG_TAGCHANGE;
 	 	     		handler.sendMessage(m);		   	
 	  	   	    }else{
 	  	   	    Toast.makeText(MainTab.this,MainTab.this.getString(R.string.interneterror), Toast.LENGTH_LONG).show();
 	  	   	    }
 				Looper.loop();
 	          	
 	           }
 	     };      
 	     t.start();

   	   
      }//end halder msg
      

      
}//end class


