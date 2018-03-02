<!-- main container -->
<div class="main-content">
<script type="text/javascript">

  function openRecomModal() {
    $('#recommendationModal').modal()
  }

  function resetPrevRecom() {
    $.get("${contextPath}/reading/inter/prev/recom/reset");
    $('#recommendationModal').modal('hide');
    location.reload();
  }

  function changeWeight(type, value) {
    $.post('${contextPath}/reading/weight', {
      'type' : type,
      'value' : value
    }, function(result) { 
     // alert(result);
    });
  }

  function reload() {
    location.reload();
  }

$(window).on("load resize",function(e){




  /* Página Leitura */
  // set the height of columns depending on screen size
  $(".reading__text").css("height", "auto");
  
  var height = $(".reading-grid").height();

  if( $(window).width() >= 768 ){
    $(".reading__column").css("height", height);
    $(".reading__column--open").css("height", height);
    $(".reading__text").css("height", height);
  }else{
    /*
    $(".reading__column").css("height", "8.33333%");
    $(".reading__column--open").css("height", "8.33333%");
    $(".reading__text").css("height", height+50);
    */
    $(".reading__text").prependTo('.reading-grid');
    $(".reading__column--open").prependTo('.reading-grid');
  }
  
});
</script>


<c:if test="${inter != null}">
  <c:set var="fragment" value="${inter.getFragment()}" />
</c:if>


<!--
${inter.getEdition().getAcronym()}
 ${expertEdition.getAcronym()}-->

<div class="row reading-grid">


<c:choose>
          <c:when test="${fragment == null}">
          


           <div class="reading__text col-xs-12 col-sm-7 no-pad style-point reading-book-title">
            

            <c:choose>
            <c:when test="${pageContext.response.locale.language.equalsIgnoreCase('pt')}">
              <h1>Livro do Desassossego de Fernando Pessoa</h1>
            </c:when>
            <c:when test="${pageContext.response.locale.language.equalsIgnoreCase('en')}">
             <h1>Book of Disquiet by Fernando Pessoa</h1>
            </c:when>  
            <c:when test="${pageContext.response.locale.language.equalsIgnoreCase('es')}">
              <h1>Libro del Desasosiego de Fernando Pessoa</h1>
            </c:when>   
            </c:choose>
          </div>

          </c:when>
</c:choose>


<c:forEach var="expertEdition" items='${ldoD.sortedExpertEdition}'>



   
<c:choose>
    <c:when test="${inter.getEdition().getAcronym() == expertEdition.getAcronym()}">
      <div class="reading__column--open col-xs-12 col-sm-1 no-pad">
    </c:when>
    <c:otherwise>
      <div class="reading__column col-xs-12 col-sm-1 no-pad">
    </c:otherwise>
</c:choose>

    
          <h4>
          <a href="${contextPath}/reading/edition/${expertEdition.getAcronym()}/start/">${expertEdition.editor}</a>
          <!--${expertEdition.editor}-->
          </h4>
          <c:choose>
          <c:when test="${fragment != null}">
          <br>
          </c:when>
          </c:choose>

          <c:choose>
          <c:when test="${fragment == null}">
            <a
              href="${contextPath}/reading/edition/${expertEdition.getAcronym()}/start/"><%-- <spring:message
                code="general.reading.start" /> --%> <img src="/resources/img/graphics/arrow_right.png"></a>
          </c:when>

          <c:otherwise>

            <!-- ###################### interpretations ###################### -->
            <c:forEach var="expertEditionInter"
              items="${expertEdition.getSortedInter4Frag(fragment)}">
              
                <!-- ###################### interpretations desktop ###################### -->
                <div class="hidden-xs" style="margin-bottom:25px">
                <a
                  href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${expertEditionInter.urlId}"><!--${expertEditionInter.getEdition().getAcronym()}-->
                  <h2>${expertEditionInter.number}</h2></a>


                <div class="arrows">

                <a href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${expertEditionInter.urlId}/prev">
                 <img src="/resources/img/graphics/arrow_left.png"></a>

                <a
                  href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${expertEditionInter.urlId}/next">
                 <img src="/resources/img/graphics/arrow_right.png"></a>
                 </div>
                </div>

                <!-- ###################### interpretations mobile ###################### -->
                <div class="visible-xs-block">
                <a
                  href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${expertEditionInter.urlId}"><!--${expertEditionInter.getEdition().getAcronym()}-->
                  <h2>${expertEditionInter.number}</h2></a>

                 <a href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${expertEditionInter.urlId}/next">
                 <img src="/resources/img/graphics/arrow_right.png"></a>

                <a href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${expertEditionInter.urlId}/prev">
                 <img src="/resources/img/graphics/arrow_left.png"></a>

               
               
                </div>

               
                 
            </c:forEach>
          </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${inter.getEdition().getAcronym() == expertEdition.getAcronym()}">
      <div class="reading__text col-xs-12 col-sm-7 no-pad style-point">
      <h1><a href="${contextPath}/fragments/fragment/${inter.getFragment().getXmlId()}/inter/${inter.getUrlId()}">${inter.title}</a></h1>
      <br><br>
      <p>
       ${writer.getTranscription()}
      </p>
    </div>
    </c:if>

</c:forEach>


<!-- ###################### RECOMMENDATION ###################### -->

