package com.abc.mxcloneshare.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abc.mxcloneshare.activity.ConnectionManagerActivity;
import com.abc.mxcloneshare.activity.ContentSharingActivity;
import com.abc.mxcloneshare.base.GlideApp;
import com.abc.mxcloneshare.model.TitleSupport;
import com.abc.mxcloneshare.R;
import com.abc.mxcloneshare.util.AppUtils;
import com.genonbeta.android.framework.ui.callback.SnackbarSupport;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HomeFragment
        extends com.genonbeta.android.framework.app.Fragment
        implements TitleSupport, SnackbarSupport, com.genonbeta.android.framework.app.FragmentImpl
{


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button actionReceive = (Button)view.findViewById(R.id.receive);

        actionReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ConnectionManagerActivity.class)
                        .putExtra(ConnectionManagerActivity.EXTRA_ACTIVITY_SUBTITLE, getString(R.string.text_receive))
                        .putExtra(ConnectionManagerActivity.EXTRA_REQUEST_TYPE, ConnectionManagerActivity.RequestType.MAKE_ACQUAINTANCE.toString()));
            }
        });

        Button actionSend = (Button)view.findViewById(R.id.send);
        actionSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ContentSharingActivity.class));
            }
        });



        return view;
    }

    public void loadProfilePictureInto(String deviceName, ImageView imageView)
    {
        try {
            FileInputStream inputStream = getActivity().openFileInput("profilePicture");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            GlideApp.with(this)
                    .load(bitmap)
                    .circleCrop()
                    .into(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            imageView.setImageDrawable(AppUtils.getDefaultIconBuilder(getActivity()).buildRound(deviceName));
        }
    }

    public void startProfileEditor()
    {
      //  new ProfileEditorDialog(getActivity()).show();
    }



    @Override
    public CharSequence getTitle(Context context)
    {
        return context.getString(R.string.text_home);
    }


}
