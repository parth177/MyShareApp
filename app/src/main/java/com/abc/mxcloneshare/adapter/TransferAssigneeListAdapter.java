package com.abc.mxcloneshare.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.abc.mxcloneshare.view.TextDrawable;
import com.abc.mxcloneshare.util.AppUtils;
import com.abc.mxcloneshare.util.NetworkDeviceLoader;
import com.abc.mxcloneshare.util.TextUtils;
import com.abc.mxcloneshare.util.TransferUtils;
import com.abc.mxcloneshare.R;
import com.abc.mxcloneshare.model.ShowingAssignee;
import com.abc.mxcloneshare.model.TransferGroup;
import com.abc.mxcloneshare.widget.EditableListAdapter;

import java.util.List;

public class TransferAssigneeListAdapter extends EditableListAdapter<ShowingAssignee, EditableListAdapter.EditableViewHolder>
{
    private TransferGroup mGroup;
    private TextDrawable.IShapeBuilder mIconBuilder;

    public TransferAssigneeListAdapter(Context context)
    {
        super(context);
        mIconBuilder = AppUtils.getDefaultIconBuilder(context);
    }

    @NonNull
    @Override
    public EditableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new EditableViewHolder(getInflater().inflate(
                isHorizontalOrientation() || isGridLayoutRequested()
                        ? R.layout.row_assignee_grid
                        : R.layout.row_assignee, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EditableViewHolder holder, int position)
    {
        ShowingAssignee assignee = getList().get(position);

        ImageView image = holder.getView().findViewById(R.id.image);
        TextView text1 = holder.getView().findViewById(R.id.text1);
        TextView text2 = holder.getView().findViewById(R.id.text2);

        text1.setText(assignee.device.nickname);
        text2.setText(TextUtils.getAdapterName(getContext(), assignee.connection));
        NetworkDeviceLoader.showPictureIntoView(assignee.device, image, mIconBuilder);
    }

    @Override
    public List<ShowingAssignee> onLoad()
    {
        return TransferUtils.loadAssigneeList(AppUtils.getDatabase(getContext()), mGroup.groupId);
    }

    public TransferAssigneeListAdapter setGroup(TransferGroup group)
    {
        mGroup = group;
        return this;
    }
}
