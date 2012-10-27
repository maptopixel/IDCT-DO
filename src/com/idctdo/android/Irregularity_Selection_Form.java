package com.idctdo.android;


import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;



public class Irregularity_Selection_Form extends EQForm {
	public boolean DEBUG_LOG = false; 

	private String topLevelAttributeDictionary = "DIC_STRUCTURAL_IRREGULARITY";
	private String topLevelAttributeKey = "STR_IRREG";
	
	private String secondLevelAttributeDictionary = "DIC_STRUCTURAL_HORIZ_IRREG";
	private String secondLevelAttributeKey = "STR_HZIR_P";
	
	private String thirdLevelAttributeDictionary = "DIC_STRUCTURAL_VERT_IRREG";
	private String thirdLevelAttributeKey = "STR_VEIR_P";
		
	
	
	private String topLevelAttributeType = "STRI";
	private String secondLevelAttributeType = "STRHI";
	private String thirdLevelAttributeType = "STRHVI";

	public TabActivity tabActivity;
	public TabHost tabHost;
	public int tabIndex = 4;

	private SelectedAdapter selectedAdapter;
	private SelectedAdapter selectedAdapter2;
	private SelectedAdapter selectedAdapter3;

	private ArrayList list;
	public ArrayList<DBRecord> secondLevelAttributesList;
	public ArrayList<DBRecord> thirdLevelAttributesList;

	ListView listview;
	ListView listview2;		 
	ListView listview3;

