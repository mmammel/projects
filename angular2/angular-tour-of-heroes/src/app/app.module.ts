import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule }    from '@angular/http';
import { FormsModule }   from '@angular/forms'; // <-- NgModel lives here

import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService }  from './in-memory-data.service';

import { AppComponent } from './app.component';
import { HeroesComponent } from './heroes.component';
import { HeroDetailComponent } from './hero-detail.component';
import { HeroSearchComponent } from './hero-search.component';
import { DashboardComponent } from './dashboard.component';
import { HeroService } from './hero.service';
import { HeroSearchService } from './hero-search.service';

import { AppRoutingModule }     from './app-routing.module';

@NgModule({
  declarations: [
    AppComponent,
    HeroesComponent,
    DashboardComponent,
    HeroSearchComponent,
    HeroDetailComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    InMemoryWebApiModule.forRoot(InMemoryDataService),
    AppRoutingModule
  ],
  providers: [
    HeroService,
    HeroSearchService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
