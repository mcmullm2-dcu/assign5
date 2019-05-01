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

package michaelmcmullin.sda.firstday.interfaces;

/**
 * Interface used by classes that have defined ways to get and set their data in a form that other
 * classes can make use of.
 *
 * @param <T> The type of data available to store and retrieve.
 */
public interface GetterSetter<T> {

  /**
   * Persists its data in a form that can be read by other classes.
   */
  void SetData();

  /**
   * Gets the persisted data.
   *
   * @return The persisted data, or null.
   */
  T GetData();
}
