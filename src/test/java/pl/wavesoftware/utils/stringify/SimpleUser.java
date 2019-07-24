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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.api.DoNotInspect;
import pl.wavesoftware.utils.stringify.api.Mask;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@Getter
@RequiredArgsConstructor
final class SimpleUser {
  private final String login;
  @DoNotInspect(conditionally = ProductionEnvironment.class)
  private final String password;
  @DoNotInspect(conditionally = ProductionEnvironment.class)
  private final String phoneNumber;
  @Mask(IbanMasker.class)
  private final String iban;
}
