package michaelmcmullin.sda.firstday.dataaccess;

import android.arch.lifecycle.ViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProcedureViewModel extends ViewModel {
  private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
  private static final CollectionReference procedureCollection = db.collection("procedure");
}
