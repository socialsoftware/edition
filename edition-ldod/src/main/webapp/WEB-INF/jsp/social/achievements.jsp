<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
    <%@ include file="/WEB-INF/jsp/common/social-properties.jsp"%>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
</head>
<body>
<script type="text/javascript">
    function showUnlocked() {
        document.getElementById('unlocked').style.visibility = 'visible';
        document.getElementById('locked').style.visibility = 'hidden';
    }
</script>
<div class="container">
    <div class="row">
        <h3 class="text-center">
            <spring:message code="Achievements" />
            <span class="fa fa fa-trophy"></span>
        </h3>
    </div>
    <!--
    <div class="achievements">
            <div class="div-link">
                <img id= src="" title="Registered in LdoD Archive" alt="Registered in LdoD Archive">
                <img id=  src="" title="Create a Virtual Edition" alt="Create a Virtual Edition">
                <img id= src="" title="Join a Virtual Edition alt="Join a Virtual Edition">
            </div>
    </div>
    -->
</div>
</body>
</html>

