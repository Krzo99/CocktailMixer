package com.dKrzmanc.coctailmixer.ui.recipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.dKrzmanc.coctailmixer.MainActivity;
import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ResizeAnimation;
import com.dKrzmanc.coctailmixer.callbacks.NotesEditedCallback;
import com.dKrzmanc.coctailmixer.ui.favs.FavsFragment;
import com.dKrzmanc.coctailmixer.ui.make_own.MakeOwnFragment;
import com.dKrzmanc.coctailmixer.ui.notes.NotesFragment;
import com.dKrzmanc.coctailmixer.ui.settings.SettingsFragment;

import java.lang.reflect.Method;
import java.util.ArrayList;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class CocktailListAdapter extends RecyclerView.Adapter<CocktailListAdapter.ListViewHolder> implements NotesEditedCallback {
    //Dataset never changes, contains all cocktails available!
    private ArrayList<CocktailListItem> mDataset;
    private Fragment CalledFrom;
    TextView CocktailIng;
    LinearLayout CocktailDescriptionLinLayout;
    private ArrayList<CocktailListItem> mSortedCoctails;
    CocktailListAdapter mContext = null;

    // TODO So we can get The correct View for when we are updating height of entry: If notes change too much, we need to update it! Not in use
    //private View thisViewContext;


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
        mContext = this;
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

    @Override
    public void onNotesEdited(String title, String Notes){

        //For some reason it doesn't work with just one dataset. I must have fucked someting up, but it works, so it's fine for now!
        ArrayList<CocktailListItem> ListOfCocktailsForThisPage;
        if (CalledFrom.getClass() == RecipesFragment.class) {
            ListOfCocktailsForThisPage = mSortedCoctails;
        }
        else {
            ListOfCocktailsForThisPage = mDataset;
        }

        MainActivity main = ((MainActivity) CalledFrom.getActivity());

        //We find the cocktail we just edited.
        for (CocktailListItem i: ListOfCocktailsForThisPage)
        {
            if (i.Title.toLowerCase().equals(title.toLowerCase()))
            {
                // And save changed notes
                i.Notes = Notes;
                mContext.notifyDataSetChanged();

                //Only add new, if it doesn't already exists!
                if (main.NotesChangedCocktails.contains(i)) {
                    int index = main.NotesChangedCocktails.indexOf(i);
                    main.NotesChangedCocktails.get(index).Notes = Notes;
                }
                else {
                    main.NotesChangedCocktails.add(i);
                }
                //updateItemsHeight(CurrentlyEditedItem);
                main.saveNotes();
                return;
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (CalledFrom.getClass() == RecipesFragment.class) {
            CurrentlyEditedItem = mSortedCoctails.get(position);
        }
        else {
            CurrentlyEditedItem = mDataset.get(position);
        }

        final CocktailListItem ThisItem = CurrentlyEditedItem;
        final View root = holder.mView;

        TextView Title = root.findViewById(R.id.Cocktail_name);
        ImageView Image = root.findViewById(R.id.Cocktail_image);
        CocktailDescriptionLinLayout = root.findViewById(R.id.LinCocktailDesc);
        CocktailDescriptionLinLayout.getLayoutParams().height = ThisItem.CurrentHeightOfItem;

        TextView CocktailDescText = root.findViewById(R.id.CocktailDesc);
        final LinearLayout CocktailCardRoot = root.findViewById(R.id.VertCoctailCard);

        LinearLayout CocktailHeader = root.findViewById(R.id.LinCocktailList);


        // Set so it opens on click!
        CocktailHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CocktailDescriptionLinLayout = root.findViewById(R.id.LinCocktailDesc);
                CocktailIng = root.findViewById(R.id.CocktailDesc);
                int CurrentHeight = CocktailDescriptionLinLayout.getLayoutParams().height;
                String Notes = ThisItem.Notes;

                if (CurrentHeight == 0) {

                    CocktailIng.measure(0,0);             //Measure Height of ings
                    int measureNotes = getHeight(CocktailIng.getContext(), Notes,(int)CocktailIng.getTextSize() - 3, CocktailIng.getWidth(), CocktailIng.getTypeface(), 10);    //Measure height of Notes, for some reason cant use the same function!
                    int measureIngs = CocktailIng.getMeasuredHeight();                      //Save Height of ings
                    ThisItem.CurrentHeightOfItem = measureIngs + measureNotes ;

                    final ResizeAnimation BiggerheigthAnimation = new ResizeAnimation(
                            CocktailDescriptionLinLayout,
                            ThisItem.CurrentHeightOfItem,
                            0
                    );

                    BiggerheigthAnimation.setDuration(200);
                    CocktailDescriptionLinLayout.startAnimation(BiggerheigthAnimation);
                }
                else {
                    ResizeAnimation SmallerHeigthAnimation = new ResizeAnimation(
                            CocktailDescriptionLinLayout,
                            0,
                            ThisItem.CurrentHeightOfItem
                    );

                    SmallerHeigthAnimation.setDuration(200);
                    CocktailDescriptionLinLayout.startAnimation(SmallerHeigthAnimation);

                    ThisItem.CurrentHeightOfItem = 0;

                }

            }
        });

        // Save on Fav. icon click
        final ImageView favBtn = root.findViewById(R.id.CocktailFavIconButton);
        updateFavBtn(favBtn, ThisItem.bIsFavourited);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = ((MainActivity) CalledFrom.getActivity());
                boolean bIsSaved = ThisItem.bIsFavourited;

                //  Negative because it'll change!
                if (!bIsSaved) {
                    ThisItem.bIsFavourited = true;
                    boolean bSuccess = mainActivity.addCocktailToFav(ThisItem);
                    if (bSuccess){
                        updateFavBtn(favBtn, true);
                    }
                    else {
                        ThisItem.bIsFavourited = false;
                    }
                }
                else{
                    ThisItem.bIsFavourited = false;
                    boolean bSuccess = mainActivity.removeCocktailFromFav(ThisItem);
                    if (bSuccess){
                        updateFavBtn(favBtn, false);
                        if (CalledFrom.getClass() == FavsFragment.class) {
                            notifyDataSetChanged();
                        }
                    }
                    else {
                        ThisItem.bIsFavourited = true;
                    }
                }
            }
        });

        // Click on notes icon!
        final ImageView notesBtn = root.findViewById(R.id.CocktailNoteIconButton);
        notesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = ((MainActivity) CalledFrom.getActivity());
                FragmentManager fm = mainActivity.getSupportFragmentManager();
                NotesFragment NotesDialogFragment = NotesFragment.newInstance(ThisItem);
                NotesDialogFragment.EditedNotesCallback = (NotesEditedCallback) mContext;
                //thisViewContext = root;
                NotesDialogFragment.show(fm, "Notes_Dialog_Show");
            }
        });

        // Reset Note to default
        notesBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ThisItem.Notes = ThisItem.NotesOG;

                MainActivity main = ((MainActivity) CalledFrom.getActivity());
                int index = main.NotesChangedCocktails.indexOf(ThisItem);
                if (index == -1){
                    Toast toast = Toast.makeText(view.getContext(), "Nothing to reset!", Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }

                main.NotesChangedCocktails.remove(index);
                if (main.saveNotes()) {
                    Toast toast = Toast.makeText(view.getContext(), String.format("Note for %s has been reset to default!", ThisItem.Title), Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    Toast toast = Toast.makeText(view.getContext(), "Error saving notes!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                notifyDataSetChanged();
                return true;
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


    //Only for already opened items!
    /*public void updateItemsHeight(CocktailListItem item)
    {
        CocktailDescriptionLinLayout = thisViewContext.findViewById(R.id.LinCocktailDesc);
        CocktailIng = thisViewContext.findViewById(R.id.CocktailDesc);
        int startH = item.CurrentHeightOfItem;
        CocktailIng.measure(0,0);             //Measure Height of ings
        int measureNotes = getHeight(CocktailIng.getContext(), item.Notes,(int)CocktailIng.getTextSize() - 3, CocktailIng.getWidth(), CocktailIng.getTypeface(), 10);    //Measure height of Notes, for some reason cant use the same function!
        int measureIngs = CocktailIng.getMeasuredHeight();                      //Save Height of ings
        item.CurrentHeightOfItem = measureIngs + measureNotes;

        final ResizeAnimation BiggerheigthAnimation = new ResizeAnimation(
                CocktailDescriptionLinLayout,
                item.CurrentHeightOfItem,
                startH
        );

        BiggerheigthAnimation.setDuration(100);
        CocktailDescriptionLinLayout.startAnimation(BiggerheigthAnimation);

    }*/

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

    private void updateFavBtn(ImageView Imgbtn, boolean bIsSaved)
    {
        if (bIsSaved)
        {
            Imgbtn.setImageResource(R.drawable.ic_navbar_fav);
        }
        else
        {
            Imgbtn.setImageResource(R.drawable.ic_fav_border);
        }
    }




    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (CalledFrom.getClass() == RecipesFragment.class ) {
            return mSortedCoctails.size();
        }
        else{
            return mDataset.size();
        }

    }

}


