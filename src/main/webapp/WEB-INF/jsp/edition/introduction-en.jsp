<li><a href="${contextPath}/edition/acronym/JPC">Jacinto do Prado Coelho</a></li>
<li><a href="${contextPath}/edition/acronym/TSC">Teresa Sobral Cunha</a></li>
<li><a href="${contextPath}/edition/acronym/RZ">Richard Zenith</a></li>
<li><a href="${contextPath}/edition/acronym/JP">Jerónimo Pizarro</a></li>
<hr>
<li><a href="${contextPath}/edition/acronym/LdoD-Arquivo">Arquivo LdoD</a></li>
<hr>
<c:forEach var="acronym" items='${ldoDSession.selectedVEAcr}'>
<li><a href="${contextPath}/edition/acronym/${acronym}">${acronym}</a></li>
</c:forEach>