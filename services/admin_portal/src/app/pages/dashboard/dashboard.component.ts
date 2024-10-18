import { Component, OnInit } from "@angular/core";
import { Chart, registerables } from 'chart.js';
import { CommonModule } from '@angular/common';

Chart.register(...registerables);
@Component({
  selector: "app-dashboard",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "dashboard.component.html",
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  public canvas: any;
  public ctx: any;
  public datasets: any;
  public data: any;
  public myChartData: any;

  ngOnInit() {

    var gradientChartOptionsConfigurationWithTooltipRed: any = {
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true
        },
        tooltip: {
          backgroundColor: '#000000',
          titleColor: '#FFFFF0', // `titleFontColor` is now `titleColor`
          bodyColor: '#FFFFF0', // `bodyFontColor` is now `bodyColor`
          bodySpacing: 8,
          padding: 8, // `xPadding` is replaced with `padding` which applies to all sides
          mode: "nearest",
          intersect: 10,
          position: "nearest"
        }
      },
      responsive: true,
      scales: {
        y: {
          grid: {
            drawBorder: true, // Ensure the border of the grid is drawn
            color: '#6f6f6f', // This sets the color of the grid lines
            drawOnChartArea: true, // This ensures that the grid lines are drawn on the chart area
            zeroLineColor: "transparent", // Customize the zero line color if needed
          },
          ticks: {
            suggestedMin: 0,
            suggestedMax: 805,
            padding: 10,
            color: "#FFFFF0",
          }
        },
        x: {
          grid: {
            drawBorder: false,
            color: 'rgba(255, 255, 240, 0.8)', // Adjust as needed for the x-axis grid lines
            drawOnChartArea: false,
            zeroLineColor: "transparent",
          },
          ticks: {
            padding: 20,
            color: "rgb(255, 255, 240)",
          }
        }
      }
    };

    var gradientChartOptionsConfigurationWithTooltipGreen: any = {
      maintainAspectRatio: false,
      legend: {
        display: false
      },

      tooltips: {
        backgroundColor: '#f5f5f5',
        titleFontColor: '#FFFFF0',
        bodyFontColor: '#FFFFF0',
        bodySpacing: 4,
        xPadding: 12,
        mode: "nearest",
        intersect: 0,
        position: "nearest"
      },
      responsive: true,
      scales: {
        y: {
          grid: {
            drawBorder: true, // Ensure the border of the grid is drawn
            color: '#6f6f6f', // This sets the color of the grid lines
            drawOnChartArea: true, // This ensures that the grid lines are drawn on the chart area
            zeroLineColor: "transparent", // Customize the zero line color if needed
          },
          ticks: {
            suggestedMin: 0,
            suggestedMax: 805,
            padding: 10,
            color: "#FFFFF0",
          }
        },
        x: {
          grid: {
            drawBorder: false,
            color: 'rgba(255, 255, 240, 0.8)', // Adjust as needed for the x-axis grid lines
            drawOnChartArea: false,
            zeroLineColor: "transparent",
          },
          ticks: {
            padding: 20,
            color: "rgb(255, 255, 240)",
          }
        }
      }
    };

    this.canvas = document.getElementById("chartLineRed");
    this.ctx = this.canvas.getContext("2d");

    var gradientStroke = this.ctx.createLinearGradient(0, 230, 0, 50);

    gradientStroke.addColorStop(1, 'rgba(236, 166, 0, 0.4)');
    gradientStroke.addColorStop(0.4, 'rgba(236, 166, 0, 0.2)');
    gradientStroke.addColorStop(0, 'rgba(236, 166, 0, 2)');

    var data = {
      labels: ['JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC', 'JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN'],
      datasets: [{
        label: "Checking Accounts",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: '#fe7a00',
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: '#fe7a00',
        pointBorderColor: 'rgba(236, 166, 0, 0.2)',
        pointHoverBackgroundColor: '#fe7a00',
        pointBorderWidth: 6,
        pointHoverRadius: 8,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: [203215, 263215, 303215, 503215, 403215, 403215, 303235, 480715, 503346, 653645, 713268, 763953],
      },
      {
        label: "Saving Accounts",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: '#ffc000',
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: '#ffc000',
        pointBorderColor: 'rgba(236, 166, 0, 0.2)',
        pointHoverBackgroundColor: '#ffc000',
        pointBorderWidth: 6,
        pointHoverRadius: 8,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: [100321, 206315, 300321, 400325, 400325, 500325, 400335, 480075, 500336, 555345, 661368, 706353],
      }]
    };

    var myChart = new Chart(this.ctx, {
      type: 'line',
      data: data,
      options: gradientChartOptionsConfigurationWithTooltipRed
    });


    this.canvas = document.getElementById("chartLineGreen");
    this.ctx = this.canvas.getContext("2d");


    var gradientStroke = this.ctx.createLinearGradient(0, 230, 0, 50);
    gradientStroke.addColorStop(1, 'rgba(254, 241, 103, 0.3)');
    gradientStroke.addColorStop(0.4, 'rgba(254, 241, 103, 0.4)');
    gradientStroke.addColorStop(0, 'rgba(254, 241, 103, 0.2)');

    var data = {
      labels: ['JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC', 'JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN'],
      datasets: [{
        label: "System Alerts",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: '#fe7a00',
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: '#fe7a00',
        pointBorderColor: 'rgba(236, 166, 0, 0.2)',
        pointHoverBackgroundColor: '#fe7a00',
        pointBorderWidth: 6,
        pointHoverRadius: 8,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: [6655, 2356, 4565, 5688, 2256, 5655, 4856, 5725, 4348, 5542, 6454, 4364],
      },
      {
        label: "Customer Support Cases",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: '#ffc000',
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: '#ffc000',
        pointBorderColor: 'rgba(236, 166, 0, 0.2)',
        pointHoverBackgroundColor: '#ffc000',
        pointBorderWidth: 6,
        pointHoverRadius: 8,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: [5000, 2000, 4000, 3500, 2000, 6000, 3500, 4000, 3550, 3500, 3000, 1000],
      }]
    };


    var myChart = new Chart(this.ctx, {
      type: 'line',
      data: data,
      options: gradientChartOptionsConfigurationWithTooltipGreen

    });

    var data1 = {
      labels: ['JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC', 'JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN'],
      datasets: [{
        label: "Total Users",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: '#fef167',
        borderWidth: 2,
        borderDashOffset: 0.0,
        pointBackgroundColor: '#fef167',
        pointBorderColor: 'rgba(236, 166, 0, 0.2)',
        pointHoverBackgroundColor: '#fef167',
        pointBorderWidth: 6,
        pointHoverRadius: 8,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: [50321, 60321, 70321, 70321, 60321, 66321, 70321, 80321, 80321, 90821, 90321, 100321],
      }]
    };


    this.canvas = document.getElementById("chartBig1");
    this.ctx = this.canvas.getContext("2d");

    var gradientStroke = this.ctx.createLinearGradient(0, 230, 0, 50);
    gradientStroke.addColorStop(1, 'rgba(254, 241, 103, 0.8)');
    gradientStroke.addColorStop(0.4, 'rgba(254, 241, 103, 0.5)');
    gradientStroke.addColorStop(0, 'rgba(254, 241, 103, 0.7)');

    this.myChartData = new Chart(this.ctx, {
      type: 'bar', // or 'bar', 'pie', etc.
      data: data1, // your data object
      options: gradientChartOptionsConfigurationWithTooltipRed // your options object
    });

  }
  public updateOptions() {
    this.myChartData.data.datasets[0].data = this.data;
    this.myChartData.update();
  }
}
