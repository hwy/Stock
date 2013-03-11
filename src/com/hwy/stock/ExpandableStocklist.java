package com.hwy.stock;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//自定义的ExpandListAdapter
class ExpandableStocklist extends BaseExpandableListAdapter
{
private Context context;
List<Map<String, String>> groups;
List<List<Map<String, String>>> childs;
String datenow;


//SQL
final DbHelper helper = new DbHelper(context, "Stock.db", null, 1);	
private SQLiteDatabase db;
/*
* 构造函数:
* 参数1:context对象
* 参数2:一级列表数据源
* 参数3:二级列表数据源
*/
public ExpandableStocklist(Context context, List<Map<String, String>> groups, List<List<Map<String, String>>> childs,SQLiteDatabase db)
{
this.groups = groups;
this.childs = childs;
this.context = context;
this.db=db;

Time date = new Time(Time.getCurrentTimezone());
date.setToNow();
int datem=date.month+1;
datenow=date.year+"-"+datem+"-"+date.monthDay;
}

@Override
public Object getChild(int groupPosition, int childPosition)
{
return childs.get(groupPosition).get(childPosition);
}

@Override
public long getChildId(int groupPosition, int childPosition)
{
return childPosition;
}

//获取二级列表的View对象
@Override
public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
ViewGroup parent)
{


final String STOCK_ID = ((Map<String, String>) getChild(groupPosition, childPosition)).get("STOCK_ID");
final String STOCK_NO = ((Map<String, String>) getChild(groupPosition, childPosition)).get("STOCK_NO");
final String Qty = ((Map<String, String>) getChild(groupPosition, childPosition)).get("Qty");
String Date = ((Map<String, String>) getChild(groupPosition, childPosition)).get("Date");
String OPEN = ((Map<String, String>) getChild(groupPosition, childPosition)).get("OPEN");
String YOPEN = ((Map<String, String>) getChild(groupPosition, childPosition)).get("YOPEN");
String DAYRANGE = ((Map<String, String>) getChild(groupPosition, childPosition)).get("DAYRANGE");
String Fitytwow = ((Map<String, String>) getChild(groupPosition, childPosition)).get("Fitytwow");
String AMOUNT = ((Map<String, String>) getChild(groupPosition, childPosition)).get("AMOUNT");
final String BUYPRICE = ((Map<String, String>) getChild(groupPosition, childPosition)).get("BUYPRICE");
String STOCKQTY = ((Map<String, String>) getChild(groupPosition, childPosition)).get("STOCKQTY");


LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//获取二级列表对应的布局文件, 并将其各元素设置相应的属性
LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.stockchildslistview, null);



TextView textViewopen = (TextView) linearLayout.findViewById(R.id.textViewopen);
textViewopen.setText(OPEN);


TextView textViewyopen = (TextView)linearLayout.findViewById(R.id.textViewyopen);
textViewyopen.setText(YOPEN);


TextView textViewdayrange = (TextView)linearLayout.findViewById(R.id.textViewdayrange);
textViewdayrange.setText(DAYRANGE);

TextView textView52w = (TextView)linearLayout.findViewById(R.id.textView52w);
textView52w.setText(Fitytwow);

TextView textViewamount = (TextView)linearLayout.findViewById(R.id.textViewamount);
textViewamount.setText(AMOUNT);

TextView textViewbuyprice = (TextView)linearLayout.findViewById(R.id.textViewbuyprice);
textViewbuyprice.setText(BUYPRICE);

TextView textViewdate = (TextView)linearLayout.findViewById(R.id.textViewdate);
textViewdate.setText(Date);

final EditText editTextstockqty = (EditText)linearLayout.findViewById(R.id.editTextstockqty);
editTextstockqty.setText(STOCKQTY);

final EditText editTextsellprice = (EditText)linearLayout.findViewById(R.id.editTextsellprice);
editTextsellprice.setText(BUYPRICE);





Button buttonedit = (Button)linearLayout.findViewById(R.id.buttonedit);

buttonedit.setOnClickListener(new Button.OnClickListener()  
{  
  @Override 
  public void onClick(View v)  
  {  
	  String stqty =editTextstockqty.getText().toString().trim();
	  String stprice=editTextsellprice.getText().toString().trim();
	  

	  	childs.get(groupPosition).get(childPosition).put("STOCKQTY", stqty);
		childs.get(groupPosition).get(childPosition).put("BUYPRICE", stprice);
		  	  	
	ContentValues cv = new ContentValues();
	cv.put("qty",stqty);
	cv.put("price",stprice);
	long long1=db.update("Stock", cv, "stock_id="+STOCK_ID, null);
System.out.println(long1);
	if (long1 >= 1) {
		Toast.makeText(context,	STOCK_NO+context.getString(R.string.expanableeditesucess), Toast.LENGTH_SHORT).show();
		 	cv.clear();
  	  		cv.put("stock_no",STOCK_NO);
  	  		cv.put("bstype","3");
  	  		cv.put("bsqty",stqty);
  	  		cv.put("bprice",stprice); // EDIT 無賣,所以SELL ITEM = BUYPRICE
  	  		cv.put("bsdate",datenow);
  	  		db.insert("Record", "", cv);
	}  
	
  }  
});  




Button buttonsell = (Button)linearLayout.findViewById(R.id.buttonsell);

