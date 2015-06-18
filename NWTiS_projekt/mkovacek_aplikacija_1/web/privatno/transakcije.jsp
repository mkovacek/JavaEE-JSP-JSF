<%-- 
    Document   : transakcije
    Created on : Jun 6, 2015, 6:24:50 PM
    Author     : Matija
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.servletContext.contextPath}/css/displaytag.css" rel="stylesheet" type="text/css" />
        <title>Transakcije</title>
    </head>
    <body>
        <a href="${pageContext.servletContext.contextPath}/Home">Izbornik</a><br/>
        <h1>Pregled transakcija</h1>
        <display:table name="${transakcije}" pagesize="${stranicenje}">
            <display:column property="korisnik"/>
            <display:column property="vrijemeTransakcije" title="Vrijeme transakcije" format="{0,date,dd.MM.yyyy HH:mm:ss}"  sortable="true" />
            <display:column property="usluga" sortable="true" />
            <display:column property="status" sortable="true" />
            <display:column property="staroStanje" title="Staro stanje" sortable="true" />
            <display:column property="cijena" sortable="true" />
            <display:column property="novoStanje" title="Novo stanje" sortable="true" />
        </display:table>
    </body>
</html>
