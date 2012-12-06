package com.hwy.stock;


	import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
	 
	public class Recordadapter extends BaseAdapter {
	 
	    private ArrayList<HashMap<String,String>> recordlist;
	    private static LayoutInflater inflater=null;

	 
	    public Recordadapter(Activity activity, ArrayList<HashMap<String,String>> recordlist) {
	        this.recordlist=recordlist;
	        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	 
	    public int getCount() {
	        return recordlist.size();
	    }
	 
	    public Object getItem(int position) {
	        return position;
	    }
	 
	    public long getItemId(int position) {
	        return position;
	    }
	 
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View vi=convertView;
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.recorditem, null);
	 
	        
	        String Stockno= recordlist.get(position).get("Stockno");
	        String Date= recordlist.get(position).get("Date");
	        String Type= recordlist.get(position).get("Type");
	        String Bsprice= recordlist.get(position).get("Bsprice");
	        String Bsprofit= recordlist.get(position).get("Bsprofit");
	        String Bsqty= recordlist.get(position).get("Bsqty");

	        
	        TextView textViewStockno = (TextView)vi.findViewById(R.id.textViewStockno);
	        textViewStockno.setText(Stockno);
	        		
	        TextView textViewbstype = (TextView)vi.findViewById(R.id.textViewbstype);
	        textViewbstype.setText(Type);
	        		
	        TextView textViewbsprofit = (TextView)vi.findViewById(R.id.textViewbsprofit);
	        textViewbsprofit.setText(Bsprofit);
	    	
	        if(Bsprofit!=null && Bsprofit.contains("-")){
	        	textViewbsprofit.setTextColor(Color.RED);	
	    	}else if(Bsprofit!=null && Bsprofit.contains("+")){
	    		textViewbsprofit.setTextColor(Color.GREEN);
	    	}
	    	
	        TextView textViewbsqty = (TextView)vi.findViewById(R.id.textViewbsqty);
	        textViewbsqty.setText(Bsqty);
	        		
	        TextView textViewChangedate = (TextView)vi.findViewById(R.id.textViewChangedate);
	        textViewChangedate.setText(Date);
	        		
	        TextView textViewbsprice = (TextView)vi.findViewById(R.id.textViewbsprice);
	        textViewbsprice.setText(Bsprice);
	        		

	        return vi;
	    }


	}