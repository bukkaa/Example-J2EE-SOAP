package ws.main.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


/**
 * Service that calls remote EJB bean for checking WebServiceRequest {@link path.to.WebServiceRequest}
 */
@ApplicationScoped
public class FirstSpecificService extends AbstractService {
    private static final Logger LOG = LoggerFactory.getLogger(FirstSpecificService.class);


    public ServiceCheckResult checkRequest(@Nonnull WebServiceRequest request) throws SomeSpecificException {
        LOG.info("checkRequest() <-- ");

        String orderId = request.requestOriginator + "_" + request.operId;

        FirstSpecificObject firstSpecificObject
                = requestService.find(/*search values for FirstSpecificObject*/);

        SecondSpecificObject secondSpecificObject
                = requestService.find(/*search values for SecondSpecificObject*/);

        ChkSRes chkRes = remoteService.check(
                request.requestOriginator,
                orderId,
                firstSpecificObject,
                secondSpecificObject);

        checkRequiredFields(chkRes);

        LOG.info("checkRequest() --> {}", chkRes);
        return new ServiceCheckResult()
                .setChkSRes(chkRes)
                .setFirstSpecificObject(firstSpecificObject)
                .setSecondSpecificObject(secondSpecificObject);
    }

    /*
     few additional util methods
     */
}
