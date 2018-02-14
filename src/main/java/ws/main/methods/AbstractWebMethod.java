package ws.main.methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import path.to.utils.mdc.ReqIdMdc;
import ws.api.validation.WsValidator;
import ws.main.handlers.AbstractHandler;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidatorFactory;
import java.util.function.Function;

public abstract class AbstractWebMethod<T extends CommonRequest, R extends ResultType> implements Function<T, R> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractWebMethod.class);

    private static final String REQ_ID_MDC_KEY = "RequestId";

    @Inject
    private Config config;

    @Inject
    protected BasicMapping basicMapping;

    @Inject
    protected TimeSupport timeSupport;

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private ValidatorFactory validatorFactory;

    private WsValidator wsValidator;

    private String requestIdHeaderName;


    @PostConstruct
    private void init() {
        wsValidator = new WsValidator(validatorFactory.getValidator());

        requestIdHeaderName = config.getRequestIdHeaderName();
        LOG.info("apache request id header: {}", requestIdHeaderName);
    }

    @Override
    public R apply(T request) {
        R response;

        try (ReqIdMdc ignored = ReqIdMdc.set(REQ_ID_MDC_KEY, requestIdHeaderName, httpServletRequest)) {
            LOG.info("{} request: {}", request.methodId.getString(), request);

            validate(request);

            WebServiceRequest webServiceRequest = toWsRequest(request);
            getHandler().handleRequest(webServiceRequest);
            response = toWsResponse(webServiceRequest);

        } catch (Throwable e) {
            LOG.error("unexpected error", e);
            response = exceptionalWsResponse(e);
        }

        LOG.info("response: {}", response);
        return response;
    }

    protected abstract @Nonnull AbstractHandler getHandler();

    /**
     * Creates {@link WebServiceRequest} by request data.
     *
     * @throws SomeSpecificException if {@link WebServiceRequest} cannot be created.
     */
    protected abstract @Nonnull WebServiceRequest toWsRequest(@Nonnull T wsRequest) throws SomeSpecificException;

    /**
     * Creates minimal WS response object.
     */
    protected abstract @Nonnull R newWsResponse();

    /**
     * Creates response by {@link WebServiceRequest}.
     */
    protected abstract @Nonnull R toWsResponse(@Nonnull WebServiceRequest webServiceRequest);

    /**
     * Creates response by exception.
     *
     * Supports {@link SomeSpecificException}.
     */
    protected @Nonnull R exceptionalWsResponse(@Nonnull Throwable e) {
        return basicMapping.toWsResponse(e, newWsResponse());
    }

    /**
     * @throws SomeSpecificException if any format violations were found
     */
    private void validate(T req) {
        String validationMessage = wsValidator.validate2SingleMessage(req);

        if (!isNullOrEmpty(validationMessage)) {
            throw new SomeSpecificException("Format error",
                    new ResultType(ERROR_FORMAT_996, validationMessage));
        }

        LOG.info(isNullOrEmpty(validationMessage)
                ? "validated successfully"
                : "validation failed: " + validationMessage);
    }
}
