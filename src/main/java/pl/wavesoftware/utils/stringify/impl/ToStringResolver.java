package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.utils.stringify.Inspect;

import java.lang.reflect.Field;
import java.time.temporal.Temporal;
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
  public String resolve() {
    inspecting(target);
    StringBuilder sb = new StringBuilder();
    sb.append('<');
    sb.append(target.getClass().getSimpleName());
    String props = propertiesForToString();
    if (!"".equals(props)) {
      sb.append(' ');
      sb.append(props);
    }
    sb.append('>');
    return sb.toString();
  }

  private String propertiesForToString() {
    Map<String, String> props;
    props = inspectTargetAsClass(target.getClass());
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, String> entry : props.entrySet()) {
      String fieldName = entry.getKey();
      String fieldStringValue = entry.getValue();
      sb.append(fieldName);
      sb.append("=");
      sb.append(fieldStringValue);
      sb.append(", ");
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  private Map<String, String> inspectTargetAsClass(Class<?> type) {
    Class<?> supertype = type.getSuperclass();
    Map<String, String> props;
    if (supertype == null || supertype.equals(Object.class)) {
      props = new LinkedHashMap<>();
    } else {
      props = inspectTargetAsClass(supertype);
    }
    inspectFields(type.getDeclaredFields(), props);
    return props;
  }

  private void inspectFields(Field[] fields,
                             Map<String, String> properties) {
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

  private void inspectAnnotatedField(Map<String, String> properties,
                                     Field field,
                                     Inspect inspect) {
    tryToExecute(() -> {
      Object value = field.get(target);
      if (value == null) {
        if (inspect.showNull()) {
          properties.put(field.getName(), null);
        }
      } else {
        properties.put(field.getName(), inspectObject(value));
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

  private String inspectObject(Object o) {
    if (o instanceof String) {
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
      || o instanceof Temporal;
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
