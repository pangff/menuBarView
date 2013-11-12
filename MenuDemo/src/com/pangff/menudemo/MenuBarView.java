package com.pangff.menudemo;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MenuBarView extends FrameLayout implements OnClickListener{

	private ImageView triggerBtnView;
	private LinearLayout menuBar;
	private List<MenuInfo> menuList;
	private Activity ctx;
	private LinearLayout menuBarLayout;
	private OnMenuItemClickListener onMenuItemClickListener;
	
	public interface OnMenuItemClickListener{
		public void OnMenuItemClick(View view,int position,int drawableId);
	}
	
	public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener){
		this.onMenuItemClickListener = onMenuItemClickListener;
	}
	
	public MenuBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ctx = (Activity) context;
		
		menuBarLayout = new LinearLayout(context);
		LayoutParams lp_l = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp_l.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;
		this.addView(menuBarLayout,lp_l);
		
		menuBar = new LinearLayout(context);
		LayoutParams lp_m = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp_m.gravity=Gravity.CENTER_VERTICAL;
		menuBarLayout.addView(menuBar,lp_m);
		menuBar.setVisibility(View.INVISIBLE);
		
		
		triggerBtnView = new ImageView(context);
		triggerBtnView.setId(R.id.triggerBtnView);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;
		
		this.addView(triggerBtnView,lp);
		
		triggerBtnView.setOnClickListener(this);
		
	}
	
	
	public void setMenuBarBackground(int id){
		menuBar.setBackgroundResource(id);
	}
	
	public void setTriggerImageResource(int id){
		triggerBtnView.setBackgroundResource(id);
		triggerBtnView.post(new Runnable() {
			
			@Override
			public void run() {
				menuBar.setPadding(triggerBtnView.getWidth()/4,0,25, 5);
				menuBarLayout.setPadding(triggerBtnView.getWidth()/2, 0, 0, 0);
			}
		});
	}

	public void setMenuList(List<MenuInfo> list){
		if(list!=null){
			menuList =  list;
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
			for(int i=0;i<menuList.size();i++){
				lp.weight=1;
				lp.leftMargin=50;
				menuBar.addView(createMenuItem(menuList.get(i),i),lp);
			}
		}else{
			Log.e("MenuBarView", "聊股菜单没有数据");
		}
	}
	
	private View createMenuItem(final MenuInfo menuInfo,final int index){
		ImageView menuIcon = new ImageView(ctx);
		menuIcon.setImageResource(menuInfo.getResId());
		
		int id = ctx.getResources().getIdentifier("menuInfo" + index,"values", ctx.getPackageName());
		menuIcon.setId(id);
		
		TextView textView = new TextView(ctx);
		textView.setTextSize(12);
		textView.getPaint().setFakeBoldText(true); 
		textView.setTextColor(Color.WHITE);
		textView.setText(menuInfo.getName());
		
		LinearLayout menuItem = new LinearLayout(ctx);
		
		menuItem.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp_icon = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp_icon.gravity=Gravity.CENTER;
		menuItem.addView(menuIcon,lp_icon);
		
		LinearLayout.LayoutParams lp_text = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp_text.gravity=Gravity.CENTER;
		menuItem.addView(textView,lp_text);
		
		menuItem.setVisibility(View.INVISIBLE);
		
		menuItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				endItemAnimation(0);
				if(onMenuItemClickListener!=null){
					onMenuItemClickListener.OnMenuItemClick(v, index,menuInfo.getResId());
				}
			}
		});
		return menuItem;
	}

	boolean isOpen = false;
	boolean isClicked = false;
	@Override
	public void onClick(View v) {
		if(triggerBtnView.equals(v)){
			if(!isClicked){
				isClicked = true;
				if(!isOpen){
					openAnimation();
				}else{
					endItemAnimation(0);
				}
			}
		}
	}
	
	private void openAnimation(){
		final TranslateAnimation translatItem = new TranslateAnimation(-menuBar.getWidth(),0,0,0);
		translatItem.setDuration(300);
		menuBar.startAnimation(translatItem);
		
		translatItem.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				menuBar.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				popItemAnimation(menuBar.getChildCount()-1);
			}
		});
	}
	
	private void closeAnimation(){
		final TranslateAnimation translatItem = new TranslateAnimation(0,-menuBar.getWidth(),0,0);
		translatItem.setDuration(300);
		menuBar.startAnimation(translatItem);
		
		translatItem.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				isOpen = false;
				isClicked = false;
				menuBar.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	private void popItemAnimation(final int index){
		if(index<0){
			isClicked = false;
			isOpen = true;
			return;
		}
		RotateAnimation roateItem = new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
		final TranslateAnimation translatItem = new TranslateAnimation(-menuBar.getChildAt(index).getX(),0, 0,0);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(roateItem);
		set.setDuration(300);
		set.addAnimation(translatItem);
		menuBar.getChildAt(index).startAnimation(set);
		set.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				menuBar.getChildAt(index).setVisibility(View.VISIBLE);
				int nextIndex = index-1;
				popItemAnimation(nextIndex);
			}
		});
	}
	
	
	private void endItemAnimation(final int index){
		if(index>=menuBar.getChildCount()){
			closeAnimation();
			return;
		}
		RotateAnimation roateItem = new RotateAnimation(360f,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
		final TranslateAnimation translatItem = new TranslateAnimation(0,-menuBar.getChildAt(index).getX(),0,0);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(roateItem);
		set.setDuration(300);
		set.addAnimation(translatItem);
		menuBar.getChildAt(index).startAnimation(set);
		set.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				menuBar.getChildAt(index).setVisibility(View.INVISIBLE);
				int nextIndex = index+1;
				endItemAnimation(nextIndex);
			}
		});
	}
}
