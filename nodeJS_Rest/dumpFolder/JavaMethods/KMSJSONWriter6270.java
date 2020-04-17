@Override
public void writeTo(Object obj, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
    Writer writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
    JsonSerialization.writer().writeValue(writer, obj);
}