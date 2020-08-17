package com.example.coctailmixer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coctailmixer.ui.gallery.GalleryFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsListAdapter extends  RecyclerView.Adapter<IngredientsListAdapter.ListViewHolderMix> {
    public ArrayList<String> mDataset;
    private GalleryFragment mGaleryFrag;
    public static class ListViewHolderMix extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ListViewHolderMix(View v) {
            super(v);
            mView = v;
        }
    }

    public IngredientsListAdapter(ArrayList<String> IngredientsList, GalleryFragment GalFrag) {
        mDataset = IngredientsList;
        mGaleryFrag = GalFrag;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public IngredientsListAdapter.ListViewHolderMix onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mix_your_own_entry_layout, parent, false);


        return new IngredientsListAdapter.ListViewHolderMix(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(IngredientsListAdapter.ListViewHolderMix holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String ThisItem = mDataset.get(position);
        View root = holder.mView;
        TextView text = root.findViewById(R.id.IngridentName);
        text.setText(ThisItem);

        ImageView DelButton = root.findViewById(R.id.DeleteIngredient);
        DelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataset.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataset.size());

                mGaleryFrag.CalcWYCMCocktails();
                mGaleryFrag.UpdateCache();
            }
        });
        /*final String ThisItem = mDataset.get(position);

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

        Context context = holder.mView.getContext();
        int resID = context.getResources().getIdentifier(ImgPath , "drawable", context.getPackageName());
        Image.setImageResource(resID);

        String Desc = ThisItem.GenerateDescription();
        CocktailDescText.setText(HtmlCompat.fromHtml(Desc, HtmlCompat.FROM_HTML_MODE_LEGACY));
*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
