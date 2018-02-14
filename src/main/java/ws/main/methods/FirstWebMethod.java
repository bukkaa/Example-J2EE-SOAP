package ws.main.methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.main.handlers.AbstractHandler;
import ws.main.handlers.FirstSpecificHandler;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class FirstWebMethod extends AbstractWebMethod<FirstRequestType, FirstResponseType> {
    private static final Logger LOG = LoggerFactory.getLogger(FirstWebMethod.class);

    @Inject
    private FirstSpecificHandler handler;


    @Override
    protected @Nonnull AbstractHandler getHandler() {
        return handler;
    }

    @Override
    protected @Nonnull FirstResponseType newWsResponse() {
        return new CreatePAReqRes();
    }

    @Override
    protected @Nonnull WebServiceRequest toWsRequest(@Nonnull FirstRequestType wsRequest) {
        LOG.info("toWsRequest() <-- {}", wsRequest);
        return basicMapping.toWebServiceRequest(wsRequest)
                .setSpecificField1(wsRequest.specField1)
                .setSpecificField2(wsRequest.specField2.someField)
                /*
                few more fields set like this
                 */;
    }

    @Override
    protected @Nonnull FirstResponseType toWsResponse(@Nonnull WebServiceRequest request) {
        LOG.info("toWsResponse() <-- {}", request);
        return basicMapping.toWsResponse(request, newWsResponse())
                .setSomeInnerObjectA(new SomeInnerObjectA()
                        .setFieldA(request.fieldAforObjA)
                        .setFieldB(request.fieldBforObjA))
                .setSomeInnerObjectB(new SomeInnerObjectB()
                        .setFieldA(request.fieldAforObjB)
                        .setFieldB(request.fieldBforObjB)
                        .setFieldC(request.getFieldCforObjBSafely()));

    }
}
