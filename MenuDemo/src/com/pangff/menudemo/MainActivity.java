package com.pangff.menudemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.pangff.menudemo.MenuBarView.OnMenuItemClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MenuBarView menuBarView = (MenuBarView) this
				.findViewById(R.id.menuBarView);
		menuBarView.setMenuBarBackground(R.drawable.menu_bar_bg);
		List<MenuInfo> list = new ArrayList<MenuInfo>();
		for (int i = 0; i < 3; i++) {
			MenuInfo info = new MenuInfo();
			if (i == 0) {
				info.setName("发表");
				info.setResId(R.drawable.menu_item_send);
			}
			if (i == 1) {
				info.setName("买入");
				info.setResId(R.drawable.menu_item_buy);
			}
			if (i == 2) {
				info.setName("卖出");
				info.setResId(R.drawable.menu_item_sell);
			}
			list.add(info);
		}
		menuBarView.setMenuList(list);
		menuBarView.setTriggerImageResource(R.drawable.menu_trigger_selector);
		menuBarView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void OnMenuItemClick(View view, int position,int resId) {
				Toast.makeText(MainActivity.this, "点击了第:"+position+"个菜单，它的icon drawable ID是:"+resId, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
