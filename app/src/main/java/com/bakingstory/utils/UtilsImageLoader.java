package com.bakingstory.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bakingstory.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by emil.ivanov on 5/6/18.
 */
public class UtilsImageLoader {
    public static void loadNetworkImage(Context context, ImageView imageView, String imageUrl) {
        Picasso.with(context).load(imageUrl)
                .placeholder(R.color.colorPrimary)
                .error(R.drawable.placeholder_image)
                .fit()
                .noFade()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView);
    }

}
