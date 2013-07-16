<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div id="fragmentTranscription" class="row-fluid">
    <div class="row-fluid span12">
        <h5>${inter.title}</h5>
        <div class="row-fluid">
            <div class="span6">
                <div id="myCarousel" class="carousel slide">
                    <div class="carousel-inner">
                        <c:forEach var="surface"
                            items='${inter.source.facsimile.surfaces}'
                            varStatus="status">
                            <c:choose>
                                <c:when test="${status.first}">
                                    <div class="active item">
                                        <img
                                            src="/facs/${surface.graphic}"
                                            alt="Carousel" />
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="item">
                                        <img
                                            src="/facs/${surface.graphic}"
                                            alt="Carousel" />
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                    <!-- Carousel nav -->
                    <a class="carousel-control left" href="#myCarousel"
                        data-slide="prev">&lsaquo;</a> <a
                        class="carousel-control right"
                        href="#myCarousel" data-slide="next">&rsaquo;</a>
                </div>
            </div>
            <div class="well span6">
                <p>${writer.getTranscription(inter)}</p>
            </div>
        </div>
    </div>
</div>