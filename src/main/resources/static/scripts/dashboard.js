/**
 * Dashboard Analytics Script
 * Author: Professional Dashboard Module
 * Version: 1.0
 */

// ==================== CHART CONFIGURATION ====================

// Global Chart.js defaults
Chart.defaults.color = '#64748b';
Chart.defaults.font.family = "'Segoe UI', sans-serif";

// ==================== CHART BUILDERS ====================

function buildPerfomanceRadar(data){
    const performanceData = data['subjectPerformances'];

    //Default fall back values
    const labels = ['Math', 'Physics', 'Chemistry', 'Biology', 'English'];
    const dataSet1 = [0, 0, 0, 0, 0];
    const dataSet2 = [0, 0, 0, 0, 0];

    if(performanceData.length > 0) {
        dataSet1.length =0;
        dataSet2.length =0;
        for (entry of performanceData) {
            labels.push(entry[0]);
            dataSet1.push(entry[2]); //Term 2
            dataSet2.push(entry[1]); //Term 1
        }
    }

    const demoData = {
        labels: labels,
        datasets: [
            {
                label: 'Current Term',
                data: dataSet1,
                fill: true,
                backgroundColor: 'rgba(59, 130, 246, 0.2)',
                borderColor: '#3b82f6',
                borderWidth: 3,
                pointBackgroundColor: '#3b82f6',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: '#3b82f6'
            },
            {
                label: 'Previous Term',
                data: dataSet2,
                fill: true,
                backgroundColor: 'rgba(234, 179, 8, 0.2)',
                borderColor: '#eab308',
                borderWidth: 3,
                pointBackgroundColor: '#eab308'
            }
        ]
    };
    new Chart(document.getElementById("performanceRadar"), {
        type: 'radar',
        data: demoData,
        options: {
            responsive: true,
            maintainAspectRatio: false,   // Added - recommended for better sizing

            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        padding: 20,
                        font: { size: 13 }
                    }
                },
                tooltip: {
                    callbacks: {
                        label: (context) => {
                            return ` ${context.dataset.label}: ${context.raw}%`;
                        }
                    }
                },
                title: {
                    display: true,
                    font: { size: 18, weight: 'bold' },
                    text: 'Performance Summary',
                }
            },

            scales: {
                r: {        // 'r' is the radial axis in radar charts
                    beginAtZero: true,
                    max: 100,                    // percentage scale
                    ticks: {
                        stepSize: 20,
                        callback: (value) => value + '%'
                    },
                    grid: {
                        color: '#e2e8f0'
                    },
                    angleLines: {
                        color: '#e2e8f0'
                    },
                    pointLabels: {
                        font: {
                            size: 13,
                            weight: '500'
                        },
                        color: '#475569'
                    }
                }
            }
        }
    });
}

function buildTrendChart(data) {
    new Chart(document.getElementById('trendChart'), {
        type: 'line',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
            datasets: [{
                label: 'Avg Score',
                data: [65, 70, 68, 75, 80],
                borderColor: '#0a80c6',
                backgroundColor: 'rgba(19,143,214,0.2)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            layout: {
                padding: { left: 5, right: 5, top: 10, bottom: 0 }
            },
            plugins: { legend: { display: false } },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { color: '#f8fafc' },
                    ticks: { padding: 10 }
                },
                x: { grid: { display: false } }
            }
        }
    });
}

function buildGradesDistChart(data) {
    const gradeDistribution = data['performanceDistribution'];

    new Chart(document.getElementById('gradeChart'), {
        type: 'pie',
        data: {
            labels: ['Excellent', 'Good', 'Average', 'Bad', 'Poor'],
            datasets: [{
                data: [
                    gradeDistribution['excellent'],
                    gradeDistribution['good'],
                    gradeDistribution['average'],
                    gradeDistribution['bad'],
                    gradeDistribution['poor']
                ],
                backgroundColor: ['#14b414', '#117ec1', '#8ad4ff', '#ffae00', '#d64427'],
                borderWidth: 0
            }]
        },
        options: {
            plugins: {
                legend: { position: 'bottom' },
                tooltip: {
                    enabled: true,
                    callbacks: {
                        label: (context) => {
                            return ` ${context.dataset.label}: ${context.raw}%`;
                        }
                    }
                }
            }
        }
    });
}

function buildPerfomanceChart(data) {
    const dataEntries = data['averagesPerGrade'].sort((a, b) => a[0] - b[0]);
    const labels = [];
    const values = [];

    if (dataEntries.length >= 1) {
        for (const entry of dataEntries) {
            labels.push(entry[0]);
            values.push(entry[1]);
        }

        new Chart(document.getElementById('barChart'), {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Class Average',
                    data: values,
                    backgroundColor: '#3b82f6',
                    borderRadius: 4,
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: (context) => `Average: ${context.parsed.y}%`
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        min: 0,
                        max: 100,
                        title: {
                            display: true,
                            text: 'Average Percentage (%)',
                            font: { size: 14, weight: 'bold' },
                            color: '#4b5563'
                        },
                        ticks: {
                            stepSize: 20,
                            callback: (value) => value + '%'
                        }
                    },
                    x: {
                        grid: { display: false },
                        title: {
                            display: true,
                            text: 'School Grade Level',
                            font: { size: 14, weight: 'bold' },
                            color: '#4b5563'
                        },
                        ticks: {
                            callback: function (value) {
                                return 'Grade ' + this.getLabelForValue(value);
                            }
                        }
                    }
                }
            }
        });
    }
}

