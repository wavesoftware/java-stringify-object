/*
 * Copyright 2018-2019 Wave Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.wavesoftware.utils.stringify;

import lombok.Setter;
import pl.wavesoftware.utils.stringify.api.DisplayNull;
import pl.wavesoftware.utils.stringify.api.DoNotInspect;
import pl.wavesoftware.utils.stringify.api.Inspect;

import java.util.List;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@Setter
class Person {
  private volatile int id;
  @DisplayNull
  private transient Person parent;
  private List<Person> childs;
  private Account account;
  @Inspect(conditionally = IsInDevelopment.class)
  private String password;
  @DoNotInspect
  private String ignored;
}
