//package it.jump3.config;
//
//import it.jump3.enumz.EnvironmentConstants;
//import it.jump3.util.HeaderData;
//import it.jump3.util.Utility;
//
//import javax.enterprise.context.RequestScoped;
//import javax.enterprise.inject.Produces;
//import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.core.Context;
//
//@RequestScoped
//public class Producers {
//
//    @Produces
//    HeaderData produceHeaderData(@Context HttpServletRequest request) {
//
//        HeaderData headerData = new HeaderData();
//        headerData.setIpClient(Utility.getIpClient(request));
//        headerData.setPort(request.getRemotePort());
//        headerData.setUsername(request.getHeader(EnvironmentConstants.Headers.HEADER_USERNAME));
//
//        return headerData;
//    }
//}
