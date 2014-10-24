$(document).ready(function() {
    $.getJSON("/app/story", function (data) {
        var entries = data.entries;
        for (var i = 0; i < entries.length; i++) {
            var entry = entries[i];
            var container = $("<div>");
            var heading = $("<h1>");
            var body = $("<body>");
            heading.text(entry.characterName);
            body.text(entry.body);
            container.append(heading);
            container.append(body);
            $("#story").append(container);
        }
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