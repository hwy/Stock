package com.hwy.stock;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Index extends Activity  {

	public static final int MEG_INVALIDATE = 3339;
	public static final int MEG_INVALIDATE2 = 3355;

	 public static final int MEG_TAGCHANGE = 3330; //自訂一個號碼
	 String stocklist;
	 Bitmap imagebm;
	 String  stockno ="%5eHSI";
	ImageView mainImageViewindex;
	ProgressBar progressbarloadindex;
	String imageurl;
	Thread t4;
	String stlist;
	int chartrange=1;
	 String  dowlist;
	 
	Button buttonhk; 
	Button buttoncn;
	Button buttonus;
	 
	 
	 Thread t;	
	 TextView textViewchartperiod1dindex; 
	 TextView textViewchartperiod5dindex; 
	 TextView textViewchartperiod1mindex; 
	 TextView textViewchartperiod3mindex; 
	 TextView textViewchartperiod6mindex; 
	 TextView textViewchartperiod1yindex; 
	 TextView textViewchartperiod2yindex; 
	 TextView textViewchartperiod5yindex; 
	 TextView textViewchartperiodmyindex; 
	 TextView textViewindexname;
	 TextView textViewhsiindex;
	 TextView textViewindexchange; 
	 // 2

	 Thread t2;
	 public static final int MEG_TAGCHANGE2 = 3332; //自訂一個號碼
	 Bitmap imagebm2;
	 String  stockno2 ="%5eHSCE";
		ImageView mainImageViewindex2;
		ProgressBar progressbarloadindex2;
		String imageurl2;

		int chartrange2=1;
		
		//chart text view range
	 TextView textViewchartperiod1dindex2; 
	 TextView textViewchartperiod5dindex2; 
	 TextView textViewchartperiod1mindex2; 
	 TextView textViewchartperiod3mindex2; 
	 TextView textViewchartperiod6mindex2; 
	 TextView textViewchartperiod1yindex2; 
	 TextView textViewchartperiod2yindex2; 
	 TextView textViewchartperiod5yindex2; 
	 TextView textViewchartperiodmyindex2; 
	 TextView textViewindexname2;
	 TextView textViewhsiindex2;
	 TextView textViewindexchange2;
	 // 3

	 Thread t3;
	 public static final int MEG_TAGCHANGE3 = 3333; //自訂一個號碼
	  
	 Bitmap imagebm3;
	 String  stockno3 ="%5eHSCC";
		ImageView mainImageViewindex3;
		ProgressBar progressbarloadindex3;
		String imageurl3;

		int chartrange3=1;
		
		//chart text view range
	 TextView textViewchartperiod1dindex3; 
	 TextView textViewchartperiod5dindex3; 
	 TextView textViewchartperiod1mindex3; 
	 TextView textViewchartperiod3mindex3; 
	 TextView textViewchartperiod6mindex3; 
	 TextView textViewchartperiod1yindex3; 
	 TextView textViewchartperiod2yindex3; 
	 TextView textViewchartperiod5yindex3; 
	 TextView textViewchartperiodmyindex3; 
	 TextView textViewindexname3;
	 TextView textViewhsiindex3;
	 TextView textViewindexchange3;
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		
 	
		textViewindexname  = (TextView) findViewById(R.id.textViewindexname);
		textViewindexname2  = (TextView) findViewById(R.id.textViewindexname2);
		textViewindexname3  = (TextView) findViewById(R.id.textViewindexname3);
	    
		buttonhk =(Button) findViewById(R.id.buttonhk);  
		buttoncn =(Button) findViewById(R.id.buttoncn);  
		buttonus =(Button) findViewById(R.id.buttonus);  
		
		
		textViewhsiindex  = (TextView) findViewById(R.id.textViewhsiindex);
		textViewindexchange  = (TextView) findViewById(R.id.textViewindexchange);

		textViewhsiindex2  = (TextView) findViewById(R.id.textViewhsiindex2);
		textViewindexchange2  = (TextView) findViewById(R.id.textViewindexchange2);

		textViewhsiindex3  = (TextView) findViewById(R.id.textViewhsiindex3);
		textViewindexchange3  = (TextView) findViewById(R.id.textViewindexchange3);

		
		
		  stlist = stockno+","+stockno2+","+stockno3;
		

      	t4=new Thread() {
               public void run() {
              	 
         		Looper.prepare();  
         		stocklist = loadmultlestock(stlist);
         		 if(stocklist!=null&&!stocklist.equals("error")){
          			Message m = new Message();
       	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
       	     		m.what = MEG_INVALIDATE;
       	     		handler.sendMessage(m); 
          	    }
         		
      			Looper.loop();
              	
               }
         };      
         t4.start();
		
         
         
         


	     buttonhk.setOnClickListener(new Button.OnClickListener()  
	     {  
	       @Override 
	       public void onClick(View v)  
	       {  
	    	   stockno="%5eHSI";
	    	   stockno2="%5eHSCE";
	    	   stockno3="%5eHSCC";
	    	   stlist = stockno+","+stockno2+","+stockno3;
	    	   
	    	   textViewindexname.setText(R.string.indexhs);
	    	   textViewindexname2.setText(R.string.indexchina);
	    	   textViewindexname3.setText(R.string.indexred);
	    	   threadload();
	    	   threadload2();
	    	   threadload3();
	    	   
	    	   buttonhk.setBackgroundResource(R.drawable.yellowbtn);
	    	   buttoncn.setBackgroundResource(R.drawable.bluebtn);
	    	   buttonus.setBackgroundResource(R.drawable.bluebtn);
	    	   
		   		textViewhsiindex.setText(R.string.indexload);
				textViewindexchange.setText(".......");
			
		   		textViewhsiindex2.setText(R.string.indexload);
				textViewindexchange2.setText(".......");
			
		   		textViewhsiindex3.setText(R.string.indexload);
				textViewindexchange3.setText(".......");
			
	    	   t4=new Thread() {
	                public void run() {
	               	 
	          		Looper.prepare(); 
	          		
	          		
	          		stocklist = loadmultlestock(stlist);
	          		 if(stocklist!=null&&!stocklist.equals("error")){
	           			Message m = new Message();
	        	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
	        	     		m.what = MEG_INVALIDATE;
	        	     		handler.sendMessage(m); 
	           	    }
	          		
	       			Looper.loop();
	               	
	                }
	          };      
	          t4.start();
	       }
	     });
         
	     
	     
	       
	     buttoncn.setOnClickListener(new Button.OnClickListener()  
	     {  
	       @Override 
	       public void onClick(View v)  
	       {  
	    	   stockno="000001.SS";
	    	   stockno2="399001.SZ";
	    	   stockno3="000300.SS";
	    	   stlist = stockno+","+stockno2+","+stockno3;
	    	   
	    	   textViewindexname.setText(R.string.indexszup);
	    	   textViewindexname2.setText(R.string.indexszs);
	    	   textViewindexname3.setText(R.string.indexszy);
	    	   
	    	   
	    	   buttoncn.setBackgroundResource(R.drawable.yellowbtn);
	    	   buttonhk.setBackgroundResource(R.drawable.bluebtn);
	    	   buttonus.setBackgroundResource(R.drawable.bluebtn);
	    	   
		   		textViewhsiindex.setText(R.string.indexload);
				textViewindexchange.setText(".......");
			
		   		textViewhsiindex2.setText(R.string.indexload);
				textViewindexchange2.setText(".......");
			
		   		textViewhsiindex3.setText(R.string.indexload);
				textViewindexchange3.setText(".......");
				
	    	   threadload();
	    	   threadload2();
	    	   threadload3();
	    	   t4=new Thread() {
	                public void run() {
	               	 
	          		Looper.prepare(); 
	          		
	          		
	          		stocklist = loadmultlestock(stlist);
	          		 if(stocklist!=null&&!stocklist.equals("error")){
	           			Message m = new Message();
	        	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
	        	     		m.what = MEG_INVALIDATE;
	        	     		handler.sendMessage(m); 
	           	    }
	          		
	       			Looper.loop();
	               	
	                }
	          };      
	          t4.start();
	       }
	     });
	     
	    
	       
	     buttonus.setOnClickListener(new Button.OnClickListener()  
	     {  
	       @Override 
	       public void onClick(View v)  
	       {  
	    	   stockno="%5EDJI";
	    	   stockno2="%5eIXIC";
	    	   stockno3="%5eGSPC";
	    	   stlist = stockno2+","+stockno3;
	    	   
	    	   textViewindexname.setText(R.string.indexdow);
	    	   textViewindexname2.setText(R.string.indexnaq);
	    	   textViewindexname3.setText(R.string.indexnor);
	    	   
	    	   buttonus.setBackgroundResource(R.drawable.yellowbtn);
	    	   buttoncn.setBackgroundResource(R.drawable.bluebtn);
	    	   buttonhk.setBackgroundResource(R.drawable.bluebtn);
	    	   
		   		textViewhsiindex.setText(R.string.indexload);
				textViewindexchange.setText(".......");
			
		   		textViewhsiindex2.setText(R.string.indexload);
				textViewindexchange2.setText(".......");
			
		   		textViewhsiindex3.setText(R.string.indexload);
				textViewindexchange3.setText(".......");
				
	    	   threadload();
	    	   threadload2();
	    	   threadload3();
	    	   t4=new Thread() {
	                public void run() {
	               	 
	          		Looper.prepare(); 
	          		
	          		
	          		stocklist = loadmultlestock(stlist);
	          		 dowlist = loadsina();
	          		 if(stocklist!=null&&!stocklist.equals("error")){
	           			Message m = new Message();
	        	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
	        	     		m.what = MEG_INVALIDATE2;
	        	     		handler.sendMessage(m); 
	           	    }
	          		
	       			Looper.loop();
	               	
	                }
	          };      
	          t4.start();
	       }
	     });
		//1 day chart
		textViewchartperiod1dindex  = (TextView) findViewById(R.id.textViewchartperiod1dindex);
		textViewchartperiod1dindex.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=1;
		    	threadload();
		    }
		});	
		
		//5 day chart
		textViewchartperiod5dindex  = (TextView) findViewById(R.id.textViewchartperiod5dindex);
		textViewchartperiod5dindex.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=2;
		    	threadload();
		    }
		});	
		
		//1 month chart
		textViewchartperiod1mindex  = (TextView) findViewById(R.id.textViewchartperiod1mindex);
		textViewchartperiod1mindex.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=3;
		    	threadload();
		    }
		});	
		
		//3 month chart
		textViewchartperiod3mindex  = (TextView) findViewById(R.id.textViewchartperiod3mindex);
		textViewchartperiod3mindex.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=4;
		    	threadload();
		    }
		});	
		
		//6month chart
		textViewchartperiod6mindex  = (TextView) findViewById(R.id.textViewchartperiod6mindex);
		textViewchartperiod6mindex.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=5;
		    	threadload();
		    }
		});	
		
		//1 year chart
		textViewchartperiod1yindex  = (TextView) findViewById(R.id.textViewchartperiod1yindex);
		textViewchartperiod1yindex.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=6;
		    	threadload();
		    }
		});	
		
		//2 year chart
		textViewchartperiod2yindex  = (TextView) findViewById(R.id.textViewchartperiod2yindex);
		textViewchartperiod2yindex.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=7;
		    	threadload();
		    }
		});	
		
		//5 year chart
		textViewchartperiod5yindex  = (TextView) findViewById(R.id.textViewchartperiod5yindex);
		textViewchartperiod5yindex.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=8;
		    	threadload();
		    }
		});	
		
		//Large chart
		textViewchartperiodmyindex  = (TextView) findViewById(R.id.textViewchartperiodmyindex);
		textViewchartperiodmyindex.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	chartrange=9;
		    	threadload();
		    }
		});
    	  
		 mainImageViewindex = (ImageView) findViewById(R.id.imageViewchartindex);
		 progressbarloadindex = (ProgressBar) findViewById(R.id.progressBarloadindex);

				 
  	     // imageurl = "http://chart.finance.yahoo.com/t?s="+stocksplit[0];
  	      //thread & set link
  	      threadload();
  	  

	     //2222222222222222222222222222
  	      
  		
  		
  		//1 day chart
  		textViewchartperiod1dindex2  = (TextView) findViewById(R.id.textViewchartperiod1dindex2);
  		textViewchartperiod1dindex2.setOnClickListener(new View.OnClickListener() {
  		    public void onClick(View v) {
  		    	chartrange2=1;
  		    	threadload2();
  		    }
  		});	
  		
  		//5 day chart
  		textViewchartperiod5dindex2  = (TextView) findViewById(R.id.textViewchartperiod5dindex2);
  		textViewchartperiod5dindex2.setOnClickListener(new View.OnClickListener() {
  		    public void onClick(View v) {
  		    	chartrange2=2;
  		    	threadload2();
  		    }
  		});	
  		
  		//1 month chart
  		textViewchartperiod1mindex2  = (TextView) findViewById(R.id.textViewchartperiod1mindex2);
  		textViewchartperiod1mindex2.setOnClickListener(new View.OnClickListener() {
  		    public void onClick(View v) {
  		    	chartrange2=3;
  		    	threadload2();
  		    }
  		});	
  		
  		//3 month chart
  		textViewchartperiod3mindex2  = (TextView) findViewById(R.id.textViewchartperiod3mindex2);
  		textViewchartperiod3mindex2.setOnClickListener(new View.OnClickListener() {
  		    public void onClick(View v) {
  		    	chartrange2=4;
  		    	threadload2();
  		    }
  		});	
  		
  		//6month chart
  		textViewchartperiod6mindex2  = (TextView) findViewById(R.id.textViewchartperiod6mindex2);
  		textViewchartperiod6mindex2.setOnClickListener(new View.OnClickListener() {
  		    public void onClick(View v) {
  		    	chartrange2=5;
  		    	threadload2();
  		    }
  		});	
  		
  		//1 year chart
  		textViewchartperiod1yindex2  = (TextView) findViewById(R.id.textViewchartperiod1yindex2);
  		textViewchartperiod1yindex2.setOnClickListener(new View.OnClickListener() {
  		    public void onClick(View v) {
  		    	chartrange2=6;
  		    	threadload2();
  		    }
  		});	
  		
  		//2 year chart
  		textViewchartperiod2yindex2  = (TextView) findViewById(R.id.textViewchartperiod2yindex2);
  		textViewchartperiod2yindex2.setOnClickListener(new View.OnClickListener() {
  		    public void onClick(View v) {
  		    	chartrange2=7;
  		    	threadload2();
  		    }
  		});	
  		
  		//5 year chart
  		textViewchartperiod5yindex2  = (TextView) findViewById(R.id.textViewchartperiod5yindex2);
  		textViewchartperiod5yindex2.setOnClickListener(new View.OnClickListener() {
  		    public void onClick(View v) {
  		    	chartrange2=8;
  		    	threadload2();
  		    }
  		});	
  		
  		//Large chart
  		textViewchartperiodmyindex2  = (TextView) findViewById(R.id.textViewchartperiodmyindex2);
  		textViewchartperiodmyindex2.setOnClickListener(new View.OnClickListener() {
  		    public void onClick(View v) {
  		    	chartrange2=9;
  		    	threadload2();
  		    }
  		});
      	  
  		 mainImageViewindex2 = (ImageView) findViewById(R.id.imageViewchartindex2);
  		 progressbarloadindex2 = (ProgressBar) findViewById(R.id.progressBarloadindex2);


    	      threadload2();
    	  

    		     //3333333333333333333333333333
    	  	      
    	  		
    	  		
    	  		//1 day chart
    	  		textViewchartperiod1dindex3  = (TextView) findViewById(R.id.textViewchartperiod1dindex3);
    	  		textViewchartperiod1dindex3.setOnClickListener(new View.OnClickListener() {
    	  		    public void onClick(View v) {
    	  		    	chartrange3=1;
    	  		    	threadload3();
    	  		    }
    	  		});	
    	  		
    	  		//5 day chart
    	  		textViewchartperiod5dindex3  = (TextView) findViewById(R.id.textViewchartperiod5dindex3);
    	  		textViewchartperiod5dindex3.setOnClickListener(new View.OnClickListener() {
    	  		    public void onClick(View v) {
    	  		    	chartrange3=3;
    	  		    	threadload3();
    	  		    }
    	  		});	
    	  		
    	  		//1 month chart
    	  		textViewchartperiod1mindex3  = (TextView) findViewById(R.id.textViewchartperiod1mindex3);
    	  		textViewchartperiod1mindex3.setOnClickListener(new View.OnClickListener() {
    	  		    public void onClick(View v) {
    	  		    	chartrange3=3;
    	  		    	threadload3();
    	  		    }
    	  		});	
    	  		
    	  		//3 month chart
    	  		textViewchartperiod3mindex3  = (TextView) findViewById(R.id.textViewchartperiod3mindex3);
    	  		textViewchartperiod3mindex3.setOnClickListener(new View.OnClickListener() {
    	  		    public void onClick(View v) {
    	  		    	chartrange3=4;
    	  		    	threadload3();
    	  		    }
    	  		});	
    	  		
    	  		//6month chart
    	  		textViewchartperiod6mindex3  = (TextView) findViewById(R.id.textViewchartperiod6mindex3);
    	  		textViewchartperiod6mindex3.setOnClickListener(new View.OnClickListener() {
    	  		    public void onClick(View v) {
    	  		    	chartrange3=5;
    	  		    	threadload3();
    	  		    }
    	  		});	
    	  		
    	  		//1 year chart
    	  		textViewchartperiod1yindex3  = (TextView) findViewById(R.id.textViewchartperiod1yindex3);
    	  		textViewchartperiod1yindex3.setOnClickListener(new View.OnClickListener() {
    	  		    public void onClick(View v) {
    	  		    	chartrange3=6;
    	  		    	threadload3();
    	  		    }
    	  		});	
    	  		
    	  		//2 year chart
    	  		textViewchartperiod2yindex3  = (TextView) findViewById(R.id.textViewchartperiod2yindex3);
    	  		textViewchartperiod2yindex3.setOnClickListener(new View.OnClickListener() {
    	  		    public void onClick(View v) {
    	  		    	chartrange3=7;
    	  		    	threadload3();
    	  		    }
    	  		});	
    	  		
    	  		//5 year chart
    	  		textViewchartperiod5yindex3  = (TextView) findViewById(R.id.textViewchartperiod5yindex3);
    	  		textViewchartperiod5yindex3.setOnClickListener(new View.OnClickListener() {
    	  		    public void onClick(View v) {
    	  		    	chartrange3=8;
    	  		    	threadload3();
    	  		    }
    	  		});	
    	  		
    	  		//Large chart
    	  		textViewchartperiodmyindex3  = (TextView) findViewById(R.id.textViewchartperiodmyindex3);
    	  		textViewchartperiodmyindex3.setOnClickListener(new View.OnClickListener() {
    	  		    public void onClick(View v) {
    	  		    	chartrange3=9;
    	  		    	threadload3();
    	  		    }
    	  		});
    	      	  
    	  		 mainImageViewindex3 = (ImageView) findViewById(R.id.imageViewchartindex3);
    	  		 progressbarloadindex3 = (ProgressBar) findViewById(R.id.progressBarloadindex3);


    	    	      threadload3();
    	    	
	}//end create
	
	
	
	
	
	
	
	
	

    
    // thread & image link
   private void threadload(){
 	   
	   switch (chartrange) {  

       case 1:
         imageurl="http://chart.finance.yahoo.com/t?s="+stockno;
   		textViewchartperiod1dindex.setBackgroundColor(Color.CYAN);
   		textViewchartperiod5dindex.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod1mindex.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod3mindex.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod6mindex.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod1yindex.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod2yindex.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod5yindex.setBackgroundColor(Color.WHITE); 
   		textViewchartperiodmyindex.setBackgroundColor(Color.WHITE); 
   	    progressbarloadindex.setVisibility(View.VISIBLE);
 	   	mainImageViewindex.setVisibility(View.INVISIBLE);
 	   	break;  
       case 2:
     	  imageurl="http://ichart.yahoo.com/v?s="+stockno+"&t=5d";
     		textViewchartperiod1dindex.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod1mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex.setVisibility(View.VISIBLE);
     	   	mainImageViewindex.setVisibility(View.INVISIBLE);
     	   	break;  
       case 3:
     	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=1m";
     		textViewchartperiod1dindex.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod3mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex.setVisibility(View.VISIBLE);
     	   	mainImageViewindex.setVisibility(View.INVISIBLE);
     	   	break;  
       case 4:
     	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=3m";
     		textViewchartperiod1dindex.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod6mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex.setVisibility(View.VISIBLE);
     	   	mainImageViewindex.setVisibility(View.INVISIBLE);
     	   	 break;  
       case 5:
     	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=6m";
     		textViewchartperiod1dindex.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod1yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex.setVisibility(View.VISIBLE);
     	   	mainImageViewindex.setVisibility(View.INVISIBLE);
     	   	break;  
       case 6:
     	  imageurl="http://ichart.finance.yahoo.com/c/bb/m/"+stockno;
     		textViewchartperiod1dindex.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod2yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex.setVisibility(View.VISIBLE);
     	   	mainImageViewindex.setVisibility(View.INVISIBLE);
     	   	break;  
       case 7:
     	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=2y";
     		textViewchartperiod1dindex.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod5yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex.setVisibility(View.VISIBLE);
     	   	mainImageViewindex.setVisibility(View.INVISIBLE);
     	   	break;  
       case 8:
     	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=5y";
     		textViewchartperiod1dindex.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex.setBackgroundColor(Color.CYAN); 
       		textViewchartperiodmyindex.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex.setVisibility(View.VISIBLE);
     	   	mainImageViewindex.setVisibility(View.INVISIBLE);
     	   	break;  
       case 9:
     	  imageurl="http://chart.finance.yahoo.com/z?s="+stockno+"&t=my";
     		textViewchartperiod1dindex.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex.setBackgroundColor(Color.CYAN); 
       	    progressbarloadindex.setVisibility(View.VISIBLE);
     	   	mainImageViewindex.setVisibility(View.INVISIBLE);
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
	  	   	    Toast.makeText(Index.this,Index.this.getString(R.string.interneterror), Toast.LENGTH_LONG).show();
	  	   	    }
				Looper.loop();
	          	
	           }
	     };      
	     t.start();

	   
   }//end halder msg
   
   
   
   
   
   // thread22222222222222
   private void threadload2(){
 	   
	   switch (chartrange2) {  

       case 1:
         imageurl2="http://chart.finance.yahoo.com/t?s="+stockno2;
   		textViewchartperiod1dindex2.setBackgroundColor(Color.CYAN);
   		textViewchartperiod5dindex2.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod1mindex2.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod3mindex2.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod6mindex2.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod1yindex2.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod2yindex2.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod5yindex2.setBackgroundColor(Color.WHITE); 
   		textViewchartperiodmyindex2.setBackgroundColor(Color.WHITE); 
   	    progressbarloadindex2.setVisibility(View.VISIBLE);
 	   	mainImageViewindex2.setVisibility(View.INVISIBLE);
 	   	break;  
       case 2:
     	  imageurl2="http://ichart.yahoo.com/v?s="+stockno2+"&t=5d";
     		textViewchartperiod1dindex2.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex2.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod1mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex2.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex2.setVisibility(View.VISIBLE);
     	   	mainImageViewindex2.setVisibility(View.INVISIBLE);
     	   	break;  
       case 3:
     	  imageurl2="http://chart.finance.yahoo.com/z?s="+stockno2+"&t=1m";
     		textViewchartperiod1dindex2.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex2.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod3mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex2.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex2.setVisibility(View.VISIBLE);
     	   	mainImageViewindex2.setVisibility(View.INVISIBLE);
     	   	break;  
       case 4:
     	  imageurl2="http://chart.finance.yahoo.com/z?s="+stockno2+"&t=3m";
     		textViewchartperiod1dindex2.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex2.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod6mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex2.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex2.setVisibility(View.VISIBLE);
     	   	mainImageViewindex2.setVisibility(View.INVISIBLE);
     	   	 break;  
       case 5:
     	  imageurl2="http://chart.finance.yahoo.com/z?s="+stockno2+"&t=6m";
     		textViewchartperiod1dindex2.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex2.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod1yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex2.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex2.setVisibility(View.VISIBLE);
     	   	mainImageViewindex2.setVisibility(View.INVISIBLE);
     	   	break;  
       case 6:
     	  imageurl2="http://ichart.finance.yahoo.com/c/bb/m/"+stockno2;
     		textViewchartperiod1dindex2.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex2.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod2yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex2.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex2.setVisibility(View.VISIBLE);
     	   	mainImageViewindex2.setVisibility(View.INVISIBLE);
     	   	break;  
       case 7:
     	  imageurl2="http://chart.finance.yahoo.com/z?s="+stockno2+"&t=2y";
     		textViewchartperiod1dindex2.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex2.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod5yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex2.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex2.setVisibility(View.VISIBLE);
     	   	mainImageViewindex2.setVisibility(View.INVISIBLE);
     	   	break;  
       case 8:
     	  imageurl2="http://chart.finance.yahoo.com/z?s="+stockno2+"&t=5y";
     		textViewchartperiod1dindex2.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex2.setBackgroundColor(Color.CYAN); 
       		textViewchartperiodmyindex2.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex2.setVisibility(View.VISIBLE);
     	   	mainImageViewindex2.setVisibility(View.INVISIBLE);
     	   	break;  
       case 9:
     	  imageurl2="http://chart.finance.yahoo.com/z?s="+stockno2+"&t=my";
     		textViewchartperiod1dindex2.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex2.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex2.setBackgroundColor(Color.CYAN); 
       	    progressbarloadindex2.setVisibility(View.VISIBLE);
     	   	mainImageViewindex2.setVisibility(View.INVISIBLE);
     	   	break;  
           
       default:  
           break;  
       }  
 	  
		 t2=new Thread() {
	           public void run() {
	          	 
	     		Looper.prepare();  
	     		imagebm2 =getBitmapFromUrl(imageurl2);
	  			 
	  	   	    if(imagebm2!=null){

	 			Message m = new Message();
	 	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
	 	     		m.what = MEG_TAGCHANGE2;
	 	     		handler.sendMessage(m);		   	
	  	   	    }else{
	  	   	    Toast.makeText(Index.this,Index.this.getString(R.string.interneterror), Toast.LENGTH_LONG).show();
	  	   	    }
				Looper.loop();
	          	
	           }
	     };      
	     t2.start();

	   
   }//end halder msg
   
   
   
   
   

   
   // thread33333333333333
   private void threadload3(){
 	   
	   switch (chartrange3) {  

       case 1:
         imageurl3="http://chart.finance.yahoo.com/t?s="+stockno3;
   		textViewchartperiod1dindex3.setBackgroundColor(Color.CYAN);
   		textViewchartperiod5dindex3.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod1mindex3.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod3mindex3.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod6mindex3.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod1yindex3.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod2yindex3.setBackgroundColor(Color.WHITE); 
   		textViewchartperiod5yindex3.setBackgroundColor(Color.WHITE); 
   		textViewchartperiodmyindex3.setBackgroundColor(Color.WHITE); 
   	    progressbarloadindex3.setVisibility(View.VISIBLE);
 	   	mainImageViewindex3.setVisibility(View.INVISIBLE);
 	   	break;  
       case 2:
     	  imageurl3="http://ichart.yahoo.com/v?s="+stockno3+"&t=5d";
     		textViewchartperiod1dindex3.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex3.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod1mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex3.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex3.setVisibility(View.VISIBLE);
     	   	mainImageViewindex3.setVisibility(View.INVISIBLE);
     	   	break;  
       case 3:
     	  imageurl3="http://chart.finance.yahoo.com/z?s="+stockno3+"&t=1m";
     		textViewchartperiod1dindex3.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex3.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod3mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex3.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex3.setVisibility(View.VISIBLE);
     	   	mainImageViewindex3.setVisibility(View.INVISIBLE);
     	   	break;  
       case 4:
     	  imageurl3="http://chart.finance.yahoo.com/z?s="+stockno3+"&t=3m";
     		textViewchartperiod1dindex3.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex3.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod6mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex3.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex3.setVisibility(View.VISIBLE);
     	   	mainImageViewindex3.setVisibility(View.INVISIBLE);
     	   	 break;  
       case 5:
     	  imageurl3="http://chart.finance.yahoo.com/z?s="+stockno3+"&t=6m";
     		textViewchartperiod1dindex3.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex3.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod1yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex3.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex3.setVisibility(View.VISIBLE);
     	   	mainImageViewindex3.setVisibility(View.INVISIBLE);
     	   	break;  
       case 6:
     	  imageurl3="http://ichart.finance.yahoo.com/c/bb/m/"+stockno3;
     		textViewchartperiod1dindex3.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex3.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod2yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex3.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex3.setVisibility(View.VISIBLE);
     	   	mainImageViewindex3.setVisibility(View.INVISIBLE);
     	   	break;  
       case 7:
     	  imageurl3="http://chart.finance.yahoo.com/z?s="+stockno3+"&t=3y";
     		textViewchartperiod1dindex3.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex3.setBackgroundColor(Color.CYAN); 
       		textViewchartperiod5yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex3.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex3.setVisibility(View.VISIBLE);
     	   	mainImageViewindex3.setVisibility(View.INVISIBLE);
     	   	break;  
       case 8:
     	  imageurl3="http://chart.finance.yahoo.com/z?s="+stockno3+"&t=5y";
     		textViewchartperiod1dindex3.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex3.setBackgroundColor(Color.CYAN); 
       		textViewchartperiodmyindex3.setBackgroundColor(Color.WHITE); 
       	    progressbarloadindex3.setVisibility(View.VISIBLE);
     	   	mainImageViewindex3.setVisibility(View.INVISIBLE);
     	   	break;  
       case 9:
     	  imageurl3="http://chart.finance.yahoo.com/z?s="+stockno3+"&t=my";
     		textViewchartperiod1dindex3.setBackgroundColor(Color.WHITE);
       		textViewchartperiod5dindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod3mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod6mindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod1yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod2yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiod5yindex3.setBackgroundColor(Color.WHITE); 
       		textViewchartperiodmyindex3.setBackgroundColor(Color.CYAN); 
       	    progressbarloadindex3.setVisibility(View.VISIBLE);
     	   	mainImageViewindex3.setVisibility(View.INVISIBLE);
     	   	break;  
           
       default:  
           break;  
       }  
 	  
		 t3=new Thread() {
	           public void run() {
	          	 
	     		Looper.prepare();  
	     		imagebm3 =getBitmapFromUrl(imageurl3);
	  			 
	  	   	    if(imagebm3!=null){

	 			Message m = new Message();
	 	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
	 	     		m.what = MEG_TAGCHANGE3;
	 	     		handler.sendMessage(m);		   	
	  	   	    }else{
	  	   	    Toast.makeText(Index.this,Index.this.getString(R.string.interneterror), Toast.LENGTH_LONG).show();
	  	   	    }
				Looper.loop();
	          	
	           }
	     };      
	     t3.start();

	   
   }//end halder msg
   
   
   
   

   
   //handler msg
   Handler handler = new Handler() 
   {

         public void handleMessage(Message msg) 
   {
    switch (msg.what) 
 {

 case MEG_TAGCHANGE:
 	    progressbarloadindex.setVisibility(View.INVISIBLE);
	   	mainImageViewindex.setImageBitmap(imagebm);
	   
	   	mainImageViewindex.setVisibility(View.VISIBLE);
		 mainImageViewindex.setOnTouchListener(new OnTouchListener()
	        {

	            @Override
	            public boolean onTouch(View v, MotionEvent event)
	            {
	            	
	            	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	            		int x = (int) event.getX();
	            		
	            		if(x>=mainImageViewindex.getWidth()/2){
	            			
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
 case MEG_TAGCHANGE2:
	    progressbarloadindex2.setVisibility(View.INVISIBLE);
	   	mainImageViewindex2.setImageBitmap(imagebm2);
	   
	   	mainImageViewindex2.setVisibility(View.VISIBLE);
		 mainImageViewindex2.setOnTouchListener(new OnTouchListener()
	        {

	            @Override
	            public boolean onTouch(View v, MotionEvent event)
	            {
	            	
	            	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	            		int x = (int) event.getX();
	            		
	            		if(x>=mainImageViewindex2.getWidth()/2){
	            			
	            			chartrange2++;
	            			if(chartrange2>9){
	            				chartrange2=1;
	            			}
	            			
	            			threadload2();
	            			
	            			
	            		}else{
	            			
	            			chartrange2--;
	            			if(chartrange2<=0){
	            				chartrange2=9;}
	            			
	            		}
	            		threadload2();
	            		
	            	}
	                return false;
	            }
	       });
		 
break;
 case MEG_TAGCHANGE3:
	    progressbarloadindex3.setVisibility(View.INVISIBLE);
	   	mainImageViewindex3.setImageBitmap(imagebm3);
	   
	   	mainImageViewindex3.setVisibility(View.VISIBLE);
		 mainImageViewindex3.setOnTouchListener(new OnTouchListener()
	        {

	            @Override
	            public boolean onTouch(View v, MotionEvent event)
	            {
	            	
	            	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	            		int x = (int) event.getX();
	            		
	            		if(x>=mainImageViewindex3.getWidth()/3){
	            			
	            			chartrange3++;
	            			if(chartrange3>9){
	            				chartrange3=1;
	            			}
	            			
	            			threadload3();
	            			
	            			
	            		}else{
	            			
	            			chartrange3--;
	            			if(chartrange3<=0){
	            				chartrange3=9;}
	            			
	            		}
	            		threadload3();
	            		
	            	}
	                return false;
	            }
	       });
		 
break;
 case MEG_INVALIDATE :
	
	 setindex();
	 
 break;
 case MEG_INVALIDATE2 :
		
	 setindex2();
	 
 break;
         }
    
    
     super.handleMessage(msg);
         }

     };//end handler msg
   
   
   
   
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

   
   
   

	public String loadmultlestock(String stockno) {

       
		String Stocklink="http://hk.finance.yahoo.com/d/quotes.csv?s="+stockno+"&f=l1c6p2";
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
	    	  System.out.println(e);

	    	removeDialog(0);
	    	//Toast.makeText(Main.this, R.string.interneterror, Toast.LENGTH_LONG).show(); 	
	            
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
	
	
	
	public void setindex() 
	{

      	
		if (stocklist!=null){
		String[] stocklistsplit = stocklist.split("\n");
		String[][] stockdetailplit = new String[stocklistsplit.length][];
		for (int i = 0; i < stocklistsplit.length; i++) {  
			stockdetailplit[i]= stocklistsplit[i].split(",");
			}  
		textViewhsiindex.setText(stockdetailplit[0][0]);
		textViewindexchange.setText((stockdetailplit[0][1])+"("+(stockdetailplit[0][2])+")");
		if(stockdetailplit[0][1].contains("-")){
			textViewhsiindex.setTextColor(Color.RED);
			textViewindexchange.setBackgroundColor(Color.RED);
		}else{
			textViewhsiindex.setTextColor(Color.GREEN);
			textViewindexchange.setBackgroundColor(Color.GREEN);
		}
		
		
		textViewhsiindex2.setText(stockdetailplit[1][0]);
		textViewindexchange2.setText((stockdetailplit[1][1])+"("+(stockdetailplit[1][2])+")");
		if(stockdetailplit[1][1].contains("-")){
			textViewhsiindex2.setTextColor(Color.RED);
			textViewindexchange2.setBackgroundColor(Color.RED);
		}else{
			textViewhsiindex2.setTextColor(Color.GREEN);
			textViewindexchange2.setBackgroundColor(Color.GREEN);
		}
		
		
		textViewhsiindex3.setText(stockdetailplit[2][0]);
		textViewindexchange3.setText((stockdetailplit[2][1])+"("+(stockdetailplit[2][2])+")");
		if(stockdetailplit[2][1].contains("-")){
			textViewhsiindex3.setTextColor(Color.RED);
			textViewindexchange3.setBackgroundColor(Color.RED);
		}else{
			textViewhsiindex3.setTextColor(Color.GREEN);
			textViewindexchange3.setBackgroundColor(Color.GREEN);
		}
		
		
		
	}//end check null
	
	}//end setup class
	
	
	public void setindex2() 
	{

		String[] dowfina; 
		dowfina= dowlist.split(",");

		
		textViewhsiindex.setText(dowfina[1]);
		textViewindexchange.setText(dowfina[4]+"("+dowfina[2]+"%)");
		if(dowfina[2].contains("-")){
			textViewhsiindex.setTextColor(Color.RED);
			textViewindexchange.setBackgroundColor(Color.RED);
		}else{
			textViewhsiindex.setTextColor(Color.GREEN);
			textViewindexchange.setBackgroundColor(Color.GREEN);
		}
		
		
		
		
		
      	
		if (stocklist!=null){
		String[] stocklistsplit = stocklist.split("\n");
		String[][] stockdetailplit = new String[stocklistsplit.length][];
		for (int i = 0; i < stocklistsplit.length; i++) {  
			stockdetailplit[i]= stocklistsplit[i].split(",");
			}  
	
		
		
		textViewhsiindex2.setText(stockdetailplit[0][0]);
		textViewindexchange2.setText((stockdetailplit[0][1])+"("+(stockdetailplit[0][2])+")");
		if(stockdetailplit[0][1].contains("-")){
			textViewhsiindex2.setTextColor(Color.RED);
			textViewindexchange2.setBackgroundColor(Color.RED);
		}else{
			textViewhsiindex2.setTextColor(Color.GREEN);
			textViewindexchange2.setBackgroundColor(Color.GREEN);
		}
		
		
		textViewhsiindex3.setText(stockdetailplit[1][0]);
		textViewindexchange3.setText((stockdetailplit[1][1])+"("+(stockdetailplit[1][2])+")");
		if(stockdetailplit[1][1].contains("-")){
			textViewhsiindex3.setTextColor(Color.RED);
			textViewindexchange3.setBackgroundColor(Color.RED);
		}else{
			textViewhsiindex3.setTextColor(Color.GREEN);
			textViewindexchange3.setBackgroundColor(Color.GREEN);
		}
		
		
		
	}//end check null
	
	}//end setup class
	
	String loadsina() {

	       
		String Stocklink="http://hq.sinajs.cn/list=gb_dji";
		
	
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
	    	//Toast.makeText(Main.this, R.string.interneterror, Toast.LENGTH_LONG).show(); 	
	            
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


	}//load sina
	
	
}
