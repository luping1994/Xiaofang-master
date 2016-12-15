package com.suntrans.xiaofang.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.suntrans.xiaofang.BaseApplication;

import java.util.HashMap;
import java.util.Map;


public class UiUtils {

	private static Toast mToast;
	public static void showToast(Context context, String str) {
		if (mToast == null) {
			mToast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		}
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 获取到字符数组
	 * @param tabNames 字符数组的id
	 */
	public static String[] getStringArray(int tabNames) {
		return getResource1().getStringArray(tabNames);
	}

	public static Resources getResource1()
	{
		return getContext().getResources();
	}

	/**
	 * dip转换px
	 */
	public static int dip2px(int dip) {
		final float scale = getResource1().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * dip转换px
	 */
	public static int dip2px(int dip,Context context) {
		final float scale =context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * pxz转换dip
	 */

	public static int px2dip(int px) {
		final float scale = getResource1().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static Context getContext() {
		return BaseApplication.getApplication();
	}

	//	public static void runOnUiThread(Runnable runnable) {
//		// 在主线程运行
//		if(android.os.Process.myTid()==BaseApplication.getMainTid()){
//			runnable.run();
//		}else{
//			//获取handler
//			BaseApplication.getHandler().post(runnable);
//		}
//	}
	public static void runOnUiThread(Runnable runnable) {
		if (android.os.Process.myTid() == BaseApplication.getMainTid()) {
			runnable.run();
		} else {
			BaseApplication.getHandler().post(runnable);
		}
	}

	/**
	 * 加载view
	 *
	 * @param layoutId
	 * @return
	 */
	public static View inflate(int layoutId) {
		return View.inflate(getContext(), layoutId, null);
	}

	public static int getDimens(int homePictureHeight) {
		return (int) getResource1().getDimension(homePictureHeight);
	}

	/**
	 * 检查网络是否可用
	 */
	public static boolean isNetworkAvailable() {
		Context context = UiUtils.getContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
//					System.out.println(i + "===状态===" + networkInfo[i].getState());
//					System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int getDisPlayWidth() {
		WindowManager wm = (WindowManager) UiUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}
//
	/**
	 * 将十六进制的字符串转化为十进制的数值
	 */
	public static long HexToDec(String hexStr) {
		Map<String, Integer> hexMap = prepareDate(); // 先准备对应关系数据
		int length = hexStr.length();
		long result = 0L; // 保存最终的结果
		for (int i = 0; i < length; i++) {
			result += hexMap.get(hexStr.subSequence(i, i + 1)) * Math.pow(16, length - 1 - i);
		}
//        System.out.println("hexStr=" + hexStr + ",result=" + result);
		return result;
	}

	/**
	 * 准备十六进制字符对应关系。如("1",1)...("A",10),("B",11)
	 */
	private static HashMap<String, Integer> prepareDate() {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		for (int i = 0; i <= 9; i++) {
			hashMap.put(i + "", i);
		}
		hashMap.put("a", 10);
		hashMap.put("b", 11);
		hashMap.put("c", 12);
		hashMap.put("d", 13);
		hashMap.put("e", 14);
		hashMap.put("f", 15);
		return hashMap;
	}

	public static int getColor(Context context, int color) {
		TypedValue tv = new TypedValue();
		context.getTheme().resolveAttribute(color, tv, true);
		return tv.data;
	}

	public static final boolean isChineseCharacter(String chineseStr) {
		char[] charArray = chineseStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			// 是否是Unicode编码,除了"�"这个字符.这个字符要另外处理
			if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD')
					|| ((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @author paulburke
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
									   String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

}
