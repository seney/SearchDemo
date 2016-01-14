package com.hsns.searchdemo;

import android.app.SearchManager;
import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    private final String LOG_TAG = "MyLog";
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setOnSuggestionListener(this);
            mSearchView.setSuggestionsAdapter(new SearchSuggestionsAdapter(this));
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(LOG_TAG, query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    /**
     * Called when a suggestion was selected by navigating to it.
     *
     * @param position the absolute position in the list of suggestions.
     * @return true if the listener handles the event and wants to override the default
     * behavior of possibly rewriting the query based on the selected item, false otherwise.
     */
    @Override
    public boolean onSuggestionSelect(int position) {
        return true;
    }

    /**
     * Called when a suggestion was clicked.
     *
     * @param position the absolute position of the clicked item in the list of suggestions.
     * @return true if the listener handles the event and wants to override the default
     * behavior of launching any intent or submitting a search query specified on that item.
     * Return false otherwise.
     */
    @Override
    public boolean onSuggestionClick(int position) {

        return true;
    }

    public static class SearchSuggestionsAdapter extends SimpleCursorAdapter
    {
        private static final String[] mFields  = { "_id", "result" };
        private static final String[] mVisible = { "result" };
        private static final int[]    mViewIds = { android.R.id.text1 };


        public SearchSuggestionsAdapter(Context context)
        {
            super(context, android.R.layout.simple_list_item_1, null, mVisible, mViewIds, 0);
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint)
        {
            return new SuggestionsCursor(constraint);
        }

        private static class SuggestionsCursor extends AbstractCursor
        {
            private ArrayList<String> mResults;

            public SuggestionsCursor(CharSequence constraint)
            {
                final int count = 100;
                mResults = new ArrayList<String>(count);
                /*mResults.add("Apple");
                mResults.add("Banana");
                mResults.add("Dido");
                mResults.add("Category");
                mResults.add("Eat");
                mResults.add("Soda");
                mResults.add("Sit");*/
                for(int i = 0; i < count; i++){
                    mResults.add("Result " + (i + 1));
                }
                if(!TextUtils.isEmpty(constraint)){
                    String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
                    Iterator<String> iter = mResults.iterator();
                    while(iter.hasNext()){
                        if(!iter.next().toLowerCase(Locale.ROOT).startsWith(constraintString))
                        {
                            iter.remove();
                        }
                    }
                }
            }

            @Override
            public int getCount()
            {
                return mResults.size();
            }

            @Override
            public String[] getColumnNames()
            {
                return mFields;
            }

            @Override
            public long getLong(int column)
            {
                if(column == 0){
                    return mPos;
                }
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public String getString(int column)
            {
                if(column == 1){
                    return mResults.get(mPos);
                }
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public short getShort(int column)
            {
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public int getInt(int column)
            {
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public float getFloat(int column)
            {
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public double getDouble(int column)
            {
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public boolean isNull(int column)
            {
                return false;
            }
        }
    }
}
