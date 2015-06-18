<%-- 
    Document   : home
    Created on : Jun 6, 2015, 6:09:03 PM
    Author     : Matija
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <h1>Izbornik</h1>
        <a href="${pageContext.servletContext.contextPath}/PregledFinancija">Pregled i ažuriranje financijskih sredstava</a><br/>
        <a href="${pageContext.servletContext.contextPath}/PregledTransakcija">Pregled transakcija</a><br/>
        <a href="${pageContext.servletContext.contextPath}/PregledDnevnika">Pregled dnevnika</a><br/>
        <a href="${pageContext.servletContext.contextPath}/OdjavaKorisnika">Odjava</a><br/>
    </body>
</html>
