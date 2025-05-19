const { Env } = await import(new URL(import.meta.url).searchParams.get("runtimeJsUrl"))
import "https://cdn.jsdelivr.net/npm/chart.js"

const canvas = Env.content.querySelector("#chart")

new Chart(canvas, {
    type: 'bar',
    data: {
        labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
        datasets: [{
            label: '# of Votes',
            data: [12, 19, 3, 5, 2, 3],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
})