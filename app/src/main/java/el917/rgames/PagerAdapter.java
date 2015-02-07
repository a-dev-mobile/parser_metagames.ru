package el917.rgames;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.material.widget.TabIndicator;

import el917.rgames.fragment.GameFragment;


public class PagerAdapter extends FragmentPagerAdapter implements TabIndicator.TabTextProvider {


    private int mCount;
    private String[] title;
    private String[] url;



    public PagerAdapter(FragmentManager fragmentManager, String[] title,String[]url) {
        super(fragmentManager);
        this.title = title;
        this.url = url;
        mCount = title.length;
    }

    @Override
    public Fragment getItem(int position) {
        return GameFragment.newInstance(url[position], position);

    }

    @Override
    public String getText(int position) {
        return title[position % title.length];
    }

    @Override
    public int getCount() {
        return mCount;
    }
}
