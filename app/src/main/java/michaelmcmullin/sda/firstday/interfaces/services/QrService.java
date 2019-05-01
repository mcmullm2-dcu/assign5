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

/**
 * Defines a service for reading and writing QR codes.
 */
public interface QrService {

  /**
   * Reads a QR code from a supplied Bitmap image passes it to another method.
   *
   * @param image The source image to read the QR code from.
   * @param consumer The method to call with the resulting String.
   * @param error Text to pass to consumer if no QR code is found.
   */
  void ReadQrCode(Bitmap image, final Consumer<String> consumer, final String error);

  /**
   * Converts a String into a QR code Bitmap image.
   *
   * @param code The String to convert to a QR code.
   * @return Returns a Bitmap of the generated QR code.
   */
  Bitmap GenerateQrCode(String code);
}