	public GemDbAdapter mDbHelper;
	public GEMSurveyObject surveyDataObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.irregularity_selectable_list);


	}

	@Override
	public void onBackPressed() {
		if (DEBUG_LOG) Log.d("IDCT","back button pressed");
		MainTabActivity a = (MainTabActivity)getParent();
		a.backButtonPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MainTabActivity a = (MainTabActivity)getParent();
		surveyDataObject = (GEMSurveyObject)getApplication();
		
		if (a.isTabCompleted(tabIndex)) {

		} else {
			mDbHelper = new GemDbAdapter(getBaseContext());        

			mDbHelper.createDatabase();      
			mDbHelper.open();

			Cursor allAttributeTypesTopLevelCursor = mDbHelper.getAttributeValuesByDictionaryTable(topLevelAttributeDictionary);     
			ArrayList<DBRecord> topLevelAttributesList = GemUtilities.cursorToArrayList(allAttributeTypesTopLevelCursor);        
			if (DEBUG_LOG) Log.d("IDCT","TYPES: " + topLevelAttributesList.toString());

			Cursor allAttributeTypesSecondLevelCursor = mDbHelper.getAttributeValuesByDictionaryTableAndScope(secondLevelAttributeDictionary,"IR");
			secondLevelAttributesList = GemUtilities.cursorToArrayList(allAttributeTypesSecondLevelCursor);

			Cursor allAttributeTypesThirdLevelCursor = mDbHelper.getAttributeValuesByDictionaryTableAndScope(thirdLevelAttributeDictionary,"IR");
			thirdLevelAttributesList = GemUtilities.cursorToArrayList(allAttributeTypesThirdLevelCursor);

			mDbHelper.close();

			selectedAdapter = new SelectedAdapter(this,0,topLevelAttributesList);
			selectedAdapter.setNotifyOnChange(true);

			listview = (ListView) findViewById(R.id.listExample);
			listview.setAdapter(selectedAdapter);        


			selectedAdapter2 = new SelectedAdapter(this,0,secondLevelAttributesList);    		
			selectedAdapter2.setNotifyOnChange(true);		
			listview2 = (ListView) findViewById(R.id.listExample2);
			listview2.setAdapter(selectedAdapter2);                      
			listview2.setVisibility(View.INVISIBLE);
			RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rel2);
			relativeLayout.setVisibility(View.INVISIBLE);

			selectedAdapter3 = new SelectedAdapter(this,0,thirdLevelAttributesList);    		
			selectedAdapter3.setNotifyOnChange(true);		
			listview3 = (ListView) findViewById(R.id.listExample3);
			listview3.setAdapter(selectedAdapter3);               
			listview3.setVisibility(View.INVISIBLE);
			RelativeLayout relativeLayout3 = (RelativeLayout) findViewById(R.id.rel3);
			relativeLayout3.setVisibility(View.INVISIBLE);


			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView arg0, View view,
						int position, long id) {
					// user clicked a list item, make it "selected"
					selectedAdapter.setSelectedPosition(position);
					selectedAdapter2.setSelectedPosition(-1);			
					surveyDataObject.putData(topLevelAttributeType, selectedAdapter.getItem(position).getAttributeValue());
					
					//Toast.makeText(getApplicationContext(), "Item clicked: " + selectedAdapter.getItem(position).getOrderName() + " " + selectedAdapter.getItem(position).getOrderStatus() + " " +selectedAdapter.getItem(position).getJson(), Toast.LENGTH_SHORT).show();				

					DBRecord selectedItem = selectedAdapter.getItem(position);
					if (DEBUG_LOG) Log.d("IDCT", "SELECTED ITEM: " + selectedItem.getJson());
					if (DEBUG_LOG) Log.d("IDCT", "SELECTED ITEM: " + selectedItem.getCode());
					if (DEBUG_LOG) Log.d("IDCT", "SELECTED ITEM: " + selectedItem.getAttributeDescription());
					//GEMSurveyObject g = (GEMSurveyObject)getApplication();
					//g.putData(key, value);

					completeThis();

					secondLevelAttributesList.clear();
					thirdLevelAttributesList.clear();	

					mDbHelper.open();			

					Cursor mCursor = mDbHelper.getAttributeValuesByDictionaryTableAndScope(secondLevelAttributeDictionary, selectedAdapter.getItem(position).getJson());

					mCursor.moveToFirst();
					while(!mCursor.isAfterLast()) {
						//mArrayList.add(mCursor.getString(mCursor.getColumnIndex(mCursor.getColumnName(1))));

						DBRecord o1 = new DBRecord();		

						if (DEBUG_LOG) Log.d("IDCT", "CURSOR TO ARRAY LIST" + mCursor.getString(mCursor.getColumnIndex(mCursor.getColumnName(1))));
						//String mTitleRaw = mCursor.getString(mCursor.getColumnIndex(mCursor.getColumnName(1)));

						o1.setAttributeDescription(mCursor.getString(0));
						o1.setAttributeValue(mCursor.getString(1));
						o1.setJson(mCursor.getString(2));
						secondLevelAttributesList.add(o1);
						mCursor.moveToNext();
					}		     
					mDbHelper.close();    		          



					if (mCursor.getCount() > 0) { 
						listview2.setVisibility(View.VISIBLE);
						RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rel2);
						relativeLayout.setVisibility(View.VISIBLE);
					}


					selectedAdapter2.notifyDataSetChanged();
					selectedAdapter3.notifyDataSetChanged();   
				}
			});        


			listview2.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView arg0, View view,int position, long id) {
					// user clicked a list item, make it "selected" 		        
					selectedAdapter2.setSelectedPosition(position);
					surveyDataObject.putData(secondLevelAttributeType, selectedAdapter2.getItem(position).getAttributeValue());
					

					//Toast.makeText(getApplicationContext(), "LV2 click: " + selectedAdapter2.getItem(position).getOrderName() + " " + selectedAdapter2.getItem(position).getOrderStatus() + " " +selectedAdapter2.getItem(position).getJson(), Toast.LENGTH_SHORT).show();

					mDbHelper.open();			

					Cursor mCursor = mDbHelper.getAttributeValuesByDictionaryTableAndScope(thirdLevelAttributeDictionary,selectedAdapter2.getItem(position).getJson());

					mCursor.moveToFirst();
					while(!mCursor.isAfterLast()) {
						//mArrayList.add(mCursor.getString(mCursor.getColumnIndex(mCursor.getColumnName(1))));

						DBRecord o1 = new DBRecord();		

						if (DEBUG_LOG) Log.d("IDCT", "CURSOR TO ARRAY LIST" + mCursor.getString(mCursor.getColumnIndex(mCursor.getColumnName(1))));
						//String mTitleRaw = mCursor.getString(mCursor.getColumnIndex(mCursor.getColumnName(1)));

						o1.setAttributeDescription(mCursor.getString(0));
						o1.setAttributeValue(mCursor.getString(1));
						o1.setJson(mCursor.getString(2));
						thirdLevelAttributesList.add(o1);
						mCursor.moveToNext();
					}		     
					mDbHelper.close();    

					if (mCursor.getCount() > 0) { 
						listview3.setVisibility(View.VISIBLE);
						RelativeLayout relativeLayout3 = (RelativeLayout) findViewById(R.id.rel3);
						relativeLayout3.setVisibility(View.VISIBLE);
					}
					selectedAdapter3.notifyDataSetChanged();

				}
			});

			listview3.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView arg0, View view,int position, long id) {
					// user clicked a list item, make it "selected" 		        
					selectedAdapter3.setSelectedPosition(position);
					
					//Broken as the column name doesn't map, columen should be strhvi not strvi
					//surveyDataObject.putData(thirdLevelAttributeType, selectedAdapter3.getItem(position).getAttributeValue());
					
					//Toast.makeText(getApplicationContext(), "LV3 click: " + selectedAdapter3.getItem(position).getOrderName() + " " + selectedAdapter3.getItem(position).getOrderStatus() + " " +selectedAdapter3.getItem(position).getJson(), Toast.LENGTH_SHORT).show();

				}
			});

		}		
	}

	public void clearThis() {
		if (DEBUG_LOG) Log.d("IDCT", "clearing stuff");
		selectedAdapter.setSelectedPosition(-1);
		selectedAdapter2.setSelectedPosition(-1);
		selectedAdapter3.setSelectedPosition(-1);	

	}

	public void completeThis() {
		MainTabActivity a = (MainTabActivity)getParent();
		a.completeTab(tabIndex);
	}

}