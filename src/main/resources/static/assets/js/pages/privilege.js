function  savePrivilege(){

    if ($('#privilegeStatus').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var privilege={
        "id": $('#privilegeId').val() == "" ? "" : $('#privilegeId').val(),
        "privilege": $('#privilegeName').val(),
        "privilegePolices": $('#privilegePolices').val(),
        "privilegeDescription": $('#privilegeDescription').val(),
        "commonStatus": $('#privilegeStatus').val()
    }

    $.ajax({
        url: "/api/erp/privilege-management/",
        type: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(privilege),
        success: function(data) {
            if(data.status) {
                loadPrivilegeTable();
                clearPrivilege();
                toastr.success("Successfully saved.");
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function searchPrivilege() {
    var privilege = $('#search_privilege').val()
    var url = "/api/erp/privilege-management/getByName/"+ privilege
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                $('#privilegeId').val(data.payload[0].id);
                $('#privilegeName').val(data.payload[0].privilege);
                $('#privilegePolices').val(data.payload[0].privilegePolices);
                $('#privilegeDescription').val(data.payload[0].privilegeDescription);
                $('#privilegeStatus').val(data.payload[0].commonStatus);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function getByIdForEditPrivilege(data) {
    var url = "/api/erp/privilege-management/"+data
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                clearPrivilege();
                $('#privilegeId').val(data.payload[0].id);
                $('#privilegeName').val(data.payload[0].privilege);
                $('#privilegePolices').val(data.payload[0].privilegePolices);
                $('#privilegeDescription').val(data.payload[0].privilegeDescription);
                $('#privilegeStatus').val(data.payload[0].commonStatus);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function deletePrivilegeConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deletePrivilege(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}


function deletePrivilege(data) {
    $.ajax({
        url:'/api/erp/privilege-management/' + data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadPrivilegeTable();
                toastr.success("Privilege Deleted Successfully.");
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

function loadPrivilegeTable() {
    $.ajax({
        url: "/api/erp/privilege-management/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setPrivilegeTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function setPrivilegeTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#privilegeTable').DataTable().clear();
        $('#privilegeTable').DataTable({
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
        $("#privilegeTable").DataTable({
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
                "columns": [{
                    "data": "privilege"
                },
                {
                    "data": "privilegePolices"
                },
                {
                    "data": "privilegeDescription"
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
                            var columnHtml = '<button value="'+data+'" onclick="deletePrivilegeConfirm(value)" type="button" ' +
                                'class="btn btn-gradient-danger btn-rounded btn-icon" style="margin-left: 10px;margin-right: 5px;">'+
                                '<i class="mdi mdi-close-circle-outline"></i></button>'+
                                '<button value="'+data+'" onclick="getByIdForEditPrivilege(value)" type="button"'+
                                'class="btn btn-gradient-info btn-rounded btn-icon" style="margin-left: 10px;">' +
                                '<i class="mdi mdi-border-color"></i></button>';
                            return (columnHtml);
                        }
                    }]
        });
    }
}

function  clearPrivilege(){
    $('#privilegeId').val('');
    $('#privilegeName').val('');
    $('#privilegePolices').val('');
    $('#privilegeDescription').val('');
    $('#privilegeStatus').val('');
    $('#search_privilege').val('');
}