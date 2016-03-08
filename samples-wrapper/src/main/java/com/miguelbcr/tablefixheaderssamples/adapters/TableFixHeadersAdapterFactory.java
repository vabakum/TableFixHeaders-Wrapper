package com.miguelbcr.tablefixheaderssamples.adapters;

import android.content.Context;

import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;
import com.miguelbcr.tablefixheaderssamples.adapters.basic.BasicTableFixHeader;
import com.miguelbcr.tablefixheaderssamples.adapters.original.OriginalTableFixHeader;
import com.miguelbcr.tablefixheaderssamples.adapters.original_sortable.OriginalSortableTableFixHeader;

/**
 * Created by miguel on 12/02/2016.
 */
public class TableFixHeadersAdapterFactory {
    public static final int ORIGINAL = 0, BASIC = 1, ORIGINAL_SORTABLE = 2;
    private Context context;

    public TableFixHeadersAdapterFactory(Context context) {
        this.context = context;
    }

    public BaseTableAdapter getAdapter(int type) {
        switch (type) {
            case ORIGINAL: return new OriginalTableFixHeader(context).getInstance();
            case BASIC: return new BasicTableFixHeader(context).getInstance();
            case ORIGINAL_SORTABLE: return new OriginalSortableTableFixHeader(context).getInstance();
            default: return new OriginalTableFixHeader(context).getInstance();
        }
    }
}
