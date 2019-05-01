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

package michaelmcmullin.sda.firstday.services;

import michaelmcmullin.sda.firstday.interfaces.services.ImageLabelService;
import michaelmcmullin.sda.firstday.interfaces.services.QrService;

/**
 * Class to store default services in one location to simplify updates.
 */
public class Services {

  /**
   * Service used for reading and writing QR codes.
   */
  public static QrService QrService = new FirebaseQr();

  /**
   * Service used for labelling a Bitmap image.
   */
  public static ImageLabelService ImageLabelService = new FirebaseImageLabeller();
}