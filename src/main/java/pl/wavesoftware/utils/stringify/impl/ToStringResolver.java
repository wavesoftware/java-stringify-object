package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.eid.utils.EidPreconditions;
import pl.wavesoftware.utils.stringify.Inspect;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static pl.wavesoftware.eid.utils.EidPreconditions.tryToExecute;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
public final class ToStringResolver {
  private static final ClassLocator TEMPORAL_CLASS_LOCATOR =
    new ClassLocator("java.time.temporal.Temporal");
  private static final Object CONTAIN = new Object();
  private static final JPALazyChecker LAZY_CHECKER = new JPALazyCheckerFacade();

  private final Map<Object, Object> resolved;
  private final Object target;

  /**
   * A default constructor
   *
   * @param target a target object to resolve
   */
  public ToStringResolver(Object target) {
    this(target, new IdentityHashMap<>());
  }

  private ToStringResolver(Object target,
                           Map<Object, Object> resolved) {
    this.resolved = resolved;
    this.target = inspecting(target);
  }

  /**
   * Resolves a {@link String} representation of given object.
   *
   * @return String representation
   */
  public CharSequence resolve() {
    inspecting(target);
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
          properties.put(field.getName(), ToStringResolver.this.inspectObject(value));
        }
      }
    }, "20130422:154938");
  }

  private Object inspecting(Object object) {
    resolved.put(object, CONTAIN);
    return object;
  }

  private boolean wasInspected(Object object) {
    return resolved.containsKey(object);
  }

  private CharSequence inspectObject(Object o) {
    if (o instanceof CharSequence) {
      return "\"" + o.toString() + "\"";
    } else if (o instanceof Character) {
      return "'" + o.toString() + "'";
    } else if (isPrimitive(o)) {
      return o.toString();
    } else if (LAZY_CHECKER.isLazy(o)) {
        return "⁂Lazy";
    } else if (o instanceof Map) {
      return inspectMap((Map<?,?>) o);
    } else if (o instanceof Iterable) {
      return inspectIterable((Iterable<?>) o);
    } else if (wasInspected(o)) {
      return "(↻)";
    } else {
      ToStringResolver sub = new ToStringResolver(o, resolved);
      return sub.resolve();
    }
  }

  private static boolean isPrimitive(Object o) {
    return o instanceof Number
      || o instanceof Boolean
      || o instanceof Enum
      || isDatelike(o);
  }

  private static boolean isDatelike(Object o) {
    return o instanceof Date
      || isInstanceOfTemporal(o);
  }

  private static boolean isInstanceOfTemporal(Object candidate) {
    if (TEMPORAL_CLASS_LOCATOR.isAvailable()) {
      Class<?> temporalCls = TEMPORAL_CLASS_LOCATOR.get();
      return temporalCls.isInstance(candidate);
    }
    return false;
  }

  private String inspectIterable(Iterable<?> iterable) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (Object elem : iterable) {
      sb.append(inspectObject(elem));
      sb.append(",");
    }
    if (sb.length() > 1) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }

  private String inspectMap(Map<?, ?> map) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      Object key = entry.getKey();
      Object value = entry.getValue();
      sb.append(inspectObject(key));
      sb.append(": ");
      sb.append(inspectObject(value));
      sb.append(", ");
    }
    if (sb.length() > 1) {
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("}");
    return sb.toString();
  }
}
