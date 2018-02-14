package ws.main.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.main.services.SecondSpecificService;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SecondSpecificHandler extends AbstractHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SecondSpecificHandler.class);

    @Inject
    private SecondSpecificService service;


    @Override
    public @Nonnull Result specificProcessing(@Nonnull WebServiceRequest request) throws SomeSpecificException {
        LOG.info("specificProcessing() <-- request: {}", request);

        /*
        some specific checks here
         */

        ServiceAuthResult serviceAuthResult = service.authenticateRequest(request);

         /*
         prepare modificated data to be set into WebServiceRequest
         to be updated at DB in AbstractHandler
         */

        LOG.info("specificProcessing() --> result: {}", result);
        return Result.OK;
    }

    /*
     few additional util methods
     */
}
