package org.xeter.flyertracker.android.settings;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.xeter.flyertracker.android.R;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 * <p>
 * Voir ça : https://stackoverflow.com/questions/26564400/creating-a-preference-screen-with-support-v21-toolbar et
 * https://github.com/davcpas1234/MaterialSettings/blob/master/app/src/main/res/layout/toolbar_settings.xml
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        AppBarLayout bar;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            root.addView(bar, 0);
//        } else {
//            ViewGroup root = findViewById(android.R.id.content);
//            ListView content = (ListView) root.getChildAt(0);
//            root.removeAllViews();
//            bar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
//
//            int height;
//            TypedValue tv = new TypedValue();
//            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
//                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
//            } else {
//                height = bar.getHeight();
//            }
//
//            content.setPadding(0, height, 0, 0);
//
//            root.addView(content);
//            root.addView(bar);
//        }

        Toolbar toolbar = (Toolbar) bar.getChildAt(0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        setupSimplePreferencesScreen();
    }

    @SuppressWarnings("deprecation")
    private void setupSimplePreferencesScreen() {
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null) {
            return result;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // If we're running pre-L, we need to 'inject' our tint aware Views in place of the
            // standard framework versions
            switch (name) {
                case "EditText":
                    return new AppCompatEditText(this, attrs);
                case "Spinner":
                    return new AppCompatSpinner(this, attrs);
                case "CheckBox":
                    return new AppCompatCheckBox(this, attrs);
                case "RadioButton":
                    return new AppCompatRadioButton(this, attrs);
                case "CheckedTextView":
                    return new AppCompatCheckedTextView(this, attrs);
            }
        }

        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        super.onPreferenceTreeClick(preferenceScreen, preference);

        if (preference != null) {
            if (preference instanceof PreferenceScreen) {
                if (((PreferenceScreen) preference).getDialog() != null) {
                    ((PreferenceScreen) preference).getDialog().getWindow().getDecorView().setBackgroundDrawable(this.getWindow().getDecorView().getBackground().getConstantState().newDrawable());
                    setUpNestedScreen((PreferenceScreen) preference);
                }
            }
        }

        return false;
    }

    public void setUpNestedScreen(PreferenceScreen preferenceScreen) {
        final Dialog dialog = preferenceScreen.getDialog();

        AppBarLayout appBar;

        ViewGroup mRootView = dialog.findViewById(android.R.id.content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LinearLayout root = null;
            try {
                root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent().getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent().getParent().getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            appBar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            root.addView(appBar, 0);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LinearLayout root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent();
            appBar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            root.addView(appBar, 0);
        } else {
            ListView content = (ListView) mRootView.getChildAt(0);
            mRootView.removeAllViews();

            LinearLayout LL = new LinearLayout(this);
            LL.setOrientation(LinearLayout.VERTICAL);

            ViewGroup.LayoutParams LLParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            LL.setLayoutParams(LLParams);

            appBar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, mRootView, false);

            LL.addView(appBar);
            LL.addView(content);

            mRootView.addView(LL);
        }

        Toolbar toolbar = (Toolbar) appBar.getChildAt(0);

        toolbar.setTitle(preferenceScreen.getTitle());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
