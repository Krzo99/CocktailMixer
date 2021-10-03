package com.dKrzmanc.coctailmixer.ui.alctypes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ResizeAnimation;
import com.dKrzmanc.coctailmixer.ui.recipes.CocktailListItem;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AlcoholListAdapter extends RecyclerView.Adapter<AlcoholListAdapter.ListViewHolder> {
    //mDataset never changes!, It only story every alcohol
    private ArrayList<AlcoholListItem> mDataset;
    private ArrayList<AlcoholListItem> mSortedAlcohols;
    LinearLayout AlcoholLinLay;
    TextView AlcoholDescText;

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ListViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    public void filter(String text) {
        mSortedAlcohols.clear();
        if(text.isEmpty()){
            mSortedAlcohols.addAll(mDataset);
        } else{
            text = text.toLowerCase();
            for(AlcoholListItem item: mDataset){
                if(item.Title.toLowerCase().contains(text)){
                    mSortedAlcohols.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public AlcoholListAdapter(ArrayList<AlcoholListItem> AlcoholItems) {
        mDataset = AlcoholItems;
        mSortedAlcohols = (ArrayList)mDataset.clone();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public AlcoholListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alcohol_list_entry_layout, parent, false);


        return new AlcoholListAdapter.ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlcoholListAdapter.ListViewHolder holder, int position) {

        final AlcoholListItem ThisItem = mSortedAlcohols.get(position);
        final View root = holder.mView;

        TextView Title = root.findViewById(R.id.Alcohol_name);
        TextView Description = root.findViewById(R.id.AlcoholDesc);
        ImageView Img = root.findViewById(R.id.Alcohol_Image);
        AlcoholLinLay = root.findViewById(R.id.LinAlcoholDesc);
        AlcoholLinLay.getLayoutParams().height = ThisItem.CurrentHeightOfItem;

        final LinearLayout AlcoholHeader = root.findViewById(R.id.LinAlcoholList);

        AlcoholHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlcoholLinLay = root.findViewById(R.id.LinAlcoholDesc);
                AlcoholDescText = root.findViewById(R.id.AlcoholDesc);
                int CurrentHeight = AlcoholLinLay.getLayoutParams().height;
                if (CurrentHeight == 0) {
                    int measuredHeight = getHeight(AlcoholDescText.getContext(), AlcoholDescText.getText(),(int)AlcoholDescText.getTextSize(), AlcoholDescText.getLayout().getWidth(), AlcoholDescText.getTypeface(), (int)dpToPix(32));
                    ThisItem.CurrentHeightOfItem = measuredHeight;
                    //  TODO: Expanded alcohol list works on al devices, and isn't just hard coded!!! Still, could be better!
                    ThisItem.CurrentHeightOfItem = measuredHeight;
                    final ResizeAnimation BiggerheigthAnimation = new ResizeAnimation(
                            AlcoholLinLay,
                            ThisItem.CurrentHeightOfItem,
                            0
                    );
                    BiggerheigthAnimation.setDuration(200);
                    AlcoholLinLay.startAnimation(BiggerheigthAnimation);
                }
                else {
                    AlcoholDescText.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ResizeAnimation SmallerHeigthAnimation = new ResizeAnimation(
                            AlcoholLinLay,
                            0,
                            ThisItem.CurrentHeightOfItem
                    );
                    SmallerHeigthAnimation.setDuration(200);
                    AlcoholLinLay.startAnimation(SmallerHeigthAnimation);

                    ThisItem.CurrentHeightOfItem = 0;

                }

            }
        });

        //  Get image
        String ImgPath = ThisItem.Title.replace(" ", "_").toLowerCase();

        //Handle special cases here:
        ImgPath = ImgPath.replace("-", "_");

        Title.setText(ThisItem.Title);
        Description.setText(ThisItem.Description);

        Context context = holder.mView.getContext();
        int resID = context.getResources().getIdentifier(ImgPath , "drawable", context.getPackageName());
        Img.setImageResource(resID);

    }

    public static int getHeight(Context context, CharSequence text, int textSize, int textWidth, Typeface typeface, int padding) {
        Log.e("textSize: ", textSize+"");
        Log.e("textWidth: ", textWidth+"");
        Log.e("padding: ", padding+"");

        TextView textView = new TextView(context);
        textView.setPadding(padding,padding,padding,padding);
        textView.setTypeface(typeface);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(textWidth - 2*padding, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

    private static float dpToPix(int dp)
    {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        return pixels;
    }

    @Override
    public int getItemCount() {
        return mSortedAlcohols.size();
    }
}
