package com.example.stopwastetime;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.util.TypedValue;

import androidx.core.content.res.ResourcesCompat;
import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class Converters {
    @TypeConverter
    public static byte[] toByte(Drawable image) { //non è possibile storare sul database delle immagini e quindi devo convertirle, non è il sistema migliore ed efficiente ma per i miei requisiti è ottimo
        Bitmap bitmap = Bitmap.createBitmap(image.getIntrinsicWidth(),
                image.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        image.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        image.draw(canvas);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @TypeConverter
    public static Drawable toDrawable(byte[] byteImage) {
        Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length));
        return image;
    }
}
