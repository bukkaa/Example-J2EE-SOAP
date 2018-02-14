package ws.main.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;

/**
 * Service that calls remote EJB bean to authenticate WebServiceRequest {@link path.to.WebServiceRequest}
 */
@ApplicationScoped
public class SecondSpecificService extends AbstractService {
    private static final Logger LOG = LoggerFactory.getLogger(SecondSpecificService.class);

    public ServiceAuthRes authenticateRequest(@Nonnull WebServiceRequest request) throws SomeSpecificException {
        LOG.info("authenticateRequest() <-- ");

        String extOrderId = request.requestOriginator + "_" + request.operId;

        ServiceAuthRes authRes = remoteService.authenticate(
                request.requestOriginator,
                extOrderId,
                request.paData.requestPa,
                request.anotherData);

        if (!authRes.isApproved()) {
            throw new SomeSpecificException(NOT_AUTHENTICATED, "Not authenticated");
        }

        LOG.info("authenticateRequest() --> {}", authRes);
        return auth3DSRes;
    }

     /*
     few additional util methods
     */
}
