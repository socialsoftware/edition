<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

<div class="row-fluid">

	<c:forEach var="fragInter" items='${fragment.sortedInterps}'>
		<c:if test="${fragInter.sourceType=='EDITORIAL'}"> Edição: 
			<a
				href="${contextPath}/edition/internalid/${fragInter.edition.externalId}">
				${fragInter.edition.editor}</a>
			<br>
			<div class="text-center">
				<table>
					<tr>
						<td><a class="btn btn-mini"
							href="${contextPath}/fragments/fragment/interpretation/prev/number/${fragInter.externalId}"><i
								class="icon-backward"></i></a></td>
						<td><c:choose>
								<c:when test="${fragInter.number !=0}">${fragInter.number}</c:when>
								<c:otherwise>${fragInter.page}</c:otherwise>
							</c:choose></td>
						<td><a class="btn btn-mini"
							href="${contextPath}/fragments/fragment/interpretation/next/number/${fragInter.externalId}"><i
								class="icon-forward"></i></a></td>
					</tr>
					<tr>
						<td><a class="btn btn-mini"
							href="${contextPath}/fragments/fragment/interpretation/prev/heteronym/${fragInter.externalId}"><i
								class="icon-backward"></i></a></td>
						<td><a
							href="${contextPath}/edition/internalid/heteronym/${fragInter.edition.externalId}/${fragInter.heteronym.externalId}">
								${fragInter.heteronym.name} </a></td>
						<td><a class="btn btn-mini"
							href="${contextPath}/fragments/fragment/interpretation/next/heteronym/${fragInter.externalId}"><i
								class="icon-forward"></i></a></td>
					</tr>
				</table>
				<br>
			</div>
		</c:if>
	</c:forEach>

</div>

