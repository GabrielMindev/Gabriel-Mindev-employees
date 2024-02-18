document.addEventListener('DOMContentLoaded', function() {
    const uploadForm = document.getElementById('uploadForm');
    const resultTableContainer = document.getElementById('resultTableContainer');

    uploadForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const fileInput = document.getElementById('file');
        const file = fileInput.files[0];

        if (file) {
            const formData = new FormData();
            formData.append('file', file);

            fetch('http://localhost:8080/upload', {
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    displayResults(data);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    });

    function displayResults(results) {
        const table = document.createElement('table');
        const thead = document.createElement('thead');
        const tbody = document.createElement('tbody');

        // Add table header
        const headerRow = document.createElement('tr');
        ['Employee ID #1', 'Employee ID #2', 'Project ID', 'Days Worked'].forEach(headerText => {
            const th = document.createElement('th');
            th.textContent = headerText;
            headerRow.appendChild(th);
        });
        thead.appendChild(headerRow);
        table.appendChild(thead);

        // Add table body
        results.forEach(result => {
            const row = document.createElement('tr');
            ['employeeId1', 'employeeId2', 'projectId', 'daysWorked'].forEach(key => {
                const cell = document.createElement('td');
                cell.textContent = result[key];
                row.appendChild(cell);
            });
            tbody.appendChild(row);
        });
        table.appendChild(tbody);

        // Remove any previous results
        resultTableContainer.innerHTML = '';

        // Append the new table to the container
        resultTableContainer.appendChild(table);
    }
});
