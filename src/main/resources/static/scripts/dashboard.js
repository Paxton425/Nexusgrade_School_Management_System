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

    console.log("performance data", performanceData);

    //Default fall back values
    const labels = ['Math', 'Physics', 'Chemistry', 'Biology', 'English'];
    const dataSet1 = [0, 0, 0, 0, 0]; //Previous
    const dataSet2 = [0, 0, 0, 0, 0]; //Current

    const terms = Object.keys(performanceData);
    const termA = performanceData[terms[0]]; //Previous
    const termB = performanceData[terms[1]]; //Current

    // Get subject codes from both terms
    const subjectsA = Object.keys(termA);
    const subjectsB = Object.keys(termB);

    // Compare subjects
    const hasSameSubjects = subjectsA.length === subjectsB.length && subjectsA.every(subject => subjectsB.includes(subject));

    if (hasSameSubjects) {
        dataSet1.length = 0;
        dataSet2.length = 0;
        labels.length = 0;

        const subjectKeys = Object.keys(termA); //Since objects match, we use same keys

        subjectKeys.forEach(key => {
            const subjectData = termA[key];
            labels.push(key);
            dataSet1.push(subjectData.performance);
            dataSet2.push(termB[key]?.performance ?? 0);
        });
    } else {
        console.error("invalid or term objects missmatch!");
        throw new Error("invalid or term objects missmatch!");
    }

    const chartData = {
        labels: labels,
        datasets: [
            {
                label: 'Current Term ('+Object.keys(performanceData)[1].replace("_", " ")+')',
                data: dataSet2,
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
                label: 'Previous Term ('+Object.keys(performanceData)[0].replace("_", " ")+')',
                data: dataSet1,
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
        data: chartData,
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
                backgroundColor: ['#168f16', '#117ec1', '#8ad4ff', '#ffae00', '#d64427'],
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
                            return ` ${context.raw}%`;
                        }
                    }
                }
            }
        }
    });
}

function buildGradePerfomanceChart(data) {
    const dataEntries = data['termAveragesPerGrade'];
    const keys = Object.keys(dataEntries);
    const dataObj = {
        labels : dataEntries[keys[0]],
        previousTerm: dataEntries[keys[1]],
        currentTerm: dataEntries[keys[2]]
    }
    console.log("dataObj", dataObj)

    if (dataObj.labels.length >= 1 && dataObj.previousTerm.length >=1) {
        if(dataObj.labels.length !== dataObj.previousTerm.length)
            throw Error("Labels and values have unequal entries")

        new Chart(document.getElementById('barChart'), {
            type: 'bar',
            data: {
                labels: dataObj.labels,
                datasets: [{
                    label: 'Previous Term',
                    data: dataObj.previousTerm,
                    backgroundColor: '#3b82f6',
                    borderRadius: 4,
                },
                {
                    label: 'Current Term',
                    data: dataObj.currentTerm,
                    backgroundColor: '#0f3c63',
                    borderRadius: 4,
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: true },
                    tooltip: {
                        callbacks: {
                            title: (context) => `Grade ${context[0].label}`,
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
        //console.log("Performed By ", activity['performedBy']);
        let performer = "";
        if(activity['performedBy'].firstName != null) performer += (activity['performedBy'].firstName).charAt(0);
        if(activity['performedBy'].middleName != null) performer += (activity['performedBy'].middleName).charAt(0);
        if(activity['performedBy'].lastName != null) performer = (performer).toUpperCase() +" "+ (activity['performedBy'].lastName);

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
                    <small>${performer} ${activity['action']}</small>
                </div>
            </li>
        `;
        activityListEl.insertAdjacentHTML('beforeend', activity_html);
    });
}

function insertTopFive(data) {
    const topFive = data['topFiveStudents'];
    console.log("Top five", topFive);
    const topFiveTableData = document.querySelector('#top_5_table_data');
    let achievementStatus = 'pass';

    topFive.forEach((result) => {
        if (result['mark'] > 90) achievementStatus = 'distinction';
        else if (result['mark'] > 50) achievementStatus = 'pass';
        else if (result['mark'] > 40) achievementStatus = 'poor';
        else achievementStatus = 'fail';
        const student = result['scoreDTO']['student'];
        const hexColor = student['color'].replace(/.*(#([0-9a-fA-F]{3,6})).*/, "$1");

        const html_row = `
            <tr>
                <td style="padding-left: 24px;">
                    <div class="d-flex align-items-center">
                        <div class="initials avatar-sm me-3" style="background-color: ${hexColor}">
                            ${(student['firstName'].charAt(0) + student['lastName'].charAt(0)).toUpperCase()}
                        </div>
                        <span>${student['firstName']} ${student['lastName']}</span>
                    </div>
                </td>
                <td>${result['scoreDTO']['assessment']['subject']['name']}</td>
                <td><strong>${result['mark']}%</strong></td>
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
    const currTermStats = stats['currTermStats'];
    const prevTermStats = stats['prevTermSats'];
    animateValue(document.getElementById('studentsCount'), 0, currTermStats['studentCount'], 1000);
    animateValue(document.getElementById('averageGrade'), 0, currTermStats['overallAverage'], 1200, '%');
    animateValue(document.getElementById('passRate'), 0, currTermStats['passRate'], 1200, '%');
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
            buildGradePerfomanceChart(dashboardData);
            insertActivities(dashboardData);
            insertTopFive(dashboardData);
        }
    })
    .catch(error => {
        console.error("Dashboard fetch failed:", error);
        alert('Failed to load dashboard data. Please try again later.');
    });