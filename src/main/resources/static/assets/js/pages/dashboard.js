function loadTodayLeaveTable() {
    $.ajax({
        url: "/leave/today-leave",
        type: "GET",
        data: {},
        success: function(data) {
            if(!data.status) {
                toastr.error(data.errorMessages);
            } else {
                setTodayLeaveTable(data.payload[0])
            }
        }
    });
}

function setTodayLeaveTable(posts) {
    if ($.isEmptyObject(posts)) {
        $('#todayLeaveTable').DataTable().clear();
        $('#todayLeaveTable').DataTable({
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
        $("#todayLeaveTable").DataTable({
            dom: 'Bfrtip',
            buttons: [{
                extend: 'pageLength'
            }, {
                    extend: 'print',
                    title: 'Leave Details',
                    pageSize: 'A4'
                }
            ],
            "destroy": true,
            "data": posts,
            "columns": [{
                "data": "leaveUser.empNumber"
            },
                {
                    "data": "reason"
                },
                {
                    "data": "leaveDate"
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
                }]
        });
    }
}