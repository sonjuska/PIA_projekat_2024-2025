package com.example.backend.modeli.responses;

public class PieData {
   private String name; 
   private int brojRezervacijaVikend;
   private int brojRezervacijaRadniDani;

   public PieData(){}
   public PieData(String name, int brojRezervacijaVikend, int brojRezervacijaRadniDani) {
    this.name = name;
    this.brojRezervacijaVikend = brojRezervacijaVikend;
    this.brojRezervacijaRadniDani = brojRezervacijaRadniDani;
   }
   public String getName() {
    return name;
   }
   public void setName(String name) {
    this.name = name;
   }
   public int getBrojRezervacijaVikend() {
    return brojRezervacijaVikend;
   }
   public void setBrojRezervacijaVikend(int brojRezervacijaVikend) {
    this.brojRezervacijaVikend = brojRezervacijaVikend;
   }
   public int getBrojRezervacijaRadniDani() {
    return brojRezervacijaRadniDani;
   }
   public void setBrojRezervacijaRadniDani(int brojRezervacijaRadniDani) {
    this.brojRezervacijaRadniDani = brojRezervacijaRadniDani;
   }

   

}

