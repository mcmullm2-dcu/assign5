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

package michaelmcmullin.sda.firstday.interfaces.services;

import android.graphics.Bitmap;
import android.support.v4.util.Consumer;
import java.util.List;
import michaelmcmullin.sda.firstday.models.ImageLabel;

/**
 * Defines a service for labelling the content of an image
 */
public interface ImageLabelService {

  /**
   * Attempts to label any objects found in an image's content.
   *
   * @param image The Bitmap image to analyse for content.
   * @param consumer The method to call with the resulting list of labels.
   * @param error Text to log in the event of an error.
   */
  void ReadImageLabels(Bitmap image, final Consumer<List<ImageLabel>> consumer, final String error);
}
