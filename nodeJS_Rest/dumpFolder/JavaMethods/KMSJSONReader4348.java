@Override
public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return type.isAssignableFrom(Map.class) || type.isAssignableFrom(List.class);
}