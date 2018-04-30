package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.eid.utils.EidPreconditions;
import pl.wavesoftware.utils.stringify.configuration.InspectionPoint;
import pl.wavesoftware.utils.stringify.configuration.Mode;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;

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
final class ToStringResolverImpl implements ToStringResolver {
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
  private InspectingFieldFactory inspectingFieldFactory;
  private BeanFactory beanFactory;
  private final State state;
  private final Function<Object, CharSequence> alternative;


  /**
   * A default constructor
   *
   * @param target a target object to resolve
   */
  ToStringResolverImpl(Object target) {
    this(
      target,
      new StateImpl(),
      new ReflectionBeanFactory(),
      new InspectingFieldFactory(Mode.DEFAULT_MODE)
    );
  }

  private ToStringResolverImpl(Object target,
                               State state,
                               BeanFactory beanFactory,
                               InspectingFieldFactory inspectingFieldFactory) {
    this.state = state;
    this.target = target;
    this.beanFactory = beanFactory;
    this.inspectingFieldFactory = inspectingFieldFactory;
    this.alternative = new ObjectInspectorImpl();
  }

  @Override
  public ToStringResolver withMode(Mode mode) {
    this.inspectingFieldFactory = new InspectingFieldFactory(mode);
    return this;
  }

  @Override
  public ToStringResolver withBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
    return this;
  }

  @Override
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
      InspectionPoint inspectionPoint = createInspectionPoint(field);
      InspectingField inspectingField = inspectingFieldFactory.create(inspectionPoint, beanFactory);
      if (inspectingField.shouldInspect()) {
        inspectAnnotatedField(properties, field, inspectingField);
      }
    }
  }

  private InspectionPoint createInspectionPoint(Field field) {
    return new InspectionPointImpl(field, target);
  }

  private void inspectAnnotatedField(final Map<String, CharSequence> properties,
                                     final Field field,
                                     final InspectingField inspectingField) {
    tryToExecute(new EidPreconditions.UnsafeProcedure() {
      @Override
      public void execute() throws IllegalAccessException {
        ensureAccessible(field);
        Object value = field.get(target);
        if (value == null) {
          if (inspectingField.showNull()) {
            properties.put(field.getName(), null);
          }
        } else {
          properties.put(field.getName(), inspectObject(value));
        }
      }
    }, "20130422:154938");
  }

  private void ensureAccessible(Field field) {
    if (!field.isAccessible()) {
      field.setAccessible(true);
    }
  }

  private CharSequence inspectObject(Object object) {
    for (ObjectInspector inspector : OBJECT_INSPECTORS) {
      if (inspector.consentTo(object, state)) {
        return inspector.inspect(object, alternative);
      }
    }
    ToStringResolverImpl sub = new ToStringResolverImpl(
      object,
      state,
      beanFactory,
      inspectingFieldFactory
    );
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
