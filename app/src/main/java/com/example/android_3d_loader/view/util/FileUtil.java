package com.example.android_3d_loader.view.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class FileUtil {

    private static final String TAG = "FileUtil";

    public static String getFilePath(Context context, Uri uri){
        String path = null;

        if (DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getAbsoluteFilePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                path = getAbsoluteFilePath(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            path = getAbsoluteFilePath(context, uri, null);
        } else if("file".equalsIgnoreCase(uri.getScheme())){
            path = uri.getPath();
        }

        return path;
    }

    private static String getAbsoluteFilePath(Context context, Uri uri, String selection) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, null, null);//todo ?
        if (cursor != null) {
            if (cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                path = cursor.getString(columnIndex);

                int columnIndex = cursor.getColumnIndexOrThrow("_data");
                path = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return path;
    }
}
