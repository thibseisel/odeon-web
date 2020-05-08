import { AudioFeature } from "@shared/remote-models"

export interface SearchResult {
    readonly id: string
    readonly title: string
    readonly album: string
    readonly artist: string
    readonly artwork_url?: string
}

export interface Track {
    /**
     * The identifier of that track on the server.
     */
    readonly id: string
    /**
     * The title of the track.
     */
    readonly name: string
    readonly artist: string
    readonly album: string
    /**
     * The number of the track.
     * If an album has several discs, the track number is the number on the specified disc.
     */
    readonly trackNo: number
    /**
     * The track length in milliseconds.
     */
    readonly duration: number
    /**
     * Popularity score, from 0 to 100.
     */
    readonly popularity: number
    readonly artworkUrl?: string

    readonly features: AudioFeature
}
