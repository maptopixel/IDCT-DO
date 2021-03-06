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
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class LLRS_Selection_Longitudinal_Transverse_Form extends EQForm {
	
	private static final String TAG = "IDCT";
	public boolean DEBUG_LOG = false; 
	
	
	
	public TabActivity tabActivity;
	public TabHost tabHost;
	public int tabIndex = 4;
	

	private String topLevelAttributeDictionary = "DIC_LLRS";
	private String secondLevelAttributeDictionary = "DIC_LLRS_DUCTILITY";
	
	private String topLevelAttributeKeyLongitudinal = "LLRS_L";
	private String topLevelAttributeKeyTransverse = "LLRS_T";
	private String secondLevelAttributeKeyLongitudinal = "LLRS_DUCT_L";	
	private String secondLevelAttributeKeyTransverse = "LLRS_DUCT_T";
	
	private SelectedAdapter selectedAdapter;
	private SelectedAdapter selectedAdapter2;
	private SelectedAdapter selectedAdapter3;
	private SelectedAdapter selectedAdapter4;
	private SelectedAdapter selectedAdapter5;

	private ArrayList list;
	public ArrayList<DBRecord> lLrsd;



	ListView listview;
	ListView listview2;
	ListView listview3;
	ListView listview4;
	ListView listview5;

	public GemDbAdapter mDbHelper;
	public GEMSurveyObject surveyDataObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.llrs_selectable_list_longitudinal_transverse);
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

			Cursor allLLRSCursor = mDbHelper.getAttributeValuesByDictionaryTable(topLevelAttributeDictionary);        
			ArrayList<DBRecord> lLrs = GemUtilities.cursorToArrayList(allLLRSCursor);        
			if (DEBUG_LOG) Log.d(TAG,"lLrs: " + lLrs.toString());


			Cursor allLLRSDCursor = mDbHelper.getAttributeValuesByDictionaryTable(secondLevelAttributeDictionary); 
			lLrsd = GemUtilities.cursorToArrayList(allLLRSDCursor);


			mDbHelper.close();

			
			selectedAdapter = new SelectedAdapter(this,0,lLrs);
			selectedAdapter.setNotifyOnChange(true);

			listview = (ListView) findViewById(R.id.listLLRS);
			listview.setAdapter(selectedAdapter);        


			selectedAdapter2 = new SelectedAdapter(this,0,lLrs);    		
			selectedAdapter2.setNotifyOnChange(true);		
			listview2 = (ListView) findViewById(R.id.listLLRSLongitudinal);
			listview2.setAdapter(selectedAdapter2);        


			selectedAdapter3 = new SelectedAdapter(this,0,lLrsd);    		
			selectedAdapter3.setNotifyOnChange(true);		
			listview3 = (ListView) findViewById(R.id.listLLRSLongitudinalDuctility);
			listview3.setAdapter(selectedAdapter3);        
			
			selectedAdapter4 = new SelectedAdapter(this,0,lLrs);    		
			selectedAdapter4.setNotifyOnChange(true);		
			listview4 = (ListView) findViewById(R.id.listLLRSTransverse);
			listview4.setAdapter(selectedAdapter4);        

			
			selectedAdapter5 = new SelectedAdapter(this,0,lLrsd);    		
			selectedAdapter5.setNotifyOnChange(true);		
			listview5 = (ListView) findViewById(R.id.listLLRSTransverseDuctility);
			listview5.setAdapter(selectedAdapter5);        

			//listview2.setVisibility(View.INVISIBLE);



			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView arg0, View view,
						int position, long id) {
					// user clicked a list item, make it "selected"
					selectedAdapter.setSelectedPosition(position);
					//surveyDataObject.putData(topLevelAttributeKeyLongitudinal, selectedAdapter.getItem(position).getAttributeValue());	

					//Toast.makeText(getApplicationContext(), "Item clicked: " + selectedAdapter.getItem(position).getOrderName() + " " + selectedAdapter.getItem(position).getOrderStatus() + " " +selectedAdapter.getItem(position).getJson(), Toast.LENGTH_SHORT).show();

				}
			});        


			listview2.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView arg0, View view,int position, long id) {
					// user clicked a list item, make it "selected" 		        
					selectedAdapter2.setSelectedPosition(position);
					surveyDataObject.putData(topLevelAttributeKeyLongitudinal, selectedAdapter2.getItem(position).getAttributeValue());					
					//Toast.makeText(getApplicationContext(), "LV2 click: " + selectedAdapter2.getItem(position).getOrderName() + " " + selectedAdapter2.getItem(position).getOrderStatus() + " " +selectedAdapter2.getItem(position).getJson(), Toast.LENGTH_SHORT).show();
					completeThis();		
					
				}
			});

			listview3.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView arg0, View view,int position, long id) {
					// user clicked a list item, make it "selected" 		        
					selectedAdapter3.setSelectedPosition(position);
					surveyDataObject.putData(secondLevelAttributeKeyLongitudinal, selectedAdapter3.getItem(position).getAttributeValue());					
					//Toast.makeText(getApplicationContext(), "LV2 click: " + selectedAdapter2.getItem(position).getOrderName() + " " + selectedAdapter2.getItem(position).getOrderStatus() + " " +selectedAdapter2.getItem(position).getJson(), Toast.LENGTH_SHORT).show();
					completeThis();		
					
				}
			});
			listview4.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView arg0, View view,int position, long id) {
					// user clicked a list item, make it "selected" 		        
					selectedAdapter4.setSelectedPosition(position);
					surveyDataObject.putData(topLevelAttributeKeyTransverse, selectedAdapter4.getItem(position).getAttributeValue());					
		
					
				}
			});
			listview5.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView arg0, View view,int position, long id) {
					// user clicked a list item, make it "selected" 		        
					selectedAdapter5.setSelectedPosition(position);
					surveyDataObject.putData(secondLevelAttributeKeyTransverse, selectedAdapter5.getItem(position).getAttributeValue());					
					//Toast.makeText(getApplicationContext(), "LV2 click: " + selectedAdapter2.getItem(position).getOrderName() + " " + selectedAdapter2.getItem(position).getOrderStatus() + " " +selectedAdapter2.getItem(position).getJson(), Toast.LENGTH_SHORT).show();
					completeThis();		
					
				}
			});
		}		
    }
	
	public void clearThis() {
		if (DEBUG_LOG) Log.d(TAG, "clearing stuff");
		selectedAdapter.setSelectedPosition(-1);
		selectedAdapter2.setSelectedPosition(-1);
	}
	

	public void completeThis() {
		MainTabActivity a = (MainTabActivity)getParent();
		a.completeTab(tabIndex);
	}
	
	@Override
	public void onBackPressed() {
		if (DEBUG_LOG) Log.d(TAG,"back button pressed");
		MainTabActivity a = (MainTabActivity)getParent();
		a.backButtonPressed();
	}
	
	

}