(function($) {
    $.fn.dataTable = function(options) {
        var opts = $.extend({
            // These are the defaults.
            type: this.data("type"),
            ths: [],
            sort: "",
            rpp: 10,
            baseUrl: "/api",
            messageDiv: "message"
        }, options);

        var crud = {
            opts: opts,
            tbl: this,
            page: 0,
            totalPages: 1,
            searchobj: {},
            load: function(pg, searchobj) {
                if (pg >= 0 && pg < this.totalPages) {
                    if (searchobj)
                        this.searchobj = searchobj;
                    $.ajax({
                        method: "get",
                        url: opts.baseUrl + "/load/" + opts.type,
                        data: $.extend({ page: pg, size: opts.rpp }, this.searchobj),
                        crud: this,
                        pg: pg
                    }).done(function(data) {
                        this.crud.page = this.pg;
                        this.crud.totalPages = data.totalPages;
                        var tbody = this.crud.tbl.find("tbody").html("");
                        for (var x = 0; x < data.content.length; x++) {
                            var tr = this.crud.createTrFromObj(data.content[x]);
                            tbody.append(tr);
                        }
                    }).fail(function(xhr) {
                        this.crud.writeMessage({ success: false, msg: xhr.responseText });
                    });
                } 
                else if (pg < 0)
                    this.writeMessage({ success: false, msg: "Already on the first page"});
                else
                    this.writeMessage({ success: false, msg: "No more pages available"});
                
            },
            save: function(tr) {
                var tds = tr.find("td");
                var getData = {
                    id: $(tr).data("id")
                }
                for (var x=0; x<this.opts.ths.length; x++) {
                    getData[this.opts.ths[x]] = $(tds[x]).text();
                }

                $.ajax({
                    method: "POST",
                    url: opts.baseUrl + "/save/" + opts.type,
                    contentType: "application/json",
                    data: JSON.stringify(getData),
                    crud: this,
                    tr: tr
                }).done(function(data) {
                    this.crud.writeMessage(data);
                    if (data.success)
                        this.tr.data("id",data.id)
                }).fail(function(xhr) {
                    this.crud.writeMessage({ success: false, msg: xhr.responseText });
                });

            },
            delete: function(tr) {
                var id = tr.data("id");
                if (id) {
                    $.ajax({
                        method: "DELETE",
                        url: opts.baseUrl + "/delete/" + opts.type + "/" + id,
                        crud: this,
                        tr: tr
                    }).done(function(data) {
                        this.crud.writeMessage({ success: true, msg: data });
                        if (data.success)
                            this.tr.fadeOut();
                    }).fail(function(xhr) {
                        this.crud.writeMessage({ success: false, msg: xhr.responseText });
                    });
                } else {
                    tr.fadeOut();
                }
            },
            // Creates a table row (tr) from a javascript object and returns it
            createTrFromObj: function(obj) {
                var tr = $("<tr>");
                tr.data("id", obj.id);
                var ths = this.opts.ths;
                for (var y = 0; y < ths.length; y++) {
                    var value = obj[ths[y]] ? obj[ths[y]] : "";
                    var td = $("<td contenteditable='true'>" + value + "</td>");
                    tr.append(td);
                }
                tr.append(this.createButtonsTd());
                return tr;
            },
            // create a td element containing save and delete buttons
            createButtonsTd: function() {
                return $("<td style='white-space: nowrap'>")
                    .append("<a href='#' class='btn btn-primary btn-save'><span class='fa fa-save'></span></a> ")
                    .append("<a href='#' class='btn btn-primary btn-delete'><span class='fa fa-trash-o'></span></a></td>");
            },
            // write a message to the page in this.opts.messageDiv
            writeMessage(msg) {
                $("#" +this.opts.messageDiv).removeClass().addClass("alert " + (msg.success ? "alert-info" : "alert-danger")).text(msg.msg + (msg.id ? " id:" + msg.id : ""));
            }
        };

            var ths = $(this).find("th");
            // store all th data-col attributes in opts.ths
            for (var x = 0; x < ths.length; x++) {
                opts.ths.push($(ths[x]).data("col"));
            }
            // add extra th for buttons
            $(this).find("thead tr").append($("<th>"));
            // append tfoot section 
            $(this).append($("<tfoot><tr><td colspan='" + (ths.length + 1) + "'>" +
                "<a href='#' class='right btn btn-success btn-add'><span class='fa fa-plus'></span> Add Task</a>" +
                "<a href='#' class='btn btn-primary btn-prev-page'><span class='fa fa-arrow-left'></span></a> " +
                "<a href='#' class='btn btn-primary btn-next-page'><span class='fa fa-arrow-right'></span></a>" +
                "</td></tr></tfoot>"));

            // bind click events for save, delete and add buttons
            var tcrud = crud;
            $(this).find("tbody").on("click", ".btn-save", function() { 
                tcrud.save($(this).parents("tr")); return false; 
            });
            $(this).find("tbody").on("click", ".btn-delete", function() { 
                tcrud.delete($(this).parents("tr")); return false; 
            });
            $(this).find("tfoot .btn-add").click(function() {
                this.tbl.append(crud.createTrFromObj({ id: 0 }));
                return false;
            }.bind(crud));
            $(this).find("tfoot .btn-prev-page").click(function() {
                this.load(crud.page-1);
                return false;
            }.bind(crud));
            $(this).find("tfoot .btn-next-page").click(function() {
                this.load(crud.page+1);
                return false;
            }.bind(crud));

            return crud;
    };
}(jQuery));
