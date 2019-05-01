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

import java.util.Date;

/**
 * Class to store information about a {@link Comment} made about a {@link Procedure}.
 */
public class Comment {

  /**
   * Unique ID of this {@link Comment} instance.
   */
  private String id;

  /**
   * The {@link User} who created this {@link Comment} instance.
   */
  private User author;

  /**
   * The timestamp when this {@link Comment} instance was created.
   *
   * The technique for handling Firebase Timestamps is based on code found on StackOverflow:
   * https://stackoverflow.com/a/33111791/5233918
   * Author: Lyla (https://stackoverflow.com/users/497605/lyla)
   */
  private Date created;

  /**
   * The content of this {@link Comment} instance.
   */
  private String message;

  /**
   * Firestore requires a constructor with no arguments.
   */
  public Comment() {}

  /**
   * Creates an instance of {@link Comment} populating its fields.
   * @param author The {@link User} who created this {@link Comment} instance.
   * @param created The Timestamp indicating when this {@link Comment} instance was created.
   * @param message The content of this {@link Comment} instance.
   */
  public Comment(User author, Date created, String message) {
    this.author = author;
    this.created = created;
    this.message = message;
  }

  /**
   * Gets the unique ID of this {@link Comment} instance.
   * @return Returns the unique ID of this {@link Comment} instance.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the unique ID of this {@link Comment} instance.
   * @param id The unique ID of this {@link Comment} instance.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the author of this {@link Comment} instance.
   * @return Returns the {@link User} who created this {@link Comment} instance.
   */
  public User getAuthor() {
    return author;
  }

  /**
   * Gets the timestamp indicating when this {@link Comment} instance was first created.
   * @return A timestamp indicating when this {@link Comment} instance was created.
   */
  public Date getCreated() {
    if (created != null) {
      return created;
    }
    return new Date();
  }

  /**
   * The content of this {@link Comment} instance text.
   * @return The text of this {@link Comment} instance.
   */
  public String getMessage() {
    return message;
  }
}
