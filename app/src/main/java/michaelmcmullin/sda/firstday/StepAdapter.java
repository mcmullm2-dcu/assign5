package michaelmcmullin.sda.firstday;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.models.Step;

public class StepAdapter extends ArrayAdapter<Step> {
  /**
   * This is our own custom constructor (it doesn't mirror a superclass constructor). The context is
   * used to inflate the layout file, and the list is the data we want to populate into the lists.
   *
   * @param context The current context. Used to inflate the layout file.
   * @param steps A List of Step objects to display in a list
   */
  public StepAdapter(Activity context, ArrayList<Step> steps) {
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
          R.layout.step_item, parent, false);
    }

    // Get the Step object located at this position in the list
    Step currentStep = getItem(position);

    // Find the TextView in the step_item.xml layout with the ID step_sequence.
    TextView sequenceTextView = listItemView.findViewById(R.id.step_sequence);

    // Get the sequence from the current Step object and set this text on the sequence TextView.
    sequenceTextView.setText(Integer.toString(currentStep.getSequence()));

    // Find the TextView in the step_item.xml layout with the ID step_name
    TextView nameTextView = listItemView.findViewById(R.id.step_name);

    // Get the name from the current Step object and set this text on the name TextView.
    nameTextView.setText(currentStep.getName());

    // Find the TextView in the step_item.xml layout with the ID step_description
    TextView descriptionTextView = listItemView.findViewById(R.id.step_description);

    // Get the description from the current Step object and set this text on the description TextView.
    descriptionTextView.setText(currentStep.getDescription());

    // Return the whole step item layout so it can be shown in the ListView
    return listItemView;
  }
}
