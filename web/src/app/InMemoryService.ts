import { InMemoryDbService, RequestInfo } from 'angular-in-memory-web-api';
import { Observable } from "rxjs";

const SAMPLE_SEARCH_RESULTS = [
    {
        id: "4VqPOruhp5EdPBeR92t6lQ",
        title: "Uprising",
        artist: "Muse",
        album: "The Resistance",
        artwork_url: "https://i.scdn.co/image/ab67616d00004851b6d4566db0d12894a1a3b7a2"
    }, {
        id: "0c4IEciLCDdXEhhKxj4ThA",
        title: "Madness",
        artist: "Muse",
        album: "The 2nd Law",
        artwork_url: "https://i.scdn.co/image/ab67616d00004851fc192c54d1823a04ffb6c8c9"
    }, {
        id: "3lPr8ghNDBLc2uZovNyLs9",
        title: "Supermassive Black Hole",
        artist: "Muse",
        album: "Black Holes and Revelations",
        artwork_url: "https://i.scdn.co/image/ab67616d0000485128933b808bfb4cbbd0385400"
    }, {
        id: "3skn2lauGk7Dx6bVIt5DVj",
        title: "Starlight",
        artist: "Muse",
        album: "Black Holes and Revelations",
        artwork_url: "https://i.scdn.co/image/ab67616d0000485128933b808bfb4cbbd0385400"
    }, {
        id: "7ouMYWpwJ422jRcDASZB7P",
        title: "Knights of Cydonia",
        artist: "Muse",
        album: "Black Holes and Revelations",
        artwork_url: "https://i.scdn.co/image/ab67616d0000485128933b808bfb4cbbd0385400"
    }, {
        id: "7xyYsOvq5Ec3P4fr6mM9fD",
        title: "Hysteria",
        artist: "Muse",
        album: "Absolution",
        artwork_url: "https://i.scdn.co/image/ab67616d000048518cb690f962092fd44bbe2bf4"
    }, {
        id: "383QXk8nb2YrARMUwDdjQS",
        title: "Psycho",
        artist: "Muse",
        album: "Drones",
        artwork_url: "https://i.scdn.co/image/ab67616d00004851808846f0223d97d5963c420d"
    }, {
        id: "3eSyMBd7ERw68NVB3jlRmW",
        title: "Pressure",
        artist: "Muse",
        album: "Simulation Theory (Super Deluxe)",
        artwork_url: "https://i.scdn.co/image/ab67616d000048514cb163c1d111f77307c842b6"
    }
];

export class InMemoryService implements InMemoryDbService {

    createDb(reqInfo?: RequestInfo): {} | Observable<{}> | Promise<{}> {
        return SAMPLE_SEARCH_RESULTS;
    }

}