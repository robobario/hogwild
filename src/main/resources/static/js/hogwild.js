$(document).ready(function() {
    $.getJSON("/app/story", function (data) {
        function toParagraphs(text){
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
            var body = $("<body>");
            heading.text(entry.characterName);
            var bodyParagraphs = toParagraphs(entry.body);
            for (var j = 0; j < bodyParagraphs.length; j++) {
                var paragraph = bodyParagraphs[j];
                var p = $("<p>");
                p.text(paragraph);
                body.append(p);
            }
            body.text(entry.body);
            container.append(heading);
            container.append(body);
            $("#story").append(container);
        }
        var offset = container.offset();
        $('html, body').animate({
            scrollTop: offset.top
        });
        if(data.myTurn){
            var form = $("<form><input type='text'></form>");
            var submit = $("<a>Add</a>");
            var edit = $("edit");
            edit.append(form);
            edit.append(submit);
            submit.click(function(){
                $.ajax({
                    type: "POST",
                    url: "/app/story",
                    data: JSON.stringify({"body":form.value()}),
                    success: function(){},
                    dataType: "json"
                });
            })
        }
    });
});