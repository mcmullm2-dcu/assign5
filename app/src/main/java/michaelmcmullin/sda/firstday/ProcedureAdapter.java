package michaelmcmullin.sda.firstday;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.models.Procedure;

/**
 * Class to describe how a single {@link Procedure} object is rendered using the
 * <code>procedure_item</code> layout.
 */
public class ProcedureAdapter extends ArrayAdapter<Procedure> {
  /**
   * This is our own custom constructor (it doesn't mirror a superclass constructor). The context is
   * used to inflate the layout file, and the list is the data we want to populate into the lists.
   *
   * @param context The current context. Used to inflate the layout file.
   * @param steps A List of Step objects to display in a list
   */
  public ProcedureAdapter(Activity context, ArrayList<Procedure> steps) {
    // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
    // The second argument is used when the ArrayAdapter is populating a single TextView.
    // Because this is a custom adapter for a more complex layout view, the adapter is not
    // going to use this second argument, so it can be any value. Here, we used 0.
    super(context, 0, steps);
  }

  /**
   * Provides a view for an AdapterView (ListView, GridView, etc.)
   *
   * @param position The position in the list of data that should be displayed in the list item
   *     view.
   * @param convertView The recycled view to populate.
   * @param parent The parent ViewGroup that is used for inflation.
   * @return The View for the position in the AdapterView.
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Check if the existing view is being reused, otherwise inflate the view
    View listItemView = convertView;
    if (listItemView == null) {
      listItemView = LayoutInflater.from(getContext()).inflate(
          R.layout.procedure_item, parent, false);
    }

    // Get the Procedure object located at this position in the list
    Procedure currentItem = getItem(position);

    // Find the procedure name in the layout file and populate it.
    TextView nameTextView = listItemView.findViewById(R.id.procedure_name_text_view);
    nameTextView.setText(currentItem.getName());

    // Find the procedure description in the layout file and populate it.
    TextView descriptionTextView = listItemView.findViewById(R.id.procedure_description_text_view);
    descriptionTextView.setText(currentItem.getDescription());

    // Return the whole step item layout so it can be shown in the ListView
    return listItemView;
  }
}
