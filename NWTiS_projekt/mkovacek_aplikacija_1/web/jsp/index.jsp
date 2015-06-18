<%-- 
    Document   : index
    Created on : Jun 6, 2015, 4:17:21 PM
    Author     : Matija
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Početna stranica</title>
    </head>
    <body>
        <h1>Početna</h1>
        <a href="${pageContext.servletContext.contextPath}/PrijavaKorisnika">Prijava</a><br/>
        <br/>
        <a href="${pageContext.servletContext.contextPath}/PregledFinancija">Pregled i ažuriranje financijskih sredstava</a><br/>
        <a href="${pageContext.servletContext.contextPath}/PregledTransakcija">Pregled transakcija</a><br/>
        <a href="${pageContext.servletContext.contextPath}/PregledDnevnika">Pregled dnevnika</a><br/>
    </body>
</html>

