@Override
public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
    return Map.class.isAssignableFrom(aClass) || List.class.isAssignableFrom(aClass);
}