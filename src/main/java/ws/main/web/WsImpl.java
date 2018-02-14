package ws.main.web;

import path.to.utils.mdc.AppNameMdcInterceptor;
import ws.api.*;
import ws.main.methods.FirstWebMethod;
import ws.main.methods.SecondWebMethod;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import static ws.api.EndpointInterface.SERVICE_NAME;
import static ws.api.EndpointInterface.SERVICE_PORT;
import static ws.api.EndpointInterface.WSDL_NAMESPACE;


@WebService(
        serviceName = SERVICE_NAME,
        portName    = SERVICE_PORT,
        targetNamespace = WSDL_NAMESPACE,
        endpointInterface = "ws.api.EndpointInterface",
        wsdlLocation = "WEB-INF/wsdl/WsdlName.wsdl"
)
@Interceptors(AppNameMdcInterceptor.class)
public class WsImpl implements EndpointInterface {

    @Inject
    private FirstWebMethod firstWebMethod;

    @Override
    public FirstResponseType checkRequest(FirstRequestType request) {
        return firstWebMethod.apply(request);
    }


    @Inject
    private SecondWebMethod secondWebMethod;

    @Override
    public SecondResponseType authenticateRequest(SecondRequestType request) {
        return secondWebMethod.apply(request);
    }
}