<div class="reading__column col-xs-12 col-sm-1 no-pad recommendation-line">
      <h4 class="f--condensed"><a href="#" class="f--condensed--link" onClick="openRecomModal()"><spring:message
            code="general.recommendation" /> </a>
          <span class="visible-xs-inline">&nbsp;</span>
          <a id="inforecom" class="infobutton"
        data-placement="bottom" role="button" data-toggle="popover"
        data-content="<spring:message code="reading.tt.recom" />">
        <span class="glyphicon glyphicon-info-sign"></span>
      </a>
      </h4>


     
  <c:if test="${fragment != null}">
        <div class="h3-group">
      
        
       


        <div>
          <a     href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${inter.urlId}">
          <!--<span class="glyphicon glyphicon-play"></span>-->
           <h3 style="color:#FC1B27">${inter.getEdition().getAcronym()}</h3><h2 style="color:#FC1B27">${inter.number}</h2></a>
        </div>

         <div class="ldod-reading-prevrecom">
          <c:if test="${prevRecom != null}">
            <div class="h3-div">
              <a href="${contextPath}/reading/inter/prev/recom">
                <h3>${prevRecom.getEdition().getAcronym()}</h3>
                <h2 class="recom-h2">${prevRecom.number}
                
                 <span class="visible-xs-inline" style="float:right">
              <img src="/resources/img/graphics/arrow_left.png">
              </span>
              <div class="arrows-recom hidden-xs">
              <img src="/resources/img/graphics/arrow_left.png">
              </div>
                </h2>

              </a>
            </div>
          </c:if>
        </div>

        
        <c:forEach var="recomInter" items="${recommendations}">
          <div>
            <a href="${contextPath}/reading/fragment/${recomInter.fragment.xmlId}/inter/${recomInter.urlId}">
            <!--<span class="glyphicon glyphicon-forward"></span>-->
              <h3>${recomInter.getEdition().getAcronym()}</h3>
              <h2 class="recom-h2">${recomInter.number}
             
              
              <span class="visible-xs-inline" style="float:right">
              <img src="/resources/img/graphics/arrow_right.png">
              </span>
              <div class="arrows-recom hidden-xs">
              <img src="/resources/img/graphics/arrow_right.png">
              </div>
              </h2>
              </a>
              
          </div>
        </c:forEach>

        </div>
  </c:if>

</div>
</div>

 
<!-- ###################### MODAL ###################### -->

<!-- Recommendations configuration Modal HTML -->
<div class="modal fade" id="recommendationModal" tabindex="-1"
  role="dialog">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" onclick="reload()"
          data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <h3 class="modal-title text-center">
          <spring:message code="general.recommendation.config" />
        </h3>
      </div>
      <!--
      <div class="modal-body">
        <div class="row">
          <div class="form-group" id="clearPrevRecomForm">
            <div class="col-md-7">
              <h4 class="text">
                <spring:message code="general.reset.list" />
                :
              </h4>
            </div>
            <div class="col-md-1">
              <button type="submit" class="btn btn-primary"
                onclick="resetPrevRecom()">
                <span class="glyphicon glyphicon-saved"></span>
                <spring:message code="general.reset" />
              </button>
            </div>
          </div>
        </div>
        <hr>
        -->
        <div class="modal-body">
        <h3 class="text-center">
              <spring:message code="recommendation.criteria" />
              :
        </h3>
        </div>
        <div class="row text-center" style="padding-left:15px; padding-right:15px">
          <!--
          <div class="col-md-3 text-center">
            <h4>
              <spring:message code="recommendation.criteria" />
              :
            </h4>
          </div>
          -->
          <div class="col-md-3 col-sm-4">
            <spring:message code="general.heteronym" />
            <input type="range" class="range"
              onChange="changeWeight('heteronym', value)"
              value='${ldoDSession.getRecommendation().getHeteronymWeight()}'
              max="1" min="0" step="0.2">
          </div>
          <div class="col-md-3 col-sm-4">
            <spring:message code="general.date" />
            <input type="range" class="range"
              onChange="changeWeight('date', value)"
              value='${ldoDSession.getRecommendation().getDateWeight()}'
              max="1" min="0" step="0.2">
          </div>
          <div class="col-md-3 col-sm-4">
            <spring:message code="general.text" />
            <input type="range" class="range"
              onChange="changeWeight('text', value)"
              value='${ldoDSession.getRecommendation().getTextWeight()}'
              max="1" min="0" step="0.2">
          </div>
          <div class="col-md-3 col-sm-4">
            <spring:message code="general.taxonomy" />
            <input type="range" class="range"
              onChange="changeWeight('taxonomy', value)"
              value="${ldoDSession.getRecommendation().getTaxonomyWeight()}"
              max="1.0" min="0.0" step="0.2">
          </div>
        </div>
        <br>
        <div class="modal-footer">
        <button type="submit" class="btn btn-danger"
                onclick="resetPrevRecom()">
                <span class="glyphicon glyphicon-saved"></span>
                <spring:message code="general.reset" />
              </button>

          <button type="button" class="btn btn-primary" onclick="reload()"
            data-dismiss="modal">
            <spring:message code="general.close" />
          </button>
        </div>
      </div>
      <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
  </div>
  <!-- /.modal -->