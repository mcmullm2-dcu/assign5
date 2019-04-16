package michaelmcmullin.sda.firstday.dataaccess;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import michaelmcmullin.sda.firstday.AppConstants;

/**
 * Extends {@link LiveData} to operate on Firestore data.
 *
 * Based on a blog post by Doug Stevenson:
 * https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 *
 * Adjusted from Realtime Database to Firestore based on Brian Begun's answer on StackOverflow:
 * https://stackoverflow.com/a/50455288/5233918
 */
public class FirestoreLiveData extends LiveData<QuerySnapshot> {

  /**
   * A Firestore query you can read or listen to.
   */
  private Query query;

  /**
   * An event listener that responds to changes in the query field's data.
   */
  private final QueryEventListener listener = new QueryEventListener();

  /**
   * Represents a listener that can be removed using the remove() method.
   */
  private ListenerRegistration listenerRegistration;

  private boolean listenerRemovePending = false;
  private final Handler handler = new Handler();

  /**
   * Creates an instance of {@link FirestoreLiveData} and attaches a {@link Query} to it.
   * @param query The Firestore {@link Query} to attached to this instance.
   */
  public FirestoreLiveData(Query query) {
    this.query = query;
  }

  private final Runnable removeListener = new Runnable() {
    @Override
    public void run() {
      listenerRegistration.remove();
      listenerRemovePending = false;
    }
  };

  /**
   * Manages the {@link QueryEventListener} when the associated Activity or Fragment becomes active.
   */
  @Override
  protected void onActive() {
    super.onActive();

    Log.d(AppConstants.TAG, "onActive");
    if (listenerRemovePending) {
      handler.removeCallbacks(removeListener);
    }
    else {
      listenerRegistration = query.addSnapshotListener(listener);
    }
    listenerRemovePending = false;
  }

  /**
   * Manages the {@link QueryEventListener} when the associated Activity or Fragment is inactive.
   */
  @Override
  protected void onInactive() {
    super.onInactive();

    Log.d(AppConstants.TAG, "onInactive: ");
    // Listener removal is schedule on a two second delay
    handler.postDelayed(removeListener, 2000);
    listenerRemovePending = true;
  }

  /**
   * Class that listens for any query data changes, notifying any observers.
   */
  private class QueryEventListener implements EventListener<QuerySnapshot> {
    @Override
    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
      if (e != null){
        Log.e(AppConstants.TAG, "Can't listen to query snapshots: " + querySnapshot + ":::" + e.getMessage());
        return;
      }
      setValue(querySnapshot);
    }
  }
}