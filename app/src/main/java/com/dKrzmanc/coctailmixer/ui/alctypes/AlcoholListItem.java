package com.dKrzmanc.coctailmixer.ui.alctypes;

public class AlcoholListItem {
    public String Title;
    public String Description;

    public int CurrentHeightOfItem = 0;

    public AlcoholListItem(String NameC, String DescriptionC)
    {
        Title = NameC;
        Description = DescriptionC;
    }
}
