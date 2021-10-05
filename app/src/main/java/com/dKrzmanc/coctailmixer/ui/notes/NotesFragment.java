package com.dKrzmanc.coctailmixer.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ui.recipes.CocktailListItem;
import com.dKrzmanc.coctailmixer.callbacks.NotesEditedCallback;
import com.dKrzmanc.coctailmixer.ui.settings.SettingsFragment;
import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NotesFragment extends DialogFragment {
    public NotesEditedCallback EditedNotesCallback;
    public NotesFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static NotesFragment newInstance(CocktailListItem ItemCalledFrom) {
        // Create new FrangmetClass
        NotesFragment frag = new NotesFragment();
        Bundle args = new Bundle();

        // We Save Notes and Cocktail title as parameters. We can't pass the pointer to the entire object!
        args.putString("title", ItemCalledFrom.Title);
        args.putString("notes", ItemCalledFrom.Notes);
        frag.setArguments(args);


        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notes_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //EditedNotesCallback = (NotesEditedCallback) getContext();

        final EditText InputField = view.findViewById(R.id.NotesMultilineInput);
        final Button CancelButton = view.findViewById(R.id.NotesCancelBtn);
        final Button ApplyButton = view.findViewById(R.id.NotesApplyBtn);

        //Parametes we got on newInstance()
        final String Title = getArguments().getString("title");
        String Notes = getArguments().getString("notes");

        //Firstly set text in this items input field
        InputField.setText(Notes);

        ApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String WrittenNotes = InputField.getText().toString();
                EditedNotesCallback.onNotesEdited(Title, WrittenNotes);
                dismiss();
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
