package ws.main.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;

import static java.time.Duration.between;
import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;

public abstract class AbstractHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractHandler.class);

    @Inject
    private SecurityService securityService;

    @Inject
    protected RequestService requestService;

    @Inject
    private TimeSupport clock;


    /**
     * Method-specific part of request processing.
     *
     * @throws SomeSpecificException if error occurred during specific processing
     */
    public abstract @Nonnull Result specificProcessing(@Nonnull WebServiceRequest request)
            throws SomeSpecificException;

    @Transactional(NOT_SUPPORTED)
    public void handleRequest(@Nonnull WebServiceRequest request) {
        ZonedDateTime startTime = clock.localNow();
        LOG.debug("start time: {}", startTime);

        Result result = BUG; // must be changed

        boolean requestPersisted = false;
        try {
            // find certificate and set its id if one exists
            securityService.init(request.getRequestOriginator(), request.getCertDer());


            /*
            some security checks here
             */


            // save request
            requestService.persist(request);
            requestPersisted = true;


            /*
            some security checks here
             */


            // call specific handler
            result = specificProcessing(request);
        } catch (SomeSpecificException e) {
            LOG.warn(e.getMessage());
            result = e.getResult();
        } catch (Exception e) {
            LOG.error("unexpected error", e);
            result = new Result(SYSTEM_MALFUNCTION, "unexpected error");
        } finally {
            try {
                ZonedDateTime finishTime = clock.localNow();
                LOG.debug("finish time: {}", finishTime);

                // set response fields
                request.setResponseTime(clock.utcNow());
                long processingTime = between(startTime, finishTime).toMillis();
                LOG.debug("processing time: {} ms", processingTime);
                request.setProcessingTime(processingTime);
                putResultIntoReq(result, request);

                // update response fields of MpicsRequest
                if (requestPersisted) {
                    requestService.update(request);
                }
            } catch (Exception e) {
                LOG.error("Error while updating request: " + e.getMessage(), e);
                result = new Result(SYSTEM_MALFUNCTION, "unexpected error");
                putResultIntoReq(result, request);
            } finally {
                LOG.info("result: {}", result);
            }
        }
    }

    private void putResultIntoReq(@Nonnull Result result, @Nonnull MpicsRequest mpicsRequest) {
        mpicsRequest.setErrorCode(result.getCode());
        mpicsRequest.setResponseDescription(result.getDescription());

        if (!isResultSuccess(result)) {
            mpicsRequest.getExtensions().removeIf(ext -> !ext.isRequestType());
        }
    }
}























