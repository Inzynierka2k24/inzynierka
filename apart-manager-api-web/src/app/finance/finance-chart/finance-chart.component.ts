import {Component, OnInit} from '@angular/core';
import {Finance} from "../../../generated";
import {FormBuilder, FormGroup, FormsModule} from "@angular/forms";
import {FinanceService} from "../service/finance.service";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";

@Component({
  selector: 'app-finance-chart',
  templateUrl: './finance-chart.component.html',
  styleUrls: ['./finance-chart.component.scss']
})
export class FinanceChartComponent implements OnInit{
  data: any;
  finances: Finance[] = [];
  options: any;
  filterForm: FormGroup;
  monthlyRevenue: any;
  years = [
    { name: "2017" },
    { name: "2018" },
    { name: "2019" },
    { name: "2020" },
    { name: "2021" },
    { name: "2022" },
    { name: "2023" },
    { name: "2024" },
  ];

  constructor(private fb: FormBuilder)
  {
    this.filterForm = this.fb.group({
      filterYear: [null],
    });

    this.filterForm.setValue({
      filterYear: 2023,
    });

    // this.filterForm.get("filterYear")?.valueChanges.subscribe(x => {
    //   this.calculateRevenue();
    // });

  }
  ngOnInit() {
    this.selectedYear = this.filterForm.value.filterYear;
    this.finances = [
        {
          userId: 1,
          apartmentId: 1,
          eventId: 1,
          eventType: "RENOVATION",
          source: "CLEANING",
          price: 120,
          date: new Date(),
          details: "Cleaned carpet",
        },
        {
          userId: 1,
          apartmentId: 1,
          eventId: 1,
          eventType: "RENOVATION",
          source: "REPAIR",
          price: 150,
          date: new Date('2023-10-11T03:24:00'),
          details: "Promotions",
        },
        {
          userId: 3,
          apartmentId: 2,
          eventId: 1,
          eventType: "RESERVATION",
          source: "BOOKING",
          price: 1000.79,
          date: new Date('2023-11-11T03:24:00'),
          details: "Promotions",
        },
        {
          userId: 3,
          apartmentId: 2,
          eventId: 1,
          eventType: "RESERVATION",
          source: "BOOKING",
          price: 1000.79,
          date: new Date('2023-08-11T03:24:00'),
          details: "Promotions",
        },
    ];
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');
    this.data = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June',
          'July', "August", "September", "October", "November", "December"],
        datasets: [
            {
                label: 'Rooms Revenue',
                data: this.monthlyRevenue,
                fill: false,
                borderColor: documentStyle.getPropertyValue('--pink-500'),
                tension: 0.4
            }
        ]
    };

    this.options = {
      maintainAspectRatio: false,
      aspectRatio: 0.6,
      plugins: {
        legend: {
          labels: {
            color: textColor,
          },
        },
      },
      scales: {
        x: {
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
        y: {
          title: {
            display: true,
            text: 'Rooms Revenue',
            fontColor: '#757575',
            fontSize: 12,
          },
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
      },
    };
  }

  selectedYear: string;

  calculateRevenue(): void {
    this.selectedYear = this.filterForm.value.filterYear.name;
    this.monthlyRevenue = new Array(12).fill(0);

    for (var val of this.finances) {
      if (val.date.getFullYear() == parseInt(this.selectedYear)){
        this.monthlyRevenue[val.date.getMonth()] += val.price;
      }
    }

    this.data = {
      labels: ['January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'],
      datasets: [
        {
          label: 'Rooms Revenue',
          data: this.monthlyRevenue,
          fill: false,
          borderColor: getComputedStyle(document.documentElement).getPropertyValue('--pink-500'),
          tension: 0.4
        }
      ]
    };
  }
}
