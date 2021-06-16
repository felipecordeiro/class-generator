package com.fcr.negocio;

public class GeradorRn {
	StringBuilder sb;

	public String transform(String packageName, String serviceName, String serviceParams, String className,
			String stringFields) {
		String methodName = getMethodName(serviceName);
		String[] resourceParams = null;
		if (serviceParams != null) {
			resourceParams = getCammelCasePatternFromArray(serviceParams.toLowerCase().split(","));
		}
		String[] xmlFields = stringFields.toLowerCase().split(",");
		for (int i=0; i < xmlFields.length; i++){
			xmlFields[i] = xmlFields[i].trim();
		}
		String[] jsonFields = getCammelCasePatternFromArray(xmlFields);
		String result = "";

		result = createResourceMethod(packageName, serviceName, resourceParams, className, methodName)
				+ createXmlJsonTo(className, xmlFields, jsonFields) + createMainJsonTo(className)
				+ createMethodRn(packageName, className, methodName) + createMainXmlTo(className)
				+ createDaoMethod(serviceName, className, methodName);

		return result;
	}

	private String[] getCammelCasePatternFromArray(String[] xmlFields) {
		String[] jsonFields = new String[xmlFields.length];
		String[] field;
		for (int i = 0; i < xmlFields.length; i++) {
			field = xmlFields[i].split("_");
			for (int j = 0; j < field.length; j++) {
				if (j == 0) {
					jsonFields[i] = field[j];
				} else {
					String fieldFormatted = field[j].substring(0, 1).toUpperCase() + field[j].substring(1);
					jsonFields[i] += fieldFormatted;
				}
			}
		}
		return jsonFields;
	}

	private String getXmlPatternFromCammelCase(String valor) {
		String xmlString = "";
		char[] caractere = valor.toCharArray();
		for (int i = 0; i < caractere.length; i++) {
			if (i == 0 && Character.isUpperCase(caractere[i])) {
				xmlString += String.valueOf(caractere[i]).toLowerCase();
			} else if (Character.isUpperCase(caractere[i])) {
				xmlString += "_" + String.valueOf(caractere[i]).toLowerCase();
			} else {
				xmlString += String.valueOf(caractere[i]).toLowerCase();
			}
		}
		return xmlString;
	}

	private String getListaMainJsonTo(String className) {
		String lista = className.substring(0, 1).toLowerCase() + className.substring(1);
		return lista;
	}

	private String getMethodName(String serviceName) {
		String methodName = "";
		String prefixMethodName = serviceName.substring(3, 5).toLowerCase();
		if (prefixMethodName.equals("ob")) {
			methodName = "obtem";
		} else if (prefixMethodName.equals("at")) {
			methodName = "atualiza";
		} else if (prefixMethodName.equals("ex")) {
			methodName = "exclusao";
		} else {
			methodName = prefixMethodName;
		}
		methodName += getCammelCasePatternFromArray(serviceName.substring(5).toLowerCase().split(","))[0];
		return methodName;
	}

	private String createResourceMethod(String packageName, String serviceName, String[] resourceParams,
			String className, String methodName) {
		sb = new StringBuilder();

		sb.append("@GET");
		sb.append(System.lineSeparator());
		sb.append("@Path(\"/" + serviceName + "\")");
		sb.append(System.lineSeparator());
		sb.append("@ApiOperation(\"Descrição\")");
		sb.append(System.lineSeparator());
		sb.append("@ApiResponses({ @ApiResponse(code = 200, message = \"OK\", response = " + className
				+ "MainJsonTo.class),");
		sb.append(System.lineSeparator());
		sb.append("@ApiResponse(code = 401, message = \"UNAUTHORIZED\", response = HttpResponseTo.class),");
		sb.append(System.lineSeparator());
		sb.append("@ApiResponse(code = 400, message = \"BAD REQUEST\", response = HttpResponseTo.class),");
		sb.append(System.lineSeparator());
		sb.append("@ApiResponse(code = 500, message = \"INTERNAL SERVER ERROR\", response = HttpResponseTo.class) })");
		sb.append(System.lineSeparator());
		if (resourceParams != null && resourceParams.length > 0) {
			sb.append("public Response " + methodName + "(");
			sb.append(System.lineSeparator());
			for (int i = 0; i < resourceParams.length; i++) {
				if (i == 0) {
					sb.append("@QueryParam(\"" + resourceParams[i] + "\") String "+resourceParams[i]);
					sb.append(System.lineSeparator());
				}
				else {
					sb.append(", @QueryParam(\"" + resourceParams[i] + "\") String "+resourceParams[i]);
					sb.append(System.lineSeparator());
				}
			}
		} else {
			sb.append("public Response " + methodName + "(");
		}
		sb.append(") {");
		sb.append(System.lineSeparator());
		sb.append(className+"XmlJsonTo param = new "+className+"XmlJsonTo();");
		sb.append(System.lineSeparator());		
		if (resourceParams != null && resourceParams.length > 0) {
			for (int i = 0; i < resourceParams.length; i++) {
				String stringFormatada = resourceParams[i].substring(0, 1).toUpperCase() + resourceParams[i].substring(1);
				sb.append("param.set"+stringFormatada+"("+resourceParams[i]+");");
				sb.append(System.lineSeparator());
			}
		}
		sb.append(className + "MainJsonTo retorno = " + packageName + "Rn." + methodName + "(param);");
		sb.append(System.lineSeparator());
		sb.append("return Response.ok(retorno).build();");
		sb.append(System.lineSeparator());
		sb.append("}");

		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		return sb.toString();
	}

