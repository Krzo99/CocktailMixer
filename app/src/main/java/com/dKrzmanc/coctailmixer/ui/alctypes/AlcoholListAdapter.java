package com.dKrzmanc.coctailmixer.ui.alctypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dKrzmanc.coctailmixer.CocktailListAdapter;
import com.dKrzmanc.coctailmixer.CocktailListItem;
import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ResizeAnimation;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class AlcoholListAdapter extends RecyclerView.Adapter<AlcoholListAdapter.ListViewHolder> {
    private ArrayList<AlcoholListItem> mDataset;
    LinearLayout AlcoholIng;

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ListViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    public AlcoholListAdapter(ArrayList<AlcoholListItem> AlcoholItems, Fragment WhereWasItCalledFrom) {
        mDataset = AlcoholItems;
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

        final AlcoholListItem ThisItem = mDataset.get(position);
        final View root = holder.mView;

        TextView Title = root.findViewById(R.id.Alcohol_name);
        TextView Description = root.findViewById(R.id.AlcoholDesc);
        ImageView Img = root.findViewById(R.id.Alcohol_Image);
        AlcoholIng = root.findViewById(R.id.LinAlcoholDesc);
        AlcoholIng.getLayoutParams().height = ThisItem.CurrentHeightOfItem;

        LinearLayout AlcoholHeader = root.findViewById(R.id.LinAlcoholList);

        AlcoholHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlcoholIng = root.findViewById(R.id.LinAlcoholDesc);
                int CurrentHeight = AlcoholIng.getLayoutParams().height;
                if (CurrentHeight == 0) {
                    AlcoholIng.measure(0, View.MeasureSpec.UNSPECIFIED);

                    //  TODO: Expanded alcohol list works on al devices, and isn't just hard coded!!!
                    ThisItem.CurrentHeightOfItem = AlcoholIng.getMeasuredHeight() * ThisItem.ExpandedSize;
                    final ResizeAnimation BiggerheigthAnimation = new ResizeAnimation(
                            AlcoholIng,
                            ThisItem.CurrentHeightOfItem,
                            0
                    );
                    BiggerheigthAnimation.setDuration(200);
                    AlcoholIng.startAnimation(BiggerheigthAnimation);
                }
                else {
                    AlcoholIng.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ResizeAnimation SmallerHeigthAnimation = new ResizeAnimation(
                            AlcoholIng,
                            0,
                            AlcoholIng.getHeight()
                    );
                    SmallerHeigthAnimation.setDuration(200);
                    AlcoholIng.startAnimation(SmallerHeigthAnimation);

                    ThisItem.CurrentHeightOfItem = 0;

                }

            }
        });

        //  Get image
        String ImgPath = ThisItem.Title.replace(" ", "_").toLowerCase();
        Title.setText(ThisItem.Title);
        Description.setText(ThisItem.Description);

        Context context = holder.mView.getContext();
        int resID = context.getResources().getIdentifier(ImgPath , "drawable", context.getPackageName());
        Img.setImageResource(resID);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
