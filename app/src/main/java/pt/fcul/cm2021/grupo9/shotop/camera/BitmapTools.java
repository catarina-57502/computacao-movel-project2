package pt.fcul.cm2021.grupo9.shotop.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class BitmapTools {

    public static Bitmap toBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data , 0, data.length);
    }

    public static byte[] toBytes(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap rotate(Bitmap in, int angle) {
        Matrix mat = new Matrix();
        mat.postRotate(angle);
        return Bitmap.createBitmap(in, 0, 0, in.getWidth(), in.getHeight(), mat, true);
    }
}