buttonsell.setOnClickListener(new Button.OnClickListener()  
{  
  @Override 
  public void onClick(View v)  
  {  
	  String stqty =editTextstockqty.getText().toString().trim();
	  String stprice=editTextsellprice.getText().toString().trim();
	  

  	if(stqty.equals(Qty)){
  		
	  	childs.get(groupPosition).get(childPosition).put("STOCKQTY", "0");
		childs.get(groupPosition).get(childPosition).put("BUYPRICE", "0");
		childs.remove(childPosition);
		groups.remove(groupPosition);
		notifyDataSetChanged();
  		long long1=db.delete("Stock", "stock_id="+STOCK_ID, null);	
  		
  		System.out.println(long1);
  		if (long1 >= 1) {
  			Toast.makeText(context,	STOCK_NO+context.getString(R.string.expanablesellall), Toast.LENGTH_SHORT).show();
  	 		
  			ContentValues cv = new ContentValues();
  	  	  		cv.put("stock_no",STOCK_NO);
  	  	  		cv.put("bstype","2");
  	  	  		cv.put("bsqty",stqty);
  	  	  		cv.put("bprice",BUYPRICE);
  	  	  		cv.put("sprice",stprice);
  	  	  		cv.put("bsdate",datenow);
  	  	  		db.insert("Record", "", cv);
   		}
  		
  		
  	}else if (Integer.parseInt(stqty)<Integer.parseInt(Qty)){
  		
  		ContentValues cv = new ContentValues();
  		
  		int lessqty =Integer.parseInt(Qty)-Integer.parseInt(stqty);
  		cv.put("qty",lessqty);
  		cv.put("price",stprice);
  		long long1=db.update("Stock", cv, "stock_id="+STOCK_ID, null);
  	System.out.println(long1);
  		if (long1 >= 1) {
  			Toast.makeText(context,	STOCK_NO+context.getString(R.string.expanablereduce)+stqty+"！", Toast.LENGTH_SHORT).show();
  		
  		  	childs.get(groupPosition).get(childPosition).put("STOCKQTY", Integer.toString(lessqty));
  		  	childs.get(groupPosition).get(childPosition).put("BUYPRICE", stprice);
  		  	
  			 	cv.clear();
	  	  		cv.put("stock_no",STOCK_NO);
	  	  		cv.put("bstype","4");
	  	  		cv.put("bsqty",stqty);
	  	  		cv.put("bprice",BUYPRICE);
	  	  		cv.put("sprice",stprice);
	  	  		cv.put("bsdate",datenow);
	  	  		db.insert("Record", "", cv);
  		}
  		
  		
  	} else{
			Toast.makeText(context,	STOCK_NO+context.getString(R.string.expanableentererror), Toast.LENGTH_SHORT).show();
  	}
  
	
  
  }  
});  

return linearLayout;
}

@Override
public int getChildrenCount(int groupPosition)
{
return childs.get(groupPosition).size();
}

@Override
public Object getGroup(int groupPosition)
{
return groups.get(groupPosition);
}

@Override
public int getGroupCount()
{
return groups.size();
}

@Override
public long getGroupId(int groupPosition)
{
return groupPosition;
}

//获取一级列表View对象
@Override
public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
{
	
String Stocknonname = groups.get(groupPosition).get("Stocknonname");
String Price = groups.get(groupPosition).get("Price");
String Changeprice = groups.get(groupPosition).get("Changeprice");
String Changepercent = groups.get(groupPosition).get("Changepercent");
String Profit = groups.get(groupPosition).get("Profit");
String profitpercent = groups.get(groupPosition).get("profitpercent");
String greenredcolor = groups.get(groupPosition).get("greenredcolor");


LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//获取一级列表布局文件,设置相应元素属性
RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.stockgrouplistview, null);
TextView textViewStockName = (TextView)relativeLayout.findViewById(R.id.textViewStockName);
textViewStockName.setText(Stocknonname);

TextView textViewnowprice = (TextView)relativeLayout.findViewById(R.id.textViewnowprice);
textViewnowprice.setText(Price);

TextView textViewStockChangeprice = (TextView)relativeLayout.findViewById(R.id.textViewStockChangeprice);
textViewStockChangeprice.setText(Changeprice);

TextView textViewStockChangepercent = (TextView)relativeLayout.findViewById(R.id.textViewStockChangepercent);
textViewStockChangepercent.setText(Changepercent);

TextView textViewStockttlprofit = (TextView)relativeLayout.findViewById(R.id.textViewStockttlprofit);
textViewStockttlprofit.setText(Profit);


TextView textViewaprofitpercent = (TextView)relativeLayout.findViewById(R.id.textViewaprofitpercent);
textViewaprofitpercent.setText(profitpercent);


//set rase & drop color
if(greenredcolor.contains("+")){
	textViewnowprice.setTextColor(Color.GREEN);
	textViewStockChangeprice.setBackgroundColor(Color.GREEN);
	textViewStockChangepercent.setBackgroundColor(Color.GREEN);
	textViewStockChangepercent.setTextColor(Color.WHITE);
	textViewStockChangeprice.setTextColor(Color.WHITE);
}else if(greenredcolor.contains("-")){
	textViewnowprice.setTextColor(Color.RED);
	textViewStockChangeprice.setBackgroundColor(Color.RED);
	textViewStockChangepercent.setBackgroundColor(Color.RED);	
	textViewStockChangepercent.setTextColor(Color.WHITE);
	textViewStockChangeprice.setTextColor(Color.WHITE);
}//end if price


if(Profit.contains("-")){
	textViewStockttlprofit.setTextColor(Color.RED);	
	textViewaprofitpercent.setTextColor(Color.RED);
}else{
	textViewStockttlprofit.setTextColor(Color.GREEN);
	textViewaprofitpercent.setTextColor(Color.GREEN);
}

return relativeLayout;
}

// **************************************
public boolean hasStableIds() {
	return true;
}

public boolean isChildSelectable(int groupPosition, int childPosition) {
	return true;
}



}