<%@ include file="/WEB-INF/jsp/common/tags-head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/jsp/common/meta-head.jsp" %>
    <script src="/resources/js/jquery-ui-1.11.4/jquery-ui.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp" %>
<div class="container">
    <br/>
    <div class="row col-md-1">
        <form class="form-inline" method="GET"
              action="${contextPath}/virtualeditions/restricted/manage/${edition.externalId}">
            <button type="submit" class="btn btn-default">
                <span class="glyphicon glyphicon-arrow-left"></span>
                <spring:message code="general.back"/>
            </button>
        </form>
    </div>
    <input id="acronym" type="hidden" name="externalId"
           value="${edition.acronym}"/>
    <div class="row col-md-12">
        <h3 class="text-center">${edition.title}</h3>
    </div>
    <br/>
    <br/>
    <br/>
    <div class="row">
        <%-- 			<div class="row col-md-12 has-error" align="right">
                        <c:forEach var="error" items='${errors}'>
                            <div class="row">
                                <spring:message code="${error}" />
                            </div>
                        </c:forEach>
                    </div>
                    <div class="row col-md-12 extra" style="display: none;" align="right">
                        <form class="form-inline extra" method="POST"
                            action="/recommendation/linear/create" style="display: none;"
                            id="create">
                            <div class="form-group input-group-sm">
                                <div class="input-group">
                                    <div class="input-group-addon">LdoD-</div>
                                    <input type="text" class="form-control" id="new-acronym"
                                        name="acronym"
                                        placeholder="<spring:message code="virtualeditionlist.acronym"/>" />
                                </div>
                            </div>

                            <div class="form-group input-group-sm">
                                <input type="text" class="form-control" name="title"
                                    id="new-title"
                                    placeholder="<spring:message code="virtualeditionlist.name" />" />
                            </div>
                            <div class="form-group input-group-sm">
                                <select name="pub" class="form-control" id="new-pub">
                                    <option value="true" selected>
                                        <spring:message code="general.public" />
                                    </option>
                                    <option value="false" selected>
                                        <spring:message code="general.private" />
                                    </option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary btn-sm input-group-sm">
                                <span class="glyphicon glyphicon-floppy-disk"></span>
                                <spring:message code="general.create" />
                            </button>
                        </form>
                    </div> --%>
        <br/> <br/><br/>
        <div class="row col-md-12">
            <table id="criteria-table" class="table table-condensed table-hover">
                <thead>
                <tr>
                    <th class="sections" style="display: none;">#</th>
                    <th class="text-center"><spring:message
                            code="recommendation.criteria"/></th>
                    <th class="text-center">
                        <%-- <spring:message
                            code="general.structure" /> --%>
                    </th>
                    <th class="text-center">
                        <%-- <spring:message code="general.save" /> --%>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th class="sections" style="display: none;">1</th>
                    <th>
                        <div class="row row-centered criteria-row" id="0"
                             style="min-height: 40px;">
                            <div class="col-md-2 col-sm-4 criteria"
                                 property-type="heteronym">
                                <p>
                                    <spring:message javaScriptEscape="true"
                                                    code="general.heteronym"/>
                                </p>
                                <input type="range" class="range" value='${heteronymWeight}'
                                       max="1" min="0" step="0.2">
                            </div>
                            <div class="col-md-2 col-sm-4 criteria" property-type="date">
                                <p>
                                    <spring:message javaScriptEscape="true" code="general.date"/>
                                </p>
                                <input type="range" class="range" value='${dateWeight}'
                                       max="1" min="0" step="0.2">
                            </div>
                            <div class="col-md-2 col-sm-4 criteria" property-type="text">
                                <p>
                                    <spring:message javaScriptEscape="true" code="general.text"/>
                                </p>
                                <input type="range" class="range" value='${textWeight}'
                                       max="1" min="0" step="0.2">
                            </div>
                            <div class="col-md-2 col-sm-4 criteria"
                                 property-type="taxonomy">
                                <p>
                                    <spring:message code="general.taxonomy"/>
                                </p>
                                <input type="range" class="range" value="${taxonomyWeight}"
                                       max="1.0" min="0.0" step="0.2">
                            </div>
                        </div>
                    </th>
                    <!--  COMMENTED BECAUSE CLUSTERING IS NOT SUPPORTED -->
                    <%--  <th>
                        <p class="text-center">
                            <input style="display: inline;" class="text-center"
                                type="checkbox" id="sections" />
                            <spring:message code="general.sections" />
                        </p>
                    </th>  --%>
                    <th>
                        <div class="form-inline extra text-center">
                            <button type="submit" id="save" class="btn btn-primary btn-sm">
                                <span class="glyphicon glyphicon-floppy-saved"></span>
                                <spring:message code="general.save"/>
                            </button>
                        </div>
                    </th>
                </tr>
                <c:forEach var="i" begin="2" end="4">
                    <tr class="sections" style="display: none;">
                        <th>${i}</th>
                        <th>
                            <div class="row row-centered criteria-row" id="${i-1}"
                                 style="min-height: 40px;"></div>
                        </th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <hr>
    <div class="row">
        <%@ include file="/WEB-INF/jsp/recommendation/virtualTable.jsp" %>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(
        function () {
            var levels = {
                heteronym: 0,
                date: 0,
                text: 0,
                taxonmy: 0
            };

            function fadeWhenZero(elem) {
                var inp = elem.children('input.range');
                var val = inp.val();
                if (val > 0) {
                    elem.fadeTo(0, 1.);
                } else {
                    elem.fadeTo(0, 0.5);
                }
            }

            function startLoad() {
                $('body').addClass('progress');
            }

            function endLoad() {
                $('body').removeClass('progress');
            }

            function sortInters(id) {
                startLoad();

                var url;
                var data;
                var id = id;
                var acr = $('#acronym').attr('value');
                var json = [];

                if ($('#sections').is(':checked')) {
                    url = '/recommendation/section';
                    var stop = false;
                    $('.criteria').each(function () {
                        var level = parseInt($(this).parent().attr('id'), 10);
                        if (isNaN(level)) {
                            alert("Not a number at " + $(this).attr('id'));
                            stop = true;
                            return false;
                        }

                        var type = $(this).attr('property-type');

                        levels[type] = level;

                        json.push({
                            type: 'property-with-level',
                            level: level,
                            property: {
                                type: type,
                                acronym: acr,
                                weight: $(this).children('input.range').val()
                            }
                        });
                    });
                    if (stop) {
                        return;
                    }
                } else {
                    url = '/recommendation/linear';
                    $('.criteria').each(function () {
                        var type = $(this).attr('property-type');
                        json.push({
                            type: type,
                            acronym: acr,
                            weight: $(this).children('input.range').val()
                        });
                    });
                }

                data = {
                    acronym: acr,
                    id: id,
                    properties: json
                };
                $.ajax({
                    url: url,
                    type: 'post',
                    data: JSON.stringify(data),
                    contentType: 'application/json;charset=UTF-8',
                    success: function (htm) {
                        endLoad();
                        $('#virtual-table').empty().append(htm)
                        console.log($(htm).children('tbody').children().length)
                        if ($(htm).children('tbody').children().length > 0)
                            $('.extra').show();
                    }
                });
            }

            function manageCriteriaRanges(checked) {
                if (checked) {
                    $('.sections').show();
                    $('.criteria > p').each(function () {
                        $(this).addClass("draggable");
                    });
                    $('.criteria-row').each(function () {
                        $(this).addClass("drop-zone");
                    });

                    $('.draggable').draggable({
                        cancel: "a.ui-icon",
                        revert: "invalid",
                        containment: "document",
                        helper: "clone",
                        cursor: "move",
                        cursorAt: {
                            top: 5,
                            left: 5
                        }
                    });

                    $('.drop-zone').droppable({
                        accept: ".draggable",
                        activeClass: "ui-state-highlight",
                        hoverClass: "ui-state-hover",
                        drop: function (event, ui) {
                            var self = this, item = $(ui.draggable).parent();
                            startLoad();
                            item.fadeOut(function () {
                                item.appendTo(self).fadeIn();
                                reSort();
                            });
                        }
                    });

                    $('.criteria[property-type="heteronym"]').appendTo(
                        $("#" + levels["heteronym"] + ".criteria-row"));
                    $('.criteria[property-type="date"]').appendTo($("#" + levels["date"] + ".criteria-row"));
                    $('.criteria[property-type="text"]').appendTo($("#" + levels["text"] + ".criteria-row"));
                    $('.criteria[property-type="taxonomy"]').appendTo(
                        $("#" + levels["taxonomy"] + ".criteria-row"));

                } else {
                    $('.criteria').each(function () {
                        $('#0').append($(this));
                        $(this).find('p').removeClass('draggable ui-draggable ui-draggable-handle');
                    });
                    $('.sections').hide();
                }
            }

            function reSort() {
                var id = $('a.inter.selected').attr('id') || $('a.inter').first().attr('id');
                sortInters(id);
            }

            $('.criteria').each(function () {
                fadeWhenZero($(this));
            });

            $('.criteria').change(function () {
                fadeWhenZero($(this));
                reSort();
            });

            $('#sections').change(function () {
                manageCriteriaRanges($(this).is(":checked"));
                reSort();
            });

            $('#save').click(function () {
                var acronym = $('#acronym').attr('value');
                if ($('#sections').is(":checked")) {
                    var json = [];
                    var stack = [];
                    var data = $('#virtual-table tbody tr').map(function () {
                        var row = $(this);
                        var arr = [];
                        $(this).find('td.level').each(function () {
                            var text = '';
                            $(this).find('p').each(function () {
                                text += $(this).text();
                            });
                            arr.push(text);
                        });
                        return {
                            depth: arr,
                            id: row.find('a.inter').attr('id')
                        }
                    }).get();

                    var old = data[0].depth || [];
                    for (i in data) {
                        if (data[i].depth.length == 0) {
                            data[i].depth = old.slice();
                        } else if (data[i].depth.length < old.length) {
                            var temp = old.slice(0, old.length - data[i].depth.length);
                            var fin = temp.concat(data[i].depth);
                            old = fin;
                            data[i].depth = fin.slice();
                        } else if (data[i].depth.length === old.length) {
                            old = data[i].depth;
                        } else {
                        }
                    }

                    var json = {
                        acronym: acronym,
                        sections: data
                    };

                    $.ajax({
                        url: '/recommendation/section/save',
                        type: 'post',
                        data: JSON.stringify(json),
                        contentType: 'application/json;charset=UTF-8',
                        success: function (htm) {
                            if (!json.error)
                                location.reload(true);
                        }
                    });
                } else {
                    var inters = [];
                    $('a.inter').each(function () {
                        inters.push($(this).attr('id'));
                    });
                    var data = {
                        acronym: acronym,
                        inter: inters
                    };
                    $.ajax({
                        url: '/recommendation/linear/save',
                        type: 'post',
                        data: data,
                        success: function (html) {
                            var newDoc = document.open("text/html", "replace");
                            newDoc.write(html);
                            newDoc.close();
                        }
                    });
                }
            });

            $('#create').submit(
                function (event) {
                    var form = $(this);
                    if ($('#sections').is(":checked")) {
                        var json = [];
                        var stack = [];

                        var data = $('#virtual-table tbody tr').map(function () {
                            var row = $(this);
                            var arr = [];

                            $(this).find('td.level').each(function () {
                                var text = '';
                                $(this).find('p').each(function () {
                                    text += $(this).text();
                                });
                                arr.push(text);
                            });

                            return {
                                depth: arr,
                                id: row.find('a.inter').attr('id')
                            }
                        }).get();

                        var old = data[0].depth || [];
                        for (i in data) {
                            if (data[i].depth.length == 0) {
                                data[i].depth = old.slice();
                            } else if (data[i].depth.length < old.length) {
                                var temp = old.slice(0, old.length - data[i].depth.length);
                                var fin = temp.concat(data[i].depth);
                                old = fin;
                                data[i].depth = fin.slice();
                            } else if (data[i].depth.length === old.length) {
                                old = data[i].depth;
                            } else {

                            }
                        }

                        var acronym = $('#new-acronym').val();
                        var pub = $('#new-pub').val();
                        var title = $('#new-title').val();

                        var json = {
                            acronym: acronym,
                            title: title,
                            pub: pub,
                            sections: data
                        };
                        $(this).attr('action', '/recommendation/section/create');
                        var depthstring = '';
                        for (i in data) {
                            $('<input />').attr('type', 'hidden').attr('name', 'inter[]').attr('value',
                                data[i].id).appendTo(form);
                            depthstring = data[i].depth[0];

                            for (var j = 1; j < data[i].depth.length; j++) {
                                depthstring += "|" + data[i].depth[j];
                            }

                            $('<input />').attr('type', 'hidden').attr('name', 'depth[]').attr('value',
                                depthstring).appendTo(form);
                        }

                    } else {
                        $('a.inter').each(
                            function () {
                                $('<input />').attr('type', 'hidden').attr('name', 'inter[]').attr(
                                    'value', $(this).attr('id')).appendTo(form);
                            });
                    }
                    return true;
                });

            $('#virtual-table').on('click', 'button.sort', function () {
                var id = $(this).parent().siblings().find('a.inter').attr('id');
                sortInters(id);
            });
        });
</script>
</body>
</html>