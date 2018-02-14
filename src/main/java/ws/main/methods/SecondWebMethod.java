package ws.main.methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.main.handlers.AbstractHandler;
import ws.main.handlers.SecondSpecificHandler;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class SecondWebMethod extends AbstractWebMethod<SecondRequestType, SecondResponseType> {
    private static final Logger LOG = LoggerFactory.getLogger(SecondWebMethod.class);

    @Inject
    private SecondSpecificHandler handler;


    @Override
    protected @Nonnull AbstractHandler getHandler() {
        return handler;
    }

    @Override
    protected @Nonnull SecondResponseType newWsResponse() {
        return new SecondResponseType();
    }

    @Override
    protected @Nonnull WebServiceRequest toWsRequest(@Nonnull SecondRequestType wsRequest) {
        LOG.info("toWsRequest() <-- {}", wsRequest);
        return basicMapping.toWebServiceRequest(wsRequest)
                .setSpecificField1(wsRequest.specField1)
                .setSpecificField2(wsRequest.specField2.someField)
                /*
                few more fields set like this
                 */;
    }

    @Override
    protected @Nonnull SecondResponseType toWsResponse(@Nonnull WebServiceRequest request) {
        LOG.info("toWsResponse() <-- ");
        return basicMapping.toWsResponse(request, newWsResponse())
                .setSomeInnerObjectA(new SomeInnerObjectA()
                        .setFieldA(request.fieldAforObjA)
                        .setFieldB(request.fieldBforObjA))
                /*
                few more fields set like this
                 */;
    }

}
