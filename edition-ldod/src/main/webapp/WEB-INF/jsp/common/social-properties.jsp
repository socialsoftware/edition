<!--Facebook Info-->
<meta property="og:url"           content="https://ldod.uc.pt/" />
<meta property="og:type"          content="website" />
<meta property="og:title"         content="LdoD" />
<!--<meta property="og:description"   content="Teste" />-->
<!--Facebook Javascript SDK-->
<div id="fb-root"></div>
<c:choose>
    <c:when test='${pageContext.response.locale.getLanguage().equals("en")}'>
        <script>var locale = "en_EN"</script>
    </c:when>
    <c:when test='${pageContext.response.locale.getLanguage().equals("es")}'>
        <script>var locale = "es_ES"</script>
    </c:when>
    <c:otherwise>
        <script>var locale = "pt_PT"</script>
    </c:otherwise>
</c:choose>
<script>(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.async=true;
    js.src = 'https://connect.facebook.net/'+locale+'/sdk.js#xfbml=1&version=v2.12';
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>