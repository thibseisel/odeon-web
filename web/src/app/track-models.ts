import { AudioFeature } from "./remote-models";

export interface SearchResult {
    id: string;
    title: string;
    album: string;
    artist: string;
    artwork_url?: string;
}

export interface Track {
    /**
     * The identifier of that track on the server.
     */
    id: string;
    /**
     * The title of the track.
     */
    name: string;
    artist: string;
    album: string;
    /**
     * The number of the track.
     * If an album has several discs, the track number is the number on the specified disc.
     */
    trackNo: number;
    /**
     * The track length in milliseconds.
     */
    duration: number;
    /**
     * Popularity score, from 0 to 100.
     */
    popularity: number;
    artworkUrl?: string;

    features: AudioFeature;
}
