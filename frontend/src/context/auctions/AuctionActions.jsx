import { apiClient } from '../../services/apiClient';

/**
 * Loads a specific action using a backend-provided HATEOAS href.
 *
 * The response is normalized from the first embedded collection found in the HAL
 * response. Missing links and request failures are reported through reducer errors.
 */

export const GET_AUCTION_SUCCESS = 'GET_AUCTION_SUCCESS';
export const GET_AUCTION_ERROR = 'GET_AUCTION_ERROR';
export const PLACE_BID_SUCCESS = 'PLACE_BID_SUCCESS';
export const PLACE_BID_ERROR = 'PLACE_BID_ERROR';
export const CLEAR_AUCTION_MESSAGES = 'CLEAR_AUCTION_MESSAGES';

function extractErrorMessage(error) {
    const rawMessage = String(error?.message ?? error);
    try {
        const parsed = JSON.parse(rawMessage);
        return parsed.message || rawMessage;
    } catch {
        return rawMessage;
    }
}

export function getAuctionSuccess(payload) {
    return { type: GET_AUCTION_SUCCESS, payload };
}

export function getAuctionError(error) {
    return { type: GET_AUCTION_ERROR, payload: error };
}

export function placeBidSuccess(payload) {
    return { type: PLACE_BID_SUCCESS, payload };
}

export function placeBidError(error) {
    return { type: PLACE_BID_ERROR, payload: error };
}

export function clearAuctionMessages() {
    return { type: CLEAR_AUCTION_MESSAGES };
}

export async function getAuction(dispatch, auctionId) {
    try {
        const result = await apiClient.getAuctionById(auctionId);
        dispatch(getAuctionSuccess(result));
    } catch (e) {
        dispatch(getAuctionError(extractErrorMessage(e)));
    }
}

export async function placeBid(dispatch, href, body) {
    if (!href) {
        dispatch(placeBidError('Missing place-bid link.'));
        return false;
    }

    try {
        const result = await apiClient.postByHref(href, body);
        dispatch(placeBidSuccess(result));
        return true;
    } catch (e) {
        dispatch(placeBidError(extractErrorMessage(e)));
        return false;
    }

}