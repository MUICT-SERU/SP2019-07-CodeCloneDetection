@GET
@Path(KMSRESTConstants.KEY_RESOURCE + "/{name:.*}")
public Response getKey(@PathParam("name") String name) throws Exception {
    try {
        LOG.trace("Entering getKey method.");
        LOG.debug("Getting key information for key with name {}.", name);
        LOG.trace("Exiting getKey method.");
        return getMetadata(name);
    } catch (Exception e) {
        LOG.debug("Exception in getKey.", e);
        throw e;
    }
}