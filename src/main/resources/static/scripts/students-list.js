$(document).ready(function() {
    // Initialize the table and store it in a variable
    const table = $('#studentDataTable').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": "/students/data",
        "dom": 't<"d-flex justify-content-between align-items-center mt-4"ip>',
        "columns": [
            {
                "data": null,
                "render": function(data, type, row) {
                    return `<div class="fw-bold text-dark">${row.firstName} ${row.lastName}</div>
                            <div class="small text-muted">${row.gender === 'MALE' ? 'Male' : 'Female'}</div>`;
                }
            },
            {
                "data": "schoolClassDTO",
                "render": (data) => `<span class="badge bg-primary-subtle text-primary rounded-pill px-3">Grade ${data.grade}</span>`
            },
            {
                "data": "status",
                "render": (data) => {
                    let status = 'Unknown';
                    let badgeTheme;
                    switch(data){
                        case 'ACTIVE':
                            status = 'Active';
                            badgeTheme = 'bg-success-subtle text-success'
                            break;
                        case 'INACTIVE':
                            status = 'Inactive';
                            badgeTheme = 'bg-secondary-subtle text-secondary'
                            break;
                        case 'COMPLETED':
                            status = 'Completed';
                            badgeTheme = 'bg-primary-subtle text-primary'
                            break;
                        default:
                            badgeTheme = 'bg-secondary-subtle text-secondary'
                            break;
                    }
                    return `<span class="badge ${badgeTheme} rounded-pill px-3">${status}</span>`;
                }

            },
            { "data": "gender" },
            { "data": "birthDay" },
            {
                "data": "id",
                "className": "text-end",
                "render": (data) => `
                    <a href="/students/student/${data}" class="btn btn-outline-secondary btn-sm btn-action"><i class="bi bi-eye-fill"></i></a>
                    <a href="/students/edit/${data}" class="btn btn-outline-primary btn-sm btn-action"><i class="bi bi-pencil-fill"></i></a>`
            }
        ]
    });

    // Custom search bar
    $('#studentSearch').on('keyup', function() {
        table.search(this.value).draw();
    });
});