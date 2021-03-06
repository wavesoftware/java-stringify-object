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

package pl.wavesoftware.utils.stringify.api;

/**
 * A mode of operation. In {@link #PROMISCUOUS} mode every defined field is
 * automatically inspected unless the field is annotated with {@link DoNotInspect} annotation.
 * <p>
 * In {@link #QUIET} mode only fields annotated with {@link Inspect} will get inspected.
 *
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
public enum Mode {
  PROMISCUOUS, QUIET;

  public static final Mode DEFAULT_MODE = PROMISCUOUS;
}
