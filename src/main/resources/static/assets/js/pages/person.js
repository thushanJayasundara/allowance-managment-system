$(document).ready(function() {
    loadPersonTable();
    clearPersonField();
});

function savePerson() {
    var gender;

    if ($('#statusId').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    if (document.getElementById('maleId').checked) {
        gender = $("input[name='maleName']:checked").val();
    } else if (document.getElementById('femaleId').checked) {
        gender = $("input[name='femaleName']:checked").val();
    } else {
        toastr.error("Please Select a gender");
        return;
    }

    var personDTO = {
        "id": $('#personId').val() == "" ? "" : $('#personId').val(),
        "fullName":$('#fullNameId').val(),
        "initials":$('#initialsId').val(),
        "firstName":$('#firstNameId').val(),
        "lastName":$('#lastNameId').val(),
        "dob":$('#dobId').val()+" 00:00",
        "nic":$('#nicId').val(),
        "address":$('#addressId').val(),
        "gramaNiladhariDivisionDTO":{"id": $('#setGNDivisionId').val()},
        "gender":gender,
        "contactNumber":$('#contactNumber').val(),
        "commonStatus":$('#statusId').val(),
    }

    $.ajax({
        url: "/api/erp/person-management/",
        type: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(personDTO),
        success: function(data) {
            if(data.status) {
                toastr.success("Successfully save.");
                loadPersonTable();
                clearPersonField();
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}


function searchPerson() {
    var search_person = $('#search_person').val()
    var url = "/api/erp/person-management/get-by-nic/"+search_person
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {

                $('#personId').val(data.payload[0].id);
                $('#fullNameId').val(data.payload[0].fullName);
                $('#initialsId').val(data.payload[0].initials);
                $('#firstNameId').val(data.payload[0].firstName);
                $('#lastNameId').val(data.payload[0].lastName);
                $('#dobId').val(data.payload[0].dob);
                $('#nicId').val(data.payload[0].nic);
                $('#addressId').val(data.payload[0].address);
                $('#setGNDivisionId').val(data.payload[0].gramaNiladhariDivision);
                if (data.payload[0].gender == "MALE")
                      $("#maleId").prop("checked", true);
                else
                    $("#femaleId").prop("checked", true);

                $('#contactNumber').val(data.payload[0].contactNumber);
                $('#statusId').val(data.payload[0].commonStatus);
                $('#setGNDivisionId').append(' <option value="'+data.payload[0].gramaNiladhariDivisionDTO.id+'">'+data.payload[0].gramaNiladhariDivisionDTO.gramaNiladhariDivision+'</option>');
                $('#setGNDivisionId').val(data.payload[0].gramaNiladhariDivisionDTO.id);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function getAllGramaNiladhariDivisionForPerson() {
    $.ajax({
        url: "/api/erp/grama-niladhari-division-management/",
        type: "GET",
        data: {},
        success: function(data) {
            var dataList = data.payload[0];
            if(dataList != null) {
                $('#setGNDivisionId').empty();
                $('#setGNDivisionId').append('<option value="-1">Select a division</option>');
                $.each(dataList, function(key, value) {
                    $('#setGNDivisionId').append(' <option value="'+value.id+'">'+value.gramaNiladhariDivision+'</option>');
                });

            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function loadPersonTable() {
    $.ajax({
        url: "/api/erp/person-management/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setPersonTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function setPersonTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#personTable').DataTable().clear();
        $('#personTable').DataTable({
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
        $("#personTable").DataTable({
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
                    title: 'Holiday Details',
                    pageSize: 'A4'
                },
                {
                    extend: 'excel',
                    title: 'Holiday Details',
                    pageSize: 'A4'
                },
                {
                    extend: 'print',
                    title: 'Holiday Details',
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
                        "data": "fullName"
                    },
                    {
                        "data": "initials"
                    },
                    {
                        "data": "firstName"
                    },
                    {
                        "data": "lastName"
                    },
                    {
                        "data": "dob"
                    },
                    {
                        "data": "registrationDate"
                    },
                    {
                        "data": "nic"
                    },
                    {
                        "data": "address"
                    },
                    {
                        "data": "contactNumber"
                    },
                    {
                        "data": "gramaNiladhariDivisionDTO.gramaNiladhariDivision"
                    },
                    {
                        "data": "gender"
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
                    },{
                        "data": "id",
                        mRender: function(data) {
                            var columnHtml = '<button value="'+data+'" onclick="deletePersonConfirm(value)" type="button" class="btn btn-gradient-danger btn-rounded btn-icon">'+
                                '<i class="mdi mdi-close-circle-outline"></i></button>'+
                                '<button value="'+data+'" onclick="getByIdForEditPerson(value)" type="button"'+
                                'class="btn btn-gradient-info btn-rounded btn-icon" style="margin-left: 10px;">'+
                                '<i class="mdi mdi-border-color"></i></button>';
                            return (columnHtml);
                        }
                    }
                ]
        });
    }
}

function deletePersonConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deletePerson(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deletePerson(data) {
    $.ajax({
        url:'/api/erp/person-management/' + data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadPersonTable();
                toastr.success("Person Deleted Successfully.");
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

function getByIdForEditPerson(data) {
    var url = "/api/erp/person-management/"+data
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                $('#personId').val(data.payload[0].id);
                $('#fullNameId').val(data.payload[0].fullName);
                $('#initialsId').val(data.payload[0].initials);
                $('#firstNameId').val(data.payload[0].firstName);
                $('#lastNameId').val(data.payload[0].lastName);
                $('#dobId').val(data.payload[0].dob);
                $('#nicId').val(data.payload[0].nic);
                $('#addressId').val(data.payload[0].address);
                $('#setGNDivisionId').val(data.payload[0].gramaNiladhariDivision);
                if (data.payload[0].gender == "MALE")
                    $("#maleId").prop("checked", true);
                else
                    $("#femaleId").prop("checked", true);

                $('#contactNumber').val(data.payload[0].contactNumber);
                $('#statusId').val(data.payload[0].commonStatus);
                $('#setGNDivisionId').append(' <option value="'+data.payload[0].gramaNiladhariDivisionDTO.id+'">'+data.payload[0].gramaNiladhariDivisionDTO.gramaNiladhariDivision+'</option>');
                $('#setGNDivisionId').val(data.payload[0].gramaNiladhariDivisionDTO.id);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function clearPersonField() {
    $('#personId').val('');
    $('#fullNameId').val('');
    $('#initialsId').val('');
    $('#firstNameId').val('');
    $('#lastNameId').val('');
    $('#dobId').val('');
    $('#nicId').val('');
    $('#addressId').val('');
    $('#femaleId').prop("checked", false);
    $('#maleId').prop("checked", false);
    $('#statusId').val('');
    $('#contactNumber').val('');
    getAllGramaNiladhariDivisionForPerson();
}