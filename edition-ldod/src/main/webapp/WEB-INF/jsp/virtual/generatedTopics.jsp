<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<div id="topics" class="row">
	<c:forEach var="categoryError" items='${categoryErrors}'>
		<div class="row has-error">${categoryError}</div>
	</c:forEach>
	<c:if test="${topicList != null}">
		<div class="row">
			<div class="col-md-1"></div>
			<div class="col-md-10">
				<form:form
					action="/virtualeditions/restricted/${virtualEdition.getExternalId()}/taxonomy/createTopics"
					method="POST" modelAttribute="topicList">
					<input type="hidden" name="_csrf" value="${_csrf.token}" />
					<form:input type="hidden" name="username" path="username"
						value="${topicList.getUsername()}" />
					<form:input type="hidden" name="taxonomyExternalId"
						path="taxonomyExternalId"
						value="${topicList.getTaxonomyExternalId()}" />

					<table class="table table-condensed">
						<thead>
							<tr>
								<th><spring:message code="topics.topic" /></th>
								<th><spring:message code="fragments" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="topic" items="${topicList.getTopics()}"
								varStatus="statusOne">
								<tr>
									<td><form:input type="hidden" name="name"
											path="topics[${statusOne.index}].name"
											value="${topic.getName()}" /> <strong>${topic.getName()}</strong></td>
									<td><c:forEach var="fragment" items="${topic.getInters()}"
											varStatus="statusTwo">
											<form:input type="hidden" name="fragExternalId"
												path="topics[${statusOne.index}].inters[${statusTwo.index}].externalId"
												value="${fragment.getExternalId()}" />
											<form:input type="hidden" name="fragPercentage"
												path="topics[${statusOne.index}].inters[${statusTwo.index}].percentage"
												value="${fragment.getPercentage()}" />
											<em>${fragment.getTitle()}</em> (${fragment.getPercentage()})
							</c:forEach></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<c:if test="${!topicList.getTopics().isEmpty()}">
						<div class="text-center">
							<button type="submit" class="btn btn-primary">
								<spring:message code="general.add" />
							</button>
						</div>
					</c:if>
				</form:form>
			</div>
		</div>
	</c:if>
</div>
