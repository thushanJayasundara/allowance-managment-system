function saveGramaNiladariDivision() {

    if ($('#divisionStatusId').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var obj = {
        "id": $('#divisionId').val() == "" ? "" : $('#divisionId').val(),
        "divisionCode":$('#divisionCodeId').val(),
        "gramaNiladhariDivision":$('#divisionNameId').val(),
        "commonStatus":$('#divisionStatusId').val(),
    }

    $.ajax({
        url: "/api/erp/grama-niladhari-division-management/",
        type: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(obj),
        success: function(data) {
            if(data.status) {
                loadLeaveTable();
                toastr.success("Successfully save.");
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function getByIdForEditGramaNiladariDivision(data) {
    var url = "/api/erp/grama-niladhari-division-management/"+data
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                clearGramaNiladhariDivision();
                $('#divisionId').val(data.payload[0].id);
                $('#divisionNameId').val(data.payload[0].gramaNiladhariDivision);
                $('#divisionCodeId').val(data.payload[0].divisionCode);
                $('#divisionStatusId').val(data.payload[0].commonStatus);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function loadDivisionTable() {
    $.ajax({
        url: "/api/erp/grama-niladhari-division-management/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setDivisionTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function setDivisionTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#divisionTable').DataTable().clear();
        $('#divisionTable').DataTable({
            "bPaginate": false,
            "bLengthChange": false,
            "bFilter": false,
            "bInfo": false,
            "destroy": true,
            "language": {
                "emptyTable": "No Data Found !!!",
                search: "",
                searchPlaceholder: "Search..."
            }
        });
    } else {
        $("#divisionTable").DataTable({
            dom: 'Bfrtip',
            lengthMenu: [
                [ 10, 25, 50, 100],
                [ '10', '25', '50', '100']
            ],
            buttons: [{
                extend: 'pageLength'
            },
                {
                    extend: 'pdf',
                    title: 'Department Details',
                    pageSize: 'A4'
                },
                {
                    extend: 'excel',
                    title: 'Department Details',
                    pageSize: 'A4'
                },
                {
                    extend: 'print',
                    title: 'Department Details',
                    pageSize: 'A4'
                }
            ],
            "destroy": true,
            "language": {
                search: "",
                searchPlaceholder: "Search..."
            },
            "data": data,
            "columns": [
                {
                    "data": "divisionCode"
                },
                {
                    "data": "gramaNiladhariDivision"
                },
                {
                    "data": "commonStatus",
                    mRender: function(data) {
                        var classLb = ''
                        if(data == "ACTIVE")
                            classLb = 'badge badge-success'
                        if(data == "INACTIVE")
                            classLb = 'badge badge-info'
                        else
                            classLb = 'badge badge-success'
                        var columnHtml = '<td><label class="'+classLb+'">'+data+'</label></td>';
                        return (columnHtml);
                    }
                }, {
                    "data": "id",
                    mRender: function(data) {
                        var columnHtml = '<button value="'+data+'" onclick="deleteGramaNiladariDivisionConfirm(value)" type="button" '+
                            'class="btn btn-gradient-danger btn-rounded btn-icon" style="margin-left: 10px;margin-right: 5px;">' +
                            '<i class="mdi mdi-close-circle-outline"></i></button>'+
                            '<button value="'+data+'" onclick="getByIdForEditGramaNiladariDivision(value)" type="button"'+
                            'class="btn btn-gradient-info btn-rounded btn-icon" style="margin-left: 10px;">' +
                            '<i class="mdi mdi-border-color"></i></button>';
                        return (columnHtml);
                    }
                }]
        });
    }
}

function deleteGramaNiladariDivisionConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deleteGramaNiladariDivision(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deleteGramaNiladariDivision(data) {
    $.ajax({
        url:'/api/erp/grama-niladhari-division-management/' + data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadDivisionTable();
                toastr.success("Grama Niladari Division Deleted Successfully.");
            }else {
                toastr.error(data.errorMessages);
                return;
            }
        },
        error: function(xhr) {
            toastr.error("Deleting Privilege failed.");
        }
    });
}

function clearGramaNiladhariDivision() {
    $('#divisionId').val('');
    $('#divisionNameId').val('');
    $('#divisionCodeId').val('');
    $('#divisionStatusId').val('');
}
