import { describe, expect, it } from 'vitest';
import { initialAuctionState, auctionReducer } from '../context/auctions/AuctionReducer';
import {
    GET_AUCTION_SUCCESS,
    GET_AUCTION_ERROR,
    PLACE_BID_SUCCESS,
    PLACE_BID_ERROR,
    CLEAR_AUCTION_MESSAGES,
} from '../context/auctions/AuctionActions.jsx';

describe('auctionReducer', () => {
    it('returns the current state for an unknown action', () => {
        const state = {
            ...initialAuctionState,
            error: 'Something went wrong',
        };

        const result = auctionReducer(state, { type: 'UNKNOWN_ACTION' });

        expect(result).toBe(state);
    });

    it('sets auction and extracts placeBidHref on GET_AUCTION_SUCCESS', () => {
        const auction = {
            title: '1984',
            startingPrice: 50,
            _links: {
                'place-bid': { href: 'http://localhost:8081/auctions/abc/bids' },
            },
        };

        const result = auctionReducer(initialAuctionState, {
            type: GET_AUCTION_SUCCESS,
            payload: auction,
        });

        expect(result.auction).toEqual(auction);
        expect(result.placeBidHref).toBe('http://localhost:8081/auctions/abc/bids');
        expect(result.error).toBeNull();
    });

    it('sets placeBidHref to null when place-bid link is missing', () => {
        const auction = {
            title: '1984',
            _links: {},
        };

        const result = auctionReducer(initialAuctionState, {
            type: GET_AUCTION_SUCCESS,
            payload: auction,
        });

        expect(result.auction).toEqual(auction);
        expect(result.placeBidHref).toBeNull();
    });

    it('sets placeBidHref to null when _links is missing', () => {
        const auction = { title: '1984' };

        const result = auctionReducer(initialAuctionState, {
            type: GET_AUCTION_SUCCESS,
            payload: auction,
        });

        expect(result.placeBidHref).toBeNull();
    });

    it('sets error on GET_AUCTION_ERROR', () => {
        const result = auctionReducer(initialAuctionState, {
            type: GET_AUCTION_ERROR,
            payload: 'Auction not found',
        });

        expect(result.error).toBe('Auction not found');
    });

    it('sets success message and clears error on PLACE_BID_SUCCESS', () => {
        const state = {
            ...initialAuctionState,
            error: 'Previous error',
        };

        const result = auctionReducer(state, {
            type: PLACE_BID_SUCCESS,
        });

        expect(result.error).toBeNull();
        expect(result.successMessage).toBe('Bid placed successfully!');
    });

    it('sets error on PLACE_BID_ERROR', () => {
        const result = auctionReducer(initialAuctionState, {
            type: PLACE_BID_ERROR,
            payload: 'Bid too low',
        });

        expect(result.error).toBe('Bid too low');
    });

    it('clears error and successMessage on CLEAR_AUCTION_MESSAGES', () => {
        const state = {
            ...initialAuctionState,
            error: 'Some error',
            successMessage: 'Bid placed!',
        };

        const result = auctionReducer(state, {
            type: CLEAR_AUCTION_MESSAGES,
        });

        expect(result.error).toBeNull();
        expect(result.successMessage).toBeNull();
    });
});