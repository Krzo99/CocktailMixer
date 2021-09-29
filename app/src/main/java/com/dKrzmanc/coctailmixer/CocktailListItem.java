package com.dKrzmanc.coctailmixer;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import com.dKrzmanc.coctailmixer.MainActivity;

public class CocktailListItem {
    public String Title;
    public ArrayList<Ingredients> Ings;
    private ArrayList<Character> Vowels = new ArrayList<Character>(Arrays.asList('A','E','I','O','U'));
    public String MissingIng;
    public String Notes;
    public int CurrentHeightOfItem = 0;

    public CocktailListItem(String TitleC, String IngsStr, String NotesC, String MissingIngC) //Format IngsStr -> Kok:Kaj;Kok:Kaj
    {
        Title = TitleC;
        Ings = new ArrayList<Ingredients>();
        MissingIng = MissingIngC;
        Notes = NotesC;
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

    String GenerateDescription(boolean bCalledFromHome)
    {
            String end = String.format("To craft %s <b><b>%s</b></b>, mix:<br />", Vowels.contains(Title.charAt(0)) ? "an" : "a" ,Title);
        for (Ingredients i : Ings) {
            boolean bIsAlcohol = (Arrays.asList(MainActivity.AlcoholListStrings).contains(i.Name));
            String Name = String.format(bIsAlcohol ? "<span style='color:#ffc4c4'>%s</span>" : "%s", i.Name);
            end += String.format(Locale.getDefault(),"<span style='color:green'>%d</span> %s <i>%s</i>&nbsp;&nbsp;&nbsp;&nbsp;%s<br />", i.Amount, (i.Amount == 1 ? "part" :  "parts" ), Name, !MissingIng.equals("") && i.Name.equals(MissingIng) && !bCalledFromHome  ? "<span style='color:red'><i>!!!</i></span>" : "");
        }
        if (!Notes.equals("")) {
            end += String.format("<br /><small><i>Notes: %s</i></small>", Notes);
        }
        return end;
    }
}