package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.eid.utils.EidPreconditions;
import pl.wavesoftware.utils.stringify.annotation.Inspect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static pl.wavesoftware.eid.utils.EidPreconditions.tryToExecute;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
public final class ToStringResolver {
  private static final Iterable<ObjectInspector> OBJECT_INSPECTORS = Arrays.asList(
    new CharSequenceInspector(),
    new PrimitiveInspector(),
    new CharacterInspector(),
    new JPALazyInspector(),
    new MapInspector(),
    new IterableInspector(),
    new RecursionInspector()
  );

  private final Object target;
  private final State state;
  private final Function<Object, CharSequence> alternative;


  /**
   * A default constructor
   *
   * @param target a target object to resolve
   */
  public ToStringResolver(Object target) {
    this(target, new StateImpl());
  }

  private ToStringResolver(Object target,
                           State state) {
    this.state = state;
    this.target = target;
    this.alternative = new ObjectInspectorImpl();
  }

  /**
   * Resolves a {@link String} representation of given object.
   *
   * @return String representation
   */
  public CharSequence resolve() {
    state.markIsInspected(target);
    StringBuilder sb = new StringBuilder();
    sb.append('<');
    sb.append(target.getClass().getSimpleName());
    CharSequence props = propertiesForToString();
    if (props.length() != 0) {
      sb.append(' ');
      sb.append(props);
    }
    sb.append('>');
    return sb;
  }

  private CharSequence propertiesForToString() {
    Map<String, CharSequence> props;
    props = inspectTargetAsClass(target.getClass());
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, CharSequence> entry : props.entrySet()) {
      String fieldName = entry.getKey();
      CharSequence fieldStringValue = entry.getValue();
      sb.append(fieldName);
      sb.append("=");
      sb.append(fieldStringValue);
      sb.append(", ");
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb;
  }

  private Map<String, CharSequence> inspectTargetAsClass(Class<?> type) {
    Class<?> supertype = type.getSuperclass();
    Map<String, CharSequence> props;
    if (supertype == null || supertype.equals(Object.class)) {
      props = new LinkedHashMap<>();
    } else {
      props = inspectTargetAsClass(supertype);
    }
    inspectFields(type.getDeclaredFields(), props);
    return props;
  }

  private void inspectFields(Field[] fields,
                             Map<String, CharSequence> properties) {
    for (Field field : fields) {
      boolean accessable = field.isAccessible();
      if (!accessable) {
        field.setAccessible(true);
      }
      if (field.isAnnotationPresent(Inspect.class)) {
        Inspect annot = field.getAnnotation(Inspect.class);
        inspectAnnotatedField(properties, field, annot);
      }
    }
  }

  private void inspectAnnotatedField(final Map<String, CharSequence> properties,
                                     final Field field,
                                     final Inspect inspect) {
    tryToExecute(new EidPreconditions.UnsafeProcedure() {
      @Override
      public void execute() throws Exception {
        Object value = field.get(target);
        if (value == null) {
          if (inspect.showNull()) {
            properties.put(field.getName(), null);
          }
        } else {
          properties.put(field.getName(), inspectObject(value));
        }
      }
    }, "20130422:154938");
  }

  private CharSequence inspectObject(Object object) {
    for (ObjectInspector inspector : OBJECT_INSPECTORS) {
      if (inspector.consentTo(object, state)) {
        return inspector.inspect(object, alternative);
      }
    }
    ToStringResolver sub = new ToStringResolver(object, state);
    return sub.resolve();
  }

  private final class ObjectInspectorImpl implements Function<Object, CharSequence> {

    @Override
    public CharSequence apply(Object object) {
      return inspectObject(object);
    }
  }

  private static final class StateImpl implements State {
    private static final Object CONTAIN = new Object();

    private final Map<Object, Object> resolved;

    private StateImpl() {
      this(new IdentityHashMap<>());
    }

    private StateImpl(Map<Object, Object> resolved) {
      this.resolved = resolved;
    }

    @Override
    public boolean wasInspected(Object object) {
      return resolved.containsKey(object);
    }

    @Override
    public void markIsInspected(Object object) {
      resolved.put(object, CONTAIN);
    }
  }
}
