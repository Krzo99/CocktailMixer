package com.dKrzmanc.coctailmixer.ui.recipes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ResizeAnimation;
import com.dKrzmanc.coctailmixer.ui.make_own.MakeOwnFragment;

import java.util.ArrayList;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CocktailListAdapter extends RecyclerView.Adapter<CocktailListAdapter.ListViewHolder> {
    //Dataset never changes, contains all cocktails available!
    private ArrayList<CocktailListItem> mDataset;
    private Fragment CalledFrom;
    LinearLayout CocktailIng;
    private ArrayList<CocktailListItem> mSortedCoctails;

    //Only here so we dont have to make it "final" in onBindViewHolder
    private CocktailListItem CurrentlyEditedItem;

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ListViewHolder(View v) {
            super(v);
            mView = v;
        }
    }


    public CocktailListAdapter(ArrayList<CocktailListItem> CocktailItems, Fragment WhereWasItCalledFrom) {
        mDataset = CocktailItems;
        mSortedCoctails = (ArrayList)mDataset.clone();;
        CalledFrom = WhereWasItCalledFrom;
    }

    public void filter(String text) {
        mSortedCoctails.clear();
        if(text.isEmpty()){
            mSortedCoctails.addAll(mDataset);
        } else{
            text = text.toLowerCase();
            for(CocktailListItem item: mDataset){
                if(item.Title.toLowerCase().contains(text) || item.doIngsContain(text)){
                    mSortedCoctails.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CocktailListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cocktail_list_entry_layout, parent, false);


        return new CocktailListAdapter.ListViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (CalledFrom.getClass() == RecipesFragment.class) {
            CurrentlyEditedItem = mSortedCoctails.get(position);
        }
        else if (CalledFrom.getClass() == MakeOwnFragment.class) {
            CurrentlyEditedItem = mDataset.get(position);
        }
        else {
            return;
        }

        final CocktailListItem ThisItem = CurrentlyEditedItem;
        final View root = holder.mView;

        TextView Title = root.findViewById(R.id.Cocktail_name);
        ImageView Image = root.findViewById(R.id.Cocktail_image);
        CocktailIng = root.findViewById(R.id.LinCocktailDesc);
        CocktailIng.getLayoutParams().height = ThisItem.CurrentHeightOfItem;

        TextView CocktailDescText = root.findViewById(R.id.CocktailDesc);
        final LinearLayout CocktailCardRoot = root.findViewById(R.id.VertCoctailCard);

        LinearLayout CocktailHeader = root.findViewById(R.id.LinCocktailList);

        CocktailHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CocktailIng = root.findViewById(R.id.LinCocktailDesc);
                int CurrentHeight = CocktailIng.getLayoutParams().height;
                if (CurrentHeight == 0) {
                    CocktailIng.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ThisItem.CurrentHeightOfItem = CocktailIng.getMeasuredHeight();
                    final ResizeAnimation BiggerheigthAnimation = new ResizeAnimation(
                            CocktailIng,
                            ThisItem.CurrentHeightOfItem,
                            0
                    );
                    BiggerheigthAnimation.setDuration(200);
                    CocktailIng.startAnimation(BiggerheigthAnimation);
                }
                else {
                    CocktailIng.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ResizeAnimation SmallerHeigthAnimation = new ResizeAnimation(
                            CocktailIng,
                            0,
                            CocktailIng.getHeight()
                    );
                    SmallerHeigthAnimation.setDuration(200);
                    CocktailIng.startAnimation(SmallerHeigthAnimation);

                    ThisItem.CurrentHeightOfItem = 0;

                }

            }
        });

        //  Get image
        String ImgPath = ThisItem.Title.replace(" ", "_").toLowerCase();

        //Handle special cases here:
        ImgPath = ImgPath.replace("ñ", "n");

        Title.setText(ThisItem.Title);


        boolean bCalledFromHome = CalledFrom.getClass() == RecipesFragment.class;
        if (!ThisItem.MissingIng.equals("") && !bCalledFromHome)
        {
            Title.setTextColor(root.getResources().getColor(R.color.LightRed));
        }
        else
        {
            Title.setTextColor(Color.WHITE);
        }

        Context context = holder.mView.getContext();
        int resID = context.getResources().getIdentifier(ImgPath , "drawable", context.getPackageName());
        Image.setImageResource(resID);

        String Desc = ThisItem.GenerateDescription(bCalledFromHome);
        CocktailDescText.setText(HtmlCompat.fromHtml(Desc, HtmlCompat.FROM_HTML_MODE_LEGACY));

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (CalledFrom.getClass() == RecipesFragment.class) {
            return mSortedCoctails.size();
        }
        else if (CalledFrom.getClass() == MakeOwnFragment.class) {
            return mDataset.size();
        }
        else
        {
            return 0;
        }

    }

}
