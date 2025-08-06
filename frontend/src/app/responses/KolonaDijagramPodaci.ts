export interface ChartSeries {
  name: string; // mesec, npr. "Januar"
  value: number; // broj rezervacija
}

export interface ChartData {
  name: string; // naziv vikendice
  series: ChartSeries[]; // niz meseci sa brojevima rezervacija
}
