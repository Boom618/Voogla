package com.ty.voogla.widght;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ty.voogla.R;
import com.ty.voogla.util.WindowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Weavey
 * @date 2016/9/3
 * 供应商 选择
 */
public class NormalSelectionDialog {

    private Dialog mDialog;
    private View dialogView;
    private TextView title;
    private Button bottomBtn;
    private LinearLayout linearLayout;

    private Builder mBuilder;
    private List<String> datas;
    /**
     * 最后一次选择的位置
     */
    private int clickPosition;

    public NormalSelectionDialog(Builder builder) {

        this.mBuilder = builder;
        mDialog = new Dialog(mBuilder.getContext(), R.style.bottomDialogStyle);
        dialogView = View.inflate(mBuilder.getContext(), R.layout.widget_bottom_dialog, null);
        // 一定要在setAttributes(lp)之前才有效
        mDialog.setContentView(dialogView);

        //设置dialog的宽
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int tempWidth = WindowUtil.INSTANCE.getScreenWidth();
        lp.width = (int) (tempWidth * builder.getItemWidth());
        lp.gravity = Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);


        title = dialogView.findViewById(R.id.action_dialog_title);
        linearLayout = dialogView.findViewById(R.id.action_dialog_linearlayout);
        bottomBtn = dialogView.findViewById(R.id.action_dialog_botbtn);
        bottomBtn.setText(builder.getCancleButtonText());
        bottomBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mDialog.dismiss();
            }
        });
        mDialog.setCanceledOnTouchOutside(builder.isTouchOutside());
    }

    //根据数据生成item
    private void loadItem() {

        //设置标题
        if (mBuilder.getTitleVisible()) {

            title.setVisibility(View.VISIBLE);
            title.setText(mBuilder.getTitleText());
            title.setTextColor(mBuilder.getTitleTextColor());
            title.setTextSize(mBuilder.getTitleTextSize());
            LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) title.getLayoutParams();
//            l.height = ResourceUtil.dip2px(mBuilder.getTitleHeight());
            l.height = (int) WindowUtil.INSTANCE.dp2px(mBuilder.getTitleHeight());
            title.setLayoutParams(l);

            if (datas.size() != 0) {
                title.setBackgroundResource(R.drawable.selector_widget_actiondialog_top);

            } else {
                title.setBackgroundResource(R.drawable.selector_widget_actiondialog_single);
            }
        } else {

            title.setVisibility(View.GONE);
        }

        //设置底部“取消”按钮
        bottomBtn.setTextColor(mBuilder.getItemTextColor());
        bottomBtn.setTextSize(mBuilder.getItemTextSize());
        LinearLayout.LayoutParams btnlp = new LinearLayout.LayoutParams(AbsListView.LayoutParams
                .MATCH_PARENT, mBuilder.getItemHeight());
        btnlp.topMargin = 10;
        bottomBtn.setLayoutParams(btnlp);

        //设置数据item
        if (datas.size() == 1) {

            Button button = getButton(datas.get(0), 0);
            if (mBuilder.getTitleVisible()) {
                button.setBackgroundResource(R.drawable.selector_widget_actiondialog_bottom);
            } else {
                button.setBackgroundResource(R.drawable.selector_widget_actiondialog_single);
            }

            linearLayout.addView(button);

        } else if (datas.size() > 1) {

            for (int i = 0; i < datas.size(); i++) {

                Button button = getButton(datas.get(i), i);
                if (!mBuilder.getTitleVisible() && i == 0) {
                    button.setBackgroundResource(R.drawable.selector_widget_actiondialog_top);
                } else {
                    if (i != datas.size() - 1) {
                        button.setBackgroundResource(R.drawable.selector_widget_actiondialog_middle);
                    } else {
                        button.setBackgroundResource(R.drawable.selector_widget_actiondialog_bottom);
                    }
                }
                linearLayout.addView(button);
            }
        }


    }

    private Button getButton(String text, int position) {

        // 动态生成选择按钮
        final Button button = new Button(mBuilder.getContext());
        button.setText(text);
        button.setTag(position);
        button.setTextColor(mBuilder.getItemTextColor());
        button.setTextSize(mBuilder.getItemTextSize());
        button.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams
                .MATCH_PARENT, mBuilder.getItemHeight()));
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mBuilder.getOnItemListener() != null) {

                    clickPosition = Integer.parseInt(button.getTag().toString());

                    //mBuilder.getOnItemListener().onItemClick(NormalSelectionDialog.this, button,clickPosition);

                    mBuilder.getOnItemListener().onClick(mDialog,clickPosition);

                }
            }
        });

        return button;
    }

    public NormalSelectionDialog setDatas(List<String> datas) {

        int count = linearLayout.getChildCount();
        if (count > 1) {
            linearLayout.removeViewsInLayout(1, count - 1);
        }
//
        this.datas = (datas == null ? new ArrayList<String>() : datas);
        loadItem();
        return this;
    }

    public List<String> getDatas() {

        return datas == null ? new ArrayList<String>() : datas;
    }

    public int getFinalClickPosition() {

        return this.clickPosition;
    }

    public void show() {

        mDialog.show();

    }

    public void dismiss() {

        mDialog.dismiss();
    }

    public Dialog getDialog() {

        return mDialog;
    }

    public static class Builder {

        //标题属性
        private boolean boolTitle;
        private int titleHeight;
        private String titleText;
        private int titleTextColor;
        private float titleTextSize;
        //item属性
        private DialogInterface.OnClickListener onItemListener;
        private int itemHeight;
        private float itemWidth;
        private int itemTextColor;
        private float itemTextSize;
        //取消按钮属性
        private String cancleButtonText;

        private boolean isTouchOutside;
        private Context mContext;

        public Builder(Context context) {

            mContext = context;

            // 默认关闭标题
            boolTitle = false;
            // 默认标题高度  dp
            titleHeight = 65;
            titleText = "请选择";
            titleTextColor = ContextCompat.getColor(context, R.color.black_light);
            titleTextSize = 13;

            onItemListener = null;
            // 默认item高度
//            itemHeight = ResourceUtil.dip2px(45);
            itemHeight = (int) WindowUtil.INSTANCE.dp2px(45);
            itemWidth = 0.92f;
            // 默认字体颜色
            itemTextColor = ContextCompat.getColor(mContext, R.color.black_light);
            //默认自体大小
            itemTextSize = 14;

            cancleButtonText = "取消";
            isTouchOutside = true;
        }

        public Context getContext() {
            return mContext;
        }

        public boolean getTitleVisible() {
            return boolTitle;
        }

        public Builder setlTitleVisible(boolean isVisible) {
            this.boolTitle = isVisible;
            return this;
        }

        public int getTitleHeight() {
            return titleHeight;
        }

        public Builder setTitleHeight(int dp) {
            this.titleHeight = dp;
            return this;
        }

        public String getTitleText() {
            return titleText;
        }

        public Builder setTitleText(String titleText) {
            this.titleText = titleText;
            return this;
        }

        public int getTitleTextColor() {
            return titleTextColor;
        }

        public Builder setTitleTextColor(@ColorRes int titleTextColor) {
            this.titleTextColor = ContextCompat.getColor(mContext, titleTextColor);
            return this;
        }

        public float getTitleTextSize() {
            return titleTextSize;
        }

        public Builder setTitleTextSize(int sp) {

            this.titleTextSize = sp;
            return this;
        }

        public DialogInterface.OnClickListener getOnItemListener() {
            return onItemListener;
        }

        public Builder setOnItemListener(DialogInterface.OnClickListener onItemListener) {
            this.onItemListener = onItemListener;
            return this;
        }

        public int getItemHeight() {
            return itemHeight;
        }

        public Builder setItemHeight(int dp) {
//            this.itemHeight = ResourceUtil.dip2px(dp);
            this.itemHeight = (int) WindowUtil.INSTANCE.dp2px(dp);
            return this;
        }

        public float getItemWidth() {
            return itemWidth;
        }

        public Builder setItemWidth(float itemWidth) {
            this.itemWidth = itemWidth;
            return this;
        }

        public int getItemTextColor() {

            return itemTextColor;
        }

        public Builder setItemTextColor(@ColorRes int itemTextColor) {

            this.itemTextColor = ContextCompat.getColor(mContext, itemTextColor);
            return this;
        }

        public float getItemTextSize() {
            return itemTextSize;
        }

        public Builder setItemTextSize(int sp) {
            this.itemTextSize = sp;
            return this;
        }

        public String getCancleButtonText() {
            return cancleButtonText;
        }

        public Builder setCancleButtonText(String cancleButtonText) {
            this.cancleButtonText = cancleButtonText;
            return this;
        }

        public boolean isTouchOutside() {
            return isTouchOutside;
        }

        public Builder setCanceledOnTouchOutside(boolean isTouchOutside) {

            this.isTouchOutside = isTouchOutside;
            return this;
        }

        public NormalSelectionDialog build() {

            return new NormalSelectionDialog(this);
        }
    }

}