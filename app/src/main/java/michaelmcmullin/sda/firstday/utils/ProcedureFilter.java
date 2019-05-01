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

package michaelmcmullin.sda.firstday.utils;

/**
 * A variety of methods to filter procedures.
 */
public enum ProcedureFilter {
  /**
   * Displays only my procedures.
   */
  MINE,

  /**
   * Displays only my draft procedures.
   */
  MY_DRAFTS,

  /**
   * Displays only procedures matching a search term
   */
  SEARCH_RESULTS,

  /**
   * Displays procedures that have been shared with me.
   */
  SHARED,

  /**
   * Displays procedures that are marked public.
   */
  PUBLIC,

  /**
   * Displays all procedures that I have access to.
   */
  ALL
}
