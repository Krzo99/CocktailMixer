package com.dKrzmanc.coctailmixer.ui.recipes;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import com.dKrzmanc.coctailmixer.Ingredients;
import com.dKrzmanc.coctailmixer.MainActivity;

public class CocktailListItem {

    public String Title;
    public ArrayList<Ingredients> Ings;
    private ArrayList<Character> Vowels = new ArrayList<Character>(Arrays.asList('A','E','I','O','U'));
    public String MissingIng;
    //Both the original note and user-changed one. If notes werent changed, Notes == NotesOG. Always display Notes!
    public String NotesOG;
    public String Notes;
    public boolean bIsFavourited;
    public int CurrentHeightOfItem = 0;


    public CocktailListItem(String TitleC, String IngsStr, String NotesC, String MissingIngC) //Format IngsStr -> Kok:Kaj;Kok:Kaj
    {
        Title = TitleC;
        Ings = new ArrayList<Ingredients>();
        MissingIng = MissingIngC;
        NotesOG = NotesC;
        Notes = NotesOG;
        String[] IngsSplit = IngsStr.split(";");
        for (String Ing : IngsSplit) {
            String[] EndSplit = Ing.split(":");

            if (EndSplit.length == 2) {
                String Name = EndSplit[1];
                int Amount = Integer.parseInt(EndSplit[0]);
                Ings.add(new Ingredients(Name, Amount));
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (this.Title.equals(((CocktailListItem)obj).Title) && obj.getClass() == getClass()) {
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // If Ingredients contain this word/phrase
    public Boolean doIngsContain(String s)
    {
        for(Ingredients item: Ings){
            String lowercaseName = item.Name.toLowerCase();
            if (lowercaseName.contains(s))
            {
                return true;
            }
        }

        return false;
    }

    String GenerateDescription(boolean bCalledFromHome)
    {
            String end = String.format("To craft %s <b><b>%s</b></b>, mix:<br />", Vowels.contains(Title.charAt(0)) ? "an" : "a" ,Title);
        for (Ingredients i : Ings) {
            boolean bIsAlcohol = (MainActivity.AlcoholListStrings.contains(i.Name.toLowerCase()));
            String Name = String.format(bIsAlcohol ? "<span style='color:#ffc4c4'>%s</span>" : "%s", i.Name);
            end += String.format(Locale.getDefault(),"<span style='color:green'>%d</span> %s <i>%s</i>&nbsp;&nbsp;&nbsp;&nbsp;%s<br />", i.Amount, (i.Amount == 1 ? "part" :  "parts" ), Name, !MissingIng.equals("") && i.Name.equals(MissingIng) && !bCalledFromHome  ? "<span style='color:red'><i>!!!</i></span>" : "");
        }
        if (!Notes.equals("")) {
            end += String.format("<br /><small><i>Notes: %s</i></small>", Notes);
        }
        return end;
    }
}