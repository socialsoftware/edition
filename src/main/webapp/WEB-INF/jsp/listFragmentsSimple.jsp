<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="fragmentList" class="row-fluid">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><spring:message code="tableofcontents.title"/></th>
			</tr>
		<tbody>
			<c:forEach var="fragment" items='${fragments}'>
				<tr>
					<td><a
						href="${contextPath}/fragments/fragment/${fragment.externalId}">${fragment.title}</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>