import { Component, OnInit } from '@angular/core';

import { Hero } from '../hero/hero';
import { HeroService } from '../hero/hero.service';

@Component({
  moduleId: module.id,
  selector: 'hero-dashboard',
  templateUrl: './hero-dashboard.component.html',
  styleUrls: ['./hero-dashboard.component.styl'],
  providers: [HeroService]
})
export class HeroDashboardComponent implements OnInit {

    heroes: Hero[] = [];

constructor(private heroService: HeroService) { }

  ngOnInit(): void {
    this.heroService.getHeroes().then(heroes => this.heroes = heroes.slice(1, 5));
  }

}
