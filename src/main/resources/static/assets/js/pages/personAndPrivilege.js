
$('#setAllUser').on('change', function() {
    if ( $('#host').val() != '-1' ){
        getPersonPrivilegeById($('#setAllUser').val().trim());
    }
});

function  savePersonPrivilege() {
    var privileges = [];
    $.each($("input[name='privilege']:checked"), function () {
        privileges.push({
            id: $(this).val().trim()
        });
    });

    var obj = {
        "id": $('#priAndPerId').val() == "" ? "" : $('#priAndPerId').val(),
        "person": {
            "id": $('#setAllUser').val().trim(),
        },
        "privilege": privileges,
        "status": "ACTIVE",
    }

    $.ajax({
        url: "/api/erp/person-privilege-management/",
        type: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(obj),
        success: function(data) {
            if(data.status) {
                loadPersonAndPrivilegeTable();
                clearPersonAndPrivilege();
                toastr.success("Successfully save.");
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function getAllUsersForPri() {
    $.ajax({
        url: "/api/erp/person-management/",
        type: "GET",
        data: {},
        success: function(data) {
            var dataList = data.payload[0];
            if(dataList != null) {
                $('#setAllUser').empty();
                $('#setAllUser').append('<option value="-1">Select a Person</option>');
                $.each(dataList, function(key, value) {
                    $('#setAllUser').append(' <option onchange="getPersonPrivilegeById(this.value)"  value="'+value.id+'">'+value.firstName+' '+value.lastName+' ('+value.nic+')</option>');
                });

            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function getAllPrivilegeForUser() {
    $.ajax({
        url: "/api/erp/privilege-management/",
        type: "GET",
        data: {},
        success: function(data) {
            var dataList = data.payload[0];
            if (dataList != null) {
                $('#privilege_div').empty();

                $.each(dataList, function(key, value) {
                    $('#privilege_div').append('<div class="form-check" style="margin-left: 25px; margin-right: 10px;">\n' +
                        '<input type="checkbox" id="personId" name="privilege" value="'+value.id+' " /> '+value.privilege+'</div>');
                });
            }
        },
    error: function(xhr) {
            toastr.error("Getting data for user role failed.");
        }
    });
}

function loadPersonAndPrivilegeTable() {
    $.ajax({
        url: "/api/erp/person-privilege-management/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setPersonAndPrivilegeTable(data.payload[0])
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function setPersonAndPrivilegeTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#privilegeAndPersonTable').DataTable().clear();
        $('#privilegeAndPersonTable').DataTable({
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
        $("#privilegeAndPersonTable").DataTable({
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
                    title: 'Attendance Details',
                    pageSize: 'A4'
                },
                {
                    extend: 'excel',
                    title: 'Attendance Details',
                    pageSize: 'A4'
                },
                {
                    extend: 'print',
                    title: 'Attendance Details',
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
                    "data": null,
                    render: function ( data, type, row ) {
                        return row.person.firstName + ' ' + row.person.lastName;
                    }
                },
                {
                    "data": "person.nic"
                },
                {
                    "data": null,
                    mRender: function( data) {
                        let columnHtml;
                        $.each(data.privilege, function(key, value) {
                           if (columnHtml == null)
                               columnHtml = String(value.privilege);
                           else {
                               columnHtml = columnHtml.concat(",",value.privilege);
                           }
                        });
                        var finalcol = '<td><label>'+columnHtml+'</label></td>'
                        return (finalcol);
                    }
                },
                {
                    "data": "status",
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
                },{
                    "data": "id",
                    mRender: function(data) {
                        var columnHtml = '<button value="'+data+'" onclick="deletePersonPrivilegeConfirm(value)" type="button" class="btn btn-gradient-danger btn-rounded btn-icon">'+
                            '<i class="mdi mdi-close-circle-outline"></i>'+
                            '</button>';
                        return (columnHtml);
                    }
                }
                ]
        });
    }
}

function deletePersonPrivilegeConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deletePersonPrivilege(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deletePersonPrivilege(data) {
    $.ajax({
        url:'/api/erp/person-privilege-management/' + data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadPersonAndPrivilegeTable();
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

function getPersonPrivilegeById(data) {
    $.ajax({
        url: '/api/erp/person-privilege-management/personId/' + data,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                $('#priAndPerId').val(data.payload[0].id);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}


function clearPersonAndPrivilege() {
    getAllPrivilegeForUser();
    getAllUsersForPri();
}