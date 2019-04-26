package michaelmcmullin.sda.firstday;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureStorer;
import michaelmcmullin.sda.firstday.interfaces.StepsSetter;

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
   */
  SharedPreferences prefs;

  /**
   * An interface implemented by the parent activity to allow this adapter to store and retrieve
   * its details.
   */
  ProcedureStorer procedureStorer;

  /**
   * An interface implemented by the parent activity to allow steps to be passed back to it.
   */
  StepsSetter stepsSetter;

  /**
   * Creates an instance of the {@link ProcedureFormAdapter} class.
   * @param fm The FragmentManager that interacts with fragments associated with this adapter's
   * calling activity.
   * @param titles An array of the titles of each tab.
   * @param prefs This app's shared preferences
   */
  public ProcedureFormAdapter(FragmentManager fm, String[] titles, ProcedureStorer storer, StepsSetter stepsSetter, SharedPreferences prefs) {
    super(fm);
    this.numOfTabs = titles.length;
    tabTitles = titles;
    this.procedureStorer = storer;
    this.stepsSetter = stepsSetter;
    this.prefs = prefs;
  }

  /**
   * Get the fragment associated with the tab at a given position.
   * @param position The position of the tab to retrieve the associated fragment for.
   * @return The tab fragment at the given position, or null if none found.
   */
  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case TAB_MAIN:
        return ProcedureFormDetailsFragment.newInstance(procedureStorer);
      case TAB_STEPS:
        return ProcedureFormStepFragment.newInstance(procedureStorer);
      case TAB_TAGS:
        return ProcedureFormTagFragment.newInstance(procedureStorer);
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
