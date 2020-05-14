import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexYAxis,
  ApexXAxis,
  ApexDataLabels,
  ApexGrid
} from "ng-apexcharts";
import {HttpEventType} from "@angular/common/http";
import {ProviderService} from "../service/provider.service";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
};

@Component({
  selector: 'app-my-bar-chart',
  templateUrl: './my-bar-chart.component.html',
  styleUrls: ['./my-bar-chart.component.css']
})
export class MyBarChartComponent implements OnInit {

  @ViewChild("chart") chart: MyBarChartComponent;
  public chartOptions: Partial<ChartOptions>;

  private k: number;
  donnees: any;
  coords_x: Array<number> = [0];
  coords_y: Array<number> = [0];
  coords_name: Array<string> = [""];
  max_x: number = 0;
  max_y: number = 0;

  ngOnInit(): void {

    this.k = 0;
    while (this.k < this.donnees.length) {
      let div1 = 1;
      let x1 = this.donnees[this.k].qualification.ca1;
      if (this.donnees[this.k].qualification.ca2 != 0) {
        x1 += this.donnees[this.k].qualification.ca2;
        ++div1;
      }
      if (this.donnees[this.k].qualification.ca3 != 0) {
        x1 += this.donnees[this.k].qualification.ca3;
        ++div1;
      }
      let div2 = 1;
      let y1 = this.donnees[this.k].qualification.ebe1;
      if (this.donnees[this.k].qualification.ebe2 != 0) {
        y1 += this.donnees[this.k].qualification.ebe2;
        ++div2;
      }
      if (this.donnees[this.k].qualification.ebe3 != 0) {
        y1 += this.donnees[this.k].qualification.ebe3;
        ++div2;
      }

      let x_valid = x1 / div1;
      let y_valid = y1 / div2;

      this.coords_x[this.k] = x_valid;
      if (this.max_x < x_valid) {
        this.max_x = x_valid;
      }
      this.coords_y[this.k] = y_valid;
      if (this.max_y < y_valid) {
        this.max_y = y_valid;
      }
      this.coords_name[this.k] = this.donnees[this.k].societe;

      this.k = this.k + 1;

    }

    let iter = 0;
    for (iter = 0; iter < this.donnees.length; ++iter) {
      let y_tem = this.coords_y[iter];
      let x_tem = this.coords_x[iter];
      this.coords_y[iter] = (y_tem * 100) / this.max_y;
      this.coords_x[iter] = Math.round((x_tem * 100) / this.max_x);
    }
    this.test();

  }
  constructor(public dialogRef: MatDialogRef<MyBarChartComponent>, private providerService:ProviderService) {}
  test() {
    this.k = 0;
    let str = [];
    while (this.k < this.donnees.length) {
      str[this.k] = {
        name: this.coords_name[this.k],
        data: [[this.coords_x[this.k], this.coords_y[this.k]]]
      }
      ++this.k;
    }
    this.chartOptions = {
      series: str,
      chart: {
        height: 350,
        type: 'scatter',
        zoom: {
          enabled: true,
          type: 'xy'
        }
      },
      xaxis: {
        title: {
          text: 'CA base 100'
        },
        tickAmount: 10,
        labels: {
          formatter: function(val) {
            return  parseInt(val).toFixed(1)
          }
        }
      },
      yaxis: {
        title: {
          text: '\nebe base 100'
        },
        tickAmount: 7,
        labels: {
          formatter: function(val) {
            return  parseInt(String(val)).toFixed(2)
          }
      }
    }
    };
  }

  onClose() {
    this.dialogRef.close();
  }

}
