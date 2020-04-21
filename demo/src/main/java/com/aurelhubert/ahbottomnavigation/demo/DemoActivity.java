package com.aurelhubert.ahbottomnavigation.demo;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import java.util.ArrayList;

public class DemoActivity extends AppCompatActivity {

	private DemoFragment currentFragment;
	private DemoViewPagerAdapter adapter;
	private AHBottomNavigationAdapter navigationAdapter;
	private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
	private boolean useMenuResource = true;
	private int[] tabColors;
	private Handler handler = new Handler();

	// UI
	private AHBottomNavigationViewPager viewPager;
	private AHBottomNavigation bottomNavigation;
	private FloatingActionButton floatingActionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean enabledTranslucentNavigation = getSharedPreferences("shared", Context.MODE_PRIVATE).getBoolean("translucentNavigation", false);
		setTheme(enabledTranslucentNavigation ? R.style.AppTheme_TranslucentNavigation : R.style.AppTheme);
		setContentView(R.layout.activity_home);
		initUI();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * Init UI
	 */
	private void initUI() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
		}

		bottomNavigation = findViewById(R.id.bottom_navigation);
		viewPager = findViewById(R.id.view_pager);
		floatingActionButton = findViewById(R.id.floating_action_button);

		if (useMenuResource) {
			tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
			navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
			navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
		} else {
			AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_apps_black_24dp);
			AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_maps_local_bar);
			AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_maps_local_restaurant);

			bottomNavigationItems.add(item1);
			bottomNavigationItems.add(item2);
			bottomNavigationItems.add(item3);

			bottomNavigation.addItems(bottomNavigationItems);
		}

		bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);
		bottomNavigation.setTranslucentNavigationEnabled(true);

		bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {

            if (currentFragment == null) {
                currentFragment = adapter.getCurrentFragment();
            }

            if (wasSelected) {
                currentFragment.refresh();
                return true;
            }

            if (currentFragment != null) {
                currentFragment.willBeHidden();
            }

            viewPager.setCurrentItem(position, false);

            if (currentFragment == null) {
                return true;
            }

            currentFragment = adapter.getCurrentFragment();
            currentFragment.willBeDisplayed();

            if (position == 1) {
                bottomNavigation.setNotification("", 1);

                floatingActionButton.setVisibility(View.VISIBLE);
                floatingActionButton.setAlpha(0f);
                floatingActionButton.setScaleX(0f);
                floatingActionButton.setScaleY(0f);
                floatingActionButton.animate()
                        .alpha(1)
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator())
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                floatingActionButton.animate()
                                        .setInterpolator(new LinearOutSlowInInterpolator())
                                        .start();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .start();

            } else {
                if (floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.animate()
                            .alpha(0)
                            .scaleX(0)
                            .scaleY(0)
                            .setDuration(300)
                            .setInterpolator(new LinearOutSlowInInterpolator())
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    floatingActionButton.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    floatingActionButton.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .start();
                }
            }

            return true;
        });

		/*
		bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
			@Override public void onPositionChange(int y) {
				Log.d("DemoActivity", "BottomNavigation Position: " + y);
			}
		});
		*/

		viewPager.setOffscreenPageLimit(4);
		adapter = new DemoViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		currentFragment = adapter.getCurrentFragment();

		handler.postDelayed(() -> {
            // Setting custom colors for notification
            AHNotification notification = new AHNotification.Builder()
                    .setText(":)")
                    .setBackgroundColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_back))
                    .setTextColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_text))
                    .build();
            bottomNavigation.setNotification(notification, 1);
            Snackbar.make(bottomNavigation, "Snackbar with bottom navigation",
                    Snackbar.LENGTH_SHORT).show();

        }, 3000);

		//bottomNavigation.setDefaultBackgroundResource(R.drawable.bottom_navigation_background);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#d7ffd9"));
	}

	/**
	 * Update the bottom navigation colored param
	 */
	public void updateBottomNavigationColor(boolean isColored) {
		bottomNavigation.setColored(isColored);
	}

	/**
	 * Return if the bottom navigation is colored
	 */
	public boolean isBottomNavigationColored() {
		return bottomNavigation.isColored();
	}

	/**
	 * Add or remove items of the bottom navigation
	 */
	public void updateBottomNavigationItems(boolean addItems) {

		if (useMenuResource) {
			if (addItems) {
				navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_5);
				navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
				bottomNavigation.setNotification("1", 3);
			} else {
				navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
				navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
			}

		} else {
			if (addItems) {
				AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.tab_4),
						ContextCompat.getDrawable(this, R.drawable.ic_maps_local_bar),
						ContextCompat.getColor(this, R.color.color_tab_4));
				AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.tab_5),
						ContextCompat.getDrawable(this, R.drawable.ic_maps_place),
						ContextCompat.getColor(this, R.color.color_tab_5));

				bottomNavigation.addItem(item4);
				bottomNavigation.addItem(item5);
				bottomNavigation.setNotification("1", 3);
			} else {
				bottomNavigation.removeAllItems();
				bottomNavigation.addItems(bottomNavigationItems);
			}
		}
	}

	/**
	 * Show or hide the bottom navigation with animation
	 */
	public void showOrHideBottomNavigation(boolean show) {
		if (show) {
			bottomNavigation.restoreBottomNavigation(true);
		} else {
			bottomNavigation.hideBottomNavigation(true);
		}
	}

	/**
	 * Show or hide selected item background
	 */
	public void updateSelectedBackgroundVisibility(boolean isVisible) {
		bottomNavigation.setSelectedBackgroundVisible(isVisible);
	}

	/**
	 * Set title state for bottomNavigation
	 */
	public void setTitleState(AHBottomNavigation.TitleState titleState) {
		if (titleState == AHBottomNavigation.TitleState.ALWAYS_HIDE) bottomNavigation.setPreferLargeIcons(true);
		bottomNavigation.setTitleState(titleState);
	}

	/**
	 * Reload activity
	 */
	public void reload() {
		startActivity(new Intent(this, DemoActivity.class));
		finish();
	}

	/**
	 * Return the number of items in the bottom navigation
	 */
	public int getBottomNavigationNbItems() {
		return bottomNavigation.getItemsCount();
	}

    public void tab1CustomColors(View view) {
        bottomNavigation.setIconActiveColor(0, Color.GREEN);
        bottomNavigation.setTitleActiveColor(0, Color.GREEN);
        bottomNavigation.setIconInactiveColor(0, Color.BLUE);
        bottomNavigation.setTitleInactiveColor(0, Color.BLUE);
    }

    public void tab2DifferentColors(View view) {
        bottomNavigation.setIconActiveColor(1, Color.MAGENTA);
        bottomNavigation.setTitleActiveColor(1, Color.YELLOW);
        bottomNavigation.setIconInactiveColor(1, Color.parseColor("#80e27e"));
        bottomNavigation.setTitleInactiveColor(1, Color.parseColor("#4dd0e1"));
    }

    public void tab3SelectedIconColor(View view) {
        bottomNavigation.setIconActiveColor(2, Color.parseColor("#6a1b9a"));
//        bottomNavigation.setIconInactiveColor(2, Color.parseColor("#6a1b9a"));
//        bottomNavigation.setTitleActiveColor(2, Color.parseColor("#6a1b9a"));
//        bottomNavigation.setTitleInactiveColor(2, Color.parseColor("#6a1b9a"));
    }

    public void setTab3FontSize(View view) {
        bottomNavigation.setTitleActiveTextSizeInSp(2, (float) 19);
        bottomNavigation.setTitleInactiveTextSizeInSp(2, null);
    }

    private boolean tab1DotVisible;
    public void tab1Dot(View view) {
        if (tab1DotVisible) {
            tab1DotVisible = false;
            bottomNavigation.setNotificationSize(0, AHNotification.NOTIFICATION_SIZE_DEFAULT);
        } else {
            tab1DotVisible = true;
            bottomNavigation.setNotificationSize(0, dpToPx(8));
        }
    }

    private boolean tab2DotVisible;
    public void tab2Dot(View view) {
        tab2BadgeVisible = false;
        if (tab2DotVisible) {
            tab2DotVisible = false;
            bottomNavigation.setNotification("", 1);
        } else {
            tab2DotVisible = true;
            bottomNavigation.setNotification(new AHNotification.Builder().setSize(dpToPx(12)).build(), 1);
        }
    }

    private boolean tab2BadgeVisible;
    public void tab2Badge(View view) {
        tab2DotVisible = false;
        if (tab2BadgeVisible) {
            tab2BadgeVisible = false;
            bottomNavigation.setNotification(new AHNotification.Builder()
                    .setText("")
                    .build(), 1);
        } else {
            tab2BadgeVisible = true;
            bottomNavigation.setNotification(new AHNotification.Builder()
                    .setText("99+")
                    .setBackgroundColor(Color.GREEN)
                    .build(), 1);
        }
    }

    private int dpToPx(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
