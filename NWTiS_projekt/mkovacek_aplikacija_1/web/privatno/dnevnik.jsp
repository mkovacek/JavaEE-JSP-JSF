<%-- 
    Document   : dnevnik
    Created on : Jun 6, 2015, 6:24:57 PM
    Author     : Matija
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.servletContext.contextPath}/css/displaytag.css" rel="stylesheet" type="text/css" />
        <title>Dnevnik</title>
    </head>
    <body>
        <a href="${pageContext.servletContext.contextPath}/Home">Izbornik</a><br/>
        <h1>Pregled dnevnika</h1>
        <p>Filtriranje podataka</p>
        <form method="POST" action="${pageContext.servletContext.contextPath}/FiltriranjeDnevnika">
            <label for="zahtjev">Zahtjev </label>
            <input id="zahtjev" name="zahtjev" /><br/><br/>
            <label for="ipAdresa">IP adresa </label>
            <input id="ipAdresa" name="ipAdresa" /><br/><br/>
            <label for="lozinka">Interval Od: </label>
            <input name="odDatum" type="datetime" id="odDatum"/>
            <label for="lozinka">Do: </label>
            <input name="doDatum" type="datetime" id="doDatum"/>
            <input type="submit" value="Filtriraj"/>
        </form>
        <br><br> 

        <display:table name="${dnevnik}" pagesize="${stranicenje}">
            <display:column property="korisnik"/>
            <display:column property="vrijeme" format="{0,date,dd.MM.yyyy HH.mm.ss}"  sortable="true" />
            <display:column property="zahtjev" sortable="true" />
            <display:column property="ipAdresa" sortable="true" />
        </display:table>
    </body>
</html>
