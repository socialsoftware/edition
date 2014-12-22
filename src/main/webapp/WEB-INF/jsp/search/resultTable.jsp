<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- class="table table-hover table-condensed" -->
<div>
	<h1>Included</h1>
	<table border="1" class="result-table">
		<thead>
			<tr>
				<td><spring:message code="fragment"/> (${fragCount})</td>
				<td><spring:message code="interpretations"/> (${interCount})</td>

				<%-- 			<c:forEach items="${search}" var="optionTitle"> --%>
				<%-- 				<td>${ optionTitle.toString()}</td> --%>
				<%-- 			</c:forEach> --%>

				<c:forEach begin="1" end="${searchLenght}" var="val">
					<td>C${val}</td>
				</c:forEach>
				<c:if test="${showSource}">
					<td><spring:message code="search.source"/></td>
				</c:if>
				<c:if test="${showSourceType}">
					<td><spring:message code="authorial.source"/></td>
				</c:if>
				<c:if test="${showLdoD}">
					<td><spring:message code="general.LdoDLabel"/>LdoD Mark</td>
				</c:if>
				<c:if test="${showPubPlace}">
					<td><spring:message code="general.published"/></td>
				</c:if>
				<c:if test="${showEdition}">
					<td><spring:message code="navigation.edition"/></td>
				</c:if>
				<c:if test="${showHeteronym}">
					<td><spring:message code="general.heteronym"/></td>
				</c:if>
				<c:if test="${showDate}">
					<td><spring:message code="general.date"/></td>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${results}" var="fragmentEntry">
				<c:forEach items="${ fragmentEntry.value }" var="fragInterEntry">
					<c:choose>
						<c:when test="${ fragInterEntry.value.size()>0 }">
							<tr>
								<td><a
									href="/fragments/fragment/${fragmentEntry.key.getExternalId()}">${fragmentEntry.key.getTitle()}</a></td>


								<c:choose>
									<c:when
										test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") && 
										fragInterEntry.key.getSource().getType() == 'MANUSCRIPT'}">
										<td><a
											href="/fragments/fragment/inter/${fragInterEntry.key.getExternalId()}">${fragInterEntry.key.getShortName()}</a></td>
									</c:when>
									<c:otherwise>
										<td><a
											href="/fragments/fragment/inter/${fragInterEntry.key.getExternalId()}">${fragInterEntry.key.getTitle()}</a></td>
									</c:otherwise>
								</c:choose>




								<c:forEach items="${search }" var="searchOption">
									<%
										String result = "";
									%>
									<c:forEach items="${ fragInterEntry.value }" var="option">
										<c:if test="${option == searchOption}">
											<%
												result = "X";
											%>
										</c:if>
									</c:forEach>
									<td><%=result%></td>
								</c:forEach>

								<c:choose>
									<c:when test="${showSource}">
										<td>${fragInterEntry.key.getSourceType()}</td>
									</c:when>
								</c:choose>

								<c:if test="${showSourceType}">
									<c:choose>
										<c:when
											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") &&
												fragInterEntry.key.getSource().getType() == 'MANUSCRIPT' &&
												fragInterEntry.key.getSource().getNotes().toLowerCase().contains(\"datil\") }">
											<td><spring:message code="general.typescript"/></td>
										</c:when>
										<c:when
											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") &&
												fragInterEntry.key.getSource().getType() == 'MANUSCRIPT' &&
												fragInterEntry.key.getSource().getNotes().toLowerCase().contains(\"manus\")}">
											<td><spring:message code="general.manuscript"/></td>
										</c:when>
										<c:otherwise>
											<td></td>
										</c:otherwise>
									</c:choose>
								</c:if>



								<c:if test="${showLdoD}">
									<c:choose>
										<c:when
											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") &&
												fragInterEntry.key.getSource().getType() == 'MANUSCRIPT' }">
											<td>${fragInterEntry.key.getSource().getHasLdoDLabel()}</td>
										</c:when>
										<c:otherwise>
											<td></td>
										</c:otherwise>
									</c:choose>
								</c:if>

								<c:if test="${showPubPlace}">
									<c:choose>
										<c:when
											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") &&
												fragInterEntry.key.getSource().getType() == 'PRINTED' }">
											<td>${fragInterEntry.key.getSource().getTitle()}</td>
										</c:when>
										<c:otherwise>
											<td></td>
										</c:otherwise>
									</c:choose>
								</c:if>

								<c:if test="${showEdition}">
									<c:choose>
										<c:when
											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"ExpertEditionInter\")}">
											<td>${fragInterEntry.key.getEdition().getEditor()}</td>
										</c:when>
										<c:otherwise>
											<td></td>
										</c:otherwise>
									</c:choose>
								</c:if>


								<c:choose>
									<c:when test="${showHeteronym}">
										<td>${fragInterEntry.key.getHeteronym().getName()}</td>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>


								<c:if test="${showDate}">
									<c:choose>
										<c:when
											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") &&
										 fragInterEntry.key.getSource().getType() == 'MANUSCRIPT' }">
											<td>${fragInterEntry.key.getSource().getDate()}</td>
										</c:when>
										<c:when
											test="${showDate && fragInterEntry.key.getClass().getSimpleName().equals(\"ExpertEditionInter\")}">
											<td>${fragInterEntry.key.getDate()}</td>
										</c:when>
										<c:otherwise>
											<td></td>
										</c:otherwise>
									</c:choose>
								</c:if>

								<%-- 					<c:if test="${showSource}"> --%>
								<%-- 						<td>${fragInterEntry.key.getSourceType()}</td> --%>
								<%-- 					</c:if> --%>
								<%-- 					<c:if test="${showLdoD }"> --%>
								<!-- 						<td>fragInterEntry.key.getHeteronym()</td> -->
								<%-- 					</c:if> --%>
								<%-- 					<c:if test="${showPubPlace}"> --%>
								<%-- 						<td>${fragInterEntry.key.getSource().getTitle()}</td> --%>
								<%-- 					</c:if> --%>
								<%-- 					<c:if test="${showEdition && fragInterEntry.key.getClass().getSimpleName().equals(\"ExpertEditionInter\")}"> --%>
								<%-- 						<td>${fragInterEntry.key.getEdition().getEditor()}</td> --%>
								<%-- 					</c:if> --%>
								<%-- 					<c:if test="${showHeteronym}"> --%>
								<%--  						<td>${fragInterEntry.key.getHeteronym().getName()}</td>  --%>
								<%-- 					</c:if> --%>
								<%-- 					<c:if test="${showDate && fragInterEntry.key.getClass().getSimpleName().equals(\"ExpertEditionInter\")}"> --%>
								<%-- 						<td>${fragInterEntry.key.getDate()}</td> --%>
								<%-- 					</c:if> --%>
								<%-- 					<c:if test="${showDate && fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\")}"> --%>
								<%-- 						<td>${fragInterEntry.key.getSource().getDate()}</td> --%>
								<%-- 					</c:if> --%>
							</tr>
						</c:when>
					</c:choose>

				</c:forEach>
			</c:forEach>


		</tbody>
	</table>
