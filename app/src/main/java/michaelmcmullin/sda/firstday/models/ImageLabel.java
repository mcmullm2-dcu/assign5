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

import android.support.annotation.NonNull;

/**
 * Class that holds information about an image label, which helps describe an image's content.
 */
public class ImageLabel implements Comparable<ImageLabel> {

  /**
   * The text description of this label.
   */
  private final String text;
  /**
   * The confidence level of this label ranging from 0.0 (not confident) to 1.0 (very confident).
   */
  private final float confidence;

  /**
   * Creates an instance of {@link ImageLabel} defining it's text and confidence level.
   *
   * @param text The text description of this label.
   * @param confidence The confidence level of this label ranging from 0.0 (not confident) to
   *     1.0 (very confident).
   */
  public ImageLabel(String text, float confidence) {
    this.text = text;
    this.confidence = confidence;
  }

  /**
   * Gets the text description of this label.
   *
   * @return Returns the detected label for an image.
   */
  public String getText() {
    return this.text;
  }

  /**
   * Gets the confidence level of this label.
   *
   * @return Returns a confidence level of this label from 0.0 (not confident) to 1.0 (very
   *     confident).
   */
  private float getConfidence() {
    return this.confidence;
  }

  /**
   * Defines the natural ordering of {@link ImageLabel} collections, specifically that they should
   * start from the most confident to least confident labels.
   *
   * @param other The {@link ImageLabel} to compare to this one.
   * @return Returns 0 if both can be ordered the same; a negative number if this instance appears
   *     before <code>other</code>, and a positive number if it appears after.
   */
  @Override
  public int compareTo(@NonNull ImageLabel other) {
    Float confidence = other.getConfidence();
    return confidence.compareTo(this.getConfidence());
  }
}
