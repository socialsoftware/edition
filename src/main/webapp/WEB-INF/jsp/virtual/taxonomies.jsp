<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<script type="text/javascript">
function validateForm() {
    var x = document.forms["createTaxonomy"]["name"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser dado um nome à taxonomia a criar");
        $("#myModal").modal('show');
        return false;
    } 
    x = document.forms["createTaxonomy"]["numTopics"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o número de tópicos a gerar");
        $("#myModal").modal('show');
        return false;
    } else if (isNaN(x) || x < 1) {
        $("#message").html("Deve ser indicado um número de tópicos positivo");
        $("#myModal").modal('show');
        return false;
    }
    x = document.forms["createTaxonomy"]["numWords"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o número de palavras a apresentar");
        $("#myModal").modal('show');
        return false;
    } else if (isNaN(x) || x < 1) {
        alert("Deve ser indicado um número de palavras positivo");
        return false;
    }
    x = document.forms["createTaxonomy"]["thresholdCategories"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o valor de corte de categorias");
        $("#myModal").modal('show');
        return false;
    } else if (isNaN(x) || x < 0 || x > 100) {
        $("#message").html("Deve ser indicado um valor de corte de categorias entre 0 e 100");
        $("#myModal").modal('show');
        return false;
    }
    x = document.forms["createTaxonomy"]["numIterations"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o número de iterações");
        $("#myModal").modal('show');
        return false;
    } else if (isNaN(x) || x < 1) {
        $("#message").html("Deve ser indicado um número de iterações positivo");
        $("#myModal").modal('show');
        return false;
    }
}
</script>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
    <div class="container">
        <h1 class="text-center">
            <spring:message code="general.taxonomies" />
            : ${virtualEdition.title} <br>
        </h1>
        <br>
        <div class="row col-md-10">
            <form class="form-inline" method="POST"
                action="${contextPath}/virtualeditions/restricted/regenerateCorpus">
                <input type="hidden" class="form-control"
                    name="externalId"
                    value="${virtualEdition.externalId}" />
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-file"></span>
                    <spring:message code="corpus.regenerate" />
                </button>
            </form>
        </div>
        <div class="row col-md-2">
            <form class="form-inline" method="GET"
                action="${contextPath}/virtualeditions">
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-th-list"></span>
                    <spring:message code="virtual.editions" />
                </button>
            </form>
        </div>
        <br /> <br /> <br />
        <div class="row col-md-12">
            <c:forEach var="error" items='${errors}'>
                <div class="row has-error">${error}</div>
            </c:forEach>
        </div>
        <div class="row col-md-12">
            <form name="createTaxonomy" class="form-inline"
                method="POST"
                action="/virtualeditions/restricted/taxonomy/createTopics"
                onsubmit="return validateForm()">
                <div class="form-group">
                    <input type="hidden" class="form-control"
                        name="externalId"
                        value="${virtualEdition.externalId}" />
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="name"
                        placeholder="<spring:message code="general.name" />">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="numTopics"
                        placeholder="<spring:message code="general.taxonomies.number.topics" />">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="numWords"
                        placeholder="<spring:message code="general.taxonomies.number.words" />">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="thresholdCategories"
                        placeholder="<spring:message code="general.taxonomies.threshold.categories" />">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control"
                        name="numIterations"
                        placeholder="<spring:message code="general.taxonomies.number.iterations" />">
                </div>
                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-plus"></span>
                    <spring:message code="general.create" />
                </button>
            </form>
        </div>
        <br />
        <div class="row col-md-12">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="general.name" /></th>
                        <th><spring:message
                                code="general.taxonomies.number.topics" /></th>
                        <th><spring:message code="general.delete" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="taxonomy"
                        items='${virtualEdition.getTaxonomiesSet()}'>
                        <tr>
                            <td><a
                                href="${contextPath}/virtualeditions/restricted/taxonomy/${taxonomy.getExternalId()}">${taxonomy.getName()}</a>
                                <c:if test="${!taxonomy.getAdHoc()}">(${taxonomy.getNumTopics()},${taxonomy.getNumWords()},${taxonomy.getThresholdCategories()},${taxonomy.getNumIterations()})</c:if>
                            </td>
                            <td>${taxonomy.getActiveCategorySet().size()}</td>
                            <td>
                                <form class="form-inline" method="POST"
                                    action="${contextPath}/virtualeditions/restricted/taxonomy/delete">
                                    <input type="hidden"
                                        name="virtualEditionExternalId"
                                        value="${taxonomy.getEdition().getExternalId()}" />
                                    <input type="hidden"
                                        name="taxonomyExternalId"
                                        value="${taxonomy.externalId}" />
                                    <button type="submit"
                                        class="btn btn-primary btn-sm">
                                        <span
                                            class="glyphicon glyphicon-remove"></span>
                                        <spring:message
                                            code="general.delete" />
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <!-- Modal HTML -->
    <div id="myModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Informação</h4>
                </div>
                <div class="modal-body">
                    <h3 id="message" />
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                        data-dismiss="modal">Fechar</button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
