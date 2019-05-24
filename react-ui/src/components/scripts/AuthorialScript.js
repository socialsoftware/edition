$(document)
    .ready(
        function() {
            $(
                '[id="visualisation-properties-authorial"][data-toggle="checkbox"]')
                .on(
                    'click',
                    function() {
                        var data = new Array();
                        $('#baseinter :checked').each(
                            function() {
                                data.push(this.value);
                            });
                        var selDiff = $(
                            'input:checkbox[name=diff]')
                            .is(':checked');
                        var selDel = $(
                            'input:checkbox[name=del]')
                            .is(':checked');
                        var selIns = $(
                            'input:checkbox[name=ins]')
                            .is(':checked');
                        var selSubst = $(
                            'input:checkbox[name=subst]')
                            .is(':checked');
                        var selNotes = $(
                            'input:checkbox[name=notes]')
                            .is(':checked');
                        var selFacs = $(
                            'input:checkbox[name=facs]')
                            .is(':checked');
                        $
                            .get(
                                "${contextPath}/fragments/fragment/inter/authorial",
                                {
                                    interp : data,
                                    diff : selDiff,
                                    del : selDel,
                                    ins : selIns,
                                    subst : selSubst,
                                    notes : selNotes,
                                    facs : selFacs
                                },
                                function(html) {
                                    $(
                                        "#fragmentTranscription")
                                        .replaceWith(
                                            html);
                                });
                    });
        });