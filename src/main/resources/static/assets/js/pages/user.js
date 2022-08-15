
function  saveUser(){

    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var password = $('#password').val();
    var confirmPassword = $('#confirmPassword').val();

    if(password !== confirmPassword) {
        toastr.error("Invalid password");
        return;
    }

    var userObj={
        "id":$('#userId').val() == "" ? "" : $('#userId').val(),
        "userName":$('#userName').val(),
        "password":$('#password').val(),
        "role":$('#role').val(),
        "commonStatus":$('#status').val(),
    }

    $.ajax({
        url: "/api/erp/user-management/",
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                clearUserField();
                loadUserTable();
                toastr.success("Successfully saved.");
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function searchUser() {
    var userId = $('#search_user').val()
    var url = "/api/erp/user-management/"+userId
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                $('#userId').val(data.payload[0].id);
                $('#username').val(data.payload[0].userName);
                $('#role').val(data.payload[0].role);
                $('#status').val(data.payload[0].commonStatus);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function deleteUserConfirm(data){

    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deleteUser(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
                        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deleteUser(data) {

        $.ajax({
            url:'/api/erp/user-management/' + data,
            type: "DELETE",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            success: function(data) {
                if (data.status) {
                    loadUserTable();
                    toastr.success("User Deleted Successfully.");
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

function loadUserTable() {
    $.ajax({
        url: "/api/erp/user-management/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setUserTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}


function setUserTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#userTable').DataTable().clear();
        $('#userTable').DataTable({
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
        $("#userTable").DataTable({
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
                    title: 'User Details',
                    pageSize: 'A4'
                },
                {
                    extend: 'excel',
                    title: 'User Details',
                    pageSize: 'A4'
                },
                {
                    extend: 'print',
                    title: 'User Details',
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
                    "data": "userName"
                },
                {
                     "data": "role"
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
                },
                    {
                        "data": "id",
                        mRender: function(data) {
                            var columnHtml = '<button value="'+data+'" onclick="deleteUserConfirm(value)" type="button" class="btn btn-gradient-danger btn-rounded btn-icon">'+
                                '<i class="mdi mdi-close-circle-outline"></i></button>'+
                                '<button value="'+data+'" onclick="getByIdForEditUser(value)" type="button"'+
                                'class="btn btn-gradient-info btn-rounded btn-icon" style="margin-left: 10px;">' +
                                '<i class="mdi mdi-border-color"></i></button>';
                            return (columnHtml);
                        }
                    }
                ]
        });
    }
}

function getByIdForEditUser(data) {
    var url = "/api/erp/user-management/"+data
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                clearUserField();
                $('#userId').val(data.payload[0].id);
                $('#userName').val(data.payload[0].userName);
                $('#role').val(data.payload[0].role);
                $('#status').val(data.payload[0].commonStatus);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function clearUserField() {
    $('#userId').val('');
    $('#username').val('');
    $('#password').val('');
    $('#confirmPassword').val('');
    $('#role').val('');
    $('#status').val('');
    $('#search_user').val('');
}



