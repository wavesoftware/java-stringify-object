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

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wavesoftware.utils.stringify.api.Inspect;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
final class Earth extends Planet {

  private static final long serialVersionUID = 20180430201544L;

  @Inspect
  private Moon moon;
  @Inspect
  private int dayOfYear;
  @Inspect
  private char type;


  Earth() {
    super(true, "Earth");
  }
}
