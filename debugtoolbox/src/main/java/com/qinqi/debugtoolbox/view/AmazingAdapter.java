package com.qinqi.debugtoolbox.view;

import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import java.util.List;


public abstract class AmazingAdapter<T> extends BaseAdapter implements SectionIndexer, OnScrollListener {
    public static final String TAG = AmazingAdapter.class.getSimpleName();

    /**
     * Pinned header state: don't show the header.
     */
    public static final int PINNED_HEADER_GONE = 0;

    /**
     * Pinned header state: show the header at the top of the list.
     */
    public static final int PINNED_HEADER_VISIBLE = 1;

    /**
     * Pinned header state: show the header. If the header extends beyond the bottom of the first shown element, push it
     * up and clip.
     */
    public static final int PINNED_HEADER_PUSHED_UP = 2;

    public abstract List<Pair<Long, List<T>>> getData();

    /**
     * Computes the desired state of the pinned header for the given position of the first visible list item. Allowed
     * return values are {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE} or {@link #PINNED_HEADER_PUSHED_UP}
     * .
     */
    public int getPinnedHeaderState(int position) {
        if (position < 0 || getCount() == 0) {
            return PINNED_HEADER_GONE;
        }

        // The header should get pushed up if the top item shown
        // is the last item in a section for a particular letter.
        int section = getSectionForPosition(position);
        int nextSectionPosition = getPositionForSection(section + 1);
        if (nextSectionPosition != -1 && position == nextSectionPosition - 1) {
            return PINNED_HEADER_PUSHED_UP;
        }

        return PINNED_HEADER_VISIBLE;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof AmazingListView) {
            ((AmazingListView) view).configureHeaderView(firstVisibleItem);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // nop
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View res = getAmazingView(position, convertView, parent);

        final int section = getSectionForPosition(position);
        boolean displaySectionHeaders = getPositionForSection(section) == position;

        bindSectionHeader(res, position, displaySectionHeaders);

        return res;
	}

//	public boolean isExist(String city) {
//		if (BQUtils.isEmpty(getData()) || BQUtils.isEmpty(city)) {
//			return false;
//		}
//		for (Pair<String, List<T>> pair : getData()) {
//			if (BQUtils.isEmpty(pair.second)) {
//				continue;
//			}
//			for (T obj : pair.second) {
//				if (obj instanceof SimpleCity) {
//					SimpleCity sc = (SimpleCity) obj;
//					if (city.equals(sc.cityName)) {
//						return true;
//					}
//				} else {
//					return false;
//				}
//			}
//		}
//		return false;
//	}

    /**
     * Configure the view (a listview item) to display headers or not based on displaySectionHeader (e.g. if
     * displaySectionHeader header.setVisibility(VISIBLE) else header.setVisibility(GONE)).
     */
    protected abstract void bindSectionHeader(View view, int position, boolean displaySectionHeader);

    /**
     * read: get view too
     */
    public abstract View getAmazingView(int position, View convertView, ViewGroup parent);

    /**
     * Configures the pinned header view to match the first visible list item.
     * 
     * @param header pinned header view.
     * @param position position of the first visible list item.
     * @param alpha fading of the header view, between 0 and 255.
     */
    public abstract void configurePinnedHeader(View header, int position, int alpha);

    @Override
    public abstract int getPositionForSection(int section);

    @Override
    public abstract int getSectionForPosition(int position);

    @Override
    public abstract Object[] getSections();
}
