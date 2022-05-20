package com.fcr.resource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fcr.negocio.GeradorRn;

// ./mvnw compile quarkus:dev
@RequestScoped
@Path("/gerador")
public class GeradorResource {

	GeradorRn geradorRn = new GeradorRn();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String obtemDados(@QueryParam("packageName") String packageName,
			@QueryParam("serviceName") String serviceName,
			@QueryParam("serviceParams") String serviceParams, @QueryParam("className") String className,
			@QueryParam("classFieldNames") String classFieldNames) {

		return geradorRn.obtemDados(packageName, serviceName, serviceParams, className, classFieldNames);
	}

	@GET
	@Path("/java-ts")
	@Produces(MediaType.TEXT_PLAIN)
	public String obtemModeloJavaTs(@QueryParam("className") String className, 
			@QueryParam("classFieldNames") String classFieldNames) {

		return geradorRn.obtemModeloJavaTs(className, classFieldNames);
	}
}
