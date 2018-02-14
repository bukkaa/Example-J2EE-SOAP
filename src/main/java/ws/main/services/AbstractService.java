package ws.main.services;

import ws.main.services.RequestService;

import javax.annotation.Resource;
import javax.ejb.EJB;

/**
 * General prototype for specific services
 */
public abstract class AbstractService {

    @Resource(lookup = "java:app/AppName")
    protected String appName;

    @EJB(lookup = "java:global/some_service/RemoteServiceBean")
    protected RemoteServiceInterface remoteService;

    @Inject
    protected RequestService requestService;
}
