import { Component, OnInit } from '@angular/core';
import { PokedexService } from '../../services/pokedex.service';
import { Pokemon } from 'src/app/models/Pokemon';
import {Router} from '@angular/router';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit {
  private TOTALPOKEMON: number = 150;
  
  private numPoke: number;
  private currentPage: number = 0;
  private numPages: number;
  private allPoke: [Pokemon];
  private pokePages: [Pokemon];

  constructor() { }

  ngOnInit() {
  }

  populatePokePages(): void{
    let count = 0;
    this.currentPage = 0;
    //display number of pokemon on page from radio button
    for (let i = (this.currentPage * this.numPoke + 1); i < (this.currentPage * this.numPoke + this.numPoke); i++){
      this.pokePages[count] = this.allPoke[i];
      count++;
    }
    this.numPages = Math.ceil(this.TOTALPOKEMON/this.numPoke);
  }

  //wrap around to first page if on last page
  nextPage(): void{
    if (this.currentPage == this.numPages){
      this.currentPage = 0;
    }
    else{
      this.currentPage++;
    }
  }

  //wrap around to last page if on first page
  prevPage(): void{
    if (this.currentPage == 0){
      this.currentPage = this.numPages;
    }
    else{
      this.currentPage--;
    }
  }

}