	private String createXmlJsonTo(String className, String[] xmlFields, String[] jsonFields) {
		sb = new StringBuilder();

		sb.append("import javax.xml.bind.annotation.XmlAccessType;");
		sb.append(System.lineSeparator());
		sb.append("import javax.xml.bind.annotation.XmlAccessorType;");
		sb.append(System.lineSeparator());
		sb.append("import javax.xml.bind.annotation.XmlAttribute;");
		sb.append(System.lineSeparator());
		sb.append("import javax.xml.bind.annotation.XmlRootElement;");
		sb.append(System.lineSeparator());
		sb.append("import com.fasterxml.jackson.annotation.JsonIgnoreProperties;");
		sb.append(System.lineSeparator());
		sb.append("import com.fasterxml.jackson.annotation.JsonProperty;");
		sb.append(System.lineSeparator());
		sb.append("import lombok.Data;");
		sb.append(System.lineSeparator());
		sb.append("import lombok.NoArgsConstructor;");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("@XmlRootElement");
		sb.append(System.lineSeparator());
		sb.append("@XmlAccessorType(XmlAccessType.FIELD)");
		sb.append(System.lineSeparator());
		sb.append("@NoArgsConstructor");
		sb.append(System.lineSeparator());
		sb.append("@Data");
		sb.append(System.lineSeparator());
		sb.append("@JsonIgnoreProperties(ignoreUnknown = true)");
		sb.append(System.lineSeparator());
		sb.append("public class " + className + "XmlJsonTo {");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		for (int i = 0; i < jsonFields.length; i++) {
			sb.append("@JsonProperty(\"" + jsonFields[i] + "\")");
			sb.append(System.lineSeparator());
			sb.append("@XmlAttribute(name = \"" + xmlFields[i] + "\")");
			sb.append(System.lineSeparator());
			sb.append("private String " + jsonFields[i] + ";");
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		sb.append(System.lineSeparator());
		sb.append("}");

		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		return sb.toString();
	}

	private String createMainJsonTo(String className) {
		sb = new StringBuilder();

		sb.append("import java.util.Collection;");
		sb.append(System.lineSeparator());
		sb.append("import com.usiminas.loci_oficinacilindros.core.xml.MensagemJsonTo;");
		sb.append(System.lineSeparator());
		sb.append("import lombok.AllArgsConstructor;");
		sb.append(System.lineSeparator());
		sb.append("import lombok.Data;");
		sb.append(System.lineSeparator());
		sb.append("import lombok.NoArgsConstructor;");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("@AllArgsConstructor");
		sb.append(System.lineSeparator());
		sb.append("@NoArgsConstructor");
		sb.append(System.lineSeparator());
		sb.append("@Data");
		sb.append(System.lineSeparator());
		sb.append("public class " + className + "MainJsonTo {");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("private Collection<" + className + "XmlJsonTo> " + getListaMainJsonTo(className) + ";");
		sb.append(System.lineSeparator());
		sb.append("private Collection<MensagemJsonTo> mensagens;");
		sb.append(System.lineSeparator());
		sb.append("}");

		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());

		return sb.toString();
	}

	private String createMethodRn(String packageName, String className, String methodName) {
		sb = new StringBuilder();

		sb.append("public " + className + "MainJsonTo " + methodName + "(" + className + "XmlJsonTo param) {");
		sb.append(System.lineSeparator());
		sb.append(className + "MainXmlTo main = new " + className + "MainXmlTo(");
		sb.append(System.lineSeparator());
		sb.append("new " + className + "MainXmlTo." + className + "ListXmlTo(param));");
		sb.append(System.lineSeparator());
		sb.append("return "+packageName+"Dao." + methodName + "(main);");
		sb.append(System.lineSeparator());
		sb.append("}");

		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());

