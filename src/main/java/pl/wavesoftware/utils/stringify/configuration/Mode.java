package pl.wavesoftware.utils.stringify.configuration;

/**
 * A mode of operation. In {@link #PROMISCUOUS} mode every defined field is
 * automatically inspected unless the field is annotated with {@link DoNotInspect} annotation.
 * <p>
 * In {@link #QUIET} mode only fields annotated with {@link Inspect} will get inspected.
 *
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
public enum Mode {
  PROMISCUOUS, QUIET;

  public static final Mode DEFAULT_MODE = PROMISCUOUS;
}
