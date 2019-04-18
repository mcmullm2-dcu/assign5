package michaelmcmullin.sda.firstday;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.models.Comment;
import michaelmcmullin.sda.firstday.models.User;

public class CommentAdapter extends ArrayAdapter<Comment> {

  /**
   * A Glide RequestManager to start and manage requests for Glide, to help bring user profile
   * images into an Adapter.
   */
  private final RequestManager glide;

  /**
   * This is our own custom constructor (it doesn't mirror a superclass constructor). The context is
   * used to inflate the layout file, and the list is the data we want to populate into the lists.
   *
   * @param context The current context. Used to inflate the layout file.
   * @param comments A List of Comment objects to display in a list
   */
  public CommentAdapter(Activity context, ArrayList<Comment> comments, RequestManager glide) {
    // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
    // The second argument is used when the ArrayAdapter is populating a single TextView.
    // Because this is a custom adapter for a more complex layout view, the adapter is not
    // going to use this second argument, so it can be any value. Here, we used 0.
    super(context, 0, comments);
    this.glide = glide;
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
          R.layout.comment_item, parent, false);
    }

    // Get the Step object located at this position in the list
    Comment currentItem = getItem(position);
    User author = currentItem.getAuthor();

    // Find and populate the user profile picture
    CircleImageView profileImageView = listItemView.findViewById(R.id.profile_photo_comment_author);
    if (author == null || author.getPhoto() == null) {
      profileImageView.setImageResource(R.drawable.ic_default_profile_picture);
    } else {
      glide
          .load(author.getPhoto())
          .placeholder(R.drawable.ic_default_profile_picture)
          .into(profileImageView);
    }

    // Find and populate the author's name
    TextView authorTextView = listItemView.findViewById(R.id.text_view_comment_author);
    authorTextView.setText(author.getName());

    // Find and populate the comment message
    TextView messageTextView = listItemView.findViewById(R.id.text_view_comment_messsage);
    messageTextView.setText(currentItem.getMessage());

    // Return the whole step item layout so it can be shown in the ListView
    return listItemView;
  }
}