		return sb.toString();
	}

	private String createMainXmlTo(String className) {
		sb = new StringBuilder();

		sb.append("import java.io.Serializable;");
		sb.append(System.lineSeparator());
		sb.append("import java.util.ArrayList;");
		sb.append(System.lineSeparator());
		sb.append("import java.util.Collection;");
		sb.append(System.lineSeparator());
		sb.append("import java.util.List;");
		sb.append(System.lineSeparator());
		sb.append("import javax.xml.bind.annotation.XmlAccessType;");
		sb.append(System.lineSeparator());
		sb.append("import javax.xml.bind.annotation.XmlAccessorType;");
		sb.append(System.lineSeparator());
		sb.append("import javax.xml.bind.annotation.XmlElement;");
		sb.append(System.lineSeparator());
		sb.append("import javax.xml.bind.annotation.XmlRootElement;");
		sb.append(System.lineSeparator());
		sb.append("import com.usiminas.loci_oficinacilindros.core.xml.MensagemXmlTo;");
		sb.append(System.lineSeparator());
		sb.append("import lombok.Data;");
		sb.append("import lombok.NoArgsConstructor;");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("@XmlRootElement(name = \"main\")");
		sb.append(System.lineSeparator());
		sb.append("@XmlAccessorType(XmlAccessType.FIELD)");
		sb.append(System.lineSeparator());
		sb.append("@NoArgsConstructor");
		sb.append(System.lineSeparator());
		sb.append("@Data");
		sb.append(System.lineSeparator());
		sb.append("public class " + className + "MainXmlTo {");
		sb.append(System.lineSeparator());
		sb.append("private transient List<MensagemXmlTo> mensagens = new ArrayList<>();");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("@XmlElement(name = \"" + getXmlPatternFromCammelCase(className) + "\")");
		sb.append(System.lineSeparator());
		sb.append("private " + className + "ListXmlTo list;");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("public " + className + "MainXmlTo(" + className + "ListXmlTo objList) {");
		sb.append(System.lineSeparator());
		sb.append("this.list = objList;");
		sb.append(System.lineSeparator());
		sb.append("}");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("@XmlRootElement(name = \"" + getXmlPatternFromCammelCase(className) + "\")");
		sb.append(System.lineSeparator());
		sb.append("@XmlAccessorType(XmlAccessType.FIELD)");
		sb.append(System.lineSeparator());
		sb.append("@NoArgsConstructor");
		sb.append(System.lineSeparator());
		sb.append("@Data");
		sb.append(System.lineSeparator());
		sb.append("public static class " + className + "ListXmlTo implements Serializable {");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("private static final long serialVersionUID = 1L;");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("@XmlElement(name = \"row\")");
		sb.append(System.lineSeparator());
		sb.append("private List<" + className + "XmlJsonTo> objetos;");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("public " + className + "ListXmlTo(" + className + "XmlJsonTo objeto) {");
		sb.append(System.lineSeparator());
		sb.append("this.objetos = new ArrayList<>();");
		sb.append(System.lineSeparator());
		sb.append("this.objetos.add(objeto);");
		sb.append(System.lineSeparator());
		sb.append("}");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("public " + className + "ListXmlTo(Collection<" + className + "XmlJsonTo> objetos) {");
		sb.append(System.lineSeparator());
		sb.append("this.objetos = new ArrayList<>();");
		sb.append(System.lineSeparator());
		sb.append("if (objetos != null)");
		sb.append(System.lineSeparator());
		sb.append("this.objetos.addAll(objetos);");
		sb.append(System.lineSeparator());
		sb.append("}");
		sb.append(System.lineSeparator());
		sb.append("}");
		sb.append(System.lineSeparator());
		sb.append("}");

		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());

		return sb.toString();
	}

	private String createDaoMethod(String serviceName, String className, String methodName) {
		sb = new StringBuilder();
		
		sb.append("public "+className+"MainJsonTo "+methodName+"("+className+"MainXmlTo filtro) {");
		sb.append(System.lineSeparator());
		sb.append("try {");
		sb.append(System.lineSeparator());
		sb.append("JAXBContext contextMain = getJaxbContext("+className+"MainXmlTo.class);");
		sb.append(System.lineSeparator());
		sb.append("String servico = \""+serviceName+"\";");
		sb.append(System.lineSeparator());
		sb.append("PlSqlXmlConfig procedure = retornaProcedureExecutada(contextMain, packageName, servico, filtro);");
		sb.append(System.lineSeparator());
		sb.append(className+"MainXmlTo main = jaxbExecutor.unmarshal(procedure.getRetornoClob(PR_XML_SAIDA), contextMain);");
		sb.append(System.lineSeparator());
		sb.append("if (main == null)");
		sb.append(System.lineSeparator());
		sb.append("main = new "+className+"MainXmlTo();");
		sb.append(System.lineSeparator());
		sb.append("main.setMensagens(obterMensagens(procedure));");
		sb.append(System.lineSeparator());
		sb.append("Collection<"+className+"XmlJsonTo> collection = null;");
		sb.append(System.lineSeparator());
		sb.append("if (main.getList() != null) {");
		sb.append(System.lineSeparator());
		sb.append("collection = modelMapper.map(main.getList().getObjetos(), TYPE_"+getXmlPatternFromCammelCase(className).toUpperCase()+");");
		sb.append(System.lineSeparator());
		sb.append("}");
		sb.append(System.lineSeparator());
		sb.append("return new "+className+"MainJsonTo (collection, modelMapper.map(main.getMensagens(), TYPE_MENSAGEM));");
		sb.append(System.lineSeparator());
		sb.append("} catch (Exception e) {");
		sb.append(System.lineSeparator());
		sb.append("throw new BancoDeDadosException(e.getMessage(), e);");
		sb.append(System.lineSeparator());
		sb.append("}");
		sb.append(System.lineSeparator());
		sb.append("}");
		
		return sb.toString();
	}
}
