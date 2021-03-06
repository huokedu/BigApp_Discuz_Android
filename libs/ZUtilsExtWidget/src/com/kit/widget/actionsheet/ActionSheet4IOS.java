package com.kit.widget.actionsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.utils.DensityUtils;
import com.kit.utils.ZogUtils;
import com.kit.widget.actionsheet.ActionSheet.OnActionSheetItemSelected;

/**
 * 
 * @ClassName ActionSheet
 * @Description 非常牛逼的ActionSheet，可以控制ActionSheet Item的个数
 * @author Zhao laozhao1005@gmail.com
 * @date 2014-5-31 上午10:05:59
 * 
 */
public class ActionSheet4IOS {

	public ActionSheet4IOS() {
	}

	/**
	 * 
	 * @Title showSheet
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @param activity
	 *            上下文环境
	 * @param actionSheetConfig
	 *            ActionSheet配置项
	 * @param actionSheetItemSelected
	 *            ActionSheet Item选中监听
	 * @param cancelListener
	 *            取消按钮的监听,无需监听可传null
	 * @return Dialog 返回类型
	 */
	@SuppressLint("NewApi")
	public Dialog show(final Activity activity,
			ActionSheetConfig actionSheetConfig,
			final OnActionSheetItemSelected actionSheetItemSelected,
			OnCancelListener cancelListener) {
		final Dialog dlg = new Dialog(activity, R.style.ActionSheet);
		LinearLayout layout = this.getLayout(activity, dlg, actionSheetConfig,
				actionSheetItemSelected);

		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetItemSelected.onActionSheetItemSelected(activity, 0);
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		// 设置点击弹窗外部取消actionsheet
		dlg.setCanceledOnTouchOutside(true);

		return dlg;
	}

	@SuppressLint("NewApi")
	private void itemsCreat(final Activity activity, final Dialog dlg,
			ActionSheetConfig actionSheetConfig,
			final OnActionSheetItemSelected actionSheetItemSelected,
			LinearLayout layout) {

		LinearLayout ll = (LinearLayout) layout.findViewById(R.id.ll);
		ll.removeAllViews();

		int count = actionSheetConfig.actionSheetStrings.length;

        //使用title
		if (!TextUtils.isEmpty(actionSheetConfig.title)
				|| actionSheetConfig.titleLayout != null) {
			if (actionSheetConfig.titleLayout == null) {
				// 只有提示语
				// 不使用自定义布局

				TextView tv = tvCreate(activity, actionSheetConfig, -1);

				ll.addView(tv);
			} else {
				// 使用自定义布局
				LinearLayout includeLL = new LinearLayout(activity);// 创建LinearLayout布局对象
				includeLL.setOrientation(LinearLayout.VERTICAL);// 设置布局LinearLayout的属性
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				includeLL.setLayoutParams(params);
				includeLL.addView(actionSheetConfig.titleLayout);
				// includeLL.setBackground(mContext.getResources().getDrawable(
				// R.drawable.actionsheet_ios_style_top_selector));
				ll.addView(includeLL);
			}
		}

		for (int index = 1; index < count + 1; index++) {

			TextView tv = tvCreate(activity, actionSheetConfig, index);

			ll.addView(tv);

			final int flag = index;
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					actionSheetItemSelected.onActionSheetItemSelected(activity,
							flag);
					dlg.dismiss();
				}
			});

		}
	}

	@SuppressLint("NewApi")
	private LinearLayout getLayout(final Activity activity, final Dialog dlg,
			ActionSheetConfig actionSheetConfig,
			final OnActionSheetItemSelected actionSheetItemSelected) {

		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.widget_actionsheet_ios_style, null);

		// switch (actionSheetConfig.actionsheetStyle) {
		//
		// case ACTIONSHEET_IOS:
		// layout = (LinearLayout) inflater.inflate(
		// R.layout.widget_actionsheet_ios_style, null);
		//
		// break;
		// case ACTIONSHEET_WECHAT:
		// layout = (LinearLayout) inflater.inflate(
		// R.layout.widget_actionsheet_wechat_style, null);
		// break;
		// default:
		// layout = (LinearLayout) inflater.inflate(
		// R.layout.widget_actionsheet_wechat_style, null);
		// }

		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		itemsCreat(activity, dlg, actionSheetConfig, actionSheetItemSelected,
				layout);

		return layout;
	}

	private TextView tvCreate(Context mContext,
			ActionSheetConfig actionSheetConfig, int index) {

		int count = actionSheetConfig.actionSheetStrings.length;

		TextView tv = new TextView(mContext);
		// tv.setId(index);

		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				DensityUtils.dip2px(mContext, 42)));

		tv.setGravity(Gravity.CENTER);

		if (index == -1) {
			// 设置tips 的颜色、大小
			tv.setTextColor(mContext.getResources().getColor(R.color.gray));
			tv.setTextSize(16.0f);
			tv.setText(actionSheetConfig.title);
		} else {
			try {
				tv.setTextColor(actionSheetConfig.colors[index - 1]);
			} catch (Exception e) {
				tv.setTextColor(0xff037bff);
				ZogUtils.showException(e);
			}
			try {
				tv.setTextSize(actionSheetConfig.actionSheetTextSize[index - 1]);
			} catch (Exception e) {
				tv.setTextSize(16.0f);
				ZogUtils.showException(e);
			}
			tv.setText(actionSheetConfig.actionSheetStrings[index - 1]);
		}

		setBackground(mContext, actionSheetConfig, tv, count, index);
		return tv;
	}

	@SuppressLint("NewApi")
	private void setBackground(Context mContext,
			ActionSheetConfig actionSheetConfig, TextView tv, int count,
			int index) {
		if (!TextUtils.isEmpty(actionSheetConfig.title)
				|| actionSheetConfig.titleLayout != null) {
			// 有tips

			if (index == -1) {
				tv.setBackground(mContext.getResources().getDrawable(
						R.drawable.actionsheet_ios_style_top_selector));
			} else {
				if (index == count) {
					// 最后一条 圆角底部背景
					tv.setBackground(mContext.getResources().getDrawable(
							R.drawable.actionsheet_ios_style_bottom_selector));
				} else {
					// 中间 直角底部背景
					tv.setBackground(mContext.getResources().getDrawable(
							R.drawable.actionsheet_ios_style_middle_selector));
				}
			}

		} else {
			if (count == 1) {
				// actionsheet item只有一个
				tv.setBackground(mContext.getResources().getDrawable(
						R.drawable.actionsheet_ios_style_single_selector));
			} else {
				// actionsheet item有两个(包含两个)以上
				if (index == 1) {
					// 第一条 圆角头部背景
					tv.setBackground(mContext.getResources().getDrawable(
							R.drawable.actionsheet_ios_style_top_selector));

				} else if (index == count) {
					// 最后一条 圆角底部背景
					tv.setBackground(mContext.getResources().getDrawable(
							R.drawable.actionsheet_ios_style_bottom_selector));
				} else {
					// 中间 直角底部背景
					tv.setBackground(mContext.getResources().getDrawable(
							R.drawable.actionsheet_ios_style_middle_selector));
				}
			}
		}
	}

}
