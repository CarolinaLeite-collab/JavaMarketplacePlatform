import { beforeEach, describe, expect, it, vi } from 'vitest';
import { apiClient } from '../services/apiClient';
import {
    GET_AUCTION_SUCCESS,
    GET_AUCTION_ERROR,
    PLACE_BID_SUCCESS,
    PLACE_BID_ERROR,
    CLEAR_AUCTION_MESSAGES,
    getAuctionSuccess,
    getAuctionError,
    placeBidSuccess,
    placeBidError,
    clearAuctionMessages,
    getAuction,
    placeBid,
} from '../context/auctions/AuctionActions.jsx';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getAuctionById: vi.fn(),
        postByHref: vi.fn(),
    },
}));

describe('AuctionActions', () => {
    const dispatch = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();
    });

    describe('action creators', () => {
        it('getAuctionSuccess returns correct action', () => {
            const payload = { title: '1984' };

            expect(getAuctionSuccess(payload)).toEqual({
                type: GET_AUCTION_SUCCESS,
                payload,
            });
        });

        it('getAuctionError returns correct action', () => {
            const error = 'Auction not found';

            expect(getAuctionError(error)).toEqual({
                type: GET_AUCTION_ERROR,
                payload: error,
            });
        });

        it('placeBidSuccess returns correct action', () => {
            const payload = { bidId: 'bid-1' };

            expect(placeBidSuccess(payload)).toEqual({
                type: PLACE_BID_SUCCESS,
                payload,
            });
        });

        it('placeBidError returns correct action', () => {
            const error = 'Bid too low';

            expect(placeBidError(error)).toEqual({
                type: PLACE_BID_ERROR,
                payload: error,
            });
        });

        it('clearAuctionMessages returns correct action', () => {
            expect(clearAuctionMessages()).toEqual({
                type: CLEAR_AUCTION_MESSAGES,
            });
        });
    });

    describe('getAuction', () => {
        it('dispatches success action when api call succeeds', async () => {
            const result = { title: '1984', startingPrice: 50 };

            apiClient.getAuctionById.mockResolvedValue(result);

            await getAuction(dispatch, 'auction-123');

            expect(apiClient.getAuctionById).toHaveBeenCalledWith('auction-123');
            expect(dispatch).toHaveBeenCalledWith({
                type: GET_AUCTION_SUCCESS,
                payload: result,
            });
        });

        it('dispatches error action when api call fails', async () => {
            apiClient.getAuctionById.mockRejectedValue(new Error('Not found'));

            await getAuction(dispatch, 'auction-123');

            expect(dispatch).toHaveBeenCalledWith({
                type: GET_AUCTION_ERROR,
                payload: 'Not found',
            });
        });

        it('dispatches parsed error message when api call fails with JSON error', async () => {
            apiClient.getAuctionById.mockRejectedValue(
                new Error(JSON.stringify({ message: 'Auction fetch failed' }))
            );

            await getAuction(dispatch, 'auction-123');

            expect(dispatch).toHaveBeenCalledWith({
                type: GET_AUCTION_ERROR,
                payload: 'Auction fetch failed',
            });
        });
    });

    describe('placeBid', () => {
        it('dispatches error action and returns false when href is missing', async () => {
            const body = { bidValue: 100, currency: 'EUR' };

            const success = await placeBid(dispatch, null, body);

            expect(apiClient.postByHref).not.toHaveBeenCalled();
            expect(dispatch).toHaveBeenCalledWith({
                type: PLACE_BID_ERROR,
                payload: 'Missing place-bid link.',
            });
            expect(success).toBe(false);
        });

        it('dispatches success action and returns true when api call succeeds', async () => {
            const href = 'http://localhost:8081/auctions/abc/bids';
            const body = { bidValue: 100, currency: 'EUR' };
            const result = { bidId: 'bid-123' };

            apiClient.postByHref.mockResolvedValue(result);

            const success = await placeBid(dispatch, href, body);

            expect(apiClient.postByHref).toHaveBeenCalledWith(href, body);
            expect(dispatch).toHaveBeenCalledWith({
                type: PLACE_BID_SUCCESS,
                payload: result,
            });
            expect(success).toBe(true);
        });

        it('dispatches error action and returns false when api call fails', async () => {
            const href = 'http://localhost:8081/auctions/abc/bids';
            const body = { bidValue: 100, currency: 'EUR' };

            apiClient.postByHref.mockRejectedValue(new Error('Bid failed'));

            const success = await placeBid(dispatch, href, body);

            expect(dispatch).toHaveBeenCalledWith({
                type: PLACE_BID_ERROR,
                payload: 'Bid failed',
            });
            expect(success).toBe(false);
        });

        it('dispatches parsed error when api call fails with JSON error', async () => {
            const href = 'http://localhost:8081/auctions/abc/bids';
            const body = { bidValue: 100, currency: 'EUR' };

            apiClient.postByHref.mockRejectedValue(
                new Error(JSON.stringify({ message: 'Bid value too low' }))
            );

            const success = await placeBid(dispatch, href, body);

            expect(dispatch).toHaveBeenCalledWith({
                type: PLACE_BID_ERROR,
                payload: 'Bid value too low',
            });
            expect(success).toBe(false);
        });
    });
});