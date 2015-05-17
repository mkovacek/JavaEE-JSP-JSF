<%-- 
    Document   : index
    Created on : May 11, 2015, 11:02:41 AM
    Author     : Matija
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>mkovacek_zadaca_3_1</title>
    </head>
    <body>
        <br><br>
        <form method="POST" action="${pageContext.servletContext.contextPath}/DodajAdresu">
            <label for="adresa">Adresa: </label>
            <input type="text" name="adresa" id="adresa" />
            <input type="submit" value="Dodaj adresu" name="DodajAdresu"/>
        </form>
        <br>
        <c:if test="${geoPrikaz}">
            <h3>Geo podaci za unesenu adresu: ${geoPodaci.getAdresa()}</h3>
            <label><b>Latitude: </b></label>${geoPodaci.getGeoloc().getLatitude()}<br>
            <label><b>Longitude: </b></label>${geoPodaci.getGeoloc().getLongitude()}<br>
            <br>
        </c:if>
        <form method="POST" action="${pageContext.servletContext.contextPath}/SpremiAdresu">
            <input type="submit"  value="Spremi adresu" name="SpremiAdresu"/>
        </form><br>
        <form method="POST" action="${pageContext.servletContext.contextPath}/PreuzmiMeteoPodatke">
            <input type="submit"  value="Preuzmi meteo podatke" name="PreuzmiMeteoPodatke"/>
        </form><br>
        <c:if test="${meteoPrikaz}">
            <h3>Meteo podaci za unesenu adresu: ${meteoAdresa} </h3>
            <label><b>Izlazak sunca: </b></label>${meteoPodaci.getSunRise()}<br>
            <label><b>Zalazak sunca: </b></label>${meteoPodaci.getSunSet()}<br>
            <label><b>Temperatura: </b></label>${meteoPodaci.getTemperatureValue()} ${meteoPodaci.getTemperatureUnit()}<br>
            <label><b>Naoblaka: </b></label>${meteoPodaci.getCloudsName()}<br>
            <label><b>Tlak zraka: </b></label>${meteoPodaci.getPressureValue()} ${meteoPodaci.getPressureUnit()}<br>
        </c:if>
    </body>
</html>
