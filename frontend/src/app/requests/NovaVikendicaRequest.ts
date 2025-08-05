import { Vikendica } from "../models/vikendica";

export interface NovaVikendicaRequest {
  vikendica: Vikendica;
  cenovnik: { sezona: string, cena: number }[];
  slike: string[];
}
