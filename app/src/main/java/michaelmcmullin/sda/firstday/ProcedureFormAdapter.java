package michaelmcmullin.sda.firstday;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ProcedureFormAdapter extends FragmentStatePagerAdapter {

  /**
   * Index of the main procedure details tab.
   */
  public static final int TAB_MAIN = 0;

  /**
   * Index of the 'procedure steps' tab.
   */
  public static final int TAB_STEPS = TAB_MAIN + 1;

  /**
   * Index of the 'procedure tags' tab.
   */
  public static final int TAB_TAGS = TAB_STEPS + 1;

  /**
   * The number of tabs in this adapter.
   */
  int numOfTabs;

  /**
   * An array of titles for each tab.
   */
  String[] tabTitles;

  /**
   * An instance of this app's shared preferences.
   * TODO: may not be necessary for this adapter, consider removing and updating the constructor code.
   */
  SharedPreferences prefs;

  /**
   * Creates an instance of the {@link ProcedureFormAdapter} class.
   * @param fm The FragmentManager that interacts with fragments associated with this adapter's
   * calling activity.
   * @param titles An array of the titles of each tab.
   * @param prefs This app's shared preferences
   * TODO: consider removing prefs, unless there's a good reason to use it.
   */
  public ProcedureFormAdapter(FragmentManager fm, String[] titles, SharedPreferences prefs) {
    super(fm);
    this.numOfTabs = titles.length;
    tabTitles = titles;
    this.prefs = prefs;
  }

  /**
   * Get the fragment associated with the tab at a given position.
   * @param position The position of the tab to retrieve the associated fragment for.
   * @return The tab fragment at the given position, or null if none found.
   */
  @Override
  public Fragment getItem(int position) {
    // TODO: Return correct fragments
    switch (position) {
      case TAB_MAIN:
        return new Fragment();
      case TAB_STEPS:
        return new Fragment();
      case TAB_TAGS:
        return new Fragment();
      default:
        return null;
    }
  }

  /**
   * Gets the number of views available (in this case, the number of tabs).
   * @return The number of views available.
   */
  @Override
  public int getCount() {
    return numOfTabs;
  }

  /**
   * Gets the title of the tab at the given position.
   * @param position The position of the tab to retrieve the title for.
   * @return The title of the tab at the given position, or null.
   */
  public CharSequence getPageTitle(int position) {
    if (tabTitles != null && position <= tabTitles.length) {
      return tabTitles[position];
    } else {
      return null;
    }
  }
}
