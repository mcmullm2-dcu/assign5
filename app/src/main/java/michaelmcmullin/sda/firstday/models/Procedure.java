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

package michaelmcmullin.sda.firstday.models;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Class for storing information about a single {@link Procedure} instance.
 */
public class Procedure {

  /**
   * Unique ID of this {@link Procedure} instance.
   */
  private String id;

  /**
   * The name of this {@link Procedure} instance.
   */
  private String name;

  /**
   * A description or summary of this {@link Procedure} instance.
   */
  private String description;

  /**
   * The {@link User} who owns this {@link Procedure} instance.
   */
  private User owner;

  /**
   * The timestamp of when this {@link Procedure} instance was first created.
   */
  private Date created;

  /**
   * Flag indicating if this {@link Procedure} is publicly accessible, or only available users who
   * have been explicitly given access.
   */
  private boolean is_public;

  /**
   * Flag indicating if this {@link Procedure} is still a draft, or only available to its owner.
   */
  private boolean is_draft;

  /**
   * List of search tags associated with this procedure.
   */
  private ArrayList<String> tags;

  /**
   * Firestore requires a constructor with no arguments.
   */
  public Procedure() {
  }

  /**
   * Creates a new instance of {@link Procedure} setting all the main fields.
   *
   * @param name The name of this {@link Procedure} instance.
   * @param description The description or summary of this {@link Procedure} instance.
   * @param owner The {@link User} who owns this {@link Procedure} instance.
   * @param created The timestamp indicating when this {@link Procedure} was first created.
   * @param is_public Indicates if this {@link Procedure} instance is publicly available.
   * @param is_draft Indicates if this {@link Procedure} instance is a draft copy.
   */
  public Procedure(String name, String description, User owner, Date created, boolean is_public,
      boolean is_draft) {
    this.name = name;
    this.description = description;
    this.owner = owner;
    this.created = created;
    this.is_public = is_public;
    this.is_draft = is_draft;
  }

  /**
   * Creates a new instance of {@link Procedure} setting the name and description.
   *
   * @param name The name of this {@link Procedure} instance.
   * @param description The description or summary of this {@link Procedure} instance.
   */
  public Procedure(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Gets the unique ID of this {@link Procedure} instance.
   *
   * @return Returns the unique ID of this {@link Procedure} instance.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the unique ID of this {@link Procedure} instance.
   *
   * @param id The unique ID to assign to this {@link Procedure} instance.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the name of this {@link Procedure} instance.
   *
   * @return Returns the name of this {@link Procedure} instance.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the description, or summary, of this {@link Procedure} instance.
   *
   * @return Returns the description of this {@link Procedure} instance.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the {@link User} who owns this {@link Procedure} instance.
   *
   * @return Returns the {@link User} who owns this {@link Procedure} instance.
   */
  public User getOwner() {
    return owner;
  }

  /**
   * Gets the timestamp indicating when this {@link Procedure} instance was first created.
   *
   * @return A timestamp indicating when this {@link Procedure} instance was created.
   */
  @ServerTimestamp
  public Date getCreated() {
    return created;
  }

  /**
   * Gets a value indicating if this {@link Procedure} instance is publicly available.
   *
   * @return Returns <code>true</code> if this {@link Procedure} instance is available to the
   *     public.
   */
  public boolean isPublic() {
    return is_public;
  }

  /**
   * Gets a value indicating if this {@link Procedure} instance is a draft.
   *
   * @return Returns <code>true</code> if this {@link Procedure} instance is a draft.
   */
  public boolean isDraft() {
    return is_draft;
  }

  /**
   * Indicates if this is a new procedure (i.e. it has no ID set).
   *
   * @return Returns <code>true</code> if this {@link Procedure} instance is a new procedure.
   */
  public boolean isNew() {
    return (this.id == null || this.id.isEmpty());
  }

  /**
   * Gets any search tags available for this procedure.
   * @return Returns a list of tags available for this procedure.
   */
  public ArrayList<String> getTags() {
    return tags;
  }

  /**
   * Sets the list of search tags for this procedure.
   * @param tags A list of search tags to assign to this procedure.
   */
  public void setTags(ArrayList<String> tags) {
    this.tags = tags;
  }

  /**
   * Sets the list of search tags for this procedure based on
   * @param tags A Set of tags to assign to this procedure. They'll be converted to an ArrayList.
   */
  public void setTags(Set<String> tags) {
    if (tags != null) {
      this.tags = new ArrayList<>(tags);
    }
  }
}
