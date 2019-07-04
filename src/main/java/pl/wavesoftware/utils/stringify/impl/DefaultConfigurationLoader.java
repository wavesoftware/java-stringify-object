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

package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.utils.stringify.spi.Configurator;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class DefaultConfigurationLoader {
  private DefaultConfigurationLoader() {
    // not reachable
  }

  static DefaultConfiguration load() {
    ServiceLoader<Configurator> serviceLoader = ServiceLoader.load(Configurator.class);
    DefaultConfiguration configuration = new DefaultConfiguration();
    StreamSupport.stream(serviceLoader.spliterator(), false)
      .forEach(configurator -> configurator.configure(configuration));
    return configuration;
  }
}
