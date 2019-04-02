package com.aurelhubert.ahbottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;

/**
 * AHBottomNavigationItem
 * The item is display in the AHBottomNavigation layout
 */
public class AHBottomNavigationItem {

	private String title = "";
	private Drawable drawable;
	private Drawable selectedDrawable;
	private int color = Color.GRAY;

	private
	@StringRes
	int titleRes = 0;
	private
	@DrawableRes
	int drawableRes = 0;
	@DrawableRes
	int selectedDrawableRes = 0;
	private
	@ColorRes
	int colorRes = 0;

	/**
	 * Constructor
	 *
	 * @param title    Title
	 * @param resource Drawable resource
	 */
	public AHBottomNavigationItem(String title, @DrawableRes int resource, @DrawableRes int selectedDrawableRes) {
		this.title = title;
		this.drawableRes = resource;
		this.selectedDrawableRes = selectedDrawableRes == 0 ? resource : selectedDrawableRes;
	}

	/**
	 * @param title    Title
	 * @param resource Drawable resource
	 * @param color    Background color
	 */
	@Deprecated
	public AHBottomNavigationItem(String title, @DrawableRes int resource, @ColorRes int color, @DrawableRes int selectedDrawableRes) {
		this.title = title;
		this.drawableRes = resource;
		this.color = color;
		this.selectedDrawableRes = selectedDrawableRes == 0 ? resource : selectedDrawableRes;
	}

	/**
	 * Constructor
	 *
	 * @param titleRes    String resource
	 * @param drawableRes Drawable resource
	 * @param colorRes    Color resource
	 */
	public AHBottomNavigationItem(@StringRes int titleRes, @DrawableRes int drawableRes, @ColorRes int colorRes, @DrawableRes int selectedDrawableRes) {
		this.titleRes = titleRes;
		this.drawableRes = drawableRes;
		this.colorRes = colorRes;
		this.selectedDrawableRes = selectedDrawableRes == 0 ? drawableRes : selectedDrawableRes;
	}

	/**
	 * Constructor
	 *
	 * @param title    String
	 * @param drawable Drawable
	 */
	public AHBottomNavigationItem(String title, Drawable drawable, Drawable selectedDrawable) {
		this.title = title;
		this.drawable = drawable;
		this.selectedDrawable = selectedDrawable == null ? drawable : selectedDrawable;
	}

	/**
	 * Constructor
	 *
	 * @param title    String
	 * @param drawable Drawable
	 * @param color    Color
	 */
	public AHBottomNavigationItem(String title, Drawable drawable, @ColorInt int color, Drawable selectedDrawable) {
		this.title = title;
		this.drawable = drawable;
		this.color = color;
		this.selectedDrawable = selectedDrawable == null ? drawable : selectedDrawable;
	}

	public String getTitle(Context context) {
		if (titleRes != 0) {
			return context.getString(titleRes);
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.titleRes = 0;
	}

	public void setTitle(@StringRes int titleRes) {
		this.titleRes = titleRes;
		this.title = "";
	}

	public int getColor(Context context) {
		if (colorRes != 0) {
			return ContextCompat.getColor(context, colorRes);
		}
		return color;
	}

	public void setColor(@ColorInt int color) {
		this.color = color;
		this.colorRes = 0;
	}

	public void setColorRes(@ColorRes int colorRes) {
		this.colorRes = colorRes;
		this.color = 0;
	}
	
	public Drawable getDrawable(Context context) {
    if (drawableRes != 0) {
      try {
        return VectorDrawableCompat.create(context.getResources(), drawableRes, null);
      }catch (Resources.NotFoundException e){
        return ContextCompat.getDrawable(context, drawableRes);
      }
    }
    return drawable;
  }
	public Drawable getSelectedDrawable(Context context) {
		if (selectedDrawableRes != 0) {
			try {
				return VectorDrawableCompat.create(context.getResources(), selectedDrawableRes, null);
			}catch (Resources.NotFoundException e){
				return ContextCompat.getDrawable(context, selectedDrawableRes);
			}
		}
		return selectedDrawable;
	}

	public void setDrawable(@DrawableRes int drawableRes) {
		this.drawableRes = drawableRes;
		this.drawable = null;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
		this.drawableRes = 0;
	}
	public void setSelectedDrawable(@DrawableRes int selectedDrawableRes) {
		this.selectedDrawableRes = selectedDrawableRes;
		this.selectedDrawable = null;
	}

	public void setSelectedDrawable(Drawable selectedDrawable) {
		this.selectedDrawable = selectedDrawable;
		this.selectedDrawableRes = 0;
	}
}