</div>

<!-- <div> -->
<!-- 	<h1>Not included</h1> -->
<!-- 	<table border="1" class="result-table"> -->
<!-- 		<thead> -->
<!-- 			<tr> -->
<%-- 				<td>Fragment (${fragCountNotAdded})</td> --%>
<%-- 				<td>Inter (${interCountNotAdded})</td> --%>

<%-- 							<c:forEach items="${search}" var="optionTitle"> --%>
<%-- 								<td>${ optionTitle.toString()}</td> --%>
<%-- 							</c:forEach> --%>

<%-- 				<c:forEach begin="1" end="${searchLenght}" var="val"> --%>
<%-- 					<td>C${val}</td> --%>
<%-- 				</c:forEach> --%>
<%-- 				<c:if test="${showSource}"> --%>
<!-- 					<td>Fonte</td> -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${showSourceType}"> --%>
<!-- 					<td>Tipo da Fonte</td> -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${showLdoD}"> --%>
<!-- 					<td>LdoD Mark</td> -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${showPubPlace}"> --%>
<!-- 					<td>Publication</td> -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${showEdition}"> --%>
<!-- 					<td>Edition</td> -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${showHeteronym}"> --%>
<!-- 					<td>Heteronym</td> -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${showDate}"> --%>
<!-- 					<td>Date</td> -->
<%-- 				</c:if> --%>
<!-- 			</tr> -->
<!-- 		</thead> -->
<!-- 		<tbody> -->
<%-- 			<c:forEach items="${results}" var="fragmentEntry"> --%>
<%-- 				<c:forEach items="${ fragmentEntry.value }" var="fragInterEntry"> --%>
<%-- 					<c:choose> --%>
<%-- 						<c:when test="${ fragInterEntry.value.size()==0 }"> --%>
<!-- 							<tr> -->
<!-- 								<td><a -->
<%-- 									href="/fragments/fragment/${fragmentEntry.key.getExternalId()}">${fragmentEntry.key.getTitle()}</a></td> --%>


<%-- 								<c:choose> --%>
<%-- 									<c:when --%>
<%-- 										test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") &&  --%>
<%-- 										fragInterEntry.key.getSource().getType() == 'MANUSCRIPT'}"> --%>
<!-- 										<td><a -->
<%-- 											href="/fragments/fragment/inter/${fragInterEntry.key.getExternalId()}">${fragInterEntry.key.getShortName()}</a></td> --%>
<%-- 									</c:when> --%>
<%-- 									<c:otherwise> --%>
<!-- 										<td><a -->
<%-- 											href="/fragments/fragment/inter/${fragInterEntry.key.getExternalId()}">${fragInterEntry.key.getTitle()}</a></td> --%>
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>




