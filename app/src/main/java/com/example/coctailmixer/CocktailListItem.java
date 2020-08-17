package com.example.coctailmixer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CocktailListItem {
    public String Title;
    public ArrayList<Ingredients> Ings;
    public String[] AlcoholList = new String[]{"Vodka", "Triple-sec", "White rum", "Orange curacao", "Dark rum", "Rum", "Tequila", "Gin", "Malibu"};
    public String MissingIng;

    public CocktailListItem(String TitleC, String IngsStr, String MissingIngC) //Format IngsStr -> Kok:Kaj;Kok:Kaj
    {
        Title = TitleC;
        Ings = new ArrayList<Ingredients>();
        MissingIng = MissingIngC;
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
        String end = String.format("To craft a <b><b>%s</b></b>, mix:<br />", Title);
        for (Ingredients i : Ings) {
            boolean bIsAlcohol = (Arrays.asList(AlcoholList).contains(i.Name));
            String Name = String.format(bIsAlcohol ? "<span style='color:#ffc4c4'>%s</span>" : "%s", i.Name);
            end += String.format("<span style='color:green'>%d</span> %s <i>%s</i>&nbsp;&nbsp;&nbsp;&nbsp;%s<br />", i.Amount, (i.Amount == 1 ? "part" :  "parts" ), Name, !MissingIng.equals("") && i.Name.equals(MissingIng) && !bCalledFromHome  ? "<span style='color:red'><i>!!!</i></span>" : "");
        }
        return end;
    }
}