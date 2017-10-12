package com.android.deak.videoplayer.manager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.deak.videoplayer.entity.VideoEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 扫描本地视频
 */
public class VideoManager {
    private Context mContext;

    public VideoManager(Context mContext) {
        this.mContext = mContext;
    }

    public List<VideoEntity> searchVideo() {
        List<VideoEntity> list = null;
        if (mContext != null) {
            Cursor cursor = mContext.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null
            );
            if (cursor != null) {
                list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    Bitmap bitmap = getAlbumPicPath(path);
                    VideoEntity entity = new VideoEntity(id, title, album, artist, displayName, mimeType, path, size, duration, bitmap);
                    list.add(entity);
                }
                cursor.close();
            }
        }
        return list;
    }

//    public List<VideoEntity> searchFileVideo(File file) {
//        List<VideoEntity> list = null;
//        //判断是否是文件夹
//        if (file.isDirectory()) {
//            File[] files = file.listFiles();// 以该文件夹的子文件或文件夹生成一个数组
//            if (files == null) return null;
//            for (File file1 : files) {
//                if (file1 != null) {
//                    searchFileVideo(file1);//得到绝对路径
//                }
//            }
//        } else {
//            if (file.length() > 0 && file.getName().lastIndexOf(".") != -1) {
//                String name = file.getName();
//                if (name.contains(".mp4") || name.contains(".3gp") || name.contains(".wmv")
//                        || name.contains(".ts") || name.contains(".rmvb") || name.contains(".mov")
//                        || name.contains(".m4v") || name.contains(".avi") || name.contains(".m3u8")
//                        || name.contains(".3gpp") || name.contains(".3gpp2") || name.contains(".mkv")
//                        || name.contains(".flv") || name.contains(".divx") || name.contains(".f4v")
//                        || name.contains(".rm") || name.contains(".asf") || name.contains(".ram")
//                        || name.contains(".mpg") || name.contains(".v8") || name.contains(".swf")
//                        || name.contains(".m2v") || name.contains(".asx") || name.contains(".ra")
//                        || name.contains(".ndivx") || name.contains(".xvid")) {
//
//                    if (mContext != null) {
//                        Cursor cursor = mContext.getContentResolver().query(
//                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.DEFAULT_SORT_ORDER
//                        );
//                        if (cursor != null) {
//                            list = new ArrayList<>();
//                            while (cursor.moveToNext()) {
//                                int id = cursor.getInt(cursor
//                                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
//                                String title = cursor
//                                        .getString(cursor
//                                                .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
//                                String album = cursor
//                                        .getString(cursor
//                                                .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
//                                String artist = cursor
//                                        .getString(cursor
//                                                .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
//                                String displayName = cursor
//                                        .getString(cursor
//                                                .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
//                                String mimeType = cursor
//                                        .getString(cursor
//                                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
//                                String path = cursor
//                                        .getString(cursor
//                                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//                                long duration = cursor
//                                        .getInt(cursor
//                                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
//                                long size = cursor
//                                        .getLong(cursor
//                                                .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
//                                Bitmap bitmap = getAlbumPicPath(path);
//                                VideoEntity entity = new VideoEntity(id, title, album, artist, displayName, mimeType, path, size, duration, bitmap);
//                                list.add(entity);
//                            }
//                            cursor.close();
//                        }
//                    }
//                }
//            }
//        }
//        return list;
//    }

    public Bitmap getAlbumPicPath(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();//获得音乐专辑的封面，也可以获取电影的封面

        try {
            retriever.setDataSource(filePath);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int timeS = Integer.valueOf(time) / 10;
            bitmap = retriever.getFrameAtTime(timeS * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