<%-- 								<c:forEach items="${search }" var="searchOption"> --%>
<%-- 									<% --%>
<%--										String result = ""; --%>
<%-- 									%> --%>
<%-- 									<c:forEach items="${ fragInterEntry.value }" var="option"> --%>
<%-- 										<c:if test="${option == searchOption}"> --%>
<%-- 											<% --%>
<%-- 												result = "X"; --%>
<%-- 											%> --%>
<%-- 										</c:if> --%>
<%-- 									</c:forEach> --%>
<%-- 									<td><%=result%></td> --%>
<%-- 								</c:forEach> --%>
<!-- 								<td>aaaa<td> -->

<%-- 								<c:choose> --%>
<%-- 									<c:when test="${showSource}"> --%>
<%-- 										<td>${fragInterEntry.key.getSourceType()}</td> --%>
<%-- 									</c:when> --%>
<%-- 								</c:choose> --%>

<%-- 								<c:if test="${showSourceType}"> --%>
<%-- 									<c:choose> --%>
<%-- 										<c:when --%>
<%-- 											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") && --%>
<%-- 												fragInterEntry.key.getSource().getType() == 'MANUSCRIPT' && --%>
<%-- 												fragInterEntry.key.getSource().getNotes().toLowerCase().contains(\"datil\") }"> --%>
<!-- 											<td>TYPESCRIPT</td> -->
<%-- 										</c:when> --%>
<%-- 										<c:when --%>
<%-- 											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") && --%>
<%-- 												fragInterEntry.key.getSource().getType() == 'MANUSCRIPT' && --%>
<%-- 												fragInterEntry.key.getSource().getNotes().toLowerCase().contains(\"manus\")}"> --%>
<!-- 											<td>MANUSCRIPT</td> -->
<%-- 										</c:when> --%>
<%-- 										<c:otherwise> --%>
<!-- 											<td></td> -->
<%-- 										</c:otherwise> --%>
<%-- 									</c:choose> --%>
<%-- 								</c:if> --%>


<%-- 								<c:if test="${showLdoD}"> --%>
<%-- 									<c:choose> --%>
<%-- 										<c:when --%>
<%-- 											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") && --%>
<%-- 												fragInterEntry.key.getSource().getType() == 'MANUSCRIPT' }"> --%>
<%-- 											<td>${fragInterEntry.key.getSource().getHasLdoDLabel()}</td> --%>
<%-- 										</c:when> --%>
<%-- 										<c:otherwise> --%>
<!-- 											<td></td> -->
<%-- 										</c:otherwise> --%>
<%-- 									</c:choose> --%>
<%-- 								</c:if> --%>

<%-- 								<c:if test="${showPubPlace}"> --%>
<%-- 									<c:choose> --%>
<%-- 										<c:when --%>
<%-- 											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") && --%>
<%-- 												fragInterEntry.key.getSource().getType() == 'PRINTED' }"> --%>
<%-- 											<td>${fragInterEntry.key.getSource().getTitle()}</td> --%>
<%-- 										</c:when> --%>
<%-- 										<c:otherwise> --%>
<!-- 											<td></td> -->
<%-- 										</c:otherwise> --%>
<%-- 									</c:choose> --%>
<%-- 								</c:if> --%>

<%-- 								<c:if test="${showEdition}"> --%>
<%-- 									<c:choose> --%>
<%-- 										<c:when --%>
<%-- 											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"ExpertEditionInter\")}"> --%>
<%-- 											<td>${fragInterEntry.key.getEdition().getEditor()}</td> --%>
<%-- 										</c:when> --%>
<%-- 										<c:otherwise> --%>
<!-- 											<td></td> -->
<%-- 										</c:otherwise> --%>
<%-- 									</c:choose> --%>
<%-- 								</c:if> --%>


<%-- 								<c:choose> --%>
<%-- 									<c:when test="${showHeteronym}"> --%>
<%-- 										<td>${fragInterEntry.key.getHeteronym().getName()}</td> --%>
<%-- 									</c:when> --%>
<%-- 									<c:otherwise> --%>
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>


<%-- 								<c:if test="${showDate}"> --%>
<%-- 									<c:choose> --%>
<%-- 										<c:when --%>
<%-- 											test="${ fragInterEntry.key.getClass().getSimpleName().equals(\"SourceInter\") && --%>
<%-- 										 fragInterEntry.key.getSource().getType() == 'MANUSCRIPT' }"> --%>
<%-- 											<td>${fragInterEntry.key.getSource().getDate()}</td> --%>
<%-- 										</c:when> --%>
<%-- 										<c:when --%>
<%-- 											test="${showDate && fragInterEntry.key.getClass().getSimpleName().equals(\"ExpertEditionInter\")}"> --%>
<%-- 											<td>${fragInterEntry.key.getDate()}</td> --%>
<%-- 										</c:when> --%>
<%-- 										<c:otherwise> --%>
<!-- 											<td></td> -->
<%-- 										</c:otherwise> --%>
<%-- 									</c:choose> --%>
<%-- 								</c:if> --%>
<!-- 							</tr> -->
<%-- 						</c:when> --%>
<%-- 					</c:choose> --%>
<%-- 				</c:forEach> --%>
<%-- 			</c:forEach> --%>
<!-- 		</tbody> -->
<!-- 	</table> -->
<!-- </div> -->