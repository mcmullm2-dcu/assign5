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
 * Interface used for classes that need to retrieve a Procedure's unique ID.
 */
public interface ProcedureIdGetter {

  /**
   * Gets the unique ID of the procedure that implementing classes need to be able to access.
   * @return The unique ID of a relevant procedure.
   */
  String getProcedureId();
}
