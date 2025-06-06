<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap -->
    <link href="<c:url value='/static/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/jumbotron.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/login.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/round.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/navbar.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/articoli.css'/>" rel="stylesheet">

    <title>${title}</title>

    <c:set var="req" value="${pageContext.request.contextPath}"/>
</head>