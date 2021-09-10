package com.abc.mxcloneshare.view;

import com.abc.mxcloneshare.fragment.EditableListFragment;
import com.abc.mxcloneshare.widget.EditableListAdapter;

public interface EditableListFragmentModelImpl<V extends EditableListAdapter.EditableViewHolder>
{
    void setLayoutClickListener(EditableListFragment.LayoutClickListener<V> clickListener);
}
