import {Component, OnInit} from '@angular/core';
import {Apartment, Finance, UserDTO} from "../../../generated";
import {FormBuilder, FormGroup, FormsModule} from "@angular/forms";
import {FinanceService} from "../service/finance.service";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {Store} from "@ngrx/store";
import {AppState} from "../../core/store/app.store";

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
    { name: "2023" },
    { name: "2022" },
    { name: "2021" },
    { name: "2020" },
    { name: "2019" },
    { name: "2018" },
    { name: "2017" },
  ];
  isUserLoggedIn = false;
  user: UserDTO;
  selectedYear: string;

  constructor(private store: Store<AppState>,
              private financeService: FinanceService,
              private fb: FormBuilder)
  {
    this.filterForm = this.fb.group({
      filterYear: [null],
    });
  }
  ngOnInit() {

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
    this.filterForm.setValue({
      filterYear: 2023,
    });
    this.selectedYear = this.filterForm.value.filterYear;

    const userDataSub = this.store
      .select(selectCurrentUser)
      .subscribe((user) => {
        if (user) {
          this.isUserLoggedIn = true;
          this.user = user;
          this.financeService.getFinances(this.user).subscribe((data: Finance[]) => {
            this.finances = data;
            this.calculateRevenue();
          });

        } else {
          this.isUserLoggedIn = false;
        }
      });
  }

  calculateRevenue(): void {
    this.selectedYear = this.filterForm.value.filterYear;
    this.monthlyRevenue = new Array(12).fill(0);

    for (var val of this.finances) {
      if (new Date(val.date).getFullYear() == parseInt(this.selectedYear)){
        this.monthlyRevenue[new Date(val.date).getMonth()] += val.price;
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
