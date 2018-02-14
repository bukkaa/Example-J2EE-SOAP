package ws.main.methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static sun.security.provider.X509Factory.BEGIN_CERT;
import static sun.security.provider.X509Factory.END_CERT;

@ApplicationScoped
public class BasicMapping {
    private static final Logger LOG = LoggerFactory.getLogger(BasicMapping.class);

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private TimeSupport clock;

    @Inject
    private Config config;

    private String sslClientCertHeaderName;

    @PostConstruct
    protected void init() {
        sslClientCertHeaderName = config.getSslClientCertHeaderName();
        LOG.info("SSL Client Certificate Header: {}", sslClientCertHeaderName);
    }

    /**
     * Creates {@link WebServiceRequest} by request data.
     *
     * @throws SomeSpecificException if {@link WebServiceRequest} cannot be created.
     */
    public @Nonnull WebServiceRequest toWebServiceRequest(CommonRequest wsRequest) {
        WebServiceRequest request = new WebServiceRequest();

        request
                .setClientIp(getClientIpFrom(httpServletRequest))
                .setRequestOriginator(wsRequest.header.originator)
                .setRequestInstance(wsRequest.header.instance)
                .setRequestId(wsRequest.header.getIdAsLong())
                .setRequestTime(wsRequest.header.time)
                .setRequestMethod(wsRequest.methodId);

        wsRequest.getNonnullExtensions().stream()
                .map(ext -> {
                    RequestExtension reqExtension = new RequestExtension(ext.id, ext.isCritical());
                    ext.getParameters().stream()
                            .map(param -> new RequestExtensionParam(param.name, param.value))
                            .forEach(reqExtension::addParam);
                    return reqExtension;
                })
                .forEach(request::addExtension);

        return request;
    }

    /**
     * Creates response by {@link WebServiceRequest}.
     */
    public @Nonnull <R extends ResultType> R toWsResponse(@Nonnull WebServiceRequest request, @Nonnull R wsResponse) {
        List<Extension> responseExtensions = request.getNonnullExtensions().stream()
                    .filter(ext -> !ext.isRequestType()) // response extensions only
                    .map(ext ->
                            new Extension(
                                    ext.idv,
                                    ext.isCritical(),
                                    ext.getNonnullParameters().stream()
                                            .map(param -> new Extension.Parameter(param.name, param.value))
                                            .collect(toList())))
                    .collect(toList());

        wsResponse
                .setExtensions(responseExtensions)
                .setHeader(new Header(
                                request.id,
                                isNullOrEmpty(request.responseTime))
                                        ? clock.localNow()
                                        : request.responseTime)
                .setResult(new ResultType(
                                convertResultCode(request.errorCode),
                                request.responseDescription));

        return wsResponse;
    }

    /**
     * Creates response from exception.
     *
     * Supports {@link SomeSpecificException}.
     */
    public @Nonnull <R extends ResultType> R toWsResponse(@Nonnull Throwable e, @Nonnull R wsResponse) {
        wsResponse
                .setHeader(new Header(0L, clock.localNow()))
                .setResult(new ResultType());

        if (e instanceof SomeSpecificException) {
            ResultType exceptionResult = ((SomeSpecificException) e).result;
            wsResponse.result
                        .setCode(exceptionResult.code)
                        .setDescription(exceptionResult.description);
        } else {
            wsResponse.result
                    .setCode(ERROR_SYSTEM_MALFUNCTION_999)
                    .setDescription(e.toString());
        }

        return wsResponse;
    }

    /*
    few additional util methods here
     */
}
