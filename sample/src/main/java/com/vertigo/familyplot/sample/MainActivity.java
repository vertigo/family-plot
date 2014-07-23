package com.vertigo.familyplot.sample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vertigo.familyplot.library.ArcGraph;
import com.vertigo.familyplot.library.Entry;
import com.vertigo.familyplot.library.HorizontalBarGraph;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        HorizontalBarGraph hbg1;
        HorizontalBarGraph hbg2;
        ArcGraph arc1;
        ArcGraph arc2;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            hbg1 = (HorizontalBarGraph)rootView.findViewById(R.id.hbg1);
            hbg2 = (HorizontalBarGraph)rootView.findViewById(R.id.hbg2);
            arc1 = (ArcGraph)rootView.findViewById(R.id.arc1);
            arc2 = (ArcGraph)rootView.findViewById(R.id.arc2);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            loadData(hbg1);
            loadData(hbg2);

            arc1.setPercentage(.27f);
            arc2.setPercentage(.42f);
        }

        private void loadData(HorizontalBarGraph graph) {
            List<Entry> list = new ArrayList<Entry>();
            list.add(new Entry("P1", 3.4f));
            list.add(new Entry("P2", 5.3f));
            list.add(new Entry("P3", 2.8f));
            list.add(new Entry("P4", 7.0f));

            graph.update(list);
        }
    }
}
