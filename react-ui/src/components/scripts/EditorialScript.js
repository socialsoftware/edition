$(document).ready(() => {
    $('[id="visualisation-properties-editorial"][data-toggle="checkbox"]').on('click', () => {
        const data = [];
        $('#baseinter :checked').each(function () {
            data.push(this.value);
        });
        const selDiff = $('input:checkbox[name=diff]').is(':checked');
        $.get('http://localhost:8080/fragments/fragment/inter/editorial', {
            interp: data,
            diff: selDiff,
        }, (html) => {
            $('#fragmentTranscription').replaceWith(html);
        });
    });
});