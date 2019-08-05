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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.wavesoftware.eid.utils.EidPreconditions.checkArgument;
import static pl.wavesoftware.eid.utils.EidPreconditions.checkNotNull;

/**
 * A {@code Namespace} is used to provide a <em>scope</em> for data saved
 * within a {@link Store}.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public final class Namespace {
  /**
   * The default, global namespace which allows access to stored data.
   */
  public static final Namespace GLOBAL = Namespace.create(new Object());

  /**
   * Create a namespace which restricts access to data to all extensions
   * which use the same sequence of {@code parts} for creating a namespace.
   *
   * <p>The order of the {@code parts} is significant.
   *
   * <p>Internally the {@code parts} are compared using {@link Object#equals(Object)}.
   */
  public static Namespace create(Object... parts) {
    checkArgument(parts.length > 0, "20190805:223659");
    for (Object part : parts) {
      checkNotNull(part, "20190805:223729");
    }
    return new Namespace(parts);
  }

  private final List<?> parts;

  private Namespace(Object... parts) {
    this.parts = new ArrayList<>(Arrays.asList(parts));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Namespace that = (Namespace) obj;
    return this.parts.equals(that.parts);
  }

  @Override
  public int hashCode() {
    return this.parts.hashCode();
  }
}