// ==================== DATA INSERTION FUNCTIONS ====================

function insertActivities(data) {
    const activity_data = data['activityLogs'];
    const activityListEl = document.querySelector('#activity_list');

    activity_data.forEach((activity) => {
        let icon = {
            class: "bi bi-clipboard-check",
            style: "color: #10b981;",
        };
        if(activity['action'].includes("updated a student") || activity['action'].includes("updated an instructor")){
            icon.class = "bi bi-person-check";
        } else if(activity['action'].includes("updated an assessment")){
            icon.class = "bi bi-reception-3";
            icon.style = "color: #4338ca;";
        } else if(activity['action'].includes("updated a class")){
            icon.class = "bi bi-book";
            icon.style = "color: #4287f5;";
        } else if(activity['action'].includes("updated a subject")){
            icon.class = "bi bi-flask-fill";
            icon.style = "color: #4287f5;";
        } else if(activity['action'].includes("deleted")){
            icon.class = "bi bi-trash";
            icon.style = "color: red";
        }
        const activity_html = `
            <li>
                <div class="activity-icon">
                    <i class="${icon.class}" style="${icon.style}"></i>
                </div>
                <div>
                    <p><strong>${activity['entityType']}</strong></p>
                    <small>${activity['performedBy']} ${activity['action']}</small>
                </div>
            </li>
        `;
        activityListEl.insertAdjacentHTML('beforeend', activity_html);
    });
}

function insertTopFive(data) {
    const topFive = data['topFiveStudents'];
    const topFiveTableData = document.querySelector('#top_5_table_data');
    let achievementStatus = 'pass';

    topFive.forEach((result) => {
        if (result['score'] > 90) achievementStatus = 'distinction';
        else if (result['score'] > 50) achievementStatus = 'pass';
        else if (result['score'] > 40) achievementStatus = 'poor';
        else achievementStatus = 'fail';

        const html_row = `
            <tr>
                <td style="padding-left: 24px;">
                    <div class="d-flex align-items-center">
                        <div class="initials avatar-sm me-3">
                            ${(result['student']['firstName'].charAt(0) + result['student']['lastName'].charAt(0)).toUpperCase()}
                        </div>
                        <span>${result['student']['firstName']} ${result['student']['lastName']}</span>
                    </div>
                </td>
                <td>${result['assessment']['subject']['name']}</td>
                <td><strong>${result['score']}%</strong></td>
                <td style="padding-right: 24px;">
                    <span class="status-labels ${achievementStatus}">
                        ${achievementStatus}
                    </span>
                </td>
            </tr>
        `;

        topFiveTableData.insertAdjacentHTML('beforeend', html_row);
    });
}

// ==================== KPI ANIMATION ====================

function animateKPIs(stats) {
    animateValue(document.getElementById('studentsCount'), 0, stats.studentsCount, 1000);
    animateValue(document.getElementById('averageGrade'), 0, stats.averageGrade, 1200, '%');
    animateValue(document.getElementById('passRate'), 0, stats.passRate, 1200, '%');
}

function animateValue(el, start, end, duration, suffix = '') {
    let startTimestamp = null;

    function step(timestamp) {
        if (!startTimestamp) startTimestamp = timestamp;
        const progress = Math.min((timestamp - startTimestamp) / duration, 1);

        const value = Math.floor(progress * (end - start) + start);
        el.textContent = value + suffix;

        if (progress < 1) {
            window.requestAnimationFrame(step);
        }
    }

    window.requestAnimationFrame(step);
}

// ==================== MAIN DATA FETCH ====================

const baseUrl = window.location.origin;

fetch(`${baseUrl}/dashboard/data`)
    .then(response => {
        if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
        return response.json();
    })
    .then(dashboardData => {
        if (dashboardData && Object.keys(dashboardData).length > 0) {
            console.log('Dashboard data loaded successfully:', dashboardData);

            animateKPIs(dashboardData.stats);
            buildPerfomanceRadar (dashboardData);
            buildTrendChart(dashboardData);
            buildGradesDistChart(dashboardData);
            buildPerfomanceChart(dashboardData);
            insertActivities(dashboardData);
            insertTopFive(dashboardData);
        }
    })
    .catch(error => {
        console.error("Dashboard fetch failed:", error);
        alert('Failed to load dashboard data. Please try again later.');
    });