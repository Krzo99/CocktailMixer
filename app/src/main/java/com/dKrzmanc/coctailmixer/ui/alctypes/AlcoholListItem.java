package com.dKrzmanc.coctailmixer.ui.alctypes;

public class AlcoholListItem {
    public String Title;
    public String Description;
    //Because measured size is not correct!
    public int ExpandedSize;
    public int CurrentHeightOfItem = 0;

    public AlcoholListItem(String NameC, String DescriptionC, int ExpandedSizeC)
    {
        Title = NameC;
        Description = DescriptionC;
        ExpandedSize = ExpandedSizeC;
    }
}
