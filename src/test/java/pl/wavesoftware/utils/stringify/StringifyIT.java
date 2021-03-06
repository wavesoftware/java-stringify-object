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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;
import pl.wavesoftware.jmh.junit.utilities.JmhCleaner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
@Tag("perf")
public class StringifyIT {
  private static final int PERCENT = 100;
  private static final int OPERATIONS = 1000;
  private static final Logger LOG = LoggerFactory.getLogger(StringifyIT.class);
  private static final Planet TEST_OBJECT = new TestRepository().createTestPlanet();

  @RegisterExtension
  static final JmhCleaner cleaner = new JmhCleaner(StringifyIT.class);

  @Test
  void performBenckmark() throws RunnerException {
    Options opt = new OptionsBuilder()
      .include(this.getClass().getName() + ".*")
      .mode(Mode.Throughput)
      .timeUnit(TimeUnit.MILLISECONDS)
      .operationsPerInvocation(OPERATIONS)
      .warmupTime(TimeValue.seconds(1))
      .warmupIterations(2)
      .measurementTime(TimeValue.seconds(1))
      .measurementIterations(5)
      .threads(4)
      .forks(1)
      .shouldFailOnError(true)
      .shouldDoGC(true)
      .jvmArgs("-Xms256m", "-Xmx256m", "-XX:+UseG1GC", "-XX:+AggressiveOpts",
        "-XX:+OptimizeStringConcat", "-XX:+UseStringDeduplication")
      .build();

    Runner runner = new Runner(opt);
    Collection<RunResult> results = runner.run();
    assertThat(results).hasSize(2);

    RunResult control = getRunResultByName(results, "lombok");
    RunResult eid = getRunResultByName(results, "stringifier");
    assertThat(control).isNotNull();
    assertThat(eid).isNotNull();

    double controlScore = control.getAggregatedResult().getPrimaryResult().getScore();
    double eidScore = eid.getAggregatedResult().getPrimaryResult().getScore();

    String title = "method speed quotient to the control sample";
    String eidTitle = String.format("%s %s should be at least %.2f%%", "#stringifier()",
      title, getSpeedThreshold() * PERCENT);

    double eidTimes = eidScore / controlScore;

    LOG.info("Control sample method time per operation: {} ops / ms", controlScore);
    LOG.info("#stringifier() method time per operation: {} ops / ms", eidScore);
    LOG.info("{} and is {}%", eidTitle, eidTimes * PERCENT);

    assertThat(eidTimes)
      .as(eidTitle)
      .isGreaterThanOrEqualTo(getSpeedThreshold());
  }

  @Benchmark
  public void lombok(Blackhole bh) {
    for (int i = 0; i < OPERATIONS; i++) {
      // Lombok
      bh.consume(TEST_OBJECT.toString());
    }
  }

  @Benchmark
  public void stringifier(Blackhole bh) {
    for (int i = 0; i < OPERATIONS; i++) {
      bh.consume(Stringify.of(TEST_OBJECT).toString());
    }
  }

  private static double getSpeedThreshold() {
    double jreVersion = Double.parseDouble(System.getProperty("java.specification.version"));
    if (jreVersion >= 9d) {
      // 3% performance of static lombok code for Java 9+
      return 0.03d;
    } else if (jreVersion >= 1.8d) {
      // 5% performance of static lombok code for Java 8
      return 0.05d;
    } else {
      // 0.5% performance of static lombok code for Java 7
      return 0.005d;
    }
  }

  private static RunResult getRunResultByName(Collection<RunResult> results, String name) {
    String fullName = String.format("%s.%s", StringifyIT.class.getName(), name);
    for (RunResult result : results) {
      if (result.getParams().getBenchmark().equals(fullName)) {
        return result;
      }
    }
    throw new EidRuntimeException("20180420:124701", "Invalid name: " + name);
  }
}
