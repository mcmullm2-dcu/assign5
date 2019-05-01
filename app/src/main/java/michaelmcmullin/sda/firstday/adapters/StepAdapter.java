/*
 * Copyright (C) 2019 Michael McMullin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package michaelmcmullin.sda.firstday.adapters;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.dialogs.ShowPhotoDialogFragment;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * Class to describe how a single {@link Step} object is rendered using the <code>step_item</code>
 * layout.
 */
public class StepAdapter extends ArrayAdapter<Step> {
  private Activity context;
  private boolean localOnly;

  /**
   * This is our own custom constructor (it doesn't mirror a superclass constructor). The context is
   * used to inflate the layout file, and the list is the data we want to populate into the lists.
   *
   * @param context The current context. Used to inflate the layout file.
   * @param steps A List of Step objects to display in a list
   */
  public StepAdapter(Activity context, ArrayList<Step> steps, boolean local) {
    // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
    // The second argument is used when the ArrayAdapter is populating a single TextView.
    // Because this is a custom adapter for a more complex layout view, the adapter is not
    // going to use this second argument, so it can be any value. Here, we used 0.
    super(context, 0, steps);
    this.context = context;
    this.localOnly = local;
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
    final Step currentStep = getItem(position);

    // Find the TextView in the step_item.xml layout with the ID step_sequence and populate.
    TextView sequenceTextView = listItemView.findViewById(R.id.step_sequence);
    sequenceTextView.setText(Integer.toString(currentStep.getSequence()));

    // Find the TextView in the step_item.xml layout with the ID step_name and populate.
    TextView nameTextView = listItemView.findViewById(R.id.step_name);
    nameTextView.setText(currentStep.getName());

    // Find the TextView in the step_item.xml layout with the ID step_description and populate
    TextView descriptionTextView = listItemView.findViewById(R.id.step_description);
    descriptionTextView.setText(currentStep.getDescription());

    // Add a photo icon if necessary
    ImageView imageLauncher = listItemView.findViewById(R.id.image_launcher);
    if (currentStep.hasPhotoId()) {
      imageLauncher.setVisibility(View.VISIBLE);
      imageLauncher.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          FragmentActivity activity = (FragmentActivity)context;
          FragmentManager fm = activity.getSupportFragmentManager();
          ShowPhotoDialogFragment photoDialog = ShowPhotoDialogFragment.newInstance(currentStep, localOnly);
          photoDialog.show(fm, "dialog_show_photo");
        }
      });
    } else {
      imageLauncher.setVisibility(View.GONE);
    }

    // Return the whole step item layout so it can be shown in the ListView
    return listItemView;
  }
}
