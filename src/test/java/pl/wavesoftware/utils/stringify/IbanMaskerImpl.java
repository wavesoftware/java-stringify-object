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

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.wavesoftware.eid.utils.EidPreconditions.checkState;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class IbanMaskerImpl implements IbanMasker {
  private static final Pattern IBAN = Pattern.compile(
    "\\b([A-Z]{2}[0-9]{2})(?:[ ]?[0-9]{4}){4}(?!(?:[ ]?[0-9]){3})([ ]?[0-9]{1,2})?\\b"
  );
  @Override
  public Class<String> type() {
    return String.class;
  }

  @Override
  public CharSequence mask(@Nullable String target) {
    return target != null
      ? maskIban(target) : "none";
  }

  private CharSequence maskIban(String iban) {
    Matcher matcher = IBAN.matcher(iban);
    checkState(matcher.find(), "20190724:232858");
    return '"' + matcher.group(1).trim()
      + " **** **** **** **** "
      + matcher.group(2).trim() + '"';
  }
}
