package ws.api;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import static ws.api.EndpointInterface.SERVICE_NAME;
import static ws.api.EndpointInterface.WSDL_NAMESPACE;
import static ws.api.Names.SERVICE_NAME;
import static ws.api.Names.WSDL_NAMESPACE;

@WebService(name = SERVICE_NAME, targetNamespace = WSDL_NAMESPACE)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface EndpointInterface {

    String SERVICE_NAME   = "EndpointInterface";
    String SERVICE_PORT   = "EndpointInterfacePort";
    String WSDL_NAMESPACE = "urn:EndpointInterface-ws";

    @WebMethod(operationName = "CheckRequest_", action = WSDL_NAMESPACE + "/CheckRequest_")
    @WebResult(name = "FirstResponseType", targetNamespace = WSDL_NAMESPACE, partName = "response")
    FirstResponseType checkRequest(
            @WebParam(name = "FirstResponseType", targetNamespace = WSDL_NAMESPACE, partName = "request")
                    FirstRequestType request);

    @WebMethod(operationName = "AuthenticateRequest_", action = WSDL_NAMESPACE + "/AuthenticateRequest_")
    @WebResult(name = "SecondResponseType", targetNamespace = WSDL_NAMESPACE, partName = "response")
    SecondResponseType authenticateRequest(
            @WebParam(name = "SecondRequestType", targetNamespace = WSDL_NAMESPACE, partName = "request")
                    SecondRequestType request);

}
