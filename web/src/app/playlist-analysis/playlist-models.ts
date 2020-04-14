import { Pitch, ImageSpec } from "../track-analysis/remote-models";

/**
 * Basic information on a playlist returned as a search result.
 */
export interface PlaylistResult {

    /**
     * The unique identifier of the playlist on the remote server.
     */
    readonly id: string;

    /**
     * The display name of the playlist, as given by its creator.
     */
    readonly name: string;

    /**
     * An optional short description of the content of the playlist.
     */
    readonly description?: string;

    /**
     * The total number of tracks that are part of the playlist.
     */
    readonly trackCount: number;

    /**
     * Images for the playlist.
     * The array may be empty or contain up to three images.
     * The images are returned by size in descending order.
     */
    readonly images: ReadonlyArray<ImageSpec>;
}