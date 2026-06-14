import {
    GET_AUCTION_SUCCESS,
    GET_AUCTION_ERROR,
    PLACE_BID_SUCCESS,
    PLACE_BID_ERROR,
    CLEAR_AUCTION_MESSAGES,
} from './AuctionActions.jsx';

export const initialAuctionState = {
    auction: null,
    placeBidHref: null,
    error: null,
    successMessage: null,
};

export function auctionReducer(state, action) {
    switch (action.type) {
        case GET_AUCTION_SUCCESS: {
            const auction = action.payload;
            const links = auction?._links ?? {};
            const placeBidHref = links['place-bid']?.href ?? null;

            return {
                ...state,
                auction,
                placeBidHref,
                error: null,
            };
        }

        case GET_AUCTION_ERROR:
            return { ...state, error: action.payload };

        case PLACE_BID_SUCCESS:
            return {
                ...state,
                error: null,
                successMessage: 'Bid placed successfully!',
            };

        case PLACE_BID_ERROR:
            return { ...state, error: action.payload };

        case CLEAR_AUCTION_MESSAGES:
            return { ...state, error: null, successMessage: null };

        default:
            return state;
    }
}