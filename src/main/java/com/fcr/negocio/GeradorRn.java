package com.fcr.negocio;

public class GeradorRn {
    StringBuilder sb;

    public String transform(String className, String stringFields) {
        String[] xmlFields = stringFields.toLowerCase().split(",");
        String[] jsonFields = getCammelCasePattern(xmlFields);
        String result = "";
        // for (String string : jsonFields) {
        //     sb.append(string + ",");
        // }
        // result = sb.toString();

        result = createXmlJsonTo(className, xmlFields, jsonFields) + 
        createMainJsonTo(className) + createMethodRn(className) + createMainXmlTo(className);
        return result;
    }

    private String[] getCammelCasePattern(String[] xmlFields) {
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
        sb.append("private Collection<" + className + "XmlJsonTo> lista;");
        sb.append(System.lineSeparator());
        sb.append("private Collection<MensagemJsonTo> mensagens;");
        sb.append(System.lineSeparator());
        sb.append("}");

        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        
        return sb.toString();
    }

    private String createMethodRn(String className){
        sb = new StringBuilder();

        sb.append("public "+ className +"MainJsonTo buscar"+className+"("+className+"XmlJsonTo param) {");
        sb.append(System.lineSeparator());
        sb.append(className+"MainXmlTo main = new "+className+"MainXmlTo(");
        sb.append(System.lineSeparator());
        sb.append("new "+className+"MainXmlTo."+className+"ListXmlTo(param));");
        sb.append(System.lineSeparator());
        sb.append("return laminadorDao.buscar"+className+"(main);");
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
        sb.append("public class "+className+"MainXmlTo {");
        sb.append(System.lineSeparator());
        sb.append("private transient List<MensagemXmlTo> mensagens = new ArrayList<>();");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("@XmlElement(name = )");
        sb.append(System.lineSeparator());
        sb.append("private "+className+"ListXmlTo list;");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("public "+className+"MainXmlTo("+className+"ListXmlTo objList) {");
        sb.append(System.lineSeparator());
        sb.append("this.list = objList;");
        sb.append(System.lineSeparator());
        sb.append("}");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("@XmlRootElement(name = )");
        sb.append(System.lineSeparator());
        sb.append("@XmlAccessorType(XmlAccessType.FIELD)");
        sb.append(System.lineSeparator());
        sb.append("@NoArgsConstructor");
        sb.append(System.lineSeparator());
        sb.append("@Data");
        sb.append(System.lineSeparator());
        sb.append("public static class "+className+"ListXmlTo implements Serializable {");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("private static final long serialVersionUID = 1L;");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("@XmlElement(name = \"row\")");
        sb.append(System.lineSeparator());
        sb.append("private List<"+className+"XmlJsonTo> objetos;");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("public "+className+"ListXmlTo("+className+"XmlJsonTo objeto) {");
        sb.append(System.lineSeparator());
        sb.append("this.objetos = new ArrayList<>();");
        sb.append(System.lineSeparator());
        sb.append("this.objetos.add(objeto);");
        sb.append(System.lineSeparator());
        sb.append("}");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("public "+className+"ListXmlTo(Collection<"+className+"XmlJsonTo> objetos) {");
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

    private String createDao() {

        return "";
    }
}
