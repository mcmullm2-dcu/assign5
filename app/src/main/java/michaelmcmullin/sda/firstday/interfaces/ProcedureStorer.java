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

import java.util.List;
import java.util.Set;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * Interface used by classes that need to store/retrieve the data from a single procedure.
 */
public interface ProcedureStorer {

  /**
   * Stores a Procedure instance for later retrieval.
   * @param procedure The Procedure instance to store.
   */
  void StoreProcedure(Procedure procedure);

  /**
   * Retrieves a previously stored Procedure instance.
   * @return Returns an instance of a previously stored instance, or null.
   */
  Procedure GetProcedure();

  /**
   * Stores a list of steps associated with a stored Procedure instance for later retrieval.
   * @param steps The list of Step instances to store.
   */
  void StoreSteps(List<Step> steps);

  /**
   * Retrieves a previously stored list of Step instances associated with a single procedure.
   * @return Returns a list of Step instances, or null.
   */
  List<Step> GetSteps();

  /**
   * Stores a set of distinct tags for a particular Procedure instance for later retrieval.
   * @param tags The set of tags to store.
   */
  void StoreTags(Set<String> tags);

  /**
   * Retrieves a previously stored list of tag strings, used for searching, associated with a
   * particular Procedure instance.
   * @return Returns a set of tag strings, or null.
   */
  Set<String> GetTags();
}
