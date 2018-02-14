package ws.main.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class FirstSpecificHandler extends AbstractHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FirstSpecificHandler.class);

    @Inject
    private FirstSpecificService service;


    @Override
    public @Nonnull Result specificProcessing(@Nonnull WebServiceRequest request) throws SomeSpecificException {
        LOG.info("specificProcessing() <-- request: {}", request);
        requestService.checkRequestAmount(request.requestAmount);

        ServiceCheckResult checkResult = service.checkRequest(request);

        /*
        another specific checks
        and data modifications
         */

        Result result = null;

        /*
        prepare modificated data to be set into Result,

        fill Result fields
        and fill a few new fields in WebServiceRequest to be updated at DB in AbstractHandler
         */

        LOG.info("specificProcessing() --> result: {}", result);
        return result;
    }

     /*
     few additional util methods
     */
}
