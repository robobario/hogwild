$(document).ready(function() {
    $.getJSON("/app/story", function (data) {
        function toParagraphs(text){
            if(!text){
                return [];
            }
            var lines = text.split("\n");
            var nonEmpty = [];
            for (var i = 0; i < lines.length; i++) {
                var line = lines[i];
                if(line && line.trim() !== ""){
                    nonEmpty.push(line.trim());
                }
            }
            return nonEmpty;
        }

        var entries = data.entries;
        for (var i = 0; i < entries.length; i++) {
            var entry = entries[i];
            var container = $("<div>");
            var heading = $("<h1>");
            var body = $("<div>");
            heading.text(entry.characterName);
            var bodyParagraphs = toParagraphs(entry.body);
            for (var j = 0; j < bodyParagraphs.length; j++) {
                var paragraph = bodyParagraphs[j];
                var p = $("<p>");
                p.text(paragraph);
                body.append(p);
            }
            container.append(heading);
            container.append(body);
            $("#story").append(container);
        }
        var offset = container.offset();
        $('html, body').animate({
            scrollTop: offset.top
        });
            var form = $("<form></form>");
            var input = $("<textarea></textarea>");
            var submit = $("<a>Add</a>");
            var edit = $("#edit");
            form.append(input);
            edit.append(form);
            edit.append(submit);
            submit.click(function(){
                $.ajax({
                    type: "POST",
                    url: "/app/story",
                    contentType:"application/json; charset=utf-8",
                    data: JSON.stringify({"body":input.val()}),
                    success: function(){},
                    dataType: "json"
                });
            })
    });
});