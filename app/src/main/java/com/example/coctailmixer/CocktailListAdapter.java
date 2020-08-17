package com.example.coctailmixer;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coctailmixer.ui.gallery.GalleryFragment;
import com.example.coctailmixer.ui.home.HomeFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CocktailListAdapter extends RecyclerView.Adapter<CocktailListAdapter.ListViewHolder> {
    private ArrayList<CocktailListItem> mDataset;
    private Fragment CalledFrom;

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
        CalledFrom = WhereWasItCalledFrom;
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
        final CocktailListItem ThisItem = mDataset.get(position);

        View root = holder.mView;

        TextView Title = root.findViewById(R.id.Cocktail_name);
        ImageView Image = root.findViewById(R.id.Cocktail_image);
        final LinearLayout CocktailIng = root.findViewById(R.id.LinCocktailDesc);
        TextView CocktailDescText = root.findViewById(R.id.CocktailDesc);
        final LinearLayout CocktailCardRoot = root.findViewById(R.id.VertCoctailCard);

        LinearLayout CocktailHeader = root.findViewById(R.id.LinCocktailList);

        CocktailHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int CurrentHeight = CocktailIng.getLayoutParams().height;
                if (CurrentHeight == 0) {
                    CocktailIng.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final ResizeAnimation resizeAnimation = new ResizeAnimation(
                            CocktailIng,
                            CocktailIng.getMeasuredHeight(),
                            0
                    );
                    resizeAnimation.setDuration(200);
                    CocktailIng.startAnimation(resizeAnimation);
                }
                else {
                    CocktailIng.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ResizeAnimation resizeAnimation = new ResizeAnimation(
                            CocktailIng,
                            0,
                            CocktailIng.getHeight()
                    );
                    resizeAnimation.setDuration(300);
                    CocktailIng.startAnimation(resizeAnimation);
                }

            }
        });

        //Get image
        String ImgPath = ThisItem.Title.replace(" ", "_").toLowerCase();
        Title.setText(ThisItem.Title);
        boolean bCalledFromHome = CalledFrom.getClass() == HomeFragment.class;
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
        return mDataset.size();
    }

}
